package masterModule;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import arrangement.ArrangementWindow;
import note.KeyConverter;
import pattern.StandardPattern;

/**
 * Controller for the Master Module. Contains logic for managing the different
 * sequencers and scenes in mstrMoModel and Graphical user interfaces in the
 * mstrMoGui and arrangeMenstWindow(arrWin). Here within is also the masterClock
 * that drives all of the sequencers
 */

public class MstrMoController implements ActionListener {

	/**
	 * Creates placeholder for a KeyConverter, is used to convert a String
	 * containing the name of a musical key into a notegenerator of this named key
	 */
	private KeyConverter keyConv;
	/**
	 * Contains Gui for the Master Module
	 */
	private MstrMoGui mstrMoGui;
	/**
	 * Containts the logic for the Master Module
	 */
	private MstrMoModel mstrMoModel;
	/**
	 * Contains Gui for the arrangement, where you can arrange your patterns into
	 * songs
	 */
	private ArrangementWindow arrangeWindow;
	/**
	 * the clock that drives the whole
	 */
	private Timer masterClock;
	/**
	 * Keeps track of which scene is currently playing
	 */
	private int currentScene = 0;
	/**
	 * Keeps track on which bar in the current scene where at
	 */
	private int barCounter = 0;
	/**
	 * Keeps track on which beat in the current bar where at
	 */
	private int beatCounter = 0;
	/**
	 * Keeps track on which tick in the current beat where at
	 */
	private int tickCounter = 0;
	/**
	 * Keeps track of the next availible index in the sequencerArray
	 */
	private int nextIndex = 0;

