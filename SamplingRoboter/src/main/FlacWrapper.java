package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlacWrapper {
	
	public static String FLAC_PROGRAM = "flac";
	private static final String PROCESS_INDICATOR = "flac>>> ";
	
	private static boolean removeWav = false;
	private static int QUALITY = 8;

	private static List<String> defaultArgs = Arrays.asList("-"+QUALITY);
	
	/**
	 * Executes local or global installation of flac encoder on given wav file
	 * @param file the file that is converted
	 * @param optArgs an optional list of arguments
	 * --- if given this will overwrite the default args
	 * --- if NULL default args are used
	 */
	public static void compress(File file, List<String> optArgs) {
	    Process p;
	    try {
			List<String> pargs = new ArrayList<>();
			pargs.add(FLAC_PROGRAM);
			if (optArgs==null)
				// default parameters
				pargs.addAll(defaultArgs);
			else
				pargs.addAll(optArgs);
			pargs.add(file.getAbsolutePath());
			
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
				System.out.println("Something went wrong using '"+FLAC_PROGRAM+"'");

			String pathStr = file.getPath();
			int i = pathStr.lastIndexOf('.');
			if (i > 0) {
				pathStr = pathStr.substring(0,i);
			}
			if (p.exitValue() == 0
				&& (new File(pathStr+".flac")).exists()) {
				System.out.println("flac file succesfully generated.");
				if (removeWav) {
					System.out.println("Deleting tmp wav file: "+file.getAbsolutePath());
					Files.deleteIfExists(file.toPath());
				}
			}
			else
				System.out.println("ERROR: flac file NOT created.");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	/** if true, removes wav file if mp3 creation was successful */
	public static void removeWavAfter(boolean rm) {
		removeWav = rm;
	}

}
