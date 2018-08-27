package standardSequencer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import note.Note;
import sequecerBase.SequencerGuiBase;

public class StandardSequencerGui extends SequencerGuiBase {

	/**
	 * The graphic user interface for the standard sequencers.
	 */

	// Create components for steppanel
	private JPanel stepPanel = new JPanel();
	private JPanel[] singleSteps = new JPanel[16];
	private String[] notes = new String[] { "C-1", "C#-1", "D-1", "D#-1", "E-1", "F#-1", "G-1", "G#-1", "A-1", "A#-1",
			"B-1", "C0", "C#0", "D0", "D#0", "E0", "F0", "F#0", "G0", "G#0", "A0", "A#0", "B0", "C1", "C#1", "D1",
			"D#1", "E1", "F1", "F#1", "G1", "G#1", "A1", "A#1", "B1", "C2", "C#2", "D2", "D#2", "E2", "F2", "F#2", "G2",
			"G#2", "A2", "A#2", "B2", "C3", "C#3", "D3", "D#3", "E3", "F3", "F#3", "G3", "G#3", "A3", "A#3", "B3", "C4",
			"C#4", "D4", "D#4", "E4", "F4", "F#4", "G4", "G#4", "A4", "A#4", "B4", "C5", "C#5", "D5", "D#5", "E5", "F5",
			"F#5", "G5", "G#5", "A5", "A#5", "B5", "C6", "C#6", "D6", "D#6", "E6", "F6", "F#6", "G6", "G#6", "A6",
			"A#6", "B6", "C7", "C#7", "D7", "D#7", "E7", "F7", "F#7", "G7", "G#7", "A7", "A#7", "B7", "C8", "C#8", "D8",
			"D#8", "E8", "F8", "F#8", "G8", "G#8", "A8", "A#8", "B8", "C9", "C#9", "D9", "D#9", "E9", "F9", "F#9",
			"G9", };
	private SpinnerListModel[] noteModel = new SpinnerListModel[16];
	private JSpinner[] noteChooser = new JSpinner[16];
	private SpinnerModel[] velocityModel = new SpinnerNumberModel[16];
	private JSpinner velocityChooser[] = new JSpinner[16];
	private JButton[] noteOnButton = new JButton[16];

	// Create components for generatorpanel
	private JPanel octaveRangePanel = new JPanel();
	private JLabel octaveRangeFromText = new JLabel("Octave-range, from:");
	private JLabel octaveRangeToText = new JLabel("to:");
	private SpinnerModel octaveLowModel = new SpinnerNumberModel(3, -1, 9, 1);
	private SpinnerModel octaveHighModel = new SpinnerNumberModel(3, -1, 9, 1);
	private JSpinner octaveLowChooser = new JSpinner(octaveLowModel);
	private JSpinner octaveHighChooser = new JSpinner(octaveHighModel);

	// Create components for nudgePanel
	private JPanel nudgePanel = new JPanel();
	private JButton nudgeLeft = new JButton("<-");
	private JButton nudgeRight = new JButton("->");
	private JLabel nudgeText = new JLabel("Nudge Sequence");

