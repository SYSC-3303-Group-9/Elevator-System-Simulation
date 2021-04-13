package elevator.gui;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.*;

public class ElevatorFrame extends JFrame {
	private static final long serialVersionUID = -4779458824801336405L;
	private JFrame frame;
	private ElevatorPanel[] elevatorPanels;
	
	public ElevatorFrame(int numElevators, int numOfFloors) {
		elevatorPanels = new ElevatorPanel[numElevators];
		frame = new JFrame("Elevators");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 1000);
		// Using gridlayout for frame so that elevators will take up max space and only be in one row
		Container frameContent = frame.getContentPane();
		frameContent.setLayout(new GridLayout(1, numElevators));
		for(int i = 0; i < numElevators; i++) {
			elevatorPanels[i] = new ElevatorPanel(i, numOfFloors);
			frameContent.add(elevatorPanels[i]);
		}
		frame.setVisible(true);
	}
	
	public ElevatorPanel getElevatorPanel(int elevatorNum) {
		return elevatorPanels[elevatorNum];
	}

}
