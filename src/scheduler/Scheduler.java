package scheduler;

import java.util.ArrayList;

import floor.InputData;

public class Scheduler implements Runnable {
	private ArrayList<Buffer<InputData>> schedulerToElevatorBuffers; // Will contain a scheudulerToElevatorBuffer for each elevator
	private Buffer<InputData> floorToSchedulerBuffer;
	private Buffer<InputData> schedulerToFloorBuffer;
	private ArrayList<Buffer<InputData>> elevatorToSchedulerBuffers;
	
	/**
	 * Creates a new instance of the Scheduler class.
	 * @param schedulerToElevatorBuffers A list of buffers to elevators the scheduler should send data to.
	 * @param elevatorToSchedulerBuffers A list of buffers from elevators the scheduler should wait for responses from.
	 * @param floorToSchedulerBuffer A buffer from the floor subsystem the scheduler should watch.
	 * @param schedulerToFloorBuffer A buffer to the floor subsystem the scheduler to send data to.
	 */
	public Scheduler(
			ArrayList<Buffer<InputData>> schedulerToElevatorBuffers,
			ArrayList<Buffer<InputData>> elevatorToSchedulerBuffers,
			Buffer<InputData> floorToSchedulerBuffer,
			Buffer<InputData> schedulerToFloorBuffer) {
		this.schedulerToElevatorBuffers = schedulerToElevatorBuffers;
		this.elevatorToSchedulerBuffers = elevatorToSchedulerBuffers;
		this.floorToSchedulerBuffer = floorToSchedulerBuffer;
		this.schedulerToFloorBuffer = schedulerToFloorBuffer;
	}
	
	@Override
	public void run() {	
		// For now, just passes along the item it gets from the floorToScheduler buffer to the first (and only) elevator buffer 
		// On future deliverables, will have to have more complex scheduling.
		while(true) {
			InputData input = floorToSchedulerBuffer.get();
			if (input == null) {
				break;
			}
			
			// TODO: For iteration 1 the scheduler only handles the connection of 1 elevator.
			schedulerToElevatorBuffers.get(0).put(input);
			
			// Wait for elevator response then send it back to floor.
			InputData response = elevatorToSchedulerBuffers.get(0).get();
			schedulerToFloorBuffer.put(response);
		}
		
		schedulerToElevatorBuffers.get(0).setIsDisabled(true);
		schedulerToFloorBuffer.setIsDisabled(true);
	}

}
