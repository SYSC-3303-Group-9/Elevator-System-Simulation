package elevator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElevatorCommandTest {
	ElevatorCommand expected1;
	ElevatorCommand expected2;
	
	@BeforeEach
	void setUp() throws Exception {
		expected1 = new ElevatorCommand(0, 0);
		expected2 = new ElevatorCommand(1, 1);
	}

	@AfterEach
	void tearDown() throws Exception {
		expected1 = null;
		expected2 = null;
	}

	@Test
	void testSameIDSameFaultEqual() {
		ElevatorCommand actual1 = new ElevatorCommand(0, 0);
		ElevatorCommand actual2 = new ElevatorCommand(1, 1);
		assertEquals(expected1, actual1);
		assertEquals(expected2, actual2);
	}
	
	@Test
	void testDifferentIDDiffereentFaultNotEquals() {
		ElevatorCommand actual1 = new ElevatorCommand(0, 0);
		ElevatorCommand actual2 = new ElevatorCommand(1, 1);
		assertNotEquals(expected1, actual2);
		assertNotEquals(expected2, actual1);
	}
	
	@Test
	void testDifferentIDSameFaultNotEquals() {
		ElevatorCommand actual1 = new ElevatorCommand(1, 0);
		ElevatorCommand actual2 = new ElevatorCommand(0, 1);
		assertNotEquals(expected1, actual1);
		assertNotEquals(expected1, actual2);
	}
	
	@Test
	void testSameIDDifferentFaultNotEquals() {
		ElevatorCommand actual1 = new ElevatorCommand(0, 1);
		ElevatorCommand actual2 = new ElevatorCommand(1, 0);
		assertNotEquals(expected1, actual1);
		assertNotEquals(expected2, actual2);
	}
	
	@Test
	void testToAndFromBytes() {
		byte[] actualBytes1 = expected1.toBytes();
		byte[] actualBytes2 = expected2.toBytes();
		assertNotEquals(actualBytes1, actualBytes2);
		ElevatorCommand actual1 = ElevatorCommand.fromBytes(actualBytes1);
		ElevatorCommand actual2 = ElevatorCommand.fromBytes(actualBytes2);
		assertNotEquals(actual1, actual2);
		assertEquals(expected1, actual1);
		assertEquals(expected2, actual2);
		assertNotEquals(expected1, actual2);
		assertNotEquals(expected2, actual1);
	}

}
