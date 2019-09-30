package util;

import java.security.SecureRandom;
import java.util.Map;

public class Random {
	
	/**
	 * This class has been extended on 6 March 2019
	 */

	private static SecureRandom random = new SecureRandom();
	
	/**
	 * 
	 * @param min   inclusive
	 * @param max   exclusive
	 * @return a number between [min, max-1]
	 * @throws exceptions   but nothing useful, be careful to use properly
	 */
	public static int rangeInt(int min, int max) {
		if (min == max)
			return min;
		return (int)(random.nextInt(max-min)+min);
	}
	
	/** range: 0 to (n-1) **/
	public static int nextInt(int n) {
		return random.nextInt(n);
	}
	
	/** 50-50 **/
	public static boolean nextBoolean() {
		return nextBoolean(0.5f);
	}
	
	/** true with possibility f **/
	public static boolean nextBoolean(float f) {
		return (random.nextFloat() < f);
	}

	public static float nextFloat() {
		return random.nextFloat();
	}
	
	/**
	 * 
	 * @param min   inclusive
	 * @param max   exclusive
	 * @return a floating-point number in [min, max]
	 * @throws exceptions   but nothing useful, be careful to use properly
	 */
	public static float rangeFloat(float min, float max) {
		return random.nextFloat()*(max-min)+min;
	}
	
	/**
	 * Gets an array of strings and an array of probabilitiy weights (must not be sum to 1) 
	 * @param strings Array of strings
	 * @param probs Array of probabilities
	 * @return One of the strings from the array with given probability
	 */
	public static String fromArray(String[] strings, float[] probs) {
		float sum = 0;
		for (float f : probs)
			sum += f;
		float choice = rangeFloat(0, sum);
		float probSum = 0;
		for (int i=0; i<strings.length; i++) {
			probSum += probs[i];
			if (probSum > choice)
				return strings[i];
		}
		return "error";
	}
	
	/**
	 * Gets an array of strings
	 * @param strings Array of strings
	 * @return One of the strings with even probability
	 */
	public static String fromArray(String[] strings) {
		float[] probs = new float[strings.length];
		for (int i=0; i<probs.length; i++)
			probs[i] = 1.f;
		return fromArray(strings, probs);
	}
	
	/**
	 * Same as fromArray put with a map as input
	 * @param map Containing strings and probabilities as floats
	 * @return One of the strings from the array with given probability
	 */
	public static String fromMap(Map<String, Float> map) {
		String[] strings = map.keySet().toArray(new String[0]);
		float[] probs = new float[strings.length];
		for (int i=0; i<strings.length; i++)
			probs[i] = map.get(strings[i]);
		return fromArray(strings, probs);
	}
}