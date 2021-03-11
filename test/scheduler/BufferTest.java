package scheduler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import common.Buffer;

class BufferTest {
	@Test
	void testPutAndGetOneInteger() {
		Buffer<Integer> buffer = new Buffer<Integer>();
		buffer.put(1);
		assertEquals(1, buffer.get());
	}
}
