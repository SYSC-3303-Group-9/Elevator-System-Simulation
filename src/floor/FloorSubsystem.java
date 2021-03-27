package floor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

import common.Clock;
import common.Constants;

public class FloorSubsystem implements Runnable {
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendReceiveSocket;

	public FloorSubsystem() {		
		try {
			sendReceiveSocket = new DatagramSocket();
		} catch(SocketException se) {
			sendReceiveSocket.close();
			se.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void run() {
		List<InputData> data;
		// Reads input data from the provided text file and adds each line of data to
		// the Buffer after converting it to a InputData type.
		try {
			File inputFile = new File(Constants.INPUT_FILE);
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			data = InputParser.parse(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}
		
		// Send all the input data to the FloorReceiver.
		for (InputData x : data) {
			System.out.println("[" + x.getTime() + "] Floor " + x.getCurrentFloor() + " requested elevator");	
			try {
				sendPacket = new DatagramPacket(x.toBytes(), x.toBytes().length, InetAddress.getLocalHost(), Constants.FLOOR_RECEIVER_PORT);
				sendReceiveSocket.send(sendPacket);
			} catch(IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			// Following section is if we want to send responses back from FloorReceiver
			// expect a response from FloorReceiver acknowledging packet receipt
			byte response[] = new byte[0];
			receivePacket = new DatagramPacket(response, response.length);
			try {
				// Block until packet is received back from FloorReceiver
				sendReceiveSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		sendReceiveSocket.close();
	}
}
