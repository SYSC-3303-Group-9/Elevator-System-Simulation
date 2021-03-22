package floor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import common.Constants;
import common.IBufferInput;
import scheduler.SchedulerMessage;

public class FloorReceiver implements Runnable {	
	private DatagramSocket sendReceiveSocket;
	private IBufferInput<SchedulerMessage> floorToScheduler;
	
	public FloorReceiver(IBufferInput<SchedulerMessage> floorToScheduler){
		this.floorToScheduler = floorToScheduler;
		try {
			sendReceiveSocket = new DatagramSocket(Constants.FLOOR_RECEIVER_PORT);
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	@Override
	public synchronized void run() {
		//Receive InputData packet from the FloorSubsystem
		while(true) {
			byte data[] = new byte[100];
		    DatagramPacket receivePacket = new DatagramPacket(data, data.length);
		    
		    //Receive Packet from FloorSubsystem
			try {
				sendReceiveSocket.receive(receivePacket);
				InputData inputData = InputData.fromBytes(receivePacket.getData());
				System.out.println("Scheduler received " + inputData);
				floorToScheduler.put(SchedulerMessage.fromInputData(inputData));
				
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
