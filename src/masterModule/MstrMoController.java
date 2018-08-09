package masterModule;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import arrangement.ArrangementWindow;
import note.KeyConverter;

public class MstrMoController implements ActionListener {

	private KeyConverter keyConv;
	private MstrMoGui mstrMoGui;
	private MstrMoModel mstrMoModel;
	private ArrangementWindow arrWin;
	private Timer masterClock;
	private int beatCounter = 0;
	private int tickCounter = 0;
	private int currentScene = 0;

	private int nextIndex = 0;

	public MstrMoController() {

		mstrMoGui = new MstrMoGui();
		mstrMoModel = new MstrMoModel();
		keyConv = new KeyConverter();
		arrWin = new ArrangementWindow();
		setMainClock(new Timer(500, this));

		// Add actionListeners to masterModuleGui
		mstrMoGui.getStandardSequencer().addActionListener(e -> createStandardSequencer());
		mstrMoGui.getPlayStopButtons()[0].addActionListener(e -> start());
		mstrMoGui.getPlayStopButtons()[1].addActionListener(e -> stop());
		mstrMoGui.getKeyChooser().addChangeListener(e -> changeKey());
		mstrMoGui.getOpenArr().addActionListener(e -> openArrWin());

		// Add actionListeners to arrangeWindow
		for (int i = 0; i < arrWin.getSceneButtons().length; i++) {
			int index = i;
			arrWin.getSceneButtons()[i].addActionListener(e -> clickSceneButton(index));
		}
		for (int i = 0; i < arrWin.getLengthChoosers().length; i++) {
			int index = i;
			arrWin.getLengthChoosers()[i].addChangeListener(e -> changeSceneLength(index));
		}
		clickSceneButton(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		tickCounter++;
		if (tickCounter == 1) {
			arrWin.markCurrentScene(currentScene);
			beatCounter++;
			mstrMoGui.setBeatCounter(beatCounter);
		}
		mstrMoModel.tick();
		if (beatCounter == mstrMoModel.getSceneLength(currentScene)) {
			if (tickCounter == 4) {
				arrWin.unMarkCurrentScene(currentScene);
				currentScene = mstrMoModel.getNextActiveScene(currentScene, arrWin.loopIsSelected());
				if (currentScene > -1) {
					mstrMoModel.setActiveSequences(currentScene);
					beatCounter = 0;
				} else {
					stop();
					// arrWin.unMarkCurrentScene(currentScene - 1);
				}
				// mstrMoGui.setBeatCounter(beatCounter);
			}
		}
		if (tickCounter == 4) {
			tickCounter = 0;
		}
	}

	private void start() {
		if (!mstrMoModel.isRunning()) {
			mstrMoModel.setRunning(true);
			changeBpm();
			currentScene = mstrMoModel.getFirstActiveScene();
			mstrMoModel.setActiveSequences(currentScene);
			masterClock.start();
			mstrMoModel.start();
			mstrMoGui.disableGui(mstrMoModel.lastUsedIndex());
		}
	}

	private void stop() {
		if (mstrMoModel.isRunning()) {
			mstrMoModel.setRunning(false);
			masterClock.stop();
			beatCounter = 0;
			tickCounter = 0;
			mstrMoGui.setBeatCounter(beatCounter);
			mstrMoModel.stop();
			mstrMoGui.enableGui(mstrMoModel.lastUsedIndex());
			if (currentScene > -1) {
				arrWin.unMarkCurrentScene(currentScene);
			} else {
				arrWin.unMarkCurrentScene(mstrMoModel.getLastActiveScene());
			}
		}
	}

	private void openArrWin() {
		arrWin.setVisible(true);
	}

	private void changeSceneLength(int scene) {
		mstrMoModel.setSceneLength(scene, (int) arrWin.getLengthChoosers()[scene].getValue());
	}

	private void clickSceneButton(int scene) {
		if (!mstrMoModel.isSceneActive(scene)) {
			mstrMoModel.setSceneActive(scene, true);
			arrWin.markActiveScene(scene);
		} else {
			mstrMoModel.setSceneActive(scene, false);
			arrWin.unmarkAciveScene(scene);
		}
	}

	private void changeBpm() {
		masterClock.setDelay(60000 / getBpm() / 4);
	}

	private int getBpm() {
		return mstrMoGui.getBpm();
	}

	private void changeKey() {
		mstrMoModel.changeKey(keyConv.getKey(mstrMoGui.getKey()));
	}

	private void createStandardSequencer() {
		if (nextIndex < 8) {
			mstrMoModel.createStandardSequencer(keyConv.getKey(mstrMoGui.getKey()), getBpm(), nextIndex,
					"Stnd Sequencer");
			mstrMoGui.addNewSeqStrip("Stnd Sequencer", nextIndex);
			addActionListenersToSeqStrip(nextIndex);
			arrWin.addInstrument(nextIndex, mstrMoModel.getTitle(nextIndex), mstrMoModel.getSequenceNames(nextIndex));
			addActionListenersToArrWin(nextIndex);
			setNextIndex();
		}
	}

	private void addActionListenersToArrWin(int instrument) {
		for (int i = 0; i < 8; i++) {
			int scene = i;
			arrWin.getSequenceChoosers()[instrument][i].addActionListener(e -> setSequenceChoice(instrument, scene));
		}
	}

	private void setSequenceChoice(int instrument, int scene) {
		mstrMoModel.setSequenceChoice(scene, instrument, arrWin.getSequenceChoice(instrument, scene));
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
		arrWin.changeTitle(mstrMoGui.getTitle(index), index);
	}

	private void remove(int index) {
		mstrMoModel.removeSequencer(index);
		removeActionListenderFromStrip(index);
		mstrMoGui.removeAllSeqStrips();
		addStripsToGui(mstrMoModel.lastUsedIndex());
		mstrMoGui.paintAndPack();
		setNextIndex();
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

	private void setNextIndex() {
		nextIndex = mstrMoModel.nextIndex();
	}

	public Timer getMainClock() {
		return masterClock;
	}

	public void setMainClock(Timer mainClock) {
		this.masterClock = mainClock;
	}
}
