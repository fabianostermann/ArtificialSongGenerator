package parts;
import java.util.HashMap;
import java.util.Map;

import org.jfugue.theory.Chord;
import org.jfugue.theory.Key;
import org.jfugue.theory.Note;

import util.JFugueExpansion;
import util.Random;

/**
 * creates a melody fully driven by random decisions,
 * will not follow anything and lead nowhere
 * TODO BUG: for key F#maj the ending note is F
 * T--- that's not the root but the maj7, but that's accepted for now
 */

public class MelodyBow extends Melody {

	public MelodyBow(Key key, int length, Chord[] chords) {
		super(key, length, chords);
	}

	public static final String SIXTEENTH = "s";
	public static final String EIGTH = "i";
	public static final String QUARTER = "q";
	public static final String HALF = "h";
	public static final String WHOLE = "w";

	public static final String NEXT = " ";
	public static final String REST = "R";
	
	public static final String[] DIATONIC_SCALE = new String[] { "C", "D", "E", "F", "G", "A", "B" };
	public static final Map<String, Integer> DIATONIC_MAP = new HashMap<>();
	static {
		DIATONIC_MAP.put("C", 0);
		DIATONIC_MAP.put("D", 1);
		DIATONIC_MAP.put("E", 2);
		DIATONIC_MAP.put("F", 3);
		DIATONIC_MAP.put("G", 4);
		DIATONIC_MAP.put("A", 5);
		DIATONIC_MAP.put("B", 6);
	}
	
	/**
	 * Creates a note in a diatonic scale as relative transposition from a given root note
	 * @param root The root position in the underlying scale (C=0, D=1,..)
	 * @param octave The octave of the root note (C1,C2,C3,..)
	 * @param relation The relative transposition through the underlying scale
	 * @return A staccato note string
	 */
	public String getMelodyToneByRelation(int root, int octave, int relation) {
		int tone = root; // relation == 0
		// TODO make tone by relation calculation more efficient
		while (relation > 0) {
			relation--; tone++;
			if (tone >= DIATONIC_SCALE.length) { tone = 0; octave++; }
		}
		while (relation < 0) {
			relation++; tone--;
			if (tone < 0) { tone = DIATONIC_SCALE.length-1; octave--;}
		}
		return DIATONIC_SCALE[tone]+octave;
	}
	
	public static final String SET_VOLUME = " :CON(7, 120) ";
	
	/** Probabilities for random choices in melody generation (memoized for one complete song) */
	private static float PROB_QuarterBind = Random.rangeFloat(0.1f, 0.9f); // Prob. for a rest
	private static float PROB_Rest = Random.rangeFloat(0.1f, 0.4f); // Prob. for a rest
	private static float PROB_QuarterNote = Random.rangeFloat(0.1f, 0.8f); // Prob. for an quarter note, else an eighth note is set
	private static float PROB_HalfNote = PROB_QuarterNote + Random.rangeFloat(-0.2f, 0.2f); // Prob. for an quarter note, else an eighth note is set
	
	@Override
	protected String newRandomMelodyString() {
		Key key = getKey();
		key = JFugueExpansion.minToMajKey(key);
		String melodyStr = SET_VOLUME+"Key:"+key.getKeySignature();
		
		int lengthEighth = getLength()*8;
		Integer[] melodyRaster = new Integer[lengthEighth];
		
		Chord[] chordRaster = new Chord[lengthEighth];
		int cq = 0;
		// fill chord raster
		for (Chord chord : getChords()) {
			if (chord.getNotes()[0].getDecoratorString().endsWith("w")) {
				chordRaster[cq++] = chord;
				chordRaster[cq++] = chord;
				chordRaster[cq++] = chord; chordRaster[cq++] = chord;
				chordRaster[cq++] = chord; chordRaster[cq++] = chord;
				chordRaster[cq++] = chord; chordRaster[cq++] = chord;
			} else if (chord.getNotes()[0].getDecoratorString().endsWith("h")) {
				chordRaster[cq++] = chord; chordRaster[cq++] = chord;
				chordRaster[cq++] = chord; chordRaster[cq++] = chord;
			}
		}
		
		// 1) fill freely with random interval hull and rests (=null)
		int hullInterval = Random.rangeInt(3,5);
		int hullOffset = Random.rangeInt(0,2);
		for (int i=0; i<melodyRaster.length; i++)
			if (Random.nextBoolean(PROB_Rest))
				melodyRaster[i] = null;
			else
				melodyRaster[i] = Random.rangeInt(-hullInterval+hullOffset, hullInterval+hullOffset);
		
		// 3) smooth out melody (maybe curve model later)
		for (int i=0; i<melodyRaster.length; i++)
			// eventually kill off-notes
			if (Random.nextBoolean(PROB_QuarterBind) && i%2==1)
				melodyRaster[i] = null;
		
		// 4) copy beginning to somewhere else: sequencing/possibly transposed (e.g ABAC/AAB/ABCA)
		int copyLength = Random.rangeInt(0, melodyRaster.length/4+2);
		for (int i=0; i<copyLength && i<melodyRaster.length; i++) {
			melodyRaster[i+melodyRaster.length/4*1] = melodyRaster[i];
		}
		copyLength = Random.rangeInt(0, melodyRaster.length/4+2);
		for (int i=0; i<copyLength && i<melodyRaster.length; i++) {
			melodyRaster[i+melodyRaster.length/4*2] = melodyRaster[i];
		}
		
		// 5) smooth out ending (end with 0)
		if (getLength()>1) {
			int endNotePos = (getLength()-1)*8;
			melodyRaster[endNotePos] = 0;
			for (int i=endNotePos+1; i<melodyRaster.length; i++)
				melodyRaster[i] = null;
		}
		
		// 6) translate to staccato and make quarter/half notes
		// and transpose to 0=root of last chord
		int referenceTone = DIATONIC_MAP.get(chordRaster[(getLength()-1)*8]
				.getNotes()[Random.nextBoolean(0.75f) ? 0 : 2].getToneString().substring(0, 1));
		for (int i=0; i<melodyRaster.length; i++) {
			melodyStr += NEXT;
			if (melodyRaster[i] == null)
				melodyStr += REST+EIGTH;
			else {
				// TODO 7) kill avoid notes (b2/b9 to any chord note)
				melodyStr += getMelodyToneByRelation(
						referenceTone, 5, melodyRaster[i]);
				if (i % 2 == 0 // is downbeat
					&& i+3 < melodyRaster.length // space available
					&& melodyRaster[i+1] == null
					&& melodyRaster[i+2] == null
					&& melodyRaster[i+3] == null // rests are following
					&& Random.nextBoolean(PROB_HalfNote)) {
						melodyStr += HALF;
						i+=3;
				} else if (i % 2 == 0 // is downbeat
					&& i+1 < melodyRaster.length // space available
					&& melodyRaster[i+1] == null // rest is following
					&& Random.nextBoolean(PROB_QuarterNote)) {
						melodyStr += QUARTER;
						i++;
				} else {
					melodyStr += EIGTH;
				}
			}
		}
		
		return melodyStr;
	}
	
}