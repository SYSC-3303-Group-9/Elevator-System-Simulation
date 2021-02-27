package elevator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import floor.InputData;

public class ElevatorTest {
	@Test
	public void getLocation_shouldFormatData() {
		// arrange
		Elevator subject = new Elevator(0);
		InputData input = new InputData(LocalTime.of(1, 1), 1, Direction.UP, 2);
		
		// act
		String output = subject.getLocation(input);
		
		// assert
		assertEquals("[Elevator 0] 1 UP 2", output);
	}
}
