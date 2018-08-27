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
	 * 
	 */

	/**
	 * Contains most of the logic and is the heart of the sequencer
	 */
	protected SequencerModelBase seq;
	/**
	 * Contains the graphical user interface for the sequencer
	 */
	protected SequencerGuiBase gui;
	/**
	 * Stores the title/namename of the sequencer
	 */
	@SuppressWarnings("unused")
	protected String title;
	/**
	 * Keeps track of which pattern is currently the active one
	 */
	protected int activePattern = 0;
	/**
	 * Keeps track of which tick in the tickgrid we´re currently at
	 */
	protected int tickCounter = 0;
	/**
	 * The threshhold for where in the tickgrid a note will be played, is used to
	 * determine how often a note will be played, i.e. setting the partnotes
	 */
	protected int partNotesThreshhold;

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
	public SequencerControllerBase(String title) {
		this.title = title;

		// setPartNotes();

		// Add actionListeners to buttons
		gui.getMidiChannelChooser().addActionListener(e -> chooseMidiChannel());
		// gui.getGenerateButton().addActionListener(e -> generatePattern());
		// gui.getNudgeLeft().addActionListener(e -> nudgeLeft());
		// gui.getNudgeRight().addActionListener(e -> nudgeRight());
		gui.getRenamePattern().addActionListener(e -> renamePattern());
		gui.getRefreshButton().addActionListener(e -> refreshMidiDeviceList());

		// Add ActionListeners to Jspinners
		gui.getNrOfStepsChooser().addChangeListener(e -> changeNrOfSteps(gui.getNrOfSteps()));
		// addActionListenersToPatternChoosers();
		// gui.getPartNotesChooser().addChangeListener(e ->
		// changePartNotes(gui.getPartnotes()));
		gui.getVeloLowChooser().addChangeListener(e -> changeVeloLow());
		gui.getVeloHighChooser().addChangeListener(e -> changeVeloHigh());
		// gui.getOctaveLowChooser().addChangeListener(e -> changeOctaveLow());
		// gui.getOctaveHighChooser().addChangeListener(e -> changeOctaveHigh());

		// Add ActionListeners to ComboBox
		addActionListenerToDeviceChooser();

		// Add ActionListeners to singleSteps
		// addActionListenersToNoteChooser();
		// addActionListenersToVelocityChooser();
		// addActionListenersToNoteOnButton();

		// gui.repaintSequencer(seq.getPattern(activePattern));
		// gui.setPatternNames(seq.getPatterns());
	}

	// The following methods just add ActionListeners to different buttons n stuff
	// as their titles says

	private void addActionListenersToPatternChoosers() {
		for (int i = 0; i < gui.getPatternChoosers().length; i++) {
			int index = i;
			gui.getPatternChoosers()[i].addActionListener(e -> choosePattern(index));
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
	 * Sets which of the available mididevices to send notes to
	 */
	private void chooseMidiDevice() {
		seq.chooseMidiDevice(gui.getChoosenDevice());
	}

	// /**
	/**
	 * 
	 * 
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
		// checkHold();
	}

	 /**
	 * Gets the sequencer ready for playback i.e. Collects needed info from gui
	 and
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
	 gui.unmarkActiveStep(seq.getCurrentStep(), seq.isFirstNote(),
	 seq.getPattern(activePattern));
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
		seq.playStep(activePattern);
		gui.markActiveStep(seq.getCurrentStep(), seq.isFirstNote(), seq.getPattern(activePattern));
		int tempStep = seq.getCurrentStep();
		tempStep++;
		if (tempStep == seq.getPattern(activePattern).length) {
			tempStep = 0;
		}
		seq.setCurrentStep(tempStep++);
	}

	// /**
	// * Prints choosen pattern to sysout. Good for doubleChecking Gui so it
	// reflects
	// * what notes there really is for example
	// *
	// * @param the
	// * pattern choosen to be printed
	// */
	// public void printPattern(int pattern) {
	// String s = "";
	// for (int i = 0; i < seq.getPattern(pattern).length; i++) {
	// s += seq.getSingleStep(pattern, i).toString();
	// }
	// System.out.println(s);
	// }

	// /**
	// * @param key
	// * the musical key from wich note will be generated
	// */
	// public void setKey(NoteGenerator key) {
	// seq.setKey(key);
	// }

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
		return title;
	}

	public SoloMute getSoloMute() {
		return seq.getSoloMute();
	}

}
