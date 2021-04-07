package common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import scheduler.RunTimeConfig;

public class Clock {
	private static long startTime;
	/**
	 * Starts the simulation clock.
	 */
	public static void startClock() {
		startTime = System.currentTimeMillis();
	}
	/**
	 * Returns the time elapsed since the clock started.
	 * @return	Time elapsed.
	 */
	public static long getTime() {
		return System.currentTimeMillis() - startTime;
	}
	
	/**
	 * Sync the clock with the scheduler's clock.
	 * @param name The name of the current application.
	 */
	public static RunTimeConfig sync(String name) {
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
		// Now immediately start the clock for this application.
		Clock.startClock();
		return RunTimeConfig.fromBytes(responseBytes);
	}
}
