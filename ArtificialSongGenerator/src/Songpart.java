import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.rhythm.Rhythm;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Key;


public class Songpart implements PatternProducer {
	
	public String mark = null;
	
	public final Melody melody;
	public final ChordProgression chords;
	public final Rhythm rhythm;
	
	public String melodyInstrument;
	public String chordInstrument;
	
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
		
		if (Config.GET.MELODY_ENABLED)
			melody = Melody.newRandomMelody(key, length);
		else melody = null;
		if (Config.GET.CHORDS_ENABLED)
			chords = ChordSequence.newRandomChordProgression(key, length);
		else chords = null;
		if (Config.GET.DRUMS_ENABLED)
			rhythm = Drums.newRandomRhythm(length);
		else rhythm = null;
	}
	
	public static Songpart newRandomSongpart() {
		return new Songpart();
	}
	
	@Override
	public Pattern getPattern() {
		int melodyChannel = Config.GET.getMelodyChannel(melodyInstrument);
		int chordsChannel = Config.GET.getChordsChannel(chordInstrument);
		int drumsChannel = 9;
		System.out.print(melodyChannel+"-");
		System.out.println(chordsChannel+"-"+drumsChannel);
		
		Pattern pattern = new Pattern();
		if (melody != null)
			pattern.add(melody.getPattern().setVoice(melodyChannel).setInstrument(melodyInstrument).setTempo(tempo));
		if (chords != null)
			pattern.add(chords.getPattern().setVoice(chordsChannel).setInstrument(chordInstrument).setTempo(tempo));
		if (rhythm != null)
			pattern.add(rhythm.getPattern().setTempo(tempo));
		
		//TODO dirty way to silent unused voices, think about cleaner way of channel setting
		String restVoice = "";
		for (int i=0; i<length; i++)
			restVoice += "Rw ";
		for (int channel=0; channel<16; channel++)
			if (channel != melodyChannel && channel != chordsChannel && channel != drumsChannel)
				pattern.add(new Pattern(restVoice).setVoice(channel).setTempo(tempo));
		
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
