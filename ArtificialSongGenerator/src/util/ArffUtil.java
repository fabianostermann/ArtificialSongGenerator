package util;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

import org.jfugue.theory.Key;

import main.ArtificialSongGenerator;
import parts.Songpart;


public class ArffUtil {

	public static final String NEXT = ", ";
	public static final String STR_DELIM = "|";
	
	public static void saveSongStructureToArff(String id, Songpart[] songStructure, File file) throws IOException {
		BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, // create if not exists
				StandardOpenOption.TRUNCATE_EXISTING, // clear to zero length
				StandardOpenOption.WRITE); // grant write access
		
		String[] attributes = new String[] { 
			"start time in s' NUMERIC", 
			"value' STRING", 
			"tempo' NUMERIC",
			"key' STRING",
			"instruments' STRING",
			"functions' STRING",
			"polyphonic degree' STRING",
		};
		
		printHeader(writer, id, attributes, songStructure.length+1); // plus one for 'end' value
		
		float secondCounter = 0.f;
		for (int i=0; i<songStructure.length; i++) {

			List<String> instruments = new LinkedList<>();
			List<String> functions = new LinkedList<>();
			List<Integer> polyphony = new LinkedList<>();
			if (songStructure[i].melody != null) {
				instruments.add(songStructure[i].melodyInstrument);
				functions.add("melody");
				polyphony.add(1);
			}
			if (songStructure[i].chords != null) {
				instruments.add(songStructure[i].chordInstrument);
				functions.add("chords");
				polyphony.add(3);
			}
			if (songStructure[i].arpeggio != null) {
				instruments.add(songStructure[i].arpeggioInstrument);
				functions.add("arpeggios");
				polyphony.add(1);
			}
			if (songStructure[i].bass != null) {
				instruments.add(songStructure[i].bassInstrument);
				functions.add("bass");
				polyphony.add(1);
			}
			if (songStructure[i].drums != null) {
				instruments.add("Drums");
				functions.add("rhythm");
				polyphony.add(-1);
			}
			
			printDataEntry(writer,
					secondCounter,
					songStructure[i].mark,
					songStructure[i].tempo,
					songStructure[i].key,
					instruments.toArray(new String[]{}),
					functions.toArray(new String[]{}),
					polyphony.toArray(new Integer[]{})
			);
			secondCounter += songStructure[i].getLengthInSeconds();
		}
		printDataEntry(writer,
				secondCounter,
				"end",
				0,
				null,
				new String[] {"-"},
				new String[] {"-"},
				new Integer[] {}
		);
		
		writer.flush();
		writer.close();
	}

	private static void printDataEntry(BufferedWriter writer, float time, String mark, int tempo, Key key, String[] instruments, String[] functions, Integer[] polyphony) throws IOException {
		writer.write(""+time);
		writer.write(NEXT);
		writer.write("'"+mark+"'");
		writer.write(NEXT);
		writer.write(""+tempo);
		writer.write(NEXT);
		writer.write("'"+(key!=null ? key.getKeySignature() : "-")+"'");
		writer.write(NEXT);
		writer.write("'");
		for (int i=0; i<instruments.length; i++) {
			writer.write(instruments[i]);
			if (i<instruments.length-1)
				writer.write(STR_DELIM);
		}
		writer.write("'");
		writer.write(NEXT);
		writer.write("'");
		for (int i=0; i<functions.length; i++) {
			writer.write(functions[i]);
			if (i<functions.length-1)
				writer.write(STR_DELIM);
		}
		writer.write("'");
		writer.write(NEXT);
		writer.write("'");
		for (int i=0; i<polyphony.length; i++) {
			writer.write(""+polyphony[i]);
			if (i<polyphony.length-1)
				writer.write(STR_DELIM);
		}
		writer.write("'");
		writer.newLine();
	}

	private static void printHeader(BufferedWriter writer, String id, String[] attributes, int rows) throws IOException {
		writer.write("@RELATION 'Annotation "+id+"'"); writer.newLine();
		writer.newLine();
		writer.write("%rows="+rows); writer.newLine();
		writer.write("%columns="+attributes.length); writer.newLine();
		writer.newLine();
		for (String attribute : attributes) {
			writer.write("@ATTRIBUTE '"+id+": "+attribute); 
			writer.newLine();
		}
		writer.newLine();
		writer.write("@DATA"); writer.newLine();
	}
	
}
