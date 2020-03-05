package parts;

import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.theory.Chord;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Key;

public abstract class Melody implements PatternProducer {

	private final Key key;
	private final int length;
	private final Chord[] chords;
	
	private String melodyString;
	
	/**
	 * Constructs a random melody fragment in JFugue's Staccato String Syntax
	 * @param key The key the melody should be in
	 * @param length The number of bars the melody should have
	 * @param chords The underlying chord progression
	 */
	public Melody(Key key, int length, Chord[] chords) {
		this.key = key;
		this.length = length;
		this.chords = chords;
		
		melodyString = newRandomMelodyString();
	}
	
	@Override
	public Pattern getPattern() {
		return new Pattern(melodyString);
	}
	
	/** construct a staccato string (JFugue) that represents a melody */
	protected abstract String newRandomMelodyString();

	/** @return The key the melody should be in */
	public Key getKey() {
		return key;
	}
	
	/** @return The number of bars the melody should have */
	public int getLength() {
		return length;
	}

	/** @return The underlying chord progression */
	public Chord[] getChords() {
		return chords;
	}
	
}
