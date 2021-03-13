package floor;

public class Main {
	public static void main(String[] args) {
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
