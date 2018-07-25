package model;

import java.util.LinkedList;
import java.util.List;

import controller.SequencerController;
import note.NoteGenerator;

public class MstrMoModel {

	private List<SequencerController> seqList = new LinkedList<SequencerController>();
	private SequencerController[] seqArr = new SequencerController[8];

	public void createStandardSequencer(NoteGenerator key, int bpm, int index) {
		seqArr[index] = (new SequencerController(key, bpm));
	}

	public void start() {
		for (int i = 0; i < seqArr.length; i++) {
			seqArr[i].playSequence();
		}
	}

	public void stop() {
		for (int i = 0; i < seqArr.length; i++) {
			seqArr[i].stopSequence();
		}
	}

	public void changeKey(NoteGenerator key) {
		for (int i = 0; i < seqArr.length; i++) {
			seqArr[i].setKey(key);
		}
	}

	public void changeBpm(int bpm) {
		for (int i = 0; i < seqArr.length; i++) {
			seqList.get(i).setBpm(bpm);
		}
	}

	public int getNextIndex() {	
		int i = 0;
		while (true) {
		if(seqArr[i] == null) {
			break;
		}
		i++;
		}
		return i;
	}

	public void removeSequencer(int index) {
		seqArr[index].disposeGui();
		seqArr[index] = null;
	}

	public void open(int index) {
		seqArr[index].open();
	}

	public void mute(int index) {
		seqArr[index].mute();

	}

	public void solo(int index) {
		seqArr[index].solo();

	}

	public void rename(String title, int index) {
		seqArr[index].setTitle(title);
	}

}
