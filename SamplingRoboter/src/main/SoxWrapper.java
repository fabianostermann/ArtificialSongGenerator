package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SoxWrapper {
	
	public static String SOX_PROGRAM = "sox";
	private static final String PROCESS_INDICATOR = "sox>>> ";
	
	/**
	 * Executes local or global installation of SoX on a given wav file
	 * @param file the file that is converted
	 */
	public static void trim(File file, float startTimeSecs) {
	    Process p;
	    try {
			File tmpFile = new File(file.getAbsolutePath()+"_tmp"+System.currentTimeMillis()+".wav");
			
			List<String> pargs = new ArrayList<>();
			pargs.add(SOX_PROGRAM);
			pargs.add(file.getAbsolutePath()); // input file
			pargs.add(tmpFile.getAbsolutePath()); // output file
			pargs.addAll(Arrays.asList(
					// verbosity (1: failures, 2: warnings, 3: processing details, 4-6: debugs)
					"-V3",
					// remove silence until sound louder than 0.1% and longer than 1.0sec
					"silence", "1", "1.0", "0.1%",
					// add silence that was skipped on recording to front of audio
					"pad", ""+startTimeSecs, "0"));
			
			ProcessBuilder pb = new ProcessBuilder(pargs);
			pb.redirectErrorStream(true);
			pb.redirectOutput(Redirect.PIPE); // INHERIT|PIPE
			
			// print full command
			System.out.print("EXEC: ");
			for (String arg : pargs)
				System.out.print(arg+" ");
			System.out.println();
			
			// run command
			p = pb.start();
	        			
	        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line = ""; 
	        while ((line = reader.readLine())!= null) {
	        	System.out.println(PROCESS_INDICATOR+line);
	        }
	        
	        // wait for termination
	        p.waitFor();
	        
	        if (p.exitValue() != 0)
				System.out.println("Something went wrong using '"+SOX_PROGRAM+"'");

			String fileStr = file.getName();
			int i = fileStr.lastIndexOf('.');
			if (i > 0) {
				fileStr = fileStr.substring(0,i);
			}
			if (p.exitValue() == 0
				&& tmpFile.exists()) {
				System.out.println("Deleting tmp wav file: "+tmpFile.getAbsolutePath());
				Files.copy(tmpFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			else {
				System.out.println("wavFile not converted by SoX");
			}
			Files.deleteIfExists(tmpFile.toPath());

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
