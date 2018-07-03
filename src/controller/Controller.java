package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.MidiDevice.Info;
import javax.swing.Timer;

import Gui.PrototypeGui;
import sequncerMotor.Sequencer;

public class Controller implements ActionListener {

	private Sequencer seq;
	private PrototypeGui gui;
	private Timer clock;

	public Controller() {
		seq = new Sequencer();
		gui = new PrototypeGui(seq.getAvailibleMidiDevices());
		clock = new Timer(500, this);

		// Add actionListeners to buttons
		gui.getPlayButton().addActionListener(e -> playSequence());
		gui.getStopButton().addActionListener(e -> stopSequence());
		gui.getDeviceChooser().addActionListener(e -> chooseMidiDevice());
		gui.getGenerateButton().addActionListener(e -> generateSequence());
	}

	private void generateSequence() {
		seq.generateSequence(gui.getNrOfSteps(), gui.getKey());
		gui.repaintSequencer(seq.getSequence());
	}

	private void chooseMidiDevice() {
		seq.chooseMidiDevice(gui.getChoosenDevice());
	}

	private void playSequence() {
		setTempo(gui.getBpm(), gui.getPartnotes());
		gui.disableGui();
		seq.playSequence();
		clock.start();
	}

	private void stopSequence() {
		seq.stopSequence();
		clock.stop();
		gui.enableGui();
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
		gui.markActiveStep(seq.getCurrentStep(), seq.isFirstNote(), seq.getSequence());
	}

}
