package standardSequencer;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import note.Note;
import note.NoteGenerator;
import note.NoteOn;
import pattern.PatternBase;
import pattern.StandardPattern;
import pattern.SubPattern;
import sequencerBase.SequencerControllerBase;
import sequencerBase.SubSequencerController;

public class StandardSequencerController extends SequencerControllerBase implements SubSequencerController {

	/**
	 * Controller for the standard sequencer. Up to 8 instances of this can be
	 * created. Contains logic in the SequencerModel and graphic user interface in
	 * the SequencerGui. Is driven by the masterClock in the Master Module
	 * Controller.
	 */

	/**
	 * Constructor
	 * 
	 * @param key
	 *            the musical key from which the notes in the noteGenerator will be
	 *            generated, can only be reset from the masterModule
	 * @param bpm
	 *            the tempo in which the sequence of notes will be played back, can
	 *            only be reset from the masterModule
	 * @param title
	 *            the title of the Sequencer or Instrument if you will call it so,
	 *            will be displayed in the top of the frame, the Master Module and
	 *            the arrangeWindow
	 */
	public StandardSequencerController(NoteGenerator key, String title) {
		super(title);

		seq = new StandardSequencerModel(key);

		gui = new StandardSequencerGui(seq.getAvailibleMidiDevices(), title);

		setPartNotes();

		// Add actionListeners to buttons
		gui.getGenerateButton().addActionListener(e -> generatePattern());
		((StandardSequencerGui) gui).getNudgeLeft().addActionListener(e -> nudgeLeft());
		((StandardSequencerGui) gui).getNudgeRight().addActionListener(e -> nudgeRight());
		gui.getRenamePattern().addActionListener(e -> renamePattern());
		gui.getRefreshButton().addActionListener(e -> refreshMidiDeviceList());
		addActionListenersToPatternChoosers();

		// Add ActionListeners to Jspinners
		gui.getMidiChannelChooser().addActionListener(e -> chooseMidiChannel());
		gui.getPartNotesChooser().addChangeListener(e -> changePartNotes(gui.getPartnotes()));
		gui.getNrOfStepsChooser().addChangeListener(e -> changeNrOfSteps(gui.getNrOfSteps()));
		((StandardSequencerGui) gui).getOctaveLowChooser().addChangeListener(e -> changeOctaveLow());
		((StandardSequencerGui) gui).getOctaveHighChooser().addChangeListener(e -> changeOctaveHigh());
		gui.getVeloLowChooser().addChangeListener(e -> changeVeloLow());
		gui.getVeloHighChooser().addChangeListener(e -> changeVeloHigh());
		addActionListenerToDeviceChooser();

		// Add ActionListeners to singleSteps
		addActionListenersToNoteChooser();
		addActionListenersToVelocityChooser();
		addActionListenersToNoteOnButton();

		((StandardSequencerGui) gui).repaintSequencer(((StandardSequencerModel) seq).getPattern(activePattern));
		gui.setPatternNames(((StandardSequencerModel) seq).getPatterns());
	}

	// The following methods just add ActionListeners to different buttons n stuff
	// as their signatures says

	public void addActionListenersToPatternChoosers() {
		for (int i = 0; i < gui.getPatternChoosers().length; i++) {
			int index = i;
			gui.getPatternChoosers()[i].addActionListener(e -> choosePattern(index));
		}

	}

	@SuppressWarnings("unused")
	private void addChangeListenerToNrOfStepsChooser() {
		((StandardSequencerGui) gui).getNrOfStepsChooser()
				.addChangeListener(e -> changeNrOfSteps(((SubPattern) gui).getNrOfSteps()));
	}

