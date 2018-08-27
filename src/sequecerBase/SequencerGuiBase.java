package sequecerBase;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import arrangement.Pattern;

public class SequencerGuiBase {

	/**
	 * The graphic user interface for the standard sequencers.
	 */

	// Create colorscheme
	protected Color backGroundColor = new Color(142, 175, 206);
	protected Color disabledStepColor = new Color(76, 94, 112);
	protected Color enabledStepColor = new Color(193, 218, 242);
	protected Color activeStepColor = Color.RED;
	protected Color muteColor = Color.BLUE;
	protected Color soloColor = Color.YELLOW;
	protected Color enabledText = Color.BLACK;
	protected Color disabledText = Color.GRAY;

	// Create dimensions
	protected Dimension buttonDimSmall = new Dimension(55, 25);
	protected Dimension buttonDimLarge = new Dimension(75, 25);
	protected Dimension veloChooserDim = new Dimension(50, 25);
	protected Dimension noteChooserDim = new Dimension(50, 25);
	protected Dimension soloMuteBarDim = new Dimension(55, 20);
	protected Dimension patternChooserDim = new Dimension(110, 25);

	// Create JFrame
	protected JFrame frame;


	// Create components for channelpanel
	protected JPanel channelPanel = new JPanel();
	protected String[] availibleDevices;
	protected JComboBox<String> deviceChooser;
	protected DefaultComboBoxModel<String> deviceChooserModel;
	protected JLabel channelText = new JLabel("ch:");
	protected Integer[] midiChannels = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
	protected JComboBox<Integer> midiChannelChooser = new JComboBox<Integer>(midiChannels);
	protected JButton refreshButton = new JButton("Refresh");
	protected JLabel soloMuteBar = new JLabel();

	// Create components for generatorpanel
	protected JPanel generatorPanel = new JPanel();
	protected JPanel generatePanel = new JPanel();
	protected JButton generateButton = new JButton("Generate");
	
	// Create components for the random velocity panel
	protected JPanel rndVeloPanel = new JPanel();
	protected JPanel rndVeloCheckPanel = new JPanel();
	protected JPanel veloLowPanel = new JPanel();
	protected JPanel veloHighPanel = new JPanel();
	protected JLabel rndVeloText = new JLabel("Random velocity:");
	protected JCheckBox rndVeloCheckBox = new JCheckBox();
	protected JLabel fromText = new JLabel("from:");
	protected JLabel toText = new JLabel("to:");
	protected SpinnerModel veloLowModel = new SpinnerNumberModel(70, 0, 127, 1);
	protected JSpinner veloLowChooser = new JSpinner(veloLowModel);
	protected SpinnerModel veloHighModel = new SpinnerNumberModel(110, 0, 127, 1);
	protected JSpinner veloHighChooser = new JSpinner(veloHighModel);

	// Create components for the generator algorithm panel
	protected JPanel generatorAlgorithmPanel = new JPanel();
	protected JLabel generatorAlgorithmText = new JLabel("Gen Algorithm:");
	protected String[] genAlgorithmStrings;
	protected JComboBox<String> generatorAlgorithmChooser;

	// // Create components for nudgePanel
	// private JPanel nudgePanel = new JPanel();
	// private JButton nudgeLeft = new JButton("<-");
	// private JButton nudgeRight = new JButton("->");
	// private JLabel nudgeText = new JLabel("Nudge Sequence");
	protected JButton[] copyPaste = new JButton[2];

	// Create components for patternsPanel
	protected JPanel patternPanel = new JPanel();
	protected JButton[] patternChoosers = new JButton[8];
	protected JPanel patternSettingsPanel = new JPanel();
	protected JLabel nrOfStepsText = new JLabel("Nr of steps:");
	protected SpinnerModel nrOfStepsModel = new SpinnerNumberModel(8, 1, 16, 1);
	protected JSpinner nrOfStepsChooser = new JSpinner(nrOfStepsModel);
	protected String[] partNotes = new String[] { "1 bar", "1/2", "1/4", "1/8", "1/16" };
	protected SpinnerModel partNotesModel = new SpinnerListModel(partNotes);
	protected JSpinner partNotesChooser = new JSpinner(partNotesModel);
	protected JLabel partNotesText = new JLabel("Partnotes:");
	protected JButton renamePattern = new JButton("Rename");

