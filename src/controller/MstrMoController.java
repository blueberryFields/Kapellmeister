package controller;

import Gui.MstrMoGui;
import model.MstrMoModel;
import note.KeyConverter;

public class MstrMoController {

	private KeyConverter keyConv;
	private MstrMoGui mstrMoGui;
	private MstrMoModel mstrMoModel;

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
		mstrMoModel.createStandardSequencer(keyConv.getKey(mstrMoGui.getKey()), mstrMoGui.getBpm());
		mstrMoGui.addNewSeqStrip("Standard Sequencer", mstrMoModel.getLastIndex());
	}

	private void start() {
		changeBpm();
		mstrMoModel.start();
	}

	private void stop() {
		mstrMoModel.stop();
	}

}
