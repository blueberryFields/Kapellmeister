package arrangement;

import note.Note;

public class Sequence {

	private String name;
	private Note[] sequence;
	private String partNotes = "1/8";

	// Konstruktor 1
	public Sequence(String name) {
		this.name = name;
		sequence = new Note[8];
		for (int i = 0; i < sequence.length; i++) {
			sequence[i] = new Note();
		}
	}

	// Konstruktor 2
	public Sequence(String name, int nrOfSteps) {
		this.name = name;
		sequence = new Note[nrOfSteps];
		for (int i = 0; i < sequence.length; i++) {
			sequence[i] = new Note();
		}
	}

	public Note[] getSequence() {
		return sequence;
	}

	public void setSequence(Note[] sequence) {
		this.sequence = sequence;
	}

	public String getPartNotesChoise() {
		return partNotes;
	}

	public void setpartNotesChoise(String partNotes) {
		this.partNotes = partNotes;
	}

	public Note getSingleStep(int index) {
		return sequence[index];
	}

	public void setSingleStep(Note note, int index) {
		sequence[index] = note;
	}

	public int getNrOfSteps() {
		return sequence.length;
	}

	public void nudgeLeft() {
		Note[] tempSeq = sequence.clone();
		for (int i = 0; i < sequence.length; i++) {
			if (i == sequence.length - 1) {
				sequence[i] = tempSeq[0];
			} else {
				sequence[i] = tempSeq[i + 1];
			}
		}
	}

	public void nudgeRight() {
		Note[] tempSeq = sequence.clone();
		for (int i = 0; i < sequence.length; i++) {
			if (i == 0) {
				sequence[i] = tempSeq[sequence.length - 1];
			} else {
				sequence[i] = tempSeq[i - 1];
			}
		}
	}

	public void changeNrOfSteps(int nrOfSteps) {
		Note[] tempArray = new Note[nrOfSteps];
		if (nrOfSteps > sequence.length) {
			for (int i = 0; i < sequence.length; i++) {
				tempArray[i] = sequence[i];
			}
			if (tempArray[tempArray.length - 1] == null) {
				tempArray[tempArray.length - 1] = new Note();
			}
		} else {
			for (int i = 0; i < nrOfSteps; i++) {
				tempArray[i] = sequence[i];
			}
		}
		sequence = tempArray;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
