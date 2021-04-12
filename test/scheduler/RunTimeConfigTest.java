package scheduler;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import common.RuntimeConfig;

import java.util.Arrays;

public class RunTimeConfigTest {

	@Test
	public void equalsShouldBeTrue() {
		// arrange
		RuntimeConfig obj1 = new RuntimeConfig(11, 4, "test.txt");
		RuntimeConfig obj2 = new RuntimeConfig(11, 4, "test.txt");

		// act
		assertEquals(obj1, obj2);
	}

	@Test
	void equalsShouldBeFalseForNull() {
		// arrange
		RuntimeConfig obj1 = new RuntimeConfig(11, 4, "test.txt");
		RuntimeConfig obj2 = null;

		// act
		assertNotEquals(obj1, obj2);
	}

	@Test
	void equalsShouldBeFalseForDifferentElevatorNum() {
		// arrange
		RuntimeConfig obj1 = new RuntimeConfig(11, 4, "test1.txt");
		RuntimeConfig obj2 = new RuntimeConfig(11, 6, "test1.txt");

		// act
		assertNotEquals(obj1, obj2);
	}

	@Test
	void equalsShouldBeFalseForDifferentFloor() {
		// arrange
		RuntimeConfig obj1 = new RuntimeConfig(10, 4, "test1.txt");
		RuntimeConfig obj2 = new RuntimeConfig(13, 4, "test1.txt");

		// act
		assertNotEquals(obj1, obj2);
	}

	@Test
	void equalsShouldBeFalseForDifferentFilePath() {
		// arrange
		RuntimeConfig obj1 = new RuntimeConfig(11, 4, "test1.txt");
		RuntimeConfig obj2 = new RuntimeConfig(11, 4, "test2.txt");

		// act
		assertNotEquals(obj1, obj2);
	}

	@Test
	void toAndFromBytes() {
		// arrange
		RuntimeConfig obj1 = new RuntimeConfig(11, 4, "test1.txt");
		RuntimeConfig obj2 = new RuntimeConfig(12, 5, "test2.txt");
		RuntimeConfig obj3 = new RuntimeConfig(13, 6, "test3.txt");

		// act
		byte toByte1[] = obj1.toBytes();
		byte toByte2[] = obj2.toBytes();
		byte toByte3[] = obj3.toBytes();
		RuntimeConfig toInputData1 = RuntimeConfig.fromBytes(toByte1);
		RuntimeConfig toInputData2 = RuntimeConfig.fromBytes(toByte2);
		RuntimeConfig toInputData3 = RuntimeConfig.fromBytes(toByte3);

		// assert
		assertEquals(toInputData1, obj1);
		assertEquals(toInputData2, obj2);
		assertEquals(toInputData3, obj3);
	}

	@Test
	void toAndFromBytes_longBuffer() {
		// arrange
		RuntimeConfig obj1 = new RuntimeConfig(11, 4, "test1.txt");

		// act
		byte toByte1[] = obj1.toBytes();
		byte[] longBuffer = Arrays.copyOf(toByte1, toByte1.length + 50);
		RuntimeConfig config = RuntimeConfig.fromBytes(longBuffer);

		// arrange
		assertEquals(obj1, config);
	}

	@Test
	void differentToAndFromBytes() {
		// arrange
		RuntimeConfig obj1 = new RuntimeConfig(11, 4, "test1.txt");
		RuntimeConfig obj2 = new RuntimeConfig(12, 5, "test2.txt");
		RuntimeConfig obj3 = new RuntimeConfig(13, 6, "test3.txt");

		// act
		byte toByte1[] = obj1.toBytes();
		byte toByte2[] = obj2.toBytes();
		byte toByte3[] = obj3.toBytes();
		RuntimeConfig toInputData1 = RuntimeConfig.fromBytes(toByte1);
		RuntimeConfig toInputData2 = RuntimeConfig.fromBytes(toByte2);
		RuntimeConfig toInputData3 = RuntimeConfig.fromBytes(toByte3);

		// arrange
		assertNotEquals(obj1, toInputData2);
		assertNotEquals(obj1, toInputData3);
		assertNotEquals(obj2, toInputData1);
		assertNotEquals(obj2, toInputData3);
		assertNotEquals(obj3, toInputData1);
		assertNotEquals(obj3, toInputData2);
	}
}
