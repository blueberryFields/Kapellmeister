package sequencerBase;

import pattern.PatternBase;
import pattern.StandardPattern;

public interface SubSequencerGui {

	public void repaintSequencer(PatternBase pattern);
	
	public void markActiveStep(int currentStep, boolean isFirstNote, PatternBase pattern);

	public void unmarkActiveStep(int currentStep, boolean isFirstNote, PatternBase pattern);
	
	public void disableGui();
	
	public void enableGui();
		
//	public void disableStep(int indexRow, int indexCol);
//	
//	public void enableStep(int indexRow, int indexCol);
}
