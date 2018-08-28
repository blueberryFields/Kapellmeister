package sequencerBase;

import pattern.StandardPattern;

public interface SubSequenceModel {

	public void initSeq();
	
//	public void generatePattern();
	
	public StandardPattern copyPattern(int activePattern);
	
	public void pastePattern(int activePattern, StandardPattern pattern);
	
	public void initPlayVariables();
	
	public void stopPlayback(int activePattern);
	
	public void playStep(int activePattern);
	
	public void killLastNote(int activePattern);
	
	public void changeNrOfSteps(int nrOfSteps, int activePattern);
	
	public String[] getPatternNames();
	
	
}
