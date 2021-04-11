package elevator;

/**
 * Different states for the Elevators
 */
public enum ElevatorState {
	INITIAL("Initial"), 
	MOVING_UP("Moving Up"), 
	MOVING_DOWN("Moving Down"), 
	OPENING_CLOSING_DOORS("Opening/Closing Doors"), 
	TRANSIENT_FAULT("Transient Fault"), 
	PERMANENT_FAULT("Permanent Fault"), 
	WAITING("Waiting"), 
	FINAL("Final"), 
	DISABLED("Disabled");
	
	private final String label;
	
	private ElevatorState(String s) {
		this.label = s;
	}
	
	@Override
	public String toString() {
		return this.label;
	}
}
