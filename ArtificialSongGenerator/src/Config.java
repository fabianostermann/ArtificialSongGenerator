import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import org.jfugue.theory.Key;


public class Config {

	public static Config GET;
	
	// Load every parameter as strings from a map, that is filled from file in the first place.
	// If a key is not added by file, a default string is used from then on.
	
	private static Map<String, String[]> configMap = new HashMap<String, String[]>();
	public static String CONFIG_FILENAME = "thesong.conf";
	public static final String DUMMY_SUFFIX = ".dummy";
	
	public static final String SET = "=";
	public static final String DELIM = ",";
	public static final String COMMENT = "#";
	
	/**
	 * Parses on line from the config file and stores it in the config map.
	 * Settings syntax is: 'key = value' or 'key = value1, value2, value3, ...'
	 * Char '#' can be used for comments
	 * @param line The string to parse
	 * @throws Exception If something cannot be parsed correctly
	 */
	public static void parseConfig(String line) {
		line = line.substring(0, line.indexOf(COMMENT)).trim();
		if (line.isEmpty())
			return;
		String[] lineSet = line.trim().split(SET, 2);
		// TODO make throw without catch
//		if (lineSet.length < 2)
//			throw new Exception("Config string must include the SET character "+SET);
		String key = lineSet[0].trim();
		String[] values = lineSet[1].trim().split(DELIM, 0);
		for (int i=0; i<values.length; i++)
			values[i] = values[i].trim();
		configMap.put(key, values);
	}
	
	public static void loadFromFile() {
		// TODO loading config from file
		System.out.println("loading config from file not implemented yet");
	}
	
	/**
	 * Reads the desired config string from the config map.
	 * @param key identificator
	 * @param defaultValue desired fallback value (added to map)
	 * @return The value that is found in or put into the map
	 */
	public static String getConfigString(String key, String defaultValue) {
		if (!configMap.containsKey(key))
			configMap.put(key, new String[]{defaultValue});
		return configMap.get(key)[0];
	}

	/**
	 * Reads the desired config string array from the config map.
	 * @param key identificator
	 * @param defaultArray desired fallback value array (added to map)
	 * @return The value array that is found in or put into the map
	 */
	public static String[] getConfigStrings(String key, String[] defaultArray) {
		if (!configMap.containsKey(key))
			configMap.put(key, defaultArray);
		return configMap.get(key);
	}

	/**
	 * Reads the desired config integer from the config map.
	 * @param key identificator
	 * @param defaultValue desired fallback value (added to map)
	 * @return The value that is found in or put into the map
	 */
	public static int getConfigInt(String key, int defaultValue) {
		if (!configMap.containsKey(key)) {
			configMap.put(key, new String[]{Integer.toString(defaultValue)});
			return defaultValue;
		}
		return Integer.parseInt(
				configMap.get(key)[0]);
	}

	/**
	 * Reads the desired config integer array from the config map.
	 * @param key identificator
	 * @param defaultArray desired fallback value array (added to map)
	 * @return The value array that is found in or put into the map
	 */
	public static int[] getConfigInts(String key, int[] defaultArray) {
		if (!configMap.containsKey(key)) {
			String[] defaultStrings = new String[defaultArray.length];
			for (int i=0; i<defaultStrings.length; i++)
				defaultStrings[i] = Integer.toString(defaultArray[i]);
			configMap.put(key, defaultStrings);
			return defaultArray;
		}
		String[] stringArray = configMap.get(key);
		int[] returnArray = new int[stringArray.length];
		for (int i=0; i<returnArray.length; i++)
			returnArray[i] = Integer.parseInt(stringArray[i]);
		return returnArray;
	}
	
	/**
	 * Reads the desired config boolean from the config map.
	 * @param key identificator
	 * @param defaultValue desired fallback value (added to map)
	 * @return The value that is found in or put into the map
	 */
	public static boolean getConfigBool(String key, boolean defaultValue) {
		if (!configMap.containsKey(key)) {
			configMap.put(key, new String[]{Boolean.toString(defaultValue)});
			return defaultValue;
		}
		return Boolean.parseBoolean(
				configMap.get(key)[0]);
	}
	
	// TODO implement float loader if needed
	
