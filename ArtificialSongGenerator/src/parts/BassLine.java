package parts;
import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.theory.Chord;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Note;

import util.Random;


public class BassLine implements PatternProducer {

	public final Chord[] chordProgression;
	
	public String bassLineString;
	
	private BassLine(Chord[] chordProgression) {
		this.chordProgression = chordProgression;
		
		bassLineString = newRandomBassLineString();
	}
	
	/**
	 * Constructs a random bass line in JFugue's Staccato String Syntax
	 * @param chordProgression the chord progression that defines the chords the bass line will be based on
	 * @return The random bass line
	 */
	public static BassLine newRandomBassLine(Chord[] chordProgression) {
		return new BassLine(chordProgression);
	}
	
	@Override
	public Pattern getPattern() {
		return new Pattern(bassLineString);
	}

	// TODO put note length identifiers in map to match integer length and make calculations easier
	public static final String SIXTEENTH = "s";
	public static final String EIGHTH = "i";
	public static final String QUARTER = "q";
	public static final String HALF = "h";
	public static final String WHOLE = "w";

	public static final String REST = "R";
	public static final String NEXT = " ";

	public static final String OCTAVE_BASS = "3";
	public static final String OCTAVE_SUB_BASS = "2";
	
	/** Probabilities for random choices in melody generation (memoized for one complete song) */
	//private static float PROB_SUBOCTAVE = Random.rangeFloat(0f, 0.3f); // Prob. for a sub-bass octave // REMOVED: suboctave not playable on normal e-bass
	
	public String newRandomBassLineString() {
		String bassLineStr = "";
		
		String OCTAVE = /*Random.nextBoolean(PROB_SUBOCTAVE) ? OCTAVE_SUB_BASS :*/ OCTAVE_BASS;
		
		for (Chord chord : chordProgression) {
			Note[] notes = chord.getNotes();
			String bassNoteStr = notes[0].toString().replaceFirst(""+notes[0].getOctave(), OCTAVE);
			bassLineStr += NEXT + bassNoteStr;
		}
		
		return bassLineStr;
	}
	
}
