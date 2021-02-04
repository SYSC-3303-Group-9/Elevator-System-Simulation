package elevator;

import floor.InputData;

/**
 * Moves from floor to floor based on the data it has been sent.
 * @author Chris
 *
 */
public class Elevator  {

	private InputData currTask;
	private int currFloor;
	
	/**
	 * Setters and Getters
	 * @return
	 */
	public InputData getCurrTask() {return currTask;}
	public void setCurrTask(InputData currTask) {this.currTask = currTask;}
	public int getCurrFloor() {return currFloor;}
	public void setCurrFloor(int currentFloor) {this.currFloor = currentFloor;}

	/**
	 * Class constructor
	 * @param currFloor The current floor that the elevator is at.
	 */
	public Elevator(int currFloor) {
		this.currFloor = currFloor;
	}
	
	/**
	 * Moves the elevator to a specified floor and prints the floor it left and the destination floor
	 * @param input	The input contains the destination to elevator needs to move to amongst other data
	 */
	public void move(InputData input) {
		currTask = input;
		currFloor = input.getDestinationFloor();
		System.out.println(printLocation());
	}
	
	/**
	 * Prints the movement of the elevator
	 * @return The
	 */
	public String printLocation() {
		return this + " going " + currTask.getDirection() + ". Left from floor " + currTask.getCurrentFloor() +
				" and has arrived at " + currTask.getDestinationFloor();
	}

		
}
