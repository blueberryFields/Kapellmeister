package controller;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.swing.SwingUtilities;

import Gui.PrototypeGui;
import sequncerMotor.Sequencer;

public class Controller {

	private Sequencer seq;
	private PrototypeGui gui;

	public Controller() {
		seq = new Sequencer();
		gui = new PrototypeGui(seq.getAvailibleMidiDevices());
		
		// Add actionListeners to buttons
		gui.getPlayButton().addActionListener(e -> playSequence());
		gui.getStopButton().addActionListener(e -> stopSequence());
		gui.getDeviceChooser().addActionListener(e -> chooseMidiDevice());
		gui.getGenerateButton().addActionListener(e -> generateSequence());
	}

	private void generateSequence() {
		seq.generateSequence(gui.getNrOfSteps(), gui.getKey());
	}

	private void chooseMidiDevice() {
		seq.chooseMidiDevice(gui.getChoosenDevice());
	}

	private void playSequence() {
		seq.playSequence();
	}

	private void stopSequence() {
		seq.stopSequence();
	}

	public Info[] getAvailibleMidiDevices() {
		return seq.getAvailibleMidiDevices();

	}
}
