package elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import common.Constants;

public class ElevatorSubsystem implements Runnable {
	
	private ElevatorMotor elevator;
	private ElevatorState state;
	ElevatorMoveCommand command = null;
	private DatagramSocket sendReceiveSocket;
	DatagramPacket receivePacket, sendPacket;

	public ElevatorSubsystem(ElevatorMotor elevator) {
		this.elevator = elevator;
		this.state = ElevatorState.INITIAL;
		try {
			// Unique port for each elevator.
			this.sendReceiveSocket = new DatagramSocket(Constants.ELEVATOR_BASE_PORT + elevator.getId());
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
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
	 * @param command
	 */
	public void sendCommand(ElevatorMoveCommand command) {
		// Move the Elevator
		elevator.move(command.getDirection());

		// Notify the scheduler that the elevator has moved down.
		ElevatorEvent elevatorInfo = new ElevatorEvent(elevator.getFloor(), elevator.getId(), true);// TO-DO: assign the service state of the elevator

		// Send ElevatorEvent packet to ElevatorCommunicator.
		try {
			sendPacket = new DatagramPacket(elevatorInfo.toBytes(), elevatorInfo.toBytes().length,
					InetAddress.getLocalHost(), receivePacket.getPort());
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			System.out.println("ElevatorSubsystem, sendCommand " + e);
			System.exit(1);
		}

		// Move to next state
		this.state = ElevatorState.WAITING;
	}
	
	
	/**
	 * Servers as a controller to the Elevator state machine.
	 * Each call "next()" enters the switch statement and transition into states accordingly.
	 * This enables the ability to track the state after each transition
	 * @return
	 */
	public boolean next() {
		switch (this.state) {

		case INITIAL:
			// Immediately move to the next state
			this.state = ElevatorState.WAITING;
			break;

		case WAITING:
			// Receive new ElevatorCommand packet from ElevatorComunicator via Elevator Base Port + Elevator ID.
			byte data[] = new byte[100];
			receivePacket = new DatagramPacket(data, data.length);

			try {
				sendReceiveSocket.receive(receivePacket);
			} catch (IOException e) {
				System.out.println("ElevatorSubsystem, run " + e);
			}

			command = ElevatorMoveCommand.fromBytes(receivePacket.getData());

			// If the buffer was disabled and returned null, stop execution.
			if (command == null) {
				// Move to the final state
				this.state = ElevatorState.FINAL;
			} else {
				// Change the state of the Elevator to Moving up or Moving down
				if (command.getDirection() == Direction.UP) {
					this.state = ElevatorState.MOVINGUP;
				} else if (command.getDirection() == Direction.DOWN) {
					this.state = ElevatorState.MOVINGDOWN;
				}
			}
			break;

		case MOVINGDOWN: {
			// Assuming at this point that the elevator has arrived.
			sendCommand(command);
			break;
		}

		case MOVINGUP: {
			// Assuming at this point that the elevator has arrived.
			sendCommand(command);
			break;
		}

		case FINAL:
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		while(true) {
			// if "next()" returns true, we have reached the final state of the state machine,
			// and therefore no longer can/need to transition
			if(next()) {
				break;
			}
		}
		
	}
}
