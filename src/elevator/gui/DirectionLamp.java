package elevator.gui;

import java.awt.Color;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import elevator.Direction;

public class DirectionLamp extends JLabel {

	private Direction direction;
	private String source;

	/**
	 * Creates a direction button lamp
	 * 
	 * @param direction the direction lamp will be pointing to
	 * @param source    the file name that contains the direction icon
	 */
	public DirectionLamp(Direction direction, String source) {
		System.out.println(source);
		this.direction = direction;
		this.source = source;
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.setOpaque(true); // Only way color change is seen on Mac OS
		this.setBounds(0, 0, 30, 30);
		this.setIcon(resizeIcon(new ImageIcon(source), this.getWidth(), this.getHeight()));
	}

	/**
	 * Gets the direction of the lamp
	 * 
	 * @return the direction enum
	 */
	public Direction getDirection() {
		return this.direction;
	}

	/**
	 * Gets the file name of the direction icon
	 * 
	 * @return the file name as string
	 */
	public String getSource() {
		return this.source;
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
}
