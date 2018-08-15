package sequencer;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import arrangement.Sequence;
import arrangement.SoloMute;
import note.Note;
import note.NoteGenerator;
import note.NoteOn;

public class SequencerController {

	private SequencerModel seq;
	private SequencerGui gui;
	@SuppressWarnings("unused")
	private String title;
	private int activeSequence = 0;
	private int tickCounter = 0;
	private int partNotesThreshhold = 8;

	// Konstruktor
	public SequencerController(NoteGenerator key, int bpm, String title) {
		this.title = title;

		seq = new SequencerModel(key, bpm);

		gui = new SequencerGui(seq.getAvailibleMidiDevices(), title);

		// clock = new Timer(500, this);
		//
		// setBpm(bpm);
		setPartNotes();

		// Add actionListeners to buttons
		gui.getMidiChannelChooser().addActionListener(e -> chooseMidiChannel());
		gui.getGenerateButton().addActionListener(e -> generateSequence());
		gui.getNudgeLeft().addActionListener(e -> nudgeLeft());
		gui.getNudgeRight().addActionListener(e -> nudgeRight());
		gui.getRenamePattern().addActionListener(e -> renameSequence());
		gui.getRefreshButton().addActionListener(e -> refreshMidiDeviceList());

		// Add ActionListeners to Jspinners
		gui.getNrOfStepsChooser().addChangeListener(e -> changeNrOfSteps(gui.getNrOfSteps()));
		addActionListenersToSequenceChoosers();
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

		gui.repaintSequencer(seq.getSequence(activeSequence));
		gui.setPatternNames(seq.getSequences());
	}

