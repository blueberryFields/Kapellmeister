package note;

/**
 * An enumeration of the different states a note can be in. ON means the note
 * will be sent to the reciever and the previous note will be killed. HOLD means
 * the note will not be sent and the previous note wont be killed. OFF means the
 * note wont be sent but the previous note will be killed.
 */

public enum NoteOn {
	ON, HOLD, OFF;
}
