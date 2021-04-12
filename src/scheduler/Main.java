package scheduler;

import java.net.SocketException;

import common.Buffer;
import common.RuntimeConfig;
import common.SystemSync;
import elevator.ElevatorCommand;
import elevator.ElevatorCommandSender;
import elevator.ElevatorEventReceiver;
import floor.FloorReceiver;
import scheduler.GUI.ConfigurationFrame;

public class Main {

	public static void main(String[] args) throws SocketException, InterruptedException {
		ConfigurationFrame configFrame = new ConfigurationFrame();
		SystemSync systemSync = new SystemSync();
		systemSync.addListener(configFrame);
		
		Thread thSystemSync = new Thread(systemSync);
		thSystemSync.start();
		
		try {
			thSystemSync.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		configFrame.waitUntilDone();
		
		RuntimeConfig configData = new RuntimeConfig(
				configFrame.getFloorNum(),
				configFrame.getElevatorNum(),
				configFrame.getInputFile());
		systemSync.startSimulation(configData);
		System.out.println("Configuration done");
		
		// Create the buffers.
		Buffer<ElevatorCommand> commandBuffer = new Buffer<ElevatorCommand>();
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		
		// Create the scheduler and its receivers.
		Scheduler scheduler = new Scheduler(configFrame.getElevatorNum(), configFrame.getFloorNum(), messageBuffer, commandBuffer);
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
