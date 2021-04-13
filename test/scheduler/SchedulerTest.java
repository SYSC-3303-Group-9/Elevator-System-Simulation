package scheduler;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import common.Buffer;
import elevator.Direction;
import elevator.ElevatorCommand;
import elevator.ElevatorDoorCommand;
import elevator.ElevatorMoveCommand;
import elevator.Fault;
import elevator.ElevatorEvent;
import floor.InputData;

public class SchedulerTest {
	enum ProcessType {
		MOVE,
		DOOR,
		NONE
	}
	
	private void processNewJob(Scheduler subject, ProcessType t) {
		subject.tick();
		assertEquals(SchedulerState.PROCESSING_NEW_JOB, subject.getState());
		if (t.equals(ProcessType.MOVE)) {
			subject.tick();
			assertEquals(SchedulerState.SCHEDULE_MOVE, subject.getState());
		}
		else if (t.equals(ProcessType.DOOR)) {
			subject.tick();
			assertEquals(SchedulerState.SCHEDULE_DOOR, subject.getState());
		}
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
	}
	
	private void processElevatorEvent(Scheduler subject, ProcessType t) {
		subject.tick();
		assertEquals(SchedulerState.PROCESSING_ELEVATOR_EVENT, subject.getState());
		if (t.equals(ProcessType.MOVE)) {
			subject.tick();
			assertEquals(SchedulerState.SCHEDULE_MOVE, subject.getState());
		}
		else if (t.equals(ProcessType.DOOR)) {
			subject.tick();
			assertEquals(SchedulerState.SCHEDULE_DOOR, subject.getState());
		}
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
	}
	
	private void mimicElevatorEvent(int floor, int elevatorId, Buffer<SchedulerMessage> messageBuffer) {
		ElevatorEvent event = new ElevatorEvent(floor, elevatorId, false, false, Direction.WAITING);
		messageBuffer.put(SchedulerMessage.fromElevatorEvent(event));
	}
	
	private void mimicElevatorFaultEvent(int floor, int elevatorId, Buffer<SchedulerMessage> messageBuffer) {
		ElevatorEvent event = new ElevatorEvent(floor, elevatorId, true, false, Direction.WAITING);
		messageBuffer.put(SchedulerMessage.fromElevatorEvent(event));
	}
	
	@Test
	public void tick_shouldScheduleGoingUp() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorCommand> commandBuffer = new Buffer<ElevatorCommand>();
		Scheduler subject = new Scheduler(1, 3, messageBuffer, commandBuffer);
		ElevatorDoorCommand doorCmd;
		ElevatorMoveCommand moveCmd;
		
