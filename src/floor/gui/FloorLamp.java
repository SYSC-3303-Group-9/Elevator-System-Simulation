package floor.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class FloorLamp extends JLabel {

	private int floor;
	private static final Font font = new Font("Dialog", Font.BOLD, 20);
	/**
	 * Creates a FloorLamp instance
	 * 
	 * @param floor
	 */
	public FloorLamp(int floor) {
		super(Integer.toString(floor));
		this.setHorizontalAlignment(JLabel.CENTER);
		this.setVerticalAlignment(JLabel.CENTER);
		this.floor = floor;
		this.setFont(font);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.setOpaque(true); // Only way color change is seen on Mac OS
		this.setSize(30, 30);
	}

	/**
	 * Gets the floor the lamp is allocated
	 * 
	 * @return floor number
	 */
	public int getFloor() {
		return this.floor;
	}

}
