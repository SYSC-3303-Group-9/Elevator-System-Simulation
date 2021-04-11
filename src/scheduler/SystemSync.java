package scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import common.Clock;
import common.Constants;
import scheduler.GUI.ConfigurationFrame;

public class SystemSync {
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendReceiveSocket;
	private Boolean floorReady = false, elevatorReady = false;
	private int floorPort, elevatorPort;
	private RunTimeConfig configData;
	private ConfigurationFrame configFrame;

	/**
	 * Receives packets from floor and elevator sync classes and starts clock after
	 * sending a confirmation packet
	 */
	public SystemSync() {
		//create a configuration frame
		configFrame = new ConfigurationFrame();

		//do nothing while configuration frame is not done
		while (!configFrame.isDone()) {
		}
		//create a RunTimeConfig object using data entered in configuration frame
		configData = new RunTimeConfig(configFrame.getFloorNum(), configFrame.getElevatorNum(),
				configFrame.getInputFile());
		try {
			sendReceiveSocket = new DatagramSocket(Constants.SYSTEM_SYNC_PORT);
		} catch (SocketException se) {
			sendReceiveSocket.close();
			se.printStackTrace();
			System.exit(1);
		}
		System.out.println("Config done");
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
		byte reply[] = configData.toBytes();

		// send reply packet
		try {
			sendPacket = new DatagramPacket(reply, reply.length, InetAddress.getLocalHost(), port);
			sendReceiveSocket.send(sendPacket);

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Checks for elevator and floor sync packets and returns a confirmation when
	 * both are received Starts the system clock after ending conffirmation packet
	 * 
	 * @return true is both elevator and floor packets are received
	 */
	public boolean syncing() {
		// Construct a DatagramPacket for receiving packets
		System.out.println("Syncing");
		byte data[] = new byte[8];
		receivePacket = new DatagramPacket(data, data.length);

		try {
			// Block until a datagram is received via sendReceiveSocket.
			sendReceiveSocket.receive(receivePacket);

			// Get string from packet and check if it's floor or elevator
			String receivedString = (new String(receivePacket.getData()).trim());

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
	
	public RunTimeConfig getConfigData() {
		return configData;
	}
	
}
