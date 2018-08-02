package main;

import javax.swing.SwingUtilities;

import masterModule.MstrMoController;

public class KapellMeisterMain {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MstrMoController masterModuleCntr = new MstrMoController();
			}
		});

	}
}
