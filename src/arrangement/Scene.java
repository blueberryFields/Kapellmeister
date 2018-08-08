package arrangement;

public class Scene {
	private String name;
	private int length = 4;
	private int[] sequenceChoice = new int[8];
	private boolean active = false;

	public Scene(String name, int choiceOfSequence) {
		for (int i = 0; i < sequenceChoice.length; i++) {
			sequenceChoice[i] = choiceOfSequence;
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

	public void setSequenceChoice(int instrument, int sequenceChoice) {
		this.sequenceChoice[instrument] = sequenceChoice;
	}

	public int getSequenceChoice(int index) {
		return sequenceChoice[index];
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
