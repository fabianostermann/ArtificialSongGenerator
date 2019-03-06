import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;


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
			"instruments' STRING",
		};
		
		printHeader(writer, id, attributes, songStructure.length+1); // plus one for 'end' value
		
		float secondCounter = 0.f;
		for (int i=0; i<songStructure.length; i++) {
			printDataEntry(writer,
					secondCounter,
					songStructure[i].mark,
					songStructure[i].tempo,
					songStructure[i].melodyInstrument,
					songStructure[i].chordInstrument);
			secondCounter += songStructure[i].getLengthInSeconds();
		}
		printDataEntry(writer,
				secondCounter,
				"end",
				songStructure[songStructure.length-1].tempo,
				"-");
		
		
		writer.close();
	}

	private static void printDataEntry(BufferedWriter writer, float time, String value, int tempo, String... instruments) throws IOException {
		writer.write(""+time);
		writer.write(NEXT);
		writer.write("'"+value+"'");
		writer.write(NEXT);
		writer.write(""+tempo);
		writer.write(NEXT);
		writer.write("'");
		for (int i=0; i<instruments.length-1; i++) {
			writer.write(instruments[i]+STR_DELIM);
		}
		writer.write(instruments[instruments.length-1]+"'");
		writer.newLine();
	}

	private static void printHeader(BufferedWriter writer, String id, String[] attributes, int rows) throws IOException {
		writer.write("@RELATION 'Annotation "+id+"'"); writer.newLine();
		writer.write("%rows="+rows); writer.newLine();
		writer.newLine();
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
