package common;

import java.nio.file.Paths;

public class Constants {

	/**
	 * Time it takes to move one floor in milliseconds.
	 */
	public static final int MOVE_TIME = 4000;
	
	/**
	 * Time it takes to open or close the doors in milliseconds.
	 */
	public static final int DOOR_TIME = 2500;
	
	/**
	 * Time it takes for passengers to load or unload from the elevator in milliseconds.
	 */
	public static final int LOADING_TIME = 4000;
	
	/**
	 * Time it takes for the system to overcome a transient fault in milliseconds.
	 */
	public static final int TRANSIENT_FAULT_TIME = 3000;
	
	/**
	 * Time it takes for the system to realize a permanent fault has occurred in milliseconds.
	 */
	public static final int PERMANENT_FAULT_TIME = 7000;
	
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
	 * SystemSync's port.
	 */
	public static final int SYSTEM_SYNC_PORT = 78;
	
	/**
	 * ElevatorEventReceiver's port.
	 */
	public static final int ELEVATOR_EVENT_RECEIVER_PORT = 110;
	
	/**
	 * SchedulerReceiver's port.
	 */
	public static final int SCHEDULER_RECEIVER_PORT = 123;
	
	/**
	 * Measurement's port.
	 */
	public static final int MEASUREMENT_RECEIVER_PORT = 139;
	
	/**
	 * The input file path.
	 * File is located in resources/input.txt by default.
	 */
	public static final String INPUT_FILE = Paths.get("resources", "input.txt").toString();
	
	/**
	 * The up arrow image file path
	 * File is located at resources/upArrow.png
	 */
	public static final String UP_ARROW = Paths.get("resources", "upArrow.png").toString();
	
	/**
	 * The up arrow image file path
	 * File is located at resources/downArrow.png
	 */
	public static final String DOWN_ARROW = Paths.get("resources", "downArrow.png").toString();
	
	/**
	 * Multiplier to determine how fast simulated time runs compared to real-time.
	 * E.g. If = 1 then simulation is real-time. If = 2 then simulation is twice as fast as real-time.
	 */
	public static final float TIME_MULTIPLIER = 5f;
}
