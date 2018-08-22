package arrangement;

import note.Note;

public class Pattern {

	private String name;
	private Note[] pattern;
	private String partNotes = "1/8";

	// Konstruktor 1
	public Pattern(String name) {
		this.name = name;
		pattern = new Note[8];
		for (int i = 0; i < pattern.length; i++) {
			pattern[i] = new Note();
		}
	}

	// Konstruktor 2
	public Pattern(String name, int nrOfSteps) {
		this.name = name;
		pattern = new Note[nrOfSteps];
		for (int i = 0; i < pattern.length; i++) {
			pattern[i] = new Note();
		}
	}

	// Konstruktor 3
	public Pattern(String name, int nrOfSteps, String partNotes) {
		this.name = name;
		this.partNotes = partNotes;
		pattern = new Note[nrOfSteps];
		for (int i = 0; i < pattern.length; i++) {
			pattern[i] = new Note();
		}
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Pattern copy() {
		Pattern tempPattern = new Pattern(name, getNrOfSteps(), partNotes);
		for (int i = 0; i < tempPattern.getPattern().length; i++) {
			tempPattern.getPattern()[i] = new Note(this.pattern[i].getVelo(), this.pattern[i].getNote(),
					this.pattern[i].getNoteOn());
		}
		return tempPattern;
	}
}
