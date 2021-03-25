package elevator;

import java.nio.ByteBuffer;

/**
 * Different states for the ElevatorDoor
 */
public enum DoorState {
	OPEN,
	CLOSED,
	STUCK;
	
	public byte[] toBytes() {
		return ByteBuffer.allocate(4).putInt(this.ordinal()).array();
	}
	
	public static DoorState fromBytes(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		try {
			return DoorState.values()[buffer.getInt()];
		} catch(IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("No Direction enum is represented by the provided byte array.");
		}
	}
}
