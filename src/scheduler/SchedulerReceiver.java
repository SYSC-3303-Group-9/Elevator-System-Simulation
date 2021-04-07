package scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import common.Constants;
import common.IBufferInput;
import elevator.ElevatorEvent;

/**
 * Responsible for receiving ElevatorEvents from the Scheduler's ElevatorEventReceiver.
 * Should only receive ElevatorEvents that have a door event.
 */
public class SchedulerReceiver implements Runnable {
	private IBufferInput<ElevatorEvent> buffer;
	private DatagramSocket socket;

	public SchedulerReceiver(IBufferInput<ElevatorEvent> buffer) throws SocketException {
		this.buffer = buffer;
		this.socket = new DatagramSocket(Constants.SCHEDULER_RECEIVER_PORT);
	}

	@Override
	public void run() {
		// Loop forever.
		while (true) {
			// Receive ElevatorEvent packets.
			DatagramPacket rcvPacket = new DatagramPacket(new byte[100], 100);
			try {
				this.socket.receive(rcvPacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			// Convert packet data to an ElevatorEvent instance.
			ElevatorEvent event = ElevatorEvent.fromBytes(rcvPacket.getData());
			System.out.println("[SchedulerReceiver] Received " + event);
			
			// Put the ElevatorEvent in the buffer.
			buffer.put(event);
		}
	}

}
