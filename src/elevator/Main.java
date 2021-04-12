package elevator;

import java.util.ArrayList;

import common.ClockSync;
import common.RuntimeConfig;
import common.SystemSync;
import elevator.gui.ElevatorFrame;

public class Main {
	public static void main(String[] args) {
		// Get the runtime config from the master application.
		RuntimeConfig config = SystemSync.sendConfigHandshake("elevator");
		
		// Create an ElevatorFrame to display the elevators
		ElevatorFrame elevatorFrame = new ElevatorFrame(config.getNumElevators());
		
		// Create the elevators.
		ArrayList<ElevatorSubsystem> elevators = new ArrayList<ElevatorSubsystem>();
		for (int i = 0; i < config.getNumElevators(); i++) {
			// Elevator IDs are assigned sequentially. All elevators start at floor 1.
			elevators.add(new ElevatorSubsystem(i, elevatorFrame.getElevatorPanel(i)));
		}

		// Create the elevator threads.
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (ElevatorSubsystem es : elevators) {
			threads.add(new Thread(es));
		}
		
		// Sync the application clock with the other applications.
		ClockSync.sync("elevator");

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
