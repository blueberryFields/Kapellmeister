package sequncerMotor;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class NoteGenerator {

	List<String> notes = new LinkedList<>();

	public Note[] getRndSequence(Note[] sequence, boolean rndVeloIsChecked, int veloLow, int veloHigh) {
		Random rn = new Random();
		int velo = 100;
		for (int i = 0; i < sequence.length; i++) {
			if (rndVeloIsChecked) {
				velo = generateRndVelo(veloLow, veloHigh);
			}
			int randomNr = rn.nextInt(notes.size());
			sequence[i] = new Note(velo, notes.get(randomNr));

		}
		return sequence;
	}

	public Note[] getRndSeqNoDuplInRow(Note[] sequence, boolean rndVeloIsChecked, int veloLow, int veloHigh) {
		Random rn = new Random();
		int velo = 100;
		Note tempNote;
		int randomNr = rn.nextInt(notes.size());
		if (rndVeloIsChecked) {
			velo = generateRndVelo(veloLow, veloHigh);
		}
		sequence[0] = new Note(velo, notes.get(randomNr));
		tempNote = sequence[0];
		notes.remove(randomNr);

		for (int i = 1; i < sequence.length; i++) {
			randomNr = rn.nextInt(notes.size());
			if (rndVeloIsChecked) {
				velo = generateRndVelo(veloLow, veloHigh);
			}
			sequence[i] = new Note(velo, notes.get(randomNr));
			notes.remove(randomNr);
			notes.add(tempNote.getNote());
			tempNote = sequence[i];
		}
		return sequence;
	}

	public int generateRndVelo(int veloLow, int veloHigh) {
		Random rn = new Random();
		return rn.nextInt(veloHigh - veloLow + 1) + veloLow;
	}
}