	private void addActionListenersToNoteChooser() {
		for (int i = 0; i < ((StandardSequencerGui) gui).getNoteChooserArray().length; i++) {
			int index = i;
			((StandardSequencerGui) gui).getNoteChooser(i).addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					((StandardSequencerModel) seq).getSingleStep(activePattern, index)
							.changeNote((String) ((StandardSequencerGui) gui).getNoteChooser(index).getValue());
					checkHold(index);
				}
			});
		}
	}

	private void addActionListenersToVelocityChooser() {
		for (int i = 0; i < ((StandardSequencerGui) gui).getVelocityChooserArray().length; i++) {
			int index = i;
			((StandardSequencerGui) gui).getVelocityChooser(i).addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					((StandardSequencerModel) seq).getSingleStep(activePattern, index)
							.setVelo((int) ((StandardSequencerGui) gui).getVelocityChooser(index).getValue());
				}
			});
		}
	}

	private void addActionListenersToNoteOnButton() {
		for (int i = 0; i < ((StandardSequencerGui) gui).getNoteOnButtonArray().length; i++) {
			int index = i;
			((StandardSequencerGui) gui).getNoteOnButton(i).addActionListener(e -> clickNoteOnButton(index));
		}
	}

	/**
	 * Change playbackSpeed relative to masterClock.
	 * 
	 * @param partNotes
	 *            accepted parameters are as follows: "1 bar", "1/2", "1/4", "1/8",
	 *            "1/16"
	 */
	private void changePartNotes(String partNotes) {
		((StandardSequencerModel) seq).setPartNotes(partNotes, activePattern);
	}

	/**
	 * Checks so the maximum value of the random octave generator cant be lower than
	 * the minimum value
	 */
	private void changeOctaveHigh() {
		if (((StandardSequencerGui) gui).getOctaveHigh() < ((StandardSequencerGui) gui).getOctaveLow()) {
			((StandardSequencerGui) gui).setOctaveHigh(((StandardSequencerGui) gui).getOctaveLow());
		}
	}

	/**
	 * Checks so the minimum value of the random octave generator cant be higher
	 * than the maximum value
	 */
	private void changeOctaveLow() {
		if (((StandardSequencerGui) gui).getOctaveLow() > ((StandardSequencerGui) gui).getOctaveHigh()) {
			((StandardSequencerGui) gui).setOctaveLow(((StandardSequencerGui) gui).getOctaveHigh());
		}
	}

	/**
	 * Moves the active pattern one step to the left, call checkHold and repaints
	 * the gui representation of the sequence
	 */
	private void nudgeLeft() {
		((StandardSequencerModel) seq).nudgeLeft(activePattern);
		checkHold();
		((StandardSequencerGui) gui).repaintSequencer(((StandardSequencerModel) seq).getPattern(activePattern));
	}

	/**
	 * Moves the active pattern one step to the right, call checkHold and repaints
	 * the gui representation of the sequence
	 */
	private void nudgeRight() {
		((StandardSequencerModel) seq).nudgeRight(activePattern);
		checkHold();
		((StandardSequencerGui) gui).repaintSequencer(((StandardSequencerModel) seq).getPattern(activePattern));
	}

	/**
	 * Click the NoteOnButton, depending on which mode is choosen now a new mode
	 * will be choosen, avalible modes are: ON, OFF, HOLD
	 * 
	 * @param index
	 *            the note which is pressed
	 */
	private void clickNoteOnButton(int index) {
		switch (((StandardSequencerGui) gui).getNoteOnButtonText(index)) {
		case "On":
			hold(index);
			break;
		case "Hold":
			Off(index);
			break;
		case "Off":
			On(index);
			break;
		}
	}

	/**
	 * Sends a stopMessage adressed to the currently playing note in the choosen
	 * pattern
	 * 
	 * @param activePattern
	 *            the currently playing pattern
	 */
	public void killLastNote(int activePattern) {
		((StandardSequencerModel) seq).killLastNote(activePattern);
	}

	/**
	 * Sets a note in the pattern to Off, which means it will not be sent to the
	 * midireciever
	 * 
	 * @param index
	 *            the note to be set to off
	 */
	private void Off(int index) {
		((StandardSequencerModel) seq).getSingleStep(activePattern, index).setHoldNote(-1);
		((StandardSequencerModel) seq).getSingleStep(activePattern, index).setNoteOn(NoteOn.OFF);
		((StandardSequencerGui) gui).setNoteOnButtonText(index, "Off");
		checkHold(index);
	}

	/**
	 * Sets a note in the pattern to On, which means it will send the choosen note
	 * to the midireciever
	 * 
	 * @param index
	 *            which note to set to On
	 */
	private void On(int index) {
		((StandardSequencerModel) seq).getSingleStep(activePattern, index).setNoteOn(NoteOn.ON);
		((StandardSequencerGui) gui).setNoteOnButtonText(index, "On");
		((StandardSequencerModel) seq).getSingleStep(activePattern, index).setHoldNote(-1);
		checkHold(index);
	}

	/**
	 * Sets a note in the pattern to Hold, which means it will hold the prievious
	 * note, show this in the sequencer, then call checkHold to set the
	 * holdNoteValue in the note before and any other note that may also need this.
	 * 
	 * @param index
	 *            the note to be set to Hold
	 */
	private void hold(int index) {
		((StandardSequencerModel) seq).getSingleStep(activePattern, index).setNoteOn(NoteOn.HOLD);
		((StandardSequencerGui) gui).setNoteOnButtonText(index, "Hold");
		checkHold(index);
	}

	/**
	 * Goes through the pattern, checks wich notes are set on Hold and sets the
	 * HoldNote in each Note accordingly If this isnt done there will be a risk of
	 * notes never being killed
	 */
	private void checkHold() {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < ((StandardSequencerModel) seq).getPatternLength(activePattern); j++) {
				if (j == 0) {
					if (((StandardSequencerModel) seq).getSingleStep(activePattern, j).getNoteOn() == NoteOn.HOLD) {
						if (((StandardSequencerModel) seq)
								.getSingleStep(activePattern,
										((StandardSequencerModel) seq).getPatternLength(activePattern) - 1)
								.getNoteOn() != NoteOn.HOLD) {
							((StandardSequencerModel) seq).getSingleStep(activePattern, j)
									.setHoldNote(
											((StandardSequencerModel) seq)
													.getSingleStep(activePattern,
															((StandardSequencerModel) seq)
																	.getPatternLength(activePattern) - 1)
													.getMidiNote());
						} else {
							((StandardSequencerModel) seq).getSingleStep(activePattern, j)
									.setHoldNote(
											((StandardSequencerModel) seq)
													.getSingleStep(activePattern,
															((StandardSequencerModel) seq)
																	.getPatternLength(activePattern) - 1)
													.getHoldNote());
						}
					}
				} else {
					if (((StandardSequencerModel) seq).getSingleStep(activePattern, j).getNoteOn() == NoteOn.HOLD) {
						if (((StandardSequencerModel) seq).getSingleStep(activePattern, j - 1)
								.getNoteOn() != NoteOn.HOLD) {
							((StandardSequencerModel) seq).getSingleStep(activePattern, j).setHoldNote(
									((StandardSequencerModel) seq).getSingleStep(activePattern, j - 1).getMidiNote());
						} else {
							((StandardSequencerModel) seq).getSingleStep(activePattern, j).setHoldNote(
									((StandardSequencerModel) seq).getSingleStep(activePattern, j - 1).getHoldNote());
						}
					}
				}
			}
		}
	}

	/**
	 * Same as checkHold() but starts from selected note in pattern, maybe this can
	 * save some nanoseconds of computing, I dont now...
	 * 
	 * @param index
	 *            the note on wich to start the check
	 */
	private void checkHold(int index) {
		int loopStart = index;
		for (int i = 0; i < 2; i++) {
			for (int j = loopStart; j < ((StandardSequencerModel) seq).getPatternLength(activePattern); j++) {
				if (j == 0) {
					if (((StandardSequencerModel) seq).getSingleStep(activePattern, j).getNoteOn() == NoteOn.HOLD) {
						if (((StandardSequencerModel) seq)
								.getSingleStep(activePattern,
										((StandardSequencerModel) seq).getPatternLength(activePattern) - 1)
								.getNoteOn() != NoteOn.HOLD) {
							((StandardSequencerModel) seq).getSingleStep(activePattern, j)
									.setHoldNote(
											((StandardSequencerModel) seq)
													.getSingleStep(activePattern,
															((StandardSequencerModel) seq)
																	.getPatternLength(activePattern) - 1)
													.getMidiNote());
						} else {
							((StandardSequencerModel) seq).getSingleStep(activePattern, j)
									.setHoldNote(
											((StandardSequencerModel) seq)
													.getSingleStep(activePattern,
															((StandardSequencerModel) seq)
																	.getPatternLength(activePattern) - 1)
													.getHoldNote());
						}
					}
				} else {
					if (((StandardSequencerModel) seq).getSingleStep(activePattern, j).getNoteOn() == NoteOn.HOLD) {
						if (((StandardSequencerModel) seq).getSingleStep(activePattern, j - 1)
								.getNoteOn() != NoteOn.HOLD) {
							((StandardSequencerModel) seq).getSingleStep(activePattern, j).setHoldNote(
									((StandardSequencerModel) seq).getSingleStep(activePattern, j - 1).getMidiNote());
						} else {
							((StandardSequencerModel) seq).getSingleStep(activePattern, j).setHoldNote(
									((StandardSequencerModel) seq).getSingleStep(activePattern, j - 1).getHoldNote());
						}
					}
				}
			}
			loopStart = 0;
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
		((StandardSequencerModel) seq).changeNrOfSteps(nrOfSteps, activePattern);
		((StandardSequencerGui) gui).repaintSequencer(((StandardSequencerModel) seq).getPattern(activePattern));
	}

	/**
	 * Tells the sequencer to generate a new pattern based on info and choices
	 * collected in Gui
	 */
	private void generatePattern() {
		((StandardSequencerModel) seq).generatePattern(((StandardSequencerGui) gui).getNrOfSteps(),
				((StandardSequencerModel) seq).getKey(), gui.getGeneratorAlgoRithmChooser(), gui.isRndVeloChecked(),
				gui.getVeloLowChooserValue(), gui.getVeloHighChooserValue(),
				((StandardSequencerGui) gui).getOctaveLow(), ((StandardSequencerGui) gui).getOctaveHigh(),
				activePattern);
		checkHold();
		((StandardSequencerGui) gui).repaintSequencer(((StandardSequencerModel) seq).getPattern(activePattern));
	}

	/**
	 * Change active pattern
	 * 
	 * @param pattern
	 *            the new pattern to be set to active
	 */
	public void choosePattern(int pattern) {
		if (this.activePattern != pattern) {
			gui.disablePatternChooser(activePattern);
			gui.enablePatternChooser(pattern);
		}
		if (seq.getRunning()) {
			((StandardSequencerModel) seq).killLastNote(activePattern);
		}
		this.activePattern = pattern;
		setPartNotes();
		((StandardSequencerModel) seq).initPlayVariables();
		((StandardSequencerGui) gui).getPartNotesChooser()
				.setValue(((StandardSequencerModel) seq).getPartNotesChoice(pattern));
		((StandardSequencerGui) gui).getNrOfStepsChooser()
				.setValue(((StandardSequencerModel) seq).getNrOfSteps(pattern));
		((StandardSequencerGui) gui).repaintSequencer(((StandardSequencerModel) seq).getPattern(pattern));
		gui.repaint();
	}

	/**
	 * 
	 * 
	 * @return a unique copy of the active pattern
	 */
	public StandardPattern copyPattern() {
		return ((StandardSequencerModel) seq).copyPattern(activePattern);
	}

	/**
	 * Takes the passed sequence and replace active sequence with it and repaints
	 * Gui to show the new sequence
	 * 
	 * @param the
	 *            sequence to be pasted into the sequencer
	 */
	public void pastePattern(PatternBase pattern) {
		((StandardSequencerModel) seq).pastePattern(activePattern, (StandardPattern) pattern);
		((StandardSequencerGui) gui).repaintSequencer(((StandardSequencerModel) seq).getPattern(activePattern));
		((StandardSequencerGui) gui).getPartNotesChooser()
				.setValue(((StandardSequencerModel) seq).getPartNotesChoice(activePattern));
		((StandardSequencerGui) gui).getNrOfStepsChooser()
				.setValue(((StandardSequencerModel) seq).getNrOfSteps(activePattern));
		gui.getPatterChoosers()[activePattern].setText(seq.getPatternName(activePattern));
		checkHold();
	}

	/**
	 * Step forward in the tickGrid, resets when partNotesThreshhold is reached
	 * Every time tickCounter reaches tick nr 1 playStep() is invoked
	 */
	public void tick() {
		tickCounter++;
		if (tickCounter == 1) {

			playStep();
		}
		if (tickCounter == partNotesThreshhold) {
			tickCounter = 0;
		}
	}

	/**
	 * This method tells the model to play a note and the Gui to mark the played
	 * note in the stepsequencer. If last note in the pattern is reached the
	 * sequencer will reset and start from the beginning after the last note is
	 * played
	 */
	public void playStep() {
		((StandardSequencerModel) seq).playStep(activePattern);
		((StandardSequencerGui) gui).markActiveStep(((StandardSequencerModel) seq).getCurrentStep(),
				((StandardSequencerModel) seq).isFirstNote(), ((StandardSequencerModel) seq).getPattern(activePattern));
		int tempStep = ((StandardSequencerModel) seq).getCurrentStep();
		tempStep++;
		if (tempStep == ((StandardSequencerModel) seq).getPatternLength(activePattern)) {
			tempStep = 0;
		}
		((StandardSequencerModel) seq).setCurrentStep(tempStep++);
	}

	/**
	 * Gets the sequencer ready for playback i.e. Collects needed info from gui and
	 * then disables the Gui
	 */
	public void playMode() {
		setPartNotes();
		((StandardSequencerModel) seq).initPlayVariables();
		((StandardSequencerGui) gui).disableGui();
	}

	/**
	 * Sets the sequencer in stopMode i.e. enables Gui again and readies the
	 * sequencer for next time it has to go int playMode
	 */
	public void stopMode() {
		((StandardSequencerModel) seq).stopPlayback(activePattern);
		((StandardSequencerGui) gui).enableGui();
		tickCounter = 0;
		((StandardSequencerGui) gui).unmarkActiveStep(((StandardSequencerModel) seq).getCurrentStep(),
				((StandardSequencerModel) seq).isFirstNote(), ((StandardSequencerModel) seq).getPattern(activePattern));
	}

	/**
	 * Prints choosen pattern to sysout. Good for doubleChecking Gui so it reflects
	 * what notes there really is for example
	 * 
	 * @param the
	 *            pattern choosen to be printed
	 */
	public void printPattern(int pattern) {
		String s = "";
		for (int i = 0; i < ((StandardSequencerModel) seq).getPatternLength(pattern); i++) {
			s += ((StandardSequencerModel) seq).getSingleStep(pattern, i).toString();
		}
		System.out.println(s);
	}

	/**
	 * @param key
	 *            the musical key from wich note will be generated
	 */
	public void setKey(NoteGenerator key) {
		((StandardSequencerModel) seq).setKey(key);
	}

	// Getters and setters for patterns
	public StandardPattern getPattern() {
		return ((StandardSequencerModel) seq).getPattern(activePattern);
	}

	public void setPattern(Note[] pattern) {
		((StandardSequencerModel) seq).setPattern(pattern, activePattern);
	}

}
