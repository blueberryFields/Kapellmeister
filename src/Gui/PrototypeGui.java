package Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import note.A;
import note.Am;
import note.Bbm;
import note.C;
import note.Note;
import note.NoteGenerator;

public class PrototypeGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5048555444576060385L;

	private NoteGenerator key;

	// Create colorscheme
	private Color backGroundColor = new Color(142, 175, 206);
	private Color disabledStepColor = new Color(76, 94, 112);
	private Color enabledStepColor = new Color(193, 218, 242);
	private Color activeStepColor = Color.RED;
	private Color muteColor = Color.BLUE;
	private Color soloColor = new Color(188, 167, 30);
	// private Color buttonColor = Color.LIGHT_GRAY;
	private Color textColor = Color.BLACK;
	// private Color stepPanelColor = new Color(77, 108, 137);

	// Create dimensions
	Dimension buttonDimSmall = new Dimension(55, 25);
	Dimension veloChooserDim = new Dimension(50, 25);
	Dimension noteChooserDim = new Dimension(50, 25);

	// Create components for steppanel
	private JPanel stepPanel = new JPanel();
	private JPanel singleSteps[] = new JPanel[16];
	private String[] notes = new String[] { "C-1", "C#-1", "D-1", "D#-1", "E-1", "F#-1", "G-1", "G#-1", "A-1", "A#-1",
			"B-1", "C0", "C#0", "D0", "D#0", "E0", "F0", "F#0", "G0", "G#0", "A0", "A#0", "B0", "C1", "C#1", "D1",
			"D#1", "E1", "F1", "F#1", "G1", "G#1", "A1", "A#1", "B1", "C2", "C#2", "D2", "D#2", "E2", "F2", "F#2", "G2",
			"G#2", "A2", "A#2", "B2", "C3", "C#3", "D3", "D#3", "E3", "F3", "G3", "G#3", "A3", "A#3", "B3", "C4", "C#4",
			"D4", "D#4", "E4", "F4", "F#4", "G4", "G#4", "A4", "A#4", "B4", "C5", "C#5", "D5", "D#5", "E5", "F5", "F#5",
			"G5", "G#5", "A5", "A#5", "B5", "C6", "C#6", "D6", "D#6", "E6", "F6", "F#6", "G6", "G#6", "A6", "A#6", "B6",
			"C7", "C#7", "D7", "D#7", "E7", "F7", "F#7", "G7", "G#7", "A7", "A#7", "B7", "C8", "C#8", "D8", "D#8", "E8",
			"F8", "F#8", "G8", "G#8", "A8", "A#8", "B8", "C9", "C#9", "D9", "D#9", "E9", "F9", "F#9", "G9", };
	private SpinnerListModel[] noteModel = new SpinnerListModel[16];
	private JSpinner noteChooser[] = new JSpinner[16];
	private SpinnerModel[] velocityModel = new SpinnerNumberModel[16];
	private JSpinner velocityChooser[] = new JSpinner[16];
	private JButton[] noteOnButton = new JButton[16];

	// Create components for masterpanel
	private JPanel masterPanel = new JPanel();
	private JButton[] playStopButtons = new JButton[] { new JButton("Play"), new JButton("Stop") };
	private String[] availibleDevices;
	private JComboBox<String> deviceChooser;
	private JLabel channelText = new JLabel("ch:");
	private Integer[] midiChannels = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
	private JComboBox<Integer> midiChannelChooser = new JComboBox<Integer>(midiChannels);

	// Create components for tempopanel
	private JPanel tempoPanel = new JPanel();
	private String[] partNotes = new String[] { "1 bar", "1/2", "1/4", "1/8", "1/16" };
	private SpinnerModel partNotesModel = new SpinnerListModel(partNotes);
	private JSpinner partNotesChooser = new JSpinner(partNotesModel);
	private JLabel bpmText = new JLabel("Bpm:");
	private JLabel partNotesText = new JLabel("Partnotes:");
	private JTextField bpmChooser = new JTextField("120", 2);
	private JButton mute = new JButton("Mute");
	private JButton solo = new JButton("Solo");

	// Create components for generatorpanel
	private JPanel generatorPanel = new JPanel();

	private JPanel generatePanel = new JPanel();
	private JButton generateButton = new JButton("Generate");
	private JPanel nrOfStepsPanel = new JPanel();
	private JLabel nrOfStepsText = new JLabel("Nr of Steps:");
	private SpinnerModel nrOfStepsModel = new SpinnerNumberModel(8, 1, 16, 1);
	private JSpinner nrOfStepsChooser = new JSpinner(nrOfStepsModel);
	private JPanel keyPanel = new JPanel();
	private JLabel keyText = new JLabel("Key:");
	private String[] keyArr = new String[] { "Am", "C" };
	private SpinnerModel keyChooserModel = new SpinnerListModel(keyArr);
	private JSpinner keyChooser = new JSpinner(keyChooserModel);

	private JPanel octaveRangePanel = new JPanel();
	private JLabel octaveRangeFromText = new JLabel("Octave-range, from:");
	private JLabel octaveRangeToText = new JLabel("to:");
	private SpinnerModel octaveLowModel = new SpinnerNumberModel(3, -1, 9, 1);
	private SpinnerModel octaveHighModel = new SpinnerNumberModel(3, -1, 9, 1);
	private JSpinner octaveLowChooser = new JSpinner(octaveLowModel);
	private JSpinner octaveHighChooser = new JSpinner(octaveHighModel);

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
	private String[] genAlgorithmStrings = { "Rnd notes", "Rnd notes, no dupl in row", "Rnd notes and On/Hold/Off",
			"Rnd notes, no dupl in row, On/Hold/Off" };
	private JComboBox<String> generatorAlgorithmChooser = new JComboBox<>(genAlgorithmStrings);

	// Create components for nudgePanel
	private JPanel nudgePanel = new JPanel();
	private JButton nudgeLeft = new JButton("<-");
	private JButton nudgeRight = new JButton("->");
	private JLabel nudgeText = new JLabel("Nudge Sequence");
	// Create stuff for JSLider
	private JPanel sliderPanel = new JPanel();
	private JLabel sliderText = new JLabel("    Gui delay:");
	private final int minDelay = 0;
	private final int maxDelay = 100;
	private final int initDelay = 0;
	private JSlider guiDelaySlider = new JSlider(JSlider.HORIZONTAL, minDelay, maxDelay, initDelay);

	// Konstruktor
	public PrototypeGui(Info[] infos) {
		super("KapellMeister");

		// Code for setting the Ui to Crossplatformlook and feel, looks like shiiiet
		// though:
		//
		// try {
		// UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// Set colors for panels
		rndVeloCheckPanel.setBackground(backGroundColor);
		rndVeloPanel.setBackground(backGroundColor);
		veloLowPanel.setBackground(backGroundColor);
		veloHighPanel.setBackground(backGroundColor);
		generatorPanel.setBackground(backGroundColor);
		generatePanel.setBackground(backGroundColor);
		generatorAlgorithmPanel.setBackground(backGroundColor);
		nudgePanel.setBackground(backGroundColor);
		keyPanel.setBackground(backGroundColor);
		nrOfStepsPanel.setBackground(backGroundColor);
		masterPanel.setBackground(backGroundColor);
		tempoPanel.setBackground(backGroundColor);
		stepPanel.setBackground(backGroundColor);
		octaveRangePanel.setBackground(backGroundColor);

		mute.setForeground(textColor);
		solo.setForeground(textColor);

		// Set BackgroundColor for frame
		getContentPane().setBackground(backGroundColor);

		// Add stuff to masterPanel

		for (int i = 0; i < playStopButtons.length; i++) {
			masterPanel.add(playStopButtons[i]);
		}
		playStopButtons[0].setEnabled(false);
		availibleDevices = new String[infos.length + 1];
		availibleDevices[0] = "Choose a device...";
		for (int i = 1; i < availibleDevices.length; i++) {
			availibleDevices[i] = infos[i - 1].toString();
		}
		masterPanel.add(deviceChooser = new JComboBox<String>(availibleDevices));
		deviceChooser.setPreferredSize(new Dimension(175, 25));
		midiChannelChooser.setPreferredSize(new Dimension(70, 25));
		//masterPanel.add(channelText);
		masterPanel.add(midiChannelChooser);

		// Add stuff to tempoPanel
		tempoPanel.add(bpmText);
		tempoPanel.add(bpmChooser);
		partNotesChooser.setEditor(new JSpinner.DefaultEditor(partNotesChooser));
		partNotesChooser.setValue("1/8");
		partNotesChooser.setPreferredSize(new Dimension(60, 25));
		tempoPanel.add(partNotesText);
		tempoPanel.add(partNotesChooser);
		mute.setPreferredSize(buttonDimSmall);
		// mute.setOpaque(true);
		tempoPanel.add(mute);
		solo.setPreferredSize(buttonDimSmall);
		// solo.setOpaque(true);
		tempoPanel.add(solo);

		// Add stuff to generatorpanel
		generatorPanel.setLayout(new GridBagLayout());
		GridBagConstraints generatorPanelGbc = new GridBagConstraints();

		nrOfStepsChooser.setEditor(new JSpinner.DefaultEditor(nrOfStepsChooser));
		nrOfStepsChooser.setPreferredSize(new Dimension(43, 25));
		keyChooser.setEditor(new JSpinner.DefaultEditor(keyChooser));
		keyChooser.setPreferredSize(new Dimension(55, 25));

		// generatorPanelGbc.insets = new Insets(0, 0, 0, 0);

		generatePanel.add(generateButton);
		nrOfStepsPanel.add(nrOfStepsText);
		nrOfStepsPanel.add(nrOfStepsChooser);
		generatePanel.add(nrOfStepsPanel);

		keyPanel.add(keyText);
		keyPanel.add(keyChooser);
		generatePanel.add(keyPanel);
		generatorPanelGbc.gridx = 0;
		generatorPanelGbc.gridy = 0;
		generatorPanel.add(generatePanel, generatorPanelGbc);

		octaveLowChooser.setEditor(new JSpinner.DefaultEditor(octaveLowChooser));
		octaveHighChooser.setEditor(new JSpinner.DefaultEditor(octaveHighChooser));

		octaveLowChooser.setPreferredSize(new Dimension(40, 25));
		octaveHighChooser.setPreferredSize(new Dimension(40, 25));

		octaveRangePanel.add(octaveRangeFromText);
		octaveRangePanel.add(octaveLowChooser);
		octaveRangePanel.add(octaveRangeToText);
		octaveRangePanel.add(octaveHighChooser);

		generatorPanelGbc.gridx = 0;
		generatorPanelGbc.gridy = 1;
		generatorPanel.add(octaveRangePanel, generatorPanelGbc);

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

		generatorPanelGbc.gridx = 0;
		generatorPanelGbc.gridy = 2;
		generatorPanel.add(rndVeloPanel, generatorPanelGbc);

		generatorAlgorithmPanel.add(generatorAlgorithmText);
		generatorAlgorithmPanel.add(generatorAlgorithmChooser);
		generatorPanelGbc.gridx = 0;
		generatorPanelGbc.gridy = 3;
		generatorPanel.add(generatorAlgorithmPanel, generatorPanelGbc);

		// Add stuff to and configure nudgeSequencePanel
		guiDelaySlider.setMajorTickSpacing(10);
		guiDelaySlider.setMinorTickSpacing(5);
		guiDelaySlider.setPaintTicks(true);
		guiDelaySlider.setPaintLabels(true);
		sliderPanel.setLayout(new BorderLayout());
		sliderPanel.add(sliderText, BorderLayout.NORTH);
		sliderPanel.add(guiDelaySlider, BorderLayout.SOUTH);
		sliderPanel.setOpaque(false);

		nudgeLeft.setPreferredSize(buttonDimSmall);
		nudgeRight.setPreferredSize(buttonDimSmall);
		nudgePanel.add(sliderPanel);
		nudgePanel.add(nudgeLeft);
		nudgePanel.add(nudgeText);
		nudgePanel.add(nudgeRight);

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
		setLayout(new GridBagLayout());
		GridBagConstraints frameGbc = new GridBagConstraints();
		frameGbc.insets = new Insets(5, 5, 5, 5);
		frameGbc.gridx = 0;
		frameGbc.gridy = 0;
		add(masterPanel, frameGbc);

		frameGbc.gridy = 1;
		add(tempoPanel, frameGbc);

		frameGbc.gridy = 2;
		add(generatorPanel, frameGbc);

		frameGbc.gridy = 3;
		add(nudgePanel, frameGbc);

		frameGbc.gridy = 4;
		add(stepPanel, frameGbc);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	public void repaintSequencer(Note[] sequence) {

		// Enable steps wich is included in sequence
		for (int i = 0; i < sequence.length; i++) {
			noteChooser[i].setEnabled(true);
			velocityChooser[i].setEnabled(true);
			noteOnButton[i].setEnabled(true);
			singleSteps[i].setBackground(enabledStepColor);
		}

		// set note and velocity on steps
		for (int i = 0; i < sequence.length; i++) {
			velocityChooser[i].setValue(sequence[i].getVelo());
			noteChooser[i].setValue(sequence[i].getNote());
			switch (sequence[i].getNoteOn()) {
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
		for (int i = sequence.length; i < 16; i++) {
			noteChooser[i].setEnabled(false);
			velocityChooser[i].setEnabled(false);
			noteOnButton[i].setEnabled(false);
			noteOnButton[i].setText("Off");
			singleSteps[i].setBackground(disabledStepColor);
		}

	}

	public NoteGenerator getKey() {
		switch ((String) keyChooser.getValue()) {
		case "Am":
			return key = new Am();
		case "A":
			return key = new A();
		case "Bbm":
			return key = new Bbm();
		case "C":
			return key = new C();
		}
		return key = null;
	}

	public int getChoosenDevice() {
		return deviceChooser.getSelectedIndex();
	}

	public JButton getPlayButton() {
		return playStopButtons[0];
	}

	public JButton getStopButton() {
		return playStopButtons[1];
	}

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

	public JSpinner getNrOfStepsChooser() {
		return nrOfStepsChooser;
	}

	public JComboBox getDeviceChooser() {
		return deviceChooser;
	}

	public JButton getGenerateButton() {
		return generateButton;
	}

	public int getNrOfSteps() {
		return (int) nrOfStepsChooser.getValue();
	}

	public int getBpm() {
		int bpm = 120;
		try {
			bpm = Integer.parseInt(bpmChooser.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					bpmChooser.getText() + " is not an accepteble Bpm-value! /n Initializing Bpm to 120");
		}
		return bpm;
	}

	public String getPartnotes() {
		return (String) partNotesChooser.getValue();
	}

	public void disableGui() {
		playStopButtons[0].setEnabled(false);
		deviceChooser.setEnabled(false);
		bpmText.setEnabled(false);
		bpmChooser.setEnabled(false);
		partNotesChooser.setEnabled(false);
		generateButton.setEnabled(false);
		keyText.setEnabled(false);
		keyChooser.setEnabled(false);
	}

	public void enableGui() {
		playStopButtons[0].setEnabled(true);
		deviceChooser.setEnabled(true);
		bpmText.setEnabled(true);
		bpmChooser.setEnabled(true);
		partNotesChooser.setEnabled(true);
		generateButton.setEnabled(true);
		keyText.setEnabled(true);
		keyChooser.setEnabled(true);
	}

	public String[] getAvailibleDevices() {
		return availibleDevices;
	}

	public void setAvailibleDevices(String[] availibleDevices) {
		this.availibleDevices = availibleDevices;
	}

	public void markActiveStep(int currentStep, boolean isFirstNote, Note[] sequence) {
		if (isFirstNote) {
			singleSteps[currentStep - 1].setBackground(activeStepColor);
		} else if (currentStep == 0 && !isFirstNote) {
			singleSteps[currentStep].setBackground(activeStepColor);
			singleSteps[sequence.length - 1].setBackground(enabledStepColor);
		} else {
			singleSteps[currentStep].setBackground(activeStepColor);
			singleSteps[currentStep - 1].setBackground(enabledStepColor);
		}
	}

	public void unmarkActiveStep(int currentStep, boolean isFirstNote, Note[] sequence) {
		if (isFirstNote) {
			singleSteps[currentStep - 1].setBackground(enabledStepColor);
		} else if (currentStep == 0 && !isFirstNote) {
			singleSteps[sequence.length - 1].setBackground(enabledStepColor);
		} else {
			singleSteps[currentStep - 1].setBackground(enabledStepColor);
		}
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

	public JButton getNudgeLeft() {
		return nudgeLeft;
	}

	public JButton getNudgeRight() {
		return nudgeRight;
	}

	public JButton getMute() {
		return mute;
	}

	public JButton getSolo() {
		return solo;
	}

	public void setMuteColor() {
		if (mute.getForeground() == textColor) {
			mute.setForeground(muteColor);
		} else {
			mute.setForeground(textColor);
		}
	}

	public void setSoloColor() {
		if (solo.getForeground() == textColor) {
			solo.setForeground(soloColor);
		} else {
			solo.setForeground(textColor);
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
	
	public JSlider getGuiDelaySLider() {
		return guiDelaySlider;
	}
}
