package main;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import asglib.ArgsUtil;
import asglib.MidiDictionary;
import info.Version;
import record.AudioRecorder;

/**
 * This unit samples the artificial midi songs from ArtificialSongGenerator to audio.
 * To do this, any synthesizer can be connected using a loopback cable on a connected ADC audio interface.
 * E.g., the author used <i>Kontakt 5 Player</i> with the <i>Komplete 11 Ultimate</i> package from <i>Native Instruments</i> for realistic sampled instruments.
 * 
 * @author Fabian Ostermann (fabian.ostermann@udo.edu)
 */

public class SamplingRoboter {

	public static final String DEFAULT_FILE_EXTENSION = ".mid";
	
	public static ArgsUtil argsUtil = null;
	
	public static List<File> INPUT_FILES = new ArrayList<>();
	public static boolean DEBUG_GUI_ENABLED = false;
	public static boolean DEBUG_RECORDINGS_ENABLED = false;
	public static String MIDI_DEVICE_NO = "0";
	public static List<String> LAME_ARGS = null;
	public static boolean COMPRESS = false;
	public static boolean DELETE_RAW = false;
	
	public static void main(String[] args) {
		argsUtil = new ArgsUtil(args);
		
		// print midi device infos
		if (args.length==0 || argsUtil.check("--help") || argsUtil.check("-h")) {
			printHelp();
			// get infos
			MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
			String deviceInfo = "Available MIDI Devices are:";
			for (int i = 0; i < infos.length; i++) {
				deviceInfo +="\n"+(i) + ") ";
				deviceInfo +=infos[i].getName() + ":   ";
				deviceInfo +=infos[i].getDescription();
			}
			System.out.println(deviceInfo);
			System.exit(0);
		}
		System.out.println("### SamplingRoboter for ArtificialSongGenerator started ### ("+new Date()+")");
		Locale.setDefault(Locale.US);
		// state version
		if (argsUtil.check("--version") || argsUtil.check("-v")) {
			System.out.println(Version.VERSION);
			System.exit(0);
		}

		// enable debug gui
		if (argsUtil.check("--debug-gui")) {
			DEBUG_GUI_ENABLED = true;
			System.out.println("DEBUG: Enabled demo GUI, use only for testing reasons");
		}
		// enable debug recordings
		if (argsUtil.check("--debug-recordings")) {
			DEBUG_RECORDINGS_ENABLED = true;
			System.out.println("DEBUG: Enabled short demo recordings");
		}
		// compression enabled
		if (argsUtil.check("--compress")) {
			COMPRESS = true;
		}
		// remove raw after compression
		if (argsUtil.check("--delete-wav")) {
			DELETE_RAW = true;
		}
		// alter location of SoX executable
		SoxWrapper.SOX_PROGRAM = argsUtil.get("--sox-path=", SoxWrapper.SOX_PROGRAM);
		// alter location of Lame executable
		LameWrapper.LAME_PROGRAM = argsUtil.get("--lame-path=", LameWrapper.LAME_PROGRAM);
		// choose midi device by number
		MIDI_DEVICE_NO = argsUtil.get("--mididevice=", MIDI_DEVICE_NO);
		// choose midi device by number
		String lameArgs = argsUtil.get("--lame-options=");
		if (lameArgs != null) {
			LAME_ARGS = new ArrayList<>();
			for (String arg : lameArgs.trim().split(" "))
				LAME_ARGS.add(arg);
		}
		// specify a midifile to use
		String infileStr = argsUtil.get("--midifile=");
		if (infileStr != null) {
			File infile = new File(infileStr);
			if (infile.exists())
				INPUT_FILES.add(infile);
			else {
				System.out.println("File '"+infile.getAbsolutePath()+"' does not exist. Exit.");
				System.exit(1);
			}
		}
		else {
			String midiDirStr = argsUtil.get("--mididir=");
			final String fileExtension = argsUtil.get("--midiext=");
			if (midiDirStr != null) {
				File midiDir = new File(midiDirStr);
				if (midiDir.exists() && midiDir.isDirectory())
					for (File midiFile : midiDir.listFiles(
							new FilenameFilter() {
								@Override public boolean accept(File dir, String name) {
									return name.endsWith(fileExtension != null
											? fileExtension : DEFAULT_FILE_EXTENSION); } }))
						INPUT_FILES.add(midiFile);
				else {
					System.out.println("Directory '"+midiDir.getAbsolutePath()+"' does not exist or is not a directory. Exit.");
					System.exit(1);
				}
				if (INPUT_FILES.isEmpty())
					System.out.println("Directory "+midiDirStr+" is empty (No .mid/.midi file found).");
			}
		}
		
		// globals
		Sequencer sequencer = null;
		MidiDevice midiDevice = null;
		int[] numOfMidiOn = null;
		long firstNoteOn = Long.MAX_VALUE;
		long lastNoteOff = Long.MIN_VALUE;
		String[] firstInstrument = null;
		try {
			// opening a file
			if (DEBUG_GUI_ENABLED) {
				JFileChooser chooser = new JFileChooser(new File("."));
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "MIDI Files", "mid", "midi");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					INPUT_FILES.clear();
					INPUT_FILES.add(chooser.getSelectedFile());
				} else {
					System.out.println("No file chosen. Exit.");
					System.exit(0);
				}
			}
			
			MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
			if (DEBUG_GUI_ENABLED) {
				String inputMsg = "Available MIDI Devices are:";
				for (int i = 0; i < infos.length; i++) {
					inputMsg +="\n"+(i) + ") ";
					inputMsg +=infos[i].getName() + ":   ";
					inputMsg +=infos[i].getDescription();
				}
				MIDI_DEVICE_NO = JOptionPane.showInputDialog(null, inputMsg, MIDI_DEVICE_NO);
			}
			if (MIDI_DEVICE_NO == null) {
				System.out.println("No Midi Device chosen. Exit.");
				System.exit(0);
			} else {
				int midiDevNumber = Math.min(infos.length-1, Math.max(0, Integer.parseInt(MIDI_DEVICE_NO)));
				MidiDevice.Info chosenInfo = infos[midiDevNumber];
				System.out.println("Chosen midi device: ("+midiDevNumber+") "+chosenInfo.getName());
				midiDevice = MidiSystem.getMidiDevice(chosenInfo);
				midiDevice.open();
			}
			
			// convert multiple files
			int infileSize = INPUT_FILES.size();
			for (int infileIdx=0; infileIdx<infileSize; infileIdx++) {
				File midiFile = INPUT_FILES.get(infileIdx);
				System.out.println("## Converting to audio ("+(infileIdx+1)+"/"+infileSize+"): "
						+midiFile.getAbsolutePath()+" ("+new Date()+")");
				
				// init midi system
				sequencer = MidiSystem.getSequencer(false);
				sequencer.open();
				sequencer.getTransmitter().setReceiver(midiDevice.getReceiver());
				Sequence seq = MidiSystem.getSequence(midiFile);
		        sequencer.setSequence(seq);
				
				// MIDI file info
		        System.out.println("Analysing song..");
		        numOfMidiOn = new int[seq.getTracks().length];
				firstNoteOn = Long.MAX_VALUE;
				lastNoteOff = Long.MIN_VALUE;
		        firstInstrument = new String[seq.getTracks().length];
		        for (int track=0; track<seq.getTracks().length; track++) {
		        	numOfMidiOn[track] = 0;
		        	firstInstrument[track] = track==9 ? "Drums" : null;
		        	for (int i=0; i<seq.getTracks()[track].size(); i++) {
		        		MidiEvent event = seq.getTracks()[track].get(i);
		        		MidiMessage msg = event.getMessage();
		        		if (msg instanceof ShortMessage) {
		                    ShortMessage sm = (ShortMessage) msg;
		                    if (sm.getCommand() == ShortMessage.NOTE_ON) {
		                    	// collect note_on event
		                    	numOfMidiOn[track] += 1;
		                    	if (event.getTick() < firstNoteOn)
		                    		firstNoteOn = event.getTick();
		                    } else if (sm.getCommand() == ShortMessage.NOTE_OFF) {
		                    	if (event.getTick() > lastNoteOff)
		                    		lastNoteOff = event.getTick();
		                    } else if (sm.getCommand() == ShortMessage.PROGRAM_CHANGE && firstInstrument[track] == null){
		                    	// collect instrument changes
		                    	firstInstrument[track] = MidiDictionary.INSTRUMENT_BYTE_TO_STRING.get((byte)sm.getData1());
		                	}
		        		}
		        	}
		        	if (numOfMidiOn[track] != 0)
		        		System.out.println("track="+track
		        				+", "+"#midiOn="+numOfMidiOn[track]
			        			+", "+"instr.name="+firstInstrument[track]);
		        }
		        System.out.println("music starts at tick="+firstNoteOn);
			
	        	// #### Playback ####
				System.out.println("# Playback full song starts..");
				doRecording(midiFile.getPath(), sequencer, firstNoteOn, lastNoteOff);
				System.out.println("Recording finished.");
			    
		        if (sequencer != null)
					sequencer.close();
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
	
	/**
	 * Manages the recording of the midi file including playback, compression and cutting
	 * @param folder The destination folder of the audio files
	 * @param filename The audio file name
	 * @param sequencer The midi sequencer to be used
	 * @param firstNoteOn The silence offset that can be skipped at the beginning
	 */
	public static void doRecording(String path, Sequencer sequencer, long firstNoteOn, long lastNoteOff) {
	
		int dotPos = path.lastIndexOf('.');
		if (dotPos > 0)
			path = path.substring(0,dotPos);
		
		File wavfile = new File(path+".wav");
		sequencer.setTickPosition(firstNoteOn);
		
		final long lengthInMicros = sequencer.getMicrosecondLength();
		final long startPosInMicros = sequencer.getMicrosecondPosition();
		final float startPosInSecs = (float)(startPosInMicros)/1000000.f;
		System.out.println("music starts at sec="+startPosInSecs);
		
		ASCIIProgressBar progressBar = new ASCIIProgressBar(50, 0, lengthInMicros/1000, true);
		progressBar.setPrintStream(System.out);
		progressBar.init();
		
		// TODO works for now, should be replaced by SoX 
		AudioRecorder.initFile(wavfile);
		AudioRecorder.startRecording();
		
		sequencer.start();
		long startTime = System.currentTimeMillis();
		
		progressBar.update(sequencer.getMicrosecondPosition()/1000);
		sleep(1000);
		// in DEBUG mode only record first two second
		if (!DEBUG_RECORDINGS_ENABLED)
			while (sequencer.isRunning() && sequencer.getTickPosition() < lastNoteOff+10)
			{
				progressBar.update(sequencer.getMicrosecondPosition()/1000);
				sleep(1000);
			}
		System.out.println();
		sequencer.stop();
		
		sleep(2000);
		AudioRecorder.stopRecording();
		System.out.println("Recorded "+wavfile.getAbsolutePath()+"("+(System.currentTimeMillis()-startTime)+"ms)"
				+ (DEBUG_RECORDINGS_ENABLED ? " !DEBUG RECORDINGS ENABLED!" : ""));
		sleep(1000);
		
		// do trimming (for perfect onset annotations)
		SoxWrapper.trim(wavfile, startPosInSecs);
		
		// do compression
		LameWrapper.removeWavAfter(DELETE_RAW);
		if (COMPRESS)
			LameWrapper.compress(wavfile, LAME_ARGS);
	}

	/**
	 * Wraps System.sleep(millis) to avoid try/catch block every time
	 * @param millis time to sleep in milli seconds
	 */
	public static void sleep(long millis) {try {Thread.sleep(millis);} catch (Exception e) {}}

	/**
	 * Prints the help document to standard system stream
	 */
	private static void printHelp() {
		System.out.println("Usage: java SamplingRoboter [options]...\n" +
				"\n"+
				"This program converts audio from midifiles via an input/output cable loop.\n" +
				"\n" +
				"Options:\n" +
				"--midifile=<file>      Convert this midifile to audio.\n" +
				"--mididir=<dir>        Convert all midifiles in specified directory (will be ignored if --midifile is used).\n" +
				"--midiext=<extension>  Uses all files with given extension, may be used to choose multitrack.\n" +
				"\n" +
				"-h, --help             Prints help and infos about available MIDI devices.\n" +
				"-v, --version          Prints version.\n" +
				"\n" +
				"--mididevice=<No>      The mididevice to use (default is 0).\n" +
				"--sox-path=<path>      Explicit location of SoX executable.\n" +
				"--lame-path=<path>     Explicit location of Lame executable.\n" +
				"--lame-options=<list>  List of arguments passed to the lame encoder (overwrites defaults).\n"+
				"--compress             Compress audio using lame (must be installed or locally executable).\n" +
				"--delete-wav           Delete raw wavs after compression.\n" +
				"\n" +
				"--debug-gui            Using simple GUI for choosing file and MIDI device.\n" +
				"--debug-recordings     Only recording first seconds of the tracks.\n"+
				"\n"+
				"Recommended usage for huge databases:\n   java SamplingRoboter --mididir=<dir> --compress --delete-wav\n"+
				"\n");
	}

}
