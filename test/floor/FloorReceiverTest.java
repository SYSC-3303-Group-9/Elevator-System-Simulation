package floor;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import common.Buffer;
import elevator.Direction;
import scheduler.SchedulerMessage;

class FloorReceiverTest {

	@Test
	void run_shouldAddSchedulerMessageToBuffer() {
		//arrange
		Buffer<SchedulerMessage> buffer = new Buffer<>();
		Thread thFloorReceiver = new Thread(new FloorReceiver(buffer));
		InputData input = new InputData(LocalTime.now(), 0, Direction.UP, 1);
		
		//act
		thFloorReceiver.start();
		try {
			DatagramSocket sendSocket = new DatagramSocket();
			DatagramPacket sendPacket = new DatagramPacket(input.toBytes(), input.toBytes().length, InetAddress.getLocalHost(), 70);
			sendSocket.send(sendPacket);
			sendSocket.close();
		} catch (IOException e) {
			System.out.println("FlooReceiverTest, run_shouldAddSchedulerMessageToBuffer " + e);
		}
		
		//arrange
		assertTrue(input.equals(buffer.get().getInputData()));
	}

}
