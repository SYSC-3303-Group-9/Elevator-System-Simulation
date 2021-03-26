package floor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import common.Clock;

public class FloorSync implements Runnable {
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendReceiveSocket;
	private Clock clock;
	
	public FloorSync() {
		try {
			sendReceiveSocket = new DatagramSocket();
		} catch(SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	@Override
	public void run() {
		try {
			//Creates the floor ready DatagramPacket
			byte data[] = "floor".getBytes();
			
			//Sends the DatagramPacket to SystemSync to notify that the floor is ready.
			//sendPacket = new DatagramPacket(new byte[0], 0, InetAddress.getLocalHost(), Constants.SYSTEM_SYNC_PORT);
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 72);	//Harcoded port for now. Waiting for merge containing Constants.SYSTEM_SYNC_PORT
			sendReceiveSocket.send(sendPacket);
			
			//Block until the start message DatagramPacket is received from SystemSync
			receivePacket = new DatagramPacket(data, data.length);
			sendReceiveSocket.receive(receivePacket);
			
			//start clock
			if(receivePacket.getData().toString().equals("start")){
				clock.startClock();
			}
			
			
		} catch (IOException e) {
			System.out.println("FloorSync, run" + e);
			System.exit(1);
		}	
		
	}

}
