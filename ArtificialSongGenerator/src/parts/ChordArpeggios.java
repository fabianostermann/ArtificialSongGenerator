package parts;
import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.theory.Chord;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Key;
import org.jfugue.theory.Note;

import util.Random;

public class ChordArpeggios extends SongPartElement {
	
	/**
	 * Creates a random arpeggio sequence
	 * @param instrument The instrument representation
	 * @param key The key the melody should be in
	 * @param key The key the melody should be in
	 * @param length The number of bars the melody should have
	 */
	public ChordArpeggios(Instrument instrument, int tempo, int length, Key key, Chord[] chords) {
		super(instrument, tempo, length, key, chords);
		
		Note lowestNote = new Note(
				Random.rangeInt(new Note("F4").getValue(),
								new Note("B4").getValue()));
		// respect instrument range
		while (lowestNote.getValue() < getInstrument().getLowestNote().getValue())
			lowestNote.changeValue(12);
		while (lowestNote.getValue()+12 > getInstrument().getHighestNote().getValue())
				lowestNote.changeValue(-12);
		LOWEST_NOTE = new Note(lowestNote.getValue(), lowestNote.getDuration());
	}
	
	private final Note LOWEST_NOTE;

	public static final String SIXTEENTH = "s";
	public static final String EIGHTH = "i";
	public static final String QUARTER = "q";
	public static final String HALF = "h";
	public static final String WHOLE = "w";

	public static final String REST = "R";
	public static final String NEXT = " ";
	
	//public static final String SET_VOLUME = " :CON(7, 70) ";

	/**
	 * Creates a random arpeggio sequence in JFugue's Staccato String Syntax
	 * @return The random arpeggio sequence
	 */
	public String makeMusic() {
		String arpeggioStr = "";
		
		// random arpeggio style (example: { 0, 1, 0, 2, 1, 1, 2, 0})
		int[] arpeggioStyle = new int[8];
		arpeggioStyle[0] = Random.rangeInt(0, 3);
		for (int i=1; i<arpeggioStyle.length; i++) {
			arpeggioStyle[i] = (arpeggioStyle[i-1] + Random.rangeInt(1, 3)) % 3; // ensure difference of consecutive notes
		}
		
		// chords to arpeggios
		for (Chord chord : getChords()) {
			String chordStr = chord.toString();
			Note[] notes = new Chord(chordStr).getNotes();
			int chordDurationInEighths = (int)(notes[0].getDuration()*8);
			
			for (int i=0; i<notes.length; i++) {
				// restrict octave -- TODO BUG: jfugue.Note has a terrible bug when setting octaves..
				while (notes[i].getValue() < LOWEST_NOTE.getValue())
					notes[i].changeValue(12);
				while (notes[i].getValue() > LOWEST_NOTE.getValue()+12)
					notes[i].changeValue(-12);
				notes[i] = new Note(notes[i].getValue(), notes[i].getDuration());
			}
			
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
