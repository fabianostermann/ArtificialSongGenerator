import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 * This module analyses a midi song file and writes an appropriate onset time (in seconds) of instruments annotation file (.arff)
 * In this current version instrument changes are read only once on each track and tempo is read from track 0 tick 0.
 * @author Fabian Ostermann (Apr 23 '19)
 *
 */

public class OnsetAnnotator {
	
	public static void printHelp() {
		System.out.print("This module analyses a midi song file and writes an appropriate onset time (in seconds) of instruments annotation file (.arff)\n" +
							"In this current version instrument changes are read only once per track and tempo change is supposed on track 0 at tick 0.\n" +
							"\n" +
							"Use --midifile to choose a specific file. Default filename is '"+fileName+".mid'" +
							"Use -v or --verbose to get verbose output." +
							"Use -h or --help to print this.");
	}
	
    //public static final int NOTE_ON = 0x90;
    //public static final int NOTE_OFF = 0x80;
	public static final int SET_TEMPO = 0x51;

	public static int resolution;
	public static int tempo; // in BPM (beats per minute)
	
	public static String[] instrumentOnChannel;
	
	public static HashMap<String, List<Integer>> keysOn = new HashMap<>(); // <instrument name, keys pressed>
	public static HashMap<Long, List<ShortMessage>> messages = new HashMap<>(); // <tick, midiOn|midiOff>
	
	public static float[] onsetTimes; // onset times ordered
	public static int[][] onsetSimilarities; // 1 if channel has onset, 0 else
	
	public static String[] arffInstrumentList = {
		"AcousticGuitar", "Balalaika", "Bandura", "BanjoFramus", "Banjolin", "Bass", "Bassoon",
		"Bawu", "Bouzouki", "Cello", "CeylonGuitar", "Clarinet", "Contrabassoon", "Cumbus",
		"DallapeAccordion", "Dilruba", "Domra", "Drums", "DungDkarTrumpet", "EgyptianFiddle",
		"ElectricBass", "ElectricGuitar", "ElectricPiano", "Erhu", "Flute", "Fujara", "Horn",
		"JinghuOperaViolin", "Kantele", "Melodica", "MorinKhuurViolin", "Oboe", "Oud", "Organ",
		"Panflute", "Piano", "Pinkillo", "PivanaFlute", "Saxophone", "ScaleChangerHarmonium",
		"Shakuhachi", "Sitar", "Tampura", "Tanbur", "Trombone", "Trumpet", "Tuba",
		"TurkeySaz", "Ukulele", "Viola", "Violin"
	};
	public static HashMap<String, String> arffToMidiInstrumentMap = new HashMap<>();
	static {
		arffToMidiInstrumentMap.put("Trumpet", "Trumpet");
		arffToMidiInstrumentMap.put("Saxophone", "Tenor_Sax");
		arffToMidiInstrumentMap.put("Flute", "Flute");
		arffToMidiInstrumentMap.put("Violin", "Violin");
		arffToMidiInstrumentMap.put("Piano", "Piano");
		arffToMidiInstrumentMap.put("ElectricPiano", "Electric_Piano");
		arffToMidiInstrumentMap.put("Organ", "Rock_Organ");
		arffToMidiInstrumentMap.put("Cello", "String_Ensemble_1");
		arffToMidiInstrumentMap.put("Drums", "Drums");
	}

	public static String fileName = "somesong";
	
    public static void main(String[] args) throws IOException {
    	OnsetAnnotator.args = args;
    	
    	if (argscheck("--help") || argscheck("-h")) {
			printHelp();
			System.exit(0);
		}
    	
    	String argsFileName = argsget("--midifile=");
		if (argsFileName != null)
			fileName = argsFileName.replaceAll(".mid", "");
    	
		
        Sequence sequence = null;
		try {
			sequence = MidiSystem.getSequence(new File(fileName+".mid"));
		} catch (InvalidMidiDataException | IOException e) {
			System.out.println("Could not read file '"+fileName+".mid"+"': "+e.getMessage());
			System.exit(1);
		}

		System.out.println("Analysing '"+fileName+".mid'..");
		
        // check division type
        if (sequence.getDivisionType() != Sequence.PPQ) {
          System.out.println("Wrong devision type. Need PPQ, got "+ sequence.getDivisionType());
          System.exit(1);
        }
        // get resolution setting (normally 128 ticks per quarter note)
        resolution = sequence.getResolution();
        
        // initiate tick2second calculation
        initTickToSecond(sequence);
        System.out.println("Read tempo successfully (set tempo="+tempo+"bpm)");

        // read in channel instrument mapping
        instrumentOnChannel = new String[16];
        for (int i = 0; i < instrumentOnChannel.length; i++)
        	instrumentOnChannel[i] = "unknown_"+(i+1);
        instrumentOnChannel[9] = "Drums";
        for (Track track :  sequence.getTracks()) {
        	for (int i=0; i < track.size(); i++) { 
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                	ShortMessage sm = (ShortMessage) message;
                	if (sm.getCommand() == ShortMessage.PROGRAM_CHANGE){
                		instrumentOnChannel[sm.getChannel()] = MidiDictionary.INSTRUMENT_BYTE_TO_STRING.get((byte)sm.getData1());
                	}
                }
        	}
        }
        
        // all tracks to one long list (hashMap)
        for (Track track :  sequence.getTracks()) {
        	for (int i=0; i < track.size(); i++) { 
        		MidiEvent event = track.get(i);
        		MidiMessage message = event.getMessage();
        		if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == ShortMessage.NOTE_ON ||
                    		sm.getCommand() == ShortMessage.NOTE_OFF) {
                    	long tick = event.getTick();
                    	if (!messages.containsKey(tick))
                    		messages.put(tick, new ArrayList<ShortMessage>());
                    	messages.get(tick).add(sm);
                    }
        		}
        	}
        }
        
        // sort ticks
        List<Long> sortedKeyList = new ArrayList<>(messages.keySet());
        java.util.Collections.sort(sortedKeyList);
        
        // read onsets of instruments
        onsetSimilarities = new int[sortedKeyList.size()][instrumentOnChannel.length];
        onsetTimes = new float[sortedKeyList.size()];
        for (String inst : instrumentOnChannel)
        	keysOn.put(inst, new ArrayList<Integer>());
        int i = -1;
        for (Long tick : sortedKeyList) {
        	i++;
        	onsetTimes[i] = tickToSecond(tick);
        	for (ShortMessage sm : messages.get(tick)) {
        		Integer key = new Integer(sm.getData1());
        		if (sm.getCommand() == ShortMessage.NOTE_ON) {
        			keysOn.get(instrumentOnChannel[sm.getChannel()]).add(key);
        		}
        		if (sm.getCommand() == ShortMessage.NOTE_OFF) {
        			keysOn.get(instrumentOnChannel[sm.getChannel()]).remove(key);
        		}
        	}
        	for (int j = 0; j < instrumentOnChannel.length; j++) {
        		onsetSimilarities[i][j] = keysOn.get(instrumentOnChannel[j]).isEmpty() ? 0 : 1;
        	}
        }
        
        if (argscheck("--verbose") || argscheck("-v")) {
	        // debug: output results
	        System.out.print("onset,");
	        for (i = 0; i < instrumentOnChannel.length; i++)
	        	System.out.print(instrumentOnChannel[i]+",");
	        System.out.println();
	        for (i = 0; i < onsetSimilarities.length; i++) {
	        	System.out.print(onsetTimes[i]+",");
	        	for (int j = 0; j < onsetSimilarities[i].length; j++) {
	        		System.out.print(onsetSimilarities[i][j]+",");
	        	}
	        	System.out.println();
	        }
        }
        
        // arff file generation
        File arffFile = new File(fileName+"-onsets.arff");
        BufferedWriter writer = null;
		try {
			writer = Files.newBufferedWriter(arffFile.toPath(), StandardCharsets.UTF_8,
					StandardOpenOption.CREATE, // create if not exists
					StandardOpenOption.TRUNCATE_EXISTING, // clear to zero length
					StandardOpenOption.WRITE);
		} catch (IOException e) {
			System.out.println("Could not create or write to '"+fileName+"-onsets.arff"+"': "+e.getMessage());
			System.exit(1);
		} // grant write access
        // writing header
        writer.write("@RELATION 'Instrument similarities in "+fileName+".mid'"); writer.newLine();
        writer.newLine();
        writer.write("@ATTRIBUTE OnsetTime NUMERIC"); writer.newLine();
        for (String instrument : arffInstrumentList) {
        	writer.write("@ATTRIBUTE 'Similarity to "+instrument+"' NUMERIC"); writer.newLine();
        } 
        writer.newLine();
        writer.write("@DATA"); writer.newLine();
        for (i = 0; i < onsetSimilarities.length; i++) {
        	writer.write(""+onsetTimes[i]);
        	for (int j = 0; j < arffInstrumentList.length; j++) {
        		String midiInstrument= arffToMidiInstrumentMap.getOrDefault(arffInstrumentList[j], arffInstrumentList[j]);
        		int channel = -1;
        		for (int k = 0; k < instrumentOnChannel.length; k++)
        			if (instrumentOnChannel[k].equals(midiInstrument))
        	        		channel = k;
        		if (channel == -1)
        			writer.write(",0");
        		else
        			writer.write(","+onsetSimilarities[i][channel]);
        	}
        	writer.newLine();
        }
        writer.close();
        System.out.println("arff file '"+arffFile.getName()+"' written.");
    }
    
    public static void initTickToSecond(Sequence sequence) {
    	// tempo is needed in track 0 at position @0 for now (TODO use tempo map later)
    	Track track = sequence.getTracks()[0]; 
    	for (int i=0; i < track.size(); i++) { 
            MidiEvent event = track.get(i);
            if (event.getTick() > 0) {
            	System.out.println("Set_Tempo event not found it track:0-tick:0");
            	System.out.println(1);
            }
            MidiMessage message = event.getMessage();
            if (message instanceof MetaMessage) {
            	MetaMessage mm = (MetaMessage) message;
            	if(mm.getType()==SET_TEMPO){
            		byte[] data = mm.getData();
            		int midiTempo = (data[0] & 0xff) << 16 | (data[1] & 0xff) << 8 | (data[2] & 0xff);
            		int tempoInBPM = 60000000 / midiTempo;
            		tempo = tempoInBPM;
                }
            	return;
            }
    	}
    }
    
    public static float tickToSecond(long tick) {
    	return (float)(tick*60)/(tempo*resolution); // (tick / resolution) * (60 / tempo)
    }
    
    private static String[] args = null;
	/**
	 * Checks if the test string is in the arguments list
	 * @param teststr The string to be tested
	 * @return True if test string is found, False otherwise
	 */
	private static boolean argscheck(String teststr) {
		for (int i=0; i<args.length; i++) {
			if(args[i].equals(teststr))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if any string from the arguments list starts with test string.
	 * @param teststr The string to be tested
	 * @return If a suiting argument is found, the remaining string is returned. Else null.
	 */
	private static String argsget(String teststr) {
		for (int i=0; i<args.length; i++) {
			if(args[i].startsWith(teststr)) {
				return args[i].replaceFirst(teststr, "");
			}
		}
		return null;
	}
}