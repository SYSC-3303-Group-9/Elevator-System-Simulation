package elevator;

import java.nio.ByteBuffer;

public abstract class ElevatorCommand {
	private int elevatorID;
	private Fault fault;
	
	public ElevatorCommand(int elevatorID, Fault fault) {
		this.elevatorID = elevatorID;
		this.fault = fault;
	}
	
	public int getID() {
		return this.elevatorID;
	}
	
	public Fault getFault() {
		return this.fault;
	}
	
	public byte[] toBytes() {
		byte[] idBytes = ByteBuffer.allocate(4).putInt(elevatorID).array();
		byte[] faultBytes = fault.toBytes();
		byte[] commandBytes = new byte[faultBytes.length + idBytes.length];
		System.arraycopy(idBytes, 0, commandBytes, 0, idBytes.length);
		System.arraycopy(faultBytes, 0, commandBytes, idBytes.length, faultBytes.length);
		return commandBytes;
	}
	
	public static ElevatorCommand fromBytes(byte[] bytes) throws IllegalArgumentException {
		// Returns either an ElevatorDoorCommand or an ElevatorMoveCommand
		ElevatorCommand command;
		switch(bytes[0]) {
			case ElevatorDoorCommand.COMMAND_IDENTIFIER: 
				command = ElevatorDoorCommand.fromBytes(bytes);
				break;
			case ElevatorMoveCommand.COMMAND_IDENTIFIER:
				command = ElevatorMoveCommand.fromBytes(bytes);
				break;
			default:
				command = null;
				break;
		}
		if(command == null) throw new IllegalArgumentException("Byte array passed to fromBytes() does not represent an ElevatorCommand object");
		else return command;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof ElevatorCommand)) return false;
		ElevatorCommand c = (ElevatorCommand) o;
		return (elevatorID == c.getID()) && (fault == c.getFault());
	}
}
