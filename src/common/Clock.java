package common;

public class Clock {
	private static long startTime;
	/**
	 * Starts the simulation clock.
	 */
	public static void startClock() {
		startTime = System.currentTimeMillis();
	}
	/**
	 * Returns the time elapsed since the clock started.
	 * @return	Time elapsed.
	 */
	public static long getTime() {
		return System.currentTimeMillis() - startTime;
	}
}
