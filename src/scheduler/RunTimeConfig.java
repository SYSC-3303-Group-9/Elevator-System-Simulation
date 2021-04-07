package scheduler;

import java.io.IOException;
import java.nio.ByteBuffer;

public class RunTimeConfig {

	private int numFloors;
	private int numElevators;
	private String inputFile;

	public RunTimeConfig(int floors, int elevators, String filePath) {
		this.numFloors = floors;
		this.numElevators = elevators;
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
		byte[] floorElevatorByte = ByteBuffer.allocate(8).putInt(numFloors).putInt(numElevators).array();
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
	public static RunTimeConfig fromBytes(byte[] bytes) {

		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		int floors = buffer.getInt();
		int elevators = buffer.getInt();

		byte[] fileArray = new byte[bytes.length - 8];
		System.arraycopy(bytes, 8, fileArray, 0, bytes.length - 8);
		String file = new String(fileArray);

		return new RunTimeConfig(floors, elevators, file.trim());

	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof RunTimeConfig) {
			RunTimeConfig other = (RunTimeConfig) o;
			return getNumFloors() == (other.getNumFloors()) && getNumElevators() == other.getNumElevators()
					&& getInputFile().equals(other.getInputFile());
		}
		return false;
	}

	public int getNumFloors() {
		return numFloors;
	}

	public int getNumElevators() {
		return numElevators;
	}

	public String getInputFile() {
		return inputFile;
	}

	@Override
	public String toString() {
		return numFloors + " " + numElevators + " " + inputFile;
	}

}
