package elevator;

import java.nio.ByteBuffer;

public class ElevatorDoorCommand extends ElevatorCommand {
	public static final byte COMMAND_IDENTIFIER = (byte) 0x02;

	/**
	 * Used to construct an ElevatorDoorCommand to be sent to the ElevatorDoor by the Scheduler
	 * @param elevatorID an int indicating the ID of the elevator whose door should be changed
	 * @param intendedDoorState a DoorState indicating what the ElevatorDoor should be set to
	 */
	public ElevatorDoorCommand(int elevatorID, Fault fault) {
		super(elevatorID, fault);
	}
	
	/**
	 * Converts an ElevatorDoorCommand object into an array of bytes to be sent with packets
	 * @return byte array representing an ElevatorDoorCommand object
	 */
	public byte[] toBytes() {
		byte[] elevatorCommandBytes = super.toBytes();
		byte[] elevatorDoorCommandBytes = new byte[elevatorCommandBytes.length + 1];
		elevatorDoorCommandBytes[0] = ElevatorDoorCommand.COMMAND_IDENTIFIER;
		System.arraycopy(elevatorCommandBytes, 0, elevatorDoorCommandBytes, 1, elevatorCommandBytes.length);
		return elevatorDoorCommandBytes;
	}
	
	/**
	 * Constructs an ElevatorDoorCommand object from a passed array of bytes
	 * @param bytes an array of bytes created from calling toBytes() on an ElevatorDoorCommand object
	 * @return the ElevatorDoorCommand object represented by the data stored in the byte array
	 */
	public static ElevatorDoorCommand fromBytes(byte[] bytes) {
		// Create a ByteBuffer for bytes
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		if(buffer.get() != ElevatorDoorCommand.COMMAND_IDENTIFIER) return null;
		int elevatorID = buffer.getInt();
		byte[] faultBytes = new byte[4];
		// Copy remaining ByteBuffer bytes over to directionBytes
		buffer.get(faultBytes);
		return new ElevatorDoorCommand(elevatorID, Fault.fromBytes(faultBytes));
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof ElevatorDoorCommand)) return false;
		ElevatorDoorCommand c = (ElevatorDoorCommand) o;
		return super.equals(c);
	}
}
