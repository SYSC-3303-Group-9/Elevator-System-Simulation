package elevator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StreamCorruptedException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElevatorCommandTest {
	ElevatorDoorCommand doorCommand;
	ElevatorMoveCommand moveCommand;
	
	@BeforeEach
	void setUp() throws Exception {
		doorCommand = new ElevatorDoorCommand(1, Fault.TRANSIENT, Direction.WAITING);
		moveCommand = new ElevatorMoveCommand(1, Fault.TRANSIENT, Direction.UP, 7, Direction.WAITING);
	}

	@AfterEach
	void tearDown() throws Exception {
		doorCommand = null;
		moveCommand = null;
	}
	
	@Test
	void testDifferentCommandDifferentBytes() throws IOException {
		byte[] actualBytes1 = doorCommand.toBytes();
		byte[] actualBytes2 = moveCommand.toBytes();
		assertNotEquals(actualBytes1, actualBytes2);
	}
	
	@Test
	void testIllegalCommandIdentifierThrowsException() throws IOException {
		// Create illegal byte array using illegal command identifier
		byte[] wrongBytes1 = doorCommand.toBytes();
		wrongBytes1[0] = (byte) 0x03;
		assertThrows(StreamCorruptedException.class, () -> {ElevatorCommand.fromBytes(wrongBytes1);});
	}
	
	@Test
	void testCorruptedMoveDirectionThrowsException() throws IOException {
		// Create illegal byte array using 'corrupted' move direction
		byte[] wrongBytes1 = moveCommand.toBytes();
		wrongBytes1[11] = (byte) 0x69;
		assertThrows(ClassNotFoundException.class, () -> {ElevatorCommand.fromBytes(wrongBytes1);});
	}
	
	@Test
	void testFromBytesReturnsProperObjects() throws ClassNotFoundException, IOException {
		byte[] actualBytes1 = doorCommand.toBytes();
		byte[] actualBytes2 = moveCommand.toBytes();
		ElevatorCommand actual1 = ElevatorCommand.fromBytes(actualBytes1);
		ElevatorCommand actual2 = ElevatorCommand.fromBytes(actualBytes2);
		assertTrue(actual1 instanceof ElevatorDoorCommand);
		assertTrue(actual2 instanceof ElevatorMoveCommand);
		ElevatorDoorCommand actualDoorCommand = (ElevatorDoorCommand) actual1;
		ElevatorMoveCommand actualMoveCommand = (ElevatorMoveCommand) actual2;
		assertEquals(doorCommand, actualDoorCommand);
		assertEquals(moveCommand, actualMoveCommand);
		assertNotEquals(doorCommand, actualMoveCommand);
		assertNotEquals(moveCommand, actualDoorCommand);
	}

}
