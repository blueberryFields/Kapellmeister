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
		addActionListenerToDeviceChooser();
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

		// Add ActionListeners to singleSteps
		addActionListenersToNoteChooser();
		addActionListenersToVelocityChooser();
		addActionListenersToNoteOnButton();

		// Add changeListeners to Sliders
		// gui.getGuiDelaySLider().addChangeListener(e -> changeGuiDelay());

		gui.repaintSequencer(seq.getSequence(activeSequence));
		gui.setPatternNames(seq.getSequences());
	}

	private void addActionListenerToDeviceChooser() {
		gui.getDeviceChooser().addActionListener(e -> chooseMidiDevice());
	}

	private void removeActionListenerFromDeviceChooser() {
		gui.getDeviceChooser().removeActionListener(e -> chooseMidiDevice());
	}

	// WORK IN PROGRESS!!!
	private void refreshMidiDeviceList() {
		removeActionListenerFromDeviceChooser();
		seq.refreshMidiDeviceList();
		gui.setAvailibleDevices(seq.getAvailibleMidiDevices());
		addActionListenerToDeviceChooser();
	}

	private void renameSequence() {
		seq.setSequenceName(activeSequence, gui.renameSequence(activeSequence));
	}

	public String[] getSequenceNames() {
		return seq.getSequenceNames();
	}

	private void changePartNotes(String partNotes) {
		seq.setPartNotes(partNotes, activeSequence);
	}

	private void addActionListenersToSequenceChoosers() {
		for (int i = 0; i < gui.getPatternChoosers().length; i++) {
			int index = i;
			gui.getPatternChoosers()[i].addActionListener(e -> chooseSequence(index));
		}

	}

	public void chooseSequence(int activeSequence) {

		if (this.activeSequence != activeSequence) {
			gui.disablePatternChooser(this.activeSequence);
			gui.enablePatternChooser(activeSequence);
		}
		if (seq.getRunning()) {
			seq.killLastNote(this.activeSequence);
		}
		this.activeSequence = activeSequence;
		setPartNotes();
		gui.getPartNotesChooser().setValue(seq.getPartNotesChoice(activeSequence));
		gui.getNrOfStepsChooser().setValue(seq.getNrOfSteps(activeSequence));
		gui.repaintSequencer(seq.getSequence(activeSequence));
		gui.repaint();
	}

	public void killLastNote(int sequence) {
		seq.killLastNote(sequence);
	}

	public void open() {
		gui.open();
	}

	public void setTitle(String title) {
		this.title = title;
		gui.setTitle(title);
	}

	public String getTitle() {
		return gui.getTitle();
	}

	private void chooseMidiChannel() {
		seq.setMidiChannel((int) gui.getMidiChannelChooser().getSelectedItem() - 1);
	}

	private void changeOctaveHigh() {
		if (gui.getOctaveHigh() < gui.getOctaveLow()) {
			gui.setOctaveHigh(gui.getOctaveLow());
		}
	}

	private void changeOctaveLow() {
		if (gui.getOctaveLow() > gui.getOctaveHigh()) {
			gui.setOctaveLow(gui.getOctaveHigh());
		}
	}

	public void disposeGui() {
		gui.dispose();
	}

	public void solo() {
		if (seq.getSoloMute() != SoloMute.SOLO) {
			seq.solo();
		} else {
			seq.unSoloMute();
		}
		gui.setSoloMuteBar(seq.getSoloMute());
	}

	public void mute() {
		if (seq.getSoloMute() != SoloMute.MUTE) {
			seq.mute();
			if (seq.getRunning()) {
				seq.killLastNote(activeSequence);
			}
			gui.setSoloMuteBar(seq.getSoloMute());
		}
	}

	public void dynamicMute() {
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

	public void unSoloMute() {
		if (seq.getSoloMute() != SoloMute.AUDIBLE) {
			seq.unSoloMute();
			gui.setSoloMuteBar(seq.getSoloMute());
		}
	}

	public SoloMute getSoloMute() {
		return seq.getSoloMute();
	}

	private void nudgeLeft() {
		seq.nudgeLeft(activeSequence);
		checkHold();
		gui.repaintSequencer(seq.getSequence(activeSequence));
	}

	private void nudgeRight() {
		seq.nudgeRight(activeSequence);
		checkHold();
		gui.repaintSequencer(seq.getSequence(activeSequence));
	}

	private void changeVeloHigh() {
		if (gui.getVeloHighChooserValue() < gui.getVeloLowChooserValue()) {
			gui.setVeloHighChooserValue(gui.getVeloLowChooserValue());
		}
	}

	private void changeVeloLow() {
		if (gui.getVeloLowChooserValue() > gui.getVeloHighChooserValue()) {
			gui.setVeloLowChooserValue(gui.getVeloHighChooserValue());
		}
	}

	private void addActionListenersToNoteOnButton() {
		for (int i = 0; i < gui.getNoteOnButtonArray().length; i++) {
			int index = i;
			gui.getNoteOnButton(i).addActionListener(e -> clickNoteOnButton(index));
		}
	}

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

	private void Off(int index) {
		seq.getSingleStep(activeSequence, index).setHoldNote(-1);
		seq.getSingleStep(activeSequence, index).setNoteOn(NoteOn.OFF);
		gui.setNoteOnButtonText(index, "Off");
		checkHold(index);
	}

	private void On(int index) {
		seq.getSingleStep(activeSequence, index).setNoteOn(NoteOn.ON);
		gui.setNoteOnButtonText(index, "On");
		seq.getSingleStep(activeSequence, index).setHoldNote(-1);
		checkHold(index);
	}

	private void hold(int index) {
		seq.getSingleStep(activeSequence, index).setNoteOn(NoteOn.HOLD);
		gui.setNoteOnButtonText(index, "Hold");
		checkHold(index);
	}

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

	private void changeNrOfSteps(int nrOfSteps) {
		seq.changeNrOfSteps(nrOfSteps, activeSequence);
		gui.repaintSequencer(seq.getSequence(activeSequence));
	}

	private void generateSequence() {
		seq.generateSequence(gui.getNrOfSteps(), seq.getKey(), gui.getGeneratorAlgoRithmChooser(),
				gui.isRndVeloChecked(), gui.getVeloLowChooserValue(), gui.getVeloHighChooserValue(), gui.getOctaveLow(),
				gui.getOctaveHigh(), activeSequence);
		checkHold();
		gui.repaintSequencer(seq.getSequence(activeSequence));
	}

	private void chooseMidiDevice() {
		seq.chooseMidiDevice(gui.getChoosenDevice());
		// if (gui.getChoosenDevice() != 0) {
		// gui.getPlayButton().setEnabled(true);
		// } else {
		// gui.getPlayButton().setEnabled(false);
		// }
	}

	public void playSequence() {
		setPartNotes();
		seq.initPlayVaribles();
		gui.disableGui();
	}

	public void stopSequence() {
		seq.stopSequence(activeSequence);
		gui.enableGui();
		tickCounter = 0;
		gui.unmarkActiveStep(seq.getCurrentStep(), seq.isFirstNote(), seq.getSequence(activeSequence));
	}

	public void setKey(NoteGenerator key) {
		seq.setKey(key);
	}

	public Info[] getAvailibleMidiDevices() {
		return seq.getAvailibleMidiDevices();
	}

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

	public void tick() {
		tickCounter++;
		if (tickCounter == 1) {
			playStep();
		}
		if (tickCounter == partNotesThreshhold) {
			tickCounter = 0;
		}
	}

	public void playStep() {
		seq.playStep(activeSequence);
		// try {
		// Thread.sleep(guiDelay);
		// } catch (InterruptedException e1) {
		// e1.printStackTrace();
		// }
		gui.markActiveStep(seq.getCurrentStep(), seq.isFirstNote(), seq.getSequence(activeSequence));
		int tempStep = seq.getCurrentStep();
		tempStep++;
		if (tempStep == seq.getSequence(activeSequence).length) {
			tempStep = 0;
		}
		seq.setCurrentStep(tempStep++);
	}

	public void printSequence(int activeSequence) {
		String s = "";
		for (int i = 0; i < seq.getSequence(activeSequence).length; i++) {
			s += seq.getSingleStep(activeSequence, i).toString();
		}
		System.out.println(s);
	}

	public Note[] getSequence() {
		return seq.getSequence(activeSequence);
	}

	public void setSequence(Note[] sequence) {
		seq.setSequence(sequence, activeSequence);
	}

	public Sequence copySequence() {
		return seq.copySequence(activeSequence);
	}

	public void pasteSequence(Sequence sequence) {
		seq.pasteSequence(activeSequence, sequence);
		gui.repaintSequencer(seq.getSequence(activeSequence));
		gui.getPartNotesChooser().setValue(seq.getPartNotesChoice(activeSequence));
		gui.getNrOfStepsChooser().setValue(seq.getNrOfSteps(activeSequence));
		gui.getPatterChoosers()[activeSequence].setText(seq.getSequenceName(activeSequence));
		checkHold();
	}

	public JButton getCopyButton() {
		return gui.getCopyButton();
	}

	public JButton getPasteButton() {
		return gui.getPasteButton();
	}
}
