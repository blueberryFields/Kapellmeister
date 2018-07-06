package sequncerMotor;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class NoteGenerator {

	// String[] notes = new String[8];

	List<String> notes = new LinkedList<>();

	public Note[] getRndSequence(Note[] sequence) {
		Random rn = new Random();
		for (int i = 0; i < sequence.length; i++) {
			int randomNr = rn.nextInt(notes.size());
			sequence[i] = new Note(100, notes.get(randomNr));
		}
		return sequence;
	}

	public Note[] getRndSeqNoDuplInRow(Note[] sequence) {
		Random rn = new Random();
		Note tempNote;
		int randomNr = rn.nextInt(notes.size());
		sequence[0] = new Note(100, notes.get(randomNr));
		tempNote = sequence[0];
		notes.remove(randomNr);
		
		for (int i = 1; i < sequence.length; i++) {
			randomNr = rn.nextInt(notes.size());
			sequence[i] = new Note(100, notes.get(randomNr));
			notes.remove(randomNr);
			notes.add(tempNote.getNote());
			tempNote = sequence[i];
		}
		return sequence;
	}
}
