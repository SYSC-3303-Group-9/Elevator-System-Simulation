package floor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import elevator.Direction;
import scheduler.Buffer;

public class FloorSubsystemTest {

	@Test
	void shouldAddDataToBuffer() {
		// arrange
		Buffer<InputData> buffer = new Buffer<InputData>();
		FloorSubsystem obj1 = new FloorSubsystem(buffer);
		Thread th = new Thread(obj1);
		th.start();
		try {
			th.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// act
		int nanoSecondsInSecond = 1000000000;
		int nanoSeconds = (int)(0.1 * nanoSecondsInSecond);
		InputData result1 = new InputData(LocalTime.of(14, 5, 15, 0), 2, Direction.UP, 4);
		InputData result2 = new InputData(LocalTime.of(1, 2, 3, (4*nanoSeconds)), 1, Direction.UP, 2);
		InputData result3 = new InputData(LocalTime.of(2, 3, 4, (5*nanoSeconds)), 2, Direction.DOWN, 1);
		// assert
		assertEquals(result1, buffer.get());
		assertEquals(result2, buffer.get());
		assertEquals(result3, buffer.get());
		System.out.print(true);

	}
}
