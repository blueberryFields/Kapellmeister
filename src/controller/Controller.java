package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Gui.PrototypeGui;
import sequncerMotor.Note;
import sequncerMotor.Sequencer;

public class Controller implements ActionListener {

	private Sequencer seq;
	private PrototypeGui gui;
	private Timer clock;

	//Konstruktor
	public Controller() {
		seq = new Sequencer();
		gui = new PrototypeGui(seq.getAvailibleMidiDevices());
		clock = new Timer(500, this);

		// Add actionListeners to buttons
		gui.getPlayButton().addActionListener(e -> playSequence());
		gui.getStopButton().addActionListener(e -> stopSequence());
		gui.getDeviceChooser().addActionListener(e -> chooseMidiDevice());
		gui.getGenerateButton().addActionListener(e -> generateSequence());

		// Add ActionListeners to Jspinners
		gui.getNrOfStepsChooser().addChangeListener(e -> changeNrOfSteps(gui.getNrOfSteps(), seq.getSequence()));

		addActionListenersToNoteChooser();
		addActionListenersToVelocityChooser();

		gui.repaintSequencer(seq.getSequence());
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

	// private void removeActionListenersFromNoteChooser() {
	// ChangeListener[] cl;
	// for (int i = 0; i < gui.getNoteChooserArray().length; i++) {
	// cl = gui.getNoteChooser(i).getChangeListeners();
	// for (int j = 0; i < cl.length; i++) {
	// gui.getNoteChooser(i).removeChangeListener(cl[j]);
	// }
	// }
	// }

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
		for (int i = 0; i < sequence.length; i++) {
			tempArray[i] = sequence[i];
		}
		if (tempArray[tempArray.length-1] == null) {
			tempArray[tempArray.length-1] = new Note();
		}
		seq.setSequence(tempArray);
		gui.repaintSequencer(seq.getSequence());
	}

	private void generateSequence() {
		seq.generateSequence(gui.getNrOfSteps(), gui.getKey());
		gui.repaintSequencer(seq.getSequence());
	}

	private void chooseMidiDevice() {
		seq.chooseMidiDevice(gui.getChoosenDevice());
		if(gui.getChoosenDevice() != 0) {
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
