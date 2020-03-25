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
import parts.SongPart;
import parts.SongPartElement;


public class ArffUtil {

	public static final String NEXT = ", ";
	public static final String STR_SEP = ",";
	public static final String STR_DELIM = "'";
	public static final String STR_OPEN = "[";
	public static final String STR_CLOSE = "]";
	
	public static void saveSongStructureToArff(String id, SongPart[] songStructure, File file) throws IOException {
		BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, // create if not exists
				StandardOpenOption.TRUNCATE_EXISTING, // clear to zero length
				StandardOpenOption.WRITE); // grant write access
		
		String[] attributes = new String[] { 
			"Start time in seconds", "NUMERIC", 
			"Mark", "STRING", 
			"Tempo", "NUMERIC",
			"Key", "STRING",
			"Instruments", "STRING",
			"Generator", "STRING",
		};
		
		printHeader(writer, id, attributes, songStructure.length+1); // plus one for 'end' value
		
		float secondCounter = 0.f;
		for (int i=0; i<songStructure.length; i++) {

			List<String> instruments = new LinkedList<>();
			List<String> generators = new LinkedList<>();
			for (SongPartElement element : songStructure[i].elements) {
				instruments.add(element.getInstrument().getName());
				generators.add(element.getName());
			}
			if (songStructure[i].drums != null) {
				instruments.add("Drums");
			}
			
			printDataEntry(writer,
					secondCounter,
					songStructure[i].mark,
					songStructure[i].tempo,
					songStructure[i].key,
					instruments.toArray(new String[]{}),
					generators.toArray(new String[]{})
			);
			secondCounter += songStructure[i].getLengthInSeconds();
		}
		printDataEntry(writer,
				secondCounter,
				"end",
				0,
				null,
				new String[] {},
				new String[] {}
		);
		
		writer.flush();
		writer.close();
	}

	private static void printDataEntry(BufferedWriter writer, float time, String mark, int tempo, Key key, String[] instruments, String[] generators) throws IOException {
		writer.write(""+time);
		writer.write(NEXT);
		writer.write(STR_DELIM+mark+STR_DELIM);
		writer.write(NEXT);
		writer.write(""+tempo);
		writer.write(NEXT);
		writer.write(STR_DELIM+(key!=null ? key.getKeySignature() : "")+STR_DELIM);
		writer.write(NEXT);
		writer.write(STR_DELIM+STR_OPEN);
		for (int i=0; i<instruments.length; i++) {
			writer.write(instruments[i]);
			if (i<instruments.length-1)
				writer.write(STR_SEP);
		}
		writer.write(STR_CLOSE+STR_DELIM);
		writer.write(NEXT);
		writer.write(STR_DELIM+STR_OPEN);
		for (int i=0; i<generators.length; i++) {
			writer.write(generators[i]);
			if (i<generators.length-1)
				writer.write(STR_SEP);
		}
		writer.write(STR_CLOSE+STR_DELIM);
		writer.newLine();
	}

	private static void printHeader(BufferedWriter writer, String id, String[] attributes, int rows) throws IOException {
		writer.write("@RELATION "+STR_DELIM+"Annotation "+id+STR_DELIM); writer.newLine();
		writer.newLine();
		writer.write("%rows="+rows); writer.newLine();
		writer.write("%columns="+attributes.length); writer.newLine();
		writer.newLine();
		for (int i=0; i<attributes.length; i+=2) {
			writer.write("@ATTRIBUTE "+STR_DELIM+attributes[i]+STR_DELIM+" "+attributes[i+1]); 
			writer.newLine();
		}
		writer.newLine();
		writer.write("@DATA"); writer.newLine();
	}
	
}
