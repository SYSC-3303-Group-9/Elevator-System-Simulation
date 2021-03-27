package floor;

import java.io.BufferedReader;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import elevator.Direction;
import elevator.Fault;

/**
 * Handles the input file parsing.
 */
public class InputParser {
	
	/**
	 * Parse the lines in a reader into a list of InputData.
	 * @param reader Reader containing lines of text to parse.
	 * @return List of parsed InputData.
	 */
	public static List<InputData> parse(BufferedReader reader) {
		return reader.lines()
			.map(i -> lineToInputData(i))
			.collect(Collectors.toList());
	}
	
	/**
	 * Convert a line of text into a single InputData object.
	 * @param line The line of text.
	 * @return The parsed InputData.
	 */
	private static InputData lineToInputData(String line) {
		String[] split = line.split(" ");
		
		LocalTime time = LocalTime.parse(split[0]);
		int currentFloor = Integer.parseInt(split[1]);
		Direction direction = Direction.valueOf(split[2].toUpperCase());
		int destinationFloor = Integer.parseInt(split[3]);
		int faultCode = Integer.parseInt(split[4]);
		
		Fault fault;
		switch (faultCode) {
		case 0:
			fault = Fault.NONE;
			break;
		case 1:
			fault = Fault.TRANSIENT;
			break;
		case 2:
			fault = Fault.PERMANENT;
			break;
		default:
			throw new UnsupportedOperationException();
		}
		
		return new InputData(time, currentFloor, direction, destinationFloor, fault);
	}
}
