package note;

import java.util.HashMap;
import java.util.Map;

/**
 * This class helps you convert from noteName to the corresponding
 * midiNoteNumber
 */

public class NoteToMidiConverter {

	Map<String, Integer> noteToMidiMap = new HashMap<String, Integer>();

	/**
	 * Constructor. Creates a HashMap and fills it with key/value-pairs for all the
	 * notes that exists in midi, from 0-127
	 */
	public NoteToMidiConverter() {
		noteToMidiMap.put("C-1", 0);
		noteToMidiMap.put("C#-1", 1);
		noteToMidiMap.put("D-1", 2);
		noteToMidiMap.put("D#-1", 3);
		noteToMidiMap.put("E-1", 4);
		noteToMidiMap.put("F-1", 5);
		noteToMidiMap.put("F#-1", 6);
		noteToMidiMap.put("G-1", 7);
		noteToMidiMap.put("G#-1", 8);
		noteToMidiMap.put("A-1", 9);
		noteToMidiMap.put("A#-1", 10);
		noteToMidiMap.put("B-1", 11);
		noteToMidiMap.put("C0", 12);
		noteToMidiMap.put("C#0", 13);
		noteToMidiMap.put("D0", 14);
		noteToMidiMap.put("D#0", 15);
		noteToMidiMap.put("E0", 16);
		noteToMidiMap.put("F0", 17);
		noteToMidiMap.put("F#0", 18);
		noteToMidiMap.put("G0", 19);
		noteToMidiMap.put("G#0", 20);
		noteToMidiMap.put("A0", 21);
		noteToMidiMap.put("A#0", 22);
		noteToMidiMap.put("B0", 23);
		noteToMidiMap.put("C1", 24);
		noteToMidiMap.put("C#1", 25);
		noteToMidiMap.put("D1", 26);
		noteToMidiMap.put("D#1", 27);
		noteToMidiMap.put("E1", 28);
		noteToMidiMap.put("F1", 29);
		noteToMidiMap.put("F#1", 30);
		noteToMidiMap.put("G1", 31);
		noteToMidiMap.put("G#1", 32);
		noteToMidiMap.put("A1", 33);
		noteToMidiMap.put("A#1", 34);
		noteToMidiMap.put("B1", 35);
		noteToMidiMap.put("C2", 36);
		noteToMidiMap.put("C#2", 37);
		noteToMidiMap.put("D2", 38);
		noteToMidiMap.put("D#2", 39);
		noteToMidiMap.put("E2", 40);
		noteToMidiMap.put("F2", 41);
		noteToMidiMap.put("F#2", 42);
		noteToMidiMap.put("G2", 43);
		noteToMidiMap.put("G#2", 44);
		noteToMidiMap.put("A2", 45);
		noteToMidiMap.put("A#2", 46);
		noteToMidiMap.put("B2", 47);
		noteToMidiMap.put("C3", 48);
		noteToMidiMap.put("C#3", 49);
		noteToMidiMap.put("D3", 50);
		noteToMidiMap.put("D#3", 51);
		noteToMidiMap.put("E3", 52);
		noteToMidiMap.put("F3", 53);
		noteToMidiMap.put("F#3", 54);
		noteToMidiMap.put("G3", 55);
		noteToMidiMap.put("G#3", 56);
		noteToMidiMap.put("A3", 57);
		noteToMidiMap.put("A#3", 58);
		noteToMidiMap.put("B3", 59);
		noteToMidiMap.put("C4", 60);
		noteToMidiMap.put("C#4", 61);
		noteToMidiMap.put("D4", 62);
		noteToMidiMap.put("D#4", 63);
		noteToMidiMap.put("E4", 64);
		noteToMidiMap.put("F4", 65);
		noteToMidiMap.put("F#4", 66);
		noteToMidiMap.put("G4", 67);
		noteToMidiMap.put("G#4", 68);
		noteToMidiMap.put("A4", 69);
		noteToMidiMap.put("A#4", 70);
		noteToMidiMap.put("B4", 71);
		noteToMidiMap.put("C5", 72);
		noteToMidiMap.put("C#5", 73);
		noteToMidiMap.put("D5", 74);
		noteToMidiMap.put("D#5", 75);
		noteToMidiMap.put("E5", 76);
		noteToMidiMap.put("F5", 77);
		noteToMidiMap.put("F#5", 78);
		noteToMidiMap.put("G5", 79);
		noteToMidiMap.put("G#5", 80);
		noteToMidiMap.put("A5", 81);
		noteToMidiMap.put("A#5", 82);
		noteToMidiMap.put("B5", 83);
		noteToMidiMap.put("C6", 84);
		noteToMidiMap.put("C#6", 85);
		noteToMidiMap.put("D6", 86);
		noteToMidiMap.put("D#6", 87);
		noteToMidiMap.put("E6", 88);
		noteToMidiMap.put("F6", 89);
		noteToMidiMap.put("F#6", 90);
		noteToMidiMap.put("G6", 91);
		noteToMidiMap.put("G#6", 92);
		noteToMidiMap.put("A6", 93);
		noteToMidiMap.put("A#6", 94);
		noteToMidiMap.put("B6", 95);
		noteToMidiMap.put("C7", 96);
		noteToMidiMap.put("C#7", 97);
		noteToMidiMap.put("D7", 98);
		noteToMidiMap.put("D#7", 99);
		noteToMidiMap.put("E7", 100);
		noteToMidiMap.put("F7", 101);
		noteToMidiMap.put("F#7", 102);
		noteToMidiMap.put("G7", 103);
		noteToMidiMap.put("G#7", 104);
		noteToMidiMap.put("A7", 105);
		noteToMidiMap.put("A#7", 106);
		noteToMidiMap.put("B7", 107);
		noteToMidiMap.put("C8", 108);
		noteToMidiMap.put("C#8", 109);
		noteToMidiMap.put("D8", 110);
		noteToMidiMap.put("D#8", 111);
		noteToMidiMap.put("E8", 112);
		noteToMidiMap.put("F8", 113);
		noteToMidiMap.put("F#8", 114);
		noteToMidiMap.put("G8", 115);
		noteToMidiMap.put("G#8", 116);
		noteToMidiMap.put("A8", 117);
		noteToMidiMap.put("A#8", 118);
		noteToMidiMap.put("B8", 119);
		noteToMidiMap.put("C9", 120);
		noteToMidiMap.put("C#9", 121);
		noteToMidiMap.put("D9", 122);
		noteToMidiMap.put("D#9", 123);
		noteToMidiMap.put("E9", 124);
		noteToMidiMap.put("F9", 125);
		noteToMidiMap.put("F#9", 126);
		noteToMidiMap.put("G9", 127);
	}

	/**
	 * @param note
	 *            the name of hte note you want to convert into midi
	 * @return the corresponding midiNoteNumber
	 */
	public int get(String note) {
		return noteToMidiMap.get(note);
	}
}
