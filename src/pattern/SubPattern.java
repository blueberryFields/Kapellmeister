package pattern;

import note.Note;

public interface SubPattern {

	public void changeNrOfSteps(int nrOfSteps);
	
	public PatternBase copy();
	
	public void paste(PatternBase pattern);
	
	public Note getCopyOfSingleStep(int noteIndex);
	
	public int getNrOfSteps();
	
	public int length();
	
}
