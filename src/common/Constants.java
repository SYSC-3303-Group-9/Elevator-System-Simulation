package common;

import java.nio.file.Paths;

public class Constants {
	/**
	 * The number of elevators.
	 */
	public static final int NUM_ELEVATORS = 4;
	
	/**
	 * The number of floors.
	 */
	public static final int NUM_FLOORS = 22;
	
	/**
	 * Time it takes to move one floor in milliseconds.
	 */
	public static final int MOVE_TIME = 4000;
	
	/**
	 * Time it takes to open *and* close the doors in milliseconds.
	 */
	public static final int DOOR_TIME = 5000;
	
	/**
	 * Time it takes for the system to overcome a transient fault in milliseconds.
	 */
	public static final int TRANSIENT_FAULT_TIME = 5000;
	
	/**
	 * ElevatorSubsystem's base port. Every sequential elevator is assigned
	 * this number plus its ID. E.g. ID0=100, ID1=101, ID2=102...
	 */
	public static final int ELEVATOR_BASE_PORT = 100;
	
	/**
	 * FloorReceiver's port.
	 */
	public static final int FLOOR_RECEIVER_PORT = 70;
	
	/**
	 * The input file path.
	 * File is located in resources/input.txt by default.
	 */
	public static final String INPUT_FILE = Paths.get("resources", "input.txt").toString();
	
	/**
	 * Multiplier to determine how fast simulated time runs compared to real-time.
	 * E.g. If = 1 then simulation is real-time. If = 2 then simulation is twice as fast as real-time.
	 */
	public static final float TIME_MULTIPLIER = 1f;
}
