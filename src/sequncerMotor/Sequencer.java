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

public class Sequencer {

	// boolean running = false;
	// private Timer clock = new Timer(500, this);
	private MidiDevice device;
	private MidiDevice.Info[] infos;
	private Receiver rcvr;
	private long timeStamp = -1;
	private Note[] sequence;
	private int currentStep = 0;
	private ShortMessage noteOn = new ShortMessage();
	private ShortMessage noteOff = new ShortMessage();
	private NoteGenerator key;
	private boolean firstNote;

	// Konstruktor
	public Sequencer() {
		infos = MidiSystem.getMidiDeviceInfo();
		initSeq();
	}

	public void initSeq() {
		sequence = new Note[8];
		for (int i = 0; i < sequence.length; i++) {
			sequence[i] = new Note();
		}
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

	public void generateSequence(int nrOfSteps, NoteGenerator key, boolean noDuplisChecked, boolean rndVeloIsChecked,
			int veloLow, int veloHigh) {
		sequence = new Note[nrOfSteps];
		if (noDuplisChecked) {
			sequence = key.getRndSeqNoDuplInRow(sequence, rndVeloIsChecked, veloLow, veloHigh);
		} else {
			sequence = key.getRndSequence(sequence, rndVeloIsChecked, veloLow, veloHigh);
		}
	}

	public void setStep(int index) {
		sequence[index] = new Note();
	}

	public void setSequence(Note[] sequence) {
		this.sequence = sequence;
	}

	public Note[] getSequence() {
		return sequence;
	}

	public Note getSingleStep(int index) {
		return sequence[index];
	}

	public void playSequence() {
		currentStep = 0;
		firstNote = true;
	}

	public void stopSequence() {
		try {
			noteOff.setMessage(ShortMessage.NOTE_OFF, 0, sequence[currentStep].getMidiNote(), 100);
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

	public void playStep() {
		if (currentStep == 0 && !firstNote) {
			if (sequence[sequence.length - 1].getNoteOnButtonEnum() != NoteOnButtonEnum.HOLD) {
				try {
					noteOff.setMessage(ShortMessage.NOTE_OFF, 0, sequence[sequence.length - 1].getMidiNote(),
							sequence[currentStep].getVelo());
				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}
			}
			if (sequence[currentStep].getNoteOnButtonEnum() != NoteOnButtonEnum.HOLD) {
				rcvr.send(noteOff, timeStamp);
			}
		} else if (currentStep != 0) {	
			if (sequence[currentStep - 1].getNoteOnButtonEnum() != NoteOnButtonEnum.HOLD) {
				try {
					noteOff.setMessage(ShortMessage.NOTE_OFF, 0, sequence[currentStep - 1].getMidiNote(),
							sequence[currentStep].getVelo());
				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}
			} else {
				try {
					noteOff.setMessage(ShortMessage.NOTE_OFF, 0, sequence[currentStep - 1].getHoldNote(),
							sequence[currentStep].getVelo());
				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}
			}
			if (sequence[currentStep].getNoteOnButtonEnum() != NoteOnButtonEnum.HOLD) {
				rcvr.send(noteOff, timeStamp);
			}
		} else {
			firstNote = false;
		}
		try {
			noteOn.setMessage(ShortMessage.NOTE_ON, 0, sequence[currentStep].getMidiNote(),
					sequence[currentStep].getVelo());
		} catch (InvalidMidiDataException e1) {
			e1.printStackTrace();
		}
		if (sequence[currentStep].getNoteOnButtonEnum() == NoteOnButtonEnum.ON) {
			rcvr.send(noteOn, timeStamp);
		}
	}

	public void setCurrentStep(int step) {
		currentStep = step;
	}

	public int getCurrentStep() {
		return currentStep;
	}

	public boolean isFirstNote() {
		return firstNote;
	}

}
