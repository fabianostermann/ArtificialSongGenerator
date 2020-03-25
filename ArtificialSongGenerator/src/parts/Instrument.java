package parts;

import org.jfugue.theory.Note;

import asglib.MidiDictionary;

public class Instrument {

	private String name = "Default_Piano";
	private String midi = MidiDictionary.INSTRUMENT_BYTE_TO_STRING.get((byte)0);
	
	private Note LOWEST_NOTE = new Note(0);
	private Note HIGHEST_NOTE = new Note(127);
	
	public Instrument() {}
	
	public Instrument(String name) {
		this.name = name;
	}
	
	public Instrument(String name, String midi) {
		this(name);
		if (midi != null)
			this.midi = midi;
	}
	
	public Instrument(String name, String midi, Note lowestNote, Note hightestNote) {
		this(name, midi);
		this.LOWEST_NOTE = lowestNote;
		this.HIGHEST_NOTE = hightestNote;
	}

	public String getName() {
		return name;
	}

	public String getMidiString() {
		return midi;
	}

	public Note getLOWEST_NOTE() {
		return LOWEST_NOTE;
	}

	public Note getHIGHEST_NOTE() {
		return HIGHEST_NOTE;
	}
	
}
