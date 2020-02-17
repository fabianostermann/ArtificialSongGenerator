import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class LameWrapper {

	private static final String PROCESS_INDICATOR = ">>> ";
	
	private static boolean removeWav = false;
	private static int VBR = 2;
	
	/** execute lame on file (linux only for now) */
	public static void execute(File file) {
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
	    	String execCommand = lameProgram+" -V "+VBR+" "+file.getAbsolutePath();
	    	System.out.println("Start external process:");
			System.out.println(PROCESS_INDICATOR+execCommand);
	        p = Runtime.getRuntime().exec(execCommand);
	        
	        BufferedReader reader = 
	                        new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line = "";           
	        while ((line = reader.readLine())!= null) {
	            System.out.println(PROCESS_INDICATOR+line);
	        }
	        
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
