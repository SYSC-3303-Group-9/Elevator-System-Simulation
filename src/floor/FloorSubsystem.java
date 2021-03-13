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
import java.nio.charset.Charset;
import java.util.List;

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
			File inputFile = new File(FloorSubsystem.class.getResource("/input.txt").getFile());
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			data = InputParser.parse(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}
		
		// Send all the input data to the FloorReceiver (Port 70).
		for (InputData x : data) {
			System.out.println("[" + x.getTime() + "] Floor " + x.getCurrentFloor() + " requested elevator");	
			try {
				sendPacket = new DatagramPacket(x.toBytes(), x.toBytes().length, InetAddress.getLocalHost(), 70);
				sendReceiveSocket.send(sendPacket);
			} catch(IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			// Following section is if we want to send responses back from FloorReceiver
			// expect a response from FloorReceiver acknowledging packet receipt
			byte response[] = new byte[20];
			receivePacket = new DatagramPacket(response, response.length);
			try {
				// Block until packet is received back from FloorReceiver
				sendReceiveSocket.receive(receivePacket);
				String s = new String(response, Charset.defaultCharset());
				System.out.println(s);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		sendReceiveSocket.close();
	}
}
