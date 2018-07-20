package controller;

import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import Gui.MstrMoGui;
import model.MstrMoModel;

public class MstrMoController {

	private MstrMoGui mstrMoGui;
	private MstrMoModel mstrMoModel;

	private List<SequencerController> seqList = new LinkedList<SequencerController>();

	public MstrMoController() {

//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
				mstrMoGui = new MstrMoGui();
		// }
		// });

		mstrMoModel = new MstrMoModel();

		// Add actionListeners
		mstrMoGui.getStandardSequencer().addActionListener(e -> createStandardSequencer());
	}

	private void createStandardSequencer() {
		seqList.add(new SequencerController(mstrMoModel.getKey(), mstrMoGui.getBpm()));

	}
}
