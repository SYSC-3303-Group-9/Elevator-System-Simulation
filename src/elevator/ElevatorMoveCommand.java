package elevator;

import java.nio.ByteBuffer;

public class ElevatorMoveCommand extends ElevatorCommand {
	public static final byte COMMAND_IDENTIFIER = (byte) 0x01;
	private Direction moveDirection;
	private int destinationFloor;
	
	/**
	 * Used to construct an ElevatorMoveCommand to be sent to the ElevatorMotor by the Scheduler
	 * @param elevatorID an int indicating the ID of the elevator to be moved
	 * @param direction the direction in which the elevator should be moved
	 */
	public ElevatorMoveCommand(int elevatorID, Fault fault, Direction direction, int destinationFloor) {
		super(elevatorID, fault);
		this.moveDirection = direction;
		this.destinationFloor = destinationFloor;
	}
	
	/**
	 * Converts an ElevatorMoveCommand object into an array of bytes to be sent with packets
	 * @return byte array representing an ElevatorMoveCommand object
	 */
	@Override
	public byte[] toBytes() {
		// Convert state into bytes
		byte[] elevatorCommandBytes = super.toBytes();
		byte[] moveDirectionBytes = moveDirection.toBytes();
		byte[] destinationFloorBytes = ByteBuffer.allocate(4).putInt(destinationFloor).array();
		// Combine all byte arrays into one, prepending command identifier
		byte[] elevatorMoveCommandBytes = new byte[elevatorCommandBytes.length + moveDirectionBytes.length + destinationFloorBytes.length + 1];
		elevatorMoveCommandBytes[0] = ElevatorMoveCommand.COMMAND_IDENTIFIER;
		System.arraycopy(elevatorCommandBytes, 0, elevatorMoveCommandBytes, 1, elevatorCommandBytes.length);
		System.arraycopy(moveDirectionBytes, 0, elevatorMoveCommandBytes, elevatorCommandBytes.length + 1, moveDirectionBytes.length);
		System.arraycopy(destinationFloorBytes, 0, elevatorMoveCommandBytes, elevatorCommandBytes.length + moveDirectionBytes.length + 1, destinationFloorBytes.length);
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
		buffer.get(faultBytes);
		buffer.get(directionBytes);
		return new ElevatorMoveCommand(elevatorID, Fault.fromBytes(faultBytes), Direction.fromBytes(directionBytes), buffer.getInt());
	}
	
	public Direction getDirection() {
		return this.moveDirection;
	}
	
	public int getDestinationFloor() {
		return this.destinationFloor;
	}
	
	/**
	 * Checks if two ElevatorMoveCommmand have the same destination floor
	 * @param c ElevatorMoveCommand to compare with
	 * @return boolean based on if their destination floors are the same
	 */
	public Boolean hasSameDestination(ElevatorMoveCommand c) {
		return this.destinationFloor == c.getDestinationFloor();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof ElevatorMoveCommand)) return false;
		ElevatorMoveCommand c = (ElevatorMoveCommand) o;
		return super.equals(c) && moveDirection.equals(c.getDirection()) && (destinationFloor == c.getDestinationFloor());
	}
}