	/**
	 * Constructor
	 * 
	 * @param infos
	 *            the availeble mididevices to be displayed in the deviceChooser
	 * @param title
	 *            the title of the sequencer/instrument to be displayed in the top
	 *            of the frame
	 */
	public StandardSequencerGui(Info[] infos, String title) {
		super(infos, title);

		genAlgorithmStrings = new String[] { "Rnd notes", "Rnd notes, no dupl in row", "Rnd notes and On/Hold/Off",
				"Rnd notes, no dupl in row, On/Hold/Off" };
		generatorAlgorithmChooser = new JComboBox<>(genAlgorithmStrings);
		generatorAlgorithmPanel.add(generatorAlgorithmText);
		generatorAlgorithmPanel.add(generatorAlgorithmChooser);
		
		// Set colors for panels n the like
		nudgePanel.setBackground(backGroundColor);
		stepPanel.setBackground(backGroundColor);
		octaveRangePanel.setBackground(backGroundColor);

		octaveLowChooser.setEditor(new JSpinner.DefaultEditor(octaveLowChooser));
		octaveHighChooser.setEditor(new JSpinner.DefaultEditor(octaveHighChooser));

		octaveLowChooser.setPreferredSize(new Dimension(40, 25));
		octaveHighChooser.setPreferredSize(new Dimension(40, 25));

		octaveRangePanel.add(octaveRangeFromText);
		octaveRangePanel.add(octaveLowChooser);
		octaveRangePanel.add(octaveRangeToText);
		octaveRangePanel.add(octaveHighChooser);

		generatePanel.add(octaveRangePanel);

		// Add stuff to and configure nudgeSequencePanel
		nudgeLeft.setPreferredSize(buttonDimSmall);
		nudgeRight.setPreferredSize(buttonDimSmall);
		nudgePanel.add(nudgeLeft);
		nudgePanel.add(nudgeText);
		nudgePanel.add(nudgeRight);

		copyPaste[0] = new JButton("Copy");
		copyPaste[1] = new JButton("Paste");
		for (int i = 0; i < copyPaste.length; i++) {
			copyPaste[i].setPreferredSize(buttonDimLarge);
			nudgePanel.add(copyPaste[i]);
		}

		// Add stuff to and configure stepPanel
		for (int i = 0; i < noteChooser.length; i++) {
			noteModel[i] = new SpinnerListModel(notes);
			noteChooser[i] = new JSpinner(noteModel[i]);
			noteChooser[i].setEditor(new JSpinner.DefaultEditor(noteChooser[i]));
			noteChooser[i].setPreferredSize(noteChooserDim);
		}

		for (int i = 0; i < velocityChooser.length; i++) {
			velocityModel[i] = new SpinnerNumberModel(100, 0, 127, 1);
			velocityChooser[i] = new JSpinner(velocityModel[i]);
			velocityChooser[i].setEditor(new JSpinner.DefaultEditor(velocityChooser[i]));
			velocityChooser[i].setPreferredSize(veloChooserDim);
		}
		for (int i = 0; i < noteOnButton.length; i++) {
			noteOnButton[i] = new JButton("");
			noteOnButton[i].setPreferredSize(buttonDimSmall);
		}

		for (int i = 0; i < singleSteps.length; i++) {
			singleSteps[i] = new JPanel();
			singleSteps[i].setLayout(new GridBagLayout());
			singleSteps[i].setBackground(enabledStepColor);
			GridBagConstraints singleStepsGbc = new GridBagConstraints();
			// singleStepsGbc.insets = new Insets(1, 1, 1, 1);
			singleStepsGbc.gridx = 0;
			singleStepsGbc.gridy = 0;
			singleSteps[i].add(noteChooser[i], singleStepsGbc);
			singleStepsGbc.gridx = 1;
			singleStepsGbc.gridy = 0;
			singleSteps[i].add(velocityChooser[i], singleStepsGbc);
			singleStepsGbc.gridx = 0;
			singleStepsGbc.gridy = 1;
			singleSteps[i].add(noteOnButton[i], singleStepsGbc);
		}

		stepPanel.setLayout(new GridBagLayout());
		GridBagConstraints stepPanelGbc = new GridBagConstraints();
		stepPanelGbc.insets = new Insets(2, 2, 2, 2);
		stepPanelGbc.gridx = 0;
		stepPanelGbc.gridy = 0;

		int k = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				stepPanel.add(singleSteps[k], stepPanelGbc);
				stepPanelGbc.gridx++;
				k++;
			}
			stepPanelGbc.gridy++;
			stepPanelGbc.gridx = 0;
		}

		// configure and add stuff to frame
		frame.setLayout(new GridBagLayout());
		GridBagConstraints frameGbc = new GridBagConstraints();
		frameGbc.insets = new Insets(5, 5, 5, 5);
		frameGbc.gridx = 0;
		frameGbc.gridy = 0;
		frame.add(channelPanel, frameGbc);

		frameGbc.gridy = 1;
		frame.add(generatePanel, frameGbc);

		frameGbc.gridy = 2;
		frame.add(rndVeloPanel, frameGbc);

		frameGbc.gridy = 3;
		frame.add(generatorAlgorithmPanel, frameGbc);

		frameGbc.gridy = 4;
		frame.add(nudgePanel, frameGbc);

		frameGbc.gridy = 5;
		frame.add(stepPanel, frameGbc);

		frameGbc.gridy = 6;
		frame.add(patternPanel, frameGbc);

		frameGbc.gridy = 7;
		frame.add(patternSettingsPanel, frameGbc);

		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Checks the passed pattern and repaints the stepSequencer accordingly
	 * 
	 * @param pattern
	 *            the pattern to be the model for the stepSequencer view
	 */
	public void repaintSequencer(Note[] pattern) {

		// Enable steps wich is included in sequence
		for (int i = 0; i < pattern.length; i++) {
			noteChooser[i].setEnabled(true);
			velocityChooser[i].setEnabled(true);
			noteOnButton[i].setEnabled(true);
			singleSteps[i].setBackground(enabledStepColor);
		}

		// set note, NoteOn and velocity on steps
		for (int i = 0; i < pattern.length; i++) {
			velocityChooser[i].setValue(pattern[i].getVelo());
			noteChooser[i].setValue(pattern[i].getNote());
			switch (pattern[i].getNoteOn()) {
			case ON:
				noteOnButton[i].setText("On");
				break;
			case HOLD:
				noteOnButton[i].setText("Hold");
				break;
			case OFF:
				noteOnButton[i].setText("Off");
				break;
			}
		}

		// Disable steps wich is not included in sequence
		for (int i = pattern.length; i < 16; i++) {
			noteChooser[i].setEnabled(false);
			velocityChooser[i].setEnabled(false);
			noteOnButton[i].setEnabled(false);
			noteOnButton[i].setText("Off");
			singleSteps[i].setBackground(disabledStepColor);
		}
	}

	/**
	 * Marks the currently playing note in the stepSequencer
	 * 
	 * @param currentStep
	 *            the currently playing step
	 * @param isFirstNote
	 *            boolean indicating if this is the first note since pressing start
	 * @param pattern
	 *            the pattern playing
	 */
	public void markActiveStep(int currentStep, boolean isFirstNote, Note[] pattern) {
		if (isFirstNote) {
			singleSteps[currentStep].setBackground(activeStepColor);
			disableStep(currentStep);
		} else if (currentStep == 0 && !isFirstNote) {
			singleSteps[currentStep].setBackground(activeStepColor);
			singleSteps[pattern.length - 1].setBackground(enabledStepColor);
			disableStep(currentStep);
			enableStep(pattern.length - 1);
		} else {
			singleSteps[currentStep].setBackground(activeStepColor);
			singleSteps[currentStep - 1].setBackground(enabledStepColor);
			disableStep(currentStep);
			enableStep(currentStep - 1);
		}
	}

	/**
	 * Disables parts of the Gui so the user can´t change settings that shouldnt be
	 * changed when sequencer is playing
	 */
	public void disableGui() {
		deviceChooser.setEnabled(false);
		midiChannelChooser.setEnabled(false);
		generateButton.setEnabled(false);
		octaveLowChooser.setEnabled(false);
		octaveHighChooser.setEnabled(false);
		rndVeloCheckBox.setEnabled(false);
		veloLowChooser.setEnabled(false);
		veloHighChooser.setEnabled(false);
		generatorAlgorithmChooser.setEnabled(false);
		refreshButton.setEnabled(false);
		for (int i = 0; i < patternChoosers.length; i++) {
			patternChoosers[i].setEnabled(false);
		}
		copyPaste[0].setEnabled(false);
		copyPaste[1].setEnabled(false);
		nudgeLeft.setEnabled(false);
		nudgeRight.setEnabled(false);
	}

	/**
	 * Enables the parts of the Gui that the disableGui() disables...
	 */
	public void enableGui() {
		deviceChooser.setEnabled(true);
		midiChannelChooser.setEnabled(true);
		generateButton.setEnabled(true);
		octaveLowChooser.setEnabled(true);
		octaveHighChooser.setEnabled(true);
		rndVeloCheckBox.setEnabled(true);
		veloLowChooser.setEnabled(true);
		veloHighChooser.setEnabled(true);
		generatorAlgorithmChooser.setEnabled(true);
		refreshButton.setEnabled(true);
		for (int i = 0; i < patternChoosers.length; i++) {
			patternChoosers[i].setEnabled(true);
		}
		copyPaste[0].setEnabled(true);
		copyPaste[1].setEnabled(true);
		nudgeLeft.setEnabled(true);
		nudgeRight.setEnabled(true);
	}

	// // WORK IN PROGRESS!!!
	// public void setAvailibleDevices(Info[] infos) {
	// availibleDevices = new String[infos.length + 1];
	// availibleDevices[0] = "Choose a device...";
	// for (int i = 1; i < availibleDevices.length; i++) {
	// availibleDevices[i] = infos[i - 1].toString();
	// }
	// deviceChooserModel = new DefaultComboBoxModel<String>(getAvailibleDevices());
	// deviceChooser.setModel(deviceChooserModel);
	// }

	/**
	 * If a pattern is being played the step that is currently playing will be
	 * marked with red color and disabled. When you stop the playback this method
	 * can be used to make sure the note is enabled again and the red color will
	 * disappear
	 * 
	 * @param currentStep
	 *            the step/note currently being played and marked as active and to
	 *            be unmarked
	 * @param isFirstNote
	 *            is the current step the first note of the first repetition of the
	 *            pattern?
	 * @param pattern
	 *            the pattern currently being played
	 */
	public void unmarkActiveStep(int currentStep, boolean isFirstNote, Note[] pattern) {
		if (isFirstNote) {
			singleSteps[currentStep].setBackground(enabledStepColor);
			enableStep(currentStep);
		} else if (currentStep == 0 && !isFirstNote) {
			singleSteps[pattern.length - 1].setBackground(enabledStepColor);
			enableStep(pattern.length - 1);
		} else {
			singleSteps[currentStep - 1].setBackground(enabledStepColor);
			enableStep(currentStep - 1);
		}
	}

	/**
	 * Disables a choosen step so the user cannot change it while it is playing
	 * since this probably will cause some kind of error
	 * 
	 * @param stepIndex
	 *            the step to be disabled
	 */
	public void disableStep(int stepIndex) {
		noteChooser[stepIndex].setEnabled(false);
		velocityChooser[stepIndex].setEnabled(false);
		noteOnButton[stepIndex].setEnabled(false);
	}

	/**
	 * Enables a step that has been disabled though the method disableStep(int
	 * stepindex)
	 * 
	 * @param stepIndex
	 *            the disabled step to be enabled
	 */
	public void enableStep(int stepIndex) {
		noteChooser[stepIndex].setEnabled(true);
		velocityChooser[stepIndex].setEnabled(true);
		noteOnButton[stepIndex].setEnabled(true);
	}

	// The rest here is mostly basic getters and setters

	public JSpinner[] getNoteChooserArray() {
		return noteChooser;
	}

	public JSpinner getNoteChooser(int index) {
		return noteChooser[index];
	}

	public JSpinner[] getVelocityChooserArray() {
		return velocityChooser;
	}

	public JSpinner getVelocityChooser(int index) {
		return velocityChooser[index];
	}

	public JButton[] getNoteOnButtonArray() {
		return noteOnButton;
	}

	public JButton getNoteOnButton(int index) {
		return noteOnButton[index];
	}

	public void setNoteOnButtonText(int index, String text) {
		noteOnButton[index].setText(text);
	}

	public String getNoteOnButtonText(int index) {
		return noteOnButton[index].getText();
	}

	public JButton getNudgeLeft() {
		return nudgeLeft;
	}

	public JButton getNudgeRight() {
		return nudgeRight;
	}

	public JSpinner getOctaveLowChooser() {
		return octaveLowChooser;
	}

	public JSpinner getOctaveHighChooser() {
		return octaveHighChooser;
	}

	public int getOctaveLow() {
		return (int) octaveLowChooser.getValue();
	}

	public int getOctaveHigh() {
		return (int) octaveHighChooser.getValue();
	}

	public void setOctaveLow(int value) {
		octaveLowChooser.setValue(value);
	}

	public void setOctaveHigh(int value) {
		octaveHighChooser.setValue(value);
	}

}
