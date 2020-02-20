package main;

import java.io.File;
import java.io.IOException;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;

import asglib.ArgsUtil;
import info.Version;
import parts.Songpart;
import util.ArffUtil;
import util.Random;


public class ArtificialSongGenerator {

	public static ArgsUtil argsUtil = null;
	
	public static Songpart[] songparts = null;
	public static Songpart[] songStructure = null;
	public static Pattern theSong;
	
	/**
	 * This unit creates pop music style midi files.
	 * The artistic quality of the music is poor.
	 * It's based on simple guided random choices only.
	 * A list of song segments is produced as well.
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
		// state version
		if (argsUtil.check("--version") || argsUtil.check("-v")) {
			System.out.println(Version.VERSION);
			System.exit(0);
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
						"'"+Config.CONFIG_FILENAME+Config.DUMMY_SUFFIX+"': "+e.getMessage());
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
		
		// #################################
		// ### GENERATION PROCESS STARTS ###
		
		// generate some songparts
		songparts = new Songpart[Config.GET.Nof_DIFFERENT_SONGPARTS];
		for (int i = 0; i < songparts.length; i++) {
			songparts[i] = Songpart.newRandomSongpart();
		}
		
		// random formation of songparts to a song
		// NOTE: if EXPLOIT_INSTRUMENTS is true and Nof_DIFFERENT_SONGPARTS < number of instruments,
		// then it is guaranteed, that each songpart has different instrumentation
		String songStructureStr = "";
		songStructure = new Songpart[Config.GET.Nof_SONGPARTS_IN_SONG];
		for (int i=0; i<songStructure.length; i++) {
			Songpart nextSongpart = songparts[Random.rangeInt(0, songparts.length)];
			if (nextSongpart.mark == null)
				nextSongpart.mark = Songpart.nextDefaultMark();
			songStructure[i] = nextSongpart;
			songStructureStr += nextSongpart.mark;
		}
		if (argsUtil.check("--print-structure")) {
			System.out.println("Song structure: "+songStructureStr);
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
				"--print-structure      Prints the structure of the song (e.g. AABACB).\n" +
				"--title=<title>        Specifies the output title id.\n" +
				"--dir=<dir>            Specifies directory for output files " +
					"(ATTENTION: fails if directory does not exist).\n" +
				"--config=<configfile>  Specifies the config file to be used. Default is '"+Config.CONFIG_FILENAME+"'\n" +
				"--config-dummy         Creates a config overview dummy file.\n");
	}

}
