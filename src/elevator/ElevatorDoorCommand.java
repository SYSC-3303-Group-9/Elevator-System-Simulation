package elevator;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ElevatorDoorCommand extends ElevatorCommand {
	private DoorState intendedDoorState;

	/**
	 * Used to construct an ElevatorDoorCommand to be sent to the ElevatorDoor by the Scheduler
	 * @param elevatorID an int indicating the ID of the elevator whose door should be changed
	 * @param intendedDoorState a DoorState indicating what the ElevatorDoor should be set to
	 */
	public ElevatorDoorCommand(int elevatorID, int fault, DoorState intendedDoorState) {
		super(elevatorID, fault);
		this.intendedDoorState = intendedDoorState;
	}
	
	/**
	 * Converts an ElevatorDoorCommand object into an array of bytes to be sent with packets
	 * @return byte array representing an ElevatorDoorCommand object
	 */
	public byte[] toBytes() {
		byte[] elevatorCommandBytes = super.toBytes();
		byte[] doorStateBytes = intendedDoorState.name().getBytes();
		byte[] elevatorDoorCommandBytes = new byte[elevatorCommandBytes.length + doorStateBytes.length];
		System.arraycopy(elevatorCommandBytes, 0, elevatorDoorCommandBytes, 0, elevatorCommandBytes.length);
		System.arraycopy(doorStateBytes, 0, elevatorDoorCommandBytes, elevatorCommandBytes.length, doorStateBytes.length);
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
		// Use static constructor method from ElevatorCommand
		byte elevatorCommandBytes[] = new byte[8]; 
		buffer.get(elevatorCommandBytes, 0, 8);
		ElevatorCommand tempCommand = ElevatorCommand.fromBytes(elevatorCommandBytes);
		// Create byte[] with the same size as the remaining bytes in the buffer (Direction string bytes)
		byte[] doorStateBytes = new byte[buffer.remaining()];
		// Copy remaining ByteBuffer bytes over to directionBytes
		buffer.get(doorStateBytes);
		// Attempt to convert the directionBytes into a Direction enum
		DoorState doorState = null;
		String enumName = new String(doorStateBytes, Charset.defaultCharset()).trim();
		try {
			doorState = DoorState.valueOf(enumName);
		} catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		if(doorState == null) return null;
		else return new ElevatorDoorCommand(tempCommand.getID(), tempCommand.getFault(), doorState);
	}

	public DoorState getIntendedDoorState() {
		return this.intendedDoorState;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof ElevatorDoorCommand)) return false;
		ElevatorDoorCommand c = (ElevatorDoorCommand) o;
		return super.equals(c) && intendedDoorState.equals(c.getIntendedDoorState());
	}
}
