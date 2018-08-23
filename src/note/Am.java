package note;

/**
 * Subclass to noteGenerator. Represents the musical key A minor
 */
public class Am extends NoteGenerator {

	/**
	 * Constructor. Fills the notes-list with the notes in A minor
	 */
	public Am() {
		notes.add(new String("A"));
		notes.add(new String("B"));
		notes.add(new String("C"));
		notes.add(new String("D"));
		notes.add(new String("E"));
		notes.add(new String("F"));
		notes.add(new String("G"));
	}
}
