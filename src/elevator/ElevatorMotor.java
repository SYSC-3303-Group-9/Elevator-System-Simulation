package elevator;

import common.Constants;

/**
 * Represents an Elevator's motor, that moves the carriage from one floor to the next
 */
public class ElevatorMotor  {
	public ElevatorMotor() {}

	/**
	 * The real-time aspect of the elevator moving from one floor to the next
	 * @param fault	Possible fault that occurs during the move action, extending the time taken
	 */
	public void move(Fault fault) {
		long waitTime;
		// No fault registered
		if(fault.equals(Fault.NONE)) {
			waitTime = (long) (Constants.MOVE_TIME / Constants.TIME_MULTIPLIER);
			// While the elapsed moving time is less than the time required to move one floor
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {}	
		}
		// Transient fault registered
		else if(fault.equals(Fault.TRANSIENT)) {
			waitTime = (long) ((Constants.MOVE_TIME + Constants.TRANSIENT_FAULT_TIME) / Constants.TIME_MULTIPLIER);
			// While the elapsed moving time is less than the time required to move one floor + the transient fault time
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {}
		}
	}
}
