package sequncerMotor;

public class MidiNote {

	private int midiNote;
	private int velo;
	private String note;
	
	//konstruktor
	public MidiNote() {
		midiNote = 48;
		velo = 100;
		setNote("C3");
	}
	
	public MidiNote(int midiNote, int velo, String note) {
		this.midiNote = midiNote;
		this.velo = velo;
		this.setNote(note);
	}
	
	public int getMidiNote() {
		return midiNote;
	}
	public void setMiniNote(int note) {
		this.midiNote = note;
	}
	public int getVelo() {
		return velo;
	}
	public void setVelo(int velo) {
		this.velo = velo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
