package sequencerBase;

import pattern.PatternBase;
import pattern.StandardPattern;

public interface SubSequencerController {

	public void addActionListenersToPatternChoosers();
	
	public void choosePattern(int pattern);
	
	public PatternBase copyPattern();
	
	public void pastePattern(PatternBase pattern);
	
	public void tick();
	
	public void playStep();
	
	public void playMode();
	
	public void stopMode();
	
	public void printPattern(int pattern);
	
//	public void setKey(NoteGenerator key);
}
