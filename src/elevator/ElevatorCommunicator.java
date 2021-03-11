package elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import common.IBufferInput;
import common.IBufferOutput;
import scheduler.SchedulerMessage;

public class ElevatorCommunicator implements Runnable {

	private IBufferOutput<ElevatorCommand> fromElevatorSubsystem;
	private IBufferInput<SchedulerMessage> toElevatorSubsystem;
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendReceiveSocket;

	public ElevatorCommunicator(IBufferOutput<ElevatorCommand> fromElevatorSubsystem,
			IBufferInput<SchedulerMessage> toElevatorSubsystem) {
		this.fromElevatorSubsystem = fromElevatorSubsystem;
		this.toElevatorSubsystem = toElevatorSubsystem;

		try {
			// Construct a Datagram socket and bind it to the specified
			sendReceiveSocket = new DatagramSocket();
		} catch (SocketException se) { // Can't create the socket.
			sendReceiveSocket.close();
			se.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void run() {

		while (true) {

			// Get new instructions from the scheduler.
			ElevatorCommand command = fromElevatorSubsystem.get();

			// If buffer is not empty send instruction to elevatorSubsystem
			if (command != null) {
				byte commandByte[] = command.toBytes();
				int elevatorId = command.getID();

				try {
					sendPacket = new DatagramPacket(commandByte, commandByte.length, InetAddress.getLocalHost(),
							elevatorId + 50);
					sendReceiveSocket.send(sendPacket);
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}else {
				break;
			}

			// Construct a DatagramPacket for receiving packets form the elevatorSubsystem
			byte data[] = new byte[8];
			receivePacket = new DatagramPacket(data, data.length);

			try {
				// Block until a datagram is received via sendReceiveSocket.
				sendReceiveSocket.receive(receivePacket);

				// Get new event from the elevatorSubsystem.
				ElevatorEvent event = ElevatorEvent.fromBytes(receivePacket.getData());

				// If event is not null put event in the scheduler buffer
				if (event != null) {
					toElevatorSubsystem.put(SchedulerMessage.fromElevatorEvent(event));
				}

			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

}
