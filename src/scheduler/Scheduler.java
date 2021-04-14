package scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import common.Constants;
import common.IBufferInput;
import common.IBufferOutput;
import elevator.Direction;
import elevator.ElevatorCommand;
import elevator.ElevatorDoorCommand;
import elevator.ElevatorMoveCommand;
import elevator.Fault;
import elevator.ElevatorEvent;
import floor.InputData;

public class Scheduler implements Runnable {
	// State specific variables
	private SchedulerState state;
	private InputData inputData;
	private ElevatorEvent elevatorEvent;
	private SchedulerElevator elevatorToCommand;
	
	/**
	 * The list of active elevators.
	 */
	private ArrayList<SchedulerElevator> elevators = new ArrayList<SchedulerElevator>();
	
	/**
	 * List of input requests that could not be handled by any elevators on arrival.
	 * These should not be attempted until an active elevator runs out of jobs.
	 */
	private ArrayList<InputData> blockedInput = new ArrayList<InputData>();
	
	/**
	 * List of input requests that were orphaned by its assigned elevator permanently failing.
	 * These should be attempted as soon as possible, they may be able to be assigned to active elevators.
	 */
	private ArrayList<InputData> reprocessList = new ArrayList<InputData>();

	private int numFloors;
	private IBufferOutput<SchedulerMessage> outputBuffer;
	private IBufferInput<ElevatorCommand> inputBuffer;
	
	
	private DatagramSocket sendSocket;
	
