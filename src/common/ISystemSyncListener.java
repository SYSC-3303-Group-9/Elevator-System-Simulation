package common;

/**
 * Defines a listener for SystemSync following the observer pattern.
 */
public interface ISystemSyncListener {
	/**
	 * Called when the elevator subsystem's handshake packet is received.
	 */
	void onElevatorHandshake();
	
	/**
	 * Called when the floor subsystem's handshake packet is received.
	 */
	void onFloorHandshake();
}
