package floor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;

public class FloorLampPanel extends JPanel {

	private FloorLampButton[][] floorGrid;
	private int columns;
	private int rows;

	/**
	 * Creates a FloorLampPanel instance
	 */
	public FloorLampPanel() {
		this.rows = 11;
		this.columns = 2;
		this.floorGrid = new FloorLampButton[columns][rows];
		this.setLayout(new GridLayout(this.rows, this.columns));
		this.setPreferredSize(new Dimension(30, 30));

		// Set button number
		int floor = 1;
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				this.floorGrid[j][i] = new FloorLampButton(floor);
				floor++;
				this.add(this.floorGrid[j][i]);
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
					this.floorGrid[j][i].setBackground(Color.GRAY);
				}
			}
		}
	}

	/**
	 * Gets the floorGrid
	 * 
	 * @return a floor grid of type FloorLampButton
	 */
	public FloorLampButton[][] getFloorGrid() {
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
