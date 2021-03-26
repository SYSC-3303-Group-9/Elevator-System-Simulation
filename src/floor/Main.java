package floor;

import common.Clock;

public class Main {
	public static void main(String[] args) {
		// Sync the application clock with the other applications.
		Clock.sync("floor");
		
		// Create the floor subsystem.
		FloorSubsystem floorSubsystem = new FloorSubsystem();
		
		// Create the thread.
		Thread thread = new Thread(floorSubsystem);
		
		// Start the thread.
		thread.start();
		
		// Wait for the thread.
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Floor Subsystem exited.");
	}
}
