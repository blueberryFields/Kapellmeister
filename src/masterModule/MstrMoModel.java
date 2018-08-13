package masterModule;

import java.util.LinkedList;
import java.util.List;

import arrangement.Scene;
import arrangement.SoloMute;
import note.NoteGenerator;
import sequencer.SequencerController;

public class MstrMoModel {

	private SequencerController[] seqArr = new SequencerController[8];
	private Scene[] scenes = new Scene[8];
	private boolean running = false;

	public MstrMoModel() {
		for (int i = 0; i < scenes.length; i++) {
			scenes[i] = new Scene("Scene " + (i + 1), i);
		}
	}

	public int getNextActiveScene(int currentScene, boolean loop) {
		for (int i = currentScene + 1; i < scenes.length; i++) {
			if (scenes[i].isActive()) {
				return i;
			}
		}
		if (loop) {
			return getFirstActiveScene();
		} else {
			return -1;
		}
	}

	public int getFirstActiveScene() {
		for (int i = 0; i < scenes.length; i++) {
			if (scenes[i].isActive()) {
				return i;
			}
		}
		return -1;
	}
	
	public int getLastActiveScene() {
		for (int i = scenes.length-1; i >= 0; i--) {
			if (scenes[i].isActive()) {
				return i;
			}
		}
		return -1;
	}

	public void setActiveSequences(int currentScene) {
		for (int i = 0; i <= lastUsedIndex(); i++) {
			seqArr[i].chooseSequence(scenes[currentScene].getSequenceChoice(i));
		}
	}

	public void killLastNote(int lastScene) {
		for (int i = 0; i <= lastUsedIndex(); i++) {
			seqArr[i].killLastNote(scenes[lastScene].getSequenceChoice(i));
		}
	}

	public Scene[] getScenes() {
		return scenes;
	}

	public String[] getSequenceNames(int index) {
		return seqArr[index].getSequenceNames();
	}

	public int getSceneLength(int sceneNr) {
		return scenes[sceneNr].getLength();
	}

	public void setSceneLength(int scene, int length) {
		scenes[scene].setLength(length);
	}

	public void setSceneActive(int scene, boolean active) {
		scenes[scene].setActive(active);
	}

	public boolean isSceneActive(int scene) {
		return scenes[scene].isActive();
	}

	public void setSequenceChoice(int scene, int instrument, int sequenceChoice) {
		scenes[scene].setSequenceChoice(instrument, sequenceChoice);
	}

	public void createStandardSequencer(NoteGenerator key, int clockDelay, int index, String title) {
		seqArr[index] = new SequencerController(key, clockDelay, title);
	}

	public void tick() {
		for (int i = 0; i <= lastUsedIndex(); i++) {
			seqArr[i].tick();
		}
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

	public int nextIndex() {
		int i = 0;
		while (i < 8) {
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

	public void renameSequencer(String title, int index) {
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

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	
	public void renameScene(int sceneNr, String newName) {
		scenes[sceneNr].setName(newName);
		
	}
}
