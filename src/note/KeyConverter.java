package note;

public class KeyConverter {

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