	/**
	 * Creates a new instance of the Scheduler class.
	 * @param numElevators The number of elevators to connect to.
	 * @param floorReceiverBuffer Buffer output to receive ScheduleEvents.
	 * @param elevatorCommandBuffer Buffer input to send ElevatorCommands.
	 */
	public Scheduler(
			int numElevators,
			int numFloors,
			IBufferOutput<SchedulerMessage> outputBuffer,
			IBufferInput<ElevatorCommand> inputBuffer) {
		this.state = SchedulerState.INITIAL;
		
		this.numFloors = numFloors;
		this.outputBuffer = outputBuffer;
		this.inputBuffer = inputBuffer;
		
		// Create default elevators.
		for (int i = 0; i < numElevators; i++) {
			elevators.add(new SchedulerElevator(i, 1, Direction.WAITING));
		}
		
		try {
			this.sendSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Gets the current state.
	 * @return The current state.
	 */
	public SchedulerState getState() {
		return this.state;
	}
	
	@Override
	public void run() {	
		while(true) {
			// If tick returns true, stop state machine.
			if (tick()) {
				sendSocket.close();
				break;
			}
		}
	}
	
	/**
	 * Handles all buffer processing and scheduling.
	 * Moving to its own function makes unit-testing easier.
	 * @return Boolean indicating whether to stop the state machine.
	 */
	public boolean tick() {
		switch (this.state) {
		case INITIAL:
		{
			// Immediately move to next state.
			this.state = SchedulerState.WAITING_FOR_MESSAGE;
			break;
		}
		case WAITING_FOR_MESSAGE:
		{
			// Check if the reprocess list has items.
			if (reprocessList.size() > 0) {
				// Get an item from the reprocess list.
				this.inputData = reprocessList.get(0);
				
				// Remove the item from the list.
				reprocessList.remove(0);
				
				this.state = SchedulerState.PROCESSING_NEW_JOB;
			}
			// Otherwise wait for a new message.
			else {
				SchedulerMessage msg = outputBuffer.get();
				
				// If the message is an InputData, process the new job.
				if ((this.inputData = msg.getInputData()) != null) {
					this.state = SchedulerState.PROCESSING_NEW_JOB;
				}
				// Otherwise the message is an elevator event, process it.
				else {
					this.elevatorEvent = msg.getElevatorEvent();
					this.state = SchedulerState.PROCESSING_ELEVATOR_EVENT;
				}
			}
			break;
		}
		case PROCESSING_NEW_JOB:
		{
			// Find elevator to handle this job.
			SchedulerElevator elevator = findElevatorForJob(this.inputData.getCurrentFloor(), this.inputData.getDirection());
			
			// If no elevator was found, add it to the blocked list and stop processing.
			if (elevator == null) {
				this.blockedInput.add(this.inputData);
				this.state = SchedulerState.WAITING_FOR_MESSAGE;
				break;
			}
			
			createJobForElevator(this.inputData, elevator);
			
			break;
		}
		case PROCESSING_ELEVATOR_EVENT:
		{
			System.out.println("Processing event " + this.elevatorEvent);
			
			// Get elevator matching ID.
			SchedulerElevator elevator = this.elevators.stream()
					.filter(el -> el.getElevatorId() == this.elevatorEvent.getElevatorId())
					.findAny()
					.orElseThrow();
			
			// Permanent fault handling
			if (this.elevatorEvent.getOutOfService()) {
				/* 
				 * Elevator has encountered a permanent fault, must reassign all jobs
				 * and remove the elevator from future use.
				 */
				
				// Add the assigned jobs to the reprocessing list.
				for (ScheduledJob job : elevator.getAssignedJobs()) {
					// Recreate the InputData but without the fault code now.
					InputData d = new InputData(
							job.getInputData().getTime(),
							job.getInputData().getCurrentFloor(),
							job.getInputData().getDirection(),
							job.getInputData().getDestinationFloor(),
							Fault.NONE);
					reprocessList.add(d);
				}
				
				// Remove the elevator from use.
				elevators.remove(elevator);
				
				// If no elevators left, exit.
				if (elevators.size() == 0) {
					System.out.println("Ran out of functioning elevators, exiting");
					this.state = SchedulerState.FINAL;
				}
				else {
					this.state = SchedulerState.WAITING_FOR_MESSAGE;
				}
				
				break;
			}
			
			// Make sure elevator is where it is expected to be.
			if (elevator.getLastKnownFloor() != this.elevatorEvent.getFloor()) {
				System.err.println(
						"Elevator "
						+ elevator.getElevatorId()
						+ " arrived at unexpected floor "
						+ this.elevatorEvent.getFloor()
						+ ". Expected: "
						+ elevator.getLastKnownFloor());
				System.exit(1);
			}
			
			// Check if elevator should pickup or drop off a passenger.
			ArrayList<ScheduledJob> completedJobs = new ArrayList<ScheduledJob>();
			boolean shouldRequestDoors = false;
			for (ScheduledJob job : elevator.getAssignedJobs()) {
				// If picking up passenger.
				if (this.elevatorEvent.getFloor() == job.getInputData().getCurrentFloor()) {
					// Set the elevator's fault code to the input's fault code.
					elevator.setPendingFault(job.getInputData().getFault());
					
					// If the doors have been opened already.
					if (elevator.getDoorsOpening()) {
						System.out.println("Elevator " + elevator.getElevatorId() + " picked up passenger on floor " + this.elevatorEvent.getFloor());
						moveToDropOff(elevator, job);
					} else {
						shouldRequestDoors = true;
						this.elevatorToCommand = elevator;
					}
				}
				// If dropping off passenger.
				else if (job.getIsOnElevator() && this.elevatorEvent.getFloor() == job.getInputData().getDestinationFloor()) {
					// If the doors have been opened already.
					if (elevator.getDoorsOpening()) {
						System.out.println(
								"Elevator "
								+ elevator.getElevatorId()
								+ " dropped off passenger on floor "
								+ this.elevatorEvent.getFloor()
								+ " for job "
								+ job.getJobId());
						completedJobs.add(job);
						
						try {
							//Notify Measurement that this job is done using DatagramPacket on Measurement's receiver port.
							DatagramPacket sendPacket = new DatagramPacket(job.getInputData().toBytes(), job.getInputData().toBytes().length, InetAddress.getLocalHost(), Constants.MEASUREMENT_RECEIVER_PORT);
							sendSocket.send(sendPacket);
						} catch (UnknownHostException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						shouldRequestDoors = true;
						this.elevatorToCommand = elevator;
					}
				}
			}
			
			// Reset the doors opening, should be handled by now.
			elevator.setDoorsOpening(false);
			
			// Remove complete jobs from elevator's assigned jobs.
			elevator.getAssignedJobs().removeAll(completedJobs);
			
			// If the elevator needs to open its doors.
			if (shouldRequestDoors) {
				// If opening doors to drop off last job, look for next job.
				if (elevator.getAssignedJobs().size() == 1
						&& elevator.getAssignedJobs().get(0).getIsOnElevator()
						&& this.blockedInput.size() > 0) {
					InputData inputData = blockedInput.get(0);
					this.blockedInput.remove(0);
					elevator.setDirection(Direction.WAITING);
					createJobForElevator(inputData, elevator);
				}
				
				this.state = SchedulerState.SCHEDULE_DOOR;
			}
			// If elevator has no jobs left.
			else if (elevator.getAssignedJobs().size() == 0) {
				elevator.setDirection(Direction.WAITING);
				elevator.setServiceDirection(Direction.WAITING);
				this.state = SchedulerState.WAITING_FOR_MESSAGE;
			}
			// If trying to move past top floor.
			else if (elevator.getLastKnownFloor() == this.numFloors && elevator.getDirection() == Direction.UP) {
				// This shouldn't happen. Print to err stream and quit.
				System.err.println("Tried to schedule elevator " + elevator.getElevatorId() + " above top floor.");
				System.exit(1);
			}
			// If trying to move below bottom floor.
			else if (elevator.getLastKnownFloor() == 1 && elevator.getDirection() == Direction.DOWN){
				// This shouldn't happen. Print to err stream and quit.
				System.err.println("Tried to schedule elevator " + elevator.getElevatorId() + " below bottom floor.");
				System.exit(1);
			}
			// Otherwise keep moving.
			else {
				this.elevatorToCommand = elevator;
				this.state = SchedulerState.SCHEDULE_MOVE;
			}
			
			break;
		}
		case SCHEDULE_MOVE:
		{
			// Update the elevator's last known floor.
			this.elevatorToCommand.updateLastKnownFloor();
			
			int destination = this.elevatorToCommand.findElevatorDestination();
			
			System.out.println(
				"Scheduling move Elevator "
				+ this.elevatorToCommand.getElevatorId()
				+ " "
				+ this.elevatorToCommand.getDirection() 
				+ " to "
				+ this.elevatorToCommand.getLastKnownFloor()
				+ ". Destination: "
				+ destination);
			
			// Permanent faults should be sent on move commands only.			
			Fault fault = Fault.NONE;
			if (this.elevatorToCommand.getPendingFault() == Fault.PERMANENT) {
				fault = Fault.PERMANENT;
				
				// Reset elevator fault code now that the command is sent.
				this.elevatorToCommand.setPendingFault(Fault.NONE);
			}
			
			// Add the elevator command to the buffer and move to waiting for message.
			this.inputBuffer.put(new ElevatorMoveCommand(
					this.elevatorToCommand.getElevatorId(),
					fault,
					this.elevatorToCommand.getDirection(),
					destination,
					this.elevatorToCommand.getServiceDirection()));
			
			
			
			this.state = SchedulerState.WAITING_FOR_MESSAGE;
			break;
		}
		case SCHEDULE_DOOR:
			System.out.println("Scheduling open doors Elevator " + this.elevatorToCommand.getElevatorId());
			
			// Set the elevator's doors as opening.
			this.elevatorToCommand.setDoorsOpening(true);
			
			// Transient faults should be sent on move commands only.
			Fault fault = Fault.NONE;
			if (this.elevatorToCommand.getPendingFault() == Fault.TRANSIENT) {
				fault = Fault.TRANSIENT;
				
				// Reset elevator fault code now that the command is sent.
				this.elevatorToCommand.setPendingFault(Fault.NONE);
			}
			
			// Add the elevator door command to the buffer and move to waiting for message.
			this.inputBuffer.put(new ElevatorDoorCommand(
					this.elevatorToCommand.getElevatorId(),
					fault,
					this.elevatorToCommand.getServiceDirection()));
			
			// Reset elevator fault code now that the command is sent.
			this.elevatorToCommand.setPendingFault(Fault.NONE);
			
			this.state = SchedulerState.WAITING_FOR_MESSAGE;
			break;
		case FINAL:
			return true;
		}
		return false;
	}

	/**
	 * Creates a job for the given elevator based on the input data.
	 * @param inputData The input data to create the job from.
	 * @param elevator The elevator to assign the job to.
	 */
	private void createJobForElevator(InputData inputData, SchedulerElevator elevator) {
		// Set the service direction for this elevator.
		elevator.setServiceDirection(inputData.getDirection());
		
		// Create the job and assign it to the chosen elevator.
		ScheduledJob job = new ScheduledJob(elevator, inputData);
		elevator.getAssignedJobs().add(job);
		System.out.println("Assigned job " + job.getJobId() + " to Elevator " + elevator.getElevatorId() + ": " + inputData);
		
		// If the elevator is waiting, start moving it towards the pickup.
		if (elevator.getDirection() == Direction.WAITING) {
			this.elevatorToCommand = elevator;
			
			// Move to appropriate scheduling state.
			if (moveToPickup(elevator, job)) {
				// Set the elevator's fault code to the input's fault code.
				elevator.setPendingFault(job.getInputData().getFault());
				this.state = SchedulerState.SCHEDULE_DOOR;
			} else {
				this.state = SchedulerState.SCHEDULE_MOVE;
			}
		}
		// Otherwise the elevator should already be moving towards the pickup
		// because of another job it is already handling.
		else {
			this.state = SchedulerState.WAITING_FOR_MESSAGE;
		}
	}
	
	/**
	 * Find the closest elevator to the start floor that is either waiting,
	 * or already moving in the correct direction for a different job.
	 * @param startFloor The starting floor of the job.
	 * @param direction The direction the job requires the elevator to move.
	 * @return The closest acceptable elevator.
	 */
	private SchedulerElevator findElevatorForJob(int startFloor, Direction direction) {
		SchedulerElevator elevator = null;
		int closestFloorGap = Integer.MAX_VALUE;
		
		// Loop through all known elevators.
		for (SchedulerElevator e : this.elevators) {
			// Check if the elevator is available.
			if (isElevatorAvailableForJob(e, startFloor, direction)) {
				// Calculate how far this elevator is from the pickup floor.
				int floorGap = Math.abs(e.getLastKnownFloor() - startFloor);
				
				// If the elevator is closer, store it.
				if (floorGap < closestFloorGap) {
					elevator = e;
					closestFloorGap = floorGap;
				}
			}
		}
		return elevator;
	}
	
	/**
	 * Determines if a specified elevator is either doing nothing, or has
	 * a position and direction that does not conflict with the givens.
	 * @param e The elevator to check.
	 * @param startFloor The floor the elevator has to get to.
	 * @param direction The direction the elevator has to be moving to reach the floor.
	 * @return Whether the elevator can take on this job.
	 */
	private boolean isElevatorAvailableForJob(SchedulerElevator e, int startFloor, Direction direction) {
		// If the elevator is waiting, it is available.
		if (e.getDirection() == Direction.WAITING) {
			return true;
		}
		// Otherwise, elevator must be moving in same direction as request.
		if (e.getDirection() == direction) {	
			// If the elevator already has requests, make sure they are in the same direction.
			// This prevents elevators from being assigned jobs in the wrong direction while they
			// move towards the pickup floor. Example: Elevator moving UP to pickup a DOWN request
			// should not be assigned an UP request.
			if (e.getAssignedJobs().size() > 0) {
				if (e.getAssignedJobs().get(0).getInputData().getDirection() != direction) {
					return false;
				}
			}
			
			// If the elevator will be moving up, it must be below the start floor to pick it up without turning around.
			if (direction == Direction.UP && e.getLastKnownFloor() <= startFloor) {
				return true;
			}
			// If the elevator will be moving down, it must be above the start floor to pick it up without turning around.
			else if (direction == Direction.DOWN && e.getLastKnownFloor() >= startFloor) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Sets the elevator's direction to move towards the pickup floor.
	 * @param elevator The elevator to move.
	 * @param job The job to move towards.
	 * @return Whether the elevator should open its doors for pickup.
	 */
	private boolean moveToPickup(SchedulerElevator elevator, ScheduledJob job) {
		// If the elevator is already on the start floor.
		if (elevator.getLastKnownFloor() == job.getInputData().getCurrentFloor()) {
			// Set the elevators direction to move towards the drop off.
			moveToDropOff(elevator, job);
			
			// Elevator should open doors for pickup.
			return true;
		}
		// If the elevator is below the start floor, move up towards it.
		else if (elevator.getLastKnownFloor() < job.getInputData().getCurrentFloor()) {
			elevator.setDirection(Direction.UP);
		}
		// If the elevator is above the start floor, move down towards it.
		else {
			elevator.setDirection(Direction.DOWN);
		}
		
		return false;
	}
	
	/**
	 * Sets the elevator's direction to move towards the drop off floor.
	 * @param elevator The elevator to move.
	 * @param job The job to move towards.
	 */
	private void moveToDropOff(SchedulerElevator elevator, ScheduledJob job) {
		job.setOnElevator(true);
		
		// If the elevator is below the end floor, move up towards it.
		if (elevator.getLastKnownFloor() < job.getInputData().getDestinationFloor()) {
			elevator.setDirection(Direction.UP);
		}
		// If the elevator is above the start floor, move down towards it.
		else {
			elevator.setDirection(Direction.DOWN);
		}
	}
}
