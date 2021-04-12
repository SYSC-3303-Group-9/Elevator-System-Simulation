package floor;

import java.net.SocketException;

import common.Buffer;
import common.ClockSync;
import common.RuntimeConfig;
import common.SystemSync;
import elevator.ElevatorEvent;
import scheduler.SchedulerReceiver;

public class Main {
	public static void main(String[] args) throws SocketException {
		// Get the runtime config from the master application.
		RuntimeConfig config = SystemSync.sendConfigHandshake("floor");
	
		// Create the ElevatorEvent buffer.
		// TODO: Connect the output side of this buffer to the FloorFrame UI.
		Buffer<ElevatorEvent> elevatorEventBuffer = new Buffer<ElevatorEvent>();
		
		// Create the SchedulerReceiver.
		SchedulerReceiver schedulerReceiver = new SchedulerReceiver(elevatorEventBuffer);
		
		// Create the floor subsystem.
		FloorSubsystem floorSubsystem = new FloorSubsystem(config);
		
		// Create the thread.
		Thread thSchedulerReceiver = new Thread(schedulerReceiver);
		Thread thFloorSubsystem = new Thread(floorSubsystem);
		
		// Sync the application clock with the other applications.
		ClockSync.sync("floor");
		
		// Start the threads.
		thSchedulerReceiver.start();
		thFloorSubsystem.start();
		
		// Wait for the thread.
		try {
			thSchedulerReceiver.join();
			thFloorSubsystem.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Floor Subsystem exited.");
	}
}
