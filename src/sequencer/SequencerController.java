package sequencer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import arrangement.SoloMute;
import masterModule.MstrMoGui;
import note.Note;
import note.NoteGenerator;
import note.NoteOn;

public class SequencerController implements ActionListener {

	private SequencerModel seq;
	private SequencerGui gui;
	private Timer clock;
	private long guiDelay;
	@SuppressWarnings("unused")
	private String title;
	private int bpm;
	private int activeSequence = 0;

	// Konstruktor
	public SequencerController(NoteGenerator key, int bpm, String title) {
		this.title = title;

		seq = new SequencerModel(key, bpm);

		gui = new SequencerGui(seq.getAvailibleMidiDevices(), title);

		clock = new Timer(500, this);

		setBpm(bpm);
		setTempo();

		// Add actionListeners to buttons
		gui.getDeviceChooser().addActionListener(e -> chooseMidiDevice());
		gui.getMidiChannelChooser().addActionListener(e -> chooseMidiChannel());
		gui.getGenerateButton().addActionListener(e -> generateSequence());
		gui.getNudgeLeft().addActionListener(e -> nudgeLeft());
		gui.getNudgeRight().addActionListener(e -> nudgeRight());
		gui.getRenamePattern().addActionListener(e -> renameSequence());

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
		gui.getGuiDelaySLider().addChangeListener(e -> changeGuiDelay());

		gui.repaintSequencer(seq.getSequence(activeSequence));
		gui.setPatternNames(seq.getSequences());
	}

	private void renameSequence() {
		seq.setSequenceName(activeSequence, gui.renameSequence(activeSequence));
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

	private void chooseSequence(int index) {
		gui.disablePatternChooser(activeSequence);
		if (seq.getRunning()) {
			seq.killLastNote(activeSequence);
		}
		activeSequence = index;
		setTempo();
		gui.enablePatternChooser(index);
		gui.getPartNotesChooser().setValue(seq.getPartNotesChoice(index));
		gui.getNrOfStepsChooser().setValue(seq.getNrOfSteps(index));
		gui.repaintSequencer(seq.getSequence(activeSequence));
		gui.repaint();
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

	private void changeGuiDelay() {
		if (!gui.getGuiDelaySLider().getValueIsAdjusting()) {
			guiDelay = gui.getGuiDelaySLider().getValue();
		}
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

	public void unSoloMute() {
		if (seq.getSoloMute() != SoloMute.AUDIBLE) {
			seq.unSoloMute();
			gui.setSoloMuteBar(seq.getSoloMute());
		}
	}

	public void muteButton() {
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
		setTempo();
		seq.initPlayVaribles();
		clock.start();
		gui.disableGui();
	}

	public void stopSequence() {
		gui.enableGui();
		seq.stopSequence(activeSequence);
		clock.stop();
		gui.unmarkActiveStep(seq.getCurrentStep(), seq.isFirstNote(), seq.getSequence(activeSequence));
	}

	public void setBpm(int bpm) {
		this.bpm = bpm;
	}

	public void setTempo() {
		int tempo = 60000 / bpm;
		switch (seq.getPartNotes(activeSequence)) {
		case "1 bar":
			tempo *= 4;
			break;
		case "1/2":
			tempo *= 2;
			break;
		case "1/4":
			break;
		case "1/8":
			tempo /= 2;
			break;
		case "1/16":
			tempo /= 4;
			break;
		}
		clock.setDelay(tempo);
	}

	public void setKey(NoteGenerator key) {
		seq.setKey(key);
	}

	public Info[] getAvailibleMidiDevices() {
		return seq.getAvailibleMidiDevices();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		seq.playStep(activeSequence);
		try {
			Thread.sleep(guiDelay);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
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
}
