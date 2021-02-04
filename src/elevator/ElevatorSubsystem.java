package elevator;

import floor.InputData;
import scheduler.Buffer;

public class ElevatorSubsystem implements Runnable {
	
	private Elevator elevator;
	private Buffer<InputData> scheduleElevatorBuffer;
	private Buffer<InputData> elevatorScheduleBuffer;
	
	public Buffer<InputData> getElevatorScheduleBuffer() {
		return elevatorScheduleBuffer;
	}

	public void setElevatorScheduleBuffer(Buffer<InputData> elevatorScheduleBuffer) {
		this.elevatorScheduleBuffer = elevatorScheduleBuffer;
	}

	public Buffer<InputData> getScheduleElevatorBuffer() {
		return scheduleElevatorBuffer;
	}

	public void setScheduleElevatorBuffer(Buffer<InputData> scheduleElevatorBuffer) {
		this.scheduleElevatorBuffer = scheduleElevatorBuffer;
	}
	
	public ElevatorSubsystem(Elevator elevator) {
		this.elevator = elevator;
		this.scheduleElevatorBuffer = new Buffer<InputData>();
		this.elevatorScheduleBuffer = new Buffer<InputData>();
	}

	@Override
	public void run() {

		while(true) {
			if(scheduleElevatorBuffer.isDisabled()) {
				break;
			}
			else {
				InputData currTask = scheduleElevatorBuffer.get();
				if(currTask == null) {
					break;
				}
				elevator.move(currTask);
				elevatorScheduleBuffer.put(currTask);
			}
			
		}
		System.out.println("DONE MOVING ELEVATOR");
	}
}
