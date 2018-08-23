package note;

/**
 * Subclass to noteGenerator. Represents the musical key C major
 */

public class C extends NoteGenerator {

	/**
	 * Constructor. Fills the notes-list with the notes in A minor
	 */
	public C() {
		notes.add(new String("C"));
		notes.add(new String("D"));
		notes.add(new String("E"));
		notes.add(new String("F"));
		notes.add(new String("G"));
		notes.add(new String("A"));
		notes.add(new String("B"));
	}
}
