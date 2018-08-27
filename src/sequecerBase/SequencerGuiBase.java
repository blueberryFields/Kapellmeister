package sequecerBase;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
import note.Note;
import sequecerBase.SoloMute;

public class SequencerGuiBase {

	/**
	 * The graphic user interface for the standard sequencers.
	 */

	// Create colorscheme
	private Color backGroundColor = new Color(142, 175, 206);
	private Color disabledStepColor = new Color(76, 94, 112);
	private Color enabledStepColor = new Color(193, 218, 242);
	private Color activeStepColor = Color.RED;
	private Color muteColor = Color.BLUE;
	private Color soloColor = Color.YELLOW;
	private Color enabledText = Color.BLACK;
	private Color disabledText = Color.GRAY;

	// Create dimensions
	private Dimension buttonDimSmall = new Dimension(55, 25);
	private Dimension buttonDimLarge = new Dimension(75, 25);
	private Dimension veloChooserDim = new Dimension(50, 25);
	private Dimension noteChooserDim = new Dimension(50, 25);
	private Dimension soloMuteBarDim = new Dimension(55, 20);
	private Dimension patternChooserDim = new Dimension(110, 25);

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

	// Create components for channelpanel
	private JPanel channelPanel = new JPanel();
	private String[] availibleDevices;
	private JComboBox<String> deviceChooser;
	private DefaultComboBoxModel<String> deviceChooserModel;
	private JLabel channelText = new JLabel("ch:");
	private Integer[] midiChannels = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
	private JComboBox<Integer> midiChannelChooser = new JComboBox<Integer>(midiChannels);
	private JButton refreshButton = new JButton("Refresh");
	private JLabel soloMuteBar = new JLabel();

	// Create components for generatorpanel
	private JPanel generatorPanel = new JPanel();
	private JPanel generatePanel = new JPanel();
	private JButton generateButton = new JButton("Generate");
	private JPanel octaveRangePanel = new JPanel();
	private JLabel octaveRangeFromText = new JLabel("Octave-range, from:");
	private JLabel octaveRangeToText = new JLabel("to:");
	private SpinnerModel octaveLowModel = new SpinnerNumberModel(3, -1, 9, 1);
	private SpinnerModel octaveHighModel = new SpinnerNumberModel(3, -1, 9, 1);
	private JSpinner octaveLowChooser = new JSpinner(octaveLowModel);
	private JSpinner octaveHighChooser = new JSpinner(octaveHighModel);

	// Create components for the random velocity panel
	private JPanel rndVeloPanel = new JPanel();
	private JPanel rndVeloCheckPanel = new JPanel();
	private JPanel veloLowPanel = new JPanel();
	private JPanel veloHighPanel = new JPanel();
	private JLabel rndVeloText = new JLabel("Random velocity:");
	private JCheckBox rndVeloCheckBox = new JCheckBox();
	private JLabel fromText = new JLabel("from:");
	private JLabel toText = new JLabel("to:");
	private SpinnerModel veloLowModel = new SpinnerNumberModel(70, 0, 127, 1);
	private JSpinner veloLowChooser = new JSpinner(veloLowModel);
	private SpinnerModel veloHighModel = new SpinnerNumberModel(110, 0, 127, 1);
	private JSpinner veloHighChooser = new JSpinner(veloHighModel);

	private JPanel generatorAlgorithmPanel = new JPanel();
	private JLabel generatorAlgorithmText = new JLabel("Gen Algorithm:");
	private String[] genAlgorithmStrings;
	private JComboBox<String> generatorAlgorithmChooser = new JComboBox<>(genAlgorithmStrings);

	// Create components for nudgePanel
	private JPanel nudgePanel = new JPanel();
	private JButton nudgeLeft = new JButton("<-");
	private JButton nudgeRight = new JButton("->");
	private JLabel nudgeText = new JLabel("Nudge Sequence");
	private JButton[] copyPaste = new JButton[2];

	// Create components for patternsPanel
	private JPanel patternPanel = new JPanel();
	private JButton[] patternChoosers = new JButton[8];
	private JPanel patternSettingsPanel = new JPanel();
	private JLabel nrOfStepsText = new JLabel("Nr of steps:");
	private SpinnerModel nrOfStepsModel = new SpinnerNumberModel(8, 1, 16, 1);
	private JSpinner nrOfStepsChooser = new JSpinner(nrOfStepsModel);
	private String[] partNotes = new String[] { "1 bar", "1/2", "1/4", "1/8", "1/16" };
	private SpinnerModel partNotesModel = new SpinnerListModel(partNotes);
	private JSpinner partNotesChooser = new JSpinner(partNotesModel);
	private JLabel partNotesText = new JLabel("Partnotes:");
	private JButton renamePattern = new JButton("Rename");

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
		super(title);

		// Set colors for panels n the like
		rndVeloCheckPanel.setBackground(backGroundColor);
		rndVeloPanel.setBackground(backGroundColor);
		veloLowPanel.setBackground(backGroundColor);
		veloHighPanel.setBackground(backGroundColor);
		generatorPanel.setBackground(backGroundColor);
		generatePanel.setBackground(backGroundColor);
		generatorAlgorithmPanel.setBackground(backGroundColor);
		nudgePanel.setBackground(backGroundColor);
		channelPanel.setBackground(backGroundColor);
		stepPanel.setBackground(backGroundColor);
		octaveRangePanel.setBackground(backGroundColor);
		soloMuteBar.setBackground(backGroundColor);

		// Set BackgroundColor for frame
		getContentPane().setBackground(backGroundColor);

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

		octaveLowChooser.setEditor(new JSpinner.DefaultEditor(octaveLowChooser));
		octaveHighChooser.setEditor(new JSpinner.DefaultEditor(octaveHighChooser));

		octaveLowChooser.setPreferredSize(new Dimension(40, 25));
		octaveHighChooser.setPreferredSize(new Dimension(40, 25));

		octaveRangePanel.add(octaveRangeFromText);
		octaveRangePanel.add(octaveLowChooser);
		octaveRangePanel.add(octaveRangeToText);
		octaveRangePanel.add(octaveHighChooser);

		generatePanel.add(octaveRangePanel);

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

		generatorAlgorithmPanel.add(generatorAlgorithmText);
		generatorAlgorithmPanel.add(generatorAlgorithmChooser);

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
		k = 0;
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

		// configure and add stuff to frame
		setLayout(new GridBagLayout());
		GridBagConstraints frameGbc = new GridBagConstraints();
		frameGbc.insets = new Insets(5, 5, 5, 5);
		frameGbc.gridx = 0;
		frameGbc.gridy = 0;
		add(channelPanel, frameGbc);

		frameGbc.gridy = 1;
		add(generatePanel, frameGbc);

		frameGbc.gridy = 2;
		add(rndVeloPanel, frameGbc);

		frameGbc.gridy = 3;
		add(generatorAlgorithmPanel, frameGbc);

		frameGbc.gridy = 4;
		add(nudgePanel, frameGbc);

		frameGbc.gridy = 5;
		add(stepPanel, frameGbc);

		frameGbc.gridy = 6;
		add(patternPanel, frameGbc);

		frameGbc.gridy = 7;
		add(patternSettingsPanel, frameGbc);

		setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		pack();
		setVisible(true);
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

	/**
	 * Sets the frame to visible if its not.
	 */
	public void open() {
		setVisible(true);
	}

	//The rest here is mostly basic getters and setters
	
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

	public JButton getNudgeLeft() {
		return nudgeLeft;
	}

	public JButton getNudgeRight() {
		return nudgeRight;
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
