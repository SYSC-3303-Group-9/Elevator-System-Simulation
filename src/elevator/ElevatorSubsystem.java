package elevator;

import floor.InputData;
import scheduler.Buffer;

public class ElevatorSubsystem implements Runnable {
	
	private Elevator elevator;
	private Buffer<InputData> schedulerToElevatorBuffer;
	private Buffer<InputData> elevatorToSchedulerBuffer;
	
	
	public ElevatorSubsystem(Elevator elevator, Buffer<InputData> schedulerToElevatorBuffer, Buffer<InputData> elevatorToSchedulerBuffer) {
		this.elevator = elevator;
		this.schedulerToElevatorBuffer = schedulerToElevatorBuffer;
		this.elevatorToSchedulerBuffer = elevatorToSchedulerBuffer;
	}

	@Override
	public void run() {
		while(true) {
			// Get new task from the scheduler.
			InputData currTask = schedulerToElevatorBuffer.get();
			
			// If the buffer was disabled and returned null, stop execution.
			if(currTask == null) {
				break;
			}
			
			// Move the elevator and notify the scheduler.
			elevator.move(currTask);
			elevatorToSchedulerBuffer.put(currTask);
		}
	}
}
