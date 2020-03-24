package parts;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.theory.Chord;
import org.jfugue.theory.Key;

import main.Config;
import util.Random;


public class SongPart implements PatternProducer {
	
	private final int quarterNotesPerBar = 4; // TODO creating music in 3/4 time measure
	
	public final String mark = SongPart.nextDefaultMark();
	
	public final Key key = Config.GET.randomKey();
	public final int tempo = Config.GET.randomTempo();
	
	/** number of bars */
	public final int length = Config.GET.randomSongpartLength();
	
	/**  used for chords, arpeggios and bass */
	private final Chord[] chordProgression;
	public final List<SongPartElement> elements = new ArrayList<>();
	public final Drums drums;
	
	public SongPart() {
		
		// make chord progression
		chordProgression = ChordProgressionFactory.makeChords(length, key);
		
		if (Random.nextBoolean(Config.GET.MELODY_ENABLED))
			elements.add(new MelodyBow(
					Config.GET.randomMelodyInstrument(),
					tempo, length, key, chordProgression));
		
		if (Random.nextBoolean(Config.GET.CHORDS_ENABLED))
			elements.add(new ChordPadsRanged(
					Config.GET.randomChordInstrument(),
					tempo, length, key, chordProgression));
		
//		if (Random.nextBoolean(Config.GET.ARPEGGIO_ENABLED))
//			elements.add(new ArpeggioSequence(
//					Config.GET.randomArpeggioInstrument(),
//					key, tempo, length, chordProgression));

		if (Random.nextBoolean(Config.GET.BASS_ENABLED))
			elements.add(new BassLine(
					Config.GET.randomBassInstrument(),
					tempo, length, key, chordProgression));

		if (Random.nextBoolean(Config.GET.DRUMS_ENABLED))
			drums = new Drums(tempo, length);
		else drums = null;
	}
	
	@Override
	public Pattern getPattern() {

		Pattern pattern = new Pattern();
		
		Iterator<SongPartElement> iterator = elements.iterator();
		for (int ch = 0; ch < 16; ch++) {
			if (ch == 9) {
				pattern.add(getDrumPattern());
				ch++;
			}
			if (iterator.hasNext())
				pattern.add(iterator.next().getPattern().setVoice(ch%16));
			else
				pattern.add(SongPartElement.newSilentElement(length).setVoice(ch));
		}
		if (iterator.hasNext()) {
			System.out.println("Warning: More than 16 SongPart elements (including drums)."
					+ "Midi channels are full, so demo midi file is finished early.");
		}
		
		return pattern;
	}
	
	/**
	 * 
	 * Returns the music from the given instrument only
	 * @param instrument The instrument
	 * @return TODO
	 */
	public Pattern getPattern(Instrument instrument) {
		return null;
	}
	
	/**
	 * @return The drum part
	 */
	public Pattern getDrumPattern() {
		Pattern drumPattern = new Pattern();
		if (drums != null)
			drumPattern.add(drums.getPattern());
		else
			drumPattern.add(Drums.newSilentRhythm(length).getPattern().setTempo(tempo));
		
		return drumPattern;
	}
	
	public float getLengthInSeconds() {
		return (float)length*quarterNotesPerBar*60.f/(float)tempo;
	}

	public static final char FIRST_CHAR = 'A';
	public static final char LAST_CHAR = 'Z';
	private static char currentChar = FIRST_CHAR-1;
	private static int currentCharCount = 1;
	private static String nextDefaultMark() {
		currentChar++;
		if (currentChar > LAST_CHAR) {
			currentChar = FIRST_CHAR;
			currentCharCount++;
		}
		String mark = "";
		for (int i=0; i<currentCharCount; i++)
			mark += currentChar;
		return mark;
	}
}
