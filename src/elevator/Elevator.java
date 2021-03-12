package elevator;

import floor.InputData;

/**
 * Moves from floor to floor based on the data it has been sent.
 */
public class Elevator  {
	private int id;
	private int floor;
	/**
	 * Creates a new instance of the Elevator class.
	 * @param id The unique identifier of this elevator instance.
	 */
	public Elevator(int id, int floor) {
		this.id = id;
		this.floor = floor;
	}

	/**
	 * Gets the ID of the elevator
	 * @return the elevators ID
	 */
	public int getId() {
		return id;
	}
	
	public int getFloor() {
		return floor;
	}
	
	/**
	 * Moves the elevator to a specified floor and prints the floor it left and the destination floor.
	 * @param input	The input contains the destination the elevator needs to move to amongst other data.
	 */
	public void move(Direction direction) {
		if(direction == Direction.UP) {
			floor++;
		}
		else {
			floor--;
		}
		
		System.out.println(this + " is moving " + direction);
		System.out.println(this + " arrived at floor " + floor);
		
		//System.out.println("[" + input.getTime() + "] " + this + " is moving " + input.getDirection());
		//System.out.println("[" + input.getTime() + "] " + this + " arrived at floor " + input.getDestinationFloor());
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
		return "Elevator " + Integer.toString(id);
	}

}
