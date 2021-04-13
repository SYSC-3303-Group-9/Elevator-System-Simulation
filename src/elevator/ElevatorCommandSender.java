package elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import common.Constants;
import common.IBufferOutput;

public class ElevatorCommandSender implements Runnable {
	private IBufferOutput<ElevatorCommand> commandBuffer;
	private DatagramSocket socket;
	
	public ElevatorCommandSender(IBufferOutput<ElevatorCommand> commandBuffer) throws SocketException {
		this.commandBuffer = commandBuffer;
		this.socket = new DatagramSocket();
	}

	@Override
	public void run() {
		while (true) {
			// Get new instructions from the scheduler.
			ElevatorCommand command = commandBuffer.get();
			System.out.println("[ElevatorCommunicator] Sent " + command);

			// If buffer is not empty send instruction to elevatorSubsystem
			if (command != null) {
				try {
					byte commandByte[] = command.toBytes();
					int elevatorId = command.getID();
					DatagramPacket sendPacket = new DatagramPacket(
							commandByte,
							commandByte.length,
							InetAddress.getLocalHost(),
							Constants.ELEVATOR_BASE_PORT + elevatorId);
					
					socket.send(sendPacket);
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}else {
				break;
			}
		}
		
	}

}
