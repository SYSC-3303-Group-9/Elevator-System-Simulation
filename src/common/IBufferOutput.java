package common;

/**
 * Represents the output side to a buffer.
 */
public interface IBufferOutput<T> {
	/**
	 * Gets the first bufferItem in the bufferStore ArrayList
	 * @return the first element in bufferStore
	 */
	public T get();
}
