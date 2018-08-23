package arrangement;

/**
 * This class represents a piece of musical arrangement or a part of the song if
 * you will, a scene. It stores information on which pattern each sequencer will
 * play in this part of the song aswell as the length of the scene.
 */

public class Scene {
	/**
	 * The title/name of the scene
	 */
	private String title;
	/**
	 * The length of the scene in bars, one bar is always 4 beats
	 */
	private int length = 4;
	/**
	 * An array containing info on which pattern should be played by each sequencer
	 */
	private int[] patternChoice = new int[8];
	/**
	 * This boolean will know if the scene is set to active or not. If its set to
	 * active(done by clicking the sceneButton) it will be included in the playback
	 * of the song
	 */
	private boolean active = false;

	/**
	 * Constructor
	 * 
	 * @param title
	 *            the title/name of the scene
	 * @param choiceOfPattern
	 *            the choice of pattern to be set for each sequencer
	 */
	public Scene(String title, int choiceOfPattern) {
		for (int i = 0; i < patternChoice.length; i++) {
			patternChoice[i] = choiceOfPattern;
		}
		this.title = title;
	}

	// The rest is simple getters and setters
	
	public void setLength(int length) {
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public void setPatternChoice(int sequencer, int patternChoice) {
		this.patternChoice[sequencer] = patternChoice;
	}

	public int getPatternChoice(int index) {
		return patternChoice[index];
	}

	public String getName() {
		return title;
	}

	public void setName(String name) {
		this.title = name;
	}

}
