package standardSequencer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import note.Note;
import note.NoteGenerator;
import note.NoteOn;
import pattern.StandardPattern;
import sequencerBase.SequencerModelBase;
import sequencerBase.SoloMute;
import sequencerBase.SubSequencerModel;

/**
 * The heart, the core and logic of the standard sequencer. This class contains
 * the different patterns, the connection the the mididevice, the methods for
 * playing notes and so on.
 */
public class StandardSequencerModel extends SequencerModelBase implements SubSequencerModel {

	/**
	 * This will be used to generate notes. Can be set to different musical keys
	 */
	private NoteGenerator key;
	/**
	 * This will contain all the noteOn messages
	 */
	protected ShortMessage noteOn = new ShortMessage();
	/**
	 * This will contain all the noteOff messages
	 */
	protected ShortMessage noteOff = new ShortMessage();

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
	public StandardSequencerModel(NoteGenerator key) {
		super();
		this.key = key;
		initSeq();
	}

	/**
	 * Initialize the sequencer
	 */
	public void initSeq() {
		patterns = new StandardPattern[8];
		soloMute = SoloMute.AUDIBLE;
		for (int i = 0; i < patterns.length; i++) {
			patterns[i] = new StandardPattern("pat " + (i + 1));
		}
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
		String tempPartNotes = ((StandardPattern) patterns[activePattern]).getPartNotesChoise();
		patterns[activePattern] = new StandardPattern(tempName, nrOfSteps, tempPartNotes);
		switch (generatorAlgorithm) {
		case "Rnd notes":
			((StandardPattern) patterns[activePattern])
					.setPattern(key.getRndSequence(((StandardPattern) patterns[activePattern]).getPattern(),
							rndVeloIsChecked, veloLow, veloHigh, octaveLow, octaveHigh));
			break;
		case "Rnd notes, no dupl in row":
			((StandardPattern) patterns[activePattern])
					.setPattern(key.getRndSeqNoDuplInRow(((StandardPattern) patterns[activePattern]).getPattern(),
							rndVeloIsChecked, veloLow, veloHigh, octaveLow, octaveHigh));
			break;
		case "Rnd notes and On/Hold/Off":
			((StandardPattern) patterns[activePattern])
					.setPattern(key.getRndSequenceOnHoldOff(((StandardPattern) patterns[activePattern]).getPattern(),
							rndVeloIsChecked, veloLow, veloHigh, octaveLow, octaveHigh));
			break;
		case "Rnd notes, no dupl in row, On/Hold/Off":
			((StandardPattern) patterns[activePattern]).setPattern(
					key.getRndSeqNoDuplInRowOnHoldOff(((StandardPattern) patterns[activePattern]).getPattern(),
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
	public StandardPattern copyPattern(int activePattern) {
		return ((StandardPattern) patterns[activePattern]).copy();
	}

	/**
	 * Takes the passed pattern and replaces the active pattern with it
	 * 
	 * @param activePattern
	 *            the index of the active pattern or other pattern to be replaced
	 * @param pattern
	 *            the pattern you want to replace the choosen pattern whith.
	 */
	public void pastePattern(int activePattern, StandardPattern pattern) {
		((StandardPattern) patterns[activePattern]).paste(pattern);
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
	 * plays the current step in the active pattern. Really long, tortuous method,
	 * to be raplaced sometime in the future
	 * 
	 * @param activePattern
	 *            the index of the sequence currently being played
	 */
	public void playStep(int activePattern) {
		if (soloMute != SoloMute.MUTE) {
			if (currentStep == 0 && !firstNote) {
				if (((StandardPattern) patterns[activePattern]).getSingleStep(getPatternLength(activePattern) - 1)
						.getNoteOn() != NoteOn.HOLD) {
					try {
						noteOff.setMessage(ShortMessage.NOTE_OFF, midiChannel,
								((StandardPattern) patterns[activePattern])
										.getSingleStep(getPatternLength(activePattern) - 1).getMidiNote(),
								((StandardPattern) patterns[activePattern]).getSingleStep(currentStep).getVelo());
					} catch (InvalidMidiDataException e1) {
						e1.printStackTrace();
					}
				}
				if (((StandardPattern) patterns[activePattern]).getSingleStep(currentStep).getNoteOn() != NoteOn.HOLD) {
					try {
						rcvr.send(noteOff, timeStamp);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (currentStep != 0) {
				if (((StandardPattern) patterns[activePattern]).getSingleStep(currentStep - 1)
						.getNoteOn() != NoteOn.HOLD) {
					try {
						noteOff.setMessage(ShortMessage.NOTE_OFF, midiChannel,
								((StandardPattern) patterns[activePattern]).getSingleStep(currentStep - 1)
										.getMidiNote(),
								((StandardPattern) patterns[activePattern]).getSingleStep(currentStep).getVelo());
					} catch (InvalidMidiDataException e1) {
						e1.printStackTrace();
					}
				} else {
					try {
						noteOff.setMessage(ShortMessage.NOTE_OFF, midiChannel,
								((StandardPattern) patterns[activePattern]).getSingleStep(currentStep - 1)
										.getHoldNote(),
								((StandardPattern) patterns[activePattern]).getSingleStep(currentStep).getVelo());
					} catch (InvalidMidiDataException e1) {
						e1.printStackTrace();
					}
				}
				if (((StandardPattern) patterns[activePattern]).getSingleStep(currentStep).getNoteOn() != NoteOn.HOLD) {
					try {
						rcvr.send(noteOff, timeStamp);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			try {
				noteOn.setMessage(ShortMessage.NOTE_ON, midiChannel,
						((StandardPattern) patterns[activePattern]).getSingleStep(currentStep).getMidiNote(),
						((StandardPattern) patterns[activePattern]).getSingleStep(currentStep).getVelo());
			} catch (InvalidMidiDataException e1) {
				e1.printStackTrace();
			}
			if (((StandardPattern) patterns[activePattern]).getSingleStep(currentStep).getNoteOn() == NoteOn.ON) {
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
		((StandardPattern) patterns[activePattern]).nudgeLeft();
	}

	/**
	 * Slides the whole pattern one step to the right
	 * 
	 * @param activePattern
	 *            the sequence to be nudged, preferebly the active pattern
	 */
	public void nudgeRight(int activePattern) {
		((StandardPattern) patterns[activePattern]).nudgeRight();
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
			if (((StandardPattern) patterns[activePattern])
					.getSingleStep(((StandardPattern) patterns[activePattern]).getPattern().length - 1)
					.getNoteOn() != NoteOn.HOLD) {
				try {
					noteOff.setMessage(ShortMessage.NOTE_OFF, midiChannel,
							((StandardPattern) patterns[activePattern])
									.getSingleStep(((StandardPattern) patterns[activePattern]).getPattern().length - 1)
									.getMidiNote(),
							((StandardPattern) patterns[activePattern]).getSingleStep(currentStep).getVelo());
				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}
			}
			rcvr.send(noteOff, timeStamp);
		} else if (currentStep != 0) {
			if (((StandardPattern) patterns[activePattern]).getSingleStep(currentStep - 1).getNoteOn() != NoteOn.HOLD) {
				try {
					noteOff.setMessage(ShortMessage.NOTE_OFF, midiChannel,
							((StandardPattern) patterns[activePattern]).getSingleStep(currentStep - 1).getMidiNote(),
							((StandardPattern) patterns[activePattern]).getSingleStep(currentStep).getVelo());
				} catch (InvalidMidiDataException e1) {
					e1.printStackTrace();
				}
			} else {
				try {
					noteOff.setMessage(ShortMessage.NOTE_OFF, midiChannel,
							((StandardPattern) patterns[activePattern]).getSingleStep(currentStep - 1).getHoldNote(),
							((StandardPattern) patterns[activePattern]).getSingleStep(currentStep).getVelo());
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
		((StandardPattern) patterns[activePattern]).changeNrOfSteps(nrOfSteps);
	}

	// The rest is simple getters and setters

	public StandardPattern getPattern(int activePattern) {
		return (StandardPattern) patterns[activePattern];
	}

	public int getPatternLength(int activePattern) {
		return ((StandardPattern) patterns[activePattern]).length();
	}

	public StandardPattern[] getPatterns() {
		return (StandardPattern[]) patterns;
	}

	public void setPattern(Note[] pattern, int activePattern) {
		((StandardPattern) patterns[activePattern]).setPattern(pattern);
	}

	public int getNrOfSteps(int index) {
		return ((StandardPattern) patterns[index]).getNrOfSteps();
	}

	public void setPartNotes(String partNotes, int activePattern) {
		((StandardPattern) patterns[activePattern]).setpartNotesChoise(partNotes);
	}

	public Note getSingleStep(int activePattern, int index) {
		return ((StandardPattern) patterns[activePattern]).getSingleStep(index);
	}

	public NoteGenerator getKey() {
		return key;
	}

	public void setKey(NoteGenerator key) {
		this.key = key;
	}

	public String getPartNotes(int index) {
		return ((StandardPattern) patterns[index]).getPartNotesChoise();
	}
}
