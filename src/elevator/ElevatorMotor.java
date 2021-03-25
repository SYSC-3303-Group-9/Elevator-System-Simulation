package elevator;

import common.Clock;
import common.Constants;

/**
 * Moves from floor to floor based on the data it has been sent.
 */
public class ElevatorMotor  {
	/**
	 * Creates a new instance of the Elevator class.
	 */
	public ElevatorMotor() {
		
	}

	/**
	 * Moves the elevator to a specified floor and prints the floor it left and the destination floor.
	 * @param input	The input contains the destination the elevator needs to move to amongst other data.
	 */
	public void move(Fault fault) {
		long startTime = Clock.getTime();
		// No fault registered
		if(fault.equals(Fault.NONE)) {
			// While the elapsed moving time is less than the time required to move one floor
			while(((Clock.getTime() - startTime)*Constants.TIME_MULTIPLIER) < Constants.MOVE_TIME) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {}
			}
		}
		// Transient fault registered
		else if(fault.equals(Fault.TRANSIENT)) {
			// While the elapsed moving time is less than the time required to move one floor + the transient fault time
			while(((Clock.getTime() - startTime)*Constants.TIME_MULTIPLIER) < (Constants.MOVE_TIME + Constants.TRANSIENT_FAULT_TIME)) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {}
			}
		}
	}
}
