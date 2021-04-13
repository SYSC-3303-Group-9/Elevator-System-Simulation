package scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import common.Constants;
import elevator.ElevatorEvent;
import elevator.gui.DirectionLampPanel;
import floor.gui.FloorFrame;

/**
 * Responsible for receiving ElevatorEvents from the Scheduler's ElevatorEventReceiver.
 * Should only receive ElevatorEvents that have a door event.
 */
public class SchedulerReceiver implements Runnable {
	private DatagramSocket socket;
	private FloorFrame floorFrame;

	public SchedulerReceiver(FloorFrame floorFrame) throws SocketException {
		this.socket = new DatagramSocket(Constants.SCHEDULER_RECEIVER_PORT);
		this.floorFrame = floorFrame;
	}

	@Override
	public void run() {
		// Loop forever.
		while (true) {
			// Receive ElevatorEvent packets.
			DatagramPacket rcvPacket = new DatagramPacket(new byte[500], 500);
			try {
				this.socket.receive(rcvPacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			// Convert packet data to an ElevatorEvent instance.
			ElevatorEvent event;
			try {
				event = ElevatorEvent.fromBytes(rcvPacket.getData());
				System.out.println("[SchedulerReceiver] Received " + event);
				DirectionLampPanel floorButton = this.floorFrame.getDirectionButton(event.getFloor()-1);
				
				// Turn off the lamp for the direction of the elevator that arrived.
				floorButton.turnOffLamp(event.getServiceDirection());
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}

}
