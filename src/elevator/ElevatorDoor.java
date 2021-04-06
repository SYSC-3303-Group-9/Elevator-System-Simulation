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
	public boolean openClose(Fault fault) {
		long waitTime = 0;
		// No fault registered
		if(fault.equals(Fault.NONE)) {
			// While the elapsed moving time is less than the time required to open/close the door
			open();
			load();
			close();
		}
		// Transient fault registered
		else if(fault.equals(Fault.TRANSIENT)) {
			// While the elapsed moving time is less than the time required to open/close the door + the transient fault time
			waitTime = (long) (Constants.TRANSIENT_FAULT_TIME / Constants.TIME_MULTIPLIER);
		}
		// Permanent fault registered
		else if(fault.equals(Fault.PERMANENT)) {
			// Sleep this thread for the time it takes to register that a permanent fault has occurred
			waitTime = (long) (Constants.PERMANENT_FAULT_TIME / Constants.TIME_MULTIPLIER);
		}
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {}
		return !fault.equals(Fault.PERMANENT);
	}
	
	
	private void open() {
		// TODO: Update elevator in GUI to OPENING at the beginning
		long waitTime = (long) (Constants.DOOR_TIME / Constants.TIME_MULTIPLIER);
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {}
		// TODO: Update elevator in GUI to OPEN
	}
	
	private void close() {
		//TODO: Update elevator in GUI to CLOSING at the beginning
		long waitTime = (long) (Constants.DOOR_TIME / Constants.TIME_MULTIPLIER);
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {}
		// TODO: Update elevator in GUI to CLOSED after close()
	}
	
	private void load() {
		long waitTime = (long) (Constants.LOADING_TIME / Constants.TIME_MULTIPLIER);
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {}
	}
}
