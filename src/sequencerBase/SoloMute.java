package sequencerBase;

/**
 * Enumeration of different states a sequencer can be in. Mute means it wont do
 * send any notes. Solo means it will be the only sequencer that will send any
 * notes. Audible mean its just normal and will send notes as usual.
 */

public enum SoloMute {
	SOLO, MUTE, AUDIBLE;
}
