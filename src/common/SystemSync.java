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
	 * Send a packet containing runtime config data.
	 * @param port The port to send to.
	 * @param configData The config data to send.
	 */
	private void sendConfigPacket(int port, RuntimeConfig configData) {
		
		// Construct a DatagramPacket for sending packet to floor
		byte reply[] = configData.toBytes();

		// send reply packet
		try {
			DatagramPacket sendPacket = new DatagramPacket(reply, reply.length, InetAddress.getLocalHost(), port);
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
	 * Start the simulation by sending config data and starting the clock.
	 * TODO: Config data and clock should be separate handshakes to allow the subsystem UIs to start in time.
	 * @param configData The config data to send.
	 */
	public void startSimulation(RuntimeConfig configData) {		
		sendConfigPacket(elevatorPort, configData);
		sendConfigPacket(floorPort, configData);
		
		Clock.startClock();
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
}
