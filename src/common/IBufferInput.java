package common;

/**
 * Represents the input side to a buffer.
 */
public interface IBufferInput<T> {

	/**
	 * Puts the bufferItem at the end of the bufferStore ArrayList
	 * @param bufferItem an item to append to bufferStore 
	 */
	public void put(T bufferItem);
}
