package elevator;

import org.junit.jupiter.api.Test;

import common.Constants;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

public class ElevatorSubsystemTest {
	private static DatagramSocket sendReceiveSocket;

	DatagramPacket sendPacket, receivePacket;
	
	ElevatorMotor elevator;
	ElevatorSubsystem system;
	Random ran = new Random();
	int upperBound = 100;
	
	@BeforeAll
	public static void setup() throws IOException {
		sendReceiveSocket = new DatagramSocket(Constants.ELEVATOR_EVENT_RECEIVER_PORT);
	}

	@AfterEach
	void tearDown() {
		system = null;
		elevator = null;
	}

	@Test
	void moveToWaiting() {
		elevator = new ElevatorMotor();
		system = new ElevatorSubsystem(elevator, 1, ran.nextInt(upperBound));

		// Transition to WAITING state
		system.next();
		assertEquals(ElevatorState.WAITING, system.getState());
	}

	@Test
	void movedElevatorUp() throws IOException {
		elevator = new ElevatorMotor();
		system = new ElevatorSubsystem(elevator, 1, ran.nextInt(upperBound));

		// Transition to WAITING state
		system.next();
		assertEquals(ElevatorState.WAITING, system.getState());

		// Move elevator one floor up
		ElevatorMoveCommand request = new ElevatorMoveCommand(system.getId(), Fault.NONE, Direction.UP, 2);

		// Construct a datagram packet that is to be sent
		sendPacket = new DatagramPacket(request.toBytes(), request.toBytes().length, InetAddress.getLocalHost(),
				Constants.ELEVATOR_BASE_PORT + system.getId());

		// Send the datagram packet to the server via the send/receive socket.
		sendReceiveSocket.send(sendPacket);

		// Transition to MOVINGUP state
		system.next();
		assertEquals(ElevatorState.MOVING_UP, system.getState());
		system.next();

		// Construct a DatagramPacket for receiving packets up
		// to 100 bytes
		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);

		// Receiving Elevator command
		// Block until a datagram is received via sendReceiveSocket.
		sendReceiveSocket.receive(receivePacket);

		ElevatorEvent response = ElevatorEvent.fromBytes(data);
		assertEquals(system.getId(), response.getElevatorId());
		assertEquals(request.getDestinationFloor(), response.getFloor());

	}

	@Test
	void movedElevatorDown() throws IOException {
		elevator = new ElevatorMotor();
		system = new ElevatorSubsystem(elevator, 3, ran.nextInt(upperBound));

		// Transition to WAITING state
		system.next();
		assertEquals(ElevatorState.WAITING, system.getState());

		// Move elevator one floor down
		ElevatorMoveCommand request = new ElevatorMoveCommand(system.getId(), Fault.NONE, Direction.DOWN, 2);

		// Construct a datagram packet that is to be sent
		sendPacket = new DatagramPacket(request.toBytes(), request.toBytes().length, InetAddress.getLocalHost(),
				Constants.ELEVATOR_BASE_PORT + system.getId());

		// Send the datagram packet to the server via the send/receive socket.
		sendReceiveSocket.send(sendPacket);

		// Transition to MOVINGUP state
		system.next();
		assertEquals(ElevatorState.MOVING_DOWN, system.getState());
		system.next();

		// Construct a DatagramPacket for receiving packets up
		// to 100 bytes
		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);

		// Receiving Elevator command
		// Block until a datagram is received via sendReceiveSocket.
		sendReceiveSocket.receive(receivePacket);

		ElevatorEvent response = ElevatorEvent.fromBytes(data);
		assertEquals(system.getId(), response.getElevatorId());
		assertEquals(request.getDestinationFloor(), response.getFloor());
	}

	@Test
	void permanentFaultElevator() throws IOException {
		elevator = new ElevatorMotor();
		system = new ElevatorSubsystem(elevator, 3, ran.nextInt(upperBound));

		// Transition to WAITING state
		system.next();
		assertEquals(ElevatorState.WAITING, system.getState());

		// Move elevator one floor down
		ElevatorMoveCommand request = new ElevatorMoveCommand(system.getId(), Fault.PERMANENT, Direction.DOWN, 2);

		// Construct a datagram packet that is to be sent
		sendPacket = new DatagramPacket(request.toBytes(), request.toBytes().length, InetAddress.getLocalHost(),
				Constants.ELEVATOR_BASE_PORT + system.getId());

		// Send the datagram packet to the server via the send/receive socket.
		sendReceiveSocket.send(sendPacket);

		// Transition to MOVINGDOWN state
		system.next();
		assertEquals(ElevatorState.PERMANENT_FAULT, system.getState());
		
		// Transition to DISABLED state
		system.next();
		assertEquals(ElevatorState.DISABLED, system.getState());

		// Transition to FINAL state
		system.next();
		assertEquals(ElevatorState.FINAL, system.getState());
	}

	@Test
	void openCloseElevatorDoors() throws IOException {
		elevator = new ElevatorMotor();
		system = new ElevatorSubsystem(elevator, 3, ran.nextInt(upperBound));

		// Transition to WAITING state
		system.next();
		assertEquals(ElevatorState.WAITING, system.getState());

		// Move elevator one floor down
		ElevatorDoorCommand request = new ElevatorDoorCommand(system.getId(), Fault.NONE);

		// Construct a datagram packet that is to be sent
		sendPacket = new DatagramPacket(request.toBytes(), request.toBytes().length, InetAddress.getLocalHost(),
				Constants.ELEVATOR_BASE_PORT + system.getId());

		// Send the datagram packet to the server via the send/receive socket.
		sendReceiveSocket.send(sendPacket);

		// Transition to OPENING_CLOSING_DOORS state
		system.next();
		assertEquals(ElevatorState.OPENING_CLOSING_DOORS, system.getState());

		// Transition to WAITING state
		system.next();
		assertEquals(ElevatorState.WAITING, system.getState());
	}
	
	@Test
	void transientFaultElevator() throws IOException {
		elevator = new ElevatorMotor();
		system = new ElevatorSubsystem(elevator, 3, ran.nextInt(upperBound));

		// Transition to WAITING state
		system.next();
		assertEquals(ElevatorState.WAITING, system.getState());

		// Move elevator one floor down
		ElevatorDoorCommand request = new ElevatorDoorCommand(system.getId(), Fault.TRANSIENT);

		// Construct a datagram packet that is to be sent
		sendPacket = new DatagramPacket(request.toBytes(), request.toBytes().length, InetAddress.getLocalHost(),
				Constants.ELEVATOR_BASE_PORT + system.getId());

		// Send the datagram packet to the server via the send/receive socket.
		sendReceiveSocket.send(sendPacket);

		// Transition to TRANSIENT_FAULT state
		system.next();
		assertEquals(ElevatorState.TRANSIENT_FAULT, system.getState());
		
		// Transition to OPENING_CLOSING_DOORS state
		system.next();
		assertEquals(ElevatorState.OPENING_CLOSING_DOORS, system.getState());

		// Transition to WAITING state
		system.next();
		assertEquals(ElevatorState.WAITING, system.getState());
	}

}
