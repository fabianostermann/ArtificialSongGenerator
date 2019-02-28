package Demo;

/*
 * JFugue, an Application Programming Interface (API) for Music Programming
 * http://www.jfugue.org
 *
 * Copyright (C) 2003-2014 David Koelle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.jfugue.pattern.PatternProducer;
import org.jfugue.player.Player;
import org.jfugue.theory.ChordProgression;

/**
 * DEMO
 * This file shows how a created song (MUSIC_TO_OUTPUT) is stored in a file
 * and later played back from it.
 * @author oyster
 *
 */

public class MidiFileManagerDemo {
	public static PatternProducer MUSIC_TO_OUTPUT = new Pattern("C D E F G A B");
	public static String OUTPUT_FILENAME = "example.mid";
	public static String INPUT_FILENAME = "example.mid";
	
	public static void main(String[] args) {

		MUSIC_TO_OUTPUT = new ChordProgression("I II III IV V VI VII").setKey("B").getPattern().setInstrument("guitar").setTempo(100);

		
		DemoPrint.start("Examples saving and loading MIDI files");
				
		// This example shows how to save the MIDI output from JFugue to a file.
		try {
			MidiFileManager.savePatternToMidi(MUSIC_TO_OUTPUT, new File(OUTPUT_FILENAME));
			DemoPrint.step("Created the file \"" + OUTPUT_FILENAME + "\" with the music " + MUSIC_TO_OUTPUT); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// This example shows how to open a MIDI file and get a Pattern from it
		try {
			Pattern pattern = MidiFileManager.loadPatternFromMidi(new File(INPUT_FILENAME));
			DemoPrint.step("Converted the MIDI file \"" + INPUT_FILENAME + "\" to the following Pattern: " + pattern);

			DemoPrint.start("Play music from file:");
		    Player player = new Player();
		    player.play(pattern);
			DemoPrint.start("Done.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		

	}

}