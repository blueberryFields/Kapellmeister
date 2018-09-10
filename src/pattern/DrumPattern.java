package pattern;

import note.Note;

public class DrumPattern extends PatternBase implements SubPattern {

	public DrumPattern(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void changeNrOfSteps(int nrOfSteps) {
		// TODO Auto-generated method stub
		
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

	@Override
	public int getNrOfSteps() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return 0;
	}

}
