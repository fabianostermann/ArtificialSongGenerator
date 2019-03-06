import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.rhythm.Rhythm;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Key;


public class Songpart implements PatternProducer {
	
	public String mark = null;
	
	public final Melody melody;
	public final ChordProgression chordProgression;
	public final Rhythm rhythm;
	
	public String melodyInstrument;
	public String chordInstrument;
	
	public final Key key;
	public int tempo;
	
	/** number of bars */
	public final int length;
	
	private Songpart() {
		
		key = Config.getRandomKey();
		tempo = Config.getRandomTempo();
		
		length = Config.getRandomSongpartLength();
		
		melodyInstrument = Config.getRandomMelodyInstrument();
		chordInstrument = Config.getRandomChordInstrument();
		
		melody = Melody.newRandomMelody(key, length);
		chordProgression = ChordSequence.newRandomChordProgression(key, length);
		rhythm = Drums.newRandomRhythm(length);
	}
	
	public static Songpart newRandomSongpart() {
		return new Songpart();
	}
	
	@Override
	public Pattern getPattern() {
		return new Pattern(
			//melody.getPattern().setVoice(0).setInstrument(melodyInstrument).setTempo(tempo),
			//chordProgression.getPattern().setVoice(1).setInstrument(chordInstrument).setTempo(tempo),
			rhythm.getPattern().setTempo(tempo)
		);
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
