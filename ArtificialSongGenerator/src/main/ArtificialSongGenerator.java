package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;

import asglib.ArgsUtil;
import info.Version;
import parts.Songpart;
import util.ArffUtil;
import util.Random;

/**
 * This unit creates pop music style midi files.
 * The artistic quality of the music is improved in respect to the original random only concept.
 * Especially the melody generator MelodyBow.java creates nice little and variation-full ideas.
 * Also a rich annotation file (.arff) is produced.
 * 
 * @author Fabian Ostermann (fabian.ostermann@udo.edu)
 */

public class ArtificialSongGenerator {

	public static ArgsUtil argsUtil = null;
	
	public static Songpart[] songparts = null;
	public static Songpart[] songStructure = null;
	public static Pattern theSong;
	
	public static boolean VERBOSE_MODE = false;
	
	/**
	 * @param args Set '--help' to print full options list.
	 */
	public static void main(String[] args) {
		argsUtil = new ArgsUtil(args);

		// ######################
		// ### SYSTEM STARTUP ###
		
		// both help option installed for clueless users
		if (argsUtil.check("--help") || argsUtil.check("-h")) {
			printHelp();
			System.exit(0);
		}
		System.out.println("### ArtificialSongGenerator started ### ("+new Date()+")");
		// state version
		if (argsUtil.check("--version") || argsUtil.check("-v")) {
			System.out.println(Version.VERSION);
			System.exit(0);
		}
		if (argsUtil.check("--verbose")) {
			VERBOSE_MODE = true;
		}
		

		// choose config file
		String configFilename = argsUtil.get("--config=");
		if (configFilename != null)
			Config.CONFIG_FILENAME = configFilename;
		
		// load config (must load from file first, gets overwritten by loadDefaults)
		if (argsUtil.check("--config-dummy")) {
			Config.loadDefaults();
			try { Config.createDummyFile();
			} catch (IOException e) {
				System.out.println("There was a problem saving the file " +
						"'"+Config.DUMMY_FILENAME+"': "+e.getMessage());
			}
			System.out.println("Created config dummy file.");
			System.exit(0);
		}
		try { Config.loadFromFile();
		} catch (IOException e) {
			System.out.println("There was a problem reading the dummy file " +
					"'"+Config.CONFIG_FILENAME+"': "+e.getMessage());
		}
		Config.loadDefaults();
		
		// change default midi file name
		String title = argsUtil.get("--title=");
		if (title != null)
			Config.GET.THESONG_TITLE = title;
		// change default output directory
		String dir = argsUtil.get("--dir=");
		if (dir != null)
			Config.GET.OUTPUT_DIR = dir;
		
		// print instrument to channel map
		if (argsUtil.check("--channel-map")) {
			Map<Integer, String> channelMap = new HashMap<>();
			for (String instrName : Config.GET.MELODY_INSTRUMENTS)
				channelMap.put(Config.GET.getMelodyChannel(instrName), instrName);
			for (String instrName : Config.GET.CHORD_INSTRUMENTS)
				channelMap.put(Config.GET.getChordsChannel(instrName), instrName);
			for (String instrName : Config.GET.BASS_INSTRUMENTS)
				channelMap.put(Config.GET.getBassChannel(instrName), instrName);
			channelMap.put(9, "Drums");
			for (int ch : channelMap.keySet())
				System.out.println(Config.COMMENT+" "+(ch+1)+" -> "+channelMap.get(ch));
			System.exit(0);
		}
		
		// #################################
		// ### GENERATION PROCESS STARTS ###
		
		// generate some songparts
		songparts = new Songpart[Config.GET.Nof_DIFFERENT_SONGPARTS];
		for (int i = 0; i < songparts.length; i++) {
			songparts[i] = Songpart.newRandomSongpart();
		}
		
		// random formation of songparts to a song
		float songTime = 0;
		ArrayList<Songpart> songStructureList = new ArrayList<>();
		songStructureList.add(songparts[0]); // add first songpart at beginning
		songTime += songparts[0].getLengthInSeconds();
		Songpart nextSongpart;
		// fill to minimum length
		while (songTime < Config.GET.MIN_LENGTH_IN_SEC) {
			nextSongpart = songparts[Random.rangeInt(0, songparts.length)];
			songStructureList.add(nextSongpart);
			songTime += nextSongpart.getLengthInSeconds();
		}
		nextSongpart = songparts[Random.rangeInt(0, songparts.length)];
		// add some more songparts, but at most to maximum length
		while (songTime + nextSongpart.getLengthInSeconds() <= Config.GET.MAX_LENGTH_IN_SEC
				&& Random.nextBoolean(0.75f)) {
			songStructureList.add(nextSongpart);
			songTime += nextSongpart.getLengthInSeconds();
			nextSongpart = songparts[Random.rangeInt(0, songparts.length)];
		}
		songStructure = songStructureList.toArray(new Songpart[songStructureList.size()]);
		if (VERBOSE_MODE) {
			String songStructureStr = "";
			for (Songpart part : songStructure)
				songStructureStr += part.mark;
				System.out.println("Song structure: "+songStructureStr + " ("+songTime+"s)");
		}
		
		// build pattern from song structure
		theSong = new Pattern();
		for (int i=0; i<songStructure.length; i++) {
			theSong.add(songStructure[i]);
		}
		if (argsUtil.check("--print-staccato")) {
			// staccato code is formatted by line breaking on tempo marks (e.g. T85)
			System.out.println("Staccato code:\n "+theSong.toString().replaceAll(" T", " \nT"));
		}
		
		// save song to file
		String midiFileStr = Config.GET.OUTPUT_DIR+File.separator+Config.GET.THESONG_TITLE+Config.GET.MIDI_SUFFIX;
		try {
			MidiFileManager.savePatternToMidi(theSong, new File(midiFileStr));
			System.out.println("Created song file '" + midiFileStr + "'");
		} catch (IOException e) {
			System.out.println("There was a problem saving the file '"+midiFileStr+"': "+e.getMessage());
		}
		
		// save .arff annotation file with segments to file
		String arffFileStr = Config.GET.OUTPUT_DIR+File.separator+Config.GET.THESONG_TITLE+Config.GET.ARFF_SUFFIX;
		try {
			ArffUtil.saveSongStructureToArff(Config.GET.THESONG_TITLE, songStructure, new File(arffFileStr));
			System.out.println("Created annotation file '" + arffFileStr + "'");
			if (VERBOSE_MODE) // print arff annotation file to standard out
				try (BufferedReader br = new BufferedReader(new FileReader(arffFileStr))) {
					   String line;
					   boolean started = false;
					   while ((line = br.readLine()) != null) {
						   if (line.startsWith("@DATA")) {
							   started = true;
							   System.out.println("[... skipping header ...]");
						   }
						   if (started)
							   System.out.println(line);
					   }
					   System.out.println("EOF");
					}
		} catch (IOException e) {
			System.out.println("There was a problem saving the file '"+arffFileStr+"': "+e.getMessage());
		}

		if (argsUtil.check("--play")) {
			System.out.print("Playing.. ");
			Player player = new Player();
		    player.play(theSong);
		    System.out.println("Thank you!");
		}
	}


	/**
	 * Prints the help document to standard system stream
	 */
	private static void printHelp() {
		System.out.println("Usage: java ArtificialSongGenerator [options]...\n" +
				"\n"+
				"This program generates a random midi song file\n" +
				"containing random songparts with melody and chords.\n" +
				"Additionally an arff file with annotated segment borders is produced.\n" +
				"\n" +
				"Options:\n" +
				"-h, --help             Prints this.\n" +
				"-v, --version          Prints version.\n" +
				"--play                 Song is played back after generation.\n" +
				"--print-staccato       Prints the Staccato code (JFugue's music syntax).\n" +
				"--verbose              Prints generation infos like the structure of the song (e.g. AABACB) and length in second.\n" +
				"--title=<title>        Specifies the output title id.\n" +
				"--dir=<dir>            Specifies directory for output files " +
					"(ATTENTION: fails if directory does not exist).\n" +
				"--config=<configfile>  Specifies the config file to be used. Default is '"+Config.CONFIG_FILENAME+"'\n" +
				"--config-dummy         Creates a config overview dummy file.\n" +
				"--channel-map          Prints the current mapping of instruments to channels.\n"
				);
	}

}
