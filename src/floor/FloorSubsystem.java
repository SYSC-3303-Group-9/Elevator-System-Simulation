package floor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import scheduler.Buffer;

public class FloorSubsystem implements Runnable {

	private Buffer<InputData> floorandSchedulerBuffer;

	public FloorSubsystem(Buffer<InputData> floorandSchedulerBuffer)
	    {
	        this.floorandSchedulerBuffer = floorandSchedulerBuffer;
	    }

	@Override
	public void run() {
		//Reads input data from the provided text file and adds each line of data to the Buffer 
		//after converting it to a InputData type
		File inputFile = new File(getClass().getResource("/input.txt").getFile());
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		List<InputData> data = InputParser.parse(reader);
		for (InputData x : data) {
			floorandSchedulerBuffer.put(x);
			System.out.println(x);
		}

	}

}
