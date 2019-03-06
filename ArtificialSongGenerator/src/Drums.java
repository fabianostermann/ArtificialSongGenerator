import java.util.HashMap;
import java.util.Map;

import org.jfugue.rhythm.Rhythm;


public class Drums {
	
	/** Probabilities for random choices in chord generation (memoized for one complete song) */
	private static float PROB_startWithCrash = 0.5f;

	public static final Map<Character, String> DEFAULT_RHYTHM_KIT = new HashMap<Character, String>() {{
        put('.', "Ri");
        put('O', "[BASS_DRUM]i");
        put('o', "Rs [BASS_DRUM]s");
        put('S', "[ACOUSTIC_SNARE]i");
        put('s', "Rs [ACOUSTIC_SNARE]s");
        put('^', "[PEDAL_HI_HAT]i");
        put('`', "[PEDAL_HI_HAT]s Rs");
        put('*', "[CRASH_CYMBAL_1]i");
        put('+', "[CRASH_CYMBAL_1]s Rs");
        put('X', "[HAND_CLAP]i");
        put('x', "Rs [HAND_CLAP]s");
        put(' ', "Ri");
    }};
    
    
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

		// TODO random generation of a groove
		String bdrumBar = "O...O...";
		String snareBar = "..S...S.";
		String cymblBar = "^^^^^^^^";
		
		// fill standard bars
		for (int i=0; i<length-1; i++) {
			bdrumAll += bdrumBar;
			snareAll += snareBar;
			cymblAll += cymblBar;
		}
		
		// TODO design fill ins (used standard bars for now)
			bdrumAll += bdrumBar;
			snareAll += snareBar;
			cymblAll += cymblBar;
			
		// randomly generate a cymbal at the beginning of a part
		if (Random.nextBoolean(PROB_startWithCrash))
			cymblAll = "C"+cymblAll.substring(1);
		
		
		return new Rhythm(newRandomRhythmKit())
        .addLayer(bdrumAll)
        .addLayer(snareAll)
        .addLayer(cymblAll);
	}

}
