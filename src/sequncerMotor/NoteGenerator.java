package sequncerMotor;

import java.util.Random;

public class NoteGenerator {

    int[] notes = new int[8];

    public int getRandomNote() {
	Random rn = new Random();
	int randomNr = rn.nextInt(8);
	return notes[randomNr];
    }
}
