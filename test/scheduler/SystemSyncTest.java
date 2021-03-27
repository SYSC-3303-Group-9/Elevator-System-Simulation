package scheduler;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import common.Constants;

public class SystemSyncTest {
	SystemSync sync;
	byte floor[] = "floor".getBytes();
	byte elevator[] = "elevator".getBytes();
	DatagramPacket floorReceive, elevatorReceive, floorSendPacket, elevatorSendPacket;
	DatagramSocket FloorsendSocket, ElevatorsendSocket;

	@BeforeEach
	void setup() throws IOException {
		// Construct a datagram socket and bind it to any available
		// port on the local host machine.
		sync = new SystemSync();
		FloorsendSocket = new DatagramSocket();
		ElevatorsendSocket = new DatagramSocket();
		floorReceive = new DatagramPacket(new byte[5], 5);
		elevatorReceive = new DatagramPacket(new byte[5], 5);
	}

	@AfterEach
	void tearDown() {
		sync = null;
	}

	@Test
	void shouldRecieveAndSendBack() throws IOException {

		floorSendPacket = new DatagramPacket(floor, floor.length, InetAddress.getLocalHost(),
				Constants.SYSTEM_SYNC_PORT);
		FloorsendSocket.send(floorSendPacket);
		sync.syncing();
	
		elevatorSendPacket = new DatagramPacket(elevator, elevator.length, InetAddress.getLocalHost(),
				Constants.SYSTEM_SYNC_PORT);
		ElevatorsendSocket.send(elevatorSendPacket);
		sync.syncing();

		

		FloorsendSocket.receive(floorReceive);
		ElevatorsendSocket.receive(elevatorReceive);

		ElevatorsendSocket.close();
		FloorsendSocket.close();

		// arrange
		String floorReply = new String(floorReceive.getData());
		String elevatorReply = new String(elevatorReceive.getData());

		System.out.print("floor" + floorReply);
		System.out.print("elevator" + elevatorReply);

		assertTrue(floorReply.equals(elevatorReply));
	}

}
