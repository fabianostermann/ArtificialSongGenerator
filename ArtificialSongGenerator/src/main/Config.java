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
import org.jfugue.theory.Note;

import parts.Instrument;
import util.Random;


public class Config {

	/**
	 * Instance of Config parameters. 
	 * Used to load default settings after loading config file.
	 */
	public static Config GET; // TODO split static methods to own class and inherit
	
	/**
	 * Load every parameter as strings from this map.
	 * The map is filled from file in the first place.
	 * If a key is not read from file, a default string is put in using the getConfig*() functions.
	 */
	private static Map<String, String[]> configMap = new HashMap<String, String[]>();
	public static String CONFIG_FILENAME = null;
	public static final String DUMMY_FILENAME = "dummy.conf";
	public static final String DEMO_SUFFIX = "Demo";
	public static final String DRUMS_SUFFIX = "Drums";
	public static final String SEGMENTS_SUFFIX = "segments";
	public static final String ONSETS_SUFFIX = "onsets";
	
	/**
	 * Some char constants for interpreting the config file
	 */
	public static final String ASSIGN = "=";
	public static final String DELIM = ",";
	public static final String ATTR_OPEN = "["; // used for regex string split
	public static final String ATTR_CLOSE = "]";
	public static final String COMMENT = "#";
	/**
	 * Some char constants for output files
	 */
	public static final String FILE_DELIM = "_";
	
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
		
