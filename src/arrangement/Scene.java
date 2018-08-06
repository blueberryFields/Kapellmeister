package arrangement;

public class Scene {
	private int length = 4;
	private int[] sequenceChoice = new int[8];
	private boolean active = false;

	public Scene() {
		for (int i = 0; i < sequenceChoice.length; i++) {
			sequenceChoice[i] = i;
		}
	}
}
