package common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClockSync implements Runnable {
	private DatagramSocket socket;
	private int elevatorPort, floorPort;
	
	public ClockSync() throws SocketException {
		socket = new DatagramSocket(Constants.CLOCK_SYNC_PORT);
	}
	
	/**
	 * Sync the clock with the master clock.
	 * @param name The name of the current application.
	 */
	public static void sync(String name) {
		// Convert the name to bytes for the packet.
		byte[] data = name.getBytes();
		byte[] responseBytes = new byte[0];
		try (DatagramSocket socket = new DatagramSocket()) {
			// Send a handshake packet to SystemSync.
			socket.send(new DatagramPacket(data, data.length, InetAddress.getLocalHost(), Constants.CLOCK_SYNC_PORT));
			System.out.println("Clock.sync " + name);
			
			// Wait for a response packet from SystemSync.
			socket.receive(new DatagramPacket(responseBytes, responseBytes.length));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		// Now immediately start the clock for this application.
		Clock.startClock();
	}
	
	/**
	 * Runs until both subsystems send their sync packets.
	 */
	@Override
	public void run() {
		try {
			for (int i = 0; i < 2; i++) {
				// Wait for an incoming packet.
				DatagramPacket receivePacket = new DatagramPacket(new byte[10], 10);
				socket.receive(receivePacket);
				
				// Convert packet data to a string.
				String subsystemName = new String(receivePacket.getData()).trim();
				System.out.println("Clock.masterSync received: " + subsystemName);
				
				// Notify listeners and save ports.
				if (subsystemName.equals("elevator")) {
					elevatorPort = receivePacket.getPort();
				} else {
					floorPort = receivePacket.getPort();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Send responses to handshakes and start this application's clock.
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void sendHandshakeReponses() throws UnknownHostException, IOException {
		// Respond to received clock handshakes.
		socket.send(new DatagramPacket(new byte[0], 0, InetAddress.getLocalHost(), elevatorPort));
		socket.send(new DatagramPacket(new byte[0], 0, InetAddress.getLocalHost(), floorPort));
		
		// Now immediately start the clock for this application.
		Clock.startClock();
	}
}
