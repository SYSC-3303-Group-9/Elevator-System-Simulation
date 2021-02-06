package floor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import scheduler.Buffer;


public class FloorSubsystem implements Runnable {

	private Buffer<InputData> floorToSchedulerBuffer;
	private Buffer<InputData> schedulerToFloorBuffer;

	public FloorSubsystem(Buffer<InputData> floorToSchedulerBuffer, Buffer<InputData> schedulerToFloorBuffer) {
		this.floorToSchedulerBuffer = floorToSchedulerBuffer;
		this.schedulerToFloorBuffer = schedulerToFloorBuffer;
	}

	@Override
	public void run() {
		List<InputData> data;
		// Reads input data from the provided text file and adds each line of data to
		// the Buffer after converting it to a InputData type.
		try {
			File inputFile = new File(FloorSubsystem.class.getResource("/input.txt").getFile());
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			data = InputParser.parse(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}
		
		// Send all the input data to the scheduler.
		for (InputData x : data) {
			System.out.println("Floor sending " + x);
			floorToSchedulerBuffer.put(x);
		}
		
		// Mark that all data is sent.
		floorToSchedulerBuffer.setIsDisabled(true);
		
		// Wait for responses from scheduler.
		while (true) {
			InputData x = schedulerToFloorBuffer.get();
			if (x == null) {
				break;
			}
			
			System.out.println("Floor received " + x.toString());
		}
	}

}
