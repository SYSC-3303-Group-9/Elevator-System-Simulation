package scheduler;

import java.io.IOException;
import java.nio.ByteBuffer;

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
		// add floor and elevator int into a byte array
		byte[] floorElevatorByte = ByteBuffer.allocate(8).putInt(floorNum).putInt(elevatorNum).array();
		// get file path byte array
		byte[] filePathByte = this.inputFile.getBytes();

		// concatenate the two byte arrays
		byte[] configArray = new byte[floorElevatorByte.length + filePathByte.length];
		ByteBuffer buff = ByteBuffer.wrap(configArray);
		buff.put(floorElevatorByte);
		buff.put(filePathByte);

		return buff.array();
	}

	/**
	 * Creates an RunTimeConfig object based on the information provided in the
	 * provided byte array
	 * 
	 * @return RunTimeConfig object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static RunTimeConfig fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {

		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		int floors = buffer.getInt();
		int elevators = buffer.getInt();

		byte[] fileArray = new byte[bytes.length - 8];
		System.arraycopy(bytes, 8, fileArray, 0, bytes.length - 8);
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
