package elevator;

import java.util.ArrayList;

import floor.InputData;

public class ElevatorSubsystem  {

	private ArrayList<InputData> elevatorScheduleBuffer;

	public ArrayList<InputData> getElevatorScheduleBuffer() {
		return elevatorScheduleBuffer;
	}

	public void setElevatorScheduleBuffer(ArrayList<InputData> elevatorScheduleBuffer) {
		this.elevatorScheduleBuffer = elevatorScheduleBuffer;
	}
	
}
