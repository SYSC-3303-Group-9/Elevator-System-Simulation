package scheduler;

import java.util.ArrayList;

public class Buffer<T> {
	private T type;
	private ArrayList<T> bufferStore;
	private boolean isDisabled = false;
	
	public boolean isDisabled() {
		return isDisabled;
	}

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
		T bufferItem = null;
		// Wait thread until ArrayList is no longer empty
		while(bufferStore.isEmpty() && !isDisabled) {
			try {
				wait();
			} catch(InterruptedException e) {}
		}
		if(!bufferStore.isEmpty()) {
			bufferItem = bufferStore.remove(0);
			notifyAll();
		}
		return bufferItem;
	}
	
	
}
