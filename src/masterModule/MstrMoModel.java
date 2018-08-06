package masterModule;

import arrangement.Scene;
import arrangement.SoloMute;
import note.NoteGenerator;
import sequencer.SequencerController;

public class MstrMoModel {

	private SequencerController[] seqArr = new SequencerController[8];
	private Scene[] scenes = new Scene[8];

	public MstrMoModel() {
		for (int i = 0; i < scenes.length; i++) {
			scenes[i] = new Scene();
		}
	}

	public Scene[] getScenes() {
		return scenes;
	}

	public String[] getSequenceNames(int index) {
		return seqArr[index].getSequenceNames();
	}

	public void createStandardSequencer(NoteGenerator key, int bpm, int index, String title) {
		seqArr[index] = new SequencerController(key, bpm, title);
	}

	public void start() {
		for (int i = 0; i <= lastUsedIndex(); i++) {
			seqArr[i].playSequence();
		}
	}

	public void stop() {
		for (int i = 0; i <= lastUsedIndex(); i++) {
			seqArr[i].stopSequence();
		}
	}

	public void changeKey(NoteGenerator key) {
		for (int i = 0; i < seqArr.length; i++) {
			seqArr[i].setKey(key);
		}
	}

	public void changeBpm(int bpm) {
		for (int i = 0; i <= lastUsedIndex(); i++) {
			seqArr[i].setBpm(bpm);
		}
	}

	public int nextIndex() {
		int i = 0;
		while (true) {
			if (seqArr[i] == null) {
				break;
			}
			i++;
		}
		return i;
	}

	public int lastUsedIndex() {
		int index = -1;
		for (int i = 0; i < seqArr.length; i++) {
			if (seqArr[i] != null) {
				index = i;
			}
		}
		return index;
	}

	public void removeSequencer(int index) {
		seqArr[index].disposeGui();
		seqArr[index] = null;
		orderSeqArr();
	}

	public void orderSeqArr() {
		for (int i = lastUsedIndex(); i > 0; i--) {
			for (int j = lastUsedIndex(); j > 0; j--) {
				if (seqArr[j] != null && seqArr[j - 1] == null) {
					seqArr[j - 1] = seqArr[j];
					seqArr[j] = null;
					break;
				}
			}
		}
	}

	public void open(int index) {
		seqArr[index].open();
	}

	public void mute(int index) {
		seqArr[index].muteButton();
	}

	public void solo(int index) {
		if (seqArr[index].getSoloMute() != SoloMute.SOLO) {
			seqArr[index].solo();
			for (int i = 0; i <= lastUsedIndex(); i++) {
				if (i != index) {
					seqArr[i].mute();
				}
			}
		} else {
			seqArr[index].solo();
			for (int i = 0; i <= lastUsedIndex(); i++) {
				if (i != index) {
					seqArr[i].unSoloMute();
				}
			}
		}
	}

	public void rename(String title, int index) {
		seqArr[index].setTitle(title);
	}

	public SoloMute getSoloMute(int index) {
		return seqArr[index].getSoloMute();
	}

	public String getTitle(int index) {
		return seqArr[index].getTitle();
	}

	public SequencerController[] getSeqArr() {
		return seqArr;
	}
}
