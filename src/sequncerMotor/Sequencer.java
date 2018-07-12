package sequncerMotor;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import note.Note;
import note.NoteGenerator;
import note.NoteOn;

public class Sequencer {

	boolean running = false;
	// private Timer clock = new Timer(500, this);
	private MidiDevice device;
	private MidiDevice.Info[] infos;
	private Receiver rcvr;
	private long timeStamp = -1;
	private Note[] sequence;
	private int currentStep = 0;
	private ShortMessage noteOn = new ShortMessage();
	private ShortMessage noteOff = new ShortMessage();
	// private NoteGenerator key;
	private boolean firstNote;
	private boolean mute = false;
	private boolean solo = false;

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

	public void generateSequence(int nrOfSteps, NoteGenerator key, String generatorAlgorithm, boolean rndVeloIsChecked,
			int veloLow, int veloHigh, int octaveLow, int octaveHigh) {
		sequence = new Note[nrOfSteps];
		switch (generatorAlgorithm) {
		case "Rnd notes":
			sequence = key.getRndSequence(sequence, rndVeloIsChecked, veloLow, veloHigh, octaveLow, octaveHigh);
			break;
		case "Rnd notes, no dupl in row":
			sequence = key.getRndSeqNoDuplInRow(sequence, rndVeloIsChecked, veloLow, veloHigh, octaveLow, octaveHigh);
			break;
		case "Rnd notes and On/Hold/Off":
			sequence = key.getRndSequenceOnHoldOff(sequence, rndVeloIsChecked, veloLow, veloHigh, octaveLow,
					octaveHigh);
		case "Rnd notes, no dupl in row, On/Hold/Off":
			sequence = key.getRndSeqNoDuplInRowOnHoldOff(sequence, rndVeloIsChecked, veloLow, veloHigh, octaveLow,
					octaveHigh);
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
		running = true;
	}

	public void stopSequence() {
		killLastNote();
		running = false;
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
		if (!mute) {
			if (currentStep == 0 && !firstNote) {
				if (sequence[sequence.length - 1].getNoteOnButtonEnum() != NoteOn.HOLD) {
					try {
						noteOff.setMessage(ShortMessage.NOTE_OFF, 0, sequence[sequence.length - 1].getMidiNote(),
								sequence[currentStep].getVelo());
					} catch (InvalidMidiDataException e1) {
						e1.printStackTrace();
					}
				}
				if (sequence[currentStep].getNoteOnButtonEnum() != NoteOn.HOLD) {
					rcvr.send(noteOff, timeStamp);
				}
			} else if (currentStep != 0) {
				if (sequence[currentStep - 1].getNoteOnButtonEnum() != NoteOn.HOLD) {
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
				if (sequence[currentStep].getNoteOnButtonEnum() != NoteOn.HOLD) {
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
			if (sequence[currentStep].getNoteOnButtonEnum() == NoteOn.ON) {
				rcvr.send(noteOn, timeStamp);
			}
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

	public void nudgeLeft() {
		Note[] tempSeq = sequence.clone();
		for (int i = 0; i < sequence.length; i++) {
			if (i == sequence.length - 1) {
				sequence[i] = tempSeq[0];
			} else {
				sequence[i] = tempSeq[i + 1];
			}
		}
	}

	public void nudgeRight() {
		Note[] tempSeq = sequence.clone();
		for (int i = 0; i < sequence.length; i++) {
			if (i == 0) {
				sequence[i] = tempSeq[sequence.length - 1];
			} else {
				sequence[i] = tempSeq[i - 1];
			}
		}
	}

	public void mute() {
		if (!mute) {
			mute = true;
		} else {
			mute = false;
		}
	}

	public void solo() {
		if (!solo) {
			solo = true;
		} else {
			solo = false;
		}
	}

	public boolean getSolo() {
		return solo;
	}

	public boolean getMute() {
		return mute;
	}

	public void killLastNote() {
		if (currentStep == 0 && !firstNote) {
			if (sequence[sequence.length - 1].getNoteOnButtonEnum() != NoteOn.HOLD) {
				try {
					noteOff.setMessage(ShortMessage.NOTE_OFF, 0, sequence[sequence.length - 1].getMidiNote(),
							sequence[currentStep].getVelo());
				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}
			}
			rcvr.send(noteOff, timeStamp);
		} else if (currentStep != 0) {
			if (sequence[currentStep - 1].getNoteOnButtonEnum() != NoteOn.HOLD) {
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
			rcvr.send(noteOff, timeStamp);
		}
	}

	public boolean getRunning() {
		return running;
	}

	public void isRunning(boolean running) {
		this.running = running;
	}

}
