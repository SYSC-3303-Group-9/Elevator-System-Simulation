package scheduler;

import java.net.SocketException;

import common.Buffer;
import common.Constants;
import elevator.ElevatorCommand;
import elevator.ElevatorCommandSender;
import elevator.ElevatorEventReceiver;
import floor.FloorReceiver;

public class Main {

	public static void main(String[] args) throws SocketException {
		
		SystemSync sync = new SystemSync();
		sync.run();
		// Create the buffers.
		Buffer<ElevatorCommand> commandBuffer = new Buffer<ElevatorCommand>();
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		
		// Create the scheduler and its receivers.
		Scheduler scheduler = new Scheduler(Constants.NUM_ELEVATORS, Constants.NUM_FLOORS, messageBuffer, commandBuffer);
		FloorReceiver floorReceiver = new FloorReceiver(messageBuffer);
		ElevatorEventReceiver elevatorCommunicator = new ElevatorEventReceiver(messageBuffer);
		ElevatorCommandSender commandSender = new ElevatorCommandSender(commandBuffer);
		
		// Create threads for each.
		Thread thScheduler = new Thread(scheduler);
		Thread thFloorReceiver = new Thread(floorReceiver);
		Thread thElevatorCommunicator = new Thread(elevatorCommunicator);
		Thread thCommandSender = new Thread(commandSender);
		
		// Start each thread.
		thScheduler.start();
		thFloorReceiver.start();
		thElevatorCommunicator.start();
		thCommandSender.start();
		
		// Wait for the threads. (Note: currently threads never finish)
		try {
			thScheduler.join();
			thFloorReceiver.join();
			thElevatorCommunicator.join();
			thCommandSender.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
