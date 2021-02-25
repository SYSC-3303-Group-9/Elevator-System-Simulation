package scheduler;

/**
 * Different states for the scheduler's state machine.
 */
public enum SchedulerState {
	INITIAL,
	WAITING_FOR_INSTRUCTION,
	WAITING_FOR_ELEVATOR,
	FINAL,
}
