package common;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SystemSyncTest {
	private class DummyListener implements ISystemSyncListener {
		public boolean elevator = false, floor = false;
		
		@Override
		public void onElevatorHandshake() {
			elevator = true;
		}

		@Override
		public void onFloorHandshake() {
			floor = true;
		}
		
	}
	
	private static DatagramSocket socket;
	
	@BeforeAll
	public static void setup() throws IOException {
		socket = new DatagramSocket();
	}
	
	@Test
	public void receiveHandshake_shouldNotifyListeners() throws UnknownHostException, IOException, InterruptedException {
		// arrange
		DummyListener l = new DummyListener();
		SystemSync subject = new SystemSync();
		subject.addListener(l);
		Thread thSubject = new Thread(subject);
		
		// act
		// start thread
		thSubject.start();
		
		// send elevator packet
		byte[] elevatorData = "elevator".getBytes();
		socket.send(new DatagramPacket(elevatorData, elevatorData.length, InetAddress.getLocalHost(), Constants.SYSTEM_SYNC_PORT));
		
		// send floor packet
		byte[] floorData = "floor".getBytes();
		socket.send(new DatagramPacket(floorData, floorData.length, InetAddress.getLocalHost(), Constants.SYSTEM_SYNC_PORT));
		
		// thread should stop now
		thSubject.join();
		
		// assert listener received handshakes
		assertTrue(l.elevator);
		assertTrue(l.floor);
	}
}
