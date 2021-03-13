package scheduler;

import java.util.ArrayList;

import elevator.Direction;

/**
 * Represents everything the scheduler knows about an elevator.
 */
public class SchedulerElevator {
	private int elevatorId;
	private int lastKnownFloor;
	private Direction direction;
	private ArrayList<ScheduledJob> assignedJobs = new ArrayList<ScheduledJob>();
	
	public SchedulerElevator(int elevatorId, int lastKnownFloor, Direction direction) {
		super();
		this.elevatorId = elevatorId;
		this.lastKnownFloor = lastKnownFloor;
		this.direction = direction;
	}
	
	/**
	 * The last known floor the elevator was located at.
	 * @param lastKnownFloor The last known floor.
	 */
	public void setLastKnownFloor(int lastKnownFloor) {
		this.lastKnownFloor = lastKnownFloor;
	}
	
	/**
	 * Sets the direction of the elevator.
	 * @param direction The direction of the elevator.
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	/**
	 * Gets the elevator's ID.
	 * @return The elevator ID.
	 */
	public int getElevatorId() {
		return elevatorId;
	}
	
	/**
	 * Gets the last known floor the elevator was located at.
	 * @return The last known floor.
	 */
	public int getLastKnownFloor() {
		return lastKnownFloor;
	}
	
	/**
	 * Gets the elevator's direction.
	 * @return The elevator direction.
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * Gets the elevator's assigned jobs.
	 * @return The assigned jobs.
	 */
	public ArrayList<ScheduledJob> getAssignedJobs() {
		return assignedJobs;
	}
	
	/**
	 * Update the last known floor based on the direction the elevator
	 * is currently moving.
	 */
	public void updateLastKnownFloor() {
		switch (this.direction) {
		case DOWN:
			this.lastKnownFloor--;
			break;
		case UP:
			this.lastKnownFloor++;
			break;
		case WAITING:
			break;
		}
	}
	
}
