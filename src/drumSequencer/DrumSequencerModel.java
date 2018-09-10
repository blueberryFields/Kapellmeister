package drumSequencer;

import pattern.DrumPattern;
import pattern.StandardPattern;
import sequencerBase.SequencerModelBase;
import sequencerBase.SoloMute;
import sequencerBase.SubSequencerModel;

public class DrumSequencerModel extends SequencerModelBase implements SubSequencerModel {

	/**
	 * Constructor
	 */
	public DrumSequencerModel() {
		super();
		initSeq();
	}
	
	@Override
	public void initSeq() {
		patterns = new DrumPattern[8];
		soloMute = SoloMute.AUDIBLE;
		for (int i = 0; i < patterns.length; i++) {
			patterns[i] = new DrumPattern("pat " + (i + 1));
		}
	}

	@Override
	public StandardPattern copyPattern(int activePattern) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pastePattern(int activePattern, StandardPattern pattern) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initPlayVariables() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopPlayback(int activePattern) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playStep(int activePattern) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void killLastNote(int activePattern) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeNrOfSteps(int nrOfSteps, int activePattern) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getPatternNames() {
		String[] patternNames = new String[8];
		for (int i = 0; i < patterns.length; i++) {
			patternNames[i] = patterns[i].getName();
		}
		return patternNames;
	}

	
	@Override
	public DrumPattern[] getPatterns() {
		return (DrumPattern[]) patterns;
	}

}
