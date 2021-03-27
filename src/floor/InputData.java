package floor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalTime;
import elevator.Direction;
import elevator.Fault;

/**
 * Represents an elevator request.
 */
public class InputData implements Serializable {
	private static final long serialVersionUID = -3706586932015524907L;
	private LocalTime time;
	private int currentFloor;
	private Direction direction;
	private int destinationFloor;
	private Fault fault;

	/**
	 * Creates a new instance of the InputData class.
	 * 
	 * @param time             The time of the event.
	 * @param currentFloor     The floor that needs servicing.
	 * @param direction        The direction the elevator must move.
	 * @param destinationFloor The floor that the elevator must reach.
	 */
	public InputData(LocalTime time, int currentFloor, Direction direction, int destinationFloor, Fault fault) {
		if ((direction == Direction.UP && currentFloor >= destinationFloor)
				|| (direction == Direction.DOWN && currentFloor <= destinationFloor)) {
			throw new UnsupportedOperationException("Elevator cannot move " + direction + " from " + currentFloor + " to " + destinationFloor);
		}
		
		this.time = time;
		this.currentFloor = currentFloor;
		this.direction = direction;
		this.destinationFloor = destinationFloor;
		this.fault = fault;
	}

	public LocalTime getTime() {
		return time;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public Direction getDirection() {
		return direction;
	}

	public int getDestinationFloor() {
		return destinationFloor;
	}
	
	public Fault getFault() {
		return fault;
	}

	/**
	 * Converts an InputData object into a byte array
	 * 
	 * @return a byte array that corresponds to the InputData Object
	 * @throws IOException 
	 */
	public byte[] toBytes() throws IOException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
			 ObjectOutputStream out = new ObjectOutputStream(bos)) {
			out.writeObject(this);
			return bos.toByteArray();
		}
	}

	/**
	 * Converts a byte array into its corresponding InputData
	 * 
	 * @param bytes the byte array to be converted
	 * @return an InputData object
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static InputData fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			 ObjectInputStream in = new ObjectInputStream(bis)) {
			return (InputData)in.readObject();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof InputData) {
			InputData other = (InputData) o;
			return getTime().equals(other.getTime()) && getCurrentFloor() == other.getCurrentFloor()
					&& getDirection().equals(other.getDirection())
					&& getDestinationFloor() == other.getDestinationFloor()
					&& getFault().equals(other.getFault());
		}
		return false;
	}

	@Override
	public String toString() {
		return time.toString() + " " + currentFloor + " " + direction.toString() + " " + destinationFloor;
	}

}
