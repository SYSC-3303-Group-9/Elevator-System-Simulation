package elevator;

/**
 * Different states for the Elevators
 */
public enum ElevatorState {
	INITIAL, MOVING_UP, MOVING_DOWN, OPENING_CLOSING_DOORS, TRANSIENT_FAULT, PERMANENT_FAULT, WAITING, FINAL, DISABLED;
}
