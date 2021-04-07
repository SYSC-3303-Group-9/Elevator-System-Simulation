package floor;

import javax.swing.JButton;

public class FloorLampButton extends JButton {

	private int floor;

	/**
	 * Creates a FloorLampButton instance
	 * 
	 * @param floor
	 */
	public FloorLampButton(int floor) {
		super(Integer.toString(floor));
		this.floor = floor;
		this.setEnabled(false);
		this.setOpaque(true); // Only way color change is seen on Mac OS
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
