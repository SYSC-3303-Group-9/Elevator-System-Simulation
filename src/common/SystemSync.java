package common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Handshake coordinator. Makes sure all applications have synced clocks and config data.
 */
public class SystemSync implements Runnable {
	private ArrayList<ISystemSyncListener> listeners = new ArrayList<ISystemSyncListener>();
	
	private DatagramSocket socket;
	private int floorPort, elevatorPort;
	
	public SystemSync() {
		try {
			socket = new DatagramSocket(Constants.SYSTEM_SYNC_PORT);
		} catch (SocketException se) {
			socket.close();
			se.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Send a packet with the given data to the given port.
	 * @param port The port to send to.
	 * @param configData The data to send.
	 */
	private void sendPacket(int port, byte[] data) {
		
		// Construct a DatagramPacket for sending packet to floor
		try {
			DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), port);
			socket.send(sendPacket);

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Receives handshakes from ElevatorSubsystem and FloorSubsystem.
	 * This is meant to be run on a stand-alone thread.
	 * @throws IOException
	 */
	private void receiveHandshakes() throws IOException {
		for (int i = 0; i < 2; i++) {
			// Wait for an incoming packet.
			DatagramPacket receivePacket = new DatagramPacket(new byte[10], 10);
			socket.receive(receivePacket);
			
			// Convert packet data to a string.
			String subsystemName = new String(receivePacket.getData()).trim();
			System.out.println("Handshake received: " + subsystemName);
			
			// Notify listeners and save ports.
			if (subsystemName.equals("elevator")) {
				elevatorPort = receivePacket.getPort();
				elevatorHandshake();
			} else {
				floorPort = receivePacket.getPort();
				floorHandshake();
			}
		}
	}
	
	/**
	 * Notify all listeners that an elevator handshake was received.
	 */
	private void elevatorHandshake() {
		for (ISystemSyncListener l : listeners) {
			l.onElevatorHandshake();
		}
	}
	
	/**
	 * Notify all listeners that a floor handshake was received.
	 */
	private void floorHandshake() {
		for (ISystemSyncListener l : listeners) {
			l.onFloorHandshake();
		}
	}

	/**
	 * Start up the simulation by sending config data so GUIs can be built.
	 * @param configData The config data to send.
	 */
	public void sendRuntimeHandshakes(RuntimeConfig configData) {
		sendPacket(elevatorPort, configData.toBytes());
		sendPacket(floorPort, configData.toBytes());
	}
	
	/**
	 * Add a listener.
	 * @param l The listener to add.
	 */
	public void addListener(ISystemSyncListener l) {
		listeners.add(l);
	}

	@Override
	public void run() {
		try {
			receiveHandshakes();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Used by children applications to sync runtime config with the master application.
	 * @param name Name of child.
	 * @return The master application's runtime config.
	 */
	public static RuntimeConfig sendConfigHandshake(String name) {
		// Convert the name to bytes for the packet.
		byte[] data = name.getBytes();
		byte[] responseBytes = new byte[100];
		try (DatagramSocket socket = new DatagramSocket()) {
			// Send a handshake packet to SystemSync.
			socket.send(new DatagramPacket(data, data.length, InetAddress.getLocalHost(), Constants.SYSTEM_SYNC_PORT));
			
			// Wait for a response packet from SystemSync.
			socket.receive(new DatagramPacket(responseBytes, responseBytes.length));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return RuntimeConfig.fromBytes(responseBytes);
	}
}
