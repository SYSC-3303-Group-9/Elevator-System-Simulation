package elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import common.Constants;
import common.IBufferInput;
import scheduler.SchedulerMessage;

/**
 * Responsible for receiving ElevatorEvents from the ElevatorSubsystem.
 */
public class ElevatorEventReceiver implements Runnable {
	private IBufferInput<SchedulerMessage> messageBuffer;
	private DatagramSocket socket;

	public ElevatorEventReceiver(IBufferInput<SchedulerMessage> messageBuffer) throws SocketException {
		this.messageBuffer = messageBuffer;
		this.socket = new DatagramSocket(Constants.ELEVATOR_EVENT_RECEIVER_PORT);
	}

	@Override
	public void run() {

		while (true) {
			// Construct a DatagramPacket for receiving packets form the elevatorSubsystem
			byte data[] = new byte[500];
			DatagramPacket receivePacket = new DatagramPacket(data, data.length);

			try {
				// Block until a datagram is received via sendReceiveSocket.
				socket.receive(receivePacket);

				// Get new event from the elevatorSubsystem.
				ElevatorEvent event = ElevatorEvent.fromBytes(receivePacket.getData());
				System.out.println("[ElevatorCommunicator] Received event " + event);

				// If event is not null put event in the scheduler buffer
				if (event != null) {
					messageBuffer.put(SchedulerMessage.fromElevatorEvent(event));
					
					// If event is a door event, forward to the SchedulerReceiver.
					if (event.getIsDoorEvent()) {
						receivePacket.setPort(Constants.SCHEDULER_RECEIVER_PORT);
						socket.send(receivePacket);
					}
				}

			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

}
