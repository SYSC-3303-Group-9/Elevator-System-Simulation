package floor;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Arrays;

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
	 * @param time The time of the event.
	 * @param currentFloor The floor that needs servicing.
	 * @param direction The direction the elevator must move.
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
	 * @return a byte array that corresponds to the InputData Object
	 */
	public byte [] toByte() {
		
		//Converting Integer values to byte
		byte intValues [] = {new Integer(this.time.getHour()).byteValue(), new Integer(this.time.getMinute()).byteValue(),
				new Integer(this.time.getSecond()).byteValue(), new Integer(this.time.getNano()).byteValue(),
				new Integer(this.currentFloor).byteValue(), new Integer(this.destinationFloor).byteValue()};
		
		//Converting enums to byte
		byte toDirection [] = this.direction.name().getBytes();
		
		byte toBytes [] = new byte[intValues.length + toDirection.length];
		
		//Inserting byte values in one byte array
		System.arraycopy(intValues, 0, toBytes, 0, intValues.length);
		System.arraycopy(toDirection, 0, toBytes, 5, toDirection.length);
		
		//Re-enter last byte in array (Last byte maybe be removed after System.arraycopy)
		toBytes[intValues.length + toDirection.length - 1] = new Integer(this.destinationFloor).byteValue();
		
		return toBytes;
	}
	
	/**
	 * Converts a byte array into its corresponding InputData
	 * @param bytes the byte array to be converted
	 * @return an InputData object
	 */
	public static InputData fromByte(byte [] bytes) {
		
		//Order of the data coming from the byte array is known
		String receivedTime = "0" + String.valueOf(bytes[0]) + ":0" + String.valueOf(bytes[1]) + ":0" + String.valueOf(bytes[2]) +
				"." + String.valueOf(bytes[3]);
		
		String atFloorReceived = String.valueOf(bytes[4]);
		
		String toDirectionReceived;
		
		if(bytes.length == 8) {
			byte stringByte [] = {bytes[5], bytes[6]};
			toDirectionReceived = new String(stringByte, StandardCharsets.UTF_8);
		} else {
			byte stringByte [] = {bytes[5], bytes[6], bytes[7], bytes[8]};
			toDirectionReceived = new String(stringByte, StandardCharsets.UTF_8);
		}
		
		String toFloorReceived = (bytes.length == 8) ? String.valueOf(bytes[7]) : String.valueOf(bytes[9]);
		
		InputData toInputData = new InputData(LocalTime.parse(receivedTime), Integer.parseInt(atFloorReceived),
				Direction.valueOf(toDirectionReceived), Integer.parseInt(toFloorReceived));
		
		return toInputData;
	
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		
		if (o instanceof InputData) {
			InputData other = (InputData)o;
			return getTime().equals(other.getTime())
					&& getCurrentFloor() == other.getCurrentFloor()
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
