package masterModule;

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
		mstrMoModel.createStandardSequencer(keyConv.getKey(mstrMoGui.getKey()), mstrMoGui.getBpm(), nextIndex,
				"Stnd Sequencer");
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

	private void removeActionListenderFromStrip(int index) {
		mstrMoGui.getTitles()[index].removeActionListener(e -> rename(index));
		mstrMoGui.getOpen()[index].removeActionListener(e -> open(index));
		mstrMoGui.getRemove()[index].removeActionListener(e -> remove(index));
		mstrMoGui.getMute()[index].removeActionListener(e -> mute(index));
		mstrMoGui.getSolo()[index].removeActionListener(e -> solo(index));
	}

	private void open(int index) {
		mstrMoModel.open(index);
	}

	private void rename(int index) {
		mstrMoModel.rename(mstrMoGui.getTitle(index), index);
	}

	private void remove(int index) {
		mstrMoModel.removeSequencer(index);
		removeActionListenderFromStrip(index);
		mstrMoGui.removeAllSeqStrips();
		addStripsToGui(mstrMoModel.lastUsedIndex());
		mstrMoGui.paintAndPack();
	}

	private void addStripsToGui(int lastUsedIndex) {
		for (int i = 0; i <= lastUsedIndex; i++) {
			mstrMoGui.addNewSeqStrip(mstrMoModel.getTitle(i), i);
			addActionListenersToSeqStrip(i);
		}
	}

	public int getLastUsedIndex(Object[] array) {
		int index = -1;
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null) {
				index = i;
			}
		}
		return index;
	}

	private void setSeqStripsColor() {
		for (int i = 0; i <= mstrMoModel.lastUsedIndex(); i++) {
			mstrMoGui.setSoloMuteBar(mstrMoModel.getSoloMute(i), i);
		}
	}

	private void mute(int index) {
		mstrMoModel.mute(index);
		mstrMoGui.setSoloMuteBar(mstrMoModel.getSoloMute(index), index);
	}

	private void solo(int index) {
		mstrMoModel.solo(index);
		setSeqStripsColor();
	}

	private void start() {
		changeBpm();
		mstrMoModel.start();
		mstrMoGui.disableGui(mstrMoModel.lastUsedIndex());
	}

	private void stop() {
		mstrMoModel.stop();
		mstrMoGui.enableGui(mstrMoModel.lastUsedIndex());
	}

	private void setNextIndex() {
		nextIndex = mstrMoModel.nextIndex();
	}
}
