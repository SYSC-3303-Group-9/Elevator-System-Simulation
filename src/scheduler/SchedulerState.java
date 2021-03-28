package scheduler;

/**
 * Different states for the scheduler's state machine.
 */
public enum SchedulerState {
	INITIAL,
	WAITING_FOR_MESSAGE,
	PROCESSING_NEW_JOB,
	PROCESSING_ELEVATOR_EVENT,
	SCHEDULE_MOVE,
	SCHEDULE_DOOR,
	FINAL,
}
