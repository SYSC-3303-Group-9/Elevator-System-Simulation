package scheduler;

import java.util.ArrayList;

public class Buffer<T> {
	private ArrayList<T> bufferStore;
	private boolean isDisabled = false;
	
	public boolean getIsDisabled() {
		return isDisabled;
	}

	/**
	 * Is disabled marks whether a get request on this buffer should return
	 * even if there is no item to return.
	 * @param isDisabled Whether the Buffer is disabled.
	 */
	public synchronized void setIsDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
		this.notifyAll();
	}

	public Buffer() {
		this.bufferStore = new ArrayList<T>();
	}
	
	/**
	 * Puts the bufferItem at the end of the bufferStore ArrayList
	 * @param bufferItem an item to append to bufferStore 
	 */
	public synchronized void put(T bufferItem) {
		bufferStore.add(bufferItem);
		notifyAll();
	}
	
	
	/**
	 * Gets the first bufferItem in the bufferStore ArrayList
	 * @return the first element in bufferStore
	 */
	public synchronized T get() {
		// Wait thread until ArrayList is no longer empty
		while(bufferStore.isEmpty() && !isDisabled) {
			try {
				wait();
			} catch(InterruptedException e) {}
		}
		
		T bufferItem = null;
		if(!bufferStore.isEmpty()) {
			bufferItem = bufferStore.remove(0);
			notifyAll();
		}
		return bufferItem;
	}
	
	
}
