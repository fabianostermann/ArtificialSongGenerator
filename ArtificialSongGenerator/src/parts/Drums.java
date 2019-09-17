package parts;
import java.util.HashMap;
import java.util.Map;
import util.Random;

import org.jfugue.rhythm.Rhythm;


public class Drums {
	
	/** Probabilities for random choices in chord generation (memoized for one complete song) */
	private static float PROB_startWithCrash = 0.6f;
	private static float PROB_fillStartWithCrash = 0.5f;
	private static float PROB_fillIn = 0.6f;
	private static float PROB_halfBarFillIn = 0.5f;

//	public static final Map<Character, String> DEFAULT_RHYTHM_KIT = new HashMap<Character, String>() {{
//        put('.', "Ri");
//        put('O', "[BASS_DRUM]i");
//        put('o', "Rs [BASS_DRUM]s");
//        put('S', "[ACOUSTIC_SNARE]i");
//        put('s', "Rs [ACOUSTIC_SNARE]s");
//        put('^', "[PEDAL_HI_HAT]i");
//        put('`', "[PEDAL_HI_HAT]s Rs");
//        put('*', "[CRASH_CYMBAL_1]i");
//        put('+', "[CRASH_CYMBAL_1]s Rs");
//        put('X', "[HAND_CLAP]i");
//        put('x', "Rs [HAND_CLAP]s");
//        put(' ', "Ri");
//    }};
    
    
    /**
     * Generates a drum kit with randomly different midi drum sounds
     * @return The drum kit
     */
    public static Map<Character, String> newRandomRhythmKit() {
    	Map<Character, String> rk = new HashMap<Character, String>();
    	
    	// rests
    	rk.put('.', "Ri");
    	rk.put(' ', "Ri");

    	// bass drums variant
    	HashMap<String, Float> bdrums = new HashMap<>();
    	bdrums.put("BASS_DRUM",          0.8f);
    	bdrums.put("ACOUSTIC_BASS_DRUM", 0.2f);
    	String bdrumVar = Random.fromMap(bdrums);
    	
    	rk.put('O', "["+bdrumVar+"]i");
    	rk.put('o', "Rs ["+bdrumVar+"]s");
    	
    	// snare variant
    	HashMap<String, Float> snares = new HashMap<>();
    	snares.put("ACOUSTIC_SNARE", 0.5f);
    	snares.put("ELECTRIC_SNARE", 0.3f);
    	snares.put("SIDE_STICK",     0.1f);
    	snares.put("HAND_CLAP",      0.1f);
    	String snareVar = Random.fromMap(snares);
 
    	rk.put('S', "["+snareVar+"]i");
    	rk.put('s', "Rs ["+snareVar+"]s");
    	
    	// cymbal variant
    	HashMap<String, Float> cymbls = new HashMap<>();
    	cymbls.put("CLOSED_HI_HAT", 0.5f);
    	cymbls.put("PEDAL_HI_HAT",  0.2f);
    	cymbls.put("RIDE_CYMBAL_1", 0.15f);
    	cymbls.put("RIDE_CYMBAL_2", 0.15f);
    	String cymblVar = Random.fromMap(cymbls);
 
    	rk.put('^', "["+cymblVar+"]i");

    	// crash variant
    	HashMap<String, Float> crashs = new HashMap<>();
    	crashs.put("CRASH_CYMBAL_1", 0.5f);
    	crashs.put("CRASH_CYMBAL_2",  0.25f);
    	crashs.put("CHINESE_CYMBAL", 0.25f);
    	String crashVar = Random.fromMap(cymbls);
 
    	rk.put('C', "["+crashVar+"]i");
    	
    	// hi tom variant
    	HashMap<String, Float> hitoms = new HashMap<>();
    	hitoms.put("HI_TOM", 0.25f);
    	hitoms.put("HI_MID_TOM",  0.5f);
    	hitoms.put("LO_MID_TOM", 0.25f);
    	String hitomVar = Random.fromMap(hitoms);

    	rk.put('H', "["+hitomVar+"]i");
    	rk.put('h', "Rs ["+hitomVar+"]s");
    	
    	// find low tom pair:
    	// "HI_TOM" with "LO_TOM"
    	// "HI_MID_TOM" with "HIGH_FLOOR_TOM"
    	// "LO_MID_TOM" with "LO_FLOOR_TOM"
    	String lowtomVar = "LO_TOM";
    	if (hitomVar.equalsIgnoreCase("HI_MID_TOM"))
    		lowtomVar = "HIGH_FLOOR_TOM";
    	else if (hitomVar.equalsIgnoreCase("LO_MID_TOM"))
    		lowtomVar = "LO_FLOOR_TOM";
    	
    	rk.put('L', "["+lowtomVar+"]i");
    	rk.put('l', "Rs ["+lowtomVar+"]s");
    	
    	return rk;
    }
    
