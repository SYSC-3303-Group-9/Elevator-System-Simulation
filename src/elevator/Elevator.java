package elevator;

import floor.InputData;

/**
 * Moves from floor to floor based on the data it has been sent.
 */
public class Elevator  {
	private int id;

	/**
	 * Creates a new instance of the Elevator class.
	 * @param id The unique identifier of this elevator instance.
	 */
	public Elevator(int id) {
		this.id = id;
	}
	
	/**
	 * Moves the elevator to a specified floor and prints the floor it left and the destination floor.
	 * @param input	The input contains the destination the elevator needs to move to amongst other data.
	 */
	public void move(InputData input) {
		System.out.println(getLocation(input));
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
		return "[E" + Integer.toString(id) + "]";
	}
}
