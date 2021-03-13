package scheduler;

import elevator.ElevatorEvent;
import floor.InputData;

/**
 * Represents a union of InputData and ElevatorEvent.
 * Used for the input buffer of the scheduler, otherwise the scheduler
 * might be waiting on one buffer while the other fills.
 */
public class SchedulerMessage {
	private InputData inputData;
	private ElevatorEvent elevatorEvent;
	
	/**
	 * Construct a new scheduler message.
	 * Note this is private so both input types can not be non-null.
	 * @param inputData The input data.
	 * @param elevatorEvent The elevator event.
	 */
	private SchedulerMessage(InputData inputData, ElevatorEvent elevatorEvent) {
		this.inputData = inputData;
		this.elevatorEvent = elevatorEvent;
	}
	
	/**
	 * Construct a message containing only InputData.
	 * @param inputData The input data.
	 * @return The message.
	 */
	public static SchedulerMessage fromInputData(InputData inputData) {
		return new SchedulerMessage(inputData, null);
	}
	
	/**
	 * Construct a message containing only ElevatorEvent.
	 * @param elevatorEvent The elevator event.
	 * @return The message.
	 */
	public static SchedulerMessage fromElevatorEvent(ElevatorEvent elevatorEvent) {
		return new SchedulerMessage(null, elevatorEvent);
	}
	
	/**
	 * Gets the input data.
	 * @return The input data.
	 */
	public InputData getInputData() {
		return inputData;
	}
	
	/**
	 * Gets the elevator event.
	 * @return The elevator event.
	 */
	public ElevatorEvent getElevatorEvent() {
		return elevatorEvent;
	}
}
