package parts;
import main.Config;

import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.rhythm.Rhythm;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Key;

import util.Random;


public class Songpart implements PatternProducer {
	
	public String mark = null;
	
	public final Melody melody;
	public final ChordProgression chordProgression; // used for chords, arpeggios and bass
	public final ChordProgression chords;
	public final ArpeggioSequence arpeggio;
	public final BassLine bass;
	public final Rhythm drums;
	
	public String melodyInstrument;
	public String chordInstrument;
	public String arpeggioInstrument;
	public String bassInstrument;
	
	public final Key key;
	public int tempo;
	
	/** number of bars */
	public final int length;
	
	private Songpart() {
		
		key = Config.GET.randomKey();
		tempo = Config.GET.randomTempo();
		
		length = Config.GET.randomSongpartLength();
		
		melodyInstrument = Config.GET.randomMelodyInstrument();
		chordInstrument = Config.GET.randomChordInstrument();
		arpeggioInstrument = Config.GET.randomChordInstrument();
		bassInstrument = Config.GET.randomBassInstrument();
		
		chordProgression = ChordSequence.newRandomChordProgression(key, length);
		if (Random.nextBoolean(Config.GET.MELODY_ENABLED))
			melody = Melody.newRandomMelody(key, length);
		else melody = null;
		if (Random.nextBoolean(Config.GET.CHORDS_ENABLED))
			chords = chordProgression;
		else chords = null;
		if (Random.nextBoolean(Config.GET.ARPEGGIO_ENABLED))
			arpeggio = ArpeggioSequence.newRandomArpeggio(chordProgression);
		else arpeggio = null;
		if (Random.nextBoolean(Config.GET.BASS_ENABLED))
			bass = BassLine.newRandomBassLine(chordProgression);
		else bass = null;
		if (Random.nextBoolean(Config.GET.DRUMS_ENABLED))
			drums = Drums.newRandomRhythm(length);
		else drums = null;
	}
	
	public static Songpart newRandomSongpart() {
		return new Songpart();
	}
	
	@Override
	public Pattern getPattern() {

		Pattern pattern = new Pattern();
		if (melody != null)
			pattern.add(melody.getPattern().setVoice(0).setInstrument(melodyInstrument).setTempo(tempo));
		if (chords != null)
			pattern.add(chords.getPattern().setVoice(1).setInstrument(chordInstrument).setTempo(tempo));
		if (arpeggio != null)
			pattern.add(arpeggio.getPattern().setVoice(2).setInstrument(arpeggioInstrument).setTempo(tempo));
		if (bass != null)
			pattern.add(bass.getPattern().setVoice(3).setInstrument(bassInstrument).setTempo(tempo));
		if (drums != null)
			pattern.add(drums.getPattern().setTempo(tempo));
		
		return pattern;
	}
	
	public float getLengthInSeconds() {
		return (float)length*4.f*60.f/(float)tempo;
	}

	public static final char FIRST_CHAR = 'A';
	public static final char LAST_CHAR = 'Z';
	private static char currentChar = FIRST_CHAR-1;
	public static String nextDefaultMark() {
		currentChar++;
		return new Character(
			currentChar > LAST_CHAR
			? currentChar = FIRST_CHAR
			: currentChar
		).toString();
	}
}
