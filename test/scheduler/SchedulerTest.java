package scheduler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import common.Buffer;
import elevator.Direction;
import elevator.ElevatorMoveCommand;
import elevator.ElevatorEvent;
import floor.InputData;

public class SchedulerTest {
	private void processNewJob(Scheduler subject, boolean shouldMoveElevator) {
		subject.tick();
		assertEquals(SchedulerState.PROCESSING_MESSAGE, subject.getState());
		subject.tick();
		assertEquals(SchedulerState.PROCESSING_NEW_JOB, subject.getState());
		if (shouldMoveElevator) {
			subject.tick();
			assertEquals(SchedulerState.SCHEDULING_ELEVATOR, subject.getState());
		}
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
	}
	
	private void processElevatorEvent(Scheduler subject, boolean shouldMoveElevator) {
		subject.tick();
		assertEquals(SchedulerState.PROCESSING_MESSAGE, subject.getState());
		subject.tick();
		assertEquals(SchedulerState.PROCESSING_ELEVATOR_EVENT, subject.getState());
		if (shouldMoveElevator) {
			subject.tick();
			assertEquals(SchedulerState.SCHEDULING_ELEVATOR, subject.getState());
		}
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
	}
	
	private void processElevatorEventUnblockJob(Scheduler subject, boolean shouldMoveElevator) {
		subject.tick();
		assertEquals(SchedulerState.PROCESSING_MESSAGE, subject.getState());
		subject.tick();
		assertEquals(SchedulerState.PROCESSING_ELEVATOR_EVENT, subject.getState());
		subject.tick();
		assertEquals(SchedulerState.PROCESSING_NEW_JOB, subject.getState());
		if (shouldMoveElevator) {
			subject.tick();
			assertEquals(SchedulerState.SCHEDULING_ELEVATOR, subject.getState());
		}
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
	}
	
	private void mimicElevatorEvent(int floor, int elevatorId, Buffer<SchedulerMessage> messageBuffer) {
		ElevatorEvent event = new ElevatorEvent(floor, elevatorId); //To-Do: assign elevator service state
		messageBuffer.put(SchedulerMessage.fromElevatorEvent(event));
	}
	
	@Test
	public void tick_shouldScheduleGoingUp() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorMoveCommand> commandBuffer = new Buffer<ElevatorMoveCommand>();
		Scheduler subject = new Scheduler(1, 3, messageBuffer, commandBuffer);
		
