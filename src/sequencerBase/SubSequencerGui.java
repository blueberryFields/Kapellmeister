package sequencerBase;

import pattern.PatternBase;
import pattern.StandardPattern;

public interface SubSequencerGui {

	public void repaintSequencer(PatternBase pattern);
	
	public void markActiveStep(int currentStep, boolean isFirstNote, StandardPattern pattern);

	public void unmarkActiveStep(int currentStep, boolean isFirstNote, StandardPattern pattern);
	
	public void disableGui();
	
	public void enableGui();
		
	public void disableStep(int stepIndex);
	
	public void enableStep(int stepIndex);
}
