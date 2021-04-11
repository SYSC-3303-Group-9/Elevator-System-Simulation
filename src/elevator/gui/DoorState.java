package elevator.gui;

import java.nio.file.Paths;
import javax.swing.ImageIcon;

public enum DoorState {
	DOOR_OPENING("Doors Opening", Paths.get("resources", "opening.png").toString()),
	DOOR_CLOSING("Doors Closing", Paths.get("resources", "closing.png").toString()),
	DOOR_OPEN("Doors Open", Paths.get("resources", "open.png").toString()),
	DOOR_CLOSED("Doors Closed", Paths.get("resources", "closed.png").toString());
	
	private final String name;
	private final ImageIcon icon;
	
	private DoorState(String name, String path) {
		this.name = name;
		this.icon = new ImageIcon(path);
	}
	
	public ImageIcon getIcon() {
		return this.icon;
	}
	
	public String toString() {
		return this.name;
	}
}
