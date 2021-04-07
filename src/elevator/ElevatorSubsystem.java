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
	public boolean move(Direction direction, Fault fault) {
		System.out.println(this + " is moving " + direction);
		// elevator is moving
		if(motor.move(fault)) {
			if (direction == Direction.UP) {
				floor++;
			} else {
				floor--;
			}	
			System.out.println(this + " arrived at floor " + floor + ". Destination floor " + this.moveCmd.getDestinationFloor());
			return true;
		}
		else {
			System.out.println(this + " has encountered a permanent error. Shutting down.");
			return false;
		}
	}

	/**
	 * Servers as a controller to the Elevator state machine. Each call "next()"
	 * enters the switch statement and transition into states accordingly. This
	 * enables the ability to track the state after each transition
	 * 
	 * @return
	 */
	public boolean next() {
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
						// Change the state of the Elevator to Moving up or Moving down or 
						if (moveCmd.getDirection() == Direction.UP) {
							this.state = ElevatorState.MOVINGUP;
						} 
						else if (moveCmd.getDirection() == Direction.DOWN) {
							this.state = ElevatorState.MOVINGDOWN;
						}
					} 
					else if (command instanceof ElevatorDoorCommand) {
						this.state = ElevatorState.OPENING_CLOSING_DOORS;
						doorCmd = (ElevatorDoorCommand) command;
					}
	
				}
				break;
	
			case MOVINGDOWN: 
				if(move(Direction.DOWN, moveCmd.getFault())) {
					// Move down was successful, so send back ElevatorEvent indicating such
					sendElevatorMoveEvent();
					this.state = ElevatorState.WAITING;					
				}
				// A permanent fault has occurred, so disable the elevator
				else {
					this.state = ElevatorState.DISABLED;
				}
				break;
	
			case MOVINGUP: 
				if(move(Direction.UP, moveCmd.getFault())) {
					// Move down was successful, so send back ElevatorEvent indicating such*
					sendElevatorMoveEvent();
					this.state = ElevatorState.WAITING;					
				}
				// A permanent fault has occurred, so disable the elevator
				else {
					this.state = ElevatorState.DISABLED;
				}
				break;
	
			case DISABLED: 
				sendElevatorFaultEvent();
				this.state = ElevatorState.FINAL;
				break;
	
			case FINAL:
				return true;
				
			case OPENING_CLOSING_DOORS:
				System.out.println(this + " opening doors");
				if(door.openClose(doorCmd.getFault())) {
					System.out.println(this + " closed doors");
					sendElevatorDoorEvent();
					this.state = ElevatorState.WAITING;
				}
				else {
					this.state = ElevatorState.DISABLED;
				}
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
