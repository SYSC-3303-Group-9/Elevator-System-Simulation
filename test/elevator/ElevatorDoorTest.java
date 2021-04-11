package elevator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import common.Clock;
import common.Constants;
import elevator.gui.Door;

class ElevatorDoorTest {
	ElevatorDoor door;
	public static final int TOTAL_DOOR_TIME = 2 * Constants.DOOR_TIME + Constants.LOADING_TIME;
	@BeforeEach
	void setUp() throws Exception {
		door = new ElevatorDoor(new Door());
	}

	@AfterEach
	void tearDown() throws Exception {
		door = null;
	}

	@Test
	void testTimeElapsesNoFault() {
		long startTime = Clock.getTime();
		door.openClose();
		long elapsedTime = (long) ((Clock.getTime() - startTime) * Constants.TIME_MULTIPLIER);
		// Elapsed time should be more than the time it takes to move floors
		assertTrue(elapsedTime >= TOTAL_DOOR_TIME);
		// Elapsed time should not be that much greater than the time it takes to move floors
		assertTrue(elapsedTime < TOTAL_DOOR_TIME * 1.1);
	}
}
