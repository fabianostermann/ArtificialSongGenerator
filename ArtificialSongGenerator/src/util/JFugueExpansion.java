package util;

import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.Token;
import org.jfugue.theory.Key;
import org.jfugue.theory.Note;
import org.jfugue.tools.GetPatternStats;

public class JFugueExpansion {

	public class GeneralStats {
		// From class org.jfugue.tools.GetPatternStats
		// for function: public int[] getGeneralStats()
		// Array index 0: N of Notes; index 1: N of rests; index 2: N of measures
		public static final int numOfNotes = 0;
		public static final int numOfRests = 1;
		public static final int numOfMeasures = 2;
		
	}
	
	/**
	 * BUG REPAIR: If pattern has no notes, getGeneralStats()[0] returns 1; if one note, also 1.
	 * @param pattern that will be checked
	 * @return true if number of notes is indeed 0, else false
	 */
	public static boolean checkIfEmpty(Pattern pattern) {
		// repair whitespace error
		pattern = repairMusicString(pattern);
		// check for notes
		for (Token t : pattern.getTokens()) {
			if (t.getType() == Token.TokenType.NOTE)
				if (!t.toString().startsWith("R"))
					// found a note token that is no rest
					return false;
		}
		return true;
	}
	
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
	 * BUG REPAIR: If Tempo markers are placed behind Voice markers, the tempo is not changed immediatly.
	 * 				This method will swap them.<br>
	 * BUG REPAIR: Also the pattern.getTokens() method fails if two whitespaces are written in music string.
	 * 				Therefor all multiple spaces " +" will be replaced by one " "
	 * @param pattern The pattern to be repaired
	 * @return The corresponding pattern with swapped Tempo and Voice markers
	 */
	public static Pattern repairMusicString(Pattern pattern) {
		pattern = new Pattern(pattern.toString().replaceAll(" +", " "));
		pattern = new Pattern(pattern.toString().replaceAll("(T[0-9]+) (V[0-9]+)", "$2 $1"));
		return pattern;
	}
}
