package scheduler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BufferTest {
	private Buffer<Integer> buffer;
	private Integer testInteger1;
	private Integer testInteger2;
	private Integer testInteger3;
	
	@BeforeEach
	void setUp() throws Exception {
		buffer = new Buffer<Integer>();
		testInteger1 = 1;
		testInteger2 = 2;
		testInteger3 = 3;
	}

	@AfterEach
	void tearDown() throws Exception {
		buffer = null;
	}

	@Test
	void testPutAndGetOneInteger() {
		buffer.put(testInteger1);
		assertEquals(1, buffer.get());
	}

	

}
