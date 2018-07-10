package Gui;

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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import sequncerMotor.A;
import sequncerMotor.Am;
import sequncerMotor.Bbm;
import sequncerMotor.C;
import sequncerMotor.Note;
import sequncerMotor.NoteGenerator;

public class PrototypeGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5048555444576060385L;

	private NoteGenerator key;

	// Create colorscheme
	private Color disabledStep = Color.GRAY;
	private Color enabledStep = Color.WHITE;
	private Color activeStep = Color.RED;

	// Create components for steppanel
	private JPanel stepPanel = new JPanel();
	private JPanel singleSteps[] = new JPanel[16];
	private String[] notes = new String[] { "C3", "D3", "E3", "F3", "G3", "A3", "B3", "C4", "D4", "E4", "F4", "G4",
			"A4" };
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

	// Create components for tempopanel
	private JPanel tempoPanel = new JPanel();
	private String[] partNotes = new String[] { "1 bar", "1/2", "1/4", "1/8", "1/16" };
	private SpinnerModel partNotesModel = new SpinnerListModel(partNotes);
	private JSpinner partNotesChooser = new JSpinner(partNotesModel);
	private JLabel bpmText = new JLabel("Bpm:");
	private JTextField bpmChooser = new JTextField("120", 2);

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

	private JPanel generatorAlgorithmPanel = new JPanel();
	private JLabel generatorAlgorithmText = new JLabel("Generator Algorithm:");
	//private JCheckBox noDuplCheck = new JCheckBox();
	private String[] genAlgorithmStrings = {"Random", "Random, no duplicates"};
	private JComboBox<String> generatorAlgorithmChooser = new JComboBox<>(genAlgorithmStrings);

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

	private JPanel nudgeSequencePanel = new JPanel();
	private JButton nudgeLeft = new JButton("<-");
	private JButton nudgeRight = new JButton("->");
	private JLabel nudgeText = new JLabel("Nudge Sequence");
	
	// Konstruktor
	public PrototypeGui(Info[] infos) {
		super("KapellMeister");

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
		masterPanel.add(deviceChooser = new JComboBox(availibleDevices));
		deviceChooser.setPreferredSize(new Dimension(175, 25));

		// Add stuff to tempoPanel
		tempoPanel.add(bpmText);
		tempoPanel.add(bpmChooser);
		partNotesChooser.setEditor(new JSpinner.DefaultEditor(partNotesChooser));
		partNotesChooser.setValue("1/8");
		partNotesChooser.setPreferredSize(new Dimension(60, 25));
		tempoPanel.add(partNotesChooser);

		// Add stuff to generatorpanel
		generatorPanel.setLayout(new GridBagLayout());
		GridBagConstraints generatorPanelGbc = new GridBagConstraints();

		nrOfStepsChooser.setEditor(new JSpinner.DefaultEditor(nrOfStepsChooser));
		nrOfStepsChooser.setPreferredSize(new Dimension(43, 25));
		keyChooser.setEditor(new JSpinner.DefaultEditor(keyChooser));
		keyChooser.setPreferredSize(new Dimension(55, 25));

		generatorPanelGbc.insets = new Insets(0, 0, 0, 0);

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
		generatorPanelGbc.gridy = 1;
		generatorPanel.add(rndVeloPanel, generatorPanelGbc);

		generatorAlgorithmPanel.add(generatorAlgorithmText);
		generatorAlgorithmPanel.add(generatorAlgorithmChooser);
		generatorPanelGbc.gridx = 0;
		generatorPanelGbc.gridy = 2;
		generatorPanel.add(generatorAlgorithmPanel, generatorPanelGbc);
		
		nudgeLeft.setPreferredSize(new Dimension(50, 25));
		nudgeRight.setPreferredSize(new Dimension(50, 25));
		nudgeSequencePanel.add(nudgeLeft);
		nudgeSequencePanel.add(nudgeText);
		nudgeSequencePanel.add(nudgeRight);
		generatorPanelGbc.gridx = 0;
		generatorPanelGbc.gridy = 3;
		generatorPanel.add(nudgeSequencePanel, generatorPanelGbc);

		// Add stuff to and configure stepPanel
		for (int i = 0; i < noteChooser.length; i++) {
			noteModel[i] = new SpinnerListModel(notes);
			noteChooser[i] = new JSpinner(noteModel[i]);
			noteChooser[i].setEditor(new JSpinner.DefaultEditor(noteChooser[i]));
			noteChooser[i].setPreferredSize(new Dimension(50, 25));
		}

		for (int i = 0; i < velocityChooser.length; i++) {
			velocityModel[i] = new SpinnerNumberModel(100, 0, 127, 1);
			velocityChooser[i] = new JSpinner(velocityModel[i]);
			velocityChooser[i].setEditor(new JSpinner.DefaultEditor(velocityChooser[i]));
			velocityChooser[i].setPreferredSize(new Dimension(50, 25));
		}
		for (int i = 0; i < noteOnButton.length; i++) {
			noteOnButton[i] = new JButton("");
			noteOnButton[i].setPreferredSize(new Dimension(55, 25));
		}

		for (int i = 0; i < singleSteps.length; i++) {
			singleSteps[i] = new JPanel();
			singleSteps[i].setLayout(new GridBagLayout());
			singleSteps[i].setBackground(enabledStep);
			GridBagConstraints singleStepsGbc = new GridBagConstraints();
			singleStepsGbc.insets = new Insets(1, 1, 1, 1);
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
		frameGbc.gridx = 0;
		frameGbc.gridy = 1;
		add(tempoPanel, frameGbc);
		frameGbc.gridx = 0;
		frameGbc.gridy = 2;
		add(generatorPanel, frameGbc);
		frameGbc.gridx = 0;
		frameGbc.gridy = 3;
		add(stepPanel, frameGbc);

		// Set BackgroundColor for frame
		// getContentPane().setBackground(Color.DARK_GRAY);

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
			singleSteps[i].setBackground(enabledStep);
		}

		// set note and velocity on steps
		for (int i = 0; i < sequence.length; i++) {
			velocityChooser[i].setValue(sequence[i].getVelo());
			noteChooser[i].setValue(sequence[i].getNote());
			switch (sequence[i].getNoteOnButtonEnum()) {
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
			singleSteps[i].setBackground(disabledStep);
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
			singleSteps[currentStep - 1].setBackground(activeStep);
		} else if (currentStep == 0 && !isFirstNote) {
			singleSteps[currentStep].setBackground(activeStep);
			singleSteps[sequence.length - 1].setBackground(enabledStep);
		} else {
			singleSteps[currentStep].setBackground(activeStep);
			singleSteps[currentStep - 1].setBackground(enabledStep);
		}
	}

	public void unmarkActiveStep(int currentStep, boolean isFirstNote, Note[] sequence) {
		if (isFirstNote) {
			singleSteps[currentStep - 1].setBackground(enabledStep);
		} else if (currentStep == 0 && !isFirstNote) {
			singleSteps[sequence.length - 1].setBackground(enabledStep);
		} else {
			singleSteps[currentStep - 1].setBackground(enabledStep);
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
}

