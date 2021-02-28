package main;

import elevator.ElevatorSubsystem;
import floor.FloorSubsystem;
import scheduler.Scheduler;

public class Main {

	public static void main(String[] args) {
		SystemBuilder builder = new SystemBuilder();
		
		// Create the subsystems.
		Scheduler scheduler = builder.buildScheduler();
		FloorSubsystem floor = builder.buildFloorSubsystem();
		ElevatorSubsystem elevator = builder.addElevator();

		// Create the threads for each subsystem.
		Thread floorThread = new Thread(floor);
		Thread elevatorThread = new Thread(elevator);
		Thread schedulerThread = new Thread(scheduler);

		// Start the threads.
		floorThread.start();
		elevatorThread.start();
		schedulerThread.start();

		// Wait for all threads to finish execution.
		try {
			floorThread.join();
			elevatorThread.join();
			schedulerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Done!");
	}

}
