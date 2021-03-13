package floor;

/*
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import common.Buffer;
import elevator.Direction;
import elevator.ElevatorEvent;
*/

public class FloorSubsystemTest {
	/* TODO: Fix this test.
	@Test
	void run_shouldAddDataToBuffer() {
		// arrange
		Buffer<InputData> floorToSchedulerBuffer = new Buffer<InputData>();
		Buffer<ElevatorEvent> schedulerToFloorBuffer = new Buffer<ElevatorEvent>();
		schedulerToFloorBuffer.setIsDisabled(true);
		
		FloorSubsystem subject = new FloorSubsystem();
		
		// act
		Thread th = new Thread(subject);
		th.start();
		try {
			th.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// assert
		int nanoSecondsInSecond = 1000000000;
		int nanoSeconds = (int)(0.1 * nanoSecondsInSecond);
		InputData result1 = new InputData(LocalTime.of(1, 2, 3, (4*nanoSeconds)), 1, Direction.UP, 2);
		InputData result2 = new InputData(LocalTime.of(2, 3, 4, (5*nanoSeconds)), 2, Direction.DOWN, 1);
		InputData result3 = new InputData(LocalTime.of(14, 5, 15, 0), 2, Direction.UP, 4);
		
		assertEquals(result1, floorToSchedulerBuffer.get());
		assertEquals(result2, floorToSchedulerBuffer.get());
		assertEquals(result3, floorToSchedulerBuffer.get());
	}
	*/
}
