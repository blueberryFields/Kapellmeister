package note;

public class Note {

	private int midiNote;
	private int velo;
	private String note;
	private NoteOn noteOn;
	private int holdNote = -1;
	private NoteToMidiConverter noteToMidiConverter = new NoteToMidiConverter();

	// konstruktor
	public Note() {
		setMidiNote(48);
		setVelo(100);
		setNote("C3");
		setNoteOn(NoteOn.ON);

	}

	public Note(int midiNote, int velo, String note) {
		setMidiNote(midiNote);
		setVelo(velo);
		setNote(note);
		setNoteOn(NoteOn.ON);
	}

	public Note(int velo, String note) {
		setVelo(velo);
		setNote(note);
		setMidiNote(noteToMidiConverter.get(note));
		setNoteOn(NoteOn.ON);
	}
	
	public Note(int velo, String note, NoteOn value) {
		setVelo(velo);
		setNote(note);
		setMidiNote(noteToMidiConverter.get(note));
		setNoteOn(value);
	}

	public void setNoteOn(NoteOn value) {
		noteOn = value;
	}

	public NoteOn getNoteOn() {
		return noteOn;
	}

	public int getMidiNote() {
		return midiNote;
	}

	public void setMidiNote(int note) {
		this.midiNote = note;
	}

	public int getVelo() {
		return velo;
	}

	public void setVelo(int velo) {
		this.velo = velo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void changeNote(String note) {
		setNote(note);
		setMidiNote(noteToMidiConverter.get(note));
	}

	public void setHoldNote(int holdValue) {
		this.holdNote = holdValue;
	}

	public int getHoldNote() {
		return holdNote;
	}

	public void initHoldValue() {
		holdNote = -1;
	}

	@Override
	public String toString() {
		return note;

	}
}
