package elevator;

import java.awt.Color;

import javax.swing.JPanel;

import common.Constants;

public class DirectionLampPanel extends JPanel {

	private DirectionLampButton up;
	private DirectionLampButton down;

	/**
	 * Creates a DirectionLampPanel instance
	 */
	public DirectionLampPanel() {
		up = new DirectionLampButton(Direction.UP, Constants.UP_ARROW);
		down = new DirectionLampButton(Direction.DOWN, Constants.DOWN_ARROW);
		this.add(down);
		this.add(up);
	}

	/**
	 * Changes the color of the direction button to 'ON'
	 * 
	 * @param ldirection the direction lamp to turn on
	 */
	public void turnOnLamp(Direction direction) {
		if (direction == Direction.UP) {
			up.setBackground(Color.YELLOW);
		} else if (direction == Direction.DOWN) {
			down.setBackground(Color.YELLOW);
		}
	}

	/**
	 * Changes the color of the direction button to 'OFF'
	 * 
	 * @param direction the direction lamp to turn off
	 */
	public void turnOffLamp(Direction direction) {
		if (direction == Direction.UP) {
			up.setBackground(Color.GRAY);
		} else if (direction == Direction.DOWN) {
			down.setBackground(Color.GRAY);
		}
	}

}
