package parts;
import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.theory.Chord;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Note;

import util.Random;


public class ArpeggioSequence implements PatternProducer {

	public final Chord[] chordProgression;
	
	public String arpeggioString;
	
	private ArpeggioSequence(Chord[] chordProgression) {
		this.chordProgression = chordProgression;
		
		arpeggioString = newRandomArpeggioString();
	}
	
	/**
	 * Constructs a random arpeggio sequence in JFugue's Staccato String Syntax
	 * @param chordProgression the chord progression that defines the chords to appegiate and thus key and length
	 * @return The random arpeggio sequence
	 */
	public static ArpeggioSequence newRandomArpeggio(Chord[] chordProgression) {
		return new ArpeggioSequence(chordProgression);
	}
	
	@Override
	public Pattern getPattern() {
		return new Pattern(arpeggioString);
	}

	// TODO put note length identifiers in map to match integer length and make calculations easier
	public static final String SIXTEENTH = "s";
	public static final String EIGHTH = "i";
	public static final String QUARTER = "q";
	public static final String HALF = "h";
	public static final String WHOLE = "w";

	public static final String REST = "R";
	public static final String NEXT = " ";
	
	public static final String SET_VOLUME = " :CON(7, 70) ";
	
	///** Probabilities for random choices in melody generation (memoized for one complete song) */
	//private static float PROB_Rest = Random.rangeFloat(0.1f, 0.4f); // Prob. for a rest
	
	public String newRandomArpeggioString() {
		String arpeggioStr = SET_VOLUME;
		
		// random arpeggio style (example: { 0, 1, 0, 2, 1, 1, 2, 0})
		int[] arpeggioStyle = new int[8];
		arpeggioStyle[0] = Random.rangeInt(0, 3);
		for (int i=1; i<arpeggioStyle.length; i++) {
			arpeggioStyle[i] = (arpeggioStyle[i-1] + Random.rangeInt(1, 3)) % 3; // ensure difference of consecutive notes
		}
		
		// chords to arpeggios
		for (Chord chord : chordProgression) {
			String chordStr = chord.toString();
			Note[] notes = new Chord(chordStr).getNotes();
			int chordDurationInEighths = (int)(notes[0].getDuration()*8);
			
			int i=0;
			while (i < chordDurationInEighths) {
				arpeggioStr += NEXT + notes[arpeggioStyle[i]].getToneString() + EIGHTH;
				i++;
			}
		}
		
//		System.out.println(arpeggioStr);
		return arpeggioStr;
	}
	
}
