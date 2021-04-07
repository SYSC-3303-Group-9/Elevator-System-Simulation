package scheduler.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

@SuppressWarnings("serial")
public class ConfigurationFrame extends JFrame implements ActionListener {

	/**
	 * JTextArea for the elevator,floor and file inputs
	 */
	private JTextField elevators, floors, file;

	/**
	 * JButton for the open and start buttons
	 */
	private JButton open, start;

	/**
	 * JFileChooser for choosing files
	 */
	private JFileChooser fc;
	/**
	 * Strings to contain file value from entered from text field
	 */
	private String inputFile = null;

	/**
	 * integers to contain elevator and floor value from entered from text field
	 */
	private int floorNum = 0, elevatorNum = 0;

	/**
	 * JFrame for configuration data
	 */
	private JFrame frame;

	/**
	 * used to check if values are registered and frame has been closed
	 */
	private boolean done = false;

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
		label.setLabelFor(elevators);
		panel1.add(label);
		panel1.add(elevators);

		// second panel containing floor text field
		JPanel panel2 = new JPanel();
		JLabel label2 = new JLabel("Number of Floors");
		floors = new JTextField("22", 2); // accepts up to 2 character
		label2.setLabelFor(floors);
		panel2.add(label2);
		panel2.add(floors);

		// third panel containing file text field
		JPanel panel3 = new JPanel((new FlowLayout(FlowLayout.LEFT)));
		JLabel label3 = new JLabel("Input file path");
		file = new JTextField(20);
		label3.setLabelFor(file);

		// creating a button for open file
		fc = new JFileChooser();
		open = new JButton("Open File");
		open.addActionListener(this);

		panel3.add(label3);
		panel3.add(file);
		panel3.add(open);

		// creating button and adding addActionListener
		start = new JButton("Start");
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

		// the the open button is clicked
		if (evt.getSource() == open) {
			int returnVal = fc.showOpenDialog(frame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fc.getSelectedFile();
				System.out.print("Opening: " + selectedFile.getAbsolutePath() + ".");
				file.setText(selectedFile.getAbsolutePath());
			} else {
				System.out.print("Open command cancelled by user.");
			}
			fc.setSelectedFile(null);

		} else {// the start button is clicked

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
				this.done = true;
				frame.dispose();
			}
		}
	}

	public int getElevatorNum() {
		return elevatorNum;
	}

	public int getFloorNum() {
		return floorNum;
	}

	public boolean isDone() {
		return done;
	}

	public String getInputFile() {
		return inputFile;
	}

	public static void main(String[] args) {
		new ConfigurationFrame();
	}

}
