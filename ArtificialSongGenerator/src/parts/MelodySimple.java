package parts;
import org.jfugue.theory.Chord;
import org.jfugue.theory.Key;

import util.JFugueExpansion;
import util.Random;

/**
 * creates a melody fully driven by random decisions,
 * will not follow anything and lead nowhere
 */

public class MelodySimple extends Melody {

	public MelodySimple(Key key, int length) {
		super(key, length, null);
	}

	public static final String SIXTEENTH = "s";
	public static final String EIGTH = "i";
	public static final String QUARTER = "q";
	public static final String HALF = "h";
	public static final String WHOLE = "w";

	public static final String NEXT = " ";
	public static final String REST = "R";
	
	public static final String SET_VOLUME = " :CON(7, 120) ";
	
	public static final String[] DIATONIC_SCALE = new String[] { "C", "D", "E", "F", "G", "A" };//"B" }; 
	private int currentNote = Random.rangeInt(0, DIATONIC_SCALE.length);
	
	/** Probabilities for random choices in melody generation (memoized for one complete song) */
	private static float PROB_Rest = Random.rangeFloat(0.1f, 0.4f); // Prob. for a rest
	private static float PROB_HalfRest = Random.rangeFloat(0.4f, 0.6f); // Prob. for a half rest
	private static float PROB_QuarterRest = Random.rangeFloat(0.7f, 0.9f); // Prob. for a quarter rest, depends on PROB_HalfRest
	private static float PROB_EigthNote = Random.rangeFloat(0.1f, 0.5f); // Prob. for an eigth note, else quarter note is set
	
	@Override
	protected String newRandomMelodyString() {
		Key key = getKey();
		key = JFugueExpansion.minToMajKey(key);
		String melodyStr = SET_VOLUME+"Key:"+key.getKeySignature();
		
		int lengthEighth = getLength()*8;
		int currLengthEighth = 0; // counter of 8th notes
		
		String nextElement = "";
		while (currLengthEighth < lengthEighth) {
			if (lengthEighth - currLengthEighth == 1) {
			// last eigth space always gets a rest
				nextElement = REST+EIGTH;
				currLengthEighth += 1;
			} else if (lengthEighth - currLengthEighth == 2) {
			// last quarter space always gets a rest
				nextElement = REST+QUARTER;
				currLengthEighth += 2;
			} else if (Random.nextBoolean(PROB_Rest)) {
			// put a rest
				if (lengthEighth - currLengthEighth >= 4 && Random.nextBoolean(PROB_HalfRest)) {
				// prob. set a half rest (if enough space)
					nextElement = REST+HALF;
					currLengthEighth += 4;
				} else if (lengthEighth - currLengthEighth >= 2 && Random.nextBoolean(PROB_QuarterRest)) {
				// prob. set a quarter rest (if enough space)
					nextElement = REST+QUARTER;
					currLengthEighth += 2;
				} else {
				// set a eighth rest (must have enough space)
					nextElement = REST+EIGTH;
					currLengthEighth += 1;
				}
			} else {
				// choose a note
				currentNote = Random.rangeInt(0, DIATONIC_SCALE.length);
				// put a note
				if (Random.nextBoolean(PROB_EigthNote)) {
				// prob. set an eigth note
					nextElement = DIATONIC_SCALE[currentNote]+EIGTH;
					currLengthEighth += 1;
				} else {
				// set a quarter note
					nextElement = DIATONIC_SCALE[currentNote]+QUARTER;
					currLengthEighth += 2;
				}
			}
			melodyStr += NEXT + nextElement;
		}
		
		return melodyStr;
	}
	
}