	private void addActionListenersToNoteChooser() {
		for (int i = 0; i < gui.getNoteChooserArray().length; i++) {
			int index = i;
			gui.getNoteChooser(i).addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					seq.getSingleStep(activeSequence, index).changeNote((String) gui.getNoteChooser(index).getValue());
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
					seq.getSingleStep(activeSequence, index).setVelo((int) gui.getVelocityChooser(index).getValue());
				}
			});
		}
	}

	private void addActionListenersToSequenceChoosers() {
		for (int i = 0; i < gui.getPatternChoosers().length; i++) {
			int index = i;
			gui.getPatternChoosers()[i].addActionListener(e -> chooseSequence(index));
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
	 * Makes a popup appear where you can type in a new name for the active sequence
	 */
	private void renameSequence() {
		seq.setSequenceName(activeSequence, gui.renameSequence(activeSequence));
	}

	/**
	 * @return an array of Strings containing the names of the different sequences
	 */
	public String[] getSequenceNames() {
		return seq.getSequenceNames();
	}

	/**
	 * Change playbackSpeed relative to masterClock.
	 * 
	 * @param partNotes
	 *            accepted parameters are as follows: "1 bar", "1/2", "1/4", "1/8",
	 *            "1/16"
	 */
	private void changePartNotes(String partNotes) {
		seq.setPartNotes(partNotes, activeSequence);
	}

	/**
	 * Change active sequence
	 * 
	 * @param sequence
	 *            the new sequence to be set to active
	 */
	public void chooseSequence(int sequence) {
		if (this.activeSequence != sequence) {
			gui.disablePatternChooser(activeSequence);
			gui.enablePatternChooser(sequence);
		}
		if (seq.getRunning()) {
			seq.killLastNote(activeSequence);
		}
		this.activeSequence = sequence;
		setPartNotes();
		gui.getPartNotesChooser().setValue(seq.getPartNotesChoice(sequence));
		gui.getNrOfStepsChooser().setValue(seq.getNrOfSteps(sequence));
		gui.repaintSequencer(seq.getSequence(sequence));
		gui.repaint();
	}

	/**
	 * Sends a stopMessage adressed to the currently playing note in the choosen
	 * sequence
	 */
	public void killLastNote(int sequence) {
		seq.killLastNote(sequence);
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
	 * Calls the disposemethod from JFrame and gets disposes all of the components
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
				seq.killLastNote(activeSequence);
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
				seq.killLastNote(activeSequence);
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
	 * Moves the active sequence one step to the left, call checkHold and repaints
	 * the gui representation of the sequence
	 */
	private void nudgeLeft() {
		seq.nudgeLeft(activeSequence);
		checkHold();
		gui.repaintSequencer(seq.getSequence(activeSequence));
	}

	/**
	 * Moves the active sequence one step to the right, call checkHold and repaints
	 * the gui representation of the sequence
	 */
	private void nudgeRight() {
		seq.nudgeRight(activeSequence);
		checkHold();
		gui.repaintSequencer(seq.getSequence(activeSequence));
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
	 * will be choosen
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
	 * Sets a note in the sequence to Off, which means it will not be sent to the
	 * midireciever
	 * 
	 * @param index
	 *            the note to be set to off
	 */
	private void Off(int index) {
		seq.getSingleStep(activeSequence, index).setHoldNote(-1);
		seq.getSingleStep(activeSequence, index).setNoteOn(NoteOn.OFF);
		gui.setNoteOnButtonText(index, "Off");
		checkHold(index);
	}

	/**
	 * Sets a note in the sequence to On, which means it will send the choosen note
	 * to the midireciever
	 * 
	 * @param index
	 *            which note to set to On
	 */
	private void On(int index) {
		seq.getSingleStep(activeSequence, index).setNoteOn(NoteOn.ON);
		gui.setNoteOnButtonText(index, "On");
		seq.getSingleStep(activeSequence, index).setHoldNote(-1);
		checkHold(index);
	}

	/**
	 * Sets a note in the sequence to Hold, which means it will hold the prievious
	 * note, show this in the sequencer, then call checkHold to set the
	 * holdNoteValue in the note before and any other note that may also need this.
	 * 
	 * @param index
	 *            the note to be set to Hold
	 */
	private void hold(int index) {
		seq.getSingleStep(activeSequence, index).setNoteOn(NoteOn.HOLD);
		gui.setNoteOnButtonText(index, "Hold");
		checkHold(index);
	}

	/**
	 * Goes through the sequence, checks wich notes are set on Hold and sets the
	 * HoldNote in each Note accordingly If this isnt done there will be a risk of
	 * notes never being killed
	 */
	private void checkHold() {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < seq.getSequence(activeSequence).length; j++) {
				if (j == 0) {
					if (seq.getSingleStep(activeSequence, j).getNoteOn() == NoteOn.HOLD) {
						if (seq.getSingleStep(activeSequence, seq.getSequence(activeSequence).length - 1)
								.getNoteOn() != NoteOn.HOLD) {
							seq.getSingleStep(activeSequence, j)
									.setHoldNote(seq
											.getSingleStep(activeSequence, seq.getSequence(activeSequence).length - 1)
											.getMidiNote());
						} else {
							seq.getSingleStep(activeSequence, j)
									.setHoldNote(seq
											.getSingleStep(activeSequence, seq.getSequence(activeSequence).length - 1)
											.getHoldNote());
						}
					}
				} else {
					if (seq.getSingleStep(activeSequence, j).getNoteOn() == NoteOn.HOLD) {
						if (seq.getSingleStep(activeSequence, j - 1).getNoteOn() != NoteOn.HOLD) {
							seq.getSingleStep(activeSequence, j)
									.setHoldNote(seq.getSingleStep(activeSequence, j - 1).getMidiNote());
						} else {
							seq.getSingleStep(activeSequence, j)
									.setHoldNote(seq.getSingleStep(activeSequence, j - 1).getHoldNote());
						}
					}
				}
			}
		}
	}

	/**
	 * Same as checkHold() but starts from selected note in sequence, maybe this can
	 * save some nanoseconds of computing, I dont now...
	 * 
	 * @param index
	 *            the note on wich to start the check
	 */
	private void checkHold(int index) {
		int loopStart = index;
		for (int i = 0; i < 2; i++) {
			for (int j = loopStart; j < seq.getSequence(activeSequence).length; j++) {
				if (j == 0) {
					if (seq.getSingleStep(activeSequence, j).getNoteOn() == NoteOn.HOLD) {
						if (seq.getSingleStep(activeSequence, seq.getSequence(activeSequence).length - 1)
								.getNoteOn() != NoteOn.HOLD) {
							seq.getSingleStep(activeSequence, j)
									.setHoldNote(seq
											.getSingleStep(activeSequence, seq.getSequence(activeSequence).length - 1)
											.getMidiNote());
						} else {
							seq.getSingleStep(activeSequence, j)
									.setHoldNote(seq
											.getSingleStep(activeSequence, seq.getSequence(activeSequence).length - 1)
											.getHoldNote());
						}
					}
				} else {
					if (seq.getSingleStep(activeSequence, j).getNoteOn() == NoteOn.HOLD) {
						if (seq.getSingleStep(activeSequence, j - 1).getNoteOn() != NoteOn.HOLD) {
							seq.getSingleStep(activeSequence, j)
									.setHoldNote(seq.getSingleStep(activeSequence, j - 1).getMidiNote());
						} else {
							seq.getSingleStep(activeSequence, j)
									.setHoldNote(seq.getSingleStep(activeSequence, j - 1).getHoldNote());
						}
					}
				}
			}
			loopStart = 0;
		}
	}

	/**
	 * Add or remove steps from sequence the repaint gui sequencer accordingly
	 * 
	 * @param the
	 *            new number of steps
	 */
	private void changeNrOfSteps(int nrOfSteps) {
		seq.changeNrOfSteps(nrOfSteps, activeSequence);
		gui.repaintSequencer(seq.getSequence(activeSequence));
	}

	/**
	 * Tells the sequencer to generate a new sequence based on info and choices
	 * collected in Gui
	 */
	private void generateSequence() {
		seq.generateSequence(gui.getNrOfSteps(), seq.getKey(), gui.getGeneratorAlgoRithmChooser(),
				gui.isRndVeloChecked(), gui.getVeloLowChooserValue(), gui.getVeloHighChooserValue(), gui.getOctaveLow(),
				gui.getOctaveHigh(), activeSequence);
		checkHold();
		gui.repaintSequencer(seq.getSequence(activeSequence));
	}

	/**
	 * Sets which of the available mididevices to send notes to
	 */
	private void chooseMidiDevice() {
		seq.chooseMidiDevice(gui.getChoosenDevice());
	}

	/**
	 * @return a unique copy of the active sequence
	 */
	public Sequence copySequence() {
		return seq.copySequence(activeSequence);
	}

	/**
	 * Takes the passed sequence and replace active sequence with it and repaints
	 * Gui to show the new sequence
	 * 
	 * @param the
	 *            sequence to be pasted into the sequencer
	 */
	public void pasteSequence(Sequence sequence) {
		seq.pasteSequence(activeSequence, sequence);
		gui.repaintSequencer(seq.getSequence(activeSequence));
		gui.getPartNotesChooser().setValue(seq.getPartNotesChoice(activeSequence));
		gui.getNrOfStepsChooser().setValue(seq.getNrOfSteps(activeSequence));
		gui.getPatterChoosers()[activeSequence].setText(seq.getSequenceName(activeSequence));
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
		seq.stopSequence(activeSequence);
		gui.enableGui();
		tickCounter = 0;
		gui.unmarkActiveStep(seq.getCurrentStep(), seq.isFirstNote(), seq.getSequence(activeSequence));
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
	 * note in the sequences. If last note in sequencer is reached the sequencer
	 * will reset and start for beginning after last note is played
	 */
	public void playStep() {
		seq.playStep(activeSequence);
		gui.markActiveStep(seq.getCurrentStep(), seq.isFirstNote(), seq.getSequence(activeSequence));
		int tempStep = seq.getCurrentStep();
		tempStep++;
		if (tempStep == seq.getSequence(activeSequence).length) {
			tempStep = 0;
		}
		seq.setCurrentStep(tempStep++);
	}

	/**
	 * Prints choosen sequence to sysout. Good for doubleChecking Gui so it reflects
	 * what notes there really is for example
	 * 
	 * @param the
	 *            sequence choosen to be printed
	 */
	public void printSequence(int sequence) {
		String s = "";
		for (int i = 0; i < seq.getSequence(sequence).length; i++) {
			s += seq.getSingleStep(sequence, i).toString();
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
		switch (seq.getPartNotes(activeSequence)) {
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

	public Note[] getSequence() {
		return seq.getSequence(activeSequence);
	}

	public void setSequence(Note[] sequence) {
		seq.setSequence(sequence, activeSequence);
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
