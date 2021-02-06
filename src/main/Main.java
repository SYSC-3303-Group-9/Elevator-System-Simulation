package main;

import elevator.ElevatorSubsystem;
import floor.FloorSubsystem;
import scheduler.Scheduler;

public class Main {

	public static void main(String[] args) {
		SystemBuilder builder = new SystemBuilder();
		
		Scheduler scheduler = builder.buildScheduler();
		FloorSubsystem floor = builder.buildFloorSubsystem();
		ElevatorSubsystem elevator = builder.addElevator();

		Thread floorThread = new Thread(floor);
		Thread elevatorThread = new Thread(elevator);
		Thread schedulerThread = new Thread(scheduler);

		floorThread.start();
		elevatorThread.start();
		schedulerThread.start();

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
