import java.io.File;

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
	public static int tempo;

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
}
