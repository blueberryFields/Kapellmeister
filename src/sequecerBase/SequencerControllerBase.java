package sequecerBase;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import arrangement.Pattern;
import note.Note;
import note.NoteGenerator;
import note.NoteOn;
import standardSequencer.StandardSequencerGui;
import standardSequencer.StandardSequencerModel;

public class SequencerControllerBase {

	/**
	 * Controller for the standard sequencer. Up to 8 instances of this can be
	 * created. Contains logic in the SequencerModel and graphic user interface in
	 * the SequencerGui. Is driven by the masterClock in the Master Module
	 * Controller.
	 */

	/**
	 * Contains most of the logic and is the heart of the sequencer
	 */
	private StandardSequencerModel seq;
	/**
	 * Contains the graphical user interface for the sequencer
	 */
	private StandardSequencerGui gui;
	/**
	 * Stores the title/namename of the sequencer
	 */
	@SuppressWarnings("unused")
	private String title;
	/**
	 * Keeps track of which pattern is currently the active one
	 */
	private int activePattern = 0;
	/**
	 * Keeps track of which tick in the tickgrid we´re currently at
	 */
	private int tickCounter = 0;
	/**
	 * The threshhold for where in the tickgrid a note will be played, is used to
	 * determine how often a note will be played, i.e. setting the partnotes
	 */
	private int partNotesThreshhold = 8;

	/**
	 * Konstruktor
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
	public SequencerControllerBase(NoteGenerator key, String title) {
		this.title = title;

		seq = new StandardSequencerModel(key);

		gui = new StandardSequencerGui(seq.getAvailibleMidiDevices(), title);

		// clock = new Timer(500, this);
		//
		// setBpm(bpm);
		setPartNotes();

		// Add actionListeners to buttons
		gui.getMidiChannelChooser().addActionListener(e -> chooseMidiChannel());
		gui.getGenerateButton().addActionListener(e -> generatePattern());
		gui.getNudgeLeft().addActionListener(e -> nudgeLeft());
		gui.getNudgeRight().addActionListener(e -> nudgeRight());
		gui.getRenamePattern().addActionListener(e -> renamePattern());
		gui.getRefreshButton().addActionListener(e -> refreshMidiDeviceList());

		// Add ActionListeners to Jspinners
		gui.getNrOfStepsChooser().addChangeListener(e -> changeNrOfSteps(gui.getNrOfSteps()));
		addActionListenersToPatternChoosers();
		gui.getPartNotesChooser().addChangeListener(e -> changePartNotes(gui.getPartnotes()));
		gui.getVeloLowChooser().addChangeListener(e -> changeVeloLow());
		gui.getVeloHighChooser().addChangeListener(e -> changeVeloHigh());
		gui.getOctaveLowChooser().addChangeListener(e -> changeOctaveLow());
		gui.getOctaveHighChooser().addChangeListener(e -> changeOctaveHigh());

		// Add ActionListeners to ComboBox
		addActionListenerToDeviceChooser();

		// Add ActionListeners to singleSteps
		addActionListenersToNoteChooser();
		addActionListenersToVelocityChooser();
		addActionListenersToNoteOnButton();

		gui.repaintSequencer(seq.getPattern(activePattern));
		gui.setPatternNames(seq.getPatterns());
	}

	// The following methods just add ActionListeners to different buttons n stuff
	// as their titles says

	private void addActionListenersToNoteChooser() {
		for (int i = 0; i < gui.getNoteChooserArray().length; i++) {
			int index = i;
			gui.getNoteChooser(i).addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					seq.getSingleStep(activePattern, index).changeNote((String) gui.getNoteChooser(index).getValue());
					checkHold(index);
				}
			});
		}
	}

	private void addActionListenersToVelocityChooser() {
		for (int i = 0; i < gui.getVelocityChooserArray().length; i++) {
			int index = i;
			gui.getVelocityChooser(i).addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					seq.getSingleStep(activePattern, index).setVelo((int) gui.getVelocityChooser(index).getValue());
				}
			});
		}
	}

	private void addActionListenersToPatternChoosers() {
		for (int i = 0; i < gui.getPatternChoosers().length; i++) {
			int index = i;
			gui.getPatternChoosers()[i].addActionListener(e -> choosePattern(index));
		}

	}

	private void addActionListenersToNoteOnButton() {
		for (int i = 0; i < gui.getNoteOnButtonArray().length; i++) {
			int index = i;
			gui.getNoteOnButton(i).addActionListener(e -> clickNoteOnButton(index));
		}
	}

	private void addActionListenerToDeviceChooser() {
		gui.getDeviceChooser().addActionListener(e -> chooseMidiDevice());
	}

	private void removeActionListenerFromDeviceChooser() {
		gui.getDeviceChooser().removeActionListener(e -> chooseMidiDevice());
	}

	// WORK IN PROGRESS!!!
	private void refreshMidiDeviceList() {
		// //removeActionListenerFromDeviceChooser();
		// seq.refreshMidiDeviceList();
		// gui.setAvailibleDevices(seq.getAvailibleMidiDevices());
		// //addActionListenerToDeviceChooser();
	}

	/**
	 * Makes a popup appear where you can type in a new name for the active pattern
	 */
	private void renamePattern() {
		seq.setPatternName(activePattern, gui.renamePattern(activePattern));
	}

