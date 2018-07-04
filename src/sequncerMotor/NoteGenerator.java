package sequncerMotor;

import java.util.Random;

public class NoteGenerator {

	Note[] notes = new Note[8];

	public Note getRandomNote() {
		Random rn = new Random();
		int randomNr = rn.nextInt(8);
		return notes[randomNr];
	}
}
