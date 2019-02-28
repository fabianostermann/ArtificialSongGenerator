import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Key;


public class ChordSequence {

	/** Probabilities for random choices in chord generation (memoized for one complete song) */
	private static float PROB_MultiChordBar = Random.rangeFloat(0.1f, 0.4f);
	
	/** simplified version of major scale degrees
	 *  - VII is omitted to simplify the creation of harmonic sequences */
	public static final String DEGREES = "I ii iii IV V vi";
	public static final String NEXT = " $";
	public static final String WHOLE = "w";
	public static final String HALF = "h";
	
	/**
	 * Constructs a random chord sequence in JFugue's Staccato String Syntax (e.g "$0w $1h $2h $3w..")
	 * @return The random chord sequence
	 */
	public static ChordProgression newRandomChordProgression(Key key, int length) {
		String randomSequence = "";
		
		boolean multiChordBarAllowed = false;
		for (int i=0; i<length; i++) {
			if (multiChordBarAllowed && Random.nextBoolean(PROB_MultiChordBar)) {
				randomSequence += NEXT + Random.rangeInt(0, 5) + HALF;
				randomSequence += NEXT + Random.rangeInt(0, 5) + HALF;
				multiChordBarAllowed = false;
			} else {
				randomSequence += NEXT + Random.rangeInt(0, 5) + WHOLE;
				multiChordBarAllowed = true;
			}
		}
		// !! it's important to remove initial whitespace
		// Staccato skips first chord otherwise !!
		randomSequence = randomSequence.trim();
		return new ChordProgression(DEGREES).setKey(key).allChordsAs(randomSequence);
	}

}
