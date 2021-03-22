package floor;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import common.Buffer;
import common.Constants;
import elevator.Direction;
import scheduler.SchedulerMessage;

class FloorReceiverTest {

	@Test
	void run_shouldAddSchedulerMessageToBuffer() throws IOException {
		//arrange
		Buffer<SchedulerMessage> buffer = new Buffer<>();
		Thread thFloorReceiver = new Thread(new FloorReceiver(buffer));
		InputData input = new InputData(LocalTime.now(), 0, Direction.UP, 1);
		
		//act
		thFloorReceiver.start();
		DatagramSocket sendSocket = new DatagramSocket();
		DatagramPacket sendPacket = new DatagramPacket(input.toBytes(), input.toBytes().length, InetAddress.getLocalHost(), Constants.FLOOR_RECEIVER_PORT);
		sendSocket.send(sendPacket);
		sendSocket.close();
		
		//arrange
		assertTrue(input.equals(buffer.get().getInputData()));
	}

}
