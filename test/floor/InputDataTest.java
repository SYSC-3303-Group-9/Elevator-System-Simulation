package floor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import elevator.Direction;

public class InputDataTest {

	@Test
	void equalsShouldBeTrue() {
		// arrange
		InputData obj1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 1);
		InputData obj2 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 1);
		
		// act
		boolean result = obj1.equals(obj2);
		
		// arrange
		assertTrue(result);
	}
	
	@Test
	void equalsShouldBeFalseForNull() {
		// arrange
		InputData obj1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 1);
		InputData obj2 = null;
		
		// act
		boolean result = obj1.equals(obj2);
		
		// arrange
		assertFalse(result);
	}
	
	@Test
	void equalsShouldBeFalseForDifferentTime() {
		// arrange
		InputData obj1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 1);
		InputData obj2 = new InputData(LocalTime.of(1, 1, 1, 2), 1, Direction.UP, 1);
		
		// act
		boolean result = obj1.equals(obj2);
		
		// arrange
		assertFalse(result);
	}
	
	@Test
	void equalsShouldBeFalseForDifferentCurrentFloor() {
		// arrange
		InputData obj1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 1);
		InputData obj2 = new InputData(LocalTime.of(1, 1, 1, 1), 2, Direction.UP, 1);
		
		// act
		boolean result = obj1.equals(obj2);
		
		// arrange
		assertFalse(result);
	}
	
	@Test
	void equalsShouldBeFalseForDifferentDirection() {
		// arrange
		InputData obj1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 1);
		InputData obj2 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.DOWN, 1);
		
		// act
		boolean result = obj1.equals(obj2);
		
		// arrange
		assertFalse(result);
	}
	
	@Test
	void equalsShouldBeFalseForDifferentDestinationFloor() {
		// arrange
		InputData obj1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 1);
		InputData obj2 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2);
		
		// act
		boolean result = obj1.equals(obj2);
		
		// arrange
		assertFalse(result);
	}
}