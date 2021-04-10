package elevator.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.*;
import floor.gui.FloorLampPanel;

public class ElevatorPanel extends JPanel {
	private DirectionLampPanel directionLamp;
	private FloorLampPanel floorLamp;
	private Door door;
	private JLabel elevatorName;
	private static Font font = new Font("Dialog", Font.BOLD, 20);
	
	public ElevatorPanel(int elevatorNum) {
		// Setup Elevator header
		elevatorName = new JLabel("Elevator " + elevatorNum);
		elevatorName.setFont(font);
		elevatorName.setAlignmentX(CENTER_ALIGNMENT);
		elevatorName.setBorder(BorderFactory.createLineBorder(Color.black));
		// Setup Elevator's Direction Lamp
		directionLamp = new DirectionLampPanel();
		directionLamp.setAlignmentX(CENTER_ALIGNMENT);
		directionLamp.setBorder(BorderFactory.createLineBorder(Color.black));
		// Setup Elevator's Floor Lamp
		floorLamp = new FloorLampPanel();
		floorLamp.setAlignmentX(CENTER_ALIGNMENT);
		floorLamp.setBorder(BorderFactory.createLineBorder(Color.black));
		// Setup Elevator's Door
		door = new Door();
		door.setAlignmentX(CENTER_ALIGNMENT);
		door.setBorder(BorderFactory.createLineBorder(Color.black));
		// Align components in the panel
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(elevatorName);
		this.add(directionLamp);
		this.add(door);
		this.add(floorLamp);
	}
	
	public static void main(String args[]) {
		ElevatorPanel p = new ElevatorPanel(1);
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		frame.getContentPane().add(p);
		frame.setVisible(true);
	}
}
