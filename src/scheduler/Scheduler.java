package scheduler;

import java.util.ArrayList;

import elevator.ElevatorEvent;
import floor.InputData;

public class Scheduler implements Runnable {
	private SchedulerState state;
	
	private ArrayList<Buffer<InputData>> schedulerToElevatorBuffers; // Will contain a scheudulerToElevatorBuffer for each elevator
	private Buffer<InputData> floorToSchedulerBuffer;
	private Buffer<ElevatorEvent> schedulerToFloorBuffer;
	private ArrayList<Buffer<ElevatorEvent>> elevatorToSchedulerBuffers;
	
	/**
	 * Creates a new instance of the Scheduler class.
	 * @param schedulerToElevatorBuffers A list of buffers to elevators the scheduler should send data to.
	 * @param elevatorToSchedulerBuffers A list of buffers from elevators the scheduler should wait for responses from.
	 * @param floorToSchedulerBuffer A buffer from the floor subsystem the scheduler should watch.
	 * @param schedulerToFloorBuffer A buffer to the floor subsystem the scheduler to send data to.
	 */
	public Scheduler(
			ArrayList<Buffer<InputData>> schedulerToElevatorBuffers,
			ArrayList<Buffer<ElevatorEvent>> elevatorToSchedulerBuffers,
			Buffer<InputData> floorToSchedulerBuffer,
			Buffer<ElevatorEvent> schedulerToFloorBuffer) {
		this.state = SchedulerState.INITIAL;
		
		this.schedulerToElevatorBuffers = schedulerToElevatorBuffers;
		this.elevatorToSchedulerBuffers = elevatorToSchedulerBuffers;
		this.floorToSchedulerBuffer = floorToSchedulerBuffer;
		this.schedulerToFloorBuffer = schedulerToFloorBuffer;
	}
	
	@Override
	public void run() {	
		// For now, just passes along the item it gets from the floorToScheduler buffer to the first (and only) elevator buffer 
		// On future deliverables, will have to have more complex scheduling.
		stateMachine: while(true) {
			switch (this.state) {
			case INITIAL:
				// Immediately move to next state.
				this.state = SchedulerState.WAITING_FOR_INSTRUCTION;
				break;
			case WAITING_FOR_INSTRUCTION:
				// Block until the FloorSubsystem.
				InputData input = floorToSchedulerBuffer.get();
				
				// If the input is null move to the Final state.
				if (input == null) {
					this.state = SchedulerState.FINAL;
				}
				// Otherwise send instructions to ElevatorSubsystem and go to WaitingForElevator.
				else {
					// TODO: For iteration 1 the scheduler only handles the connection of 1 elevator.
					System.out.println("[" + input.getTime() + "] Scheduling elevator to pick up at floor "
							+ input.getCurrentFloor() + " and move to floor " + input.getDestinationFloor());
					
					schedulerToElevatorBuffers.get(0).put(input);
					this.state = SchedulerState.WAITING_FOR_ELEVATOR;
				}
				break;
			case WAITING_FOR_ELEVATOR:
				// Wait for elevator response then send it back to floor.
				ElevatorEvent response = elevatorToSchedulerBuffers.get(0).get();
				schedulerToFloorBuffer.put(response);
				this.state = SchedulerState.WAITING_FOR_INSTRUCTION;
				break;
			case FINAL:
				// Disable the buffers to propagate the thread shutdowns.
				schedulerToElevatorBuffers.get(0).setIsDisabled(true);
				schedulerToFloorBuffer.setIsDisabled(true);
				break stateMachine;
			}
		}
	}
}
