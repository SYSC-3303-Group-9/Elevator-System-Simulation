package elevator;

public class ElevatorMoveCommand extends ElevatorCommand {
	private static final long serialVersionUID = 175254660284202558L;
	private Direction moveDirection;
	private int destinationFloor;
	
	/**
	 * Used to construct an ElevatorMoveCommand to be sent to the ElevatorMotor by the Scheduler
	 * @param elevatorID an int indicating the ID of the elevator to be moved
	 * @param direction the direction in which the elevator should be moved
	 */
	public ElevatorMoveCommand(int elevatorID, Fault fault, Direction direction, int destinationFloor, Direction serviceDirection) {
		super(elevatorID, fault, serviceDirection);
		this.moveDirection = direction;
		this.destinationFloor = destinationFloor;
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
	
	@Override
	public String toString() {
		return super.toString() + "move " + moveDirection;
	}
}
