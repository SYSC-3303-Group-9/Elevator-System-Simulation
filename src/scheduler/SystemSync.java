package scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import common.Clock;
import common.Constants;

public class SystemSync implements Runnable {
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendReceiveSocket;
	private Boolean floorReady, elevatorReady;
	private Clock clock;

	public SystemSync() {
		try {
			sendReceiveSocket = new DatagramSocket(Constants.SYSTEM_SYNC_PORT);
		} catch (SocketException se) {
			sendReceiveSocket.close();
			se.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void run() {
		// Construct a DatagramPacket for receiving packets
		byte data[] = new byte[8];
		receivePacket = new DatagramPacket(data, data.length);

		// Receive 2 packets only
		for (int i = 0; i < 2; i++) {
			try {
				// Block until a datagram is received via sendReceiveSocket.
				sendReceiveSocket.receive(receivePacket);

				// Get string from packet
				if (receivePacket.getData().toString().equals("floor")) {
					floorReady = true;
				} else if (receivePacket.getData().toString().equals("elevator")) {
					elevatorReady = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		if (elevatorReady && floorReady) {
			
				// send reply to floor
				this.sendPacket(true);
				
				// send reply to elevator
				this.sendPacket(false);
				
				//start system clock
				this.clock.startClock();
				

		}

	}

	private void sendPacket(boolean floor) {

		int port = floor ? Constants.FLOOR_RECEIVER_PORT : Constants.ELEVATOR_BASE_PORT;

		// Construct a DatagramPacket for sending packet to floor
		byte reply[] = "start".getBytes();

		// send reply packet
		try {
			sendPacket = new DatagramPacket(reply, reply.length, InetAddress.getLocalHost(), port);
			sendReceiveSocket.send(sendPacket);
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
