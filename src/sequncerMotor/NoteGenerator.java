package sequncerMotor;

import java.util.Random;

public class NoteGenerator {

	String[] notes = new String[8];

	public String getRandomNote() {
		Random rn = new Random();
		int randomNr = rn.nextInt(8);
		return notes[randomNr];
	}
}
