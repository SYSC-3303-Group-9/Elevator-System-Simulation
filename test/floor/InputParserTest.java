package floor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import elevator.Direction;

public class InputParserTest {
	
	@Test
	void parseShouldGetMultipleInputData() {
		// arrange
		InputParser parser = new InputParser();
		
		String text = "14:05:15.0 2 Up 4\n"
				+ "05:02:01.1 3 Down 1\n";
		
		BufferedReader reader = new BufferedReader(new StringReader(text));
		
		// act
		List<InputData> data = parser.parse(reader);
		
		// assert
		assertEquals(2, data.size());
		
		// check first input data
		InputData expected1 = new InputData(LocalTime.of(14, 5, 15, 0), 2, Direction.UP, 4);
		assertEquals(expected1, data.get(0));
		
		// check second input data
		int nanoSecondsInSecond = 1000000000;
		int nanoSeconds = (int)(0.1 * nanoSecondsInSecond);
		InputData expected2 = new InputData(LocalTime.of(5, 2, 1, nanoSeconds), 3, Direction.DOWN, 1);
		assertEquals(expected2, data.get(1));
	}

}