	/**
	 * Constructs a random rhythm played by drums in JFugue's Rhythm layers
	 * @return The random rhythm
	 */
	public static Rhythm newRandomRhythm(int length) {

		String bdrumAll = "";
		String snareAll = "";
		String cymblAll = "";

		//// random generation of a groove
		// bassdrum grooves
		String bdrumBar = "O"; // 0
		bdrumBar += Random.nextBoolean(0.3f) ? "O" : "."; // 1
		bdrumBar += Random.nextBoolean(0.1f) ? "O" : "."; // 2
		bdrumBar += Random.nextBoolean(0.4f) ? "O" : "."; // 3
		bdrumBar += Random.nextBoolean(0.5f) ? "O" : "."; // 4
		bdrumBar += Random.nextBoolean(0.3f) ? "O" : "."; // 5
		bdrumBar += Random.nextBoolean(0.1f) ? "O" : "."; // 6
		bdrumBar += Random.nextBoolean(0.4f) ? "O" : "."; // 7
		
		// snare grooves
    	HashMap<String, Float> snareGrooves = new HashMap<>();
    	snareGrooves.put("..S...S.", 0.7f); // standard snare on 2 and 4
    	snareGrooves.put("....S...", 0.15f); // halftime snare
    	snareGrooves.put("......S.", 0.05f); // 4 only snare
    	snareGrooves.put(".s....S.",  0.05f); // simple latin snare
    	snareGrooves.put("..SS..S.",  0.05f); // rock n roll snare
    	String snareBar = Random.fromMap(snareGrooves);

		// cymbal grooves
    	HashMap<String, Float> cymbalGrooves = new HashMap<>();
    	cymbalGrooves.put("^^^^^^^^", 0.7f); // eight snare
    	cymbalGrooves.put("^.^.^.^.",  0.25f); // quarter snare
    	cymbalGrooves.put(".^.^.^.^", 0.05f); // off-beat snare
    	String cymblBar = Random.fromMap(cymbalGrooves);
		
		// fill standard bars
		for (int i=0; i<length-1; i++) {
			bdrumAll += bdrumBar;
			snareAll += snareBar;
			cymblAll += cymblBar;
		}
		
		// fillIn design
		if (Random.nextBoolean(PROB_fillIn)) {
			// bass drum is kept
			bdrumAll += bdrumBar;
			// length can be full (8 eights) or half bar (4 eights)
			int fillLen = Random.nextBoolean(PROB_halfBarFillIn) ? 4 : 8;
			// if halfbar, snare and cymbl go on for 4 eights
			snareAll += snareBar.substring(0, 8-fillLen);
			cymblAll += cymblBar.substring(0, 8-fillLen);
			for (int i=fillLen; i>0; i--) { // 8 or 4 till 1
				// make fill eight or sixteens on toms or snare
				HashMap<String, Float> fillInst = new HashMap<>();
				fillInst.put(".", 3f*(i)); // 8 to 1
				fillInst.put("S", 3f*(i)); // 8 to 1 (*3)
				fillInst.put("s", 1f*(i));
				fillInst.put("H", 3f*((-Math.abs(i-4)+4)*2)); // 0 to 8 to 2 (*3)
				fillInst.put("h", 1f*((-Math.abs(i-4)+4)*2));
				fillInst.put("L", 3f*(9-i)); // 1 to 8 (*3)
				fillInst.put("l", 1f*(9-i));
				snareAll += Random.fromMap(fillInst);
				// cymbal gets rest
				if (i==8 && Random.nextBoolean(PROB_fillStartWithCrash))
					cymblAll += "C";
				else cymblAll += ".";
			}
		} else {
			// no fillIn
			bdrumAll += bdrumBar;
			snareAll += snareBar;
			cymblAll += cymblBar;
		}
			
		// randomly generate a cymbal at the beginning of the part
		if (Random.nextBoolean(PROB_startWithCrash))
			cymblAll = "C"+cymblAll.substring(1);
		
		
		return new Rhythm(newRandomRhythmKit())
        .addLayer(bdrumAll)
        .addLayer(snareAll)
        .addLayer(cymblAll);
	}

}
