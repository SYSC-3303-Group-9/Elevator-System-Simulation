package elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import common.Constants;

public class ElevatorSubsystem implements Runnable {

	private ElevatorMotor motor;
	private ElevatorState state;
	private ElevatorDoor door;
	private int id;
	private int floor;
	private ElevatorCommand command = null;
	private ElevatorMoveCommand moveCmd = null;
	private ElevatorDoorCommand doorCmd = null;
	private DatagramSocket sendReceiveSocket;
	private DatagramPacket receivePacket, sendPacket;

	public ElevatorSubsystem(ElevatorMotor motor, int floor, int id) {
		this.motor = motor;
		this.state = ElevatorState.INITIAL;
		this.door = new ElevatorDoor();
		this.id = id;
		this.floor = floor;
		try {
			// Unique port for each elevator.
			this.sendReceiveSocket = new DatagramSocket(Constants.ELEVATOR_BASE_PORT + id);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Gets the ID of the elevatorSubsystem
	 * 
	 * @return the elevators ID
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the floor number
	 * 
	 * @return the floor number
	 */
	public int getFloor() {
		return floor;
	}

	/**
	 * Gets the current state of the ElevatorSubsystem state machine.
	 * 
	 * @return The current state.
	 */
	public ElevatorState getState() {
		return this.state;
	}

	/**
	 * Sends an ElevatorEvent to the scheduler.
	 * @param isPermanentFault Whether this elevator encountered a permanent fault.
	 * @param isDoorEvent Whether this was a door event.
	 */
	private void sendElevatorEvent(boolean isPermanentFault, boolean isDoorEvent) {
		ElevatorEvent elevatorInfo = new ElevatorEvent(this.floor, this.id, isPermanentFault, isDoorEvent);

		// Send ElevatorEvent packet to ElevatorCommunicator.
		try {
			sendPacket = new DatagramPacket(elevatorInfo.toBytes(), elevatorInfo.toBytes().length,
					InetAddress.getLocalHost(), Constants.ELEVATOR_EVENT_RECEIVER_PORT);
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	

	/**
	 * Notifies the Scheduler that the elevator is disabled due to a permanent fault
	 * @param isDoorEvent boolean whether the ElevatorEvent that occurred was a door event
	 */
	private void sendElevatorMoveEvent() {
		sendElevatorEvent(false, false);
	}
	
	/**
	 * Notifies the Scheduler that the elevator opened and closed its doors.
	 */
	private void sendElevatorDoorEvent() {
		sendElevatorEvent(false, true);
	}
	
	/**
	 * Notifies the Scheduler that the elevator encountered a permanent fault.
	 */
	private void sendElevatorFaultEvent() {
		sendElevatorEvent(true, false);
	}

	/**
	 * Moves the elevator to a specified floor and prints the floor it left and the
	 * destination floor.
	 * 
	 * @param direction The direction that the elevator will move in.
	 * @param fault A possible fault that will occur during the move action.
	 */
	public void move(Direction direction) {
		System.out.println(this + " is moving " + direction);
		// elevator is moving
		motor.move();
		if (direction == Direction.UP) {
			floor++;
		} else {
			floor--;
		}	
		System.out.println(this + " arrived at floor " + floor + ". Destination floor " + this.moveCmd.getDestinationFloor());
	}

	/**
	 * Servers as a controller to the Elevator state machine. Each call "next()"
	 * enters the switch statement and transition into states accordingly. This
	 * enables the ability to track the state after each transition
	 * 
	 * @return
	 */
	public boolean next() {
		long waitTime;
		switch (this.state) {
			case INITIAL:
				// Immediately move to the next state
				this.state = ElevatorState.WAITING;
				break;
	
			case WAITING:
				// Receive new ElevatorCommand packet from ElevatorComunicator via Elevator Base
				// Port + Elevator ID.
				byte data[] = new byte[100];
				receivePacket = new DatagramPacket(data, data.length);
	
				try {
					sendReceiveSocket.receive(receivePacket);
				} catch (IOException e) {
					System.out.println("ElevatorSubsystem, run " + e);
				}
	
				command = ElevatorCommand.fromBytes(receivePacket.getData());
	
				// If the buffer was disabled and returned null, stop execution.
				if (command == null) {
					// Move to the final state
					this.state = ElevatorState.FINAL;
				} 
				else {
					// Identify what kind of elevator command was passed
					if (command instanceof ElevatorMoveCommand) {
						moveCmd = (ElevatorMoveCommand) command;
						// Check if there is a permanent fault
						if(moveCmd.getFault() == Fault.PERMANENT) {
							this.state = ElevatorState.PERMANENT_FAULT;
						}
						else {
							// Change the state of the Elevator to Moving up or Moving down or 
							if (moveCmd.getDirection() == Direction.UP) {
								this.state = ElevatorState.MOVING_UP;
							} 
							else if (moveCmd.getDirection() == Direction.DOWN) {
								this.state = ElevatorState.MOVING_DOWN;
							}
						}
					} 
					else if (command instanceof ElevatorDoorCommand) {
						doorCmd = (ElevatorDoorCommand) command;
						if(doorCmd.getFault() == Fault.TRANSIENT) {
							this.state = ElevatorState.TRANSIENT_FAULT;
						}
						else {							
							this.state = ElevatorState.OPENING_CLOSING_DOORS;
						}
					}
				}
				break;
			
			case TRANSIENT_FAULT:
				System.out.println(this + " has encountered a fault. Attempting to overcome it.");
				waitTime = (long) (Constants.TRANSIENT_FAULT_TIME / Constants.TIME_MULTIPLIER);
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {}
				System.out.println(this + " has overcome a transient fault.");
				this.state = ElevatorState.OPENING_CLOSING_DOORS;
				break;
				
			case PERMANENT_FAULT:
				System.out.println(this + " has encountered a fault. Attempting to overcome it.");
				waitTime = (long) (Constants.PERMANENT_FAULT_TIME / Constants.TIME_MULTIPLIER);
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {}
				System.out.println(this + " has encountered a permanent fault. Shutting down.");
				this.state = ElevatorState.DISABLED;
				break;
				
			case MOVING_DOWN: 
				move(Direction.DOWN);
				sendElevatorMoveEvent();
				this.state = ElevatorState.WAITING;	
				break;
	
			case MOVING_UP: 
				move(Direction.UP);
				sendElevatorMoveEvent();
				this.state = ElevatorState.WAITING;	
				break;
				
			case DISABLED: 
				sendElevatorFaultEvent();
				this.state = ElevatorState.FINAL;
				break;
	
			case FINAL:
				return true;
				
			case OPENING_CLOSING_DOORS:
				System.out.println(this + " opening doors");
				door.openClose();
				System.out.println(this + " closed doors");
				sendElevatorDoorEvent();
				this.state = ElevatorState.WAITING;
				break;		
		}
		return false;
	}

	@Override
	public void run() {
		while (true) {
			// if "next()" returns true, we have reached the final state of the state
			// machine,
			// and therefore no longer can/need to transition
			if (next()) {
				break;
			}
		}

	}
	
	@Override
	public String toString() {
		return "Elevator " + this.id;
	}
}
