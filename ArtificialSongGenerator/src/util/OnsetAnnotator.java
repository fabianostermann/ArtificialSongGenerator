package util;
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

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.jfugue.theory.Note;

import main.ArtificialSongGenerator;
import main.Config;
import parts.Instrument;
import sun.nio.cs.ext.ISCII91;

/**
 * This unit analyses a midi song file and writes an appropriate onset time (in seconds) of instruments annotation file (.arff)
 * For each onset the midi key numbers are written to the arff file.
 * 
 * @author Fabian Ostermann (fabian.ostermann@udo.edu)
 */

public class OnsetAnnotator {
	
//	public static ArgsUtil argsUtil = null;
	
//	public static void printHelp() {
//		System.out.println("USAGE java OnsetAnnotator --midifile=<file>\n" +
//							"\n" +
//							"This module analyses a midi song file and writes an appropriate onset time (in seconds) of instruments annotation file (.arff)\n" +
//							"In this current version instrument change events are read only once per track.\n" +
//							"For each onset the midi key numbers are written to the arff file.\n" +
//							"\n" +
//							"Use --midifile  choose a file. \n" +
//							"Use --verbose to get verbose output.\n" +
//							"Use -v or --version to show version number.\n" +
//							"Use -h or --help to print this.");
//	}
	
//    public static final int SET_TEMPO = 0x51;

	private Sequencer sequencer;

	public static List<String> allConfigInstruments;
	private Map<Float, Map<String, List<ShortMessage>>> allMessages = new HashMap<>();
	
//	private float[] onsetTimes; // onset times ordered
//	private String[][] onsetEvents; // a list of keys, e.g. [60,72,73], or [] if empty
	
    public void parse(String instrumentName, File midiFile) {

		// init full instrument list from Config
    	if (allConfigInstruments == null) {
    		allConfigInstruments = new ArrayList<>();
    		for (String inst : Config.GET.MELODY_INSTRUMENTS)
    			allConfigInstruments.add(Config.parseInstrumentName(inst));
    		for (String inst : Config.GET.CHORD_INSTRUMENTS)
    			allConfigInstruments.add(Config.parseInstrumentName(inst));
    		for (String inst : Config.GET.BASS_INSTRUMENTS)
    			allConfigInstruments.add(Config.parseInstrumentName(inst));
    		allConfigInstruments.add(Config.DRUMS_SUFFIX);
    		// alphabetical order
    		java.util.Collections.sort(allConfigInstruments);
    		if (ArtificialSongGenerator.VERBOSE_MODE)
    			System.out.println("Full instrument list from config: " + allConfigInstruments);
    	}
    	
    	Instrument instrument = Instrument.find(instrumentName);
    	if (instrument==null && instrumentName != Config.DRUMS_SUFFIX) {
    		System.out.println("Instrument '"+instrumentName+"' was not used in music");
    		return;
    	}
    	
    	Sequence sequence = null;
		try {
			sequence = MidiSystem.getSequence(midiFile);
		} catch (InvalidMidiDataException | IOException e) {
			System.out.println("Could not read file '"+midiFile.getName()+"': "+e.getMessage());
			System.exit(1);
		}
		
		System.out.println("Analysing '"+midiFile.getName()+"'..");
		
		// initiate tick2second calculation
        try {
			initTickToSecond(sequence);
		} catch (MidiUnavailableException | InvalidMidiDataException e) {
			System.out.println("Error while initiating midi sequencer: "+e.getMessage());
			System.exit(1);
		}
		
        
        // all tracks to one long list (hashMap)
        HashMap<Long, List<ShortMessage>> messages = new HashMap<>(); // <tick, midiOn|midiOff>
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
        
        
        // add messages to global message store
        for (Long tick : messages.keySet()) {
        	float sec = tickToSecond(tick);
        	if (!allMessages.containsKey(sec))
        		allMessages.put(sec, new HashMap<String, List<ShortMessage>>());
        	allMessages.get(sec).put(instrumentName, messages.get(tick));
        }
        
        if (sequencer != null)
        	sequencer.close();
    }

