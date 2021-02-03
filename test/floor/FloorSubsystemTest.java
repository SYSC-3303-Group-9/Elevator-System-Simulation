package floor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class FloorSubsystemTest {

	@Test
	void shouldAddDataToBuffer() {
		// arrange
		Buffer buffer = new Buffer;
		FloorSubsystem obj1 = new FloorSubsystem(buffer);
		
		// act
		int result = buffer.size();

		// assert
		assertEquals(result,3);
	}
}
