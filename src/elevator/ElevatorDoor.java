package elevator;

import common.Constants;

/**
 * Represents an Elevator's door which opens and closes when it reaches the desired floor
 */
public class ElevatorDoor {
	public ElevatorDoor() {}
	
	/**
	 * The real-time aspect of the elevator opening and closing its door
	 * @param fault	Possible fault that occurs during the door action, extending the time taken
	 */
	public void openClose(Fault fault) {
		long waitTime;
		// No fault registered
		if(fault.equals(Fault.NONE)) {
			waitTime = (long) (Constants.DOOR_TIME / Constants.TIME_MULTIPLIER);
			// While the elapsed moving time is less than the time required to move one floor
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {}	
		}
		// Transient fault registered
		else if(fault.equals(Fault.TRANSIENT)) {
			waitTime = (long) ((Constants.DOOR_TIME + Constants.TRANSIENT_FAULT_TIME) / Constants.TIME_MULTIPLIER);
			// While the elapsed moving time is less than the time required to move one floor + the transient fault time
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {}
		}
	}
}
