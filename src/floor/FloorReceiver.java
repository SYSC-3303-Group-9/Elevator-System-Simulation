package floor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import scheduler.Buffer;

public class FloorReceiver implements Runnable {
	private DatagramSocket receiveSocket;
	private Buffer<InputData> floorToScheduler;
	
	FloorReceiver(Buffer<InputData> floorToScheduler){
		this.floorToScheduler = floorToScheduler;
		try {
			receiveSocket = new DatagramSocket(70);
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	@Override
	public synchronized void run() {
		while(true) {
			byte data[] = new byte[100];
		    DatagramPacket receivePacket = new DatagramPacket(data, data.length);
		    
			try {
				receiveSocket.receive(receivePacket);
				floorToScheduler.put(InputData.fromBytes(receivePacket.getData()));
				
			} catch(IOException e) {
				System.out.println("FloorReceiver, receiverPacket " + e);
			}
			
			
		}
	}
}
