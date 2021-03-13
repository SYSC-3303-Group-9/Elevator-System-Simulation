package floor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import scheduler.Buffer;

public class FloorReceiver implements Runnable {
	private DatagramSocket sendReceiveSocket;
	private Buffer<InputData> floorToScheduler;
	
	FloorReceiver(Buffer<InputData> floorToScheduler){
		this.floorToScheduler = floorToScheduler;
		try {
			sendReceiveSocket = new DatagramSocket(70);
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	@Override
	public synchronized void run() {
		//Receive InputData packet from the FloorSubsystem via port 70
		while(true) {
			byte data[] = new byte[100];
		    DatagramPacket receivePacket = new DatagramPacket(data, data.length);
		    
		    //Receive Packet from FloorSubsystem
			try {
				sendReceiveSocket.receive(receivePacket);
				floorToScheduler.put(InputData.fromBytes(receivePacket.getData()));
				
			} catch(IOException e) {
				System.out.println("FloorReceiver, receiverPacket " + e);
			}
			
			//Send respond back to FloorSubsystem
			DatagramPacket respond;
			try {
				respond = new DatagramPacket(new byte[0], 0, InetAddress.getLocalHost(), receivePacket.getPort());
				sendReceiveSocket.send(respond);
			} catch (IOException e) {
				System.out.println("FloorReceiver, run" + e);
				System.exit(1);
			}	
		}
	}
}
