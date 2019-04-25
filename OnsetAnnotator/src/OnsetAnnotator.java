import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 * Reads out a midi file and writes an appropriate onset time (in seconds) annotation file (.arff)
 * @author Fabian Ostermann (Apr 23 '19)
 *
 */

public class OnsetAnnotator {
    //public static final int NOTE_ON = 0x90;
    //public static final int NOTE_OFF = 0x80;
	public static final int SET_TEMPO = 0x51;

	public static int resolution;
	public static int tempo; // in BPM (beats per minute)
	
	public static String[] instrumentOnChannel;
	
	public static HashMap<String, List<Integer>> keysOn = new HashMap<>(); // <instrument name, keys pressed>
	public static HashMap<Long, List<ShortMessage>> messages = new HashMap<>(); // <tick, midiOn|midiOff>
	
	public static float[] onsetTimes;
	public static int[][] onsetSimilarities; 

    public static void main(String[] args) throws Exception {
        Sequence sequence = MidiSystem.getSequence(new File("somesong.mid"));
        
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
        instrumentOnChannel[9] = "drums";
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
    
}
