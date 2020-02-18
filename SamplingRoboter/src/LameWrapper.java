import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LameWrapper {

	private static final String PROCESS_INDICATOR = "lame>>> ";
	
	private static boolean removeWav = false;
	private static int VBR = 2;

	private static List<String> defaultArgs = Arrays.asList("-V", ""+VBR, "--disptime", "1");
	
	/**
	 * Executes local or global installation of lame encoder on given wav file
	 * @param file the file that is converted
	 * @param optArgs an optional list of arguments
	 * --- if given this will overwrite the default args
	 * --- if NULL default args are used
	 */
	public static void execute(File file, List<String> optArgs) {
	    Process p;
	    try {
	    	String lameProgram = "lame";
	    	if ((new File("./lame")).exists()) {
	    		lameProgram = "./lame";
				System.out.println("Found linux like local program './lame'.");
			}
			if ((new File("./lame.exe")).exists()) {
	    		lameProgram = "./lame.exe";
				System.out.println("Found windows like local program './lame.exe'.");
			}
			
			List<String> pargs = new ArrayList<>();
			pargs.add(lameProgram);
			if (optArgs==null)
				// default parameters
				pargs.addAll(defaultArgs);
			else
				pargs.addAll(optArgs);
			pargs.add("--nohist"); // never allow histogram view
			pargs.add(file.getAbsolutePath());
			ProcessBuilder pb = new ProcessBuilder(pargs);
			pb.redirectErrorStream(true);
			pb.redirectOutput(Redirect.PIPE); // INHERIT|PIPE
			
			// print full command
			System.out.print(PROCESS_INDICATOR);
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
				System.out.println("Something went wrong using '"+lameProgram+"'");

			String fileStr = file.getName();
			int i = fileStr.lastIndexOf('.');
			if (i > 0) {
				fileStr = fileStr.substring(0,i);
			}
			if (removeWav && p.exitValue() == 0
				&& (new File(file.getParent() +"//"+ fileStr+".mp3")).exists()) {
				System.out.println("Deleting tmp wav file: "+file.getAbsolutePath());
				file.delete();
			}
			else
				System.out.println("mp3 NOT created.");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	/** -V n --- quality setting for VBR.  default n=4, 0=high quality,bigger files. 9.999=smaller files */
	public static void setVBR(int V) {
		if (V < 0 || V >= 10)
			return;
		VBR = V;
	}
	
	/** current VBR setting, set with setVBR() */
	public static int getVBR() {
		return VBR;
	}
	
	/** if true, removes wav file if mp3 creation was successful */
	public static void removeWavAfter(boolean rm) {
		removeWav = rm;
	}

}
