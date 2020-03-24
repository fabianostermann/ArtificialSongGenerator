package asglib;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Logger {

	private PrintStream consoleOut;
	private PrintStream fileOut;
	
	private Logger(String name) {
		consoleOut = System.out;
		try {
			fileOut = new PrintStream(new BufferedOutputStream(new FileOutputStream(name+".log")), true);
		} catch (FileNotFoundException e) {
			System.out.println("log file cannot be created: "+e.getMessage());
			fileOut = null;
		}
	}
	
	public void print(String msg) {
		consoleOut.print(msg);
		if (fileOut != null)
			fileOut.print(msg);
	}
	
	public void println(String msg) {
		print(msg+"\n");
	}
}
