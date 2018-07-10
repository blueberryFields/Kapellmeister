package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Gui.PrototypeGui;
import sequncerMotor.Note;
import sequncerMotor.NoteOnButtonEnum;
import sequncerMotor.Sequencer;

public class Controller implements ActionListener {

	private Sequencer seq;
	private PrototypeGui gui;
	private Timer clock;

	// Konstruktor
	public Controller() {
		seq = new Sequencer();
		gui = new PrototypeGui(seq.getAvailibleMidiDevices());
		clock = new Timer(500, this);

		// Add actionListeners to buttons
		gui.getPlayButton().addActionListener(e -> playSequence());
		gui.getStopButton().addActionListener(e -> stopSequence());
		gui.getDeviceChooser().addActionListener(e -> chooseMidiDevice());
		gui.getGenerateButton().addActionListener(e -> generateSequence());
		gui.getNudgeLeft().addActionListener(e -> nudgeLeft());
		gui.getNudgeRight().addActionListener(e -> nudgeRight());

		// Add ActionListeners to Jspinners
		gui.getNrOfStepsChooser().addChangeListener(e -> changeNrOfSteps(gui.getNrOfSteps(), seq.getSequence()));
		gui.getVeloLowChooser().addChangeListener(e -> changeVeloLow());
		gui.getVeloHighChooser().addChangeListener(e -> changeVeloHigh());

		// Add ActionListeners to singleSteps
		addActionListenersToNoteChooser();
		addActionListenersToVelocityChooser();
		addActionListenersToNoteOnButton();

		gui.repaintSequencer(seq.getSequence());
	}
	
	private void nudgeLeft() {
		seq.nudgeLeft();
		gui.repaintSequencer(seq.getSequence());
	}
	
	private void nudgeRight() {
		seq.nudgeRight();
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
		seq.getSingleStep(index).setNoteOnButtonEnum(NoteOnButtonEnum.OFF);
		gui.setNoteOnButtonText(index, "Off");
		setHold(index);
	}

	private void On(int index) {
		seq.getSingleStep(index).setNoteOnButtonEnum(NoteOnButtonEnum.ON);
		gui.setNoteOnButtonText(index, "On");
		seq.getSingleStep(index).setHoldNote(-1);
		setHold(index);
	}

	private void hold(int index) {
		seq.getSingleStep(index).setNoteOnButtonEnum(NoteOnButtonEnum.HOLD);
		gui.setNoteOnButtonText(index, "Hold");
		setHold(index);
	}

	private void setHold(int index) {
		int loopStart = index;
		for (int i = 0; i < 2; i++) {
			for (int j = loopStart; j < seq.getSequence().length; j++) {
				if (j == 0) {
					if (seq.getSingleStep(j).getNoteOnButtonEnum() == NoteOnButtonEnum.HOLD) {
						if (seq.getSingleStep(seq.getSequence().length - 1)
								.getNoteOnButtonEnum() != NoteOnButtonEnum.HOLD) {
							seq.getSingleStep(j)
									.setHoldNote(seq.getSingleStep(seq.getSequence().length - 1).getMidiNote());
						} else {
							seq.getSingleStep(j)
									.setHoldNote(seq.getSingleStep(seq.getSequence().length - 1).getHoldNote());
						}
					}
				} else {
					if (seq.getSingleStep(j).getNoteOnButtonEnum() == NoteOnButtonEnum.HOLD) {
						if (seq.getSingleStep(j - 1).getNoteOnButtonEnum() != NoteOnButtonEnum.HOLD) {
							seq.getSingleStep(j).setHoldNote(seq.getSingleStep(j - 1).getMidiNote());
						} else {
							seq.getSingleStep(j).setHoldNote(seq.getSingleStep(j - 1).getHoldNote());
						}
					}
				}
			}
			loopStart = 0;
		}
		// for (int i = 0; i < seq.getSequence().length; i++) {
		// if (i == 0) {
		// if (seq.getSingleStep(i).getNoteOnButtonEnum() == NoteOnButtonEnum.HOLD) {
		// if (seq.getSingleStep(seq.getSequence().length - 1).getNoteOnButtonEnum() !=
		// NoteOnButtonEnum.HOLD) {
		// seq.getSingleStep(i).setHoldNote(seq.getSingleStep(seq.getSequence().length -
		// 1).getMidiNote());
		// } else {
		// seq.getSingleStep(i).setHoldNote(seq.getSingleStep(seq.getSequence().length -
		// 1).getHoldNote());
		// }
		// }
		// } else {
		// if (seq.getSingleStep(i).getNoteOnButtonEnum() == NoteOnButtonEnum.HOLD) {
		// if (seq.getSingleStep(i - 1).getNoteOnButtonEnum() != NoteOnButtonEnum.HOLD)
		// {
		// seq.getSingleStep(i).setHoldNote(seq.getSingleStep(i - 1).getMidiNote());
		// } else {
		// seq.getSingleStep(i).setHoldNote(seq.getSingleStep(i - 1).getHoldNote());
		// }
		// }
		// }
		// }
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
		seq.generateSequence(gui.getNrOfSteps(), gui.getKey(), gui.getGeneratorAlgoRithmChooser(), gui.isRndVeloChecked(),
				gui.getVeloLowChooserValue(), gui.getVeloHighChooserValue());
		gui.repaintSequencer(seq.getSequence());
	}

	private void chooseMidiDevice() {
		seq.chooseMidiDevice(gui.getChoosenDevice());
		if (gui.getChoosenDevice() != 0) {
			gui.getPlayButton().setEnabled(true);
		} else {
			gui.getPlayButton().setEnabled(false);
		}
	}

	private void playSequence() {
		setTempo(gui.getBpm(), gui.getPartnotes());
		seq.playSequence();
		clock.start();
		gui.disableGui();
	}

	private void stopSequence() {
		gui.enableGui();
		seq.stopSequence();
		clock.stop();
		gui.unmarkActiveStep(seq.getCurrentStep(), seq.isFirstNote(), seq.getSequence());
	}

	public void setTempo(int bpm, String partNotes) {
		int tempo = 60000 / bpm;
		switch (partNotes) {
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

	public Info[] getAvailibleMidiDevices() {
		return seq.getAvailibleMidiDevices();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		seq.playStep();
		try {
			Thread.sleep(85);
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
