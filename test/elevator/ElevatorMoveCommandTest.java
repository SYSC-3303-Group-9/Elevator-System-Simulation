package elevator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElevatorMoveCommandTest {
	ElevatorMoveCommand expected;
	@BeforeEach
	void setUp() throws Exception {
		expected = new ElevatorMoveCommand(1, Fault.TRANSIENT, Direction.UP, 7, Direction.WAITING);
	}

	@AfterEach
	void tearDown() throws Exception {
		expected = null;
	}

	@Test
	void testSameCommandsEqual() {
		ElevatorMoveCommand actual = new ElevatorMoveCommand(1, Fault.TRANSIENT, Direction.UP, 7, Direction.WAITING);
		assertEquals(expected, actual);
	}
	
	@Test
	void testDifferentCommandsNotEqual() {
		ElevatorMoveCommand actual = new ElevatorMoveCommand(2, Fault.TRANSIENT, Direction.DOWN, 7, Direction.WAITING);
		assertNotEquals(expected, actual);
	}
	
	@Test
	void testSameCommandsDifferentIDNotEqual() {
		ElevatorMoveCommand actual = new ElevatorMoveCommand(2, Fault.TRANSIENT, Direction.UP, 7, Direction.WAITING);
		assertNotEquals(expected, actual);
	}
	
	@Test 
	void testDifferentCommandsDifferentIDNotEqual() {
		ElevatorMoveCommand actual = new ElevatorMoveCommand(2, Fault.TRANSIENT, Direction.DOWN, 7, Direction.WAITING);
		assertNotEquals(expected, actual);
	}
	
	@Test
	void testDifferentDestinationFloorsNotEqual() {
		ElevatorMoveCommand actual = new ElevatorMoveCommand(1, Fault.TRANSIENT, Direction.UP, 9, Direction.WAITING);
		assertNotEquals(expected, actual);
	}
	
	@Test
	void testHasSameDestination() {
		ElevatorMoveCommand actual1 = new ElevatorMoveCommand(1, Fault.NONE, Direction.UP, 7, Direction.WAITING);
		ElevatorMoveCommand actual2 = new ElevatorMoveCommand(1, Fault.TRANSIENT, Direction.UP, 9, Direction.WAITING);
		assertTrue(expected.hasSameDestination(actual1));
		assertFalse(expected.hasSameDestination(actual2));
	}
	
	@Test
	void testToAndFromBytes() throws ClassNotFoundException, IOException {
		byte[] elevatorMoveCommandBytes = expected.toBytes();
		ElevatorMoveCommand actual = (ElevatorMoveCommand)ElevatorCommand.fromBytes(elevatorMoveCommandBytes);
		ElevatorMoveCommand badActual1 = new ElevatorMoveCommand(1, Fault.TRANSIENT, Direction.DOWN, 7, Direction.WAITING);
		ElevatorMoveCommand badActual2 = new ElevatorMoveCommand(2, Fault.TRANSIENT, Direction.UP, 7, Direction.WAITING);
		assertEquals(expected, actual);
		assertNotEquals(expected, badActual1);
		assertNotEquals(expected, badActual2);
	}

}
