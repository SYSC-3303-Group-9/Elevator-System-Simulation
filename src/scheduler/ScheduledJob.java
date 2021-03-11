package scheduler;

import elevator.Direction;
import floor.InputData;

/**
 * Represents a job to completed by the scheduler.
 */
public class ScheduledJob {
	private static int curJobId = 0;
	
	private int jobId;
	private int startFloor;
	private int endFloor;
	private Direction direction;
	private SchedulerElevator assignedElevator;
	private boolean isOnElevator = false;
	
	public ScheduledJob(SchedulerElevator assignedElevator, int startFloor, int endFloor, Direction direction) {
		super();
		this.jobId = curJobId++;
		this.startFloor = startFloor;
		this.endFloor = endFloor;
		this.direction = direction;
		this.assignedElevator = assignedElevator;
	}
	
	public ScheduledJob(SchedulerElevator assignedElevator, InputData inputData) {
		super();
		this.jobId = curJobId++;
		this.startFloor = inputData.getCurrentFloor();
		this.endFloor = inputData.getDestinationFloor();
		this.direction = inputData.getDirection();
		this.assignedElevator = assignedElevator;
	}
	
	/**
	 * Gets this job's ID.
	 * @return The job ID.
	 */
	public int getJobId() {
		return jobId;
	}
	
	/**
	 * Gets this job's starting floor.
	 * @return The start floor.
	 */
	public int getStartFloor() {
		return startFloor;
	}
	
	/**
	 * Gets this job's ending floor.
	 * @return The end floor.
	 */
	public int getEndFloor() {
		return endFloor;
	}
	
	/**
	 * Gets this job's direction.
	 * @return The direction.
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * Gets this job's assigned elevator.
	 * @return The assigned elevator.
	 */
	public SchedulerElevator getAssignedElevator() {
		return assignedElevator;
	}
	
	/**
	 * Gets whether the person has boarded the elevator.
	 * @return Has the person boarded an elevator.
	 */
	public boolean getIsOnElevator() {
		return isOnElevator;
	}

	/**
	 * Sets whether the person has boarded the elevator.
	 * @param isOnElevator Has the person boarded an elevator.
	 */
	public void setOnElevator(boolean isOnElevator) {
		this.isOnElevator = isOnElevator;
	}
	
}
