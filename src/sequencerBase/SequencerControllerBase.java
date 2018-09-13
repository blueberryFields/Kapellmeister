package sequencerBase;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.JButton;

import drumSequencer.DrumSequencerModel;
import note.Note;
import pattern.StandardPattern;
import standardSequencer.StandardSequencerController;
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
	public SequencerControllerBase(String title) {
		this.title = title;
	}

	// The following methods just add ActionListeners to different buttons n stuff
	// as their titles says

	protected void addActionListenerToDeviceChooser() {
		gui.getDeviceChooser().addActionListener(e -> chooseMidiDevice());
	}

	@SuppressWarnings("unused")
	private void removeActionListenerFromDeviceChooser() {
		gui.getDeviceChooser().removeActionListener(e -> chooseMidiDevice());
	}

	// WORK IN PROGRESS!!!
	protected void refreshMidiDeviceList() {
		// //removeActionListenerFromDeviceChooser();
		// seq.refreshMidiDeviceList();
		// gui.setAvailibleDevices(seq.getAvailibleMidiDevices());
		// //addActionListenerToDeviceChooser();
	}

	/**
	 * Makes a popup appear where you can type in a new name for the active pattern
	 */
	protected void renamePattern() {
		seq.setPatternName(activePattern, gui.renamePattern(activePattern));
	}

	/**
	 * @return an array of Strings containing the names of the different patterns
	 */
	public String[] getPatternNames() {
		return seq.getPatternNames();
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
	protected void chooseMidiChannel() {
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
				if (seq instanceof StandardSequencerModel) {
					((StandardSequencerModel) seq).killLastNote(activePattern);
				} 
				if (seq instanceof DrumSequencerModel) {
					((DrumSequencerModel) seq).killLastNote(activePattern);
				}
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
				if (seq instanceof StandardSequencerModel) {
					((StandardSequencerModel) seq).killLastNote(activePattern);
				} 
				if (seq instanceof DrumSequencerModel) {
					((DrumSequencerModel) seq).killLastNote(activePattern);
				}
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
	protected void changeVeloHigh() {
		if (gui.getVeloHighChooserValue() < gui.getVeloLowChooserValue()) {
			gui.setVeloHighChooserValue(gui.getVeloLowChooserValue());
		}
	}

	/**
	 * Check so the minimum value in the random velocity generator cant be higher
	 * than the maximum value
	 */
	protected void changeVeloLow() {
		if (gui.getVeloLowChooserValue() > gui.getVeloHighChooserValue()) {
			gui.setVeloLowChooserValue(gui.getVeloHighChooserValue());
		}
	}

	/**
	 * Sets which of the available mididevices to send notes to
	 */
	private void chooseMidiDevice() {
		seq.chooseMidiDevice(gui.getChoosenDevice());
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
		switch (seq.getPartNotesChoice(activePattern)) {
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
