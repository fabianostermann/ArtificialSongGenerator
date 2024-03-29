package parts;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.theory.Chord;
import org.jfugue.theory.Key;

import main.ArtificialSongGenerator;
import main.Config;
import util.JFugueExpansion;
import util.Random;

/**
 * @author Fabian Ostermann (fabian.ostermann@udo.edu)
 */
 
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
	public RhythmSimpleGrooves drums = null;
	
	/**
	 * One song part of the full composition.
	 */
	public SongPart() {

		// make chord progression
		chordProgression = ChordProgressionFactory.makeChords(length, key);
		
		while (elements.isEmpty() && drums == null) { // ensures songpart is not empty
			
			if (Random.nextBoolean(Config.GET.MELODY_ENABLED))
				elements.add(new MelodyBow(
						Config.GET.randomMelodyInstrument(),
						tempo, length, key, chordProgression));
			
			Instrument chordInstr = Config.GET.randomChordInstrument();
			if (Random.nextBoolean(Config.GET.CHORDS_ENABLED))
				elements.add(new ChordPadsRanged(
						chordInstr,
						tempo, length, key, chordProgression));
			
			if (Random.nextBoolean(Config.GET.ARPEGGIO_ENABLED)) {
				Instrument arpInstr = null;
				while (arpInstr == null || arpInstr.equals(chordInstr))
					arpInstr = Config.GET.randomArpeggioInstrument();
				elements.add(new ChordArpeggios(
						arpInstr,
						tempo, length, key, chordProgression));
			}
	
			if (Random.nextBoolean(Config.GET.BASS_ENABLED))
				elements.add(new BassLine(
						Config.GET.randomBassInstrument(),
						tempo, length, key, chordProgression));
	
			if (Random.nextBoolean(Config.GET.DRUMS_ENABLED))
				drums = new RhythmSimpleGrooves(tempo, length);
			else drums = null;
		}
	}
	
	@Override
	public Pattern getPattern() {
		Pattern pattern = new Pattern();
		
		Iterator<SongPartElement> iterator = elements.iterator();
		for (int ch = 0; ch < 16; ch++) {
			if (ch == 9) {
				if (drums != null)
					pattern.add(drums.getPattern());
				else
					pattern.add(RhythmSimpleGrooves.newSilentRhythm(length, tempo));
				ch++;
			}
			if (iterator.hasNext())
				pattern.add(iterator.next().getPattern().setVoice(ch));
			else
				pattern.add(SongPartElement.newSilentElement(length, tempo).setVoice(ch));
		}
		if (iterator.hasNext()) {
			System.out.println("Warning: More than 16 SongPart elements (including drums)."
					+ "Midi channels are full, so demo midi file is incomplete.");
		}
		
		return JFugueExpansion.repairMusicString(pattern);
	}
	
	/**
	 * 
	 * Returns the music from the given instrument only or all music if instrument is null
	 * @param instrument An instrument or null
	 * @return a pattern containing all music played by the given instrument
	 * 			or demo music with up to 16 instruments if parameter was null
	 */
	public Pattern getPattern(final Instrument instrument) {
		Pattern pattern = SongPartElement.newSilentElement(length, tempo);
		
		// new version does not support voices (tracks) anymore for safety reasons!

		Iterator<SongPartElement> iterator = elements.iterator();
		while (iterator.hasNext()) {
			SongPartElement element = iterator.next();
			if (element.getInstrument().equals(instrument)) {
				pattern = element.getPattern();
				break;
			}
		}
		while (iterator.hasNext()) {
			SongPartElement element = iterator.next();
			if (element.getInstrument().equals(instrument)) {
				ArtificialSongGenerator.LOGGER.log(Level.WARNING,
						"Songpart "+mark+" has more than one songpart element using "+instrument.getName()+"."
						+ " That is not supported at the moment and will cause errors on sampling!");
				ArtificialSongGenerator.LOGGER.log(Level.SEVERE,
						"Aborting.");
				System.exit(1);
			}
		}
		
		return JFugueExpansion.repairMusicString(pattern);
	}
	
	/**
	 * @return The drum part of this songpart
	 */
	public Pattern getDrumPattern() {
		Pattern drumPattern = new Pattern();
		if (drums != null)
			drumPattern.add(drums.getPattern());
		else
			drumPattern.add(RhythmSimpleGrooves.newSilentRhythm(length, tempo));
		
		// tempo track
		drumPattern.add(SongPartElement.newSilentElement(length, tempo).setVoice(0));
		
		return JFugueExpansion.repairMusicString(drumPattern);
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
