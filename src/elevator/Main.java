package elevator;

import java.util.ArrayList;

import common.Constants;

public class Main {
	public static void main(String[] args) {
		// Create the elevators.
		ArrayList<ElevatorSubsystem> elevators = new ArrayList<ElevatorSubsystem>();
		for (int i = 0; i < Constants.NUM_ELEVATORS; i++) {
			// Elevator IDs are assigned sequentially. All elevators start at floor 1.
			elevators.add(new ElevatorSubsystem(new Elevator(i, 1)));
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