	public void write(File file) {
		
		System.out.println("Write onset annotation file..");
		
		// arff file generation
        BufferedWriter writer = null;
		try {
			writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8,
					StandardOpenOption.CREATE, // create if not exists
					StandardOpenOption.TRUNCATE_EXISTING, // clear to zero length
					StandardOpenOption.WRITE);
		
			List<Float> eventSecs = new ArrayList<>(allMessages.keySet());
			java.util.Collections.sort(eventSecs);
		
	        // writing header
	        writer.write("@RELATION 'Onset events in "+Config.GET.THESONG_TITLE+"'"); writer.newLine();
	        writer.newLine();
	        writer.write("@ATTRIBUTE 'Onset time in seconds' NUMERIC"); writer.newLine();
	        for (String instrument : allConfigInstruments) {
	        	writer.write("@ATTRIBUTE 'Onset events of "+instrument+"' STRING"); writer.newLine();
	        } 
	        writer.newLine();
	        writer.write("@DATA"); writer.newLine();
	        
	        // write onset data
	        Map<String, int[]> noteOn = new HashMap<>();
	        for (String instrName : allConfigInstruments)
	        	noteOn.put(instrName, new int[128]);
	        for (Float sec : eventSecs) {
	        	// write onset time in seconds
	        	writer.write(""+sec);
	        	
	        	Map<String, List<ShortMessage>> messages = allMessages.get(sec);
	        	for (String instrName : allConfigInstruments) {
	        		// get current array (must be pushed back later)
	        		int[] noteOnMessages = noteOn.get(instrName);
	        		Instrument instrument = Instrument.find(instrName);
	        		
	        		for (int i = 0; i < noteOnMessages.length; i++)
	        			noteOnMessages[i] = noteOnMessages[i]==0 ? 0 : noteOnMessages[i]+1;
	        		for (ShortMessage sm : messages.getOrDefault(instrName, new ArrayList<>())) {
	        			int key = sm.getData1();
	        			if (sm.getCommand() == ShortMessage.NOTE_OFF)
	        				noteOnMessages[key] = 0;
	        			else if (sm.getCommand() == ShortMessage.NOTE_ON) {
	        				noteOnMessages[key] = 1;
	                        // check for notes that are out of the specific instrument range
	                        // this is an error and should be prevented by element creators
	                        if (instrument != null)
	                        	if (key < instrument.getLowestNote().getValue()
	                        			|| key > instrument.getHighestNote().getValue())
	                        		System.out.println("WARNING: Unplayable pitch.. "+instrument+" cannot play midi note "+key);
	        			}
	        		}
	        		// write instrument annotation entry
	        		writer.write(ArffUtil.NEXT);
	        		writer.write(ArffUtil.STR_DELIM+ArffUtil.STR_OPEN);
	        		String notesStr = "";
	        		for (int key = 0; key < noteOnMessages.length; key++) {
	        			int keyState = noteOnMessages[key];
	        			if (keyState==1)
	        				notesStr += "+";
	        			if (keyState > 0)
	        				notesStr += key + ArffUtil.STR_SEP;
	        		}
	        		if (!notesStr.isEmpty())
	        			notesStr = notesStr.substring(0, notesStr.length()-1);
	        		writer.write(notesStr+ArffUtil.STR_CLOSE+ArffUtil.STR_DELIM);
	        		// push back array
	        		noteOn.put(instrName, noteOnMessages);
	        	}
	        	writer.newLine();
	        }
	        
//	                		

	        	
			writer.close();
			
		} catch (IOException e) {
			System.out.println("Could not create or write to '"+file.getName()+"': "+e.getMessage());
			System.exit(1);
		} // grant write access
		
		System.out.println("Created onset annotation file '"+file.getName()+"'.");
	}
    
	private void initTickToSecond(Sequence sequence) throws MidiUnavailableException, InvalidMidiDataException {
    	// init midi system
		sequencer = MidiSystem.getSequencer();
    	sequencer.open();
    	sequencer.setSequence(sequence);
    }
    
    // see: https://stackoverflow.com/questions/23070510/how-to-get-exact-time-of-a-midi-event
	private float tickToSecond(long tick) {
    	sequencer.setTickPosition(tick);
    	return ((float) sequencer.getMicrosecondPosition()) / 1000000.f;
    }
    
    public static final String[] noteNames = { "C", "Cis", "D", "Dis", "E", "F", "Fis", "G",
			"Gis", "A", "Ais", "B" };
    /**
     * converts a midi key number to note string
     * @param noteNumber midi key number
     * @return the note string OR null if noteNumber<0
     */
	private String generateNoteName(int noteNumber) {
		if (noteNumber >= 0) {
			int noteName = (noteNumber % 12);
			int octave = (noteNumber / 12) - 1;
			return noteNames[noteName] + octave;
		}
		return null;
		// C2 36, C1 24, C0 12, C-1 0
	}

}
