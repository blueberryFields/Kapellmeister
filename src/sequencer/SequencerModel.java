package sequencer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import arrangement.Pattern;
import arrangement.SoloMute;
import note.Note;
import note.NoteGenerator;
import note.NoteOn;

/**
 * The heart, the core and logic of the standard sequencer. This class contains
 * the different patterns, the connection the the mididevice, the methods for
 * playing notes and so on.
 */
public class SequencerModel {

	/**
	 * The mididevice to connect to
	 */
	private MidiDevice device;
	/**
	 * A list of availeble midiDevices
	 */
	private MidiDevice.Info[] infos;
	/**
	 * The reciever to send midinotes to
	 */
	private Receiver rcvr;
	/**
	 * standard timestamp to go with the midinotes
	 */
	private long timeStamp = -1;
	/**
	 * An array of the patterns the sequencer know and can play back
	 */
	private Pattern[] patterns;
	/**
	 * This will contain all the noteOn messages
	 */
	private ShortMessage noteOn = new ShortMessage();
	/**
	 * This will contain all the noteOff messages
	 */
	private ShortMessage noteOff = new ShortMessage();
	/**
	 * Enumeration of hte diferrent states this sequencer can be in, SOLO, MUTE,
	 * AUDIBLE
	 */
	private SoloMute soloMute;
	/**
	 * the midichannel on which the sequencer will send its notes on
	 */
	private int midiChannel = 0;
	/**
	 * This will be used to generate notes. Can be set to different musical keys
	 */
	private NoteGenerator key;
	/**
	 * Indicates if the sequencer is currently running/playing
	 */
	boolean running = false;
	/**
	 * Indicates if the current note is the first note of the first repetition of
	 * the played pattern. Needed for the playNote method. Hopefully will be
	 * outdated whit future updates of playNote
	 */
	private boolean firstNote;
	/**
	 * Keeps track of which note/step is currently playing
	 */
	private int currentStep = 0;

	/**
	 * Constructor
	 * 
	 * @param key
	 *            the musical key from which the notes in the noteGenerator will be
	 *            generated, can only be reset from the masterModule
	 * @param bpm
	 *            the tempo in which the pattern of notes will be played back, can
	 *            only be reset from the masterModule
	 */
	public SequencerModel(NoteGenerator key) {
		infos = MidiSystem.getMidiDeviceInfo();
		this.key = key;
		initSeq();
	}

	/**
	 * Initialize the sequencer
	 */
	public void initSeq() {
		patterns = new Pattern[8];
		soloMute = SoloMute.AUDIBLE;
		for (int i = 0; i < patterns.length; i++) {
			patterns[i] = new Pattern("pat " + (i + 1));
		}
	}

	/**
	 * Tries to connect to a mididevice
	 * 
	 * @param index
	 *            index of the mididevice to connect to
	 */
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

	// WORK IN PROGRESS!!!
	public void refreshMidiDeviceList() {
		infos = MidiSystem.getMidiDeviceInfo();
	}

	/**
	 * Sends a test note(C4, 1 sec) to the reciever
	 */
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

	/**
	 * Generates a new pattern using the noteGenerator-class
	 * 
	 * @param nrOfSteps
	 *            nr of steps in the pattern to be generated
	 * @param key
	 *            the musical key from which the pattern will be generated, this
	 *            class inherits from the noteGenerator-class
	 * @param generatorAlgorithm
	 *            the choice of generator-algorithm to be used
	 * @param rndVeloIsChecked
	 *            is random velocity to be used?
	 * @param veloLow
	 *            if random velocity is to be used this parameter will set the lower
	 *            confines of the range
	 * @param veloHigh
	 *            if random velocity is to be used this parameter will set the upper
	 *            confines of the range
	 * @param octaveLow
	 *            sets the lower confines of the octaveRange from which octavenumber
	 *            will be generated
	 * @param octaveHigh
	 *            sets the upper confines of the octaveRange from which octavenumber
	 *            will be generated
	 * @param activePattern
	 *            index of the pattern the be replaced by the newly generated one
	 */
	public void generatePattern(int nrOfSteps, NoteGenerator key, String generatorAlgorithm, boolean rndVeloIsChecked,
			int veloLow, int veloHigh, int octaveLow, int octaveHigh, int activePattern) {
		String tempName = patterns[activePattern].getName();
		String tempPartNotes = patterns[activePattern].getPartNotesChoise();
		patterns[activePattern] = new Pattern(tempName, nrOfSteps, tempPartNotes);
		switch (generatorAlgorithm) {
		case "Rnd notes":
			patterns[activePattern].setPattern(key.getRndSequence(patterns[activePattern].getPattern(),
					rndVeloIsChecked, veloLow, veloHigh, octaveLow, octaveHigh));
			break;
		case "Rnd notes, no dupl in row":
			patterns[activePattern].setPattern(key.getRndSeqNoDuplInRow(patterns[activePattern].getPattern(),
					rndVeloIsChecked, veloLow, veloHigh, octaveLow, octaveHigh));
			break;
		case "Rnd notes and On/Hold/Off":
			patterns[activePattern].setPattern(key.getRndSequenceOnHoldOff(patterns[activePattern].getPattern(),
					rndVeloIsChecked, veloLow, veloHigh, octaveLow, octaveHigh));
			break;
		case "Rnd notes, no dupl in row, On/Hold/Off":
			patterns[activePattern].setPattern(key.getRndSeqNoDuplInRowOnHoldOff(patterns[activePattern].getPattern(),
					rndVeloIsChecked, veloLow, veloHigh, octaveLow, octaveHigh));
			break;
		}
	}