		// act 0: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// assert 1: Requesting 2 -> 3
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 2, Direction.UP, 3);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		
		// assert 1: move up to floor 2 for pickup
		processNewJob(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert 2: elevator was moved up to floor 3 for drop off
		processElevatorEvent(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert the command buffer has no remaining commands
		processElevatorEvent(subject, false);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldScheduleGoingUpExtended() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorMoveCommand> commandBuffer = new Buffer<ElevatorMoveCommand>();
		Scheduler subject = new Scheduler(1, 5, messageBuffer, commandBuffer);
		
		// act 0: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange 1: Requesting 3 -> 5
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 3, Direction.UP, 5);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));		
		
		// assert 1: elevator was moved up to floor 2
		processNewJob(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert 2: elevator was moved up to floor 3 for pickup
		processElevatorEvent(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert 3: elevator was moved up to floor 4
		processElevatorEvent(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(4, 0, messageBuffer);
		
		// assert 4: elevator was moved up to floor 5 for drop off
		processElevatorEvent(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(5, 0, messageBuffer);
		
		// assert the command buffer has no remaining commands
		processElevatorEvent(subject, false);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldScheduleGoingDown() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorMoveCommand> commandBuffer = new Buffer<ElevatorMoveCommand>();
		Scheduler subject = new Scheduler(1, 5, messageBuffer, commandBuffer);
		
		// act 0: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange 1: Requesting 5 -> 2
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 5, Direction.DOWN, 2);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		
		// assert 1: elevator was moved up to floor 2
		processNewJob(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert 2: elevator was moved up to floor 3
		processElevatorEvent(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert 3: elevator was moved up to floor 4
		processElevatorEvent(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(4, 0, messageBuffer);
		
		// assert 4: elevator was moved up to floor 5 for pickup
		processElevatorEvent(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(5, 0, messageBuffer);
		
		// assert 5: elevator was moved down to floor 4
		processElevatorEvent(subject, true);
		assertEquals(Direction.DOWN, commandBuffer.get().getDirection());
		mimicElevatorEvent(4, 0, messageBuffer);
		
		// assert 6: elevator was moved down to floor 3
		processElevatorEvent(subject, true);
		assertEquals(Direction.DOWN, commandBuffer.get().getDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert 7: elevator was moved down to floor 2 for drop off
		processElevatorEvent(subject, true);
		assertEquals(Direction.DOWN, commandBuffer.get().getDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert the command buffer has no remaining commands
		processElevatorEvent(subject, false);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldScheduleClosestElevator() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorMoveCommand> commandBuffer = new Buffer<ElevatorMoveCommand>();
		Scheduler subject = new Scheduler(2 /* 2 elevators */, 5, messageBuffer, commandBuffer);
		
		// act 0: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange 1: Requesting 1 -> 2
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		
		// assert 1: elevator was moved up to floor 2
		processNewJob(subject, true);
		ElevatorMoveCommand command1 = commandBuffer.get();
		assertEquals(Direction.UP, command1.getDirection());
		mimicElevatorEvent(2, command1.getID(), messageBuffer);
		
		// arrange 2: Requesting 1 -> 2
		processElevatorEvent(subject, false);
		InputData request2 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2);
		messageBuffer.put(SchedulerMessage.fromInputData(request2));
		
		// assert 2: *different* elevator was moved up to floor 2
		processNewJob(subject, true);
		ElevatorMoveCommand command2 = commandBuffer.get();
		assertEquals(Direction.UP, command1.getDirection());
		assertNotEquals(command1.getID(), command2.getID());
		mimicElevatorEvent(2, command2.getID(), messageBuffer);
		
		// assert the command buffer has no remaining commands
		processElevatorEvent(subject, false);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldScheduleElevatorMovingInSameDirection() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorMoveCommand> commandBuffer = new Buffer<ElevatorMoveCommand>();
		Scheduler subject = new Scheduler(1, 5, messageBuffer, commandBuffer);
		
		// act 0: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange 1: Requesting 1 -> 3
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 3);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		
		// assert 1: elevator was moved up to floor 2
		processNewJob(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		
		/* before mimicking elevator event, i.e., before elevator has reached floor 2 but after it was scheduled to move there. */
		InputData request2 = new InputData(LocalTime.of(1, 1, 1, 1), 2, Direction.UP, 4);
		messageBuffer.put(SchedulerMessage.fromInputData(request2));
		processNewJob(subject, false);
		/* now mimic elevator event, i.e., elevator has now reached floor 2, after having received another job starting on floor 2. */
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert 3: elevator was moved up to floor 3 to drop off first request
		processElevatorEvent(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert 4: elevator was moved up to floor 4 to drop off second request
		processElevatorEvent(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(4, 0, messageBuffer);
		
		// assert the command buffer has no remaining commands
		processElevatorEvent(subject, false);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldQueueBlockedJobsUntilReady_ChangeDirectionToStart() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorMoveCommand> commandBuffer = new Buffer<ElevatorMoveCommand>();
		Scheduler subject = new Scheduler(1, 5, messageBuffer, commandBuffer);
		
		// act 0: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange 1: Requesting 1 -> 3 *and* 2 -> 1; Elevator will have to
		// change direction to get to the pickup of the second request.
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 3);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		InputData request2 = new InputData(LocalTime.of(1, 1, 1, 1), 2, Direction.DOWN, 1);
		messageBuffer.put(SchedulerMessage.fromInputData(request2));
		
		// assert 1: elevator was moved up to floor 2
		processNewJob(subject, true);
		processNewJob(subject, false);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert 3: elevator was moved up to floor 3 to drop off first request
		processElevatorEvent(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert 4: elevator was moved down to floor 2 to pickup second request
		processElevatorEventUnblockJob(subject, true);
		assertEquals(Direction.DOWN, commandBuffer.get().getDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert 5: elevator was moved down to floor 1 to drop off second request
		processElevatorEvent(subject, true);
		assertEquals(Direction.DOWN, commandBuffer.get().getDirection());
		mimicElevatorEvent(1, 0, messageBuffer);
		
		// assert the command buffer has no remaining commands
		processElevatorEvent(subject, false);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldQueueBlockedJobsUntilReady_ContinueDirectionToStart() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorMoveCommand> commandBuffer = new Buffer<ElevatorMoveCommand>();
		Scheduler subject = new Scheduler(1, 5, messageBuffer, commandBuffer);
		
		// act 0: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange 1: Requesting 1 -> 3 *and* 4 -> 3; Elevator will have to
		// keep going in the same direction to get to the pickup of the second request.
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 3);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		InputData request2 = new InputData(LocalTime.of(1, 1, 1, 1), 4, Direction.DOWN, 3);
		messageBuffer.put(SchedulerMessage.fromInputData(request2));
		
		// assert 1: elevator was moved up to floor 2
		processNewJob(subject, true);
		processNewJob(subject, false);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert 3: elevator was moved up to floor 3 to drop off first request
		processElevatorEvent(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert 4: elevator was moved up to floor 4 to pickup second request
		processElevatorEventUnblockJob(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(4, 0, messageBuffer);
		
		// assert 5: elevator was moved down to floor3 to drop off second request
		processElevatorEvent(subject, true);
		assertEquals(Direction.DOWN, commandBuffer.get().getDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert the command buffer has no remaining commands
		processElevatorEvent(subject, false);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldQueueBlockedJobsUntilReady_MovingSameDirectionButPastPickupAlready() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorMoveCommand> commandBuffer = new Buffer<ElevatorMoveCommand>();
		Scheduler subject = new Scheduler(1, 5, messageBuffer, commandBuffer);
		
		// act 0: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange 1: Requesting 1 -> 3
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 3);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		
		// assert 1: elevator was moved up to floor 2
		processNewJob(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// arrange 3: Requesting 1 -> 2; This request also moves up but we are already on floor 2,
		// must wait until previous request finishes.
		InputData request2 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2);
		messageBuffer.put(SchedulerMessage.fromInputData(request2));
		
		// assert 3: elevator was moved up to floor 3 to drop off first request
		processElevatorEvent(subject, true);
		processNewJob(subject, false);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert 4: elevator was moved down to floor 2 on its way to pickup second request
		processElevatorEventUnblockJob(subject, true);
		assertEquals(Direction.DOWN, commandBuffer.get().getDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert 6: elevator was moved down to floor 1 to pickup second request
		processElevatorEvent(subject, true);
		assertEquals(Direction.DOWN, commandBuffer.get().getDirection());
		mimicElevatorEvent(1, 0, messageBuffer);
		
		// assert 6: elevator was moved up to floor 2 to drop off second request
		processElevatorEvent(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert the command buffer has no remaining commands
		processElevatorEvent(subject, false);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldScheduleJobForEachElevator_DifferentDirections() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorMoveCommand> commandBuffer = new Buffer<ElevatorMoveCommand>();
		Scheduler subject = new Scheduler(2 /* 2 elevators */, 5, messageBuffer, commandBuffer);
		
		// act 0: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange 1: Requesting 2 -> 4 *and* 3 -> 1; Elevator will have to
		// keep going in the same direction to get to the pickup of the second request.
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 2, Direction.UP, 4);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		InputData request2 = new InputData(LocalTime.of(1, 1, 1, 1), 3, Direction.DOWN, 1);
		messageBuffer.put(SchedulerMessage.fromInputData(request2));
		
		// assert 1: Both elevators were moved up to floor 2
		processNewJob(subject, true);
		processNewJob(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(2, 1, messageBuffer);
		
		// assert 2: Both elevators were moved up to floor 3
		processElevatorEvent(subject, true);
		processElevatorEvent(subject, true);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		assertEquals(Direction.UP, commandBuffer.get().getDirection());
		mimicElevatorEvent(3, 1, messageBuffer);
		
		// assert 3: One elevator picked up at floor 3 and moves down to floor 2
		//			 Other elevator still moving up to dropoff floor 4
		processElevatorEvent(subject, true);
		processElevatorEvent(subject, true);
		ElevatorMoveCommand command1 = commandBuffer.get();
		ElevatorMoveCommand command2 = commandBuffer.get();
		
		assertNotEquals(command1.getDirection(), command2.getDirection());
		int pickupFloor2Id = -1;
		int pickupFloor3Id = -1;
		if (command1.getDirection() == Direction.UP && command2.getDirection() == Direction.DOWN) {
			pickupFloor2Id = command1.getID();
			pickupFloor3Id = command2.getID();
		} 
		else if (command1.getDirection() == Direction.DOWN && command2.getDirection() == Direction.UP) {
			pickupFloor2Id = command2.getID();
			pickupFloor3Id = command1.getID();
		}
		assertNotEquals(-1, pickupFloor2Id);
		assertNotEquals(-1, pickupFloor3Id);
		
		mimicElevatorEvent(4, pickupFloor2Id, messageBuffer);
		mimicElevatorEvent(2, pickupFloor3Id, messageBuffer);
		
		// assert 4: Elevator that picked up at floor 2 should have dropped off at floor 4.
		//			 Elevator that picked up at floor 3 should still be moving down to floor 1.
		processElevatorEvent(subject, false);
		processElevatorEvent(subject, true);
		assertEquals(Direction.DOWN, commandBuffer.get().getDirection());
		mimicElevatorEvent(1, 0, messageBuffer);
		
		// assert the command buffer has no remaining commands
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
}
