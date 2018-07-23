package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Gui.MstrMoGui;
import Gui.SequencerGui;
import model.SequencerModel;
import note.Note;
import note.NoteGenerator;
import note.NoteOn;

public class SequencerController implements ActionListener {

	private SequencerModel seq;
	private SequencerGui gui;
	private Timer clock;
	private long guiDelay;

	// Konstruktor
	public SequencerController(NoteGenerator key, int bpm) {
		seq = new SequencerModel(key, bpm);

		gui = new SequencerGui(seq.getAvailibleMidiDevices());

		clock = new Timer(500, this);
		setTempo();

		// Add actionListeners to buttons
		// gui.getPlayButton().addActionListener(e -> playSequence());
		// gui.getStopButton().addActionListener(e -> stopSequence());
		gui.getDeviceChooser().addActionListener(e -> chooseMidiDevice());
		gui.getMidiChannelChooser().addActionListener(e -> chooseMidiChannel());
		gui.getGenerateButton().addActionListener(e -> generateSequence());
		gui.getNudgeLeft().addActionListener(e -> nudgeLeft());
		gui.getNudgeRight().addActionListener(e -> nudgeRight());
		gui.getMute().addActionListener(e -> mute());
		gui.getSolo().addActionListener(e -> solo());

		// Add ActionListeners to Jspinners
		gui.getNrOfStepsChooser().addChangeListener(e -> changeNrOfSteps(gui.getNrOfSteps(), seq.getSequence()));
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

		gui.repaintSequencer(seq.getSequence());
	}

	public void setTitle(String title) {
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

	private void solo() {
		// TODO
		gui.setSoloColor();
		if (seq.getMute()) {
			seq.mute();
			gui.setMuteColor();
		}
		seq.solo();
	}

	private void mute() {
		gui.setMuteColor();
		if (seq.getSolo()) {
			seq.solo();
			gui.setSoloColor();
		}
		seq.mute();
		if (seq.getRunning()) {
			seq.killLastNote();
		}
	}

	private void nudgeLeft() {
		seq.nudgeLeft();
		checkHold();
		gui.repaintSequencer(seq.getSequence());
	}

	private void nudgeRight() {
		seq.nudgeRight();
		checkHold();
		gui.repaintSequencer(seq.getSequence());
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
		seq.getSingleStep(index).setHoldNote(-1);
		seq.getSingleStep(index).setNoteOn(NoteOn.OFF);
		gui.setNoteOnButtonText(index, "Off");
		checkHold(index);
	}

	private void On(int index) {
		seq.getSingleStep(index).setNoteOn(NoteOn.ON);
		gui.setNoteOnButtonText(index, "On");
		seq.getSingleStep(index).setHoldNote(-1);
		checkHold(index);
	}

	private void hold(int index) {
		seq.getSingleStep(index).setNoteOn(NoteOn.HOLD);
		gui.setNoteOnButtonText(index, "Hold");
		checkHold(index);
	}

	private void checkHold() {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < seq.getSequence().length; j++) {
				if (j == 0) {
					if (seq.getSingleStep(j).getNoteOn() == NoteOn.HOLD) {
						if (seq.getSingleStep(seq.getSequence().length - 1).getNoteOn() != NoteOn.HOLD) {
							seq.getSingleStep(j)
									.setHoldNote(seq.getSingleStep(seq.getSequence().length - 1).getMidiNote());
						} else {
							seq.getSingleStep(j)
									.setHoldNote(seq.getSingleStep(seq.getSequence().length - 1).getHoldNote());
						}
					}
				} else {
					if (seq.getSingleStep(j).getNoteOn() == NoteOn.HOLD) {
						if (seq.getSingleStep(j - 1).getNoteOn() != NoteOn.HOLD) {
							seq.getSingleStep(j).setHoldNote(seq.getSingleStep(j - 1).getMidiNote());
						} else {
							seq.getSingleStep(j).setHoldNote(seq.getSingleStep(j - 1).getHoldNote());
						}
					}
				}
			}
		}
	}

	private void checkHold(int index) {
		int loopStart = index;
		for (int i = 0; i < 2; i++) {
			for (int j = loopStart; j < seq.getSequence().length; j++) {
				if (j == 0) {
					if (seq.getSingleStep(j).getNoteOn() == NoteOn.HOLD) {
						if (seq.getSingleStep(seq.getSequence().length - 1).getNoteOn() != NoteOn.HOLD) {
							seq.getSingleStep(j)
									.setHoldNote(seq.getSingleStep(seq.getSequence().length - 1).getMidiNote());
						} else {
							seq.getSingleStep(j)
									.setHoldNote(seq.getSingleStep(seq.getSequence().length - 1).getHoldNote());
						}
					}
				} else {
					if (seq.getSingleStep(j).getNoteOn() == NoteOn.HOLD) {
						if (seq.getSingleStep(j - 1).getNoteOn() != NoteOn.HOLD) {
							seq.getSingleStep(j).setHoldNote(seq.getSingleStep(j - 1).getMidiNote());
						} else {
							seq.getSingleStep(j).setHoldNote(seq.getSingleStep(j - 1).getHoldNote());
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
					seq.getSingleStep(index).changeNote((String) gui.getNoteChooser(index).getValue());
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
					seq.getSingleStep(index).setVelo((int) gui.getVelocityChooser(index).getValue());
				}
			});
		}
	}

	private void changeNrOfSteps(int nrOfSteps, Note[] sequence) {
		Note[] tempArray = new Note[nrOfSteps];
		if (nrOfSteps > sequence.length) {
			for (int i = 0; i < sequence.length; i++) {
				tempArray[i] = sequence[i];
			}
			if (tempArray[tempArray.length - 1] == null) {
				tempArray[tempArray.length - 1] = new Note();
			}
		} else {
			for (int i = 0; i < nrOfSteps; i++) {
				tempArray[i] = sequence[i];
			}
		}
		seq.setSequence(tempArray);
		gui.repaintSequencer(seq.getSequence());
	}

	private void generateSequence() {
		seq.generateSequence(gui.getNrOfSteps(), seq.getKey(), gui.getGeneratorAlgoRithmChooser(),
				gui.isRndVeloChecked(), gui.getVeloLowChooserValue(), gui.getVeloHighChooserValue(), gui.getOctaveLow(),
				gui.getOctaveHigh());
		checkHold();
		gui.repaintSequencer(seq.getSequence());
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
		seq.playSequence();
		clock.start();
		gui.disableGui();
	}

	public void stopSequence() {
		gui.enableGui();
		seq.stopSequence();
		clock.stop();
		gui.unmarkActiveStep(seq.getCurrentStep(), seq.isFirstNote(), seq.getSequence());
	}

	public void setBpm(int bpm) {
		seq.setBpm(bpm);
	}
	
	public void setTempo() {
		int tempo = 60000 / seq.getBpm();
		switch (gui.getPartnotes()) {
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
		seq.playStep();
		try {
			Thread.sleep(guiDelay);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		gui.markActiveStep(seq.getCurrentStep(), seq.isFirstNote(), seq.getSequence());
		int tempStep = seq.getCurrentStep();
		tempStep++;
		if (tempStep == seq.getSequence().length) {
			tempStep = 0;
		}
		seq.setCurrentStep(tempStep++);
	}

	public void printSequence() {
		String s = "";
		for (int i = 0; i < seq.getSequence().length; i++) {
			s += seq.getSingleStep(i).toString();
		}
		System.out.println(s);
	}
}
