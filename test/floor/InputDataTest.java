package floor;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import elevator.Direction;
import elevator.Fault;
public class InputDataTest {

	@Test
	void equalsShouldBeTrue() {
		// arrange
		InputData obj1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.NONE);
		InputData obj2 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.NONE);
		
		// act
		boolean result = obj1.equals(obj2);
		
		// arrange
		assertTrue(result);
	}
	
	@Test
	void equalsShouldBeFalseForNull() {
		// arrange
		InputData obj1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.NONE);
		InputData obj2 = null;
		
		// act
		boolean result = obj1.equals(obj2);
		
		// arrange
		assertFalse(result);
	}
	
	@Test
	void equalsShouldBeFalseForDifferentTime() {
		// arrange
		InputData obj1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.NONE);
		InputData obj2 = new InputData(LocalTime.of(1, 1, 1, 2), 1, Direction.UP, 2, Fault.NONE);
		
		// act
		boolean result = obj1.equals(obj2);
		
		// arrange
		assertFalse(result);
	}
	
	@Test
	void equalsShouldBeFalseForDifferentCurrentFloor() {
		// arrange
		InputData obj1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.NONE);
		InputData obj2 = new InputData(LocalTime.of(1, 1, 1, 1), 2, Direction.UP, 3, Fault.NONE);
		
		// act
		boolean result = obj1.equals(obj2);
		
		// arrange
		assertFalse(result);
	}
	
	@Test
	void equalsShouldBeFalseForDifferentDirection() {
		// arrange
		InputData obj1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.NONE);
		InputData obj2 = new InputData(LocalTime.of(1, 1, 1, 1), 2, Direction.DOWN, 1, Fault.NONE);
		
		// act
		boolean result = obj1.equals(obj2);
		
		// arrange
		assertFalse(result);
	}
	
	@Test
	void equalsShouldBeFalseForDifferentDestinationFloor() {
		// arrange
		InputData obj1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.NONE);
		InputData obj2 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 3, Fault.NONE);
		
		// act
		boolean result = obj1.equals(obj2);
		
		// arrange
		assertFalse(result);
	}
	
	@Test
	void equalsShouldBeFalseForDifferentFault() {
		// arrange
		InputData obj1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.NONE);
		InputData obj2 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.TRANSIENT);
		
		// act
		boolean result = obj1.equals(obj2);
		
		// arrange
		assertFalse(result);
	}
	
	@Test
	void toAndFromBytes() throws IOException, ClassNotFoundException {
		//arrange
		InputData obj1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.NONE);
		InputData obj2 = new InputData(LocalTime.of(1, 1, 1, 1), 2, Direction.DOWN, 1, Fault.NONE);
		InputData obj3 = new InputData(LocalTime.of(13, 43, 27, 5220314), 3, Direction.UP, 5, Fault.NONE);
		
		
		//act
		byte toByte1 [] = obj1.toBytes();
		byte toByte2 [] = obj2.toBytes();
		byte toByte3 [] = obj3.toBytes();
		InputData toInputData1 = InputData.fromBytes(toByte1);
		InputData toInputData2 = InputData.fromBytes(toByte2);
		InputData toInputData3 = InputData.fromBytes(toByte3);
		
		//assert
		assertEquals(toInputData1, obj1);
		assertEquals(toInputData2, obj2);
		assertEquals(toInputData3, obj3);
	}
	
	@Test
	void toAndFromBytes_longBuffer() throws IOException, ClassNotFoundException {
		//arrange
		InputData obj1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.NONE);
		
		//act
		byte toByte1 [] = obj1.toBytes();
		byte[] longBuffer = Arrays.copyOf(toByte1, toByte1.length + 50);
		InputData toInputData1 = InputData.fromBytes(longBuffer);
		
		//arrange
		assertEquals(obj1, toInputData1);
	}
	
	@Test
	void differentToAndFromBytes() throws IOException, ClassNotFoundException {
		//arrange
		InputData obj1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.NONE);
		InputData obj2 = new InputData(LocalTime.of(1, 1, 1, 1), 2, Direction.DOWN, 1, Fault.NONE);
		InputData obj3 = new InputData(LocalTime.of(13, 43, 27, 5220314), 3, Direction.UP, 5, Fault.NONE);
		
		//act
		byte toByte1 [] = obj1.toBytes();
		byte toByte2 [] = obj2.toBytes();
		byte toByte3 [] = obj3.toBytes();
		InputData toInputData1 = InputData.fromBytes(toByte1);
		InputData toInputData2 = InputData.fromBytes(toByte2);
		InputData toInputData3 = InputData.fromBytes(toByte3);
		
		//arrange
		assertNotEquals(obj1, toInputData2);
		assertNotEquals(obj1, toInputData3);
		assertNotEquals(obj2, toInputData1);
		assertNotEquals(obj2, toInputData3);
		assertNotEquals(obj3, toInputData1);
		assertNotEquals(obj3, toInputData2);	
	}
}
