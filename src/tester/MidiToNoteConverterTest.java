package tester;

import note.MidiToNoteConverter;
import note.NoteToMidiConverter;

public class MidiToNoteConverterTest {
	
	
	public static void main(String[] args) {
		NoteToMidiConverter noteToMidiConverter = new NoteToMidiConverter();
		String key = "G3";
		System.out.println("På element " + key + ": " + noteToMidiConverter.get(key));

	}

}
