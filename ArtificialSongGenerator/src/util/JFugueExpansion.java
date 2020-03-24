package util;

import org.jfugue.pattern.Pattern;
import org.jfugue.theory.Key;
import org.jfugue.theory.Note;

public class JFugueExpansion {

	/**
	 * BUG REPAIR: jfugue minor signature has bug when transposing notes from diatonic scale
	 * @param the desired minor key
	 * @return the corresponding major key with equal accidental signature
	 */
	public static Key minToMajKey(Key key) {
		Key majKey = new Key(key);
		if (key.getKeySignature().endsWith("min")) {
			Note newRoot = key.getRoot();
			newRoot = new Note(newRoot.getValue()+3);
			majKey = new Key(newRoot + "maj");
		}
		return majKey;
	}
	
	/**
	 * BUG REPAIR: If Tempo markers are placed behind Voice markers, the tempo is not changed immediatly
	 * @param pattern The pattern to be repaired
	 * @return The corresponding pattern with swapped Tempo and Voice markers
	 */
	public static Pattern repairTempoVoiceBug(Pattern pattern) {
		return new Pattern(pattern.toString().replaceAll("(T[0-9]+) (V[0-9]+)", "$2 $1"));
	}
}
