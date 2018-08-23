package arrangement;

import note.Note;

/**
 * This class represent a pattern or sequence of notes that can be played by a
 * sequencer. A pattern can consist of between 1 and 16 notes as well as a title
 * and information about how fast it will be played relative to the bpm, the
 * partNotes variable. It also contains some useful operations you can use to
 * manipulate the pattern, like nudge(), copy(), paste() and changeNrOfSteps()
 */

public class Pattern {

	/**
	 * Title/name of the pattern
	 */
	private String title;
	/**
	 * The array that contains the notes in the pattern
	 */
	private Note[] pattern;
	/**
	 * A String were the choise of playbackspeed/partnotes will be stored
	 */
	private String partNotes = "1/8";

	/**
	 * Constructor nr 1. Creates a new instance of Pattern containing a pattern with
	 * 8 standard notes(C3, velo 100) and the standard setting for partNotes(1/8)
	 * 
	 * @param title
	 *            a String containing the title of the pattern
	 */
	public Pattern(String title) {
		this.title = title;
		pattern = new Note[8];
		for (int i = 0; i < pattern.length; i++) {
			pattern[i] = new Note();
		}
	}

	/**
	 * Constructor nr 2. Creates a new instance of Pattern containing a pattern with
	 * a given number of standard notes(C3, velo 100) and the standard setting for
	 * partNotes(1/8)
	 * 
	 * @param title
	 *            a String containing the title of the pattern
	 * @param nrOfSteps
	 *            int contatining the nr of notes in the pattern
	 */
	public Pattern(String title, int nrOfSteps) {
		this.title = title;
		pattern = new Note[nrOfSteps];
		for (int i = 0; i < pattern.length; i++) {
			pattern[i] = new Note();
		}
	}

	/**
	 * Constructor nr 2. Creates a new instance of Pattern containing a pattern with
	 * a given number of standard notes(C3, velo 100) and a given setting for the
	 * partNotes-Option
	 * 
	 * @param title
	 *            a String containing the title of the pattern
	 * @param nrOfSteps
	 *            int contatining the nr of notes in the pattern
	 * @param partNotes
	 *            String containing partNotes-settings, the availible choices are:
	 *            "1 bar", "1/2", "1/4", "1/8", "1/16"
	 */
	public Pattern(String title, int nrOfSteps, String partNotes) {
		this.title = title;
		this.partNotes = partNotes;
		pattern = new Note[nrOfSteps];
		for (int i = 0; i < pattern.length; i++) {
			pattern[i] = new Note();
		}
	}

	/**
	 * Slides the whole pattern one step to the left
	 */
	public void nudgeLeft() {
		Note[] tempPattern = pattern.clone();
		for (int i = 0; i < pattern.length; i++) {
			if (i == pattern.length - 1) {
				pattern[i] = tempPattern[0];
			} else {
				pattern[i] = tempPattern[i + 1];
			}
		}
	}

	/**
	 * Slides the whole pattern one step to the right
	 */
	public void nudgeRight() {
		Note[] tempPattern = pattern.clone();
		for (int i = 0; i < pattern.length; i++) {
			if (i == 0) {
				pattern[i] = tempPattern[pattern.length - 1];
			} else {
				pattern[i] = tempPattern[i - 1];
			}
		}
	}

	/**
	 * Change nr of notes or steps in the pattern
	 * 
	 * @param nrOfSteps
	 *            the new nr of steps to be set
	 */
	public void changeNrOfSteps(int nrOfSteps) {
		Note[] tempPattern = new Note[nrOfSteps];
		if (nrOfSteps > pattern.length) {
			for (int i = 0; i < pattern.length; i++) {
				tempPattern[i] = pattern[i];
			}
			if (tempPattern[tempPattern.length - 1] == null) {
				tempPattern[tempPattern.length - 1] = new Note();
			}
		} else {
			for (int i = 0; i < nrOfSteps; i++) {
				tempPattern[i] = pattern[i];
			}
		}
		pattern = tempPattern;

	}

	/**
	 * Makes an individual copy of the pattern and returns it
	 * 
	 * @return an individual copy of the pattern
	 */
	public Pattern copy() {
		Pattern tempPattern = new Pattern(title, getNrOfSteps(), partNotes);
		for (int i = 0; i < tempPattern.getPattern().length; i++) {
			tempPattern.getPattern()[i] = new Note(this.pattern[i].getVelo(), this.pattern[i].getNote(),
					this.pattern[i].getNoteOn());
		}
		return tempPattern;
	}

	/**
	 * Takes the passed pattern and replaces the existing one with an individual
	 * copy of the new one.
	 * 
	 * @param pattern
	 *            the new pattern to be pasted
	 */
	public void paste(Pattern pattern) {
		this.pattern = new Note[pattern.getNrOfSteps()];
		for (int i = 0; i < pattern.getNrOfSteps(); i++) {
			this.pattern[i] = pattern.getCopyOfSingleStep(i);
		}
	}

	/**
	 * 
	 * @param noteIndex
	 *            the note to copy
	 * @return a individual copy of the given note
	 */
	public Note getCopyOfSingleStep(int noteIndex) {
		Note tempNote = new Note(pattern[noteIndex].getVelo(), pattern[noteIndex].getNote(),
				pattern[noteIndex].getNoteOn());
		return tempNote;
	}

	// The rest is simple getters and setters

	public Note[] getPattern() {
		return pattern;
	}

	public void setPattern(Note[] sequence) {
		this.pattern = sequence;
	}

	public String getPartNotesChoise() {
		return partNotes;
	}

	public void setpartNotesChoise(String partNotes) {
		this.partNotes = partNotes;
	}

	public Note getSingleStep(int index) {
		return pattern[index];
	}

	public void setSingleStep(Note note, int index) {
		pattern[index] = note;
	}

	public int getNrOfSteps() {
		return pattern.length;
	}

	public String getName() {
		return title;
	}

	public void setName(String name) {
		this.title = name;
	}

}
