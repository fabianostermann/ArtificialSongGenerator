package parts;
import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.theory.Chord;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Key;
import org.jfugue.theory.Note;

import util.Random;


public class BassLine extends SongPartElement {
	
	/**
	 * Constructs a random bass line in JFugue's Staccato String Syntax
	 * @param chordProgression the chord progression that defines the chords the bass line will be based on
	 * @return The random bass line
	 */
	public BassLine(Instrument instrument, int tempo, int length, Key key, Chord[] chords) {
		super(instrument, tempo, length, key, chords);
	}

	// TODO put note length identifiers in map to match integer length and make calculations easier
	public static final String SIXTEENTH = "s";
	public static final String EIGHTH = "i";
	public static final String QUARTER = "q";
	public static final String HALF = "h";
	public static final String WHOLE = "w";

	public static final String REST = "R";
	public static final String NEXT = " ";

	public static final String BASS_OCTAVE = "3";
	
	/** Probabilities for random choices in melody generation (memoized for one complete song) */
	//private static float PROB_SUBOCTAVE = Random.rangeFloat(0f, 0.3f); // Prob. for a sub-bass octave // REMOVED: suboctave not playable on normal e-bass
	
	public String makeMusic() {
		String bassLineStr = "";
		
		for (Chord chord : getChords()) {
			Note[] notes = chord.getNotes();
			String bassNoteStr = notes[0].toString().replaceFirst(""+notes[0].getOctave(), BASS_OCTAVE);
			bassLineStr += NEXT + bassNoteStr;
		}
		
		return bassLineStr;
	}
	
}
