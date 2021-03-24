package elevator;

public class ElevatorDoorCommand {
	private int elevatorID;
	private DoorState intendedDoorState;

	/**
	 * Used to construct an ElevatorDoorCommand to be sent to the ElevatorDoor by the Scheduler
	 * @param elevatorID an int indicating the ID of the elevator whose door should be changed
	 * @param intendedDoorState a DoorState indicating what the ElevatorDoor should be set to
	 */
	public ElevatorDoorCommand(int elevatorID, DoorState intendedDoorState) {
		this.elevatorID = elevatorID;
		this.intendedDoorState = intendedDoorState;
	}
	
	/**
	 * Converts an ElevatorDoorCommand object into an array of bytes to be sent with packets
	 * @return byte array representing an ElevatorDoorCommand object
	 */
	public byte[] toBytes() {
		return null;
	}
	
	/**
	 * Constructs an ElevatorDoorCommand object from a passed array of bytes
	 * @param bytes an array of bytes created from calling toBytes() on an ElevatorDoorCommand object
	 * @return the ElevatorDoorCommand object represented by the data stored in the byte array
	 */
	public static ElevatorDoorCommand fromBytes(byte[] bytes) {
		return null;
	}
	
	public int getElevatorID() {
		return this.elevatorID;
	}
	
	public DoorState getIntendedDoorState() {
		return this.intendedDoorState;
	}
	
	@Override
	public boolean equals(Object o) {
		return false;
	}
}
