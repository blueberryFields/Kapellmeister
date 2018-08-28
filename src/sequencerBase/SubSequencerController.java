package sequencerBase;

import note.NoteGenerator;
import pattern.StandardPattern;

public interface SubSequencerController {

	public void addActionListenersToPatternChoosers();
	
	public void choosePattern(int pattern);
	
	public StandardPattern copyPattern();
	
	public void pastePattern(StandardPattern pattern);
	
	public void tick();
	
	public void playStep();
	
	public void playMode();
	
	public void stopMode();
	
	public void printPattern(int pattern);
	
//	public void setKey(NoteGenerator key);
}
