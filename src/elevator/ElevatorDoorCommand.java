package elevator;

public class ElevatorDoorCommand {
	private int elevatorID;
	private DoorState intendedDoorState;
	
	public ElevatorDoorCommand(int elevatorID, DoorState intendedDoorState) {
		this.elevatorID = elevatorID;
		this.intendedDoorState = intendedDoorState;
	}
	
	public byte[] toBytes() {
		return null;
	}
	
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
