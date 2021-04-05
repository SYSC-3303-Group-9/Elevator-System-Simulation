package scheduler;

import java.nio.ByteBuffer;
import java.util.Arrays;

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
	 * @return
	 */
	public static RunTimeConfig fromBytes(byte[] bytes) {

		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		int floors = buffer.getInt();
		//System.out.print("floors: " + floors);
		int elevators = buffer.getInt();
		//System.out.print("elevators: " + elevators);

		byte[] fileArray = new byte[9];
		//System.arraycopy(bytes, 8, fileArray, 0, 9);
		String file = new String(fileArray);
		//System.out.print("file: " + file);
		return new RunTimeConfig(floors, elevators, file);

	}

//	public static void main(String[] args) {
//		RunTimeConfig run = new RunTimeConfig(12, 4, "input.txt");
//
//		System.out.println(Arrays.toString(run.toBytes()));
//
//		run.fromBytes(run.toBytes());
//
//	}

}
