package floor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import scheduler.Buffer;

public class FloorSubsystem implements Runnable {

	private Buffer<InputData> floorSchedulerBuffer;

	public FloorSubsystem(Buffer<InputData> floorSchedulerBuffer) {
		this.floorSchedulerBuffer = floorSchedulerBuffer;
	}

	@Override
	public void run() {
		// Reads input data from the provided text file and adds each line of data to
		// the Buffer after converting it to a InputData type
		try {
			File inputFile = new File(FloorSubsystem.class.getResource("/input.txt").getFile());
			 BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			
			 List<InputData> data = InputParser.parse(reader);
			for (InputData x : data) {
				floorSchedulerBuffer.put(x);
				System.out.println(x);
			}
			floorSchedulerBuffer.setIsDisabled(true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
