package elevator;

import java.nio.ByteBuffer;

public class ElevatorMoveCommand extends ElevatorCommand {
	public static final byte COMMAND_IDENTIFIER = (byte) 0x01;
	private Direction moveDirection;
	
	/**
	 * Used to construct an ElevatorMoveCommand to be sent to the ElevatorMotor by the Scheduler
	 * @param elevatorID an int indicating the ID of the elevator to be moved
	 * @param direction the direction in which the elevator should be moved
	 */
	public ElevatorMoveCommand(int elevatorID, Fault fault, Direction direction) {
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
		byte[] moveDirectionBytes = moveDirection.toBytes();
		byte[] elevatorMoveCommandBytes = new byte[elevatorCommandBytes.length + moveDirectionBytes.length + 1];
		elevatorMoveCommandBytes[0] = ElevatorMoveCommand.COMMAND_IDENTIFIER;
		System.arraycopy(elevatorCommandBytes, 0, elevatorMoveCommandBytes, 1, elevatorCommandBytes.length);
		System.arraycopy(moveDirectionBytes, 0, elevatorMoveCommandBytes, elevatorCommandBytes.length + 1, moveDirectionBytes.length);
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
		if(buffer.get() != ElevatorMoveCommand.COMMAND_IDENTIFIER) return null;
		int elevatorID = buffer.getInt();
		byte[] faultBytes = new byte[4];
		byte[] directionBytes = new byte[4];
		// Copy remaining ByteBuffer bytes over to directionBytes
		buffer.get(faultBytes);
		buffer.get(directionBytes);
		return new ElevatorMoveCommand(elevatorID, Fault.fromBytes(faultBytes), Direction.fromBytes(directionBytes));
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
