package elevator;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

import java.time.LocalTime;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import floor.InputData;
import common.Buffer;
import scheduler.Scheduler;
import scheduler.SchedulerMessage;

public class ElevatorSubsystemTest {
	Elevator elevator;
	Buffer<SchedulerMessage> messageBuffer;
	Buffer<ElevatorCommand> commandBuffer;
	ElevatorSubsystem system;
	Scheduler scheduler;
	Random ran = new Random();
	int upperBound = 25;

	@BeforeEach
	void setup() {
		messageBuffer = new Buffer<SchedulerMessage>();
		commandBuffer = new Buffer<ElevatorCommand>();
	}

	@AfterEach
	void tearDown() {
		messageBuffer = null;
		commandBuffer = null;
		system = null;
		scheduler = null;
	}

	@Test
	void moveToWaiting() {
		elevator = new Elevator(ran.nextInt(upperBound), 1);
		system = new ElevatorSubsystem(elevator);
		scheduler = new Scheduler(1, 4, messageBuffer, commandBuffer);

		// Transition to WAITING state
		system.next();
		scheduler.tick();
		assertEquals(ElevatorState.WAITING, system.getState());
	}

	@Test
	void movedElevatorUp() {
		elevator = new Elevator(ran.nextInt(upperBound), 1);
		system = new ElevatorSubsystem(elevator);
		scheduler = new Scheduler(1, 4, messageBuffer, commandBuffer);

		// Transition to WAITING state
		system.next();
		scheduler.tick();
		assertEquals(ElevatorState.WAITING, system.getState());

		// Move elevator one floor up
		InputData request = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2);
		messageBuffer.put(SchedulerMessage.fromInputData(request));
		
		//WAITING_FOR_MESSAGE
		scheduler.tick();
		
		//PROCESSING_MESSAGE
		scheduler.tick();
		
		//PROCESSING_NEW_JOB
		scheduler.tick();
		
		//SCHEDULING_ELEVATOR
		scheduler.tick();

		// Transition to MOVINGUP state
		system.next();
		assertEquals(ElevatorState.MOVINGUP, system.getState());
	}

	@Test
	void movedElevatorDown() {
		elevator = new Elevator(ran.nextInt(upperBound), 1);
		system = new ElevatorSubsystem(elevator);
		scheduler = new Scheduler(1, 4, messageBuffer, commandBuffer);

		// Transition to WAITING state
		system.next();
		scheduler.tick();
		assertEquals(ElevatorState.WAITING, system.getState());

		// Move elevator one floor up
		InputData request = new InputData(LocalTime.of(1, 1, 1, 1), 3, Direction.DOWN, 2);
		messageBuffer.put(SchedulerMessage.fromInputData(request));
		
		//WAITING_FOR_MESSAGE
		scheduler.tick();
		
		//PROCESSING_MESSAGE
		scheduler.tick();
		
		//PROCESSING_NEW_JOB
		scheduler.tick();
		
		//SCHEDULING_ELEVATOR
		scheduler.tick();

		// Transition to MOVINGDOWN state
		system.next();
		assertEquals(ElevatorState.MOVINGDOWN, system.getState());
	}

}
