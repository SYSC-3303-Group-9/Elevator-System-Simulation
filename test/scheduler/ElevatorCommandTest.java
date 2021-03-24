package scheduler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import elevator.Direction;
import elevator.ElevatorMoveCommand;

class ElevatorCommandTest {
	ElevatorMoveCommand expected;
	@BeforeEach
	void setUp() throws Exception {
		expected = new ElevatorMoveCommand(1, Direction.UP);
	}

	@AfterEach
	void tearDown() throws Exception {
		expected = null;
	}

	@Test
	void testSameCommandsEqual() {
		ElevatorMoveCommand actual = new ElevatorMoveCommand(1, Direction.UP);
		assertEquals(expected, actual);
	}
	
	@Test
	void testDifferentCommandsNotEqual() {
		ElevatorMoveCommand actual = new ElevatorMoveCommand(1, Direction.DOWN);
		assertNotEquals(expected, actual);
	}
	
	@Test
	void SameCommandsDifferentIDNotEqual() {
		ElevatorMoveCommand actual = new ElevatorMoveCommand(2, Direction.UP);
		assertNotEquals(expected, actual);
	}
	
	@Test 
	void DifferentCommandsDifferentIDNotEqual() {
		ElevatorMoveCommand actual = new ElevatorMoveCommand(2, Direction.DOWN);
		assertNotEquals(expected, actual);
	}
	
	@Test
	void testToAndFromBytes() {
		byte[] elevatorCommandBytes = expected.toBytes();
		ElevatorMoveCommand actual = ElevatorMoveCommand.fromBytes(elevatorCommandBytes);
		ElevatorMoveCommand badActual1 = new ElevatorMoveCommand(1, Direction.DOWN);
		ElevatorMoveCommand badActual2 = new ElevatorMoveCommand(2, Direction.UP);
		assertEquals(expected, actual);
		assertNotEquals(expected, badActual1);
		assertNotEquals(expected, badActual2);
	}

}
