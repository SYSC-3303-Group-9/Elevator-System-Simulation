package floor.gui;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import elevator.Fault;

public class FloorLampPanel extends JPanel {

	private FloorLamp[][] floorGrid;
	private int columns;
	private int rows;
	private int onLamp;

	/**
	 * Creates a FloorLampPanel based on the number of floors
	 * 
	 * @param numOfFloors the number of floors in the system
	 */
	public FloorLampPanel(int numOfFloors) {
		if (numOfFloors % 2 == 0) {
			this.rows = numOfFloors / 2;
			this.columns = 2;

			this.floorGrid = new FloorLamp[columns][rows];
			this.setLayout(new GridLayout(this.rows, this.columns));
			this.setSize(30, 30);

			// set lamp number
			int floor = numOfFloors;
			for (int i = 0; i < this.rows; i++) {
				for (int j = 0; j < this.columns; j++) {
					this.floorGrid[j][i] = new FloorLamp(floor);
					this.floorGrid[j][i].setBackground(Color.WHITE);
					floor--;
					if (this.floorGrid[j][i].getFloor() % 2 == 0) {
						this.floorGrid[j][i] = new FloorLamp(this.floorGrid[j][i].getFloor() - 1);
						this.floorGrid[j][i].setBackground(Color.WHITE);
					} else {
						this.floorGrid[j][i] = new FloorLamp(this.floorGrid[j][i].getFloor() + 1);
						this.floorGrid[j][i].setBackground(Color.WHITE);
					}
					this.add(this.floorGrid[j][i]);
				}
			}

		} else if(numOfFloors % 3 == 0 && numOfFloors > 10){
			this.rows = numOfFloors / 3;
			this.columns = 3;

			this.floorGrid = new FloorLamp[columns][rows];
			this.setLayout(new GridLayout(this.rows, this.columns));
			this.setSize(30, 30);

			// set lamp number
			int floor = numOfFloors;
			for (int i = 0; i < this.rows; i++) {
				for (int j = 0; j < this.columns; j++) {
					this.floorGrid[j][i] = new FloorLamp(floor);
					this.floorGrid[j][i].setBackground(Color.WHITE);
					floor--;
					if (this.floorGrid[j][i].getFloor() % 3 == 0) {
						this.floorGrid[j][i] = new FloorLamp(this.floorGrid[j][i].getFloor() - 2);
						this.floorGrid[j][i].setBackground(Color.WHITE);
					} else if((this.floorGrid[j][i].getFloor() % 2 == 0 || this.floorGrid[j][i].getFloor() % 2 != 0 ) && (j != 1)){
						this.floorGrid[j][i] = new FloorLamp(this.floorGrid[j][i].getFloor() + 2);
						this.floorGrid[j][i].setBackground(Color.WHITE);
					}
					this.add(this.floorGrid[j][i]);
				}
			}
			
		} else if(numOfFloors > 10){
			this.rows = (numOfFloors + 1) / 2;
			this.columns = 2;

			this.floorGrid = new FloorLamp[columns][rows];
			this.setLayout(new GridLayout(this.rows, this.columns));
			this.setSize(30, 30);

			// set lamp number
			int floor = numOfFloors + 1;
			for (int i = 0; i < this.rows; i++) {
				for (int j = 0; j < this.columns; j++) {
					this.floorGrid[j][i] = new FloorLamp(floor);
					this.floorGrid[j][i].setBackground(Color.WHITE);
					floor--;
					if (this.floorGrid[j][i].getFloor() % 2 == 0) {
						this.floorGrid[j][i] = new FloorLamp(this.floorGrid[j][i].getFloor() - 1);
						this.floorGrid[j][i].setBackground(Color.WHITE);
					} else {
						this.floorGrid[j][i] = new FloorLamp(this.floorGrid[j][i].getFloor() + 1);
						this.floorGrid[j][i].setBackground(Color.WHITE);
					}
					
					if(this.floorGrid[j][i].getFloor() == (numOfFloors + 1)){
						this.floorGrid[j][i].setText("");
					}
					
					this.add(this.floorGrid[j][i]);
				}
			}
		} else {
			// Unexpected scenario
			// 1 column display
			this.rows = numOfFloors;
			this.columns = 1;

			this.floorGrid = new FloorLamp[columns][rows];
			this.setLayout(new GridLayout(this.rows, this.columns));
			this.setSize(30, 30);

			// set lamp number
			int floor = numOfFloors;
			for (int i = 0; i < this.rows; i++) {
				for (int j = 0; j < this.columns; j++) {
					this.floorGrid[j][i] = new FloorLamp(floor);
					this.floorGrid[j][i].setBackground(Color.WHITE);
					floor--;
					this.add(this.floorGrid[j][i]);
				}
			}
		}
	}

	/**
	 * Changes the color of the floor button to 'ON'
	 * 
	 * @param floor the floor on which the lamp is to turn on
	 */
	public void turnOnLamp(int floor) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				if (this.floorGrid[j][i].getFloor() == floor) {
					this.floorGrid[j][i].setBackground(Color.YELLOW);
				}
			}
		}
		onLamp = floor;
	}

	/**
	 * Changes the color of the floor button to 'OFF'
	 * 
	 * @param floor the floor on which the lamp is to turn off
	 */
	public void turnOffLamp(int floor) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				if (this.floorGrid[j][i].getFloor() == floor) {
					this.floorGrid[j][i].setBackground(Color.WHITE);
				}
			}
		}
	}

	public void turnOffLamp() {
		turnOffLamp(onLamp);
		onLamp = -1;
	}

	public void errorLamp(Fault fault) {
		Color c;
		if (fault == Fault.TRANSIENT) {
			c = Color.ORANGE;
		} else if (fault == Fault.NONE) {
			c = Color.YELLOW;
		} else {
			c = Color.RED;
		}
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				if (this.floorGrid[j][i].getFloor() == onLamp) {
					this.floorGrid[j][i].setBackground(c);
				}
			}
		}
	}

	/**
	 * Gets the floorGrid
	 * 
	 * @return a floor grid of type FloorLamp
	 */
	public FloorLamp[][] getFloorGrid() {
		return floorGrid;
	}

	/**
	 * Gets the number of columns in the gird
	 * 
	 * @return number columns
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * Gets the number of rows in the grid
	 * 
	 * @return number of rows
	 */
	public int getRows() {
		return rows;
	}
}
