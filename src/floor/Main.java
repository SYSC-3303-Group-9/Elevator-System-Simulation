package floor;

import java.net.SocketException;

import common.ClockSync;
import common.RuntimeConfig;
import common.SystemSync;
import floor.gui.FloorFrame;
import scheduler.SchedulerReceiver;

public class Main {
	public static void main(String[] args) throws SocketException {
		// Get the runtime config from the master application.
		RuntimeConfig config = SystemSync.sendConfigHandshake("floor");

		// Create an ElevatorFrame to display the elevators
		FloorFrame floorFrame = new FloorFrame(config.getNumFloors());

		// Create the SchedulerReceiver.
		SchedulerReceiver schedulerReceiver = new SchedulerReceiver(floorFrame);


		// Create the floor subsystem.
		FloorSubsystem floorSubsystem = new FloorSubsystem(config, floorFrame);
		
		// Create the Measurement Receiver
		MeasurementReceiver measurementReceiver = new MeasurementReceiver();

		// Create the thread.
		Thread thSchedulerReceiver = new Thread(schedulerReceiver);
		Thread thFloorSubsystem = new Thread(floorSubsystem);
		Thread thMeasurementReceiver = new Thread(measurementReceiver);

		// Sync the application clock with the other applications.
		ClockSync.sync("floor");

		// Start the threads.
		thSchedulerReceiver.start();
		thFloorSubsystem.start();
		thMeasurementReceiver.start();

		// Wait for the thread.
		try {
			thSchedulerReceiver.join();
			thFloorSubsystem.join();
			thMeasurementReceiver.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Floor Subsystem exited.");
	}
}
