package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import elevator.ElevatorSubsystem;
import floor.FloorSubsystem;
import floor.InputData;
import floor.InputParser;
import scheduler.Scheduler;

public class Main {

	public static void main(String[] args) {
		FloorSubsystem floors = new FloorSubsystem();
		ElevatorSubsystem elevators = new ElevatorSubsystem();
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
		
		// TODO Remove this once parsing is hooked up to FloorSubsystem.
		try {
			File fs = new File(Main.class.getResource("/input.txt").getFile());
			BufferedReader s = new BufferedReader(new FileReader(fs));
			List<InputData> data = InputParser.parse(s);
			for (InputData x : data) {
				System.out.println(x);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("Done!");
	}

}
