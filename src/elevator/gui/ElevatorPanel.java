package elevator.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import floor.gui.FloorLampPanel;

public class ElevatorPanel extends JPanel {
	private DirectionLampPanel directionLamp;
	private FloorLampPanel floorLamp;
	private Door door;
	private JLabel elevatorName;
	private static final Font font = new Font("Dialog", Font.BOLD, 24);
	
	public ElevatorPanel(int elevatorNum) {
		// Setup Elevator header
		elevatorName = new JLabel("Elevator " + elevatorNum);
		elevatorName.setFont(font);
		elevatorName.setAlignmentX(CENTER_ALIGNMENT);
		// Setup Elevator's Direction Lamp
		directionLamp = new DirectionLampPanel();
		directionLamp.setAlignmentX(CENTER_ALIGNMENT);
		// Setup Elevator's Floor Lamp
		floorLamp = new FloorLampPanel();
		floorLamp.setAlignmentX(CENTER_ALIGNMENT);
		floorLamp.setBorder(new EmptyBorder(0, 5, 0, 5));
		// Setup Elevator's Door
		door = new Door();
		door.setAlignmentX(CENTER_ALIGNMENT);
		// Align components in the panel
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(elevatorName);
		this.add(directionLamp);
		this.add(door);
		this.add(floorLamp);
		this.setBorder(BorderFactory.createLineBorder(Color.black, 3));
	}
}
