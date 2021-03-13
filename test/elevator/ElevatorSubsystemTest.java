package elevator;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import floor.InputData;

public class ElevatorSubsystemTest {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendReceiveSocket;
	Elevator elevator;
	ElevatorSubsystem system;
	Random ran = new Random();
	int upperBound = 25;

	@BeforeEach
	void setup() {
		elevator = new Elevator(ran.nextInt(upperBound), 1);

		try {
			// Construct a datagram socket and bind it to any available
			// port on the local host machine.
			sendReceiveSocket = new DatagramSocket();
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
	}

	@AfterEach
	void tearDown() {
		system = null;
		elevator = null;
	}

	@Test
	void moveToWaiting() {
		elevator = new Elevator(ran.nextInt(upperBound), 1);
		system = new ElevatorSubsystem(elevator);

		// Transition to WAITING state
		system.next();
		assertEquals(ElevatorState.WAITING, system.getState());
	}

	@Test
	void movedElevatorUp() {
		elevator = new Elevator(ran.nextInt(upperBound), 1);
		system = new ElevatorSubsystem(elevator);

		// Transition to WAITING state
		system.next();
		assertEquals(ElevatorState.WAITING, system.getState());

		// Move elevator one floor up
		InputData request = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2);

		// Construct a datagram packet that is to be sent
		try {
			sendPacket = new DatagramPacket(request.toBytes(), request.toBytes().length, InetAddress.getLocalHost(),
					50 + elevator.getId());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Send the datagram packet to the server via the send/receive socket.
		try {
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Transition to MOVINGUP state
		system.next();
		assertEquals(ElevatorState.MOVINGUP, system.getState());
		system.next();

		// Construct a DatagramPacket for receiving packets up
		// to 100 bytes
		byte data[] = new byte[8];
		receivePacket = new DatagramPacket(data, data.length);

		// Receiving Elevator command
		try {
			// Block until a datagram is received via sendReceiveSocket.
			sendReceiveSocket.receive(receivePacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Close the socket.
		sendReceiveSocket.close();

		ElevatorEvent response = ElevatorEvent.fromBytes(data);
		assertEquals(elevator.getId(), response.getElevatorId());
		assertEquals(request.getDestinationFloor(), response.getFloor());

	}

	@Test
	void movedElevatorDown() {
		elevator = new Elevator(ran.nextInt(upperBound), 3);
		system = new ElevatorSubsystem(elevator);

		// Transition to WAITING state
		system.next();
		assertEquals(ElevatorState.WAITING, system.getState());

		// Move elevator one floor down
		InputData request = new InputData(LocalTime.of(1, 1, 1, 1), 3, Direction.DOWN, 2);

		// Construct a datagram packet that is to be sent
		try {
			sendPacket = new DatagramPacket(request.toBytes(), request.toBytes().length, InetAddress.getLocalHost(),
					50 + elevator.getId());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Send the datagram packet to the server via the send/receive socket.
		try {
			sendReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Transition to MOVINGUP state
		system.next();
		assertEquals(ElevatorState.MOVINGDOWN, system.getState());
		system.next();

		// Construct a DatagramPacket for receiving packets up
		// to 100 bytes
		byte data[] = new byte[8];
		receivePacket = new DatagramPacket(data, data.length);

		// Receiving Elevator command
		try {
			// Block until a datagram is received via sendReceiveSocket.
			sendReceiveSocket.receive(receivePacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Close the socket.
		sendReceiveSocket.close();

		ElevatorEvent response = ElevatorEvent.fromBytes(data);
		assertEquals(elevator.getId(), response.getElevatorId());
		assertEquals(request.getDestinationFloor(), response.getFloor());
	}

}
