package elevator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElevatorDoorCommandTest {
	ElevatorDoorCommand expected;
	
	@BeforeEach
	void setUp() throws Exception {
		expected = new ElevatorDoorCommand(1, Fault.TRANSIENT, Direction.WAITING);
	}

	@AfterEach
	void tearDown() throws Exception {
		expected = null;
	}

	@Test
	void testSameCommandsEqual() {
		ElevatorDoorCommand actual = new ElevatorDoorCommand(1, Fault.TRANSIENT, Direction.WAITING);
		assertEquals(expected, actual);
	}
	
	@Test
	void testDifferentCommandsNotEqual() {
		ElevatorDoorCommand actual = new ElevatorDoorCommand(1, Fault.PERMANENT, Direction.WAITING);
		assertNotEquals(expected, actual);
	}
	
	@Test
	void testToAndFromBytes() throws ClassNotFoundException, IOException {
		byte[] elevatorDoorCommandBytes = expected.toBytes();
		ElevatorDoorCommand actual = (ElevatorDoorCommand)ElevatorCommand.fromBytes(elevatorDoorCommandBytes);
		ElevatorDoorCommand badActual1 = new ElevatorDoorCommand(1, Fault.PERMANENT, Direction.WAITING);
		ElevatorDoorCommand badActual2 = new ElevatorDoorCommand(3, Fault.NONE, Direction.WAITING);
		assertEquals(expected, actual);
		assertNotEquals(expected, badActual1);
		assertNotEquals(expected, badActual2);
	}

}