	/**
	 * Constructor
	 * 
	 * @param infos
	 *            the availeble mididevices to be displayed in the deviceChooser
	 * @param title
	 *            the title of the sequencer/instrument to be displayed in the top
	 *            of the frame
	 */
	public SequencerGuiBase(Info[] infos, String title) {
		frame = new JFrame(title);

		// Set colors for panels n the like
		rndVeloCheckPanel.setBackground(backGroundColor);
		rndVeloPanel.setBackground(backGroundColor);
		veloLowPanel.setBackground(backGroundColor);
		veloHighPanel.setBackground(backGroundColor);
		generatorPanel.setBackground(backGroundColor);
		generatePanel.setBackground(backGroundColor);
		generatorAlgorithmPanel.setBackground(backGroundColor);
		// nudgePanel.setBackground(backGroundColor);
		channelPanel.setBackground(backGroundColor);
		// stepPanel.setBackground(backGroundColor);
		// octaveRangePanel.setBackground(backGroundColor);
		soloMuteBar.setBackground(backGroundColor);

		// Set BackgroundColor for frame
		frame.getContentPane().setBackground(backGroundColor);

		// Add stuff to channelPanel
		// setAvailibleDevices(infos);
		availibleDevices = new String[infos.length + 1];
		availibleDevices[0] = "Choose a device...";
		for (int i = 1; i < availibleDevices.length; i++) {
			availibleDevices[i] = infos[i - 1].toString();
		}
		deviceChooserModel = new DefaultComboBoxModel<String>(availibleDevices);
		channelPanel.add(deviceChooser = new JComboBox<String>(deviceChooserModel));
		deviceChooser.setPreferredSize(new Dimension(175, 25));

		midiChannelChooser.setPreferredSize(new Dimension(70, 25));
		channelPanel.add(channelText);
		channelPanel.add(midiChannelChooser);
		soloMuteBar.setPreferredSize(soloMuteBarDim);
		soloMuteBar.setOpaque(true);
		soloMuteBar.setHorizontalAlignment(SwingConstants.CENTER);
		refreshButton.setPreferredSize(buttonDimLarge);
		channelPanel.add(refreshButton);
		channelPanel.add(soloMuteBar);

		generatePanel.add(generateButton);

		// Configure and add stuff to the random velocity panel
		veloLowChooser.setEditor(new JSpinner.DefaultEditor(veloLowChooser));
		veloHighChooser.setEditor(new JSpinner.DefaultEditor(veloHighChooser));

		rndVeloCheckPanel.add(rndVeloText);
		rndVeloCheckPanel.add(rndVeloCheckBox);
		rndVeloPanel.add(rndVeloCheckPanel);

		veloLowPanel.add(fromText);
		veloLowPanel.add(veloLowChooser);
		rndVeloPanel.add(veloLowPanel);

		veloHighPanel.add(toText);
		veloHighPanel.add(veloHighChooser);
		rndVeloPanel.add(veloHighPanel);

		// Configure
		

		// // Add stuff to and configure nudgeSequencePanel
		// nudgeLeft.setPreferredSize(buttonDimSmall);
		// nudgeRight.setPreferredSize(buttonDimSmall);
		// nudgePanel.add(nudgeLeft);
		// nudgePanel.add(nudgeText);
		// nudgePanel.add(nudgeRight);
		//
		// copyPaste[0] = new JButton("Copy");
		// copyPaste[1] = new JButton("Paste");
		// for (int i = 0; i < copyPaste.length; i++) {
		// copyPaste[i].setPreferredSize(buttonDimLarge);
		// nudgePanel.add(copyPaste[i]);
		// }

		// // Add stuff to and configure stepPanel
		// for (int i = 0; i < noteChooser.length; i++) {
		// noteModel[i] = new SpinnerListModel(notes);
		// noteChooser[i] = new JSpinner(noteModel[i]);
		// noteChooser[i].setEditor(new JSpinner.DefaultEditor(noteChooser[i]));
		// noteChooser[i].setPreferredSize(noteChooserDim);
		// }
		//
		// for (int i = 0; i < velocityChooser.length; i++) {
		// velocityModel[i] = new SpinnerNumberModel(100, 0, 127, 1);
		// velocityChooser[i] = new JSpinner(velocityModel[i]);
		// velocityChooser[i].setEditor(new JSpinner.DefaultEditor(velocityChooser[i]));
		// velocityChooser[i].setPreferredSize(veloChooserDim);
		// }
		// for (int i = 0; i < noteOnButton.length; i++) {
		// noteOnButton[i] = new JButton("");
		// noteOnButton[i].setPreferredSize(buttonDimSmall);
		// }
		//
		// for (int i = 0; i < singleSteps.length; i++) {
		// singleSteps[i] = new JPanel();
		// singleSteps[i].setLayout(new GridBagLayout());
		// singleSteps[i].setBackground(enabledStepColor);
		// GridBagConstraints singleStepsGbc = new GridBagConstraints();
		// // singleStepsGbc.insets = new Insets(1, 1, 1, 1);
		// singleStepsGbc.gridx = 0;
		// singleStepsGbc.gridy = 0;
		// singleSteps[i].add(noteChooser[i], singleStepsGbc);
		// singleStepsGbc.gridx = 1;
		// singleStepsGbc.gridy = 0;
		// singleSteps[i].add(velocityChooser[i], singleStepsGbc);
		// singleStepsGbc.gridx = 0;
		// singleStepsGbc.gridy = 1;
		// singleSteps[i].add(noteOnButton[i], singleStepsGbc);
		// }
		//
		// stepPanel.setLayout(new GridBagLayout());
		// GridBagConstraints stepPanelGbc = new GridBagConstraints();
		// stepPanelGbc.insets = new Insets(2, 2, 2, 2);
		// stepPanelGbc.gridx = 0;
		// stepPanelGbc.gridy = 0;
		//
		// int k = 0;
		// for (int i = 0; i < 4; i++) {
		// for (int j = 0; j < 4; j++) {
		// stepPanel.add(singleSteps[k], stepPanelGbc);
		// stepPanelGbc.gridx++;
		// k++;
		// }
		// stepPanelGbc.gridy++;
		// stepPanelGbc.gridx = 0;
		// }

		// Configure and add stuff to patternPanel
		patternPanel.setBackground(backGroundColor);
		patternPanel.setLayout(new GridBagLayout());
		GridBagConstraints patternPanelGbc = new GridBagConstraints();
		for (int i = 0; i < patternChoosers.length; i++) {
			patternChoosers[i] = new JButton();
			patternChoosers[i].setPreferredSize(patternChooserDim);
		}

		patternPanelGbc.gridx = 0;
		patternPanelGbc.gridy = 0;
		int k = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 4; j++) {
				patternChoosers[k].setForeground(disabledText);
				patternPanel.add(patternChoosers[k], patternPanelGbc);
				patternPanelGbc.gridx++;
				k++;
			}
			patternPanelGbc.gridy++;
			patternPanelGbc.gridx = 0;
		}
		patternChoosers[0].setForeground(enabledText);

		patternSettingsPanel.setBackground(backGroundColor);

		nrOfStepsChooser.setEditor(new JSpinner.DefaultEditor(nrOfStepsChooser));
		nrOfStepsChooser.setPreferredSize(new Dimension(43, 25));
		partNotesChooser.setEditor(new JSpinner.DefaultEditor(partNotesChooser));
		partNotesChooser.setValue("1/8");
		partNotesChooser.setPreferredSize(new Dimension(60, 25));
		patternSettingsPanel.add(nrOfStepsText);
		patternSettingsPanel.add(nrOfStepsChooser);
		patternSettingsPanel.add(partNotesText);
		patternSettingsPanel.add(partNotesChooser);
		patternSettingsPanel.add(renamePattern);

		// // configure and add stuff to frame
		// frame.setLayout(new GridBagLayout());
		// GridBagConstraints frameGbc = new GridBagConstraints();
		// frameGbc.insets = new Insets(5, 5, 5, 5);
		// frameGbc.gridx = 0;
		// frameGbc.gridy = 0;
		// frame.add(channelPanel, frameGbc);
		//
		// frameGbc.gridy = 1;
		// frame.add(generatePanel, frameGbc);
		//
		// frameGbc.gridy = 2;
		// frame.add(rndVeloPanel, frameGbc);
		//
		// frameGbc.gridy = 3;
		// frame.add(generatorAlgorithmPanel, frameGbc);
		//
		// frameGbc.gridy = 4;
		// frame.add(nudgePanel, frameGbc);
		//
		// frameGbc.gridy = 5;
		// frame.add(stepPanel, frameGbc);
		//
		// frameGbc.gridy = 6;
		// frame.add(patternPanel, frameGbc);
		//
		// frameGbc.gridy = 7;
		// frame.add(patternSettingsPanel, frameGbc);
		//
		// frame.setResizable(false);
		// frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		// frame.pack();
		// frame.setVisible(true);
	}

	/**
	 * Sets the title of the frame
	 * 
	 * @param title
	 *            a String containing the new title
	 */
	public void setTitle(String title) {
		frame.setTitle(title);
	}

	/**
	 * Calls repaint for the JFrame
	 */
	public void repaint() {
		frame.repaint();
	}

	/**
	 * Calls dispose for the JFrame
	 */
	public void dispose() {
		frame.dispose();
	}

	// /**
	// * Checks the passed pattern and repaints the stepSequencer accordingly
	// *
	// * @param pattern
	// * the pattern to be the model for the stepSequencer view
	// */
	// public void repaintSequencer(Note[] pattern) {
	//
	// // Enable steps wich is included in sequence
	// for (int i = 0; i < pattern.length; i++) {
	// noteChooser[i].setEnabled(true);
	// velocityChooser[i].setEnabled(true);
	// noteOnButton[i].setEnabled(true);
	// singleSteps[i].setBackground(enabledStepColor);
	// }
	//
	// // set note, NoteOn and velocity on steps
	// for (int i = 0; i < pattern.length; i++) {
	// velocityChooser[i].setValue(pattern[i].getVelo());
	// noteChooser[i].setValue(pattern[i].getNote());
	// switch (pattern[i].getNoteOn()) {
	// case ON:
	// noteOnButton[i].setText("On");
	// break;
	// case HOLD:
	// noteOnButton[i].setText("Hold");
	// break;
	// case OFF:
	// noteOnButton[i].setText("Off");
	// break;
	// }
	// }
	//
	// // Disable steps wich is not included in sequence
	// for (int i = pattern.length; i < 16; i++) {
	// noteChooser[i].setEnabled(false);
	// velocityChooser[i].setEnabled(false);
	// noteOnButton[i].setEnabled(false);
	// noteOnButton[i].setText("Off");
	// singleSteps[i].setBackground(disabledStepColor);
	// }
	// }

	/**
	 * Makes a popup appear where you can type in a new name for the active pattern
	 */
	public String renamePattern(int activePattern) {
		String newName = JOptionPane.showInputDialog(this, "New name:");
		patternChoosers[activePattern].setText(newName);
		return newName;
	}

	/**
	 * Updates the text on the patternChooserButtons according to the passed array
	 * of patterns
	 * 
	 * @param patterns
	 *            an array containing patterns from wich the names to be written on
	 *            the patternChooserButtons will be retrieved
	 */
	public void setPatternNames(Pattern[] patterns) {
		for (int i = 0; i < patterns.length; i++) {
			patternChoosers[i].setText(patterns[i].getName());
		}
	}

	/**
	 * Sets the choosen patternChooserButtons text to indicate it is enabled/active
	 * 
	 * @param index
	 *            the index of the patternChooserButton to be marked as
	 *            enabled/active
	 */
	public void enablePatternChooser(int index) {
		patternChoosers[index].setForeground(enabledText);
	}

	/**
	 * Sets the choosen patternChooserButtons text to indicate it is
	 * disabled/inactive
	 * 
	 * @param index
	 *            the index of the patternChooserButton to be marked as
	 *            disabled/inactive
	 */
	public void disablePatternChooser(int index) {
		patternChoosers[index].setForeground(disabledText);
	}

	/**
	 * Removes all items from the deviceChooser
	 */
	public void emptyDeviceChooser() {
		deviceChooser.removeAllItems();
	}

	// /**
	// * Marks the currently playing note in the stepSequencer
	// *
	// * @param currentStep
	// * the currently playing step
	// * @param isFirstNote
	// * boolean indicating if this is the first note since pressing start
	// * @param pattern
	// * the pattern playing
	// */
	// public void markActiveStep(int currentStep, boolean isFirstNote, Note[]
	// pattern) {
	// if (isFirstNote) {
	// singleSteps[currentStep].setBackground(activeStepColor);
	// disableStep(currentStep);
	// } else if (currentStep == 0 && !isFirstNote) {
	// singleSteps[currentStep].setBackground(activeStepColor);
	// singleSteps[pattern.length - 1].setBackground(enabledStepColor);
	// disableStep(currentStep);
	// enableStep(pattern.length - 1);
	// } else {
	// singleSteps[currentStep].setBackground(activeStepColor);
	// singleSteps[currentStep - 1].setBackground(enabledStepColor);
	// disableStep(currentStep);
	// enableStep(currentStep - 1);
	// }
	// }

	// /**
	// * Disables parts of the Gui so the user can´t change settings that shouldnt
	// be
	// * changed when sequencer is playing
	// */
	// public void disableGui() {
	// deviceChooser.setEnabled(false);
	// midiChannelChooser.setEnabled(false);
	// generateButton.setEnabled(false);
	// octaveLowChooser.setEnabled(false);
	// octaveHighChooser.setEnabled(false);
	// rndVeloCheckBox.setEnabled(false);
	// veloLowChooser.setEnabled(false);
	// veloHighChooser.setEnabled(false);
	// generatorAlgorithmChooser.setEnabled(false);
	// refreshButton.setEnabled(false);
	// for (int i = 0; i < patternChoosers.length; i++) {
	// patternChoosers[i].setEnabled(false);
	// }
	// copyPaste[0].setEnabled(false);
	// copyPaste[1].setEnabled(false);
	// nudgeLeft.setEnabled(false);
	// nudgeRight.setEnabled(false);
	// }
	//
	// /**
	// * Enables the parts of the Gui that the disableGui() disables...
	// */
	// public void enableGui() {
	// deviceChooser.setEnabled(true);
	// midiChannelChooser.setEnabled(true);
	// generateButton.setEnabled(true);
	// octaveLowChooser.setEnabled(true);
	// octaveHighChooser.setEnabled(true);
	// rndVeloCheckBox.setEnabled(true);
	// veloLowChooser.setEnabled(true);
	// veloHighChooser.setEnabled(true);
	// generatorAlgorithmChooser.setEnabled(true);
	// refreshButton.setEnabled(true);
	// for (int i = 0; i < patternChoosers.length; i++) {
	// patternChoosers[i].setEnabled(true);
	// }
	// copyPaste[0].setEnabled(true);
	// copyPaste[1].setEnabled(true);
	// nudgeLeft.setEnabled(true);
	// nudgeRight.setEnabled(true);
	// }

	// WORK IN PROGRESS!!!
	public void setAvailibleDevices(Info[] infos) {
		availibleDevices = new String[infos.length + 1];
		availibleDevices[0] = "Choose a device...";
		for (int i = 1; i < availibleDevices.length; i++) {
			availibleDevices[i] = infos[i - 1].toString();
		}
		deviceChooserModel = new DefaultComboBoxModel<String>(getAvailibleDevices());
		deviceChooser.setModel(deviceChooserModel);
	}

	// /**
	// * If a pattern is being played the step that is currently playing will be
	// * marked with red color and disabled. When you stop the playback this method
	// * can be used to make sure the note is enabled again and the red color will
	// * disappear
	// *
	// * @param currentStep
	// * the step/note currently being played and marked as active and to
	// * be unmarked
	// * @param isFirstNote
	// * is the current step the first note of the first repetition of the
	// * pattern?
	// * @param pattern
	// * the pattern currently being played
	// */
	// public void unmarkActiveStep(int currentStep, boolean isFirstNote, Note[]
	// pattern) {
	// if (isFirstNote) {
	// singleSteps[currentStep].setBackground(enabledStepColor);
	// enableStep(currentStep);
	// } else if (currentStep == 0 && !isFirstNote) {
	// singleSteps[pattern.length - 1].setBackground(enabledStepColor);
	// enableStep(pattern.length - 1);
	// } else {
	// singleSteps[currentStep - 1].setBackground(enabledStepColor);
	// enableStep(currentStep - 1);
	// }
	// }

	// /**
	// * Disables a choosen step so the user cannot change it while it is playing
	// * since this probably will cause some kind of error
	// *
	// * @param stepIndex
	// * the step to be disabled
	// */
	// public void disableStep(int stepIndex) {
	// noteChooser[stepIndex].setEnabled(false);
	// velocityChooser[stepIndex].setEnabled(false);
	// noteOnButton[stepIndex].setEnabled(false);
	// }

	// /**
	// * Enables a step that has been disabled though the method disableStep(int
	// * stepindex)
	// *
	// * @param stepIndex
	// * the disabled step to be enabled
	// */
	// public void enableStep(int stepIndex) {
	// noteChooser[stepIndex].setEnabled(true);
	// velocityChooser[stepIndex].setEnabled(true);
	// noteOnButton[stepIndex].setEnabled(true);
	// }

	/**
	 * Sets the frame to visible if its not.
	 */
	public void open() {
		frame.setVisible(true);
	}

	// The rest here is mostly basic getters and setters

	public JButton[] getPatternChoosers() {
		return patternChoosers;
	}

	public JPanel getPatternPanels() {
		return patternSettingsPanel;
	}

	public JSpinner getNrOfStepsChooser() {
		return nrOfStepsChooser;
	}

	public JSpinner getPartNotesChooser() {
		return partNotesChooser;
	}

	public JButton[] getPatterChoosers() {
		return patternChoosers;
	}

	public JComboBox<String> getDeviceChooser() {
		return deviceChooser;
	}

	public JButton getGenerateButton() {
		return generateButton;
	}

	public String[] getAvailibleDevices() {
		return availibleDevices;
	}

	public String getGeneratorAlgoRithmChooser() {
		return (String) generatorAlgorithmChooser.getSelectedItem();
	}

	public boolean isRndVeloChecked() {
		return rndVeloCheckBox.isSelected();
	}

	public int getVeloLowChooserValue() {
		return (int) veloLowChooser.getValue();
	}

	public void setVeloLowChooserValue(int value) {
		veloLowChooser.setValue(value);
	}

	public int getVeloHighChooserValue() {
		return (int) veloHighChooser.getValue();
	}

	public void setVeloHighChooserValue(int value) {
		veloHighChooser.setValue(value);
	}

	public JSpinner getVeloLowChooser() {
		return veloLowChooser;
	}

	public JSpinner getVeloHighChooser() {
		return veloHighChooser;
	}

	public int getChoosenDevice() {
		return deviceChooser.getSelectedIndex();
	}

	public void setSoloMuteBar(SoloMute soloMute) {
		switch (soloMute) {
		case MUTE:
			soloMuteBar.setBackground(muteColor);
			soloMuteBar.setText("MUTE");
			break;
		case SOLO:
			soloMuteBar.setBackground(soloColor);
			soloMuteBar.setText("SOLO");
			break;
		case AUDIBLE:
			soloMuteBar.setBackground(backGroundColor);
			soloMuteBar.setText("");
			break;
		default:
			soloMuteBar.setBackground(backGroundColor);
			soloMuteBar.setText("");
		}
	}

	public JComboBox<Integer> getMidiChannelChooser() {
		return midiChannelChooser;
	}

	public int getNrOfSteps() {
		return (int) nrOfStepsChooser.getValue();
	}

	public String getPartnotes() {
		return (String) partNotesChooser.getValue();
	}

	public JButton getRenamePattern() {
		return renamePattern;
	}

	public JButton getRefreshButton() {
		return refreshButton;
	}
	
	public JButton getCopyButton() {
		return copyPaste[0];
	}
	
	public JButton getPasteButton() {
		return copyPaste[1];
	}
}
