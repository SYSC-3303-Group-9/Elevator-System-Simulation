package elevator;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ElevatorMoveCommand {
	private int elevatorID;
	private Direction moveDirection;
	
	/**
	 * Used to construct an ElevatorMoveCommand to be sent to the ElevatorMotor by the Scheduler
	 * @param elevatorID an int indicating the ID of the elevator to be moved
	 * @param direction the direction in which the elevator should be moved
	 */
	public ElevatorMoveCommand(int elevatorID, Direction direction) {
		this.moveDirection = direction;
		this.elevatorID = elevatorID;
	}
	
	/**
	 * Converts an ElevatorMoveCommand object into an array of bytes to be sent with packets
	 * @return byte array representing an ElevatorMoveCommand object
	 */
	public byte[] toBytes() {
		byte[] elevatorIDArray = ByteBuffer.allocate(4).putInt(elevatorID).array();
		byte[] moveDirectionArray = moveDirection.name().getBytes();
		byte[] eventArray = new byte[elevatorIDArray.length + moveDirectionArray.length];
		System.arraycopy(elevatorIDArray, 0, eventArray, 0, elevatorIDArray.length);
		System.arraycopy(moveDirectionArray, 0, eventArray, elevatorIDArray.length, moveDirectionArray.length);
		return eventArray;
	}
	
	/**
	 * Constructs an ElevatorMoveCommand object from a passed array of bytes
	 * @param bytes an array of bytes created from calling toBytes() on an ElevatorMoveCommand object
	 * @return the ElevatorMoveCommand object represented by the data stored in the byte array
	 */
	public static ElevatorMoveCommand fromBytes(byte[] bytes) {
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
		else return new ElevatorMoveCommand(id, direction);
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
		if(!(o instanceof ElevatorMoveCommand)) return false;
		ElevatorMoveCommand c = (ElevatorMoveCommand) o;
		return this.moveDirection.equals(c.getDirection()) && (this.elevatorID == c.getID());
	}
}
