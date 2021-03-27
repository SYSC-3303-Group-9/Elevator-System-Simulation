package scheduler;

import common.Buffer;
import common.Constants;
import elevator.ElevatorMoveCommand;
import elevator.ElevatorCommunicator;
import floor.FloorReceiver;

public class Main {

	public static void main(String[] args) {
		
		SystemSync sync = new SystemSync();
		sync.run();
		// Create the buffers.
		Buffer<ElevatorMoveCommand> commandBuffer = new Buffer<ElevatorMoveCommand>();
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		
		// Create the scheduler and its receivers.
		Scheduler scheduler = new Scheduler(Constants.NUM_ELEVATORS, Constants.NUM_FLOORS, messageBuffer, commandBuffer);
		FloorReceiver floorReceiver = new FloorReceiver(messageBuffer);
		ElevatorCommunicator elevatorCommunicator = new ElevatorCommunicator(commandBuffer, messageBuffer);
		
		// Create threads for each.
		Thread thScheduler = new Thread(scheduler);
		Thread thFloorReceiver = new Thread(floorReceiver);
		Thread thElevatorCommunicator = new Thread(elevatorCommunicator);
		
		// Start each thread.
		thScheduler.start();
		thFloorReceiver.start();
		thElevatorCommunicator.start();
		
		// Wait for the threads. (Note: currently threads never finish)
		try {
			thScheduler.join();
			thFloorReceiver.join();
			thElevatorCommunicator.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