	/**
	 * @return an array of Strings containing the names of the different patterns
	 */
	public String[] getPatternNames() {
		return seq.getPatternNames();
	}

	/**
	 * Change playbackSpeed relative to masterClock.
	 * 
	 * @param partNotes
	 *            accepted parameters are as follows: "1 bar", "1/2", "1/4", "1/8",
	 *            "1/16"
	 */
	private void changePartNotes(String partNotes) {
		seq.setPartNotes(partNotes, activePattern);
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
			seq.killLastNote(activePattern);
		}
		this.activePattern = pattern;
		setPartNotes();
		gui.getPartNotesChooser().setValue(seq.getPartNotesChoice(pattern));
		gui.getNrOfStepsChooser().setValue(seq.getNrOfSteps(pattern));
		gui.repaintSequencer(seq.getPattern(pattern));
		gui.repaint();
	}

	/**
	 * Sends a stopMessage adressed to the currently playing note in the choosen
	 * pattern
	 * 
	 * @param activePattern
	 *            the currently playing pattern
	 */
	public void killLastNote(int activePattern) {
		seq.killLastNote(activePattern);
	}

	/**
	 * Shows the sequencer GUI if it doesnt already show
	 */
	public void open() {
		gui.open();
	}

	/**
	 * Set the channel on which to send midinotes to the reciever
	 */
	private void chooseMidiChannel() {
		seq.setMidiChannel((int) gui.getMidiChannelChooser().getSelectedItem() - 1);
	}

	/**
	 * Checks so the maximum value of the random octave generator cant be lower than
	 * the minimum value
	 */
	private void changeOctaveHigh() {
		if (gui.getOctaveHigh() < gui.getOctaveLow()) {
			gui.setOctaveHigh(gui.getOctaveLow());
		}
	}

	/**
	 * Checks so the minimum value of the random octave generator cant be higher
	 * than the maximum value
	 */
	private void changeOctaveLow() {
		if (gui.getOctaveLow() > gui.getOctaveHigh()) {
			gui.setOctaveLow(gui.getOctaveHigh());
		}
	}

	/**
	 * Calls the dispose-method from JFrame and gets disposes all of the components
	 * contained inside
	 */
	public void disposeGui() {
		gui.dispose();
	}

	/**
	 * If sequencer is set to anything else than solo this method will set it to
	 * solo. If its already set to solo it will be set to audible
	 */
	public void solo() {
		if (seq.getSoloMute() != SoloMute.SOLO) {
			seq.solo();
		} else {
			seq.unSoloMute();
		}
		gui.setSoloMuteBar(seq.getSoloMute());
	}

	/**
	 * Sets the sequencer to mute
	 */
	public void mute() {
		if (seq.getSoloMute() != SoloMute.MUTE) {
			seq.mute();
			if (seq.getRunning()) {
				seq.killLastNote(activePattern);
			}
			gui.setSoloMuteBar(seq.getSoloMute());
		}
	}

	/**
	 * If the sequencer is set to anything else than mute, this method will set it
	 * to mute, if its already set to mute it will be unmuted
	 */
	public void muteUnmute() {
		if (seq.getSoloMute() != SoloMute.MUTE) {
			seq.mute();
			if (seq.getRunning()) {
				seq.killLastNote(activePattern);
			}
		} else {
			seq.unSoloMute();
		}
		gui.setSoloMuteBar(seq.getSoloMute());
	}

	/**
	 * If the sequencer is set to something else then audible this method will set
	 * it to audible and show this in the soloMuteBar
	 */
	public void unSoloMute() {
		if (seq.getSoloMute() != SoloMute.AUDIBLE) {
			seq.unSoloMute();
			gui.setSoloMuteBar(seq.getSoloMute());
		}
	}

	/**
	 * Moves the active pattern one step to the left, call checkHold and repaints
	 * the gui representation of the sequence
	 */
	private void nudgeLeft() {
		seq.nudgeLeft(activePattern);
		checkHold();
		gui.repaintSequencer(seq.getPattern(activePattern));
	}

	/**
	 * Moves the active pattern one step to the right, call checkHold and repaints
	 * the gui representation of the sequence
	 */
	private void nudgeRight() {
		seq.nudgeRight(activePattern);
		checkHold();
		gui.repaintSequencer(seq.getPattern(activePattern));
	}

	/**
	 * Check so the maximum value in the random velocity generator cant be lower
	 * than the minimum value
	 */
	private void changeVeloHigh() {
		if (gui.getVeloHighChooserValue() < gui.getVeloLowChooserValue()) {
			gui.setVeloHighChooserValue(gui.getVeloLowChooserValue());
		}
	}

	/**
	 * Check so the minimum value in the random velocity generator cant be higher
	 * than the maximum value
	 */
	private void changeVeloLow() {
		if (gui.getVeloLowChooserValue() > gui.getVeloHighChooserValue()) {
			gui.setVeloLowChooserValue(gui.getVeloHighChooserValue());
		}
	}

	/**
	 * Click the NoteOnButton, depending on which mode is choosen now a new mode
	 * will be choosen, avalible modes are: ON, OFF, HOLD
	 * 
	 * @param index
	 *            the note which is pressed
	 */
	private void clickNoteOnButton(int index) {
		switch (gui.getNoteOnButtonText(index)) {
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
	 * Sets a note in the pattern to Off, which means it will not be sent to the
	 * midireciever
	 * 
	 * @param index
	 *            the note to be set to off
	 */
	private void Off(int index) {
		seq.getSingleStep(activePattern, index).setHoldNote(-1);
		seq.getSingleStep(activePattern, index).setNoteOn(NoteOn.OFF);
		gui.setNoteOnButtonText(index, "Off");
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
		seq.getSingleStep(activePattern, index).setNoteOn(NoteOn.ON);
		gui.setNoteOnButtonText(index, "On");
		seq.getSingleStep(activePattern, index).setHoldNote(-1);
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
		seq.getSingleStep(activePattern, index).setNoteOn(NoteOn.HOLD);
		gui.setNoteOnButtonText(index, "Hold");
		checkHold(index);
	}

	/**
	 * Goes through the pattern, checks wich notes are set on Hold and sets the
	 * HoldNote in each Note accordingly If this isnt done there will be a risk of
	 * notes never being killed
	 */
	private void checkHold() {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < seq.getPattern(activePattern).length; j++) {
				if (j == 0) {
					if (seq.getSingleStep(activePattern, j).getNoteOn() == NoteOn.HOLD) {
						if (seq.getSingleStep(activePattern, seq.getPattern(activePattern).length - 1)
								.getNoteOn() != NoteOn.HOLD) {
							seq.getSingleStep(activePattern, j).setHoldNote(
									seq.getSingleStep(activePattern, seq.getPattern(activePattern).length - 1)
											.getMidiNote());
						} else {
							seq.getSingleStep(activePattern, j).setHoldNote(
									seq.getSingleStep(activePattern, seq.getPattern(activePattern).length - 1)
											.getHoldNote());
						}
					}
				} else {
					if (seq.getSingleStep(activePattern, j).getNoteOn() == NoteOn.HOLD) {
						if (seq.getSingleStep(activePattern, j - 1).getNoteOn() != NoteOn.HOLD) {
							seq.getSingleStep(activePattern, j)
									.setHoldNote(seq.getSingleStep(activePattern, j - 1).getMidiNote());
						} else {
							seq.getSingleStep(activePattern, j)
									.setHoldNote(seq.getSingleStep(activePattern, j - 1).getHoldNote());
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
			for (int j = loopStart; j < seq.getPattern(activePattern).length; j++) {
				if (j == 0) {
					if (seq.getSingleStep(activePattern, j).getNoteOn() == NoteOn.HOLD) {
						if (seq.getSingleStep(activePattern, seq.getPattern(activePattern).length - 1)
								.getNoteOn() != NoteOn.HOLD) {
							seq.getSingleStep(activePattern, j).setHoldNote(
									seq.getSingleStep(activePattern, seq.getPattern(activePattern).length - 1)
											.getMidiNote());
						} else {
							seq.getSingleStep(activePattern, j).setHoldNote(
									seq.getSingleStep(activePattern, seq.getPattern(activePattern).length - 1)
											.getHoldNote());
						}
					}
				} else {
					if (seq.getSingleStep(activePattern, j).getNoteOn() == NoteOn.HOLD) {
						if (seq.getSingleStep(activePattern, j - 1).getNoteOn() != NoteOn.HOLD) {
							seq.getSingleStep(activePattern, j)
									.setHoldNote(seq.getSingleStep(activePattern, j - 1).getMidiNote());
						} else {
							seq.getSingleStep(activePattern, j)
									.setHoldNote(seq.getSingleStep(activePattern, j - 1).getHoldNote());
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
		seq.changeNrOfSteps(nrOfSteps, activePattern);
		gui.repaintSequencer(seq.getPattern(activePattern));
	}

	/**
	 * Tells the sequencer to generate a new pattern based on info and choices
	 * collected in Gui
	 */
	private void generatePattern() {
		seq.generatePattern(gui.getNrOfSteps(), seq.getKey(), gui.getGeneratorAlgoRithmChooser(),
				gui.isRndVeloChecked(), gui.getVeloLowChooserValue(), gui.getVeloHighChooserValue(), gui.getOctaveLow(),
				gui.getOctaveHigh(), activePattern);
		checkHold();
		gui.repaintSequencer(seq.getPattern(activePattern));
	}

	/**
	 * Sets which of the available mididevices to send notes to
	 */
	private void chooseMidiDevice() {
		seq.chooseMidiDevice(gui.getChoosenDevice());
	}

	/**
	 * @return a unique copy of the active pattern
	 */
	public Pattern copyPattern() {
		return seq.copyPattern(activePattern);
	}

	/**
	 * Takes the passed sequence and replace active sequence with it and repaints
	 * Gui to show the new sequence
	 * 
	 * @param the
	 *            sequence to be pasted into the sequencer
	 */
	public void pastePattern(Pattern pattern) {
		seq.pastePattern(activePattern, pattern);
		gui.repaintSequencer(seq.getPattern(activePattern));
		gui.getPartNotesChooser().setValue(seq.getPartNotesChoice(activePattern));
		gui.getNrOfStepsChooser().setValue(seq.getNrOfSteps(activePattern));
		gui.getPatterChoosers()[activePattern].setText(seq.getPatternName(activePattern));
		checkHold();
	}

	/**
	 * Gets the sequencer ready for playback i.e. Collects needed info from gui and
	 * then disables the Gui
	 */
	public void playMode() {
		setPartNotes();
		seq.initPlayVariables();
		gui.disableGui();
	}

	/**
	 * Sets the sequencer in stopMode i.e. enables Gui again and readies the
	 * sequencer for next time it has to go int playMode
	 */
	public void stopMode() {
		seq.stopPlayback(activePattern);
		gui.enableGui();
		tickCounter = 0;
		gui.unmarkActiveStep(seq.getCurrentStep(), seq.isFirstNote(), seq.getPattern(activePattern));
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
	 * note in the stepsequencer. If last note in the pattern is reached the sequencer
	 * will reset and start from the beginning after the last note is played
	 */
	public void playStep() {
		seq.playStep(activePattern);
		gui.markActiveStep(seq.getCurrentStep(), seq.isFirstNote(), seq.getPattern(activePattern));
		int tempStep = seq.getCurrentStep();
		tempStep++;
		if (tempStep == seq.getPattern(activePattern).length) {
			tempStep = 0;
		}
		seq.setCurrentStep(tempStep++);
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
		for (int i = 0; i < seq.getPattern(pattern).length; i++) {
			s += seq.getSingleStep(pattern, i).toString();
		}
		System.out.println(s);
	}

	/**
	 * @param key
	 *            the musical key from wich note will be generated
	 */
	public void setKey(NoteGenerator key) {
		seq.setKey(key);
	}

	/**
	 * @return an array containg info of the availeble mididevices
	 */
	public Info[] getAvailibleMidiDevices() {
		return seq.getAvailibleMidiDevices();
	}

	/**
	 * Sets the partNoteThreshhold
	 */
	public void setPartNotes() {
		switch (seq.getPartNotes(activePattern)) {
		case "1 bar":
			partNotesThreshhold = 64;
			break;
		case "1/2":
			partNotesThreshhold = 32;
			break;
		case "1/4":
			partNotesThreshhold = 16;
			break;
		case "1/8":
			partNotesThreshhold = 8;
			break;
		case "1/16":
			partNotesThreshhold = 4;
			break;
		}
	}

	public Note[] getPattern() {
		return seq.getPattern(activePattern);
	}

	public void setPattern(Note[] pattern) {
		seq.setPattern(pattern, activePattern);
	}

	public JButton getCopyButton() {
		return gui.getCopyButton();
	}

	public JButton getPasteButton() {
		return gui.getPasteButton();
	}

	public void setTitle(String title) {
		this.title = title;
		gui.setTitle(title);
	}

	public String getTitle() {
		return gui.getTitle();
	}

	public SoloMute getSoloMute() {
		return seq.getSoloMute();
	}

}
