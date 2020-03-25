package parts;

import java.util.ArrayList;
import java.util.List;

import org.jfugue.theory.Chord;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Key;

import util.JFugueExpansion;
import util.Random;

public class ChordProgressionFactory {
	
	/** Probabilities for random choices in chord generation (memoized for one complete song) */
	private static float PROB_MultiChordBar = Random.rangeFloat(0.1f, 0.4f);
	
	/** simplified version of major scale degrees
	 *  - vii is omitted to simplify the creation of harmonic sequences */
	public static final String DEGREES = "I ii iii IV V vi"; // TODO add secondary dominantes
	public static final String WHOLE = "w";
	public static final String HALF = "h";

	
	
	/**
	 * Constructs a simple random but likable chord progression in JFugue's Staccato String Syntax
	 * @return The created chord progression as array
	 */
	public static Chord[] makeChords(int length, Key key) {
		
		int rootPos = 0;
		if (key.getKeySignature().toLowerCase().endsWith("min"))
			rootPos = 5;
		key = JFugueExpansion.minToMajKey(key);
		Chord[] chordPool = new ChordProgression(DEGREES).setKey(key).getChords();
		
		List<Chord> chords = new ArrayList<>();
		List<String> chordLengths = new ArrayList<>();
		
		// start with I, IV or V // 
		chords.add(chordPool[Random.fromArray(new Integer[]{rootPos,3,4})]); chordLengths.add(WHOLE);
		boolean multiChordBarAllowed = true;
		for (int i=1; i<length-1; i++) {
			if (multiChordBarAllowed && Random.nextBoolean(PROB_MultiChordBar)) {
				chords.add(chordPool[Random.rangeInt(0, 6)]); chordLengths.add(HALF);
				chords.add(chordPool[Random.rangeInt(0, 6)]); chordLengths.add(HALF);
				multiChordBarAllowed = false;
			} else {
				chords.add(chordPool[Random.rangeInt(0, 6)]); chordLengths.add(WHOLE);
				multiChordBarAllowed = true;
			}
		}
		if (length>1) {
			chords.add(chordPool[rootPos]); chordLengths.add(WHOLE);
		}
		
		String chordStr;
		if (chords.size() != chordLengths.size())
			throw new RuntimeException("Size of 'chords' and 'chordsLength' does not match.");
		Chord[] chordProg = new Chord[chords.size()];
		for (int i=0; i<chordProg.length; i++) {
			chordStr = chords.get(i).getPattern() + chordLengths.get(i);
			chordProg[i] = new Chord(chordStr);
		}
		
		return chordProg;
	}
	
}
