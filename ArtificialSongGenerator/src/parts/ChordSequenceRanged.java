package parts;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.theory.Chord;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Key;
import org.jfugue.theory.Note;

import util.JFugueExpansion;
import util.Random;


public class ChordSequenceRanged implements PatternProducer {
	
	private final Key key;
	private final int length;
	
	private Chord[] chordPool;
	private Chord[] chords;
	
	/**
	 * Constructs a random chord sequence in JFugue's Staccato String Syntax
	 * @param key The key the melody should be in
	 * @param length The number of bars the melody should have
	 */
	public ChordSequenceRanged(Key key, int length) {
		this.key = key;
		this.length = length;
		
		chords = newRandomChords(key, length);
	}
	
	private Note LOWEST_NOTE = new Note(
			Random.rangeInt(new Note("D4").getValue(),
							new Note("A4").getValue()));
	@Override
	public Pattern getPattern() {
		String musicStr = "";
		for (Chord chord : chords) {
			musicStr += NEXT;	
			for (int i=0; i<chord.getNotes().length; i++) {
				Note note = chord.getNotes()[i];
				// restrict octave -- TODO BUG: jfugue.Note has a terrible bug when setting octaves..
				while (note.getValue() < LOWEST_NOTE.getValue())
					note.changeValue(12);
				while (note.getValue() > LOWEST_NOTE.getValue()+12)
					note.changeValue(-12);
				note = new Note(note.getValue(), note.getDuration());
				musicStr += (i>0 ? BIND : "") + note;
			}
		}
		return new Pattern(musicStr);
	}
	
	public Chord[] getChords() {
		return chords;
	}
	
	/** Probabilities for random choices in chord generation (memoized for one complete song) */
	private static float PROB_MultiChordBar = Random.rangeFloat(0.1f, 0.4f);
	
	/** simplified version of major scale degrees
	 *  - vii is omitted to simplify the creation of harmonic sequences */
	public static final String DEGREES = "I ii iii IV V vi"; // TODO add secondary dominantes
	public static final String NEXT = " ";
	public static final String BIND = "+";
	public static final String WHOLE = "w";
	public static final String HALF = "h";

	/**
	 * Constructs a random chord sequence in JFugue's Staccato String Syntax
	 * @return The random chord sequence as chord array
	 */
	private Chord[] newRandomChords(Key key, int length) {
		
		int rootPos = 0;
		if (key.getKeySignature().toLowerCase().endsWith("min"))
			rootPos = 5;
		key = JFugueExpansion.minToMajKey(key);
		chordPool = new ChordProgression(DEGREES).setKey(key).getChords();
		
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
