package scheduler;

import java.util.ArrayList;

import floor.InputData;

public class Scheduler implements Runnable {
	private ArrayList<Buffer<InputData>> schedulerToElevatorBuffers; // Will contain a scheudulerToElevatorBuffer for each elevator
	private Buffer<InputData> floorToSchedulerBuffer;
	private InputData input;
	
	/**
	 * Scheduler constructor
	 * @param scheduleToElevatorBuffers ArrayList of buffers for each elevator that the scheduler sends InputData to
	 * @param floorToSchedulerBuffer a buffer shared with floor subsystem to get InputData from
	 */
	public Scheduler(ArrayList<Buffer<InputData>> scheduleToElevatorBuffers, Buffer<InputData> floorToSchedulerBuffer) {
		this.schedulerToElevatorBuffers = scheduleToElevatorBuffers;
		this.floorToSchedulerBuffer = floorToSchedulerBuffer;
	}
	
	@Override
	public void run() {	
		// For now, just passes along the item it gets from the floorToScheduler buffer to the first (and only) elevator buffer 
		// On future deliverables, will have to have more complex scheduling
		while(true) {
			input = floorToSchedulerBuffer.get();
			schedulerToElevatorBuffers.get(0).put(input);
		}
	}

}
