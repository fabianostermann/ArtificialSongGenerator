package parts;

import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.theory.Chord;
import org.jfugue.theory.Key;

public abstract class SongPartElement implements PatternProducer {
	
	private final Instrument instrument;
	/** number of bars */
	private final int length;
	private final int tempo;
	
	private final Key key;
	private final Chord[] chords;
	
	private String musicString = null;
	
	/**
	 * 
	 * @param instrument The string representation of the desired 
	 * 					instrument of the musical element
	 * @param key The key of the musical element
	 * @param tempo The tempo of the musical element
	 * @param length The number of bars of the musical element
	 */
	public SongPartElement(Instrument instrument, int tempo, int length, Key key, Chord[] chords) {
		if (instrument == null)
			throw new NullPointerException("instrument must not be null");
		this.instrument = instrument;
		this.key = key;
		this.tempo = tempo;
		this.length = length;
		this.chords = chords;
	}
	
	/**
	 * The main function were the music is composed
	 * @return The music as JFugue Staccato music string
	 */
	abstract public String makeMusic();

	/**
	 * Triggers the makeMusic() function on first call.
	 * Sets the patterns instrument and tempo.
	 * @return The pattern that was created
	 */
	@Override
	public final Pattern getPattern() {
		// first time make music string
		if (musicString == null)
			musicString = makeMusic();
		return (new Pattern(musicString))
				.setInstrument(getInstrument().getMidiString())
				.setTempo(getTempo());
	}
	
//	/**
//	 * For short descriptions of the implementing generators behavior
//	 * @return the informative description
//	 */
//	abstract public String getDescription();
	/**
	 * The name of the generator class
	 * @return
	 */
	public String getName() {
		return this.getClass().getSimpleName();
	}

	/** @return The string representation of the desired 
	 * 			instrument of the musical element */
	public Instrument getInstrument() {
		return instrument;
	}

	/** @return The key of the musical element */
	public Key getKey() {
		return key;
	}
	
	/** @return An array of chords */
	public Chord[] getChords() {
		return chords;
	}
	
	/** @return The tempo of the musical element */
	public int getTempo() {
		return tempo;
	}
	
	/** @return The number of bars of the musical element */
	public int getLength() {
		return length;
	}
	
	public static Pattern newSilentElement(int length, int tempo) {
		String restVoice = "";
		for (int i=0; i<length; i++) {
			restVoice += "Rw ";
		}
		return (new Pattern(restVoice)).setTempo(tempo);
	}
}
