package main;

import elevator.Elevator;
import elevator.ElevatorSubsystem;
import floor.FloorSubsystem;
import floor.InputData;
import scheduler.Buffer;
import scheduler.Scheduler;

public class Main {

	public static void main(String[] args) {
		Buffer<InputData> floorSchedulerBuffer = new Buffer<InputData>();
		FloorSubsystem floors = new FloorSubsystem(floorSchedulerBuffer);
		Buffer<InputData> scheduleToElevatorBuffer = new Buffer<InputData>();
		Buffer<InputData> elevatorToScheduleBuffer = new Buffer<InputData>();
		Elevator elevator = new Elevator(0);
		ElevatorSubsystem elevators = new ElevatorSubsystem(elevator, floorSchedulerBuffer, elevatorToScheduleBuffer);
		Scheduler scheduler = new Scheduler();

		Thread floorThread = new Thread(floors);
		Thread elevatorThread = new Thread(elevators);
		Thread schedulerThread = new Thread(scheduler);

		floorThread.start();
		elevatorThread.start();
		schedulerThread.start();

		try {
			floorThread.join();
			elevatorThread.join();
			schedulerThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Done!");
	}

}
