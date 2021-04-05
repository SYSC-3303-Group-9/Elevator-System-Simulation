package scheduler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class ConfigurationFrame extends JFrame implements ActionListener {

	/**
	 * JTextArea for the elevator,floor and file inputs
	 */
	private JTextField elevators, floors, file;

	/**
	 * Strings to contain elevator, floor and file value from entered from text
	 * field
	 */
	private String elevatorNum = null, floorNum = null, inputFile = null;

	/**
	 * JFrame for
	 */
	private JFrame frame;

	public ConfigurationFrame() {
		// Create the frame
		frame = new JFrame("System Configuration");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 200);

		// Creating the panels and adding components
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// first panel containing elevator text field
		JPanel panel1 = new JPanel();
		JLabel label = new JLabel("Number of Elevators");
		elevators = new JTextField(2); // accepts up to 2 character
		panel1.add(label);
		panel1.add(elevators);

		// second panel containing floor text field
		JPanel panel2 = new JPanel();
		JLabel label2 = new JLabel("Number of Floors");
		floors = new JTextField(2); // accepts up to 2 character

		panel2.add(label2);
		panel2.add(floors);

		// third panel containing file text field
		JPanel panel3 = new JPanel();
		JLabel label3 = new JLabel("Input file path");
		file = new JTextField(30); // accepts up to 30 character
		file.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel3.add(label3);
		panel3.add(file);

		// creating button and adding addActionListener
		JButton start = new JButton("Start");
		start.setAlignmentX(Component.CENTER_ALIGNMENT);
		start.addActionListener(this);

		panel.add(panel1);
		panel.add(panel2);
		panel.add(panel3);
		panel.add(start);

		// Adding Components to the frame and making frame visible
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.setVisible(true);

	}

	public void actionPerformed(ActionEvent evt) {
		elevatorNum = elevators.getText();
		floorNum = floors.getText();
		inputFile = file.getText();
		if (elevatorNum != null && floorNum != null && inputFile != null) {
			frame.dispose();
		} else {
			System.out.print("all values must be entered");
		}

	}

	public String getElevatorNum() {
		return elevatorNum;
	}

	public String getFloorNum() {
		return floorNum;
	}

	public String getInputFile() {
		return inputFile;
	}

	public static void main(String[] args) {
		ConfigurationFrame frame = new ConfigurationFrame();
	}

}
