package scheduler;

import java.nio.ByteBuffer;
import java.util.Arrays;

import floor.InputData;

public class RunTimeConfig {

	private int floorNum;
	private int elevatorNum;
	private String inputFile;

	public RunTimeConfig(int floors, int elevators, String filePath) {
		this.floorNum = floors;
		this.elevatorNum = elevators;
		this.inputFile = filePath;
	}

	/**
	 * Creates a byte array representing the information stored in RunTimeConfig
	 * object.
	 * 
	 * @return RunTimeConfig byte array
	 */
	public byte[] toBytes() {
		byte[] configArray = ByteBuffer.allocate(17).putInt(floorNum).putInt(elevatorNum).put(inputFile.getBytes())
				.array();
		return configArray;
	}

	/**
	 * Creates an RunTimeConfig object based on the information provided in the
	 * provided byte array
	 * 
	 * @return RunTimeConfig object
	 */
	public static RunTimeConfig fromBytes(byte[] bytes) {

		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		int floors = buffer.getInt();
		int elevators = buffer.getInt();

		byte[] fileArray = new byte[9];
		System.arraycopy(bytes, 8, fileArray, 0, 9);
		String file = new String(fileArray);
		return new RunTimeConfig(floors, elevators, file);

	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof RunTimeConfig) {
			RunTimeConfig other = (RunTimeConfig) o;
			return getFloorNum() == (other.getFloorNum()) && getElevatorNum() == other.getElevatorNum()
					&& getInputFile().equals(other.getInputFile());
		}
		return false;
	}

	public int getFloorNum() {
		return floorNum;
	}

	public int getElevatorNum() {
		return elevatorNum;
	}

	public String getInputFile() {
		return inputFile;
	}

	@Override
	public String toString() {
		return floorNum + " " + elevatorNum + " " + inputFile;
	}

}
