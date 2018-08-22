package note;

/**
 * This class is used to convert a String containing the name of a musical key
 * into a notegenerator of this named key
 */
public class KeyConverter {

	/**
	 * 
	 * @param key
	 *            a String with the name of the desired key
	 * @return a NoteGenerator of the desired musical key
	 */

	public NoteGenerator getKey(String key) {
		switch (key) {
		case "Am":
			return new Am();
		case "A":
			return new A();
		case "Bbm":
			return new Bbm();
		case "C":
			return new C();
		}
		return null;
	}
}
