package model;

import java.util.LinkedList;
import java.util.List;

import controller.SequencerController;
import note.NoteGenerator;

public class MstrMoModel {

	private List<SequencerController> seqList = new LinkedList<SequencerController>();

	// private NoteGenerator key;

	public void createStandardSequencer(NoteGenerator key, int bpm) {
		seqList.add(new SequencerController(key, bpm));
	}

	public void start() {
		for (int i = 0; i < seqList.size(); i++) {
			seqList.get(i).playSequence();
		}
	}

	public void stop() {
		for (int i = 0; i < seqList.size(); i++) {
			seqList.get(i).stopSequence();
		}
	}

	public void changeKey(NoteGenerator key) {
		for (int i = 0; i < seqList.size(); i++) {
			seqList.get(i).setKey(key);
		}
	}

	public void changeBpm(int bpm) {
		for (int i = 0; i < seqList.size(); i++) {
			seqList.get(i).setBpm(bpm);
		}
	}

	public int getLastIndex() {
		return seqList.size() - 1;
	}

	public void removeSequencer(int index) {
		seqList.get(index).disposeGui();
		seqList.remove(index);
	}

	public void open(int index) {
		seqList.get(index).open();

	}

	public void mute(int index) {
		seqList.get(index).mute();

	}

	public void solo(int index) {
		seqList.get(index).solo();

	}

	public void rename(String title, int index) {
		seqList.get(index).setTitle(title);
	}

}
