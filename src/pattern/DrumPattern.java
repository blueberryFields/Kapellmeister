package pattern;

import note.Note;
import note.NoteOn;

public class DrumPattern extends PatternBase implements SubPattern {

	/**
	 * The 2D-array that contains the notes in the pattern
	 */
	private Note[][] pattern;

	/**
	 * Constructor nr 1. Creates a new instance of Pattern containing a pattern with
	 * 8 standard notes(C3, velo 100) and the standard setting for partNotes(1/16)
	 * 
	 * @param title
	 *            a String containing the title of the pattern
	 */
	public DrumPattern(String title) {
		super(title);
		partNotes = "1/16";
		pattern = new Note[8][8];
		for (int i = 0; i < pattern[0].length; i++) {
			for (int j = 0; j < pattern[1].length; j++)
				pattern[i][j] = new Note(NoteOn.OFF);
		}
	}

	/**
	 * Constructor nr 2. Creates a new instance of Pattern containing a pattern with
	 * a given number of standard notes(C3, velo 100) and the standard setting for
	 * partNotes(1/16)
	 * 
	 * @param title
	 *            a String containing the title of the pattern
	 * @param nrOfSteps
	 *            int contatining the nr of notes in the pattern
	 */
	public DrumPattern(String title, int nrOfSteps) {
		super(title);
		partNotes = "1/16";
		pattern = new Note[8][nrOfSteps];
		for (int i = 0; i < pattern[0].length; i++) {
			for (int j = 0; j < pattern[1].length; j++)
				pattern[i][j] = new Note(NoteOn.OFF);
		}
	}

	/**
	 * Constructor nr 3. Creates a new instance of Pattern containing a pattern with
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
	public DrumPattern(String title, int nrOfSteps, String partNotes) {
		super(title);
		partNotes = "1/16";
		this.partNotes = partNotes;
		pattern = new Note[8][nrOfSteps];
		for (int i = 0; i < pattern[0].length; i++) {
			for (int j = 0; j < pattern[1].length; j++)
				pattern[i][j] = new Note(NoteOn.OFF);
		}
	}

	@Override
	public void changeNrOfSteps(int nrOfSteps) {
		Note[][] tempPattern = new Note[8][nrOfSteps];

		if (nrOfSteps > pattern[1].length) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < pattern[1].length; j++) {
					tempPattern[i][j] = pattern[i][j];
				}
				if (tempPattern[i][tempPattern[1].length - 1] == null) {
					tempPattern[i][tempPattern[1].length - 1] = new Note(100, pattern[i][0].getNote(), NoteOn.OFF);
				}
			}
		} else {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < nrOfSteps; j++) {
					tempPattern[i][j] = pattern[i][j];
				}
			}
		}
		pattern = tempPattern;
	}
	
	public void changeNote(String note, int instrument) {
		for (int i = 0; i < pattern[1].length; i++) {
			pattern[instrument][i].changeNote(note);
		}
	}

	@Override
	public PatternBase copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void paste(PatternBase pattern) {
		// TODO Auto-generated method stub

	}

	@Override
	public Note getCopyOfSingleStep(int noteIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// The rest is simple getters and setters
	
	public Note[][] getPattern() {
		return pattern;
	}

	public void setPattern(Note[][] pattern) {
		this.pattern = pattern;
	}

	public Note getSingleStep(int indexRow, int indexCol) {
		return pattern[indexRow][indexCol];
	}

	public void setSingleStep(Note note, int indexRow, int indexCol) {
		pattern[indexRow][indexCol] = note;
	}

	@Override
	public int getNrOfSteps() {
		return pattern[1].length;
	}

	@Override
	public int length() {
		return pattern[1].length;
	}

}