	/**
	 * Makes a copy of the active pattern and returns it
	 * 
	 * @param activePattern
	 *            the index of the active pattern, or other choosen pattern to make
	 *            a copy from
	 * @return a individual copy of the choosen pattern
	 */
	public Pattern copyPattern(int activePattern) {
		return patterns[activePattern].copy();
	}

	/**
	 * Takes the passed pattern and replaces the active pattern with it
	 * 
	 * @param activePattern
	 *            the index of the active pattern or other pattern to be replaced
	 * @param pattern
	 *            the pattern you want to replace the choosen pattern whith.
	 */
	public void pastePattern(int activePattern, Pattern pattern) {
		patterns[activePattern] = pattern;
	}

	/**
	 * This method is to be called when the playback starts. Initializes some
	 * variables that need to be set before the sequencer starts playing and sets
	 * the boolean running to true
	 */
	public void initPlayVariables() {
		currentStep = 0;
		firstNote = true;
		running = true;
	}

	/**
	 * This method is to be called when the playback stops, kills the playing note
	 * and sets boolean running to false
	 * 
	 * @param activePattern
	 *            the currently playing pattern
	 */
	public void stopPlayback(int activePattern) {
		killLastNote(activePattern);
		running = false;
	}

	/**
	 * Closes the midiDevice
	 */
	public void closeDevice() {
		if (device.isOpen()) {
			device.close();
		}
	}

	/**
	 * Closes the reciever
	 */
	public void closeRcvr() {
		rcvr.close();
	}

