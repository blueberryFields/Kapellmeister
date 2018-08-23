package note;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Base-class for the keys. Used for generating patterns. Which key the pattern
 * will be generating from is specified by the different subclasses availeble,
 * one for each key
 */

public class NoteGenerator {

	/**
	 * This list will be filled with the available notes from each key when the
	 * subclasses is created and is where the generator-methods draws notes from.
	 */
	List<String> notes = new LinkedList<>();

	/**
	 * Generates a pattern of random notes drawn from whithin the scope specified by
	 * the parameters passed
	 * 
	 * @param pattern
	 *            the pattern which will be filled with the new notes
	 * @param rndVeloIsChecked
	 *            generate random velocity or not?
	 * @param veloLow
	 *            if random velocity is to be used this parameter will set the lower
	 *            confines of the range
	 * @param veloHigh
	 *            if random velocity is to be used this parameter will set the upper
	 *            confines of the range
	 * @param octaveLow
	 *            sets the lower confines of the octaveRange from which octavenumber
	 *            will be generated
	 * @param octaveHigh
	 *            sets the upper confines of the octaveRange from which octavenumber
	 *            will be generated
	 * @return the newly generated pattern
	 */
	public Note[] getRndSequence(Note[] pattern, boolean rndVeloIsChecked, int veloLow, int veloHigh, int octaveLow,
			int octaveHigh) {
		Random rn = new Random();
		int velo = 100;
		for (int i = 0; i < pattern.length; i++) {
			if (rndVeloIsChecked) {
				velo = generateRndVelo(veloLow, veloHigh);
			}
			int randomNr = rn.nextInt(notes.size());
			pattern[i] = new Note(velo, getNote(randomNr, octaveLow, octaveHigh));

		}
		return pattern;
	}

	// !!!Sometimes something goes wrong here!!!
	/**
	 * Generates a pattern of random notes(except that there will be no duplicate
	 * notes in a row) drawn from whithin the scope specified by the parameters
	 * passed.
	 * 
	 * @param pattern
	 *            the pattern which will be filled with the new notes
	 * @param rndVeloIsChecked
	 *            generate random velocity or not?
	 * @param veloLow
	 *            if random velocity is to be used this parameter will set the lower
	 *            confines of the range
	 * @param veloHigh
	 *            if random velocity is to be used this parameter will set the upper
	 *            confines of the range
	 * @param octaveLow
	 *            sets the lower confines of the octaveRange from which octavenumber
	 *            will be generated
	 * @param octaveHigh
	 *            sets the upper confines of the octaveRange from which octavenumber
	 *            will be generated
	 * @return the newly generated pattern
	 */
	public Note[] getRndSeqNoDuplInRow(Note[] pattern, boolean rndVeloIsChecked, int veloLow, int veloHigh,
			int octaveLow, int octaveHigh) {
		Random rn = new Random();
		int velo = 100;
		String tempNote;
		int randomNr = rn.nextInt(notes.size());
		if (rndVeloIsChecked) {
			velo = generateRndVelo(veloLow, veloHigh);
		}
		pattern[0] = new Note(velo, getNote(randomNr, octaveLow, octaveHigh));
		tempNote = pattern[0].toString().substring(0, 1);
		notes.remove(randomNr);
		for (int i = 1; i < pattern.length - 1; i++) {
			randomNr = rn.nextInt(notes.size());
			if (rndVeloIsChecked) {
				velo = generateRndVelo(veloLow, veloHigh);
			}
			pattern[i] = new Note(velo, getNote(randomNr, octaveLow, octaveHigh));
			notes.remove(randomNr);
			notes.add(tempNote);
			tempNote = pattern[i].toString().substring(0, 1);
		}
		randomNr = rn.nextInt(notes.size());
		if (rndVeloIsChecked) {
			velo = generateRndVelo(veloLow, veloHigh);
		}
		pattern[pattern.length - 1] = new Note(velo, getNote(randomNr, octaveLow, octaveHigh));
		notes.remove(randomNr);
		if (pattern[pattern.length - 1].getNote().equals(pattern[0].getNote())) {
			pattern[pattern.length - 1] = new Note(velo, getNote(randomNr, octaveLow, octaveHigh));
		}
		return pattern;
	}

