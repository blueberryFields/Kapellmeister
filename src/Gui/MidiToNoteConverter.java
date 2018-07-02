package Gui;

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
		hmap.put(71, "C4");

	}

	public String getNote(int key) {
		return hmap.get(key);
	}
}
