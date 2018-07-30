package model;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import note.Note;
import note.NoteGenerator;
import note.NoteOn;

public class SequencerModel {

	boolean running = false;
	// private Timer clock = new Timer(500, this);
	private MidiDevice device;
	private MidiDevice.Info[] infos;
	private Receiver rcvr;
	private long timeStamp = -1;
	private Note[] sequence;
	private Note[][] sequences;
	private int currentStep = 0;
	private ShortMessage noteOn = new ShortMessage();
	private ShortMessage noteOff = new ShortMessage();
	private ShortMessage midiNote = new ShortMessage();
	// private NoteGenerator key;
	private boolean firstNote;
	private SoloMute soloMute;
	// private boolean mute = false;
	// private boolean solo = false;
	private int midiChannel = 0;
	private long tempoWait;

	private NoteGenerator key;
	// private int bpm;

	// Konstruktor
	public SequencerModel(NoteGenerator key, int bpm) {
		infos = MidiSystem.getMidiDeviceInfo();
		this.key = key;
		// this.bpm = bpm;
		initSeq();
	}

	public void initSeq() {
		sequences = new Note[8][8];
		soloMute = SoloMute.NONE;
		for (int i = 0; i < sequences.length; i++) {
			for (int j = 0; j < sequences[i].length; j++)
				sequences[i][j] = new Note();
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

	public void setMidiChannel(int midiChannel) {
		this.midiChannel = midiChannel;
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
			int veloLow, int veloHigh, int octaveLow, int octaveHigh, int activeSequence) {
		sequences[activeSequence] = new Note[nrOfSteps];
		switch (generatorAlgorithm) {
		case "Rnd notes":
			sequences[activeSequence] = key.getRndSequence(sequences[activeSequence], rndVeloIsChecked, veloLow,
					veloHigh, octaveLow, octaveHigh);
			break;
		case "Rnd notes, no dupl in row":
			sequences[activeSequence] = key.getRndSeqNoDuplInRow(sequences[activeSequence], rndVeloIsChecked, veloLow,
					veloHigh, octaveLow, octaveHigh);
			break;
		case "Rnd notes and On/Hold/Off":
			sequences[activeSequence] = key.getRndSequenceOnHoldOff(sequences[activeSequence], rndVeloIsChecked,
					veloLow, veloHigh, octaveLow, octaveHigh);
			break;
		case "Rnd notes, no dupl in row, On/Hold/Off":
			sequences[activeSequence] = key.getRndSeqNoDuplInRowOnHoldOff(sequences[activeSequence], rndVeloIsChecked,
					veloLow, veloHigh, octaveLow, octaveHigh);
			break;
		}
	}

	public void setStep(int index) {
		sequence[index] = new Note();
	}

	public void setSequence(Note[] sequence, int activeSequence) {
		sequences[activeSequence] = sequence;
	}

	public Note[] getSequence(int activeSequence) {
		return sequences[activeSequence];
	}

	public Note getSingleStep(int activeSequence, int index) {
		return sequences[activeSequence][index];
	}

	public void initPlayVaribles() {
		currentStep = 0;
		firstNote = true;
		running = true;
	}

	public void stopSequence(int activeSequence) {
		killLastNote(activeSequence);
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

	public void playStep(int activeSequence) {
		if (soloMute != SoloMute.MUTE) {
			if (currentStep == 0 && !firstNote) {
				if (sequences[activeSequence][sequences[activeSequence].length - 1].getNoteOn() != NoteOn.HOLD) {
					try {
						noteOff.setMessage(ShortMessage.NOTE_OFF, midiChannel,
								sequences[activeSequence][sequences[activeSequence].length - 1].getMidiNote(),
								sequences[activeSequence][currentStep].getVelo());
					} catch (InvalidMidiDataException e1) {
						e1.printStackTrace();
					}
				}
				if (sequences[activeSequence][currentStep].getNoteOn() != NoteOn.HOLD) {
					try {
						rcvr.send(noteOff, timeStamp);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (currentStep != 0) {
				if (sequences[activeSequence][currentStep - 1].getNoteOn() != NoteOn.HOLD) {
					try {
						noteOff.setMessage(ShortMessage.NOTE_OFF, midiChannel,
								sequences[activeSequence][currentStep - 1].getMidiNote(),
								sequences[activeSequence][currentStep].getVelo());
					} catch (InvalidMidiDataException e1) {
						e1.printStackTrace();
					}
				} else {
					try {
						noteOff.setMessage(ShortMessage.NOTE_OFF, midiChannel,
								sequences[activeSequence][currentStep - 1].getHoldNote(),
								sequences[activeSequence][currentStep].getVelo());
					} catch (InvalidMidiDataException e1) {
						e1.printStackTrace();
					}
				}
				if (sequences[activeSequence][currentStep].getNoteOn() != NoteOn.HOLD) {
					try {
						rcvr.send(noteOff, timeStamp);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				firstNote = false;
			}
			try {
				noteOn.setMessage(ShortMessage.NOTE_ON, midiChannel,
						sequences[activeSequence][currentStep].getMidiNote(),
						sequences[activeSequence][currentStep].getVelo());
			} catch (InvalidMidiDataException e1) {
				e1.printStackTrace();
			}
			if (sequences[activeSequence][currentStep].getNoteOn() == NoteOn.ON) {
				try {
					rcvr.send(noteOn, timeStamp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void playStep2() {
		if (soloMute != SoloMute.MUTE) {
			// Set midiOn message
			try {
				midiNote.setMessage(ShortMessage.NOTE_ON, midiChannel, sequence[currentStep].getMidiNote(),
						sequence[currentStep].getVelo());
			} catch (InvalidMidiDataException e1) {
				e1.printStackTrace();
			}
			// Send midiNote
			if (sequence[currentStep].getNoteOn() == NoteOn.ON) {
				try {
					rcvr.send(midiNote, timeStamp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// Wait a little
			try {
				Thread.sleep(tempoWait);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// set NoteOff message
			try {
				midiNote.setMessage(ShortMessage.NOTE_OFF, midiNote.getChannel(), midiNote.getData1(),
						midiNote.getData2());
			} catch (InvalidMidiDataException e1) {
				e1.printStackTrace();
			}
			// Send noteOffmessage
			try {
				rcvr.send(midiNote, timeStamp);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void setTempo(int tempo) {
		this.tempoWait = (long) tempo;
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

	public void nudgeLeft(int activeSequence) {
		Note[] tempSeq = sequences[activeSequence].clone();
		for (int i = 0; i < sequences[activeSequence].length; i++) {
			if (i == sequences[activeSequence].length - 1) {
				sequences[activeSequence][i] = tempSeq[0];
			} else {
				sequences[activeSequence][i] = tempSeq[i + 1];
			}
		}
	}

	public void nudgeRight(int activeSequence) {
		Note[] tempSeq = sequences[activeSequence].clone();
		for (int i = 0; i < sequences[activeSequence].length; i++) {
			if (i == 0) {
				sequences[activeSequence][i] = tempSeq[sequences[activeSequence].length - 1];
			} else {
				sequences[activeSequence][i] = tempSeq[i - 1];
			}
		}
	}

	public void mute() {
		soloMute = SoloMute.MUTE;
	}

	public void solo() {
		soloMute = SoloMute.SOLO;
	}

	public void unSoloMute() {
		soloMute = SoloMute.NONE;
	}

	public void killLastNote(int activeSequence) {
		if (currentStep == 0 && !firstNote) {
			if (sequences[activeSequence][sequences[activeSequence].length - 1].getNoteOn() != NoteOn.HOLD) {
				try {
					noteOff.setMessage(ShortMessage.NOTE_OFF, 0,
							sequences[activeSequence][sequences[activeSequence].length - 1].getMidiNote(),
							sequences[activeSequence][currentStep].getVelo());
				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}
			}
			rcvr.send(noteOff, timeStamp);
		} else if (currentStep != 0) {
			if (sequences[activeSequence][currentStep - 1].getNoteOn() != NoteOn.HOLD) {
				try {
					noteOff.setMessage(ShortMessage.NOTE_OFF, 0,
							sequences[activeSequence][currentStep - 1].getMidiNote(),
							sequences[activeSequence][currentStep].getVelo());
				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}
			} else {
				try {
					noteOff.setMessage(ShortMessage.NOTE_OFF, 0,
							sequences[activeSequence][currentStep - 1].getHoldNote(),
							sequences[activeSequence][currentStep].getVelo());
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

	public NoteGenerator getKey() {
		return key;
	}

	public void setKey(NoteGenerator key) {
		this.key = key;
	}

	// public int getBpm() {
	// return bpm;
	// }
	//
	// public void setBpm(int bpm) {
	// this.bpm = bpm;
	// }

	public SoloMute getSoloMute() {
		return soloMute;
	}

	public void setSoloMute(SoloMute soloMute) {
		this.soloMute = soloMute;
	}

}
