package elevator.gui;

import java.awt.Font;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Door extends JLabel {
	private static final long serialVersionUID = -6776502908344405247L;
	private static final int WIDTH = 100;
	private static final int HEIGHT = 100;
	private static final Font font = new Font("Dialog", Font.BOLD, 16);
	/**
	 * Creates a direction button lamp
	 * 
	 * @param direction the direction lamp will be pointing to
	 * @param source    the file name that contains the direction icon
	 */
	public Door() {
		this.setOpaque(true);
		this.setBounds(0, 0, WIDTH, HEIGHT);
		this.setFont(font);
		this.setHorizontalAlignment(JLabel.CENTER);
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setVerticalTextPosition(JLabel.BOTTOM);
		// Default for each elevator will be closed
		this.setIcon(resizeIcon(DoorState.DOOR_CLOSED.getIcon(), WIDTH, HEIGHT));
		this.setText(DoorState.DOOR_CLOSED.toString());
	}

	/**
	 * Resizes the image to look like an icon
	 * 
	 * @param icon          the icon to resize
	 * @param resizedWidth  the width of the lamp
	 * @param resizedHeight the height of the lamp
	 * @return the Icon
	 */
	private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
		Image image = icon.getImage();
		Image resizedImage = image.getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImage);
	}
	
	public void setState(DoorState state) {
		this.setIcon(resizeIcon(state.getIcon(), WIDTH, HEIGHT));
		this.setText(state.toString());
	}
}