	/**
	 * Constructor
	 */
	public MstrMoController() {

		mstrMoGui = new MstrMoGui();
		mstrMoModel = new MstrMoModel();
		keyConv = new KeyConverter();
		arrangeWindow = new ArrangementWindow();
		setMainClock(new Timer(500, this));

		// Add actionListeners to masterModuleGui
		mstrMoGui.getStandardSequencer().addActionListener(e -> createStandardSequencer());
		mstrMoGui.getDrumSequencer().addActionListener(e -> createDrumSequencer());
		mstrMoGui.getPlayStopButtons()[0].addActionListener(e -> start());
		mstrMoGui.getPlayStopButtons()[1].addActionListener(e -> stop());
		mstrMoGui.getKeyChooser().addChangeListener(e -> changeKey());
		mstrMoGui.getOpenArr().addActionListener(e -> openArrangeWindow());

		// Add actionListeners to arrangeWindow
		for (int i = 0; i < arrangeWindow.getSceneButtons().length; i++) {
			int index = i;
			arrangeWindow.getSceneButtons()[i].addActionListener(e -> clickSceneButton(index));
		}
		for (int i = 0; i < arrangeWindow.getSceneButtons().length; i++) {
			int index = i;
			arrangeWindow.getSceneButtons()[i].addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (SwingUtilities.isRightMouseButton(e)) {
						renameScene(index);
					}
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
				}
			});
		}
		for (int i = 0; i < arrangeWindow.getLengthChoosers().length; i++) {
			int index = i;
			arrangeWindow.getLengthChoosers()[i].addChangeListener(e -> changeSceneLength(index));
		}
		clickSceneButton(0);
	}

	// The following methods just adds or removes actionListeners from buttons and
	// such
	private void addActionListenersToArrWinInstr(int instrument) {
		for (int i = 0; i < 8; i++) {
			int scene = i;
			arrangeWindow.getSequenceChoosers()[instrument][i]
					.addActionListener(e -> setPatternChoice(instrument, scene));
		}
	}

	private void addActionListenersToSeqStrip(int index) {
		mstrMoGui.getTitles()[index].addActionListener(e -> rename(index));
		mstrMoGui.getOpen()[index].addActionListener(e -> open(index));
		mstrMoGui.getRemove()[index].addActionListener(e -> remove(index));
		mstrMoGui.getMute()[index].addActionListener(e -> mute(index));
		mstrMoGui.getSolo()[index].addActionListener(e -> solo(index));
	}

	private void removeActionListenerFromStrip(int index) {
		mstrMoGui.getTitles()[index].removeActionListener(e -> rename(index));
		mstrMoGui.getOpen()[index].removeActionListener(e -> open(index));
		mstrMoGui.getRemove()[index].removeActionListener(e -> remove(index));
		mstrMoGui.getMute()[index].removeActionListener(e -> mute(index));
		mstrMoGui.getSolo()[index].removeActionListener(e -> solo(index));
	}

	private void removeActionListenersFromArrWinInstr(int instrument) {
		for (int i = 0; i < 8; i++) {
			int scene = i;
			arrangeWindow.getSequenceChoosers()[instrument][i]
					.removeActionListener(e -> setPatternChoice(instrument, scene));
		}
	}

	/**
	 * When the masterClock generates an event this method will be called. The
	 * tickcounter will increase and the sequencers contained in this class will be
	 * notified. Also the Gui will be notified and the barcounter in it will be set
	 * acoordingly
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		tickCounter++;
		if (tickCounter == 1) {
			arrangeWindow.markCurrentScene(currentScene);
			beatCounter++;
			mstrMoGui.setBeatCounter(beatCounter);
			if (beatCounter == 1) {
				barCounter++;
				mstrMoGui.setBarCounter(barCounter);
			}
		}
		mstrMoModel.tick();
		if (barCounter == mstrMoModel.getSceneLength(currentScene)) {
			if (beatCounter == 4 && tickCounter == 16) {
				arrangeWindow.unMarkCurrentScene(currentScene);
				currentScene = mstrMoModel.getNextActiveScene(currentScene, arrangeWindow.loopIsSelected());
				if (currentScene > -1) {
					mstrMoModel.setActivePatterns(currentScene);
					barCounter = 0;
				} else {
					stop();
				}
			}
		}
		if (beatCounter == 4 && tickCounter == 16) {
			beatCounter = 0;
		}
		if (tickCounter == 16) {
			tickCounter = 0;
		}
	}

	/**
	 * Gets everything ready for playback and then starts playback
	 */
	private void start() {
		if (!mstrMoModel.isRunning()) {
			mstrMoModel.setRunning(true);
			changeBpm();
			currentScene = mstrMoModel.getFirstActiveScene();
			mstrMoModel.setActivePatterns(currentScene);
			masterClock.start();
			mstrMoModel.start();
			mstrMoGui.disableGui(mstrMoModel.lastUsedIndex());
		}
	}

	/**
	 * Stops playback of the sequencers and do some associated stuff like unmark the
	 * active scene, init some variables and the barcounter of the gui
	 */
	private void stop() {
		if (mstrMoModel.isRunning()) {
			mstrMoModel.setRunning(false);
			masterClock.stop();
			barCounter = 0;
			beatCounter = 0;
			tickCounter = 0;
			mstrMoGui.setBarCounter(barCounter);
			mstrMoGui.setBeatCounter(beatCounter);
			mstrMoModel.stop();
			mstrMoGui.enableGui(mstrMoModel.lastUsedIndex());
			if (currentScene > -1) {
				arrangeWindow.unMarkCurrentScene(currentScene);
			} else {
				arrangeWindow.unMarkCurrentScene(mstrMoModel.getLastActiveScene());
			}
		}
	}

	/**
	 * Opens the arrangewindow if its closed.
	 */
	private void openArrangeWindow() {
		arrangeWindow.setVisible(true);
	}

	/**
	 * Renames the choosen scene
	 * 
	 * @param sceneNr
	 *            the index of the scene to be renamed.
	 */
	private void renameScene(int sceneNr) {
		mstrMoModel.renameScene(sceneNr, arrangeWindow.renameScene(sceneNr));

	}

	/**
	 * Changes the length of the scene in steps of one bar
	 * 
	 * @param scene
	 *            the index of the scene to be changed
	 */
	private void changeSceneLength(int scene) {
		mstrMoModel.setSceneLength(scene, (int) arrangeWindow.getLengthChoosers()[scene].getValue());
	}

	/**
	 * If you click a scenebutton this method is called and the scene is set active
	 * which means it and its associated sequences will be included in the playback
	 * 
	 * @param scene
	 *            the scene to be set as active
	 */
	private void clickSceneButton(int scene) {
		if (!mstrMoModel.isSceneActive(scene)) {
			mstrMoModel.setSceneActive(scene, true);
			arrangeWindow.markActiveScene(scene);
		} else {
			mstrMoModel.setSceneActive(scene, false);
			arrangeWindow.unmarkAciveScene(scene);
		}
	}

	/**
	 * Change the tempo of the playback by setting the masterClock delay, divided by
	 * 16 to get the delay between the ticks, 16 ticks is one beat
	 */
	private void changeBpm() {
		masterClock.setDelay(60000 / getBpm() / 16);
	}

	/**
	 * Change the musical key from which the notegenerators will generate notes.
	 * Apllies to all sequencers contained in mstrMoModel
	 */
	private void changeKey() {
		mstrMoModel.changeKey(keyConv.getKey(mstrMoGui.getKey()));
	}

	/**
	 * Creates a new instance of the standard sequencer and gui for this. Adds
	 * guistrips in mastermodule and arrangeWindow. Collects all the information
	 * needed, like key and bpm from the mastermodule and pass it on Max 8
	 * sequencers can be created
	 */
	private void createStandardSequencer() {
		if (nextIndex < 8) {
			mstrMoModel.createStandardSequencer(keyConv.getKey(mstrMoGui.getKey()), nextIndex, "Stnd Sequencer");
			int index = nextIndex;
			mstrMoModel.getCopyButton(index).addActionListener(e -> copy(index));
			mstrMoModel.getPasteButton(index).addActionListener(e -> paste(index));
			mstrMoGui.addNewSeqStrip("Stnd Sequencer", nextIndex);
			addActionListenersToSeqStrip(nextIndex);
			arrangeWindow.addSequencer(nextIndex, mstrMoModel.getTitle(nextIndex),
					mstrMoModel.getPatternNames(nextIndex));
			addActionListenersToArrWinInstr(nextIndex);
			setNextIndex();
		}
	}

	private void createDrumSequencer() {
		if(nextIndex < 8) {
			mstrMoModel.createDrumSequencer(nextIndex, "Drum Sequencer");
			int index = nextIndex;
			mstrMoModel.getCopyButton(index).addActionListener(e -> copy(index));
			mstrMoModel.getPasteButton(index).addActionListener(e -> paste(index));
			mstrMoGui.addNewSeqStrip("Drum Sequencer", nextIndex);
			addActionListenersToSeqStrip(nextIndex);
			arrangeWindow.addSequencer(nextIndex, mstrMoModel.getTitle(nextIndex),
					mstrMoModel.getPatternNames(nextIndex));
			addActionListenersToArrWinInstr(nextIndex);
			setNextIndex();
		}
	}
	
	/**
	 * makes a individual copy of a pattern and stores it in the clipboard in
	 * mstrMoModel.
	 * 
	 * @param index
	 *            index of the instrument/sequencer whos active pattern is to be
	 *            copied
	 */
	public void copy(int index) {
		mstrMoModel.copyPattern(index);
	}

	/**
	 * If there is a pattern stored in
	 * 
	 * @param index
	 */
	public void paste(int index) {
		mstrMoModel.pastePattern(index);
	}

	/**
	 * This method is used to set the choice of pattern for a instrument in a given
	 * scene
	 * 
	 * @param sequencer
	 *            the sequencer to choose pattern for
	 * @param scene
	 *            the scene to set the choice of pattern in
	 */
	private void setPatternChoice(int sequencer, int scene) {
		mstrMoModel.setPatternChoice(scene, sequencer, arrangeWindow.getSequenceChoice(sequencer, scene));
	}

	/**
	 * If a sequencer gui is closed/hidden this method can be used to open/show it
	 * again
	 * 
	 * @param index
	 *            index of the sequencer to show
	 */
	private void open(int index) {
		mstrMoModel.open(index);
	}

	/**
	 * Creates a popup-menu where you can type in a new name for the given sequencer
	 * 
	 * @param index
	 *            index of the sequencer to rename
	 */
	private void rename(int index) {
		mstrMoModel.renameSequencer(mstrMoGui.getTitle(index), index);
		arrangeWindow.changeTitle(mstrMoGui.getTitle(index), index);
	}

	/**
	 * Removes a sequencer from the sequencerArray. Disposes all associated Gui. All
	 * data contained is lost. Also reorders the sequencerArray and the arrays of
	 * Gui-strips so theres no gap in the arrays
	 * 
	 * @param index
	 *            index of the sequencer to be removed
	 */
	private void remove(int index) {
		// removeActionListenersFromArrWinInstr(index);
		arrangeWindow.removeAllInstruments(mstrMoModel.lastUsedIndex());
		mstrMoModel.removeSequencer(index);
		// removeActionListenerFromStrip(index);
		mstrMoGui.removeAllSeqStrips();
		addStripsToGui(mstrMoModel.lastUsedIndex());
		mstrMoGui.paintAndPack();
		setNextIndex();
		addSequencerToArrWin(mstrMoModel.lastUsedIndex());
		arrangeWindow.repaintAndPack();
	}

	/**
	 * Adds a new strip to represent a sequencer in the arrangeWindow. Also adds
	 * actionListeners to the strip
	 * 
	 * @param lastUsedIndex
	 *            the last used index in the sequencerArray so the method shall now
	 *            where to put the new sequencer
	 */
	private void addSequencerToArrWin(int lastUsedIndex) {
		for (int i = 0; i <= lastUsedIndex; i++) {
			arrangeWindow.addSequencer(i, mstrMoModel.getTitle(i), mstrMoModel.getPatternNames(i));
			addActionListenersToArrWinInstr(i);
		}
	}

	/**
	 * Adds a new strip in the master module Gui to represent a Sequencer
	 * 
	 * @param lastUsedIndex
	 *            the last used index in the sequencerArray so the method shall now
	 *            where to put the new strip
	 */
	private void addStripsToGui(int lastUsedIndex) {
		for (int i = 0; i <= lastUsedIndex; i++) {
			mstrMoGui.addNewSeqStrip(mstrMoModel.getTitle(i), i);
			addActionListenersToSeqStrip(i);
		}
	}

	/**
	 * Checks an array and return an Integer of the last used index. Most often used
	 * for checking the sequencerArray to see where to put a new sequencer
	 * 
	 * @param array
	 *            the array to check, probably the sequencerArray in mstrMoModel
	 * @return the last index in the array that isnt nulll
	 */
	public int getLastUsedIndex(Object[] array) {
		int index = -1;
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null) {
				index = i;
			}
		}
		return index;
	}

	/**
	 * Sets the soloMuteBars in the sequencerStrips of the mstrMoGui acoording to
	 * what each separate sequencer is set to
	 */
	private void setSoloMuteBars() {
		for (int i = 0; i <= mstrMoModel.lastUsedIndex(); i++) {
			mstrMoGui.setSoloMuteBar(mstrMoModel.getSoloMute(i), i);
		}
	}

	/**
	 * Sets the selected sequencer to mute and repaints all soloMuteBars accordingly
	 * 
	 * @param index
	 *            index of the sequencer to be set to mute
	 */
	private void mute(int index) {
		mstrMoModel.mute(index);
		setSoloMuteBars();
	}

	/**
	 * Sets the selected sequencer to solo and repaints all soloMuteBars accordingly
	 * 
	 * @param index
	 *            index of the sequencer to be set to solo
	 */
	private void solo(int index) {
		mstrMoModel.solo(index);
		setSoloMuteBars();
	}

	/**
	 * Checks which index in the sequencerArray is next to be filled with a
	 * sequencer and sets the nextIndex to show this.
	 */
	private void setNextIndex() {
		nextIndex = mstrMoModel.nextIndex();
	}

	// Simple getters and setters

	private int getBpm() {
		return mstrMoGui.getBpm();
	}

	public Timer getMainClock() {
		return masterClock;
	}

	public void setMainClock(Timer mainClock) {
		this.masterClock = mainClock;
	}
}
