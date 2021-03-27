package elevator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElevatorDoorCommandTest {
	ElevatorDoorCommand expected;
	
	@BeforeEach
	void setUp() throws Exception {
		expected = new ElevatorDoorCommand(1, Fault.TRANSIENT);
	}

	@AfterEach
	void tearDown() throws Exception {
		expected = null;
	}

	@Test
	void testSameCommandsEqual() {
		ElevatorDoorCommand actual = new ElevatorDoorCommand(1, Fault.TRANSIENT);
		assertEquals(expected, actual);
	}
	
	@Test
	void testDifferentCommandsNotEqual() {
		ElevatorDoorCommand actual = new ElevatorDoorCommand(1, Fault.PERMANENT);
		assertNotEquals(expected, actual);
	}
	
	@Test
	void testToAndFromBytes() {
		byte[] elevatorDoorCommandBytes = expected.toBytes();
		ElevatorDoorCommand actual = ElevatorDoorCommand.fromBytes(elevatorDoorCommandBytes);
		ElevatorDoorCommand badActual1 = new ElevatorDoorCommand(1, Fault.PERMANENT);
		ElevatorDoorCommand badActual2 = new ElevatorDoorCommand(3, Fault.NONE);
		assertEquals(expected, actual);
		assertNotEquals(expected, badActual1);
		assertNotEquals(expected, badActual2);
	}

}
