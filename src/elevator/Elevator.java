package elevator;

import java.util.ArrayList;

import floor.InputData;

public class Elevator implements Runnable {
	
	private ArrayList<InputData> elevatorScheduleBuffer;
	private InputData currTask;
	private int currentFloor;
	
	public ArrayList<InputData> getElevatorScheduleBuffer() {return elevatorScheduleBuffer;}
	public void setElevatorScheduleBuffer(ArrayList<InputData> elevatorScheduleBuffer) {this.elevatorScheduleBuffer = elevatorScheduleBuffer;}

	public InputData getCurrTask() {return currTask;}
	public void setCurrTask(InputData currTask) {this.currTask = currTask;}

	public int getCurrentFloor() {return currentFloor;}
	public void setCurrentFloor(int currentFloor) {this.currentFloor = currentFloor;}

	
	public Elevator(int currFloor) {
		this.elevatorScheduleBuffer = new ArrayList<>();
		this.currentFloor = currFloor;
	}
	
	
	public void move(InputData input) {
		this.currTask = input;
		this.currentFloor = input.getDestinationFloor();
	}
	
	
	public String printLocation() {
		return this + " going " + this.currTask.getDirection() + " left from floor " + currTask.getCurrentFloor() +
				" and has arrived at " + currTask.getDestinationFloor();
	}

	@Override
	public void run() {
		for(InputData curr : elevatorScheduleBuffer) {
			this.move(curr);
			System.out.println(this.printLocation());
		}
		
	}
	
	
}
