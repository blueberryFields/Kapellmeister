package sequncerMotor;

import java.util.Random;

public class NoteGenerator {

	MidiNote[] notes = new MidiNote[8];

	public MidiNote getRandomNote() {
		Random rn = new Random();
		int randomNr = rn.nextInt(8);
		return notes[randomNr];
	}
}
