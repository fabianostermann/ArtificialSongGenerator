package util;

import org.jfugue.theory.Key;
import org.jfugue.theory.Note;

public class JFugueExpansion {

	public static Key minToMajKey(Key key) {
		Key majKey = new Key(key);
		if (key.getKeySignature().endsWith("min")) {
			// TODO BUG: jfugue minor signature has bug when transposing notes from diatonic scale
			Note newRoot = key.getRoot();
			newRoot = new Note(newRoot.getValue()+3);
			majKey = new Key(newRoot + "maj");
		}
		return majKey;
	}
	
}
