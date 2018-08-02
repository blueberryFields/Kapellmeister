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
import note.Sequence;

public class SequencerModel {

	boolean running = false;
	// private Timer clock = new Timer(500, this);
	private MidiDevice device;
	private MidiDevice.Info[] infos;
	private Receiver rcvr;
	private long timeStamp = -1;
	private Note[] sequence;
	// private Note[][] sequences;
	private Sequence[] sequences;
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
		sequences = new Sequence[8];
		soloMute = SoloMute.AUDIBLE;
		for (int i = 0; i < sequences.length; i++) {
			sequences[i] = new Sequence("pat " + (i + 1));
		}
	}

	public void setSequenceName(int activeSequence, String newName) {
		sequences[activeSequence].setName(newName);
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
		String tempName = sequences[activeSequence].getName();
		sequences[activeSequence] = new Sequence(tempName, nrOfSteps);
		switch (generatorAlgorithm) {
		case "Rnd notes":
			sequences[activeSequence].setSequence(key.getRndSequence(sequences[activeSequence].getSequence(),
					rndVeloIsChecked, veloLow, veloHigh, octaveLow, octaveHigh));
			break;
		case "Rnd notes, no dupl in row":
			sequences[activeSequence].setSequence(key.getRndSeqNoDuplInRow(sequences[activeSequence].getSequence(),
					rndVeloIsChecked, veloLow, veloHigh, octaveLow, octaveHigh));
			break;
		case "Rnd notes and On/Hold/Off":
			sequences[activeSequence].setSequence(key.getRndSequenceOnHoldOff(sequences[activeSequence].getSequence(),
					rndVeloIsChecked, veloLow, veloHigh, octaveLow, octaveHigh));
			break;
		case "Rnd notes, no dupl in row, On/Hold/Off":
			sequences[activeSequence]
					.setSequence(key.getRndSeqNoDuplInRowOnHoldOff(sequences[activeSequence].getSequence(),
							rndVeloIsChecked, veloLow, veloHigh, octaveLow, octaveHigh));
			break;
		}
	}

	public void setStep(int index) {
		sequence[index] = new Note();
	}

	public void setSequence(Note[] sequence, int activeSequence) {
		sequences[activeSequence].setSequence(sequence);
	}

	public Note[] getSequence(int activeSequence) {
		return sequences[activeSequence].getSequence();
	}

	public Sequence[] getSequences() {
		return sequences;
	}

	public Note getSingleStep(int activeSequence, int index) {
		return sequences[activeSequence].getSingleStep(index);
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
				if (sequences[activeSequence].getSingleStep(getSequence(activeSequence).length - 1)
						.getNoteOn() != NoteOn.HOLD) {
					try {
						noteOff.setMessage(
								ShortMessage.NOTE_OFF, midiChannel, sequences[activeSequence]
										.getSingleStep(getSequence(activeSequence).length - 1).getMidiNote(),
								sequences[activeSequence].getSingleStep(currentStep).getVelo());
					} catch (InvalidMidiDataException e1) {
						e1.printStackTrace();
					}
				}
				if (sequences[activeSequence].getSingleStep(currentStep).getNoteOn() != NoteOn.HOLD) {
					try {
						rcvr.send(noteOff, timeStamp);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (currentStep != 0) {
				if (sequences[activeSequence].getSingleStep(currentStep - 1).getNoteOn() != NoteOn.HOLD) {
					try {
						noteOff.setMessage(ShortMessage.NOTE_OFF, midiChannel,
								sequences[activeSequence].getSingleStep(currentStep - 1).getMidiNote(),
								sequences[activeSequence].getSingleStep(currentStep).getVelo());
					} catch (InvalidMidiDataException e1) {
						e1.printStackTrace();
					}
				} else {
					try {
						noteOff.setMessage(ShortMessage.NOTE_OFF, midiChannel,
								sequences[activeSequence].getSingleStep(currentStep - 1).getHoldNote(),
								sequences[activeSequence].getSingleStep(currentStep).getVelo());
					} catch (InvalidMidiDataException e1) {
						e1.printStackTrace();
					}
				}
				if (sequences[activeSequence].getSingleStep(currentStep).getNoteOn() != NoteOn.HOLD) {
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
						sequences[activeSequence].getSingleStep(currentStep).getMidiNote(),
						sequences[activeSequence].getSingleStep(currentStep).getVelo());
			} catch (InvalidMidiDataException e1) {
				e1.printStackTrace();
			}
			if (sequences[activeSequence].getSingleStep(currentStep).getNoteOn() == NoteOn.ON) {
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
		sequences[activeSequence].nudgeLeft();
	}

	public void nudgeRight(int activeSequence) {
		sequences[activeSequence].nudgeRight();
	}

	public void mute() {
		soloMute = SoloMute.MUTE;
	}

	public void solo() {
		soloMute = SoloMute.SOLO;
	}

	public void unSoloMute() {
		soloMute = SoloMute.AUDIBLE;
	}

	public void killLastNote(int activeSequence) {
		if (currentStep == 0 && !firstNote) {
			if (sequences[activeSequence].getSingleStep(sequences[activeSequence].getSequence().length - 1)
					.getNoteOn() != NoteOn.HOLD) {
				try {
					noteOff.setMessage(
							ShortMessage.NOTE_OFF, 0, sequences[activeSequence]
									.getSingleStep(sequences[activeSequence].getSequence().length - 1).getMidiNote(),
							sequences[activeSequence].getSingleStep(currentStep).getVelo());
				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}
			}
			rcvr.send(noteOff, timeStamp);
		} else if (currentStep != 0) {
			if (sequences[activeSequence].getSingleStep(currentStep - 1).getNoteOn() != NoteOn.HOLD) {
				try {
					noteOff.setMessage(ShortMessage.NOTE_OFF, 0,
							sequences[activeSequence].getSingleStep(currentStep - 1).getMidiNote(),
							sequences[activeSequence].getSingleStep(currentStep).getVelo());
				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}
			} else {
				try {
					noteOff.setMessage(ShortMessage.NOTE_OFF, 0,
							sequences[activeSequence].getSingleStep(currentStep - 1).getHoldNote(),
							sequences[activeSequence].getSingleStep(currentStep).getVelo());
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

	public SoloMute getSoloMute() {
		return soloMute;
	}

	public void setSoloMute(SoloMute soloMute) {
		this.soloMute = soloMute;
	}

	public String getPartNotesChoice(int index) {
		return sequences[index].getPartNotesChoise();
	}

	public int getNrOfSteps(int index) {
		return sequences[index].getNrOfSteps();
	}

	public String getPartNotes(int index) {
		return sequences[index].getPartNotesChoise();
	}

	public void changeNrOfSteps(int nrOfSteps, int activeSequence) {
		sequences[activeSequence].changeNrOfSteps(nrOfSteps);
	}

	public void setPartNotes(String partNotes, int activeSequence) {
		sequences[activeSequence].setpartNotesChoise(partNotes);
	}

}
