package scheduler;

import java.util.ArrayList;

public class Buffer<T> {
	private T type;
	private ArrayList<T> bufferStore;
	private boolean isDisabled = true;
	
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
	 * Puts the bufferItem at the specified index of the bufferStore ArrayList if possible, otherwise the bufferItem is just appended
	 * @param index where bufferItem will be placed in bufferStore
	 * @param bufferItem the item to place in bufferStore
	 */
	public synchronized void put(int index, T bufferItem) {
		// Put the bufferItem at index in bufferStore, if out of bounds, put at end of bufferStore
		try {
			bufferStore.add(index, bufferItem);
		} catch(IndexOutOfBoundsException e) {
			bufferStore.add(bufferItem);
		}
		notifyAll();
	}
	
	/**
	 * Gets the first bufferItem in the bufferStore ArrayList
	 * @return the first element in bufferStore
	 */
	public synchronized T get() {
		T bufferItem = null;
		// Wait thread until ArrayList is no longer empty
		while(bufferStore.isEmpty() && isDisabled) {
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
	
	/**
	 * Gets the bufferItem at the specified index, if the index provided is out of bounds, get the first element of bufferStore
	 * @return element at index in bufferStore
	 */
	public synchronized T get(int index) {
		T bufferItem = null;
		// Wait thread until ArrayList is no longer empty
		while(bufferStore.isEmpty() && isDisabled) {
			try {
				wait();
			} catch(InterruptedException e) {}
		}
		if(!bufferStore.isEmpty()) {
			// Get element at index, if unavailable, get first element
			try {
				bufferItem = bufferStore.remove(index);
			} catch(IndexOutOfBoundsException e) {
				bufferItem = bufferStore.remove(0);
			}
			
		}
		notifyAll();
		return bufferItem;
	}
}