		if (CONFIG_FILENAME == null)
			return;
		
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
		File file = new File(DUMMY_FILENAME);
		BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, // create if not exists
				StandardOpenOption.TRUNCATE_EXISTING, // clear to zero length
				StandardOpenOption.WRITE); // grant write access

		// print config info header
		writer.write(COMMENT+" This is a dummy config file. "+COMMENT); writer.newLine();
		writer.newLine();
		writer.write(COMMENT+" Specify a name you like and use '--config=<configfile>'"); writer.newLine();
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
	
	/**
	 * Parses a config string into a instrument object.
	 * @param instrStr the string to parse
	 * @return the instrument with desired attributes
	 */
	public static Instrument parseInstrument(String instrStr) {
		Instrument instrument = null;
		instrStr = instrStr.replaceAll("\\s","");
		String[] strParts = instrStr.split("\\"+ATTR_OPEN);
		try {
			instrument = Instrument.findOrCreate(strParts[0]);
			if (strParts.length > 1 && strParts[1].endsWith("]")) {
				strParts[1] = strParts[1].substring(0, strParts[1].length()-1);
				String[] attrParts = strParts[1].split(DELIM);
				int attrCount = 0;
				if (attrParts.length > attrCount)
					instrument.setLowestNote(new Note(new Integer(attrParts[attrCount++])));
				if (attrParts.length > attrCount)
					instrument.setHighestNote(new Note(new Integer(attrParts[attrCount++])));
				if (attrParts.length > attrCount)
					instrument.setSampler(attrParts[attrCount++]);
				if (attrParts.length > attrCount)
					instrument.setMidiString(attrParts[attrCount++]);
			}
		} catch (Exception e) {
			System.out.println("Error while parsing instrument string '"+instrStr+"': "+e.getClass()+" "+e.getMessage());
			System.exit(1);
		}
		return instrument;
	}
	
	/**
	 * Parses a config string into a instrument object.
	 * @param instrStr the string to parse
	 * @return the instrument with desired attributes
	 */
	public static String parseInstrumentName(String instrStr) {
		instrStr = instrStr.replaceAll("\\s","");
		String[] strParts = instrStr.split("\\"+ATTR_OPEN);
		return strParts[0];
	}
	
	// ########## DEFAULT SETTINGS (non-static) ###############

	public String THESONG_TITLE = getConfigString("title", "thesong");
	public final String MIDI_SUFFIX = getConfigString("midi-suffix", ".mid");
	public final String ARFF_SUFFIX = getConfigString("arff-suffix", ".arff");
	public String OUTPUT_DIR = getConfigString("directory", ".");
	
	public final int Nof_DIFFERENT_SONGPARTS = getConfigInt("number-of-different-songparts", 3);
	public final float MIN_LENGTH_IN_SEC = getConfigInt("minimum-song-length-in-seconds", 60*2+30); // guaranteed
	public final float MAX_LENGTH_IN_SEC = getConfigInt("maximum-song-length-in-seconds", 60*3); // may be slightly more

	public final String[] KEYS = getConfigStrings("keys", new String[]
			{ "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" });
	public final String[] TONALITIES = getConfigStrings("tonalities", new String[]
			{  "maj", "min" });
	/** If true, key is the same for full song after randomly drawn once.
	 * Else, the key is altered from original key with different possibilities for modulation step sizes */
	public final boolean MEMOIZE_KEY = getConfigBool("memoize-keys", false);
	private int keyPos = -1;
	private int tonalityPos = -1;
	/** make random choice on major keys (memoizable) */
	public Key randomKey() {
		if (keyPos == -1) {
			keyPos = Random.rangeInt(0, KEYS.length);
			tonalityPos = Random.rangeInt(0, TONALITIES.length);
			return new Key(KEYS[keyPos]+TONALITIES[tonalityPos]);
		}
		if (MEMOIZE_KEY) {
			return new Key(KEYS[keyPos]+TONALITIES[tonalityPos]);
		}
		HashMap<Integer, Float> keyMod = new HashMap<>();
			keyMod.put(0, 0.6f); // no modulation
			keyMod.put(5, 0.1f); keyMod.put(7, 0.05f); // fourth and fifth
			keyMod.put(4, 0.03f); keyMod.put(8, 0.02f); // major third up and down = minor sixth
			keyMod.put(3, 0.03f); keyMod.put(9, 0.02f); // minor third up and down = major sixth
			keyMod.put(2, 0.1f); keyMod.put(1, 0.05f); // whole tone up / half tone up
    	int mod = Random.fromMap(keyMod);
    	tonalityPos = Random.rangeInt(0, TONALITIES.length);
		return new Key(KEYS[(keyPos+mod)%KEYS.length]+TONALITIES[tonalityPos]);
	}

	public final int[] TEMPO_RANGE = getConfigInts("tempo-range", new int[] { 60, 180 });
	/** If true, tempo is constant for full song after randomly drawn once. */
	public final boolean MEMOIZE_TEMPO = getConfigBool("memoize-tempo", false); //TODO now need onset generator to be improved in tempo recognition
	private int tempo = -1;
	/** make random choice on tempo (memoizable) */
	public int randomTempo() {
		if (tempo == -1) {
			tempo = Random.rangeInt(TEMPO_RANGE[0], TEMPO_RANGE[TEMPO_RANGE.length-1]);
			return tempo;
		}
		if (MEMOIZE_TEMPO) {
			return tempo;
		}
		if (Random.nextBoolean(0.1f)) {
			tempo = Random.rangeInt(TEMPO_RANGE[0], TEMPO_RANGE[TEMPO_RANGE.length-1]);
			return tempo;
		}
		HashMap<Float, Float> tempoMod = new HashMap<>();
			tempoMod.put(1f, 0.5f); // keep tempo
			tempoMod.put(0.5f, 0.15f); tempoMod.put(2f, 0.15f); // half / double time
			tempoMod.put(2f/3f, 0.05f); tempoMod.put(4f/3f, 0.05f); // 2/3 / 4/3 time
			tempoMod.put(2f/3f, 0.05f); tempoMod.put(4f/3f, 0.05f); // 2/3 / 4/3 time
		float mod = -1; int tempoReq = -1; int breakCount = 10;
		// ensure a tempo request suitable to desired tempo range
		while (!(tempoReq>=TEMPO_RANGE[0] && tempoReq<=TEMPO_RANGE[TEMPO_RANGE.length-1])) {
			mod = --breakCount > 0 ? Random.fromMap(tempoMod) : 1f; // TODO make deterministic
			tempoReq = (int) ((float)tempo*mod);
		}
		return tempoReq;
	}
	
	public final int[] SONGPARTS_LENGTH = getConfigInts("allowed-songpart-lengths", new int[] { 4, 6, 8, 12, 16 });
	/** make random choice on songparts length */
	public int randomSongpartLength() {
		return SONGPARTS_LENGTH[Random.rangeInt(0, SONGPARTS_LENGTH.length)];
	}
	
	// INFO: For all possible instruments see org.jfugue.midi:MidiDictionary.java
	
	/** If true, no instrument is occuring twice before all instruments were drawn once. */
	public final boolean EXPLOIT_INSTRUMENTS = getConfigBool("exploit-instruments", true);	
	/** If true, the instruments for the different functions are memoized. */
	public final boolean MEMOIZE_INSTRUMENTS = getConfigBool("memoize-instruments", true);
	public final float MEMOIZE_INSTRUMENTS_FUZZINESS = getConfigFloat("memoize-instruments-fuzziness", 0.33f);
	
	public final String[] MELODY_INSTRUMENTS = getConfigStrings("melody-instruments", new String[] {
		"Violin [55,96,Violins_SessionStringsPro]",
		"Viola [48,84,Violas_SessionStringsPro]",
		"Erhu [57,84,EthnoWorld_Erhu,Fiddle]",

		"Trumpet [52,89,Trumpet1_SessionHornsPro]",
		"Flugelhorn [52,83,Flugelhorn_SessionHornsPro,Trombone]",
		"Trombone [40,72,TenorTrombone_SessionHornsPro]",

		"Clarinet [50,87,Clarinet_Essential]",
		"AltoSax [49,81,AltoSax_SessionHornsPro,Alto_Sax]",
		"TenorSax [44,76,TenorSax_SessionHornsPro,Tenor_Sax]",

		"Flute [60,96,Flute_Essential]",
		"Shakuhachi [57,88,EthnoWorld_Shakuhachi,Skakuhachi]",
		"Fujara [36,96,EthnoWorld_Fujara,Blown_Bottle]",
	});
	/** make random choice on melody instrument */
	private int melodyPos = -1;
	private final List<String> melodyInstList = new ArrayList<String>();
	public Instrument randomMelodyInstrument() {
		if (MEMOIZE_INSTRUMENTS && !Random.nextBoolean(MEMOIZE_INSTRUMENTS_FUZZINESS)) {
			if (melodyPos == -1)
				melodyPos = Random.rangeInt(0, MELODY_INSTRUMENTS.length);
			return parseInstrument(MELODY_INSTRUMENTS[melodyPos]);
		}
		if (!EXPLOIT_INSTRUMENTS)
			return parseInstrument(MELODY_INSTRUMENTS[Random.rangeInt(0, MELODY_INSTRUMENTS.length)]);
		if (melodyInstList.isEmpty())
			melodyInstList.addAll(Arrays.asList(MELODY_INSTRUMENTS));
		return parseInstrument(melodyInstList.remove(Random.rangeInt(0, melodyInstList.size())));
	}
	
	public final String[] CHORD_INSTRUMENTS = getConfigStrings("chord-instruments", new String[] {
		"Cello [36,72,Cellos_SessionStringsPro]",

		"Piano [21,108,The_Giant_soft]",
		"BrightPiano [21,108,Alicias_Keys,Bright_Acoustic]",
		"ElectricPiano [24,96,Scarbee_A200_DI,Electric_Piano]",

		"AcousticGuitar [40,79,ChrisHein????AccGuitar,Guitar]",
		"ElectricGuitarClean [40,79,ChrisHein????CleanGuitar,Electric_Clean_Guitar]",
		"ElectricGuitarDistort [40,79,ChrisHein????CleanGuitar+VSTAmp,Overdriven_Guitar]",
		"Ukulele [57,83,EthnoWorld_Ukulele,Guitar]",
		"Sitar [48,79,EthnoWorld_Sitar]",
	});
	/** make random choice on chord instrument */
	private int chordPos = -1;
	private final List<String> chordInstList = new ArrayList<String>();
	public Instrument randomChordInstrument() {
		if (MEMOIZE_INSTRUMENTS && !Random.nextBoolean(MEMOIZE_INSTRUMENTS_FUZZINESS)) {
			if (chordPos == -1)
				chordPos = Random.rangeInt(0, CHORD_INSTRUMENTS.length);
			if (chordPos == arpeggioPos)
				chordPos = (arpeggioPos + Random.rangeInt(0, CHORD_INSTRUMENTS.length-1)) % CHORD_INSTRUMENTS.length;
			return parseInstrument(CHORD_INSTRUMENTS[chordPos]);
		}
		if (!EXPLOIT_INSTRUMENTS) {
			chordPos = Random.rangeInt(0, CHORD_INSTRUMENTS.length);
		}
		if (chordInstList.isEmpty())
			chordInstList.addAll(Arrays.asList(CHORD_INSTRUMENTS));
		return parseInstrument(chordInstList.remove(Random.rangeInt(0, chordInstList.size())));
	}
	
	private int arpeggioPos = -1;
	/** make random choice on arpeggio instrument (depended to chord instrument) */
	public Instrument randomArpeggioInstrument() {
		if (MEMOIZE_INSTRUMENTS && !Random.nextBoolean(MEMOIZE_INSTRUMENTS_FUZZINESS)) {
			if (arpeggioPos == -1)
				arpeggioPos = Random.rangeInt(0, CHORD_INSTRUMENTS.length);
			if (arpeggioPos == chordPos)
				arpeggioPos = (chordPos + Random.rangeInt(0, CHORD_INSTRUMENTS.length-1)) % CHORD_INSTRUMENTS.length;
			return parseInstrument(CHORD_INSTRUMENTS[arpeggioPos]);
		}
		if (!EXPLOIT_INSTRUMENTS)
			return parseInstrument(CHORD_INSTRUMENTS[Random.rangeInt(0, CHORD_INSTRUMENTS.length)]);
		if (chordInstList.isEmpty())
			chordInstList.addAll(Arrays.asList(CHORD_INSTRUMENTS));
		return parseInstrument(chordInstList.remove(Random.rangeInt(0, chordInstList.size())));
	}
	
	public final String[] BASS_INSTRUMENTS = getConfigStrings("bass-instruments", new String[] {
		"ElectricBass [28,55,Scarbee_JayBass_Neck,Electric_Bass_Finger]",
		"DoubleBassPizz [24,65,Basses_SessionStringsPro,Acoustic_Bass]",
		"DoubleBassArco [24,65,Basses_SessionStringsPro,Contrabass]",
	});
	/** make random choice on bass instrument */
	private int bassPos = -1;
	private final List<String> bassInstList = new ArrayList<String>();
	public Instrument randomBassInstrument() {
		if (MEMOIZE_INSTRUMENTS) { // && !Random.nextBoolean(MEMOIZE_INSTRUMENTS_FUZZINESS)) {
			if (bassPos == -1)
				bassPos = Random.rangeInt(0, BASS_INSTRUMENTS.length);
			return parseInstrument(BASS_INSTRUMENTS[bassPos]);
		}
		if (!EXPLOIT_INSTRUMENTS)
			return parseInstrument(BASS_INSTRUMENTS[Random.rangeInt(0, BASS_INSTRUMENTS.length)]);
		if (bassInstList.isEmpty())
			bassInstList.addAll(Arrays.asList(BASS_INSTRUMENTS));
		return parseInstrument(bassInstList.remove(Random.rangeInt(0, bassInstList.size())));
	}

	public final float MELODY_ENABLED = getConfigFloat("melody-enabled", 0.9f);
	public final float CHORDS_ENABLED = getConfigFloat("chords-enabled", 0.8f);
	public final float DRUMS_ENABLED = getConfigFloat("drums-enabled", 0.8f);
	public final float ARPEGGIO_ENABLED = getConfigFloat("arpeggio-enabled", 0.5f);
	public final float BASS_ENABLED = getConfigFloat("bass-enabled", 0.9f);
}
