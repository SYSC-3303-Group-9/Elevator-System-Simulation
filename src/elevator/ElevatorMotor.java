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
	public void move() {
		// Sleep this thread for the time it takes to move one floor
		long waitTime = (long) (Constants.MOVE_TIME / Constants.TIME_MULTIPLIER);
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {}
	}
}
