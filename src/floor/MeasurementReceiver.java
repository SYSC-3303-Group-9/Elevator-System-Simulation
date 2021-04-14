package floor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.LocalTime;
import java.util.Map;

import common.Clock;
import common.Constants;
import common.IBufferOutput;
import scheduler.SchedulerMessage;

public class MeasurementReceiver implements Runnable {

	private DatagramSocket receiveSocket;
	private IBufferOutput<Map<LocalTime, Long>> outputBuffer;
	private InputData currentData;
	private long totalProcessingTime = 0;

	/**
	 * Creates a new instance of the MeasurementReceiver class
	 */
	public MeasurementReceiver(IBufferOutput<Map<LocalTime, Long>> outputBuffer) {
		this.outputBuffer = outputBuffer;

		try {
			this.receiveSocket = new DatagramSocket(Constants.MEASUREMENT_RECEIVER_PORT);
		} catch (SocketException error) {
			receiveSocket.close();
			error.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean computeProcessingTime() {
		boolean computed = false;
		// Construct a DatagramPacket for receiving packets up
		// to to 500
		byte data[] = new byte[500];
		DatagramPacket receivePacket = new DatagramPacket(data, data.length);
		long currentTime = Clock.getTime();

		// Receive Packet from Scheduler
		try {
			receiveSocket.receive(receivePacket);
			this.currentData = InputData.fromBytes(receivePacket.getData());
			System.out.println("[MeasurementReceiver] Received " + this.currentData);
			// Get current time and add processing time to running total
			Map<LocalTime, Long> dataTimes = outputBuffer.get();
			for (Map.Entry<LocalTime, Long> entry : dataTimes.entrySet()) {
				if (entry.getKey() == this.currentData.getTime()) {
					this.totalProcessingTime += (currentTime - entry.getValue());
				}
			}
			System.out.println("[MeasurementReceiver] Running total of processing times requests: " + this.totalProcessingTime);
			computed = true;
		} catch (IOException | ClassNotFoundException error) {
			computed = false;
			error.printStackTrace();
			System.exit(1);
		}
		
		return computed;
	}

	@Override
	public synchronized void run() {
		while(true) {
			if(!computeProcessingTime()) {
				break;
			}
		}
	}

}
