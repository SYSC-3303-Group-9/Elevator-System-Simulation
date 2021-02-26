package elevator;

import floor.InputData;
import scheduler.Buffer;

public class ElevatorSubsystem implements Runnable {
	
	private Elevator elevator;
	private ElevatorState state;
	private Buffer<InputData> schedulerToElevatorBuffer;
	private Buffer<InputData> elevatorToSchedulerBuffer;
	
	public ElevatorSubsystem(Elevator elevator, Buffer<InputData> schedulerToElevatorBuffer,
	Buffer<InputData> elevatorToSchedulerBuffer) {
		this.elevator = elevator;
		this.state = ElevatorState.INITIAL;
		this.schedulerToElevatorBuffer = schedulerToElevatorBuffer;
		this.elevatorToSchedulerBuffer = elevatorToSchedulerBuffer;
	}
	
	@Override
	public void run() {
		while (true) {
			
			// Get new task from the scheduler.
			InputData currTask = schedulerToElevatorBuffer.get();

			stateMachine: switch (this.state) {
				
				case INITIAL:
					// Immidiately move to the next state
					this.state = ElevatorState.WAITING;
					break;
				case WAITING:
					// If the buffer was disabled and returned null, stop execution.
					if (currTask == null) {
						// Move to the final state
						this.state = ElevatorState.FINAL;
						break;
					}
				
					// Move the elevator
					elevator.move(currTask);
					
					// Change the state of the Elevator to Moving up or Moving down
					if(currTask.getDirection() == Direction.UP){
						this.state = ElevatorState.MOVINGUP;
					} else if(currTask.getDirection() == Direction.DOWN){
						this.state = ElevatorState.MOVINGDOWN;
					}
				
				case MOVINGDOWN:
				// Assuming at this point that the elevator has arrived.

				//Notify the scheduler that the elevator has moved down.
				elevatorToSchedulerBuffer.put(currTask);

				// Move to next state
				this.state = ElevatorState.WAITING;
				break;

				case MOVINGUP:
				// Assuming at this point that the elevator has arrived.

				//Notify the scheduler that the elevator has moved up.
				elevatorToSchedulerBuffer.put(currTask);

				// Move to next state
				this.state = ElevatorState.WAITING;
				break stateMachine;
			case FINAL:
				break;
			}
		}
	}
}
