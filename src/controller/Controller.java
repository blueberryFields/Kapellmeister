package controller;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.swing.SwingUtilities;

import Gui.PrototypeGui;
import sequncerMotor.Sequencer;

public class Controller {

    private Sequencer seq;
    
    public Controller() {
	seq = new Sequencer();
		
    }
    
//    public void displayAvailibleMidiDevices(){
//	gui.DisplayAvailibleMidiDevices(seq.getAvailibleMidiDevices());
//    }
    
    public void setMidiDevice(int index) {
	seq.chooseMidiDevice(index);
    }

    public Info[] getAvailibleMidiDevices(){
	return seq.getAvailibleMidiDevices();
	
    }
}
