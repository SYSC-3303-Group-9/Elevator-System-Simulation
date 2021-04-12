package scheduler;

import java.io.IOException;
import java.net.SocketException;

import common.Buffer;
import common.ClockSync;
import common.RuntimeConfig;
import common.SystemSync;
import elevator.ElevatorCommand;
import elevator.ElevatorCommandSender;
import elevator.ElevatorEventReceiver;
import floor.FloorReceiver;
import scheduler.GUI.ConfigurationFrame;

public class Main {

	public static void main(String[] args) throws SocketException, InterruptedException {
		// Start the configuration UI.
		ConfigurationFrame configFrame = new ConfigurationFrame();
		
		// Create the system sync and add the config frame as a listener.
		SystemSync systemSync = new SystemSync();
		systemSync.addListener(configFrame);
		
		// Start the system sync, this will receive packets when the elevator & floor
		// subsystems are started and update the config frame.
		Thread thSystemSync = new Thread(systemSync);
		thSystemSync.start();
		
		// Wait for the system sync thread to end, this means the other subsystems
		// have been started and the config frame should allow the user to press start.
		try {
			thSystemSync.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		// Wait for the user to press the start button.
		configFrame.waitUntilDone();
		
		// Start the clock sync thread now, so any packets won't be lost while this main method executes.
		ClockSync clockSync = new ClockSync();
		Thread thClockSync = new Thread(clockSync);
		thClockSync.start();
		
		// Gather the runtime config from the GUI.
		RuntimeConfig configData = new RuntimeConfig(
				configFrame.getFloorNum(),
				configFrame.getElevatorNum(),
				configFrame.getInputFile());
		
		// Respond to the subsystem handshakes with the config data, they should start building
		// their UIs but not start executing until the clock sync signal below.
		systemSync.sendRuntimeHandshakes(configData);
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
		
		try {
			// Wait for clock handshakes, subsystems should have built their UIs and
			// be ready for imminent execution.
			thClockSync.join();
			
			// Respond to clock handshakes to start simulation.
			// Note that this method is not called at the end of the clock sync's run() since
			// that could mean the handshakes are sent before *this* method is ready.
			// Calling it here after the join() above means that everything is guaranteed to be ready.
			clockSync.sendHandshakeReponses();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
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
