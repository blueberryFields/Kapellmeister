package drumSequencer;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pattern.DrumPattern;
import pattern.PatternBase;
import sequencerBase.SequencerControllerBase;
import sequencerBase.SubSequencerController;
import standardSequencer.StandardSequencerGui;
import standardSequencer.StandardSequencerModel;

public class DrumSequencerController extends SequencerControllerBase implements SubSequencerController {

	public DrumSequencerController(String title) {
		super(title);

		seq = new DrumSequencerModel();

		gui = new DrumSequencerGui(seq.getAvailibleMidiDevices(), title);

		gui.getPartNotesChooser().addChangeListener(e -> changePartNotes(gui.getPartnotes()));
		gui.getNrOfStepsChooser().addChangeListener(e -> changeNrOfSteps(gui.getNrOfSteps()));
		addChangeListenersToNoteChoosers();
		
		gui.setPatternNames(((DrumSequencerModel) seq).getPatterns());
		
		((DrumSequencerGui) gui).repaintSequencer(((DrumSequencerModel) seq).getPattern(activePattern));
	}

	@Override
	public void addActionListenersToPatternChoosers() {
		// TODO Auto-generated method stub

	}

	public void addChangeListenersToNoteChoosers() {
		for(int i = 0; i < ((DrumSequencerGui) gui).getNoteChooserArray().length; i++) {
			int index = i;
			((DrumSequencerGui) gui).getNoteChooser(i).addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					((DrumSequencerModel) seq).getPatterns()[activePattern].changeNote((String) ((DrumSequencerGui) gui).getNoteChooser(index).getValue(), index);	
					//((DrumSequencerGui) gui).repaintSequencer(((DrumSequencerModel) seq).getPattern(activePattern));
				}
			});	
		}
	}
	
	/**
	 * Add or remove steps from the pattern and then repaint sequencerGui
	 * accordingly
	 * 
	 * @param the
	 *            new number of steps
	 */
	private void changeNrOfSteps(int nrOfSteps) {
		((DrumSequencerModel) seq).changeNrOfSteps(nrOfSteps, activePattern);
		((DrumSequencerGui) gui).repaintSequencer(((DrumSequencerModel) seq).getPattern(activePattern));
	}

	/**
	 * Change playbackSpeed relative to masterClock.
	 * 
	 * @param partNotes
	 *            accepted parameters are as follows: "1 bar", "1/2", "1/4", "1/8",
	 *            "1/16"
	 */
	private void changePartNotes(String partNotes) {
		((DrumSequencerModel) seq).setPartNotes(partNotes, activePattern);
	}

	@Override
	public void choosePattern(int pattern) {
		// TODO Auto-generated method stub

	}

	@Override
	public DrumPattern copyPattern() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pastePattern(PatternBase pattern) {
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
