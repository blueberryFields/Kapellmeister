package arrangement;

public class Scene {
	private String name;
	private int length = 4;
	private int[] patternChoice = new int[8];
	private boolean active = false;

	public Scene(String name, int choiceOfPattern) {
		for (int i = 0; i < patternChoice.length; i++) {
			patternChoice[i] = choiceOfPattern;
		}
		this.name = name;
	}

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
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
