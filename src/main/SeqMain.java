package main;

import javax.swing.SwingUtilities;

import Gui.PrototypeGui;
import controller.Controller;

public class SeqMain {
    
    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {

	    @Override
	    public void run() {
		PrototypeGui gui = new PrototypeGui();
	    }
	});
	
    }

}
