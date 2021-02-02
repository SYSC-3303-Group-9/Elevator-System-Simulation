package floor;

import java.time.LocalTime;

import elevator.Direction;

public class InputData {
	private LocalTime time;
	private int currentFloor;
	private Direction direction;
	private int destinationFloor;
	
	public InputData(LocalTime time, int currentFloor, Direction direction, int destinationFloor) {
		super();
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
}
