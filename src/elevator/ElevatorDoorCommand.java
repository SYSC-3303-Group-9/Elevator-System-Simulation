package elevator;

public class ElevatorDoorCommand extends ElevatorCommand {
	private static final long serialVersionUID = -8509389953753655444L;

	/**
	 * Used to construct an ElevatorDoorCommand to be sent to the ElevatorDoor by the Scheduler
	 * @param elevatorID an int indicating the ID of the elevator whose door should be changed
	 * @param intendedDoorState a DoorState indicating what the ElevatorDoor should be set to
	 */
	public ElevatorDoorCommand(int elevatorID, Fault fault, Direction serviceDirection) {
		super(elevatorID, fault, serviceDirection);
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof ElevatorDoorCommand)) return false;
		ElevatorDoorCommand c = (ElevatorDoorCommand) o;
		return super.equals(c);
	}
	
	@Override
	public String toString() {
		return super.toString() + "open and close doors";
	}
}
