package sequncerMotor;

public class MidiNote {

	private int note;
	private int velo;
	
	//konstruktor
	public MidiNote() {
		note = 48;
		velo = 100;
	}
	
	public MidiNote(int note, int velo) {
		this.note = note;
		this.velo = velo;
	}
	
	public int getNote() {
		return note;
	}
	public void setNote(int note) {
		this.note = note;
	}
	public int getVelo() {
		return velo;
	}
	public void setVelo(int velo) {
		this.velo = velo;
	}
}