		// act: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange: requesting 2 -> 3
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 2, Direction.UP, 3, Fault.NONE);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		
		// assert: move up to floor 2
		processNewJob(subject, ProcessType.MOVE);
		moveCmd = (ElevatorMoveCommand)commandBuffer.get();
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: open doors for pickup
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: move up to floor 3
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = (ElevatorMoveCommand)commandBuffer.get();
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert: open doors for drop off
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert: command buffer has no remaining commands
		processElevatorEvent(subject, ProcessType.NONE);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldScheduleGoingUpExtended() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorCommand> commandBuffer = new Buffer<ElevatorCommand>();
		Scheduler subject = new Scheduler(1, 5, messageBuffer, commandBuffer);
		ElevatorDoorCommand doorCmd;
		ElevatorMoveCommand moveCmd;
		
		// act: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange: requesting 3 -> 5
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 3, Direction.UP, 5, Fault.NONE);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));		
		
		// assert: move up to floor 2
		processNewJob(subject, ProcessType.MOVE);
		moveCmd = (ElevatorMoveCommand)commandBuffer.get();
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: move up to floor 3
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = (ElevatorMoveCommand)commandBuffer.get();
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert: open doors for pickup
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert: move up to floor 4
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = (ElevatorMoveCommand)commandBuffer.get();
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(4, 0, messageBuffer);
		
		// assert: move up to floor 5
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = (ElevatorMoveCommand)commandBuffer.get();
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(5, 0, messageBuffer);
		
		// assert: open doors for drop off
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(5, 0, messageBuffer);
		
		// assert: command buffer has no remaining commands
		processElevatorEvent(subject, ProcessType.NONE);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldScheduleGoingDown() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorCommand> commandBuffer = new Buffer<ElevatorCommand>();
		Scheduler subject = new Scheduler(1, 5, messageBuffer, commandBuffer);
		ElevatorDoorCommand doorCmd;
		ElevatorMoveCommand moveCmd;
		
		// act: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange: requesting 5 -> 2
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 5, Direction.DOWN, 2, Fault.NONE);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		
		// assert: move up to floor 2
		processNewJob(subject, ProcessType.MOVE);
		moveCmd = (ElevatorMoveCommand)commandBuffer.get();
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.DOWN, moveCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: move up to floor 3
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = (ElevatorMoveCommand)commandBuffer.get();
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.DOWN, moveCmd.getServiceDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert: move up to floor 4
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = (ElevatorMoveCommand)commandBuffer.get();
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.DOWN, moveCmd.getServiceDirection());
		mimicElevatorEvent(4, 0, messageBuffer);
		
		// assert: move up to floor 5
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = (ElevatorMoveCommand)commandBuffer.get();
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.DOWN, moveCmd.getServiceDirection());
		mimicElevatorEvent(5, 0, messageBuffer);
		
		// assert: open doors for pickup
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.DOWN, doorCmd.getServiceDirection());
		mimicElevatorEvent(5, 0, messageBuffer);
		
		// assert: move down to floor 4
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = (ElevatorMoveCommand)commandBuffer.get();
		assertEquals(Direction.DOWN, moveCmd.getDirection());
		assertEquals(Direction.DOWN, moveCmd.getServiceDirection());
		mimicElevatorEvent(4, 0, messageBuffer);
		
		// assert: move down to floor 3
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = (ElevatorMoveCommand)commandBuffer.get();
		assertEquals(Direction.DOWN, moveCmd.getDirection());
		assertEquals(Direction.DOWN, moveCmd.getServiceDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert: move down to floor 2
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = (ElevatorMoveCommand)commandBuffer.get();
		assertEquals(Direction.DOWN, moveCmd.getDirection());
		assertEquals(Direction.DOWN, moveCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: open doors for drop off
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.DOWN, doorCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: command buffer has no remaining commands
		processElevatorEvent(subject, ProcessType.NONE);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldScheduleClosestElevator() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorCommand> commandBuffer = new Buffer<ElevatorCommand>();
		Scheduler subject = new Scheduler(2 /* 2 elevators */, 5, messageBuffer, commandBuffer);
		ElevatorDoorCommand doorCmd;
		ElevatorMoveCommand moveCmd;
		
		// act: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange: requesting 1 -> 2
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.NONE);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		
		// assert: E0 open doors for pickup
		processNewJob(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(1, 0, messageBuffer);
		
		// assert: E0 move up to floor 2
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		assertEquals(0, moveCmd.getID());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: E0 open doors for drop off
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		processElevatorEvent(subject, ProcessType.NONE);
		
		// arrange: requesting 1 -> 2
		InputData request2 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.NONE);
		messageBuffer.put(SchedulerMessage.fromInputData(request2));
		
		// assert: E1 open doors for pickup
		processNewJob(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(1, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(1, 1, messageBuffer);
		
		// assert: E1 move up to floor 2
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		assertEquals(1, moveCmd.getID());
		mimicElevatorEvent(2, 1, messageBuffer);
		
		// assert: E1 open doors for drop off
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(1, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(2, 1, messageBuffer);
		
		// assert: command buffer has no remaining commands
		processElevatorEvent(subject, ProcessType.NONE);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldScheduleElevatorMovingInSameDirection() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorCommand> commandBuffer = new Buffer<ElevatorCommand>();
		Scheduler subject = new Scheduler(1, 5, messageBuffer, commandBuffer);
		ElevatorDoorCommand doorCmd;
		ElevatorMoveCommand moveCmd;
		
		// act: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange 1: requesting 1 -> 3
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 3, Fault.NONE);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		
		// assert: open doors for pickup
		processNewJob(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(1, 0, messageBuffer);
		
		// assert: move up to floor 2
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		
		/* request 2 -> 4 before mimicking elevator event, i.e., before elevator has reached floor 2 but after it was scheduled to move there. */
		InputData request2 = new InputData(LocalTime.of(1, 1, 1, 1), 2, Direction.UP, 4, Fault.NONE);
		messageBuffer.put(SchedulerMessage.fromInputData(request2));
		processNewJob(subject, ProcessType.NONE);
		
		/* now mimic elevator event, i.e., elevator has now reached floor 2, after having received another job starting on floor 2. */
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: open doors for pickup
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: move up to floor 3
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert: open doors for drop off
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert: move up to floor 4
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(4, 0, messageBuffer);
		
		// assert: open doors for drop off
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(4, 0, messageBuffer);
		
		// assert: command buffer has no remaining commands
		processElevatorEvent(subject, ProcessType.NONE);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldQueueBlockedJobsUntilReady_ChangeDirectionToStart() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorCommand> commandBuffer = new Buffer<ElevatorCommand>();
		Scheduler subject = new Scheduler(1, 5, messageBuffer, commandBuffer);
		ElevatorDoorCommand doorCmd;
		ElevatorMoveCommand moveCmd;
		
		// act: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange: requesting 1 -> 3 *and* 2 -> 1; Elevator will have to
		// change direction to get to the pickup of the second request.
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 3, Fault.NONE);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		InputData request2 = new InputData(LocalTime.of(1, 1, 1, 1), 2, Direction.DOWN, 1, Fault.NONE);
		messageBuffer.put(SchedulerMessage.fromInputData(request2));
		
		// assert: open doors for pickup
		processNewJob(subject, ProcessType.DOOR);
		processNewJob(subject, ProcessType.NONE);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(1, 0, messageBuffer);
		
		// assert: move up to floor 2
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: move up to floor 3
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert: open doors for drop off
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.DOWN, doorCmd.getServiceDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert: move down to floor 2, unblock queued job
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.DOWN, moveCmd.getDirection());
		assertEquals(Direction.DOWN, moveCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: open doors for pick up
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.DOWN, doorCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: move down to floor 1
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.DOWN, moveCmd.getDirection());
		assertEquals(Direction.DOWN, moveCmd.getServiceDirection());
		mimicElevatorEvent(1, 0, messageBuffer);
		
		// assert: open doors for drop off
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.DOWN, doorCmd.getServiceDirection());
		mimicElevatorEvent(1, 0, messageBuffer);
		
		// assert: command buffer has no remaining commands
		processElevatorEvent(subject, ProcessType.NONE);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldQueueBlockedJobsUntilReady_ContinueDirectionToStart() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorCommand> commandBuffer = new Buffer<ElevatorCommand>();
		Scheduler subject = new Scheduler(1, 5, messageBuffer, commandBuffer);
		ElevatorDoorCommand doorCmd;
		ElevatorMoveCommand moveCmd;
		
		// act: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange: requesting 1 -> 3 *and* 4 -> 3; Elevator will have to
		// keep going in the same direction to get to the pickup of the second request.
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 3, Fault.NONE);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		InputData request2 = new InputData(LocalTime.of(1, 1, 1, 1), 4, Direction.DOWN, 3, Fault.NONE);
		messageBuffer.put(SchedulerMessage.fromInputData(request2));
		
		// assert: open doors for pick up
		processNewJob(subject, ProcessType.DOOR);
		processNewJob(subject, ProcessType.NONE);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(1, 0, messageBuffer);
		
		// assert: move up to floor 2
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: move up to floor 3
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert: open doors for drop off
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.DOWN, doorCmd.getServiceDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert: move up to floor 4
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.DOWN, moveCmd.getServiceDirection());
		mimicElevatorEvent(4, 0, messageBuffer);
		
		// assert: open doors for pick up
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.DOWN, doorCmd.getServiceDirection());
		mimicElevatorEvent(4, 0, messageBuffer);
		
		// assert: move down to floor 3
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.DOWN, moveCmd.getDirection());
		assertEquals(Direction.DOWN, moveCmd.getServiceDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert: open doors for drop off
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.DOWN, doorCmd.getServiceDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert: command buffer has no remaining commands
		processElevatorEvent(subject, ProcessType.NONE);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldQueueBlockedJobsUntilReady_MovingSameDirectionButPastPickupAlready() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorCommand> commandBuffer = new Buffer<ElevatorCommand>();
		Scheduler subject = new Scheduler(1, 5, messageBuffer, commandBuffer);
		ElevatorDoorCommand doorCmd;
		ElevatorMoveCommand moveCmd;
		
		// act: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange: requesting 1 -> 3
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 3, Fault.NONE);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		
		// assert: open doors for pick up
		processNewJob(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(1, 0, messageBuffer);
		
		// assert: move up to floor 2
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// arrange: requesting 1 -> 2; This request also moves up but we are already on floor 2,
		// must wait until previous request finishes.
		InputData request2 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.NONE);
		messageBuffer.put(SchedulerMessage.fromInputData(request2));
		
		// assert: move up to floor 3
		processElevatorEvent(subject, ProcessType.MOVE);
		processNewJob(subject, ProcessType.NONE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert: open doors for drop off
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert: move down to floor 2
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.DOWN, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: move down to floor 1
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.DOWN, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(1, 0, messageBuffer);
		
		// assert: open doors for pick up
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(1, 0, messageBuffer);
		
		// assert: move up to floor 2
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: open doors for drop off
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: command buffer has no remaining commands
		processElevatorEvent(subject, ProcessType.NONE);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldScheduleJobForEachElevator_DifferentDirections() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorCommand> commandBuffer = new Buffer<ElevatorCommand>();
		Scheduler subject = new Scheduler(2 /* 2 elevators */, 5, messageBuffer, commandBuffer);
		ElevatorDoorCommand doorCmd;
		ElevatorMoveCommand moveCmd;
		
		// act: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange: requesting 2 -> 4 *and* 3 -> 1; Elevator will have to
		// keep going in the same direction to get to the pickup of the second request.
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 2, Direction.UP, 4, Fault.NONE);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		InputData request2 = new InputData(LocalTime.of(1, 1, 1, 1), 3, Direction.DOWN, 1, Fault.NONE);
		messageBuffer.put(SchedulerMessage.fromInputData(request2));
		
		// assert: E0 and E1 move up to floor 2
		processNewJob(subject, ProcessType.MOVE);
		processNewJob(subject, ProcessType.MOVE);
		
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.DOWN, moveCmd.getServiceDirection());
		mimicElevatorEvent(2, 1, messageBuffer);
		
		// assert: E0 opens doors for pickup; E1 moves up to floor 3
		processElevatorEvent(subject, ProcessType.DOOR);
		processElevatorEvent(subject, ProcessType.MOVE);
		
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.DOWN, moveCmd.getServiceDirection());
		
		mimicElevatorEvent(2, 0, messageBuffer);
		mimicElevatorEvent(3, 1, messageBuffer);
		
		// assert: E0 moves up to floor 3; E1 opens doors for pick up
		processElevatorEvent(subject, ProcessType.MOVE);
		processElevatorEvent(subject, ProcessType.DOOR);
		
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(1, doorCmd.getID());
		assertEquals(Direction.DOWN, doorCmd.getServiceDirection());
		
		mimicElevatorEvent(3, 0, messageBuffer);
		mimicElevatorEvent(3, 1, messageBuffer);
		
		// assert: E0 moves up to floor 4; E1 moves down to floor 2
		processElevatorEvent(subject, ProcessType.MOVE);
		processElevatorEvent(subject, ProcessType.MOVE);

		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.DOWN, moveCmd.getDirection());
		assertEquals(Direction.DOWN, moveCmd.getServiceDirection());
		
		mimicElevatorEvent(4, 0, messageBuffer);
		mimicElevatorEvent(2, 1, messageBuffer);
		
		// assert: E0 opens doors for drop off; E1 moves down to floor 1
		processElevatorEvent(subject, ProcessType.DOOR);
		processElevatorEvent(subject, ProcessType.MOVE);

		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.DOWN, moveCmd.getDirection());
		assertEquals(Direction.DOWN, moveCmd.getServiceDirection());
		
		mimicElevatorEvent(4, 0, messageBuffer);
		mimicElevatorEvent(1, 1, messageBuffer);
		
		// assert: E1 opens doors for drop off
		processElevatorEvent(subject, ProcessType.NONE);
		processElevatorEvent(subject, ProcessType.DOOR);

		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(1, doorCmd.getID());
		assertEquals(Direction.DOWN, doorCmd.getServiceDirection());
		
		mimicElevatorEvent(1, 1, messageBuffer);
		
		// assert: command buffer has no remaining commands
		processElevatorEvent(subject, ProcessType.NONE);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldShutdownWhenAllElevatorsFailed() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorCommand> commandBuffer = new Buffer<ElevatorCommand>();
		Scheduler subject = new Scheduler(1 /* 1 elevators */, 5, messageBuffer, commandBuffer);
		
		// act: move out of initial state.
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange: requesting 1 -> 2 with a permanent fault
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.PERMANENT);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		
		// assert: elevator returns permanent fault
		processNewJob(subject, ProcessType.DOOR);
		assertEquals(Fault.PERMANENT, commandBuffer.get().getFault());
		mimicElevatorFaultEvent(1, 0, messageBuffer);
		
		// assert: scheduler shuts down when only elevator faults
		subject.tick();
		assertEquals(SchedulerState.PROCESSING_ELEVATOR_EVENT, subject.getState());
		subject.tick();
		assertEquals(SchedulerState.FINAL, subject.getState());
		assertTrue(subject.tick());
		
		// assert: command buffer has no remaining commands
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldNotApplyFaultUntilJobIsStarted() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorCommand> commandBuffer = new Buffer<ElevatorCommand>();
		Scheduler subject = new Scheduler(1 /* 1 elevators */, 5, messageBuffer, commandBuffer);
		ElevatorDoorCommand doorCmd;
		ElevatorMoveCommand moveCmd;
		
		// act: move out of initial state.
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange: requesting 1 -> 2 and 2 -> 3 with a permanent fault
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.NONE);
		InputData request2 = new InputData(LocalTime.of(1, 1, 1, 1), 2, Direction.UP, 3, Fault.PERMANENT);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		messageBuffer.put(SchedulerMessage.fromInputData(request2));
		
		// assert: open doors for pickup
		processNewJob(subject, ProcessType.DOOR);
		processNewJob(subject, ProcessType.NONE);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Fault.NONE, doorCmd.getFault());
		mimicElevatorEvent(1, 0, messageBuffer);
		
		// assert: move up to floor 2
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		assertEquals(Fault.NONE, moveCmd.getFault());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: open doors for pickup and drop off, should have permanent fault.
		processElevatorEvent(subject, ProcessType.DOOR);
		assertEquals(Fault.PERMANENT, commandBuffer.get().getFault());
		mimicElevatorFaultEvent(2, 0, messageBuffer);
		
		// assert: scheduler shuts down when only elevator faults
		subject.tick();
		assertEquals(SchedulerState.PROCESSING_ELEVATOR_EVENT, subject.getState());
		subject.tick();
		assertEquals(SchedulerState.FINAL, subject.getState());
		assertTrue(subject.tick());
		
		// assert: command buffer has no remaining commands
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldReassignElevatorWhenPermanentFault() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorCommand> commandBuffer = new Buffer<ElevatorCommand>();
		Scheduler subject = new Scheduler(2 /* 2 elevators */, 5, messageBuffer, commandBuffer);
		ElevatorDoorCommand doorCmd;
		ElevatorMoveCommand moveCmd;
		
		// act: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange: requesting 1 -> 2 with a permanent fault
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.PERMANENT);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		
		// assert: E0 return permanent fault
		processNewJob(subject, ProcessType.DOOR);
		assertEquals(Fault.PERMANENT, commandBuffer.get().getFault());
		mimicElevatorFaultEvent(1, 0, messageBuffer);
		
		// assert: E1 handles request instead; E1 opens doors for pick up
		processElevatorEvent(subject, ProcessType.NONE);
		// job to reassign should be "new"
		processNewJob(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(1, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(1, 1, messageBuffer);
		
		// assert: E1 moves up to floor 2
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(2, 1, messageBuffer);
		
		// assert: E1 open doors for drop off
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(1, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(2, 1, messageBuffer);
		
		// assert: command buffer has no remaining commands
		processElevatorEvent(subject, ProcessType.NONE);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
	
	@Test
	public void tick_shouldOpenDoorsOnceForPickupAndDropOffOnSameFloor() {
		// arrange subject
		Buffer<SchedulerMessage> messageBuffer = new Buffer<SchedulerMessage>();
		Buffer<ElevatorCommand> commandBuffer = new Buffer<ElevatorCommand>();
		Scheduler subject = new Scheduler(1 /* 1 elevators */, 5, messageBuffer, commandBuffer);
		ElevatorDoorCommand doorCmd;
		ElevatorMoveCommand moveCmd;
		
		// act: move out of initial state
		subject.tick();
		assertEquals(SchedulerState.WAITING_FOR_MESSAGE, subject.getState());
		
		// arrange: requesting 1 -> 2 and 2 -> 3
		InputData request1 = new InputData(LocalTime.of(1, 1, 1, 1), 1, Direction.UP, 2, Fault.NONE);
		messageBuffer.put(SchedulerMessage.fromInputData(request1));
		InputData request2 = new InputData(LocalTime.of(1, 1, 1, 1), 2, Direction.UP, 3, Fault.NONE);
		messageBuffer.put(SchedulerMessage.fromInputData(request2));
		
		// assert: open doors for pickup
		processNewJob(subject, ProcessType.DOOR);
		processNewJob(subject, ProcessType.NONE);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(1, 0, messageBuffer);
		
		// assert: move up to floor 2
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: open doors for pickup *and* drop off
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(2, 0, messageBuffer);
		
		// assert: move up to floor 3
		processElevatorEvent(subject, ProcessType.MOVE);
		moveCmd = ((ElevatorMoveCommand)commandBuffer.get());
		assertEquals(Direction.UP, moveCmd.getDirection());
		assertEquals(Direction.UP, moveCmd.getServiceDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert: open doors for drop off
		processElevatorEvent(subject, ProcessType.DOOR);
		doorCmd = (ElevatorDoorCommand)commandBuffer.get();
		assertEquals(0, doorCmd.getID());
		assertEquals(Direction.UP, doorCmd.getServiceDirection());
		mimicElevatorEvent(3, 0, messageBuffer);
		
		// assert: command buffer has no remaining commands
		processElevatorEvent(subject, ProcessType.NONE);
		commandBuffer.setIsDisabled(true);
		assertEquals(commandBuffer.get(), null);
	}
}