	/**
	 * Generates a pattern of random notes drawn from whithin the scope specified by
	 * the parameters passed. Also every note gets a random NoteOn-value
	 * 
	 * @param pattern
	 *            the pattern which will be filled with the new notes
	 * @param rndVeloIsChecked
	 *            generate random velocity or not?
	 * @param veloLow
	 *            if random velocity is to be used this parameter will set the lower
	 *            confines of the range
	 * @param veloHigh
	 *            if random velocity is to be used this parameter will set the upper
	 *            confines of the range
	 * @param octaveLow
	 *            sets the lower confines of the octaveRange from which octavenumber
	 *            will be generated
	 * @param octaveHigh
	 *            sets the upper confines of the octaveRange from which octavenumber
	 *            will be generated
	 * @return the newly generated pattern
	 */
	public Note[] getRndSequenceOnHoldOff(Note[] sequence, boolean rndVeloIsChecked, int veloLow, int veloHigh,
			int octaveLow, int octaveHigh) {
		Random rn = new Random();
		NoteOn noteOn;
		int velo = 100;
		for (int i = 0; i < sequence.length; i++) {
			if (rndVeloIsChecked) {
				velo = generateRndVelo(veloLow, veloHigh);
			}
			if (i == 0) {
				noteOn = generateNoteOn();
			} else if (i == sequence.length - 1) {
				if (sequence[0].getNoteOn() == NoteOn.HOLD && sequence[i - 2].getNoteOn() == NoteOn.OFF) {
					noteOn = NoteOn.ON;
				} else if (sequence[0].getNoteOn() == NoteOn.HOLD) {
					noteOn = generateNoteOnNoOff();
				} else if (sequence[i - 1].getNoteOn() == NoteOn.OFF) {
					noteOn = generateNoteOnNoHold();
				} else {
					noteOn = generateNoteOn();
				}
			} else {
				if (sequence[i - 1].getNoteOn() == NoteOn.OFF) {
					noteOn = generateNoteOnNoHold();
				} else {
					noteOn = generateNoteOn();
				}
			}
			int randomNr = rn.nextInt(notes.size());
			sequence[i] = new Note(velo, getNote(randomNr, octaveLow, octaveHigh), noteOn);

		}
		return sequence;
	}

	// !!!Sometimes something goes wrong here!!!
	/**
	 * Generates a pattern of random notes(except that there will be no duplicate
	 * notes in a row) drawn from whithin the scope specified by the parameters
	 * passed. Also every note gets a random NoteOn-value
	 * 
	 * @param pattern
	 *            the pattern which will be filled with the new notes
	 * @param rndVeloIsChecked
	 *            generate random velocity or not?
	 * @param veloLow
	 *            if random velocity is to be used this parameter will set the lower
	 *            confines of the range
	 * @param veloHigh
	 *            if random velocity is to be used this parameter will set the upper
	 *            confines of the range
	 * @param octaveLow
	 *            sets the lower confines of the octaveRange from which octavenumber
	 *            will be generated
	 * @param octaveHigh
	 *            sets the upper confines of the octaveRange from which octavenumber
	 *            will be generated
	 * @return the newly generated pattern
	 */
	public Note[] getRndSeqNoDuplInRowOnHoldOff(Note[] sequence, boolean rndVeloIsChecked, int veloLow, int veloHigh,
			int octaveLow, int octaveHigh) {
		// generate for startindex of array
		NoteOn noteOn;
		Random rn = new Random();
		int velo = 100;
		String tempNote;
		int randomNr = rn.nextInt(notes.size());
		if (rndVeloIsChecked) {
			velo = generateRndVelo(veloLow, veloHigh);
		}
		noteOn = generateNoteOn();
		sequence[0] = new Note(velo, getNote(randomNr, octaveLow, octaveHigh), noteOn);
		tempNote = sequence[0].toString().substring(0, 1);
		notes.remove(randomNr);
		// generate for middle of array
		for (int i = 1; i < sequence.length - 1; i++) {
			randomNr = rn.nextInt(notes.size());
			if (rndVeloIsChecked) {
				velo = generateRndVelo(veloLow, veloHigh);
			}
			if (sequence[i - 1].getNoteOn() == NoteOn.OFF) {
				noteOn = generateNoteOnNoHold();
			} else {
				noteOn = generateNoteOn();
			}

			sequence[i] = new Note(velo, getNote(randomNr, octaveLow, octaveHigh), noteOn);
			notes.remove(randomNr);
			notes.add(tempNote);
			tempNote = sequence[i].toString().substring(0, 1);
		}
		// generate for last index of array
		randomNr = rn.nextInt(notes.size());
		if (rndVeloIsChecked) {
			velo = generateRndVelo(veloLow, veloHigh);
		}
		if (sequence[0].getNoteOn() == NoteOn.HOLD && sequence[sequence.length - 2].getNoteOn() == NoteOn.OFF) {
			noteOn = NoteOn.ON;
		} else if (sequence[0].getNoteOn() == NoteOn.HOLD) {
			noteOn = generateNoteOnNoOff();
		} else if (sequence[sequence.length - 2].getNoteOn() == NoteOn.OFF) {
			noteOn = generateNoteOnNoHold();
		} else {
			noteOn = generateNoteOn();
		}
		sequence[sequence.length - 1] = new Note(velo, getNote(randomNr, octaveLow, octaveHigh), noteOn);
		notes.remove(randomNr);
		if (sequence[sequence.length - 1].getNote().equals(sequence[0].getNote())) {
			sequence[sequence.length - 1] = new Note(velo, getNote(randomNr, octaveLow, octaveHigh), noteOn);
		}
		return sequence;
	}

