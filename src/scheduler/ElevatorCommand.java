package scheduler;

import java.nio.charset.Charset;

import elevator.Direction;

public class ElevatorCommand {
	private Direction moveDirection;
	
	public ElevatorCommand(Direction direction) {
		this.moveDirection = direction;
	}
	
	public byte[] toBytes() {
		byte[] eventArray = moveDirection.name().getBytes();
		return eventArray;
	}
	
	public static ElevatorCommand fromBytes(byte[] bytes) {
		ElevatorCommand command = null;
		String enumName = new String(bytes, Charset.defaultCharset());
		try {
			command = new ElevatorCommand(Direction.valueOf(enumName));
		} catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		return command;
	}
	
	public Direction getDirection() {
		return this.moveDirection;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof ElevatorCommand)) return false;
		ElevatorCommand c = (ElevatorCommand) o;
		return this.getDirection().equals(c.getDirection());
	}
}
