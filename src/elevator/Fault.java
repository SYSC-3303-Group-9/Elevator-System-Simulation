package elevator;

import java.nio.ByteBuffer;

public enum Fault {
	NONE,
	TRANSIENT,
	PERMANENT;
	
	public byte[] toBytes() {
		return ByteBuffer.allocate(4).putInt(this.ordinal()).array();
	}
	
	public static Fault fromBytes(byte[] bytes) throws IllegalArgumentException {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		try {
			return Fault.values()[buffer.getInt()];
		} catch(IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("No Fault enum is represented by the provided byte array.");
		}
	}
}
