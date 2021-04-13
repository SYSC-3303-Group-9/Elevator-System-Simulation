package floor.gui;

import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import elevator.gui.DirectionLampPanel;


public class FloorFrame extends JFrame {
	private static final long serialVersionUID = 8377902406913625950L;
	private JFrame frame;
	private int numFloors;
	private DirectionLampPanel[] floorButtons;

	public FloorFrame(int numFloors) {
		this.numFloors = numFloors;
		this.floorButtons = new DirectionLampPanel[numFloors];
		this.frame = new JFrame("Floors");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(600, 600);

		// create JPanel to contain floor numbers and directions
		JPanel frameContent = new JPanel();
		frameContent.setLayout(new GridLayout(5, 0));

		for (int i = 0; i < this.numFloors; i++) {
			JPanel floorPanel = new JPanel();

			floorPanel.setLayout(new BoxLayout(floorPanel, BoxLayout.Y_AXIS));

			floorButtons[i] = new DirectionLampPanel();
			JLabel floorNum = new JLabel("Floor " + (i + 1));
			floorNum.setAlignmentX(CENTER_ALIGNMENT);

			floorPanel.add(floorNum);
			floorPanel.add(floorButtons[i]);
			frameContent.add(floorPanel);

		}
		// add the panel to a JScrollPane and configure the jScrollPane
		JScrollPane jScrollPane = new JScrollPane(frameContent);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.getContentPane().add(jScrollPane);
		
		frame.setVisible(true);

	}
	
	public DirectionLampPanel getDirectionButton(int floorNum) {
		return floorButtons[floorNum];
	}
}
