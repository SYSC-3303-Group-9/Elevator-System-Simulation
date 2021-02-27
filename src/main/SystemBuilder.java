package main;

import java.util.ArrayList;

import elevator.Elevator;
import elevator.ElevatorEvent;
import elevator.ElevatorSubsystem;
import floor.FloorSubsystem;
import floor.InputData;
import scheduler.Buffer;
import scheduler.Scheduler;

/**
 * Helper class to manage the buffer links between subsystems.
 */
public class SystemBuilder {
	private Buffer<ElevatorEvent> schedulerToFloorBuffer = new Buffer<ElevatorEvent>();
	private ArrayList<Buffer<InputData>> schedulerToElevatorBuffers = new ArrayList<Buffer<InputData>>();
	
	private Buffer<InputData> floorToSchedulerBuffer = new Buffer<InputData>();
	
	private ArrayList<Buffer<ElevatorEvent>> elevatorToSchedulerBuffers = new ArrayList<Buffer<ElevatorEvent>>();
	
	/**
	 * Builds a scheduler instance with the corresponding buffers attached.
	 * @return A new scheduler instance.
	 */
	public Scheduler buildScheduler() {
		return new Scheduler(schedulerToElevatorBuffers, elevatorToSchedulerBuffers, floorToSchedulerBuffer, schedulerToFloorBuffer);
	}
	
	/**
	 * Builds a floor subsystem instance with the corresponding buffers attached.
	 * @return A new floor subsystem instance.
	 */
	public FloorSubsystem buildFloorSubsystem() {
		return new FloorSubsystem(floorToSchedulerBuffer, schedulerToFloorBuffer);
	}
	
	/**
	 * Builds an elevator subsystem with the corresponding buffers attached and links it
	 * to the scheduler's buffer lists.
	 * 
	 * Calling this multiple times will add multiple elevator instances to the system.
	 * 
	 * TODO: At iteration 1 the extra elevator instances are ignored by the scheduler.
	 * @return A new elevator subsystem instance.
	 */
	public ElevatorSubsystem addElevator() {
		Buffer<InputData> schedulerToElevatorBuffer = new Buffer<InputData>();
		schedulerToElevatorBuffers.add(schedulerToElevatorBuffer);
		
		Buffer<ElevatorEvent> elevatorToSchedulerBuffer = new Buffer<ElevatorEvent>();
		elevatorToSchedulerBuffers.add(elevatorToSchedulerBuffer);
		
		Elevator elevator = new Elevator(schedulerToElevatorBuffers.size());
		return new ElevatorSubsystem(elevator, schedulerToElevatorBuffer, elevatorToSchedulerBuffer);
	}
}
