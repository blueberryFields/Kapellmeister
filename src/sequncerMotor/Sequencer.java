package sequncerMotor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.swing.Timer;

public class Sequencer implements ActionListener {

	// boolean running = false;
	Timer clock = new Timer(500, this);
	MidiDevice device;
	MidiDevice.Info[] infos;
	Receiver rcvr;
	long timeStamp = -1;
	int[] sequence;
	int currentStep = 0;
	ShortMessage noteOn = new ShortMessage();
	ShortMessage noteOff = new ShortMessage();

	// Constructor
	public Sequencer() {
		infos = MidiSystem.getMidiDeviceInfo();
	}

	public void chooseMidiDevice(int index) {
		try {
			device = MidiSystem.getMidiDevice(infos[index]);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		if (!(device.isOpen())) {
			try {
				device.open();
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
		}
		try {
			rcvr = MidiSystem.getReceiver();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	public MidiDevice.Info[] getAvailibleMidiDevices() {
		return infos;
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

	public void playTestNote() {
		ShortMessage testNoteOn = new ShortMessage();
		try {
			testNoteOn.setMessage(ShortMessage.NOTE_ON, 0, 60, 100);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		ShortMessage testNoteOff = new ShortMessage();
		try {
			testNoteOff.setMessage(ShortMessage.NOTE_OFF, 0, 60, 100);
		} catch (InvalidMidiDataException e1) {
			e1.printStackTrace();
		}

		rcvr.send(testNoteOn, timeStamp);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		rcvr.send(testNoteOff, timeStamp);
	}

	public void generateSequence(int nrOfSteps, NoteGenerator key) {
		sequence = new int[nrOfSteps];
		for (int i = 0; i < sequence.length; i++) {
			sequence[i] = key.getRandomNote();
		}
	}

	public int[] getSequence() {
		return sequence;
	}

	public void playSequence() {
		currentStep = 0;
		clock.start();
	}

	public void stopSequence() {
		clock.stop();
		try {
			noteOff.setMessage(ShortMessage.NOTE_OFF, 0, sequence[currentStep - 1], 100);
		} catch (InvalidMidiDataException e1) {
			e1.printStackTrace();
		}
		rcvr.send(noteOff, timeStamp);
	}

	public void closeDevice() {
		if (device.isOpen()) {
			device.close();
		}
	}

	public void closeRcvr() {
		rcvr.close();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			noteOn.setMessage(ShortMessage.NOTE_ON, 0, sequence[currentStep], 100);
		} catch (InvalidMidiDataException e1) {
			e1.printStackTrace();
		}
		rcvr.send(noteOn, timeStamp);
		if (currentStep > 0) {
			try {
				noteOff.setMessage(ShortMessage.NOTE_OFF, 0, sequence[currentStep - 1], 100);
			} catch (InvalidMidiDataException e1) {
				e1.printStackTrace();
			}
			rcvr.send(noteOff, timeStamp);
		}
		currentStep++;
		if (currentStep == sequence.length) {
			currentStep = 0;
		}
	}

	public void setBpm(int bpm) {

	}

	public void setCurrentStep(int step) {
		currentStep = step;
	}

}
