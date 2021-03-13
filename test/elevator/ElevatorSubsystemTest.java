package elevator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import floor.InputData;
import scheduler.Buffer;

public class ElevatorSubsystemTest {
	@Test
	public void run_shouldSendEvents() {
		// arrange subject
		Buffer<InputData> schedulerToElevatorBuffer = new Buffer<InputData>();
		Buffer<ElevatorEvent> elevatorToSchedulerBuffer = new Buffer<ElevatorEvent>();
		
		ElevatorSubsystem subject = new ElevatorSubsystem(new Elevator(0,0), schedulerToElevatorBuffer, elevatorToSchedulerBuffer);
		
		// arrange data
		InputData input1 = new InputData(LocalTime.of(0, 0), 1, Direction.UP, 2);
		schedulerToElevatorBuffer.put(input1);
		
		InputData input2 = new InputData(LocalTime.of(0, 1), 2, Direction.DOWN, 1);
		schedulerToElevatorBuffer.put(input2);
		
		schedulerToElevatorBuffer.setIsDisabled(true);
		
		// act
		Thread th = new Thread(subject);
		th.start();
		try {
			th.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// assert
		ElevatorEvent event1 = new ElevatorEvent(input1.getDestinationFloor(), 0);
		ElevatorEvent event2 = new ElevatorEvent(input2.getDestinationFloor(), 0);
		
		assertEquals(event1, elevatorToSchedulerBuffer.get());
		assertEquals(event2, elevatorToSchedulerBuffer.get());
	}
}
