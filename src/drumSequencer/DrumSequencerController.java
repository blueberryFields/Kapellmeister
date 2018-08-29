package drumSequencer;

import pattern.StandardPattern;
import sequencerBase.SequencerControllerBase;
import sequencerBase.SubSequencerController;
import standardSequencer.StandardSequencerGui;

public class DrumSequencerController extends SequencerControllerBase implements SubSequencerController {

	public DrumSequencerController(String title) {
		super(title);
		
		seq = new DrumSequencerModel();
		
		gui = new DrumSequencerGui(seq.getAvailibleMidiDevices(), title);
	}

	@Override
	public void addActionListenersToPatternChoosers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void choosePattern(int pattern) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StandardPattern copyPattern() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pastePattern(StandardPattern pattern) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playMode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopMode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printPattern(int pattern) {
		// TODO Auto-generated method stub
		
	}

}
