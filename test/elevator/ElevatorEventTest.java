package elevator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElevatorEventTest {
	ElevatorEvent event1;
	ElevatorEvent event2;
	@BeforeEach
	void setUp() throws Exception {
		event1 = new ElevatorEvent(6, 1, false, true);
		event2 = new ElevatorEvent(6, 1, true, false);
	}

	@AfterEach
	void tearDown() throws Exception {
		event1 = null;
	}

	@Test
	void testToAndFromBytes() {
		byte[] elevatorEventBytes1 = event1.toBytes();
		ElevatorEvent testResult1 = ElevatorEvent.fromBytes(elevatorEventBytes1);
		
		byte[] elevatorEventBytes2 = event2.toBytes();
		ElevatorEvent testResult2 = ElevatorEvent.fromBytes(elevatorEventBytes2);
		
		assertEquals(event1, testResult1);
		assertEquals(event2, testResult2);
	}
	
	@Test
	void testDifferentEventsNotEqual() {
		byte[] differentEvent1 = new ElevatorEvent(4, 3, false, false).toBytes();
		byte[] differentEvent2 = new ElevatorEvent(4, 3, true, true).toBytes();
		
		byte[] event1Bytes = event1.toBytes();
		byte[] event2Bytes = event2.toBytes();
		
		assertNotEquals(event1Bytes, differentEvent1);
		assertNotEquals(event2Bytes, differentEvent2);
	}

}
