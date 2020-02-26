package parts;
import main.Config;

import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.rhythm.Rhythm;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Key;

import util.Random;


public class Songpart implements PatternProducer {
	
	private int quarterNotesPerBar = 4; // TODO creating music in 3/4 time measure
	
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
		
		if (this.mark == null)
			this.mark = Songpart.nextDefaultMark();
		
		key = Config.GET.randomKey();
		tempo = Config.GET.randomTempo();
		
		length = Config.GET.randomSongpartLength();
		
		melodyInstrument = Config.GET.randomMelodyInstrument();
		chordInstrument = Config.GET.randomChordInstrument();
		arpeggioInstrument = Config.GET.randomArpeggioInstrument();
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

		String restVoice = "";
		for (int i=0; i<length; i++) {
			restVoice += "Rw ";
		}

		Pattern pattern = new Pattern();
		
		// map instruments to midi channels
		for (int ch=0; ch<16; ch++) {
			if (ch == Config.GET.getMelodyChannel(melodyInstrument) && melody != null)
				pattern.add(melody.getPattern().setVoice(ch).setInstrument(melodyInstrument).setTempo(tempo));
			else if (ch == Config.GET.getChordsChannel(chordInstrument) && chords != null)
				pattern.add(chords.getPattern().setVoice(ch).setInstrument(chordInstrument).setTempo(tempo));
			else if (ch == Config.GET.getChordsChannel(arpeggioInstrument) && arpeggio != null)
				pattern.add(arpeggio.getPattern().setVoice(ch).setInstrument(arpeggioInstrument).setTempo(tempo));
			else if (ch == Config.GET.getBassChannel(bassInstrument) && bass != null)
				pattern.add(bass.getPattern().setVoice(ch).setInstrument(bassInstrument).setTempo(tempo));
			else if (ch == 9)
				if (drums != null)
					pattern.add(drums.getPattern().setTempo(tempo));
				else
					pattern.add(Drums.newSilentRhythm(length).getPattern().setTempo(tempo));
			else
				pattern.add(new Pattern(restVoice).setVoice(ch).setTempo(tempo));
		}
		
		return pattern;
	}
	
	public float getLengthInSeconds() {
		return (float)length*quarterNotesPerBar*60.f/(float)tempo;
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
