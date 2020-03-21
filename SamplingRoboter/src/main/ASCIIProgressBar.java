package main;

import java.io.PrintStream;

public class ASCIIProgressBar {

	private int size = 50;
	private long start = 0;
	private long end = 100;
	
	private boolean printMillis = false;
	
	private PrintStream out = null;
	
	public void setPrintStream(PrintStream stream) {
		this.out = stream;
	}
	
	public ASCIIProgressBar() {}
	
	public ASCIIProgressBar(int size, long start, long end, boolean printMillis) {
		this.size = size;
		this.start = start;
		this.end = end;
		this.printMillis = printMillis;
	}
	
	public String init() {
		String bar = "[";
		for (int i=0; i<size; i++)
			bar += ".";
		bar += "] ";
		
		if (printMillis) {
			bar += millisToTime(start) +" / " + millisToTime(end);
		}
		if (out != null)
			out.print(bar);
		return bar;
	}
	
	public String update(long now) {
		String bar = "\r[";
		
		// print bar
		int pos = (int)(((double)(now-start)/(double)(end-start))*(double)size);
		for (int i=0; i<size; i++)
			bar += pos>=i ? "=" : ".";
		
		bar += "] ";
		
		if (printMillis) {
			bar += millisToTime(now) +" / " + millisToTime(end);
		}
		if (out != null)
			out.print(bar);
		return bar;
	}
	
	public String millisToTime(long millis) {
		String time = "";
		int hh = (int)(millis/1000/60/60);
		int mm = (int)(millis/1000/60)-(hh*60);
		int ss = (int)(millis/1000%60);
		if (hh>0)
			time += hh+":";
		time += (mm<10?"0":"")+mm+":"+(ss<10?"0":"")+ss;
		return time;
	}
	
}
