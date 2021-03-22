package elevator;

import floor.InputData;

/**
 * Moves from floor to floor based on the data it has been sent.
 */
public class Elevator  {
	/**
	 * Creates a new instance of the Elevator class.
	 * @param id The unique identifier of this elevator instance.
	 */
	public Elevator() {
	
	}

	/**
	 * Moves the elevator to a specified floor and prints the floor it left and the destination floor.
	 * @param input	The input contains the destination the elevator needs to move to amongst other data.
	 */
	public void move(Direction direction) {
	
	}
	
	/**
	 * Gets the movement of the elevator.
	 * @return The elevator's movement.
	 */
	public String getLocation(InputData task) {
		return this + " " + task.getCurrentFloor() + " " + task.getDirection() + " " + task.getDestinationFloor();
	}

	@Override
	public String toString() {
		return "";
	}

}
