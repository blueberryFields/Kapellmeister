package note;

/**
 *  A test, isnt used in the application as for now, doubt it even works...
 */

import java.util.HashMap;

public class MidiToNoteConverter {

	HashMap<Integer, String> hmap;

	public void MidiToNoteConverter() {
		hmap = new HashMap<Integer, String>();

		hmap.put(48, "C3");
		hmap.put(49, "C#3");
		hmap.put(50, "D3");
		hmap.put(51, "D#3");
		hmap.put(52, "E3");
		hmap.put(53, "F3");
		hmap.put(54, "F#3");
		hmap.put(55, "G3");
		hmap.put(56, "G#3");
		hmap.put(57, "A3");
		hmap.put(58, "A#3");
		hmap.put(59, "B3");
		hmap.put(60, "C4");
		hmap.put(61, "C#4");
		hmap.put(62, "D4");
		hmap.put(63, "D#4");
		hmap.put(64, "E4");
		hmap.put(65, "F4");
		hmap.put(66, "F#4");
		hmap.put(67, "G4");
		hmap.put(68, "G#4");
		hmap.put(69, "A4");
		hmap.put(70, "A#4");
		hmap.put(71, "B4");

	}

	public String getNote(int key) {
		return hmap.get(key);
	}

	public String getNoteSwitch(int key) {
		switch (key) {
		case 48:
			return "C3";
		case 49:
			return "C#3";
		case 50:
			return "D3";
		case 51:
			return "D#3";
		case 52:
			return "E3";
		case 53:
			return "F3";
		case 54:
			return "F#3";
		case 55:
			return "G3";
		case 56:
			return "G#";
		case 57:
			return "A3";
		case 58:
			return "A#3";
		case 59:
			return "B3";
		case 60:
			return "C4";
		case 61:
			return "C#4";
		case 62:
			return "D4";
		case 63:
			return "D#4";
		case 64:
			return "E4";
		case 65:
			return "F4";
		case 66:
			return "F#4";
		case 67:
			return "G4";
		case 68:
			return "G#4";
		case 69:
			return "A4";
		case 70:
			return "A#4";
		case 71:
			return "B4";
		}
		return null;
	}
}
