package note;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class NoteGenerator {

	List<String> notes = new LinkedList<>();

	public Note[] getRndSequence(Note[] sequence, boolean rndVeloIsChecked, int veloLow, int veloHigh, int octaveLow,
			int octaveHigh) {
		Random rn = new Random();
		int velo = 100;
		for (int i = 0; i < sequence.length; i++) {
			if (rndVeloIsChecked) {
				velo = generateRndVelo(veloLow, veloHigh);
			}
			int randomNr = rn.nextInt(notes.size());
			sequence[i] = new Note(velo, getNote(randomNr, octaveLow, octaveHigh));

		}
		return sequence;
	}

	public Note[] getRndSeqNoDuplInRow(Note[] sequence, boolean rndVeloIsChecked, int veloLow, int veloHigh,
			int octaveLow, int octaveHigh) {
		Random rn = new Random();
		int velo = 100;
		String tempNote;
		int randomNr = rn.nextInt(notes.size());
		if (rndVeloIsChecked) {
			velo = generateRndVelo(veloLow, veloHigh);
		}
		sequence[0] = new Note(velo, getNote(randomNr, octaveLow, octaveHigh));
		tempNote = sequence[0].toString().substring(0, 1);
		notes.remove(randomNr);
		for (int i = 1; i < sequence.length-1; i++) {
			randomNr = rn.nextInt(notes.size());
			if (rndVeloIsChecked) {
				velo = generateRndVelo(veloLow, veloHigh);
			}
			sequence[i] = new Note(velo, getNote(randomNr, octaveLow, octaveHigh));
			notes.remove(randomNr);
			notes.add(tempNote);
			tempNote = sequence[i].toString().substring(0, 1);
		}
		randomNr = rn.nextInt(notes.size());
		if (rndVeloIsChecked) {
			velo = generateRndVelo(veloLow, veloHigh);
		}
		sequence[sequence.length-1] = new Note(velo, getNote(randomNr, octaveLow, octaveHigh));
		notes.remove(randomNr);
//		notes.add(tempNote);
//		tempNote = sequence[sequence.length-1].toString().substring(0, 1);
		if(sequence[sequence.length-1].getNote().equals(sequence[0].getNote())) {
			sequence[sequence.length-1] = new Note(velo, getNote(randomNr, octaveLow, octaveHigh));
		}
		return sequence;
	}

	public int generateRndVelo(int veloLow, int veloHigh) {
		Random rn = new Random();
		return rn.nextInt(veloHigh - veloLow + 1) + veloLow;
	}

	public int getRandomOctave(int octaveLow, int octaveHigh) {
		Random rn = new Random();
		return rn.nextInt(octaveHigh - octaveLow + 1) + octaveLow;
	}

	public String getNote(int randomNr, int octaveLow, int octaveHigh) {
		String note = notes.get(randomNr);
		int octave = getRandomOctave(octaveLow, octaveHigh);
		note += Integer.toString(octave);
		return note;
	}
}
