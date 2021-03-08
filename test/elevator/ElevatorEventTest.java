package elevator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElevatorEventTest {
	ElevatorEvent event1;
	@BeforeEach
	void setUp() throws Exception {
		event1 = new ElevatorEvent(6, 1);
	}

	@AfterEach
	void tearDown() throws Exception {
		event1 = null;
	}

	@Test
	void testToAndFromBytes() {
		byte[] elevatorEventBytes = event1.toBytes();
		ElevatorEvent testResult = ElevatorEvent.fromBytes(elevatorEventBytes);
		assertEquals(event1, testResult);
	}
	
	@Test
	void testDifferentEventsNotEqual() {
		byte[] differentEvent = new ElevatorEvent(4, 3).toBytes();
		byte[] event1Bytes = event1.toBytes();
		assertNotEquals(event1Bytes, differentEvent);
	}

}
