package elevator;

import common.Constants;

/**
 * Represents an Elevator's motor which moves the carriage from one floor to the next
 */
public class ElevatorMotor  {
	public ElevatorMotor() {}

	/**
	 * The real-time aspect of the elevator moving from one floor to the next
	 * @param fault	Possible fault that occurs during the move action, extending the time taken
	 * @return boolean based on whether the move action is successfully completed
	 */
	public boolean move(Fault fault) {
		long waitTime = 0;
		// No fault registered
		if(fault.equals(Fault.NONE)) {
			// Sleep this thread for the time it takes to move one floor
			waitTime = (long) (Constants.MOVE_TIME / Constants.TIME_MULTIPLIER);
		}
		// Transient fault registered
		else if(fault.equals(Fault.TRANSIENT)) {
			// Sleep this thread for the time it takes to move one floor + the time it takes to overcome a transient fault
			waitTime = (long) ((Constants.MOVE_TIME + Constants.TRANSIENT_FAULT_TIME) / Constants.TIME_MULTIPLIER);
		}
		// Permanent fault is registered
		else if(fault.equals(Fault.PERMANENT)) {
			// Sleep this thread for the time it takes to register that a permanent fault has occurred
			waitTime = (long) (Constants.PERMANENT_FAULT_TIME / Constants.TIME_MULTIPLIER);
		}
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {}
		return !fault.equals(Fault.PERMANENT);
	}
}
