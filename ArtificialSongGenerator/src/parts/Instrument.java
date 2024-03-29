package parts;

import java.util.LinkedList;
import java.util.List;

import org.jfugue.theory.Note;

import asglib.MidiDictionary;
import main.Config;

	/**
	 * @author Fabian Ostermann (fabian.ostermann@udo.edu)
	 */
	 
public class Instrument implements Comparable<Instrument> {

	private String name;
	private String sampler;
	private String midi;
	
	private Note lowestNote = new Note(0);
	private Note highestNote = new Note(127);
	
	private static List<Instrument> instrumentPool = new LinkedList<>();
	
	/**
	 * Creates a new instrument. If an instrument with that name
	 * 	does already exist, that instrument is returned.
	 * @param name Synthesizer name is distinct id
	 * @param midi The midi instrument used for demo midi files
	 * @param lowestNote The lowest note, that is possible to play on this instrument
	 * @param highestNote The highest note, that is possible to play on this instrument
	 * @return
	 */
	public static Instrument findOrCreate(String name) {
		if (name == Config.DEMO_SUFFIX)
			System.out.println("WARNING: Instrument called '"+Config.DEMO_SUFFIX+"' will overwrite Demo midi file.");
		Instrument candidate = new Instrument(name);
		int index = instrumentPool.indexOf(candidate);
		if (index >= 0)
			return instrumentPool.get(index);
		else {
			instrumentPool.add(candidate);
			return candidate;
		}
	}
	
	public static Instrument find(String name) {
		for (Instrument instrument : instrumentPool)
			if (instrument.getName().equals(name))
				return instrument;
		return null;
	}
	
	/**
	 * @return An array containing all instruments created for the current song
	 */
	public static Instrument[] getPool() {
		return instrumentPool.toArray(new Instrument[]{});
	}
	
	private Instrument(String name) {
		if (name == null)
			throw new NullPointerException("no name given for instrument");
		this.name = name;
		setSampler(name);
		setMidiString(name);
	}
	
	@Override
	public int compareTo(Instrument o) {
		return this.getName().compareTo(o.getName());
	}
	
	@Override
	public boolean equals(Object o) {
		return o.getClass().equals(this.getClass())
			? this.compareTo((Instrument)o) == 0
			: false;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		String s = getName()+Config.ATTR_OPEN+lowestNote+"-"+highestNote;
		if (!getSampler().equals(getName()))
			s += ","+getSampler();
		if (!getMidiString().equals(getName()))
			s += ","+getMidiString();
		return s + Config.ATTR_CLOSE;
	}

	/** Getter & Setter */
	public void setSampler(String sampler) {
		if (sampler != null) this.sampler = sampler; }
	public String getSampler() { return sampler; }
	
	public void setMidiString(String midi) {
		if (midi != null) this.midi = midi; }
	public String getMidiString() { return midi; }
	
	public void setLowestNote(Note lowestNote) {
		if (lowestNote != null) this.lowestNote = lowestNote; }
	public Note getLowestNote() { return lowestNote; }
	
	public void setHighestNote(Note highestNote) {
		if (highestNote != null) this.highestNote = highestNote; }
	public Note getHighestNote() { return highestNote; }



	
}
