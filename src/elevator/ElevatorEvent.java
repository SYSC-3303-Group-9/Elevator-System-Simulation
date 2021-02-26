package elevator;

public class ElevatorEvent {

	private int floor;
	private int elevatorId;

	public ElevatorEvent(int floor, int elevatorId) {
		this.floor = floor;
		this.elevatorId = elevatorId;
	}

	public String toString() {
		return "Elevator " + Integer.toString(elevatorId) + "arrived at floor " + Integer.toString(floor);

	}
}
