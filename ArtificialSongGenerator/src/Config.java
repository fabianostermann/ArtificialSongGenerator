import org.jfugue.theory.Key;


public class Config {

	// TODO put all config paramters in a map and load every parameter as strings from the map.
	// Then the config loading can just overwrite map entries with strings
	
//	private static Properties properties = new Properties();
//	private static final String PROPERTIES_FILENAME = "config.txt";
//	private static final String PROPERTIES_DUMMY_FILENAME = "dummy.config.txt";

	public static String THESONG_TITLE = "thesong";
	public static String MIDI_SUFFIX = ".midi";
	public static String ARFF_SUFFIX = ".arff";
	public static String OUTPUT_DIR = "";
	
	public static int Nof_DIFFERENT_SONGPARTS = 3;
	public static int Nof_SONGPARTS_IN_SONG = 8;
	
	
	// make random choice on major keys (memoizable)
	public static String[] KEYS = new String[]
			{ "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
	public static boolean MEM_KEY = false;
	public static int keyPos = -1;
	public static Key getRandomKey() {
		if (keyPos == -1 || !MEM_KEY)
			keyPos = Random.rangeInt(0, KEYS.length);
		return new Key(KEYS[keyPos]+"maj");
	}

	// make random choice on tempo (memoizable)
	public static int[] TEMPO_RANGE = new int[] { 60, 180 };
	public static boolean MEM_TEMPO = false;
	public static int tempo = -1;
	public static int getRandomTempo() {
		if (tempo == -1 || !MEM_TEMPO)
			tempo = Random.rangeInt(TEMPO_RANGE[0], TEMPO_RANGE[1]);
		return tempo;
	}
	
	// make random choice on songparts length
		public static int[] SONGPARTS_LENGTH = new int[] { 4, 6, 8 };
		public static int getRandomSongpartLength() {
			return SONGPARTS_LENGTH[Random.rangeInt(0, SONGPARTS_LENGTH.length)];
		}
	

	// INFO: For all possible instruments see org.jfugue.midi:MidiDictionary.java
		
	// make random choice on melody instrument
	public static String[] MELODY_INSTRUMENTS = new String[] {
		"Vibraphone", "Distortion_Guitar", "Violin", "Trumpet", "Tenor_Sax",
		"Flute", "Synth_Voice"
	};
	public static String getRandomMelodyInstrument() {
		return MELODY_INSTRUMENTS[Random.rangeInt(0, MELODY_INSTRUMENTS.length)];
	}
	
	// make random choice on chord instrument
	public static String[] CHORD_INSTRUMENTS = new String[] {
		"Piano", "Electric_Piano", "Vibraphone", "Rock_Organ", "Guitar",
		"Electric_Jazz_Guitar", "Overdriven_Guitar", "String_Ensemble_1",
		"Poly_Synth"
	};
	public static String getRandomChordInstrument() {
		return CHORD_INSTRUMENTS[Random.rangeInt(0, CHORD_INSTRUMENTS.length)];
	}
	
	/*
	 * loads all properties change requests from file, do it at start
	 */
//	static {
//		
//		BufferedInputStream stream = new BufferedInputStream(new FileInputStream("beispiel.properties"));
//		properties.load(stream);
//		stream.close();
//	}
	
	
//	/**
//	 * Creates a dummy file as overview to all possible settings
//	 */
//	public static void createDummyFile() {
//		// TODO write config dummy file creation
//		BufferedOutputStream stream;
//		try {
//			stream = new BufferedOutputStream(new FileOutputStream(PROPERTIES_DUMMY_FILENAME));
//			properties.store(stream, "Rename the file to '"+PROPERTIES_FILENAME+"' if you want it to be used.");
//			stream.close();
//		} catch (FileNotFoundException e) {
//			System.out.println("File could not be created: "+e.getMessage());
//		} catch (IOException e) {
//			System.out.println("Unable to store properties to file: "+e.getMessage());
//		}
//		System.out.println("Successfully stored all available properties in file '"+PROPERTIES_DUMMY_FILENAME+"'.\n" +
//				"Rename the file to '"+PROPERTIES_FILENAME+"' if you want it to be used.");
//	}
}
