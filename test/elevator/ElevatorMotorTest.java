package elevator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import common.Clock;
import common.Constants;

class ElevatorMotorTest {
	ElevatorMotor motor;
	@BeforeEach
	void setUp() throws Exception {
		motor = new ElevatorMotor();
	}

	@AfterEach
	void tearDown() throws Exception {
		motor = null;
	}

	@Test
	void testTimeElapsesNoFault() {
		long startTime = Clock.getTime();
		assertTrue(motor.move(Fault.NONE));
		long elapsedTime = (long) ((Clock.getTime() - startTime) * Constants.TIME_MULTIPLIER);
		// Elapsed time should be more than the time it takes to move floors
		assertTrue(elapsedTime >= Constants.MOVE_TIME);
		// Elapsed time should not be that much greater than the time it takes to move floors
		assertTrue(elapsedTime < (Constants.MOVE_TIME + 50));
	}
	
	@Test
	void testTimeElapsesTransientFault() {
		long startTime = Clock.getTime();
		assertTrue(motor.move(Fault.TRANSIENT));
		long elapsedTime = (long) ((Clock.getTime() - startTime) * Constants.TIME_MULTIPLIER);
		// Elapsed time should be more than the time it takes to move floors + fault time
		assertTrue(elapsedTime >= (Constants.MOVE_TIME + Constants.TRANSIENT_FAULT_TIME));
		// Elapsed time should not be that much greater than the time it takes to move floors + fault time
		assertTrue(elapsedTime < (Constants.MOVE_TIME + Constants.TRANSIENT_FAULT_TIME + 50));
	}
	
	@Test
	void testTimeElapsesPermanentFault() {
		long startTime = Clock.getTime();
		assertFalse(motor.move(Fault.PERMANENT));
		long elapsedTime = (long) ((Clock.getTime() - startTime) * Constants.TIME_MULTIPLIER);
		// Elapsed time should be more than the time it takes to move floors + fault time
		assertTrue(elapsedTime >= Constants.PERMANENT_FAULT_TIME);
		// Elapsed time should not be that much greater than the time it takes to move floors + fault time
		assertTrue(elapsedTime < Constants.PERMANENT_FAULT_TIME + 50);
	}
}