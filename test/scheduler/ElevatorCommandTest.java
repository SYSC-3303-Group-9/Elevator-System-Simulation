package scheduler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import elevator.Direction;
import elevator.ElevatorCommand;

class ElevatorCommandTest {
	ElevatorCommand expected;
	@BeforeEach
	void setUp() throws Exception {
		expected = new ElevatorCommand(1, Direction.UP);
	}

	@AfterEach
	void tearDown() throws Exception {
		expected = null;
	}

	@Test
	void testSameCommandsEqual() {
		ElevatorCommand actual = new ElevatorCommand(1, Direction.UP);
		assertEquals(expected, actual);
	}
	
	@Test
	void testDifferentCommandsNotEqual() {
		ElevatorCommand actual = new ElevatorCommand(1, Direction.DOWN);
		assertNotEquals(expected, actual);
	}
	
	@Test
	void SameCommandsDifferentIDNotEqual() {
		ElevatorCommand actual = new ElevatorCommand(2, Direction.UP);
		assertNotEquals(expected, actual);
	}
	
	@Test 
	void DifferentCommandsDifferentIDNotEqual() {
		ElevatorCommand actual = new ElevatorCommand(2, Direction.DOWN);
		assertNotEquals(expected, actual);
	}
	
	@Test
	void testToAndFromBytes() {
		byte[] elevatorCommandBytes = expected.toBytes();
		ElevatorCommand actual = ElevatorCommand.fromBytes(elevatorCommandBytes);
		ElevatorCommand badActual1 = new ElevatorCommand(1, Direction.DOWN);
		ElevatorCommand badActual2 = new ElevatorCommand(2, Direction.UP);
		assertEquals(expected, actual);
		assertNotEquals(expected, badActual1);
		assertNotEquals(expected, badActual2);
	}

}
