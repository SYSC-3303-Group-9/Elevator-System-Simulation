package scheduler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import elevator.Direction;

class ElevatorCommandTest {
	ElevatorCommand expected;
	@BeforeEach
	void setUp() throws Exception {
		expected = new ElevatorCommand(Direction.UP);
	}

	@AfterEach
	void tearDown() throws Exception {
		expected = null;
	}

	@Test
	void testSameCommandsEqual() {
		ElevatorCommand actual = new ElevatorCommand(Direction.UP);
		assertEquals(expected, actual);
	}
	
	@Test
	void testDifferentCommandsNotEqual() {
		ElevatorCommand actual = new ElevatorCommand(Direction.DOWN);
		assertNotEquals(expected, actual);
	}
	
	@Test
	void testToAndFromBytes() {
		byte[] elevatorCommandBytes = expected.toBytes();
		ElevatorCommand actual = ElevatorCommand.fromBytes(elevatorCommandBytes);
		ElevatorCommand badActual = new ElevatorCommand(Direction.DOWN);
		assertEquals(expected, actual);
		assertNotEquals(expected, badActual);
	}

}
