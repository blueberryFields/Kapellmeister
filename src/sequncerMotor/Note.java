package sequncerMotor;

public class Note {

	private int midiNote;
	private int velo;
	private String note;
	private NoteOnButtonEnum noteOnButton;
	private int holdNote = -1;

	// konstruktor
	public Note() {
		setMidiNote(48);
		setVelo(100);
		setNote("C3");
		setNoteOnButtonEnum(NoteOnButtonEnum.ON);
		
	}

	public Note(int midiNote, int velo, String note) {
		setMidiNote(midiNote);
		setVelo(velo);
		setNote(note);
		setNoteOnButtonEnum(NoteOnButtonEnum.ON);
	}

	public Note(int velo, String note) {
		setVelo(velo);
		setNote(note);
		setMidiNote(noteToMidiConverter(note));
		setNoteOnButtonEnum(NoteOnButtonEnum.ON);
	}
	
	public void setNoteOnButtonEnum(NoteOnButtonEnum value) {
		noteOnButton = value;
	}
	
	public NoteOnButtonEnum getNoteOnButtonEnum() {
		return noteOnButton;
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
		setMidiNote(noteToMidiConverter(note));
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

	public int noteToMidiConverter(String note) {
		switch (note) {
		case "C3":
			return 48;
		case "C#3":
			return 49;
		case "D3":
			return 50;
		case "D#3":
			return 51;
		case "E3":
			return 52;
		case "F3":
			return 53;
		case "F#3":
			return 54;
		case "G3":
			return 55;
		case "G#":
			return 56;
		case "A3":
			return 57;
		case "A#3":
			return 58;
		case "B3":
			return 59;
		case "C4":
			return 60;
		case "C#4":
			return 61;
		case "D4":
			return 62;
		case "D#4":
			return 63;
		case "E4":
			return 64;
		case "F4":
			return 65;
		case "F#4":
			return 66;
		case "G4":
			return 67;
		case "G#4":
			return 68;
		case "A4":
			return 69;
		case "A#4":
			return 70;
		case "B4":
			return 71;
		case "C5":
			return 72;
		}
		return 48;
	}

	@Override
	public String toString() {
		String s = note + ", " + midiNote + ", " + velo + "; ";
		return s;

	}
}