	/**
	 * @return a random NoteON-value
	 */
	public NoteOn generateNoteOn() {
		Random rn = new Random();
		switch (rn.nextInt(4)) {
		case 0:
			return NoteOn.ON;
		case 1:
			return NoteOn.HOLD;
		case 2:
			return NoteOn.OFF;
		case 3:
			return NoteOn.ON;
		default:
			return NoteOn.ON;
		}
	}

	/**
	 * @return a random NoteOn-value(except from Hold)
	 */
	public NoteOn generateNoteOnNoHold() {
		Random rn = new Random();
		switch (rn.nextInt(3)) {
		case 0:
			return NoteOn.ON;
		case 1:
			return NoteOn.ON;
		case 2:
			return NoteOn.OFF;
		default:
			return NoteOn.ON;
		}
	}

	/**
	 * @return a random NoteOn-value(except from Off)
	 */
	public NoteOn generateNoteOnNoOff() {
		Random rn = new Random();
		switch (rn.nextInt(3)) {
		case 0:
			return NoteOn.ON;
		case 1:
			return NoteOn.ON;
		case 2:
			return NoteOn.HOLD;
		default:
			return NoteOn.ON;
		}
	}

	/**
	 * Generates a random velocity value
	 * 
	 * @param veloLow
	 *            the lower confines of the velocity-value to be generated
	 * @param veloHigh
	 *            the higher confines of the velocity-value to be generated
	 * @return
	 */
	public int generateRndVelo(int veloLow, int veloHigh) {
		Random rn = new Random();
		return rn.nextInt(veloHigh - veloLow + 1) + veloLow;
	}

	/**
	 * Generates a random octave-number to be added to the noteName
	 * 
	 * @param octaveLow
	 *            the lower confines of the octaveNumber to be generated
	 * @param octaveHigh
	 *            the higher confines of the octaveNumber to be generated
	 * @return a random Int with the value specified by the parameters
	 */
	public int getRandomOctave(int octaveLow, int octaveHigh) {
		Random rn = new Random();
		return rn.nextInt(octaveHigh - octaveLow + 1) + octaveLow;
	}

	/**
	 * @param randomNr
	 *            a random number from wich a note will be drawn
	 * @param octaveLow
	 *            the lower confines of the octaveNumber to be generated
	 * @param octaveHigh
	 *            the higher confines of the octaveNumber to be generated
	 * @return a new note with random octave based on the passed parameters
	 */
	public String getNote(int randomNr, int octaveLow, int octaveHigh) {
		String note = notes.get(randomNr);
		int octave = getRandomOctave(octaveLow, octaveHigh);
		note += Integer.toString(octave);
		return note;
	}
}
