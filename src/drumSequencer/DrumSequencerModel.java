package drumSequencer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import note.Note;
import note.NoteOn;
import pattern.DrumPattern;
import pattern.StandardPattern;
import sequencerBase.SequencerModelBase;
import sequencerBase.SoloMute;
import sequencerBase.SubSequencerModel;

public class DrumSequencerModel extends SequencerModelBase implements SubSequencerModel {

	/**
	 * This will contain all the noteOn messages
	 */
	private ShortMessage[] noteOnArray = new ShortMessage[8];
	/**
	 * This will contain all the noteOff messages
	 */
	private ShortMessage[] noteOffArray = new ShortMessage[8];

	/**
	 * Constructor
	 */
	public DrumSequencerModel() {
		super();

		for (int i = 0; i < noteOnArray.length; i++) {
			noteOnArray[i] = new ShortMessage();
			noteOffArray[i] = new ShortMessage();
		}

		initSeq();
	}

	@Override
	public void initSeq() {
		patterns = new DrumPattern[8];
		soloMute = SoloMute.AUDIBLE;
		for (int i = 0; i < patterns.length; i++) {
			patterns[i] = new DrumPattern("pat " + (i + 1), "Volca Beats");
		}
	}

	@Override
	public StandardPattern copyPattern(int activePattern) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pastePattern(int activePattern, StandardPattern pattern) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initPlayVariables() {
		currentStep = 0;
		firstNote = true;
		running = true;
	}

	@Override
	public void stopPlayback(int activePattern) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playStep(int activePattern) {
		if (soloMute != SoloMute.MUTE) {
			for (int i = 0; i < 8; i++) {
				if (((DrumPattern) patterns[activePattern]).getSingleStep(i, currentStep).getNoteOn() == NoteOn.ON) {
					// make note on
					try {
						noteOnArray[i].setMessage(ShortMessage.NOTE_ON, midiChannel,
								((DrumPattern) patterns[activePattern]).getSingleStep(i, currentStep).getMidiNote(),
								((DrumPattern) patterns[activePattern]).getSingleStep(i, currentStep).getVelo());
					} catch (InvalidMidiDataException e1) {
						e1.printStackTrace();
					}

					// send noteOn
					try {
						rcvr.send(noteOnArray[i], timeStamp);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (firstNote == true) {
			firstNote = false;
		}
	}

	public void killStep(int activePattern) {
		if (soloMute != SoloMute.MUTE) {
			for (int i = 0; i < 8; i++) {
				if (((DrumPattern) patterns[activePattern]).getSingleStep(i, currentStep).getNoteOn() == NoteOn.ON) {
					// make noteOff
					try {
						noteOffArray[i].setMessage(ShortMessage.NOTE_OFF, midiChannel, noteOnArray[i].getData1(),
								noteOnArray[i].getData2());
					} catch (InvalidMidiDataException e1) {
						e1.printStackTrace();
					}

					// send noteOff
					try {
						rcvr.send(noteOffArray[i], timeStamp);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void killLastNote(int activePattern) {
		killStep(activePattern);
	}

	@Override
	public void changeNrOfSteps(int nrOfSteps, int activePattern) {
		((DrumPattern) patterns[activePattern]).changeNrOfSteps(nrOfSteps);
	}

	public int getNrOfSteps(int index) {
		return ((DrumPattern) patterns[index]).getNrOfSteps();
	}

	@Override
	public DrumPattern[] getPatterns() {
		return (DrumPattern[]) patterns;
	}

	public DrumPattern getPattern(int activePattern) {
		return (pattern.DrumPattern) patterns[activePattern];
	}

	public void setPartNotes(String partNotes, int activePattern) {
		((DrumPattern) patterns[activePattern]).setpartNotesChoise(partNotes);

	}

	public Note getSingleStep(int activePattern, int indexRow, int indexCol) {
		return ((DrumPattern) patterns[activePattern]).getSingleStep(indexRow, indexCol);
	}

	public int getPatternLength(int activePattern) {
		return ((DrumPattern) patterns[activePattern]).length();
	}

}
