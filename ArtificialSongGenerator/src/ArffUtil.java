import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;


public class ArffUtil {
	
	public static void saveSongStructureToArff(String id, Songpart[] songStructure, File file) throws IOException {
		BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, // create if not exists
				StandardOpenOption.TRUNCATE_EXISTING, // clear to zero length
				StandardOpenOption.WRITE); // grant write access
		
		printHeader(writer, id, songStructure.length+1); // plus one for 'end' value
		
		float secondCounter = 0.f;
		for (int i=0; i<songStructure.length; i++) {
			printDataEntry(writer, secondCounter, songStructure[i].mark);
			secondCounter += songStructure[i].getLengthInSeconds();
		}
		printDataEntry(writer, secondCounter, "end");
		
		
		writer.close();
	}

	private static void printDataEntry(BufferedWriter writer, float time, String value) throws IOException {
		writer.write(""+time);
		writer.write(", ");
		writer.write("'"+value+"'");
		writer.newLine();
	}

	private static void printHeader(BufferedWriter writer, String id, int rows) throws IOException {
		writer.write("@RELATION 'Annotation "+id+"'"); writer.newLine();
		writer.write("%rows="+rows); writer.newLine();
		writer.newLine();
		writer.write("%columns=2"); writer.newLine();
		writer.newLine();
		writer.write("@ATTRIBUTE '"+id+": start time in s' NUMERIC"); writer.newLine();
		writer.write("@ATTRIBUTE '"+id+": value' STRING"); writer.newLine();
		writer.newLine();
		writer.write("@DATA"); writer.newLine();
	}
	
}
