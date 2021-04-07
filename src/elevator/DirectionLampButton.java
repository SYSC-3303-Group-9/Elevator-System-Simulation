package elevator;

import java.awt.Image;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import common.Constants;

public class DirectionLampButton extends JButton{
	
	private Direction direction;
	private String source;

	/**
	 * Creates a direction button lamp
	 * @param direction the direction lamp will be pointing to
	 * @param source the file name that contains the direction icon
	 */
	public DirectionLampButton(Direction direction, String source) {
		System.out.println(source);
		this.direction = direction;
		this.source = source;
		this.setBounds(0, 0, 60, 60);
		this.setIcon(resizeIcon(new ImageIcon(getImageURL(source)), this.getWidth(), this.getHeight()));
	}
	
	/**
	 * Gets the direction of the lamp
	 * @return the direction enum
	 */
	public Direction getDirection() {
		return this.direction;
	}
	
	/**
	 * Gets the file name of the direction icon
	 * @return the file name as string
	 */
	public String getSource() {
		return this.source;
	}
	
	/**
	 * Gets the image URL 
	 * @param fileName the file name of the icon
	 * @return the file name URL
	 */
	private static URL getImageURL(String fileName) {
		return DirectionLampPanel.class.getResource(fileName);
	}
	
	/**
	 * Resizes the image to look like an icon
	 * @param icon the icon to resize
	 * @param resizedWidth the width of the lamp
	 * @param resizedHeight the height of the lamp
	 * @return the Icon
	 */
	private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
		Image image = icon.getImage();
		Image resizedImage = image.getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImage);
	}
}
