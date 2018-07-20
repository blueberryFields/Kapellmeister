package main;

import javax.swing.SwingUtilities;

import Gui.SequencerGui;
import controller.SequencerController;

public class SeqMain {
    
    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {

	    @Override
	    public void run() {
		SequencerController ctrl = new SequencerController();
	    }
	});	
    }
}
