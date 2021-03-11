package elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import scheduler.Buffer;
import scheduler.ElevatorCommand;

public class ElevatorCommunicator implements Runnable {

	private Buffer<ElevatorCommand> packetToElevatorSubsystem;
	private Buffer<ElevatorEvent> packetFromElevatorSubsystem;
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendReceiveSocket;

	public ElevatorCommunicator(Buffer<ElevatorCommand> packetToElevatorSubsystem,
			Buffer<ElevatorEvent> packetFromElevatorSubsystem) {
		this.packetToElevatorSubsystem = packetToElevatorSubsystem;
		this.packetFromElevatorSubsystem = packetFromElevatorSubsystem;

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
			ElevatorCommand command = packetToElevatorSubsystem.get();

			// If buffer is not empty send instruction to elevatorSubsystem
			if (command != null) {
				byte commandByte[] = command.toBytes();
				sendPacket = new DatagramPacket(commandByte, commandByte.length);
				try {
					sendReceiveSocket.send(sendPacket);
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}

			// Construct a DatagramPacket for receiving packets form the elevatorSubsystem
			byte data[] = new byte[8];
			receivePacket = new DatagramPacket(data, data.length);

			try {
				// Block until a datagram is received via sendReceiveSocket.
				sendReceiveSocket.receive(receivePacket);

				// Get new event from the elevatorSubsystem.
				ElevatorEvent event = ElevatorEvent.fromBytes(receivePacket.getData());

				// If buffer is not empty put event in the scheduler buffer
				if (event != null) {
					packetFromElevatorSubsystem.put(event);
				}

			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

}