	/**
	 * plays the current step in the active patterne. Really long, tortuous method,
	 * to be raplaced sometime in the future
	 * 
	 * @param activePattern
	 *            the index of the sequence currently being played
	 */
	public void playStep(int activePattern) {
		if (soloMute != SoloMute.MUTE) {
			if (currentStep == 0 && !firstNote) {
				if (patterns[activePattern].getSingleStep(getPattern(activePattern).length - 1)
						.getNoteOn() != NoteOn.HOLD) {
					try {
						noteOff.setMessage(
								ShortMessage.NOTE_OFF, midiChannel, patterns[activePattern]
										.getSingleStep(getPattern(activePattern).length - 1).getMidiNote(),
								patterns[activePattern].getSingleStep(currentStep).getVelo());
					} catch (InvalidMidiDataException e1) {
						e1.printStackTrace();
					}
				}
				if (patterns[activePattern].getSingleStep(currentStep).getNoteOn() != NoteOn.HOLD) {
					try {
						rcvr.send(noteOff, timeStamp);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (currentStep != 0) {
				if (patterns[activePattern].getSingleStep(currentStep - 1).getNoteOn() != NoteOn.HOLD) {
					try {
						noteOff.setMessage(ShortMessage.NOTE_OFF, midiChannel,
								patterns[activePattern].getSingleStep(currentStep - 1).getMidiNote(),
								patterns[activePattern].getSingleStep(currentStep).getVelo());
					} catch (InvalidMidiDataException e1) {
						e1.printStackTrace();
					}
				} else {
					try {
						noteOff.setMessage(ShortMessage.NOTE_OFF, midiChannel,
								patterns[activePattern].getSingleStep(currentStep - 1).getHoldNote(),
								patterns[activePattern].getSingleStep(currentStep).getVelo());
					} catch (InvalidMidiDataException e1) {
						e1.printStackTrace();
					}
				}
				if (patterns[activePattern].getSingleStep(currentStep).getNoteOn() != NoteOn.HOLD) {
					try {
						rcvr.send(noteOff, timeStamp);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			try {
				noteOn.setMessage(ShortMessage.NOTE_ON, midiChannel,
						patterns[activePattern].getSingleStep(currentStep).getMidiNote(),
						patterns[activePattern].getSingleStep(currentStep).getVelo());
			} catch (InvalidMidiDataException e1) {
				e1.printStackTrace();
			}
			if (patterns[activePattern].getSingleStep(currentStep).getNoteOn() == NoteOn.ON) {
				try {
					rcvr.send(noteOn, timeStamp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (firstNote == true) {
			firstNote = false;
		}
	}

	/**
	 * Slides the whole pattern one step to the left
	 * 
	 * @param activePattern
	 *            the pattern to be nudged, preferebly the active pattern
	 */
	public void nudgeLeft(int activePattern) {
		patterns[activePattern].nudgeLeft();
	}

	/**
	 * Slides the whole pattern one step to the right
	 * 
	 * @param activePattern
	 *            the sequence to be nudged, preferebly the active pattern
	 */
	public void nudgeRight(int activePattern) {
		patterns[activePattern].nudgeRight();
	}

	/**
	 * Sets the sequencer in mute mode.
	 */
	public void mute() {
		soloMute = SoloMute.MUTE;
	}

	/**
	 * Sets the sequencer in solo mode
	 */
	public void solo() {
		soloMute = SoloMute.SOLO;
	}

	/**
	 * Sets the sequencer in audible mode
	 */
	public void unSoloMute() {
		soloMute = SoloMute.AUDIBLE;
	}

	/**
	 * When you stop playback of the sequencer this method is to be called to make
	 * shure the last played note will be stopped. Otherwise theres a chance it will
	 * never stop playing
	 * 
	 * @param activePattern
	 *            the index of the pattern currently playing
	 */
	public void killLastNote(int activePattern) {
		if (currentStep == 0 && !firstNote) {
			if (patterns[activePattern].getSingleStep(patterns[activePattern].getPattern().length - 1)
					.getNoteOn() != NoteOn.HOLD) {
				try {
					noteOff.setMessage(
							ShortMessage.NOTE_OFF, midiChannel, patterns[activePattern]
									.getSingleStep(patterns[activePattern].getPattern().length - 1).getMidiNote(),
							patterns[activePattern].getSingleStep(currentStep).getVelo());
				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}
			}
			rcvr.send(noteOff, timeStamp);
		} else if (currentStep != 0) {
			if (patterns[activePattern].getSingleStep(currentStep - 1).getNoteOn() != NoteOn.HOLD) {
				try {
					noteOff.setMessage(ShortMessage.NOTE_OFF, midiChannel,
							patterns[activePattern].getSingleStep(currentStep - 1).getMidiNote(),
							patterns[activePattern].getSingleStep(currentStep).getVelo());
				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}
			} else {
				try {
					noteOff.setMessage(ShortMessage.NOTE_OFF, midiChannel,
							patterns[activePattern].getSingleStep(currentStep - 1).getHoldNote(),
							patterns[activePattern].getSingleStep(currentStep).getVelo());
				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}
			}
			rcvr.send(noteOff, timeStamp);
		}
	}

	/**
	 * Change number of steps in the choosen pattern
	 * 
	 * @param nrOfSteps
	 *            the desired new number of steps
	 * @param activePattern
	 *            the pattern to be changed
	 */
	public void changeNrOfSteps(int nrOfSteps, int activePattern) {
		patterns[activePattern].changeNrOfSteps(nrOfSteps);
	}

	/**
	 * Choose which midichannel the midinotes will be sent on
	 * 
	 * @param midiChannel
	 *            the midichannel you wich to send your midinotes on
	 */
	public void setMidiChannel(int midiChannel) {
		this.midiChannel = midiChannel;
	}

	public void setPattern(Note[] pattern, int activePattern) {
		patterns[activePattern].setPattern(pattern);
	}

	public Note[] getPattern(int activePattern) {
		return patterns[activePattern].getPattern();
	}

	public Pattern[] getPatterns() {
		return patterns;
	}

	public String[] getPatternNames() {
		String[] patternNames = new String[8];
		for (int i = 0; i < patterns.length; i++) {
			patternNames[i] = patterns[i].getName();
		}
		return patternNames;
	}

	public Note getSingleStep(int activePattern, int index) {
		return patterns[activePattern].getSingleStep(index);
	}

	public void setCurrentStep(int step) {
		currentStep = step;
	}

	public int getCurrentStep() {
		return currentStep;
	}

	public void setPatternName(int activePattern, String newName) {
		patterns[activePattern].setName(newName);
	}

	public void setPartNotes(String partNotes, int activePattern) {
		patterns[activePattern].setpartNotesChoise(partNotes);
	}

	public MidiDevice.Info[] getAvailibleMidiDevices() {
		return infos;
	}

	public boolean isFirstNote() {
		return firstNote;
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
		return patterns[index].getPartNotesChoise();
	}

	public int getNrOfSteps(int index) {
		return patterns[index].getNrOfSteps();
	}

	public String getPatternName(int index) {
		return patterns[index].getName();
	}

	public String getPartNotes(int index) {
		return patterns[index].getPartNotesChoise();
	}
}
