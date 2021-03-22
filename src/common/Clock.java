package common;
public class Clock {
	private static long startTime;
	
	public static void startClock() {
		startTime = System.currentTimeMillis();
	}
	
	public static long getTime() {
		return System.currentTimeMillis() - startTime;
	}
}
