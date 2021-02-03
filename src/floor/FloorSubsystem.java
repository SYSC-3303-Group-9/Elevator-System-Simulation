package floor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import main.Main;

public class FloorSubsystem implements Runnable {

	//TODO update syntax after Buffer.java is implemented
	private Buffer floorandSchedulerBuffer;

	public FloorSubsystem(Buffer floorandSchedulerBuffer)
	    {
	        this.floorandSchedulerBuffer = floorandSchedulerBuffer;
	    }

	@Override
	public void run() {
		File inputFile = new File(Main.class.getResource("/input.txt").getFile());
		BufferedReader reader;
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
