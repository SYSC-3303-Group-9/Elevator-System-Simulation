package elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import common.Constants;

public class ElevatorSubsystem implements Runnable {

	private ElevatorMotor elevator;
	private ElevatorState state;
	private ElevatorDoor door;
	private int id;
	private int floor;
	private ElevatorCommand command = null;
	private ElevatorMoveCommand moveCmd = null;
	private ElevatorDoorCommand doorCmd = null;
	private DatagramSocket sendReceiveSocket;
	private DatagramPacket receivePacket, sendPacket;

	public ElevatorSubsystem(ElevatorMotor elevator, int floor, int id) {
		this.elevator = elevator;
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
	 * Notifies the Scheduler that the elevator has moved
	 * 
	 * @param command
	 */
	public void sendMoveCommand(ElevatorMoveCommand command) {
		// Move the Elevator
		this.move(command.getDirection());

		ElevatorEvent elevatorInfo = new ElevatorEvent(this.floor, this.id, false);

		// Send ElevatorEvent packet to ElevatorCommunicator.
		try {
			sendPacket = new DatagramPacket(elevatorInfo.toBytes(), elevatorInfo.toBytes().length,
					InetAddress.getLocalHost(), receivePacket.getPort());
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Move to next state
		this.state = ElevatorState.WAITING;
	}

	/**
	 * Notifies the Scheduler that the elevator is disabled due to a permanent fault
	 * 
	 * @param command
	 */
	public void sendFault() {

		// Notify the scheduler that the elevator has a permanent fault
		ElevatorEvent elevatorInfo = new ElevatorEvent(this.floor, this.id, true);

		// Send ElevatorEvent packet to ElevatorCommunicator.
		try {
			sendPacket = new DatagramPacket(elevatorInfo.toBytes(), elevatorInfo.toBytes().length,
					InetAddress.getLocalHost(), receivePacket.getPort());
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			System.out.println("ElevatorSubsystem, sendFault " + e);
			System.exit(1);
		}

	}

	/**
	 * Moves the elevator to a specified floor and prints the floor it left and the
	 * destination floor.
	 * 
	 * @param input The input contains the destination the elevator needs to move to
	 *              amongst other data.
	 */
	public void move(Direction direction) {

		// opening and closing elevator door
		if (command.getFault() == Fault.TRANSIENT) {
			door.openClose(Fault.TRANSIENT);
		} else {
			door.openClose(Fault.NONE);
		}

		// elevator is moving
		elevator.move(Fault.NONE);

		// opening and closing elevator door
		if (command.getFault() == Fault.TRANSIENT) {
			door.openClose(Fault.TRANSIENT);
		} else {
			door.openClose(Fault.NONE);
		}

		if (direction == Direction.UP) {
			floor++;
		} else {
			floor--;
		}

		System.out.println("Elevator " + this.getId() + " is moving " + direction);
		System.out.println("Elevator " + this.getId() + " arrived at floor " + floor);

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
			} else {
				// Change the state of the Elevator to Moving up or Moving down or
				// notFunctional?
				if (command.getFault() == Fault.PERMANENT) {
					this.state = ElevatorState.DISABLED;
				} else {
					// identify what kind of elevator command was passed
					if (command instanceof ElevatorMoveCommand) {
						moveCmd = (ElevatorMoveCommand) command;
						if (moveCmd.getDirection() == Direction.UP) {
							this.state = ElevatorState.MOVINGUP;
						} else if (moveCmd.getDirection() == Direction.DOWN) {
							this.state = ElevatorState.MOVINGDOWN;
						}
					} else if (command instanceof ElevatorDoorCommand) {
						this.state = ElevatorState.OPENING_CLOSING_DOORS;
						doorCmd = (ElevatorDoorCommand) command;
					}

				}
			}
			break;

		case MOVINGDOWN: {
			// Sending instruction to move elevator down
			sendMoveCommand(moveCmd);
			this.state = ElevatorState.WAITING;
			break;
		}

		case MOVINGUP: {
			// Sending instruction to move elevator up
			sendMoveCommand(moveCmd);
			this.state = ElevatorState.WAITING;
			break;
		}

		case DISABLED: {
			sendFault();
			this.state = ElevatorState.FINAL;
			break;
		}

		case FINAL:
			return true;
		case OPENING_CLOSING_DOORS:
			door.openClose(doorCmd.getFault());
			this.state = ElevatorState.WAITING;
			break;
		default:
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
}
