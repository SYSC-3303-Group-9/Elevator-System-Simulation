package elevator;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ElevatorMoveCommand extends ElevatorCommand {
	private Direction moveDirection;
	
	/**
	 * Used to construct an ElevatorMoveCommand to be sent to the ElevatorMotor by the Scheduler
	 * @param elevatorID an int indicating the ID of the elevator to be moved
	 * @param direction the direction in which the elevator should be moved
	 */
	public ElevatorMoveCommand(int elevatorID, int fault, Direction direction) {
		super(elevatorID, fault);
		this.moveDirection = direction;
	}
	
	/**
	 * Converts an ElevatorMoveCommand object into an array of bytes to be sent with packets
	 * @return byte array representing an ElevatorMoveCommand object
	 */
	@Override
	public byte[] toBytes() {
		byte[] elevatorCommandBytes = super.toBytes();
		byte[] moveDirectionBytes = moveDirection.name().getBytes();
		byte[] elevatorMoveCommandBytes = new byte[elevatorCommandBytes.length + moveDirectionBytes.length];
		System.arraycopy(elevatorCommandBytes, 0, elevatorMoveCommandBytes, 0, elevatorCommandBytes.length);
		System.arraycopy(moveDirectionBytes, 0, elevatorMoveCommandBytes, elevatorCommandBytes.length, moveDirectionBytes.length);
		return elevatorMoveCommandBytes;
	}
	
	/**
	 * Constructs an ElevatorMoveCommand object from a passed array of bytes
	 * @param bytes an array of bytes created from calling toBytes() on an ElevatorMoveCommand object
	 * @return the ElevatorMoveCommand object represented by the data stored in the byte array
	 */
	public static ElevatorMoveCommand fromBytes(byte[] bytes) {
		// Create a ByteBuffer for bytes
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		// Use static constructor method from ElevatorCommand
		byte elevatorCommandBytes[] = new byte[8]; 
		buffer.get(elevatorCommandBytes, 0, 8);
		ElevatorCommand tempCommand = ElevatorCommand.fromBytes(elevatorCommandBytes);
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
		else return new ElevatorMoveCommand(tempCommand.getID(), tempCommand.getFault(), direction);
	}
	
	public Direction getDirection() {
		return this.moveDirection;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof ElevatorMoveCommand)) return false;
		ElevatorMoveCommand c = (ElevatorMoveCommand) o;
		return super.equals(c) && moveDirection.equals(c.getDirection());
	}
}
