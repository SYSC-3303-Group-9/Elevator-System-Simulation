package scheduler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

public class RunTimeConfigTest {

	@Test
	public void equalsShouldBeTrue() {
		// arrange
		RunTimeConfig obj1 = new RunTimeConfig(11, 4, "test.txt");
		RunTimeConfig obj2 = new RunTimeConfig(11, 4, "test.txt");

		// act
		assertEquals(obj1, obj2);
	}

	@Test
	void equalsShouldBeFalseForNull() {
		// arrange
		RunTimeConfig obj1 = new RunTimeConfig(11, 4, "test.txt");
		RunTimeConfig obj2 = null;

		// act
		boolean result = obj1.equals(obj2);

		// arrange
		assertFalse(result);
	}

	@Test
	void equalsShouldBeFalseForDifferentElevatorNum() {
		// arrange
		RunTimeConfig obj1 = new RunTimeConfig(11, 4, "test1.txt");
		RunTimeConfig obj2 = new RunTimeConfig(11, 6, "test1.txt");

		// act
		boolean result = obj1.equals(obj2);

		// arrange
		assertFalse(result);
	}

	@Test
	void equalsShouldBeFalseForDifferentFloor() {
		// arrange
		RunTimeConfig obj1 = new RunTimeConfig(10, 4, "test1.txt");
		RunTimeConfig obj2 = new RunTimeConfig(13, 4, "test1.txt");

		// act
		assertNotEquals(obj1, obj2);
	}

	@Test
	void equalsShouldBeFalseForDifferentFilePath() {
		// arrange
		RunTimeConfig obj1 = new RunTimeConfig(11, 4, "test1.txt");
		RunTimeConfig obj2 = new RunTimeConfig(11, 4, "test2.txt");

		// act
		assertNotEquals(obj1, obj2);
	}

	@Test
	void toAndFromBytes(){
		// arrange
		RunTimeConfig obj1 = new RunTimeConfig(11, 4, "test1.txt");
		RunTimeConfig obj2 = new RunTimeConfig(12, 5, "test2.txt");
		RunTimeConfig obj3 = new RunTimeConfig(13, 6, "test3.txt");

		// act
		byte toByte1[] = obj1.toBytes();
		byte toByte2[] = obj2.toBytes();
		byte toByte3[] = obj3.toBytes();
		RunTimeConfig toInputData1 = RunTimeConfig.fromBytes(toByte1);
		RunTimeConfig toInputData2 = RunTimeConfig.fromBytes(toByte2);
		RunTimeConfig toInputData3 = RunTimeConfig.fromBytes(toByte3);

		// assert
		assertEquals(toInputData1, obj1);
		assertEquals(toInputData2, obj2);
		assertEquals(toInputData3, obj3);
	}

	@Test
	void toAndFromBytes_longBuffer() {
		// arrange
		RunTimeConfig obj1 = new RunTimeConfig(11, 4, "test1.txt");

		// act
		byte toByte1[] = obj1.toBytes();
		byte[] longBuffer = Arrays.copyOf(toByte1, toByte1.length + 50);
		RunTimeConfig config = RunTimeConfig.fromBytes(longBuffer);

		// arrange
		assertEquals(obj1, config);
	}

	@Test
	void differentToAndFromBytes() {
		// arrange
		RunTimeConfig obj1 = new RunTimeConfig(11, 4, "test1.txt");
		RunTimeConfig obj2 = new RunTimeConfig(12, 5, "test2.txt");
		RunTimeConfig obj3 = new RunTimeConfig(13, 6, "test3.txt");

		// act
		byte toByte1[] = obj1.toBytes();
		byte toByte2[] = obj2.toBytes();
		byte toByte3[] = obj3.toBytes();
		RunTimeConfig toInputData1 = RunTimeConfig.fromBytes(toByte1);
		RunTimeConfig toInputData2 = RunTimeConfig.fromBytes(toByte2);
		RunTimeConfig toInputData3 = RunTimeConfig.fromBytes(toByte3);

		// arrange
		assertNotEquals(obj1, toInputData2);
		assertNotEquals(obj1, toInputData3);
		assertNotEquals(obj2, toInputData1);
		assertNotEquals(obj2, toInputData3);
		assertNotEquals(obj3, toInputData1);
		assertNotEquals(obj3, toInputData2);
	}
}
