package drumSequencer;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import note.NoteOn;
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

		// Add action/change-listeners
		gui.getPartNotesChooser().addChangeListener(e -> changePartNotes(gui.getPartnotes()));
		gui.getNrOfStepsChooser().addChangeListener(e -> changeNrOfSteps(gui.getNrOfSteps()));
		addChangeListenersToNoteChoosers();
		addActionListenersToNoteOnButtons();
		addActionListenersToPatternChoosers();
		
		addActionListenerToDeviceChooser();
		gui.getMidiChannelChooser().addActionListener(e -> chooseMidiChannel());

		gui.setPatternNames(((DrumSequencerModel) seq).getPatterns());

		((DrumSequencerGui) gui).repaintSequencer(((DrumSequencerModel) seq).getPattern(activePattern));
	}

	private void addActionListenersToNoteOnButtons() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 16; j++) {
				int indexRow = i;
				int indexCol = j;
				((DrumSequencerGui) gui).getNoteOnButtons()[i][j]
						.addActionListener(e -> clickNoteOnButton(indexRow, indexCol));
			}
		}
	}

	private void clickNoteOnButton(int indexRow, int indexCol) {
		switch (((DrumSequencerGui) gui).getNoteOnButtonText(indexRow, indexCol)) {
		case "On":
			((DrumSequencerModel) seq).getSingleStep(activePattern, indexRow, indexCol).setNoteOn(NoteOn.OFF);
			((DrumSequencerGui) gui).setNoteOnButton(indexRow, indexCol, "Off");
			break;
		case "Off":
			((DrumSequencerModel) seq).getSingleStep(activePattern, indexRow, indexCol).setNoteOn(NoteOn.ON);
			((DrumSequencerGui) gui).setNoteOnButton(indexRow, indexCol, "On");
			break;
		default:
			break;
		}
	}

	@Override
	public void addActionListenersToPatternChoosers() {
		for (int i = 0; i < gui.getPatternChoosers().length; i++) {
			int index = i;
			gui.getPatternChoosers()[i].addActionListener(e -> choosePattern(index));
		}
	}

	public void addChangeListenersToNoteChoosers() {
		for (int i = 0; i < ((DrumSequencerGui) gui).getNoteChooserArray().length; i++) {
			int index = i;
			((DrumSequencerGui) gui).getNoteChooser(i).addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					((DrumSequencerModel) seq).getPatterns()[activePattern]
							.changeNote((String) ((DrumSequencerGui) gui).getNoteChooser(index).getValue(), index);
					// ((DrumSequencerGui) gui).repaintSequencer(((DrumSequencerModel)
					// seq).getPattern(activePattern));
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
	
	/**
	 * Change active pattern
	 * 
	 * @param pattern
	 *            the new pattern to be set to active
	 */
	@Override
	public void choosePattern(int pattern) {
		if (this.activePattern != pattern) {
			gui.disablePatternChooser(activePattern);
			gui.enablePatternChooser(pattern);
		}
		if (seq.getRunning()) {
			((DrumSequencerModel) seq).killLastNote(activePattern);
		}
		this.activePattern = pattern;
		setPartNotes();
		((DrumSequencerModel) seq).initPlayVariables();
		((DrumSequencerGui) gui).getPartNotesChooser()
				.setValue(((DrumSequencerModel) seq).getPartNotesChoice(pattern));
		((DrumSequencerGui) gui).getNrOfStepsChooser()
				.setValue(((DrumSequencerModel) seq).getNrOfSteps(pattern));
		((DrumSequencerGui) gui).repaintSequencer(((DrumSequencerModel) seq).getPattern(pattern));
		gui.repaint();
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
		tickCounter++;
		if (tickCounter == 1) {
			playStep();
		}
		if(tickCounter == 4) {
			((DrumSequencerModel) seq).killStep(activePattern);
		}
		if (tickCounter == partNotesThreshhold) {
			tickCounter = 0;
		}
	}

	@Override
	public void playStep() {
		((DrumSequencerModel) seq).playStep(activePattern);
		((DrumSequencerGui) gui).markActiveStep(((DrumSequencerModel) seq).getCurrentStep(),
				((DrumSequencerModel) seq).isFirstNote(), ((DrumSequencerModel) seq).getPattern(activePattern));
		int tempStep = ((DrumSequencerModel) seq).getCurrentStep();
		tempStep++;
		if (tempStep == ((DrumSequencerModel) seq).getPatternLength(activePattern)) {
			tempStep = 0;
		}
		((DrumSequencerModel) seq).setCurrentStep(tempStep++);
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
