package floor;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.time.LocalTime;
import elevator.Direction;

/**
 * Represents an elevator request.
 */
public class InputData {
	private LocalTime time;
	private int currentFloor;
	private Direction direction;
	private int destinationFloor;

	/**
	 * Creates a new instance of the InputData class.
	 * 
	 * @param time             The time of the event.
	 * @param currentFloor     The floor that needs servicing.
	 * @param direction        The direction the elevator must move.
	 * @param destinationFloor The floor that the elevator must reach.
	 */
	public InputData(LocalTime time, int currentFloor, Direction direction, int destinationFloor) {
		this.time = time;
		this.currentFloor = currentFloor;
		this.direction = direction;
		this.destinationFloor = destinationFloor;
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

	/**
	 * Converts an InputData object into a byte array
	 * 
	 * @return a byte array that corresponds to the InputData Object
	 */
	public byte[] toBytes() {

		// Converting Integer values to byte
		byte intValues[] = ByteBuffer.allocate(24).putInt(time.getHour()).putInt(time.getMinute()).putInt(time.getSecond())
				.putInt(time.getNano()).putInt(currentFloor).putInt(destinationFloor).array();

		// Converting enums to byte
		byte toDirection[] = this.direction.name().getBytes();

		byte toBytes[] = new byte[intValues.length + toDirection.length];

		// Inserting byte values in one byte array
		System.arraycopy(intValues, 0, toBytes, 0, intValues.length);
		System.arraycopy(toDirection, 0, toBytes, intValues.length, toDirection.length);
		return toBytes;
	}

	/**
	 * Converts a byte array into its corresponding InputData
	 * 
	 * @param bytes the byte array to be converted
	 * @return an InputData object
	 */
	public static InputData fromBytes(byte[] bytes) {
		// Order of the data coming from the byte array is known
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		LocalTime receivedTime = LocalTime.of(buffer.getInt(), buffer.getInt(), buffer.getInt(), buffer.getInt());
		int receivedCurrentFloor = buffer.getInt();
		int receivedDestinationFloor = buffer.getInt();
		
		byte[] receivedDirectionBytes = new byte[buffer.remaining()];
		buffer.get(receivedDirectionBytes);
		Direction receivedDirection = null;
		String enumName = new String(receivedDirectionBytes, Charset.defaultCharset());
		try {
			receivedDirection = Direction.valueOf(enumName);
		} catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		if(receivedDirection == null) return null;
		else return new InputData(receivedTime, receivedCurrentFloor, receivedDirection, receivedDestinationFloor);
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
					&& getDestinationFloor() == other.getDestinationFloor();
		}
		return false;
	}

	@Override
	public String toString() {
		return time.toString() + " " + currentFloor + " " + direction.toString() + " " + destinationFloor;
	}

}
