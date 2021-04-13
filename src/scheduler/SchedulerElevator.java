package scheduler;

import java.util.ArrayList;

import elevator.Direction;
import elevator.Fault;

/**
 * Represents everything the scheduler knows about an elevator.
 */
public class SchedulerElevator {
	private int elevatorId;
	private int lastKnownFloor;
	private Direction direction;
	
	private Fault pendingFault = Fault.NONE;
	private boolean doorsOpening = false;
	private Direction serviceDirection = Direction.WAITING;
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
	 * Gets the elevator's pending fault.
	 * @return The pending fault.
	 */
	public Fault getPendingFault() {
		return pendingFault;
	}
	
	/**
	 * Sets the elevator's pending fault.
	 * @param pendingFault The pending fault.
	 */
	public void setPendingFault(Fault pendingFault) {
		this.pendingFault = pendingFault;
	}
	
	/**
	 * Gets whether the elevator's doors have been requested to open.
	 * @return Whether the doors have been requested to open.
	 */
	public boolean getDoorsOpening() {
		return doorsOpening;
	}
	
	/**
	 * Sets whether the elevator's doors have been requested to open.
	 * @param doorsOpening Whether the doors have been requested to open.
	 */
	public void setDoorsOpening(boolean doorsOpening) {
		this.doorsOpening = doorsOpening;
	}
	
	/**
	 * Gets the elevator's assigned jobs.
	 * @return The assigned jobs.
	 */
	public ArrayList<ScheduledJob> getAssignedJobs() {
		return assignedJobs;
	}
	
	/**
	 * The direction the elevator will travel to service requests.
	 * This is not necessarily the current direction of the elevator if
	 * the elevator is empty.
	 * @return
	 */
	public Direction getServiceDirection() {
		return this.serviceDirection;
	}
	
	/**
	 * Sets the direction the elevator will travel to service requests.
	 * This is not necessarily the current direction of the elevator if
	 * the elevator is empty.
	 * @param The service direction.
	 */
	public void setServiceDirection(Direction serviceDirection) {
		this.serviceDirection = serviceDirection;
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
	
	/**
	 * Gets the closest destination floor of the elevator's assigned jobs.
	 * @param elevator The elevator to search.
	 * @return The closest destination floor.
	 */
	public int findElevatorDestination() {
		int destination = this.direction == Direction.UP ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		
		for (ScheduledJob job : this.assignedJobs) {
			if (this.direction == Direction.UP) {
				if (job.getIsOnElevator() && job.getInputData().getDestinationFloor() < destination) {
					destination = job.getInputData().getDestinationFloor();
				}
				else if (!job.getIsOnElevator() && job.getInputData().getCurrentFloor() < destination) {
					destination = job.getInputData().getCurrentFloor();
				}
			}
			else if (this.direction == Direction.DOWN) {
				if (job.getIsOnElevator() && job.getInputData().getDestinationFloor() > destination) {
					destination = job.getInputData().getDestinationFloor();
				}
				else if (!job.getIsOnElevator() && job.getInputData().getCurrentFloor() > destination) {
					destination = job.getInputData().getCurrentFloor();
				}
			}
		}
		
		return destination;
	}
	
}
