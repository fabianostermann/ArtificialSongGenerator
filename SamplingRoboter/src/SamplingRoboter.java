import java.io.File;
import java.util.Date;
import java.util.Locale;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SamplingRoboter {

	public static boolean DEBUG_GUI_ENABLED = false;
	
	public static void main(String[] args) {
		if (args.length > 0 && args[0].equalsIgnoreCase("--debug-gui"))
			DEBUG_GUI_ENABLED = true;
		
		System.out.println("## SamplingRoboter for ArtificialSongGenerator started ## ("+new Date()+")");
		
		Locale.setDefault(Locale.US);
		
		if (args.length > 0 && args[0].equals("--help")) {
			System.out.println("TODO help print. Exit.");
			System.exit(0);
		}
		
		// globals
		Sequencer sequencer = null;
		MidiDevice midiDevice = null;
		File midiFile = null;
		int[] numOfMidiOn = null;
		String[] firstInstrument = null;
		try {
			
			// opening a file
			if (!DEBUG_GUI_ENABLED)
				midiFile = new File("/media/oyster/Dropbox/content/Dropbox/WHF/ArtificialSongGenerator_workspace/Sampling/Database-20190930/01.mid");
			else {
				JFileChooser chooser = new JFileChooser(new File("."));
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "MIDI Files", "mid", "midi");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					midiFile = chooser.getSelectedFile();
				} else {
					System.out.println("No file chosen. Exit.");
					System.exit(0);
				}
			}
			System.out.println("midiFile for conversion: "+midiFile);
			
			MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
			String retVal = "0";
			if (DEBUG_GUI_ENABLED) {
				String inputMsg = "Available MIDI Devices are:";
				for (int i = 0; i < infos.length; i++) {
					inputMsg +="\n"+(i) + ") ";
					inputMsg +=infos[i].getName() + ":   ";
					inputMsg +=infos[i].getDescription();
				}
				retVal = JOptionPane.showInputDialog(null, inputMsg, retVal);
			}
			if (retVal == null) {
				System.out.println("No Midi Device chosen. Exit.");
				System.exit(0);
			} else {
				int midiDevNumber = Math.min(infos.length-1, Math.max(0, Integer.parseInt(retVal)));
				MidiDevice.Info chosenInfo = infos[midiDevNumber];
				System.out.println("Chosen midi device: ("+midiDevNumber+") "+chosenInfo.getName());
				midiDevice = MidiSystem.getMidiDevice(chosenInfo);
				midiDevice.open();
			}
			
			// init midi system
			sequencer = MidiSystem.getSequencer(false);
			sequencer.open();
			sequencer.getTransmitter().setReceiver(midiDevice.getReceiver());
			Sequence seq = MidiSystem.getSequence(midiFile);
	        sequencer.setSequence(seq);
			
			// MIDI file info
	        System.out.println("Analysing song..");
	        numOfMidiOn = new int[seq.getTracks().length];
	        firstInstrument = new String[seq.getTracks().length];
	        for (int track=0; track<seq.getTracks().length; track++) {
	        	numOfMidiOn[track] = 0;
	        	firstInstrument[track] = track==9 ? "Drums" : null;
	        	for (int i=0; i<seq.getTracks()[track].size(); i++) {
	        		MidiMessage msg = seq.getTracks()[track].get(i).getMessage();
	        		if (msg instanceof ShortMessage) {
	                    ShortMessage sm = (ShortMessage) msg;
	                    if (sm.getCommand() == ShortMessage.NOTE_ON)
	                    	numOfMidiOn[track] += 1;
	                    else if (sm.getCommand() == ShortMessage.PROGRAM_CHANGE && firstInstrument[track] == null){
	                    	firstInstrument[track] = MidiDictionary.INSTRUMENT_BYTE_TO_STRING.get((byte)sm.getData1());
	                	}
	        		}	
	        	}
	        	System.out.println("track("+track + ") midiOns="+numOfMidiOn[track]+", instrument="+firstInstrument[track]);
	        }
	        
//	        for (int track=0; track<seq.getTracks().length; track++) {
		    for (int track=0; track<1; track++) {
	        	
	        	if (numOfMidiOn[track] <= 0)
	        		continue;
	        	
	        	// choose right track
	        	for (int i=0; i<seq.getTracks().length; i++) {
	        		sequencer.setTrackSolo(i, false);
	        		sequencer.setTrackMute(i, false);
	        	}
		        sequencer.setTrackSolo(track, true);
		        
			    //#### Playback ####
				System.out.println("Playback of '"+ firstInstrument[track] +"' starts..");
				doRecording(midiFile.getParent(), "00000kontakttest__"+firstInstrument[track], sequencer);
				System.out.println("Creation of '"+ firstInstrument[track] +"' done.");
				
	        }
			
		} catch (Exception e) {
			System.out.println("Oh oh.. "+e.getClass() + ": " + e.getMessage());
//			e.printStackTrace();
		} finally {
			if (sequencer != null)
				sequencer.close();
			if (midiDevice != null)
				midiDevice.close();
		}
		
		System.out.println("Exit.");
	}
	
	public static void doRecording(String folder, String filename, Sequencer sequencer) {
		
		File wavfile = new File(folder+"/"+filename+".wav");
		
		AudioRecorder.initFile(wavfile);
		AudioRecorder.startRecording();
		
		sequencer.setMicrosecondPosition(0);
		sequencer.start();
		long startTime = System.currentTimeMillis();
		while (sequencer.isRunning() && System.currentTimeMillis() - startTime < 5000)
			sleep(200);
		sequencer.stop();
		
		sleep(1000);
		AudioRecorder.stopRecording();
		
		LameWrapper.removeWavAfter(true);
		LameWrapper.execute(wavfile);
	}

	/**
	 * Wraps System.sleep(millis) to avoid try/catch block every time
	 * @param millis time to sleep in milli seconds
	 */
	public static void sleep(long millis) {try {Thread.sleep(millis);} catch (Exception e) {}}
}
