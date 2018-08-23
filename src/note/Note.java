package note;

/**
 * A class that represents a note/step in pattern. Contains info on noteName,
 * midiNoteNumber, velocity, a NoteOn-enumeration that describes if the note is
 * set to ON, OFF or HOLD aswell as a holdNote which knows which note to kill if
 * the previous note was set to Hold and a noteToMidiCnverter for converting
 * from noteNames to midiNoteNumbers
 */

public class Note {

	/**
	 * the midiNoteNUmber that repesents the note
	 */
	private int midiNote;
	/**
	 * the velocity of the note
	 */
	private int velo;
	/**
	 * The name of the note, i.e. F#
	 */
	private String note;
	/**
	 * An enumeration that can be set to ON, OFF or HOLD. ON means it will be played
	 * normally, OFF means that it wont be played but the previous note will be
	 * killed, HOLD means that the current note wont be played but the previous wont
	 * be killed before next ON or OFF comes around
	 */
	private NoteOn noteOn;
	/**
	 * If the previous note were set to HOLD this is where info on which midiNote to
	 * kill when the time comes will be stored. Handled by the checkHold()-methods
	 * in the sequencerController-class
	 */
	private int holdNote = -1;
	/**
	 * This little tool will convert from noteNames to midiNotes, String to Int
	 */
	private NoteToMidiConverter noteToMidiConverter = new NoteToMidiConverter();

	/**
	 * Constructor 1. Creates a standard note(C3, velo 100, NoteOn.ON)
	 */
	public Note() {
		setMidiNote(48);
		setVelo(100);
		setNote("C3");
		setNoteOn(NoteOn.ON);

	}

	// public Note(int midiNote, int velo, String note) {
	// setMidiNote(midiNote);
	// setVelo(velo);
	// setNote(note);
	// setNoteOn(NoteOn.ON);
	// }

	/**
	 * Constructor 2. Will create a note of your choice with a velocity of your
	 * choice
	 * 
	 * @param velo
	 *            the velocity you want this note to have
	 * @param note
	 *            the note you want this note to be
	 */
	public Note(int velo, String note) {
		setVelo(velo);
		setNote(note);
		setMidiNote(noteToMidiConverter.get(note));
		setNoteOn(NoteOn.ON);
	}

	/**
	 * Constructor 3. Will create a note of your choice with the velocity of your
	 * choice and a NoteOnValue of your choice
	 * 
	 * @param velo
	 *            the velocity you want this note to have
	 * @param note
	 *            the note you want this note to be
	 * @param value
	 *            the noteOnValue you want the note to have
	 */
	public Note(int velo, String note, NoteOn value) {
		setVelo(velo);
		setNote(note);
		setMidiNote(noteToMidiConverter.get(note));
		setNoteOn(value);
	}

	// The rest is simple getters and setters and an overridden toString-method

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
