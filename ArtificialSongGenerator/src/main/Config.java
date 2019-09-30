package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfugue.theory.Key;

import util.Random;


public class Config {

	/**
	 * Instance of Config parameters. 
	 * Used to load default settings after loading config file.
	 */
	public static Config GET;
	
	/**
	 * Load every parameter as strings from this map.
	 * The map is filled from file in the first place.
	 * If a key is not read from file, a default string is put in using the getConfig*() functions.
	 */
	private static Map<String, String[]> configMap = new HashMap<String, String[]>();
	public static String CONFIG_FILENAME = "thesong.conf";
	public static final String DUMMY_SUFFIX = ".dummy";
	
	/**
	 * Some char constants used when interpreting the config file
	 */
	public static final String ASSIGN = "=";
	public static final String DELIM = ",";
	public static final String COMMENT = "#";
	
	/**
	 * Parses on line from the config file and stores it in the config map.
	 * Settings syntax is: 'key = value' or 'key = value1, value2, value3, ...'
	 * Char '#' can be used for comments
	 * @param line The string to parse
	 * @throws Exception If something cannot be parsed correctly
	 */
	public static void parseConfigAndStore(String line) {
		if (line.contains(COMMENT))
			line = line.substring(0, line.indexOf(COMMENT));
		line = line.trim();
		if (line.isEmpty())
			return;
		String[] lineSet = line.trim().split(ASSIGN, 2);
		if (lineSet.length < 2)
			throw new RuntimeException("Config line must include the ASSIGN character "+ASSIGN);
		String key = lineSet[0].trim();
		String[] values = lineSet[1].trim().split(DELIM, 0);
		for (int i=0; i<values.length; i++)
			values[i] = values[i].trim();
		configMap.put(key, values);
//		System.out.println("Just put '"+values[0]+"' and maybe more to '"+key+"'");
	}
	
	/**
	 * Loads the config file and stores all parameters in config map.
	 * @throws IOException If something goes wrong while reading
	 */
	public static void loadFromFile() throws IOException {
		
		File file = new File(CONFIG_FILENAME);
		if (!file.exists())
			return; // test existence and aborts
		
		BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8); // read only
		
		String line = "";
		while ((line = reader.readLine()) != null) {
			parseConfigAndStore(line);
		}
		reader.close();
		System.out.println("Config file '"+CONFIG_FILENAME+"' loaded.");
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
	
	/**
	 * Reads the desired config float from the config map.
	 * @param key identificator
	 * @param defaultValue desired fallback value (added to map)
	 * @return The value that is found in or put into the map
	 */
	public static float getConfigFloat(String key, float defaultValue) {
		if (!configMap.containsKey(key)) {
			configMap.put(key, new String[]{Float.toString(defaultValue)});
			return defaultValue;
		}
		return Float.parseFloat(
				configMap.get(key)[0]);
	}
	
	/**
	 * Creates a dummy config file, that can be used as template.
	 * @throws IOException If something goes wrong writing.
	 */
	public static void createDummyFile() throws IOException {
		File file = new File(CONFIG_FILENAME+DUMMY_SUFFIX);
		BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, // create if not exists
				StandardOpenOption.TRUNCATE_EXISTING, // clear to zero length
				StandardOpenOption.WRITE); // grant write access

		// print config info header
		writer.write(COMMENT+" This is a dummy config file. "+COMMENT); writer.newLine();
		writer.newLine();
		writer.write(COMMENT+" Remove the suffix '"+DUMMY_SUFFIX+"' to make it be used "); writer.newLine();
		writer.write(COMMENT+"   or specify a name you like by using '--config=<configfile>'"); writer.newLine();
		writer.newLine();
		writer.write(COMMENT+" Remove the comment char '"+COMMENT+"' from the lines you like to enable."); writer.newLine();
		writer.write(COMMENT+" Settings syntax is: 'key = value' or 'key = value1, value2, value3, ...'"); writer.newLine();
		writer.newLine();
		
		// print config settings (encommented)
		for (String key : configMap.keySet()) {
			writer.write(COMMENT+key+" "+ASSIGN+" ");
			String[] values = configMap.get(key);
			for (int i=0; i<values.length-1; i++)
				writer.write(values[i]+DELIM+" ");
			writer.write(values[values.length-1]);
			writer.newLine();
		}
		
