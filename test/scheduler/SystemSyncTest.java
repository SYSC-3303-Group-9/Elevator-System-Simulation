package scheduler;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

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
		floorReceive = new DatagramPacket(new byte[30], 30);
		elevatorReceive = new DatagramPacket(new byte[30], 30);
	}

	@AfterEach
	void tearDown() {
		sync = null;
	}

	@Test
	void shouldRecieveAndSendBack() throws IOException, ClassNotFoundException {
		//create packet and send from floor socket
		floorSendPacket = new DatagramPacket(floor, floor.length, InetAddress.getLocalHost(),
				Constants.SYSTEM_SYNC_PORT);
		FloorsendSocket.send(floorSendPacket);
		sync.syncing();
		
		//create packet and send from elevator socket
		elevatorSendPacket = new DatagramPacket(elevator, elevator.length, InetAddress.getLocalHost(),
				Constants.SYSTEM_SYNC_PORT);
		ElevatorsendSocket.send(elevatorSendPacket);
		sync.syncing();

		//receive packets in both floor and elevator sockets
		FloorsendSocket.receive(floorReceive);
		ElevatorsendSocket.receive(elevatorReceive);

		//close both elevator and floor sockets
		ElevatorsendSocket.close();
		FloorsendSocket.close();

		// arrange
		String floorReply = new String(floorReceive.getData());
		String elevatorReply = new String(elevatorReceive.getData());

		//create a RunTimeConfig object and verify 
		RunTimeConfig data = RunTimeConfig.fromBytes(floorReceive.getData());

		assertTrue(floorReply.equals(elevatorReply));
		assertTrue(data.toString().equals("22 4 input.txt"));
	}

}
