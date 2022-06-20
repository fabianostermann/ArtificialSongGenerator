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

	/**
	 * @author Fabian Ostermann (fabian.ostermann@udo.edu)
	 */

public class ChordPadsRanged extends SongPartElement {
	
	/**
	 * Constructs a random chord sequence in JFugue's Staccato String Syntax
	 * @param instrument The instrument representation
	 * @param key The key the melody should be in
	 * @param key The key the melody should be in
	 * @param length The number of bars the melody should have
	 */
	public ChordPadsRanged(Instrument instrument, int tempo, int length, Key key, Chord[] chords) {
		super(instrument, tempo, length, key, chords);
		
		Note lowestNote = new Note(
				Random.rangeInt(new Note("D4").getValue(),
								new Note("G4").getValue()));
		// respect instrument range
		while (lowestNote.getValue() < getInstrument().getLowestNote().getValue())
			lowestNote.changeValue(12);
		while (lowestNote.getValue()+12 > getInstrument().getHighestNote().getValue())
				lowestNote.changeValue(-12);
		LOWEST_NOTE = new Note(lowestNote.getValue(), lowestNote.getDuration());
	}
	
	public static final String NEXT = " ";
	public static final String BIND = "+";
	
	private final Note LOWEST_NOTE;

	/**
	 * Creates a random chord sequence in JFugue's Staccato String Syntax
	 */
	@Override
	public String makeMusic() {
		String musicStr = "";
		for (Chord chord : getChords()) {
			musicStr += NEXT;	
			for (int i=0; i<chord.getNotes().length; i++) {
				Note note = chord.getNotes()[i];
				// restrict octave -- TODO BUG: jfugue.Note has a terrible bug when setting octaves..
				while (note.getValue() < LOWEST_NOTE.getValue())
					note.changeValue(12);
				while (note.getValue() > LOWEST_NOTE.getValue()+12)
					note.changeValue(-12);
				note = new Note(note.getValue(), note.getDuration());
				
				// check assert
				if (note.getValue() < getInstrument().getLowestNote().getValue()
						|| note.getValue() > getInstrument().getHighestNote().getValue())
					throw new IllegalStateException("Instrument "+getInstrument().getName()+" cannot play midi key "+note.getValue());
				
				musicStr += (i>0 ? BIND : "") + note;
			}
		}
		return musicStr;
	}
	

}