		writer.close();
	}
	
	/**
	 * Sets all config parameters, that are not set by config file, on default values.
	 * Every parameter setting, that is not loaded from file until this call, will be overwritten.
	 */
	public static void loadDefaults() {
		Config.GET = new Config();
	}
	
	// ########## DEFAULT SETTINGS (non-static) ###############

	public String THESONG_TITLE = getConfigString("title", "thesong");
	public final String MIDI_SUFFIX = getConfigString("midi-suffix", ".mid");
	public final String ARFF_SUFFIX = getConfigString("arff-suffix", ".arff");
	public String OUTPUT_DIR = getConfigString("directory", ".");
	
	public final int Nof_DIFFERENT_SONGPARTS = getConfigInt("number-of-different-songparts", 3);
	public final int Nof_SONGPARTS_IN_SONG = getConfigInt("number-of-songparts-in-song", 8);
	
	// make random choice on major keys (memoizable)
	public final String[] KEYS = getConfigStrings("keys", new String[]
			{ "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" });
	/** If true, key is constant for full song after randomly drawn once. */
	public final boolean MEMOIZE_KEY = getConfigBool("memoize-keys", false);
	private int keyPos = -1;
	public Key randomKey() {
		if (keyPos == -1 || !MEMOIZE_KEY)
			keyPos = Random.rangeInt(0, KEYS.length);
		return new Key(KEYS[keyPos]+"maj");
	}

	// make random choice on tempo (memoizable)
	public final int[] TEMPO_RANGE = getConfigInts("tempo-range", new int[] { 60, 180 });
	/** If true, tempo is constant for full song after randomly drawn once. */
	public final boolean MEMOIZE_TEMPO = getConfigBool("memoize-tempo", true);
	private int tempo = -1;
	public int randomTempo() {
		if (tempo == -1 || !MEMOIZE_TEMPO)
			tempo = Random.rangeInt(TEMPO_RANGE[0], TEMPO_RANGE[TEMPO_RANGE.length-1]);
		return tempo;
	}
	
	// make random choice on songparts length
	public final int[] SONGPARTS_LENGTH = getConfigInts("allowed-songpart-lengths", new int[] { 4, 6, 8 });
	public int randomSongpartLength() {
		return SONGPARTS_LENGTH[Random.rangeInt(0, SONGPARTS_LENGTH.length)];
	}
	
	// INFO: For all possible instruments see org.jfugue.midi:MidiDictionary.java
	
	/** If true, no instrument is occuring twice before all instruments were drawn once. */
	public final boolean EXPLOIT_INSTRUMENTS = getConfigBool("exploit-instruments", true);	
	/** If true, the instruments for the different functions are memoized. */
	public final boolean MEMOIZE_INSTRUMENTS = getConfigBool("memoize-instruments", true);
	public final float MEMOIZE_INSTRUMENTS_FUZZINESS = getConfigFloat("memoize-instruments-fuzziness", 0.33f);
	
	// make random choice on melody instrument
	public final String[] MELODY_INSTRUMENTS = getConfigStrings("melody-instruments", new String[] {
		"Trumpet", "Tenor_Sax", "Flute", "Violin", "Skakuhachi"
		//No NativeInstrument available: "Vibraphone", "Distortion_Guitar", "Synth_Voice"
	});
	public int getMelodyChannel(String melodyInstrument) {
		for (int i=0; i<MELODY_INSTRUMENTS.length; i++)
			if (MELODY_INSTRUMENTS[i].equals(melodyInstrument))
				return (i<9) ? i : i+1;
		return -1;
	}
	private int melodyPos = -1;
	private final List<String> melodyInstList = new ArrayList<String>();
	public String randomMelodyInstrument() {
		if (MEMOIZE_INSTRUMENTS && !Random.nextBoolean(MEMOIZE_INSTRUMENTS_FUZZINESS)) {
			if (melodyPos == -1)
				melodyPos = Random.rangeInt(0, MELODY_INSTRUMENTS.length);
			return MELODY_INSTRUMENTS[melodyPos];
		}
		if (!EXPLOIT_INSTRUMENTS)
			return MELODY_INSTRUMENTS[Random.rangeInt(0, MELODY_INSTRUMENTS.length)];
		if (melodyInstList.isEmpty())
			melodyInstList.addAll(Arrays.asList(MELODY_INSTRUMENTS));
		return melodyInstList.remove(Random.rangeInt(0, melodyInstList.size()));
	}
	
	// make random choice on chord instrument
	public final String[] CHORD_INSTRUMENTS = getConfigStrings("chord-instruments", new String[] {
		"Piano", "Electric_Piano", "Rock_Organ", "String_Ensemble_1", "Sitar"
		//No NativeInstrument available: "Poly_Synth", "Electric_Jazz_Guitar", "Overdriven_Guitar", "Guitar", "Vibraphone",
	});
	public int getChordsChannel(String chordInstrument) {
		for (int i=0; i<CHORD_INSTRUMENTS.length; i++)
			if (CHORD_INSTRUMENTS[i].equals(chordInstrument))
				return ((i+MELODY_INSTRUMENTS.length<9) ? i : i+1)+MELODY_INSTRUMENTS.length;
		return -1;
	}
	private int chordPos = -1;
	private final List<String> chordInstList = new ArrayList<String>();
	public String randomChordInstrument() {
		if (MEMOIZE_INSTRUMENTS && !Random.nextBoolean(MEMOIZE_INSTRUMENTS_FUZZINESS)) {
			if (chordPos == -1)
				chordPos = Random.rangeInt(0, CHORD_INSTRUMENTS.length);
			if (chordPos == arpeggioPos)
				chordPos = (arpeggioPos + Random.rangeInt(0, CHORD_INSTRUMENTS.length-1)) % CHORD_INSTRUMENTS.length;
			return CHORD_INSTRUMENTS[chordPos];
		}
		if (!EXPLOIT_INSTRUMENTS) {
			chordPos = Random.rangeInt(0, CHORD_INSTRUMENTS.length);
		}
		if (chordInstList.isEmpty())
			chordInstList.addAll(Arrays.asList(CHORD_INSTRUMENTS));
		return chordInstList.remove(Random.rangeInt(0, chordInstList.size()));
	}
	
	// make random choice on arpeggio instrument (depended to chord instrument)
	private int arpeggioPos = -1;
	public String randomArpeggioInstrument() {
		if (MEMOIZE_INSTRUMENTS && !Random.nextBoolean(MEMOIZE_INSTRUMENTS_FUZZINESS)) {
			if (arpeggioPos == -1)
				arpeggioPos = Random.rangeInt(0, CHORD_INSTRUMENTS.length);
			if (arpeggioPos == chordPos)
				arpeggioPos = (chordPos + Random.rangeInt(0, CHORD_INSTRUMENTS.length-1)) % CHORD_INSTRUMENTS.length;
			return CHORD_INSTRUMENTS[arpeggioPos];
		}
		if (!EXPLOIT_INSTRUMENTS)
			return CHORD_INSTRUMENTS[Random.rangeInt(0, CHORD_INSTRUMENTS.length)];
		if (chordInstList.isEmpty())
			chordInstList.addAll(Arrays.asList(CHORD_INSTRUMENTS));
		return chordInstList.remove(Random.rangeInt(0, chordInstList.size()));
	}
	
	// make random choice on bass instrument
	public final String[] BASS_INSTRUMENTS = getConfigStrings("bass-instruments", new String[] {
		"Acoustic_Bass", "Electric_Bass_Finger", "Slap_Bass_1", "Contrabass" //"Synth_Bass_2"
	});
	public int getBassChannel(String bassInstrument) {
		for (int i=0; i<BASS_INSTRUMENTS.length; i++)
			if (BASS_INSTRUMENTS[i].equals(bassInstrument))
				return ((i+MELODY_INSTRUMENTS.length+CHORD_INSTRUMENTS.length<9) ? i : i+1)+MELODY_INSTRUMENTS.length+CHORD_INSTRUMENTS.length;
		return -1;
	}
	private int bassPos = -1;
	private final List<String> bassInstList = new ArrayList<String>();
	public String randomBassInstrument() {
		if (MEMOIZE_INSTRUMENTS) { // && !Random.nextBoolean(MEMOIZE_INSTRUMENTS_FUZZINESS)) {
			if (bassPos == -1)
				bassPos = Random.rangeInt(0, BASS_INSTRUMENTS.length);
			return BASS_INSTRUMENTS[bassPos];
		}
		if (!EXPLOIT_INSTRUMENTS)
			return BASS_INSTRUMENTS[Random.rangeInt(0, BASS_INSTRUMENTS.length)];
		if (bassInstList.isEmpty())
			bassInstList.addAll(Arrays.asList(BASS_INSTRUMENTS));
		return bassInstList.remove(Random.rangeInt(0, bassInstList.size()));
	}

	public final float MELODY_ENABLED = getConfigFloat("melody-enabled", 1.0f);
	public final float CHORDS_ENABLED = getConfigFloat("chords-enabled", 0.8f);
	public final float DRUMS_ENABLED = getConfigFloat("drums-enabled", 0.8f);
	public final float ARPEGGIO_ENABLED = getConfigFloat("arpeggio-enabled", 0.5f);
	public final float BASS_ENABLED = getConfigFloat("bass-enabled", 0.9f);
}
