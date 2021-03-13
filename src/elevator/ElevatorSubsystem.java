package elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import floor.InputData;
import scheduler.Buffer;
import scheduler.ElevatorCommand;

public class ElevatorSubsystem implements Runnable {
	
	private Elevator elevator;
	private ElevatorState state;
	private Buffer<InputData> schedulerToElevatorBuffer;
	private Buffer<ElevatorEvent> elevatorToSchedulerBuffer;
	private DatagramSocket sendReceiveSocket;
	DatagramPacket receivePacket, sendPacket;
	
	public ElevatorSubsystem(Elevator elevator, Buffer<InputData> schedulerToElevatorBuffer,
	Buffer<ElevatorEvent> elevatorToSchedulerBuffer) {
		this.elevator = elevator;
		this.state = ElevatorState.INITIAL;
		this.schedulerToElevatorBuffer = schedulerToElevatorBuffer;
		this.elevatorToSchedulerBuffer = elevatorToSchedulerBuffer;
		try {
			//Unique port for each elevator
			this.sendReceiveSocket = new DatagramSocket(50 + elevator.getId());
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	public void sendCommand(ElevatorCommand command) {
		//Move the Elevator
		elevator.move(command.getDirection());
		
		// Notify the scheduler that the elevator has moved down.
		ElevatorEvent elevatorInfo = new ElevatorEvent(elevator.getFloor(), elevator.getId());
		
		//Send ElevatorEvent packet to ElevatorCommand via port 50 + Elevator ID
		try {
			sendPacket = new DatagramPacket(elevatorInfo.toBytes(),elevatorInfo.toBytes().length, InetAddress.getLocalHost(), 50 + elevator.getId());
			sendReceiveSocket.send(sendPacket);
		} catch(IOException e) {
			System.out.println("ElevatorSubsystem, sendCommand " + e);
			System.exit(1);
		}
		
		// Move to next state
		this.state = ElevatorState.WAITING;
	}
	
	
	
	@Override
	public void run() {
		ElevatorCommand command = null;
		stateMachine: while (true) {
			switch (this.state) {
				
				case INITIAL:
					// Immediately move to the next state
					this.state = ElevatorState.WAITING;
					break;
					
				case WAITING:
					// Receive new ElevatorCommand packet from ElevatorCoomunicator via port 50 + Elevator ID.
					byte data[] = new byte[100];
					DatagramPacket receivePacket = new DatagramPacket(data, data.length);
					
					try {
						sendReceiveSocket.receive(receivePacket);
					} catch(IOException e) {
						System.out.println("ElevatorSubsystem, run " + e);
					}
					
					command = ElevatorCommand.fromBytes(receivePacket.getData());
					
					// If the buffer was disabled and returned null, stop execution.
					if (command == null) {
						// Move to the final state
						this.state = ElevatorState.FINAL;
					}
					else {
						// Change the state of the Elevator to Moving up or Moving down
						if(command.getDirection() == Direction.UP){
							this.state = ElevatorState.MOVINGUP;
						} else if(command.getDirection() == Direction.DOWN){
							this.state = ElevatorState.MOVINGDOWN;
						}
					}
					break;
				
				case MOVINGDOWN:
				{	
					// Assuming at this point that the elevator has arrived.
					sendCommand(command);
					break;
				}

				case MOVINGUP:
				{
					// Assuming at this point that the elevator has arrived.
					sendCommand(command);
					break;
				}
		
				case FINAL:
					break stateMachine;
				}
		}
	}
}
