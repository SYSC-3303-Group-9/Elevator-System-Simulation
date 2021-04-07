package elevator;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import common.Constants;

public class DirectionLampPanel extends JPanel {
	
	private DirectionLampButton up;
	private DirectionLampButton down;

	/**
	 * Creates a DirectionLampPanel instance
	 */
	public DirectionLampPanel() {
		up = new DirectionLampButton(Direction.UP, "/resources/upArrow.png");
		down = new DirectionLampButton(Direction.DOWN, "/resources/downArrow.png");
		this.add(down);
		this.add(up);
	}
	
	/**
	 * Changes the color of the direction button to 'ON'
	 * @param lamp the lamp to turn on
	 */
	public void turnOnLamp(DirectionLampButton lamp) {
		lamp.setBackground(Color.YELLOW);
	}
	
	/**
	 * Changes the color of the direction button to 'OFF'
	 * @param lamp the lamp to turn off
	 */
	public void turnOffLamp(DirectionLampButton lamp) {
		lamp.setBackground(Color.GRAY);
	}
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("DirectionLampPanel");
		DirectionLampPanel lamps = new DirectionLampPanel();
		frame.setSize(700, 700);
		frame.add(lamps);
		frame.setVisible(true);
	}

}
