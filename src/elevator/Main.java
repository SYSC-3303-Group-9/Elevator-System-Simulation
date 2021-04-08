package elevator;

import java.util.ArrayList;
import scheduler.RunTimeConfig;
import common.Clock;
import common.Constants;

public class Main {
	public static void main(String[] args) {
		// Sync the application clock with the other applications.
		RunTimeConfig config = Clock.sync("elevator");

		// Create the elevators.
		ArrayList<ElevatorSubsystem> elevators = new ArrayList<ElevatorSubsystem>();
		for (int i = 0; i < config.getNumElevators(); i++) {
			// Elevator IDs are assigned sequentially. All elevators start at floor 1.
			elevators.add(new ElevatorSubsystem(new ElevatorMotor(), 1, i));
		}

		// Create the elevator threads.
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (ElevatorSubsystem es : elevators) {
			threads.add(new Thread(es));
		}

		// Start the threads.
		for (Thread t : threads) {
			t.start();
		}

		// Wait for the threads. (Note: currently threads never finish)
		try {
			for (Thread t : threads) {
				t.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
