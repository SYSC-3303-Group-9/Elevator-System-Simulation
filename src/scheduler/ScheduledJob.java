package scheduler;

import floor.InputData;

/**
 * Represents a job to completed by the scheduler.
 */
public class ScheduledJob {
	private static int curJobId = 0;
	
	private int jobId;
	private SchedulerElevator assignedElevator;
	private InputData inputData;
	private boolean isOnElevator = false;
	
	public ScheduledJob(SchedulerElevator assignedElevator, InputData inputData) {
		super();
		this.jobId = curJobId++;
		this.assignedElevator = assignedElevator;
		this.inputData = inputData;
	}
	
	/**
	 * Gets this job's ID.
	 * @return The job ID.
	 */
	public int getJobId() {
		return jobId;
	}
	
	/**
	 * Gets this job's input data.
	 * @return The start floor.
	 */
	public InputData getInputData() {
		return inputData;
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
