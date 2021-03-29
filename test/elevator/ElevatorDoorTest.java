package elevator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import common.Clock;
import common.Constants;

class ElevatorDoorTest {
	ElevatorDoor door;
	@BeforeEach
	void setUp() throws Exception {
		door = new ElevatorDoor();
	}

	@AfterEach
	void tearDown() throws Exception {
		door = null;
	}

	@Test
	void testTimeElapsesNoFault() {
		long startTime = Clock.getTime();
		assertTrue(door.openClose(Fault.NONE));
		long elapsedTime = (long) ((Clock.getTime() - startTime) * Constants.TIME_MULTIPLIER);
		// Elapsed time should be more than the time it takes to move floors
		assertTrue(elapsedTime >= Constants.DOOR_TIME);
		// Elapsed time should not be that much greater than the time it takes to move floors
		assertTrue(elapsedTime < Constants.DOOR_TIME * 1.1);
	}
	
	@Test
	void testTimeElapsesTransientFault() {
		long startTime = Clock.getTime();
		assertTrue(door.openClose(Fault.TRANSIENT));
		long elapsedTime = (long) ((Clock.getTime() - startTime) * Constants.TIME_MULTIPLIER);
		// Elapsed time should be more than the time it takes to move floors + fault time
		assertTrue(elapsedTime >= (Constants.DOOR_TIME + Constants.TRANSIENT_FAULT_TIME));
		// Elapsed time should not be that much greater than the time it takes to move floors + fault time
		assertTrue(elapsedTime < (Constants.DOOR_TIME + Constants.TRANSIENT_FAULT_TIME) * 1.1);
	}
	
	@Test
	void testTimeElapsesPermanentFault() {
		long startTime = Clock.getTime();
		assertFalse(door.openClose(Fault.PERMANENT));
		long elapsedTime = (long) ((Clock.getTime() - startTime) * Constants.TIME_MULTIPLIER);
		// Elapsed time should be more than the time it takes to move floors + fault time
		assertTrue(elapsedTime >= Constants.PERMANENT_FAULT_TIME);
		// Elapsed time should not be that much greater than the time it takes to move floors + fault time
		assertTrue(elapsedTime < Constants.PERMANENT_FAULT_TIME * 1.1);
	}
}
