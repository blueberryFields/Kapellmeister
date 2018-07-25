package controller;

import javax.swing.JOptionPane;

import Gui.MstrMoGui;
import model.MstrMoModel;
import note.KeyConverter;

public class MstrMoController {

	private KeyConverter keyConv;
	private MstrMoGui mstrMoGui;
	private MstrMoModel mstrMoModel;
	
	private int nextIndex = 0;

	public MstrMoController() {

		mstrMoGui = new MstrMoGui();
		mstrMoModel = new MstrMoModel();
		keyConv = new KeyConverter();

		// Add actionListeners
		mstrMoGui.getStandardSequencer().addActionListener(e -> createStandardSequencer());
		mstrMoGui.getPlayStopButtons()[0].addActionListener(e -> start());
		mstrMoGui.getPlayStopButtons()[1].addActionListener(e -> stop());
		mstrMoGui.getKeyChooser().addChangeListener(e -> changeKey());
		// mstrMoGui.getBpmChooser().addActionListener(e -> changeBpm());
	}

	private void changeBpm() {
		mstrMoModel.changeBpm(mstrMoGui.getBpm());
	}

	private void changeKey() {
		mstrMoModel.changeKey(keyConv.getKey(mstrMoGui.getKey()));
	}

	private void createStandardSequencer() {
		mstrMoModel.createStandardSequencer(keyConv.getKey(mstrMoGui.getKey()), mstrMoGui.getBpm(), nextIndex);
		mstrMoGui.addNewSeqStrip("Stnd Sequencer", nextIndex);
		addActionListenersToSeqStrip(nextIndex);
		setNextIndex();
	}

	private void addActionListenersToSeqStrip(int index) {
		mstrMoGui.getTitles()[index].addActionListener(e -> rename(index));
		mstrMoGui.getOpen()[index].addActionListener(e -> open(index));
		mstrMoGui.getRemove()[index].addActionListener(e -> remove(index));
		mstrMoGui.getMute()[index].addActionListener(e -> mute(index));
		mstrMoGui.getSolo()[index].addActionListener(e -> solo(index));
	}

	private void open(int index) {
		mstrMoModel.open(index);
	}

	private void rename(int index) {
		mstrMoModel.rename(mstrMoGui.getTitle(index), index);
	}

	private void remove(int index) {
		mstrMoModel.removeSequencer(index);
		mstrMoGui.removeSeqStrip(index);
	}

	private void mute(int index) {
		mstrMoModel.mute(index);
	}

	private void solo(int index) {
		mstrMoModel.solo(index);
	}

	private void start() {
		changeBpm();
		mstrMoModel.start();
	}

	private void stop() {
		mstrMoModel.stop();
	}

	private void setNextIndex() {
		nextIndex = mstrMoModel.getNextIndex();
	}
}
