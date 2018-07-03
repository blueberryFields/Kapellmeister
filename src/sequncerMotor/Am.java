package sequncerMotor;

public class Am extends NoteGenerator {

	public Am() {
		notes[0] = new MidiNote(57, 100, "A3");
		notes[1] = new MidiNote(59, 100, "B3");
		notes[2] = new MidiNote(60, 100, "C4");
		notes[3] = new MidiNote(62, 100, "D4");
		notes[4] = new MidiNote(64, 100, "E4");
		notes[5] = new MidiNote(65, 100, "F4");
		notes[6] = new MidiNote(67, 100, "G4");
		notes[7] = new MidiNote(69, 100, "A4");
	}
}
