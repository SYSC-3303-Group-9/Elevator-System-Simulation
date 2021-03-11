package scheduler;

import java.util.ArrayList;

import common.IBufferInput;
import common.IBufferOutput;
import elevator.Direction;
import elevator.ElevatorCommand;
import elevator.ElevatorEvent;
import floor.InputData;

public class Scheduler implements Runnable {
	// State specific variables
	private SchedulerState state;
	private SchedulerMessage message;
	private InputData inputData;
	private ElevatorEvent elevatorEvent;
	private SchedulerElevator elevatorToCommand;
	
	private ArrayList<SchedulerElevator> elevators = new ArrayList<SchedulerElevator>();
	private ArrayList<InputData> blockedInput = new ArrayList<InputData>();

	private int numFloors;
	private IBufferOutput<SchedulerMessage> outputBuffer;
	private IBufferInput<ElevatorCommand> inputBuffer;
	
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
			// Wait for a new message and move to message processing state.
			this.message = outputBuffer.get();
			this.state = SchedulerState.PROCESSING_MESSAGE;
			break;
		}
		case PROCESSING_MESSAGE:
		{
			// If the message is an InputData, process the new job.
			if ((this.inputData = this.message.getInputData()) != null) {
				this.state = SchedulerState.PROCESSING_NEW_JOB;
			}
			// Otherwise the message is an elevator event, process it.
			else {
				this.elevatorEvent = this.message.getElevatorEvent();
				this.state = SchedulerState.PROCESSING_ELEVATOR_EVENT;
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
			
			// Create the job and assign it to the chosen elevator.
			ScheduledJob job = new ScheduledJob(elevator, inputData);
			elevator.getAssignedJobs().add(job);
			
			// If the elevator is waiting, start moving it towards the pickup.
			if (elevator.getDirection() == Direction.WAITING) {
				moveToPickup(elevator, job);
				this.elevatorToCommand = elevator;
				this.state = SchedulerState.SCHEDULING_ELEVATOR;
			}
			// Otherwise the elevator should already be moving towards the pickup
			// because of another job it is already handling.
			else {
				this.state = SchedulerState.WAITING_FOR_MESSAGE;
			}
			
			break;
		}
		case PROCESSING_ELEVATOR_EVENT:
		{			
			// Get elevator matching ID.
			SchedulerElevator elevator = this.elevators.stream()
					.filter(el -> el.getElevatorId() == this.elevatorEvent.getElevatorId())
					.findAny()
					.orElseThrow();
			
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
			for (ScheduledJob job : elevator.getAssignedJobs()) {
				// If picking up passenger.
				if (this.elevatorEvent.getFloor() == job.getStartFloor()) {
					job.setOnElevator(true);
					moveToDropOff(elevator, job);
				}
				// If dropping off passenger.
				else if (job.getIsOnElevator() && this.elevatorEvent.getFloor() == job.getEndFloor()) {
					completedJobs.add(job);
				}
			}
			
			// Remove complete jobs from elevator's assigned jobs.
			elevator.getAssignedJobs().removeAll(completedJobs);
			
			// If elevator has no jobs left.
			if (elevator.getAssignedJobs().size() == 0) {
				elevator.setDirection(Direction.WAITING);
				
				// Look for blocked jobs.
				if (this.blockedInput.size() > 0) {
					this.inputData = blockedInput.get(0);
					this.blockedInput.remove(0);
					this.state = SchedulerState.PROCESSING_NEW_JOB;
				}
				// Otherwise wait for next message.
				else {
					this.state = SchedulerState.WAITING_FOR_MESSAGE;
				}
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
				this.state = SchedulerState.SCHEDULING_ELEVATOR;
			}
			
			break;
		}
		case SCHEDULING_ELEVATOR:
		{
			// Update the elevator's last known floor.
			this.elevatorToCommand.updateLastKnownFloor();
			
			// Add the elevator command to the buffer and move to waiting for message.
			this.inputBuffer.put(new ElevatorCommand(this.elevatorToCommand.getElevatorId(), this.elevatorToCommand.getDirection()));
			this.state = SchedulerState.WAITING_FOR_MESSAGE;
			break;
		}
		case FINAL:
			return true;
		}
		return false;
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
	 */
	private void moveToPickup(SchedulerElevator elevator, ScheduledJob job) {
		// If the elevator is already on the start floor.
		if (elevator.getLastKnownFloor() == job.getStartFloor()) {
			// Put the person on the elevator and start moving towards drop off.
			job.setOnElevator(true);
			moveToDropOff(elevator, job);
		}
		// If the elevator is below the start floor, move up towards it.
		else if (elevator.getLastKnownFloor() < job.getStartFloor()) {
			elevator.setDirection(Direction.UP);
		}
		// If the elevator is above the start floor, move down towards it.
		else {
			elevator.setDirection(Direction.DOWN);
		}
	}
	
	/**
	 * Sets the elevator's direction to move towards the drop off floor.
	 * @param elevator The elevator to move.
	 * @param job The job to move towards.
	 */
	private void moveToDropOff(SchedulerElevator elevator, ScheduledJob job) {
		// If the elevator is below the end floor, move up towards it.
		if (elevator.getLastKnownFloor() < job.getEndFloor()) {
			elevator.setDirection(Direction.UP);
		}
		// If the elevator is above the start floor, move down towards it.
		else {
			elevator.setDirection(Direction.DOWN);
		}
	}
}
