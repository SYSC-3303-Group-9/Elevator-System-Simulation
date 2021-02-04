package elevator;

import floor.InputData;
import scheduler.Buffer;

public class ElevatorSubsystem implements Runnable {
	
	private Elevator elevator;
	private Buffer<InputData> scheduleToElevatorBuffer;
	private Buffer<InputData> elevatorToScheduleBuffer;
	
	
	public ElevatorSubsystem(Elevator elevator, Buffer<InputData> scheduleToElevatorBuffer, Buffer<InputData> elevatorToScheduleBuffer) {
		this.elevator = elevator;
		this.scheduleToElevatorBuffer = scheduleToElevatorBuffer;
		this.elevatorToScheduleBuffer = elevatorToScheduleBuffer;
	}

	@Override
	public void run() {

		while(true) {
			InputData currTask = scheduleToElevatorBuffer.get();
			if(currTask == null) {
				break;
			}
			elevator.move(currTask);
			elevatorToScheduleBuffer.put(currTask);
		}
		System.out.println("DONE MOVING ELEVATOR");
	}
}