	public static void createDummyFile() throws IOException {
		File file = new File(CONFIG_FILENAME+DUMMY_SUFFIX);
		BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, // create if not exists
				StandardOpenOption.TRUNCATE_EXISTING, // clear to zero length
				StandardOpenOption.WRITE); // grant write access
		
		// TODO implement creating config dummy-file
		System.out.println("creating config dummy-file not fully implemented yet");

		// print config info header
		writer.write(COMMENT+" This is a dummy config file. "+COMMENT); writer.newLine();
		writer.newLine();
		writer.write(COMMENT+" Remove the suffix '"+DUMMY_SUFFIX+"' to make it be used "); writer.newLine();
		writer.write(COMMENT+"   or specify a name you like by using '--config=<configfile>'"); writer.newLine();
		writer.newLine();
		writer.write(COMMENT+" Remove the comment char '"+COMMENT+"' from the lines you like to enable."); writer.newLine();
		writer.write(COMMENT+" Settings syntax is: 'key = value' or 'key = value1, value2, value3, ...'"); writer.newLine();
		writer.newLine();
		
				
		writer.close();
	}
	
	public static void loadDefaults() {
		Config.GET = new Config();
	}
	
	// ########## DEFAULT SETTINGS (non-static!) ###############
	// every setting that is not loaded from file until now will be overwritten here

	public String THESONG_TITLE = getConfigString("title", "thesong");
	public final String MIDI_SUFFIX = getConfigString("midi-suffix", ".midi");
	public final String ARFF_SUFFIX = getConfigString("arff-suffix", ".arff");
	public String OUTPUT_DIR = getConfigString("directory", "");
	
	public final int Nof_DIFFERENT_SONGPARTS = getConfigInt("number-of-different-songparts", 3);
	public final int Nof_SONGPARTS_IN_SONG = getConfigInt("number-of-songparts-in-song", 8);
	
	// make random choice on major keys (memoizable)
	public final String[] KEYS = getConfigStrings("keys", new String[]
			{ "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" });
	public final boolean MEM_KEY = getConfigBool("memoize-keys", false);
	private int keyPos = -1;
	public Key randomKey() {
		if (keyPos == -1 || !MEM_KEY)
			keyPos = Random.rangeInt(0, KEYS.length);
		return new Key(KEYS[keyPos]+"maj");
	}

	// make random choice on tempo (memoizable)
	public final int[] TEMPO_RANGE = getConfigInts("tempo-range", new int[] { 60, 180 });
	public final boolean MEM_TEMPO = getConfigBool("memoize-tempo", true);
	private int tempo = -1;
	public int randomTempo() {
		if (tempo == -1 || !MEM_TEMPO)
			tempo = Random.rangeInt(TEMPO_RANGE[0], TEMPO_RANGE[TEMPO_RANGE.length-1]);
		return tempo;
	}
	
	// make random choice on songparts length
	public final int[] SONGPARTS_LENGTH = getConfigInts("allowed-songpart-lengths", new int[] { 4, 6, 8 });
	public int randomSongpartLength() {
		return SONGPARTS_LENGTH[Random.rangeInt(0, SONGPARTS_LENGTH.length)];
	}
	
	// INFO: For all possible instruments see org.jfugue.midi:MidiDictionary.java
		
	// make random choice on melody instrument
	public final String[] MELODY_INSTRUMENTS = getConfigStrings("melody-instruments", new String[] {
		"Vibraphone", "Distortion_Guitar", "Violin", "Trumpet", "Tenor_Sax",
		"Flute", "Synth_Voice"
	});
	public String randomMelodyInstrument() {
		return MELODY_INSTRUMENTS[Random.rangeInt(0, MELODY_INSTRUMENTS.length)];
	}
	
	// make random choice on chord instrument
	public final String[] CHORD_INSTRUMENTS = getConfigStrings("chord-instruments", new String[] {
		"Piano", "Electric_Piano", "Vibraphone", "Rock_Organ", "Guitar",
		"Electric_Jazz_Guitar", "Overdriven_Guitar", "String_Ensemble_1",
		"Poly_Synth"
	});
	public String randomChordInstrument() {
		return CHORD_INSTRUMENTS[Random.rangeInt(0, CHORD_INSTRUMENTS.length)];
	}
	
	
}
