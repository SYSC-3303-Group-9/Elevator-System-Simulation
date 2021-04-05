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
	 * Strings to contain file value from entered from text field
	 */
	private String inputFile = null;

	/**
	 * integers to contain elevator and floor value from entered from text field
	 */
	int floorNum = 0, elevatorNum = 0;

	/**
	 * JFrame for configuration data
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
		elevators = new JTextField("4", 2); // accepts up to 2 character
		panel1.add(label);
		panel1.add(elevators);

		// second panel containing floor text field
		JPanel panel2 = new JPanel();
		JLabel label2 = new JLabel("Number of Floors");
		floors = new JTextField("22", 2); // accepts up to 2 character

		panel2.add(label2);
		panel2.add(floors);

		// third panel containing file text field
		JPanel panel3 = new JPanel();
		JLabel label3 = new JLabel("Input file path");
		file = new JTextField("input.txt", 30); // accepts up to 30 character
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
		// verify floor and elevator values are integers, if not display a warning
		try {
			elevatorNum = Integer.parseInt(elevators.getText());
			floorNum = Integer.parseInt(floors.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(frame, "Please ensure Elevator and Floor values are integers!",
					"Invalid format", JOptionPane.ERROR_MESSAGE);
		}
		if (elevatorNum == 0 || floorNum == 0) {
			JOptionPane.showMessageDialog(frame, "Please ensure Elevator and Floor values are not zero!",
					"Invalid format", JOptionPane.ERROR_MESSAGE);
		}

		// get file value from text field
		inputFile = file.getText();

		// if value is entered for all fields, close frame
		if (elevatorNum != 0 && floorNum != 0 && inputFile != null) {
			frame.dispose();
		}
	}

	public int getElevatorNum() {
		return elevatorNum;
	}

	public int getFloorNum() {
		return floorNum;
	}

	public String getInputFile() {
		return inputFile;
	}

	public static void main(String[] args) {
		new ConfigurationFrame();
	}

}
