package pattern;

/**
 * This is the base/super-class for a pattern or sequence of notes that can be
 * played by a sequencer. Further specification is made in the sub-classes:
 * StandardPattern and DrumPattern.
 */

public class PatternBase {

	/**
	 * Title/name of the pattern
	 */
	protected String title;

	/**
	 * Constructor
	 * 
	 * @param title
	 *            a String containing the title of the pattern
	 */
	public PatternBase(String title) {
		this.title = title;
	}

	// The rest is simple getters and setters

	public String getName() {
		return title;
	}

	public void setName(String name) {
		this.title = name;
	}
}
