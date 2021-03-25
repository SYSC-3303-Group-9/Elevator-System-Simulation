package elevator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import elevator.Direction;
import elevator.ElevatorMoveCommand;

class ElevatorMoveCommandTest {
	ElevatorMoveCommand expected;
	@BeforeEach
	void setUp() throws Exception {
		expected = new ElevatorMoveCommand(1, Fault.TRANSIENT, Direction.UP);
	}

	@AfterEach
	void tearDown() throws Exception {
		expected = null;
	}

	@Test
	void testSameCommandsEqual() {
		ElevatorMoveCommand actual = new ElevatorMoveCommand(1, Fault.TRANSIENT, Direction.UP);
		assertEquals(expected, actual);
	}
	
	@Test
	void testDifferentCommandsNotEqual() {
		ElevatorMoveCommand actual = new ElevatorMoveCommand(1, Fault.TRANSIENT, Direction.DOWN);
		assertNotEquals(expected, actual);
	}
	
	@Test
	void testSameCommandsDifferentIDNotEqual() {
		ElevatorMoveCommand actual = new ElevatorMoveCommand(2, Fault.TRANSIENT, Direction.UP);
		assertNotEquals(expected, actual);
	}
	
	@Test 
	void testDifferentCommandsDifferentIDNotEqual() {
		ElevatorMoveCommand actual = new ElevatorMoveCommand(2, Fault.TRANSIENT, Direction.DOWN);
		assertNotEquals(expected, actual);
	}
	
	@Test
	void testToAndFromBytes() {
		byte[] elevatorMoveCommandBytes = expected.toBytes();
		ElevatorMoveCommand actual = ElevatorMoveCommand.fromBytes(elevatorMoveCommandBytes);
		ElevatorMoveCommand badActual1 = new ElevatorMoveCommand(1, Fault.TRANSIENT, Direction.DOWN);
		ElevatorMoveCommand badActual2 = new ElevatorMoveCommand(2, Fault.TRANSIENT, Direction.UP);
		assertEquals(expected, actual);
		assertNotEquals(expected, badActual1);
		assertNotEquals(expected, badActual2);
	}

}
