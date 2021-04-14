package floor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.temporal.ChronoField;

import common.Clock;
import common.Constants;

public class MeasurementReceiver implements Runnable {

	private DatagramSocket receiveSocket;
	private InputData currentData;
	private long totalProcessingTime = 0;
	private int numberOfDataReceived = 0;

	/**
	 * Creates a new instance of the MeasurementReceiver class
	 */
	public MeasurementReceiver() {

		try {
			this.receiveSocket = new DatagramSocket(Constants.MEASUREMENT_RECEIVER_PORT);
		} catch (SocketException error) {
			receiveSocket.close();
			error.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Computes the processing time of the elevator requests
	 * @return whether or not InputData was received
	 */
	public boolean computeProcessingTime() {
		boolean computed = false;
		// Construct a DatagramPacket for receiving packets up
		// to to 500
		byte data[] = new byte[500];
		DatagramPacket receivePacket = new DatagramPacket(data, data.length);

		// Receive Packet from Scheduler
		try {
			receiveSocket.receive(receivePacket);
			this.currentData = InputData.fromBytes(receivePacket.getData());
			System.out.println("[MeasurementReceiver] Received " + this.currentData);
			// Get current time and add processing time to running total
			this.totalProcessingTime += ((Clock.getTime() * Constants.TIME_MULTIPLIER) - this.currentData.getTime().getLong(ChronoField.MILLI_OF_DAY));
			System.out.println("[MeasurementReceiver] Running total processing time of requests: " + this.totalProcessingTime);
			computed = true;
			numberOfDataReceived++;
			System.out.println("[MeasurementReceiver] Running Average processing time of requests: " + (this.totalProcessingTime / numberOfDataReceived));
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
