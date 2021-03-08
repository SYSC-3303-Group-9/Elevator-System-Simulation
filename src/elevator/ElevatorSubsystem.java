package elevator;

import floor.InputData;
import scheduler.Buffer;

public class ElevatorSubsystem implements Runnable {
	
	private Elevator elevator;
	private ElevatorState state;
	private Buffer<InputData> schedulerToElevatorBuffer;
	private Buffer<ElevatorEvent> elevatorToSchedulerBuffer;
	
	public ElevatorSubsystem(Elevator elevator, Buffer<InputData> schedulerToElevatorBuffer,
	Buffer<ElevatorEvent> elevatorToSchedulerBuffer) {
		this.elevator = elevator;
		this.state = ElevatorState.INITIAL;
		this.schedulerToElevatorBuffer = schedulerToElevatorBuffer;
		this.elevatorToSchedulerBuffer = elevatorToSchedulerBuffer;
	}
	
	@Override
	public void run() {
		InputData instructions = null;
		stateMachine: while (true) {
			switch (this.state) {
				
				case INITIAL:
					// Immediately move to the next state
					this.state = ElevatorState.WAITING;
					break;
					
				case WAITING:

					// Get new instructions from the scheduler.
					instructions = schedulerToElevatorBuffer.get();

					// If the buffer was disabled and returned null, stop execution.
					if (instructions == null) {
						// Move to the final state
						this.state = ElevatorState.FINAL;
					}
					else {
						// Change the state of the Elevator to Moving up or Moving down
						if(instructions.getDirection() == Direction.UP){
							this.state = ElevatorState.MOVINGUP;
						} else if(instructions.getDirection() == Direction.DOWN){
							this.state = ElevatorState.MOVINGDOWN;
						}
					}
					break;
				
				case MOVINGDOWN:
				{
					// Assuming at this point that the elevator has arrived.

					// Move the Elevator
					elevator.move(instructions);

					// Notify the scheduler that the elevator has moved down.
					ElevatorEvent elevatorInfo = new ElevatorEvent(instructions.getDestinationFloor(), elevator.getId());
					elevatorToSchedulerBuffer.put(elevatorInfo);

					// Move to next state
					this.state = ElevatorState.WAITING;
					break;
				}

				case MOVINGUP:
				{
					// Assuming at this point that the elevator has arrived.

					// Move the Elevator
					elevator.move(instructions);

					// Notify the scheduler that the elevator has moved up.
					ElevatorEvent elevatorInfo = new ElevatorEvent(instructions.getDestinationFloor(), elevator.getId());
					elevatorToSchedulerBuffer.put(elevatorInfo);

					// Move to next state
					this.state = ElevatorState.WAITING;
					break;
				}
		
				case FINAL:
					break stateMachine;
				}
		}
	}
}
