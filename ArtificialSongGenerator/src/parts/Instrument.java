package parts;

import java.util.LinkedList;
import java.util.List;

import org.jfugue.theory.Note;

import asglib.MidiDictionary;

public class Instrument implements Comparable<Instrument> {

	private String name = "Default_Piano";
	private String midi = MidiDictionary.INSTRUMENT_BYTE_TO_STRING.get((byte)0);
	
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
		Instrument candidate = new Instrument(name);
		int index = instrumentPool.indexOf(candidate);
		if (index >= 0)
			return instrumentPool.get(index);
		else {
			instrumentPool.add(candidate);
			return candidate;
		}
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

	/** Getter & Setter */
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
