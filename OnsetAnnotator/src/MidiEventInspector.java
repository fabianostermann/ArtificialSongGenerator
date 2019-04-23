import java.io.File;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 * https://stackoverflow.com/questions/3850688/reading-midi-files-in-java
 * @author Sami Koivu (Oct 3 '10 at 17:56)
 * @author Fabian Ostermann (added SET_TEMPO meta message support - Apr 23 '19)
 *
 */

public class MidiEventInspector {
    //public static final int NOTE_ON = 0x90;
    //public static final int NOTE_OFF = 0x80;
	public static final int SET_TEMPO = 0x51;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    public static void main(String[] args) throws Exception {
        Sequence sequence = MidiSystem.getSequence(new File("somesong.mid"));
        
        System.out.println("Division Type:" +sequence.getDivisionType() + "(PPQ="+Sequence.PPQ+")");
        System.out.println("Resolution: "+sequence.getResolution());
        
//        System.exit(0);

        int trackNumber = 0;
        for (Track track :  sequence.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            for (int i=0; i < track.size(); i++) { 
                MidiEvent event = track.get(i);
                System.out.print("@" + event.getTick() + " ");
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    System.out.print("Channel: " + sm.getChannel() + " ");
                    if (sm.getCommand() == ShortMessage.NOTE_ON) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else if (sm.getCommand() == ShortMessage.NOTE_OFF) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else if (sm.getCommand() == ShortMessage.PROGRAM_CHANGE) {
                        int instrument = sm.getData1();
                        System.out.println("Program change, instrument=" + instrument +" "+ MidiDictionary.INSTRUMENT_BYTE_TO_STRING.get((byte)instrument));
                    } else {
                        System.out.println("Command:" + sm.getCommand());// +"-"+ sm.getData1() +"-"+ sm.getData2() +"-"+ sm.getStatus());
                    }
                } else if (message instanceof MetaMessage) {
                	MetaMessage mm = (MetaMessage) message;
                	if(mm.getType()==SET_TEMPO){
                		byte[] data = mm.getData();
                		int tempo = (data[0] & 0xff) << 16 | (data[1] & 0xff) << 8 | (data[2] & 0xff);
                		int bpm = 60000000 / tempo;
                		System.out.println("Set tempo="+bpm+"bpm");
                    }
                } else {
                    System.out.println("Other message: " + message.getClass());
                }
            }

            System.out.println();
        }

    }
}
