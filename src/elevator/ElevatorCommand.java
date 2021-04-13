package elevator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class ElevatorCommand implements Serializable {
	private static final long serialVersionUID = 5262291917939169088L;
	private int elevatorID;
	private Fault fault;
	private Direction serviceDirection;
	
	public ElevatorCommand(int elevatorID, Fault fault, Direction serviceDirection) {
		this.elevatorID = elevatorID;
		this.fault = fault;
		this.serviceDirection = serviceDirection;
	}
	
	public int getID() {
		return this.elevatorID;
	}
	
	public Fault getFault() {
		return this.fault;
	}
	
	/**
	 * The direction the elevator will travel to service requests.
	 * This is not necessarily the current direction of the elevator if
	 * the elevator is empty.
	 * @return
	 */
	public Direction getServiceDirection() {
		return this.serviceDirection;
	}
	
	/**
	 * Converts an ElevatorCommand object into a byte array.
	 * 
	 * @return a byte array that corresponds to the ElevatorCommand Object
	 * @throws IOException 
	 */
	public byte[] toBytes() throws IOException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
			 ObjectOutputStream out = new ObjectOutputStream(bos)) {
			out.writeObject(this);
			return bos.toByteArray();
		}
	}

	/**
	 * Converts a byte array into its corresponding ElevatorCommand.
	 * 
	 * @param bytes the byte array to be converted
	 * @return an ElevatorCommand object
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static ElevatorCommand fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			 ObjectInputStream in = new ObjectInputStream(bis)) {
			return (ElevatorCommand)in.readObject();
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof ElevatorCommand)) return false;
		ElevatorCommand c = (ElevatorCommand) o;
		return (elevatorID == c.getID()) && (fault == c.getFault());
	}
	
	@Override
	public String toString() {
		// Concrete subclasses should concatenate their intention to this.
		return "(" + fault + ") Command Elevator " + elevatorID + " to ";
	}
}
