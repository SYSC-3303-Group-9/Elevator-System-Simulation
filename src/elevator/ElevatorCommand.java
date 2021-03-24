package elevator;

import java.nio.ByteBuffer;

public class ElevatorCommand {
	private int elevatorID;
	private int fault;
	
	public ElevatorCommand(int elevatorID, int fault) {
		this.elevatorID = elevatorID;
		this.fault = fault;
	}
	
	public int getID() {
		return this.elevatorID;
	}
	
	public int getFault() {
		return this.fault;
	}
	
	public byte[] toBytes() {
		return ByteBuffer.allocate(8).putInt(elevatorID).putInt(fault).array();
	}
	
	public static ElevatorCommand fromBytes(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		return new ElevatorCommand(buffer.getInt(), buffer.getInt());
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof ElevatorCommand)) return false;
		ElevatorCommand c = (ElevatorCommand) o;
		return (elevatorID == c.getID()) && (fault == c.getFault());
	}
}
