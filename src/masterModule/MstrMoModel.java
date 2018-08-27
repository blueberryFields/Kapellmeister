package masterModule;

import javax.swing.JButton;

import arrangement.Pattern;
import arrangement.Scene;
import note.NoteGenerator;
import sequecerBase.SequencerControllerBase;
import sequecerBase.SoloMute;
import standardSequencer.StandardSequencerController;

/**
 * The heart of the whole application, the core and most of the logic for the
 * master module. This class contains all the sequencers and the arrangement
 */

public class MstrMoModel {

	/**
	 * Contains up to 8 instances of sequencers
	 */
	private StandardSequencerController[] sequencerArray = new StandardSequencerController[8];
	/**
	 * Contains the 8 available scenes in wich you can store information on which
	 * pattern to play in which scene
	 */
	private Scene[] scenes = new Scene[8];
	/**
	 * Boolean that indicates if the program is running/playing
	 */
	private boolean running = false;
	/**
	 * Clipboard for storing patterns you copy via the copy-functionality
	 */
	private Pattern clipBoard;

	/**
	 * Constructor
	 */
	public MstrMoModel() {
		for (int i = 0; i < scenes.length; i++) {
			scenes[i] = new Scene("Scene " + (i + 1), i);
		}
	}

	/**
	 * Checks the scenes for the next scene in turn that is set to active
	 * 
	 * @param currentScene
	 *            the scene currently playing
	 * @param loop
	 *            is the song set to loop? In that case when there is no more active
	 *            scenes following the currently playing this method will return the
	 *            first one in the scenes array, else it will return -1 which
	 *            indicates that the program shall stop playback
	 * 
	 * @return the index of the next scene to be played or -1 which indicates that
	 *         the program shall stop playback if there is no more scenes to be
	 *         played
	 */
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

