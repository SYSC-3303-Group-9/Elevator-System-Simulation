package elevator;

import java.nio.ByteBuffer;

/**
 * Indicates the direction an elevator is moving.
 */
public enum Direction {
	UP,
	DOWN,
	WAITING;
	
	public byte[] toBytes() {
		return ByteBuffer.allocate(4).putInt(this.ordinal()).array();
	}
	
	public static Direction fromBytes(byte[] bytes) throws IllegalArgumentException {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		try {
			return Direction.values()[buffer.getInt()];
		} catch(IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("No Direction enum is represented by the provided byte array.");
		}
	}
}
