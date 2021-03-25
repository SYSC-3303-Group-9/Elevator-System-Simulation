package elevator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElevatorCommandTest {
	ElevatorDoorCommand doorCommand;
	ElevatorMoveCommand moveCommand;
	
	@BeforeEach
	void setUp() throws Exception {
		doorCommand = new ElevatorDoorCommand(1, Fault.TRANSIENT, DoorState.OPEN);
		moveCommand = new ElevatorMoveCommand(1, Fault.TRANSIENT, Direction.UP);
	}

	@AfterEach
	void tearDown() throws Exception {
		doorCommand = null;
		moveCommand = null;
	}
	
	@Test
	void testDifferentCommandDifferentBytes() {
		byte[] actualBytes1 = doorCommand.toBytes();
		byte[] actualBytes2 = moveCommand.toBytes();
		assertNotEquals(actualBytes1, actualBytes2);
	}
	
	@Test
	void testIllegalCommandIdentifierThrowsException() {
		// Create illegal byte array using illegal command identifier
		byte[] wrongBytes1 = doorCommand.toBytes();
		wrongBytes1[0] = (byte) 0x03;
		assertThrows(IllegalArgumentException.class, () -> {ElevatorCommand.fromBytes(wrongBytes1);});
	}
	
	@Test
	void testCorruptedMoveDirectionThrowsException() {
		// Create illegal byte array using 'corrupted' move direction
		byte[] wrongBytes1 = moveCommand.toBytes();
		wrongBytes1[11] = (byte) 0x69;
		assertThrows(IllegalArgumentException.class, () -> {ElevatorCommand.fromBytes(wrongBytes1);});
	}
	
	@Test
	void testCorruptedDoorStateThrowsException() {
		// Create illegal byte array using 'corrupted' move direction
		byte[] wrongBytes1 = doorCommand.toBytes();
		wrongBytes1[11] = (byte) 0x69;
		assertThrows(IllegalArgumentException.class, () -> {ElevatorCommand.fromBytes(wrongBytes1);});
	}
	
	@Test
	void testFromBytesReturnsProperObjects() {
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
