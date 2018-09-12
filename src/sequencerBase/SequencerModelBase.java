package sequencerBase;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import pattern.DrumPattern;
import pattern.PatternBase;

/**
 * The heart, the core and logic of the standard sequencer. This class contains
 * the different patterns, the connection the the mididevice, the methods for
 * playing notes and so on.
 */
public class SequencerModelBase {

	/**
	 * The mididevice to connect to
	 */
	protected MidiDevice device;
	/**
	 * A list of availeble midiDevices
	 */
	protected MidiDevice.Info[] infos;
	/**
	 * The reciever to send midinotes to
	 */
	protected Receiver rcvr;
	/**
	 * standard timestamp to go with the midinotes
	 */
	protected long timeStamp = -1;
	/**
	 * An array of the patterns the sequencer know and can play back
	 */
	protected PatternBase[] patterns;
	/**
	 * This will contain all the noteOn messages
	 */
	protected ShortMessage noteOn = new ShortMessage();
	/**
	 * This will contain all the noteOff messages
	 */
	protected ShortMessage noteOff = new ShortMessage();
	/**
	 * Enumeration of hte diferrent states this sequencer can be in, SOLO, MUTE,
	 * AUDIBLE
	 */
	protected SoloMute soloMute;
	/**
	 * the midichannel on which the sequencer will send its notes on
	 */
	protected int midiChannel = 0;
	
	/**
	 * Indicates if the current note is the first note of the first repetition of
	 * the played pattern. Needed for the playNote method. Hopefully will be
	 * outdated whit future updates of playNote
	 */
	protected boolean firstNote;
	
	/**
	 * Indicates if the sequencer is currently running/playing
	 */
	protected boolean running = false;
	/**
	 * Keeps track of which note/step is currently playing
	 */
	protected int currentStep = 0;

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
	public SequencerModelBase() {
		infos = MidiSystem.getMidiDeviceInfo();
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
	 * Choose which midichannel the midinotes will be sent on
	 * 
	 * @param midiChannel
	 *            the midichannel you wich to send your midinotes on
	 */
	public void setMidiChannel(int midiChannel) {
		this.midiChannel = midiChannel;
	}

	// The rest is simple getters and setters

	public MidiDevice.Info[] getAvailibleMidiDevices() {
		return infos;
	}

	public boolean getRunning() {
		return running;
	}

	public void isRunning(boolean running) {
		this.running = running;
	}

	public SoloMute getSoloMute() {
		return soloMute;
	}

	public void setSoloMute(SoloMute soloMute) {
		this.soloMute = soloMute;
	}

	public String getPatternName(int index) {
		return patterns[index].getName();
	}

	public void setPatternName(int activePattern, String newName) {
		patterns[activePattern].setName(newName);
	}

	public String[] getPatternNames() {
		String[] patternNames = new String[8];
		for (int i = 0; i < patterns.length; i++) {
			patternNames[i] = patterns[i].getName();
		}
		return patternNames;
	}

	public String getPartNotesChoice(int index) {
		return patterns[index].getPartNotesChoise();
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
