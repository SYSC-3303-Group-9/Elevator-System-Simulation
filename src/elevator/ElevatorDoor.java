package elevator;

import common.Constants;
import elevator.gui.Door;
import elevator.gui.DoorState;

/**
 * Represents an Elevator's door which opens and closes when it reaches the desired floor
 */
public class ElevatorDoor {
	private Door door;
	
	public ElevatorDoor(Door door) {
		this.door = door;
	}
	
	/**
	 * The real-time aspect of the elevator opening and closing its doors.
	 * @param fault	Possible fault that occurs during the door action, extending the time taken
	 * @return boolean based on whether the door event was completed (false if a Permanent fault occurred)
	 */
	public void openClose() {
		open();
		load();
		close();
	}
	
	/**
	 * Private method called by openClose to open the elevator doors. Any possible faults will happen during the execution
	 * of this method.
	 * @param fault possible fault that will occur during the doors opening
	 * @return boolean based on whether the doors ended up open
	 */
	private void open() {
		door.setState(DoorState.DOOR_OPENING);
		long waitTime = (long) (Constants.DOOR_TIME / Constants.TIME_MULTIPLIER);
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {}
		door.setState(DoorState.DOOR_OPEN);
	}
	
	/**
	 * Private method called by openClose to close the elevator doors.
	 */
	private void close() {
		door.setState(DoorState.DOOR_CLOSING);
		long waitTime = (long) (Constants.DOOR_TIME / Constants.TIME_MULTIPLIER);
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {}
		door.setState(DoorState.DOOR_CLOSED);
	}
	
	/**
	 * Private method called by openClose to pass the time it takes to load/unload some passengers.
	 */
	private void load() {
		long waitTime = (long) (Constants.LOADING_TIME / Constants.TIME_MULTIPLIER);
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {}
	}
}
