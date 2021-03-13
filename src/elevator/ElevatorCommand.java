package elevator;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ElevatorCommand {
	private int elevatorID;
	private Direction moveDirection;
	
	public ElevatorCommand(int elevatorID, Direction direction) {
		this.moveDirection = direction;
		this.elevatorID = elevatorID;
	}
	
	public byte[] toBytes() {
		byte[] elevatorIDArray = ByteBuffer.allocate(4).putInt(elevatorID).array();
		byte[] moveDirectionArray = moveDirection.name().getBytes();
		byte[] eventArray = new byte[elevatorIDArray.length + moveDirectionArray.length];
		System.arraycopy(elevatorIDArray, 0, eventArray, 0, elevatorIDArray.length);
		System.arraycopy(moveDirectionArray, 0, eventArray, elevatorIDArray.length, moveDirectionArray.length);
		return eventArray;
	}
	
	public static ElevatorCommand fromBytes(byte[] bytes) {
		// Create a ByteBuffer for bytes
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		// Save elevatorID value from ByteBuffer
		int id = buffer.getInt();
		// Create byte[] with the same size as the remaining bytes in the buffer (Direction string bytes)
		byte[] directionBytes = new byte[buffer.remaining()];
		// Copy remaining ByteBuffer bytes over to directionBytes
		buffer.get(directionBytes);
		// Attempt to convert the directionBytes into a Direction enum
		Direction direction = null;
		String enumName = new String(directionBytes, Charset.defaultCharset()).trim();
		try {
			direction = Direction.valueOf(enumName);
		} catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		if(direction == null) return null;
		else return new ElevatorCommand(id, direction);
	}
	
	public Direction getDirection() {
		return this.moveDirection;
	}
	
	public int getID() {
		return this.elevatorID;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof ElevatorCommand)) return false;
		ElevatorCommand c = (ElevatorCommand) o;
		return this.moveDirection.equals(c.getDirection()) && (this.elevatorID == c.getID());
	}
}