	/**
	 * Checks where in the scenes array the first active scene is, i.e. where to
	 * start playback. If there is no active scene -1 will be return
	 * 
	 * @return index of the first active scene in order or -1 if theres in no one
	 */
	public int getFirstActiveScene() {
		for (int i = 0; i < scenes.length; i++) {
			if (scenes[i].isActive()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Checks where in the scenes array the last active scene is
	 * 
	 * @return index of the last active scene or -1 if there is no one
	 */
	public int getLastActiveScene() {
		for (int i = scenes.length - 1; i >= 0; i--) {
			if (scenes[i].isActive()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * When playing back this method is used to set which pattern each sequencer
	 * should play during the current scene
	 * 
	 * @param currentScene
	 *            index of the current playing scene
	 */
	public void setActivePatterns(int currentScene) {
		for (int i = 0; i <= lastUsedIndex(); i++) {
			sequencerArray[i].choosePattern(scenes[currentScene].getPatternChoice(i));
		}
	}

	/**
	 * This method is used to set the choice of pattern for a instrument in a given
	 * scene
	 * 
	 * @param scene
	 *            the scene to set the choice of pattern in
	 * @param sequencer
	 *            the sequencer to choose pattern for
	 * @param patternChoice
	 *            the choice of pattern for the given instrument in the given scene
	 */
	public void setPatternChoice(int scene, int sequencer, int patternChoice) {
		scenes[scene].setPatternChoice(sequencer, patternChoice);
	}

	/**
	 * Creates a new instance of the standard sequencer in the sequencerArray.
	 * 
	 * @param key
	 *            the musical key from which to draw notes when generating patterns
	 * @param index
	 *            which index in the sequencer array to put the new sequencer
	 * @param title
	 *            a String containing the title of the new Sequencer
	 */
	public void createStandardSequencer(NoteGenerator key, int index, String title) {
		sequencerArray[index] = new StandardSequencerController(key, title);
	}

	/**
	 * Creates a indivudal copy of the selected pattern and stores it in the
	 * clipboard
	 * 
	 * @param index
	 *            index of the pattern to be copied
	 */
	public void copyPattern(int index) {
		clipBoard = sequencerArray[index].copyPattern();
	}

	/**
	 * replace the selected pattern with the pattern stored in the clipboard(if any)
	 * 
	 * @param index
	 *            index of the pattern to be replaced
	 */
	public void pastePattern(int index) {
		if (clipBoard != null) {
			sequencerArray[index].pastePattern(clipBoard);
		}
	}

	/**
	 * Tells all the sequencers in the sequencerArray to take one step forward in
	 * the tickgrid
	 */
	public void tick() {
		for (int i = 0; i <= lastUsedIndex(); i++) {
			sequencerArray[i].tick();
		}
	}

	/**
	 * Sets all the sequencers in the sequencerArray in playMode
	 */
	public void start() {
		for (int i = 0; i <= lastUsedIndex(); i++) {
			sequencerArray[i].playMode();
		}
	}

	/**
	 * Sets all the sequencers in the sequencerArray in stopMode
	 */
	public void stop() {
		for (int i = 0; i <= lastUsedIndex(); i++) {
			sequencerArray[i].stopMode();
		}
	}

	/**
	 * Changes key of the noteGEnerators in all of the sequencers in the
	 * sequencerArray
	 * 
	 * @param key
	 *            the new key to be passed to the sequencers
	 */
	public void changeKey(NoteGenerator key) {
		for (int i = 0; i < sequencerArray.length; i++) {
			sequencerArray[i].setKey(key);
		}
	}

	/**
	 * Checks the sequencerArray for the next free index
	 * 
	 * @return the next free index in the sequencerArray
	 */
	public int nextIndex() {
		int i = 0;
		while (i < 8) {
			if (sequencerArray[i] == null) {
				break;
			}
			i++;
		}
		return i;
	}

	/**
	 * Checks the sequencerArray for the last used index
	 * 
	 * @return the last index thats not null.
	 */
	public int lastUsedIndex() {
		int index = -1;
		for (int i = 0; i < sequencerArray.length; i++) {
			if (sequencerArray[i] != null) {
				index = i;
			}
		}
		return index;
	}

	/**
	 * Removes the given sequencer from the sequencerArray and disposes all related
	 * Gui
	 * 
	 * @param index
	 *            index of the sequencer to be removed
	 */
	public void removeSequencer(int index) {
		sequencerArray[index].disposeGui();
		sequencerArray[index] = null;
		orderSeqArr();
	}

	/**
	 * Goes through the sequencerArray and closes all eventual gaps
	 */
	public void orderSeqArr() {
		for (int i = lastUsedIndex(); i > 0; i--) {
			for (int j = lastUsedIndex(); j > 0; j--) {
				if (sequencerArray[j] != null && sequencerArray[j - 1] == null) {
					sequencerArray[j - 1] = sequencerArray[j];
					sequencerArray[j] = null;
					break;
				}
			}
		}
	}

	/**
	 * shows the given sequencer gui if its not visible
	 * 
	 * @param index
	 *            index of the sequencer to show
	 */
	public void open(int index) {
		sequencerArray[index].open();
	}

	/**
	 * If any sequencer is soloed this method will unsolo it and unmute the selected
	 * sequencer. If the selected sequencer is not muted and no sequencer is soloed
	 * the selected sequencer will be muted
	 * 
	 * @param index
	 */
	public void mute(int index) {
		for (int i = 0; i <= lastUsedIndex(); i++) {
			if (sequencerArray[i].getSoloMute() == SoloMute.SOLO) {
				sequencerArray[i].unSoloMute();
			}
		}
		sequencerArray[index].muteUnmute();
	}

	/**
	 * If the selected sequencer is not soloed this method will solo it and mute all
	 * the others. If already soloed it will be unsoloed
	 * 
	 * @param index
	 */
	public void solo(int index) {
		if (sequencerArray[index].getSoloMute() != SoloMute.SOLO) {
			sequencerArray[index].solo();
			for (int i = 0; i <= lastUsedIndex(); i++) {
				if (i != index) {
					sequencerArray[i].mute();
				}
			}
		} else {
			sequencerArray[index].solo();
			for (int i = 0; i <= lastUsedIndex(); i++) {
				if (i != index) {
					sequencerArray[i].unSoloMute();
				}
			}
		}
	}

	/**
	 * Renames the given sequencer
	 * 
	 * @param title
	 *            the new name of the sequencer
	 * @param index
	 *            index of the sequencer to be renamed
	 */
	public void renameSequencer(String title, int index) {
		sequencerArray[index].setTitle(title);
	}

	/**
	 * renames the given scene
	 * 
	 * @param sceneNr
	 *            index of the scene to be renamed
	 * @param newName
	 *            the new name of the scene
	 */
	public void renameScene(int sceneNr, String newName) {
		scenes[sceneNr].setName(newName);
	}

	/**
	 * Not really sure whats the intention with this method
	 */
	// public void killLastNote(int lastScene) {
	// for (int i = 0; i <= lastUsedIndex(); i++) {
	// sequencerArray[i].killLastNote(scenes[lastScene].getPatternChoice(i));
	// }
	// }

	// The rest here is simple getters and setters

	public Scene[] getScenes() {
		return scenes;
	}

	public String[] getPatternNames(int index) {
		return sequencerArray[index].getPatternNames();
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

	public JButton getCopyButton(int index) {
		return sequencerArray[index].getCopyButton();
	}

	public JButton getPasteButton(int index) {
		return sequencerArray[index].getPasteButton();
	}

	public SoloMute getSoloMute(int index) {
		return sequencerArray[index].getSoloMute();
	}

	public String getTitle(int index) {
		return sequencerArray[index].getTitle();
	}

	public StandardSequencerController[] getSeqArr() {
		return sequencerArray;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
