package floor;

import java.time.LocalTime;

import elevator.Direction;

/**
 * Represents an elevator request.
 */
public class InputData {
	private LocalTime time;
	private int currentFloor;
	private Direction direction;
	private int destinationFloor;
	
	/**
	 * Creates a new instance of the InputData class.
	 * @param time The time of the event.
	 * @param currentFloor The floor that needs servicing.
	 * @param direction The direction the elevator must move.
	 * @param destinationFloor The floor that the elevator must reach.
	 */
	public InputData(LocalTime time, int currentFloor, Direction direction, int destinationFloor) {
		this.time = time;
		this.currentFloor = currentFloor;
		this.direction = direction;
		this.destinationFloor = destinationFloor;
	}
	
	public LocalTime getTime() {
		return time;
	}
	
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public int getDestinationFloor() {
		return destinationFloor;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		
		if (o instanceof InputData) {
			InputData other = (InputData)o;
			return getTime().equals(other.getTime())
					&& getCurrentFloor() == other.getCurrentFloor()
					&& getDirection().equals(other.getDirection())
					&& getDestinationFloor() == other.getDestinationFloor();
		}
		return false;
	}
	
	@Override
	public String toString() {
		return time.toString() + " " + currentFloor + " " + direction.toString() + " " + destinationFloor;
	}
}
