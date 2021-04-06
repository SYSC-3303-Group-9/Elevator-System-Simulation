package floor;

import java.net.SocketException;

import common.Buffer;
import common.Clock;
import elevator.ElevatorEvent;
import scheduler.SchedulerReceiver;

public class Main {
	public static void main(String[] args) throws SocketException {
		// Sync the application clock with the other applications.
		Clock.sync("floor");
		
		// Create the ElevatorEvent buffer.
		// TODO: Connect the output side of this buffer to the FloorFrame UI.
		Buffer<ElevatorEvent> elevatorEventBuffer = new Buffer<ElevatorEvent>();
		
		// Create the SchedulerReceiver.
		SchedulerReceiver schedulerReceiver = new SchedulerReceiver(elevatorEventBuffer);
		
		// Create the floor subsystem.
		FloorSubsystem floorSubsystem = new FloorSubsystem();
		
		// Create the thread.
		Thread thSchedulerReceiver = new Thread(schedulerReceiver);
		Thread thFloorSubsystem = new Thread(floorSubsystem);
		
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
