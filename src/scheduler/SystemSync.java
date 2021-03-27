package scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

import common.Clock;
import common.Constants;

public class SystemSync {
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendReceiveSocket;
	private Boolean floorReady = false, elevatorReady = false;
	int floorPort, elevatorPort;

	/**
	 * Receives packets from floor and elevator sync classes and starts clock
	 * after sending a confirmation packet
	 */
	public SystemSync() {
		try {
			sendReceiveSocket = new DatagramSocket(Constants.SYSTEM_SYNC_PORT);
		} catch (SocketException se) {
			sendReceiveSocket.close();
			se.printStackTrace();
			System.exit(1);
		}
	}

	public void run() {
		while (true) {
			if (syncing())
				break;
		}

	}

	/**
	 * Sends packet to floor is boolean is true, if boolean is false sends to
	 * elevator
	 * 
	 * @param floor
	 */
	private void sendPacket(boolean floor) {

		int port = floor ? this.floorPort : this.elevatorPort;

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

	/** Checks for elevator and floor sync packets and returns a confirmation when both are received
	 * Starts the system clock after ending conffirmation packet
	 * @return true is both elevator and floor packets are received
	 */
	public boolean syncing() {
		// Construct a DatagramPacket for receiving packets
		byte data[] = new byte[8];
		receivePacket = new DatagramPacket(data, data.length);

		try {
			// Block until a datagram is received via sendReceiveSocket.
			sendReceiveSocket.receive(receivePacket);
			//System.out.println(Arrays.toString(receivePacket.getData()));

			// Get string from packet and check if it's floor or elevator
			String receivedString = (new String(receivePacket.getData()).trim());
			//System.out.println(receivedString);

			if (receivedString.equals("floor")) {
				// get port number of floor
				floorPort = receivePacket.getPort();
				floorReady = true;
			} else if (receivedString.equals("elevator")) {
				// get port number of elevator
				elevatorPort = receivePacket.getPort();
				elevatorReady = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		if (elevatorReady && floorReady) {

			// send reply to floor
			this.sendPacket(true);

			// send reply to elevator
			this.sendPacket(false);

			// start system clock
			Clock.startClock();

			sendReceiveSocket.close();

			return true;
		}
		return false;

	}
}
