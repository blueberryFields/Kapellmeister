package Gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.JButton;
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
import sequncerMotor.MidiNote;
import sequncerMotor.NoteGenerator;

public class PrototypeGui extends JFrame {

	private NoteGenerator key;
	// private MidiToNoteConverter midiToNote = new MidiToNoteConverter();
	private HashMap<Integer, String> noteMap = new HashMap<Integer, String>();

	// Create colorscheme
	private Color disabledStep = Color.GRAY;
	private Color enabledStep = Color.WHITE;
	private Color activeStep = Color.RED;

	// Create components for steppanel
	private JPanel stepPanel = new JPanel();
	private JPanel singleSteps[] = new JPanel[16];
	private SpinnerModel octaveModel[] = new SpinnerNumberModel[16];
	private JSpinner octaveChooser[] = new JSpinner[16];
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
	private JComboBox deviceChooser;

	// Create components for tempopanel
	private JPanel tempoPanel = new JPanel();
	private String[] partNotes = new String[] { "1 bar", "1/2", "1/4", "1/8", "1/16" };
	private SpinnerModel partNotesModel = new SpinnerListModel(partNotes);
	private JSpinner partNotesChooser = new JSpinner(partNotesModel);

	private JLabel bpm = new JLabel("Bpm:");
	private JTextField bpmChooser = new JTextField("120", 2);

	// Create components for generatorpanel
	private JPanel generatorPanel = new JPanel();
	private JButton generateButton = new JButton("Generate");
	private JLabel nrOfStepsText = new JLabel("Nr of Steps:");
	private SpinnerModel nrOfStepsModel = new SpinnerNumberModel(8, 1, 16, 1);
	private JSpinner nrOfSteps = new JSpinner(nrOfStepsModel);
	private JLabel keyText = new JLabel("Key:");
	private String[] keyArr = new String[] { "Am", "C" };
	private SpinnerModel keyChooserModel = new SpinnerListModel(keyArr);
	private JSpinner keyChooser = new JSpinner(keyChooserModel);

	// Konstruktor
	public PrototypeGui(Info[] infos) {
		super("KapellMeister");

		// fill hashmap with midimessage to notes
		// noteMap.put(48, "C3");
		// noteMap.put(49, "C#3");
		// noteMap.put(50, "D3");
		// noteMap.put(51, "D#3");
		// noteMap.put(52, "E3");
		// noteMap.put(53, "F3");
		// noteMap.put(54, "F#3");
		// noteMap.put(55, "G3");
		// noteMap.put(56, "G#3");
		// noteMap.put(57, "A3");
		// noteMap.put(58, "A#3");
		// noteMap.put(59, "B3");
		// noteMap.put(60, "C4");
		// noteMap.put(61, "C#4");
		// noteMap.put(62, "D4");
		// noteMap.put(63, "D#4");
		// noteMap.put(64, "E4");
		// noteMap.put(65, "F4");
		// noteMap.put(66, "F#4");
		// noteMap.put(67, "G4");
		// noteMap.put(68, "G#4");
		// noteMap.put(69, "A4");
		// noteMap.put(70, "A#4");
		// noteMap.put(71, "B4");
		// noteMap.put(72, "C5");

		// Add stuff to masterPanel
		for (int i = 0; i < playStopButtons.length; i++) {
			masterPanel.add(playStopButtons[i]);
		}
		availibleDevices = new String[infos.length + 1];
		availibleDevices[0] = "Choose a device...";
		for (int i = 1; i < availibleDevices.length; i++) {
			availibleDevices[i] = infos[i - 1].toString();
		}
		masterPanel.add(deviceChooser = new JComboBox(availibleDevices));
		deviceChooser.setPreferredSize(new Dimension(175, 25));

		// Add stuff to tempoPanel
		tempoPanel.add(bpm);
		tempoPanel.add(bpmChooser);
		partNotesChooser.setEditor(new JSpinner.DefaultEditor(partNotesChooser));
		partNotesChooser.setValue("1/16");
		partNotesChooser.setPreferredSize(new Dimension(60, 25));
		tempoPanel.add(partNotesChooser);

		// Add stuff to generatorpanel
		nrOfSteps.setEditor(new JSpinner.DefaultEditor(nrOfSteps));
		nrOfSteps.setPreferredSize(new Dimension(43, 25));
		keyChooser.setEditor(new JSpinner.DefaultEditor(keyChooser));
		keyChooser.setPreferredSize(new Dimension(55, 25));
		generatorPanel.add(generateButton);
		generatorPanel.add(nrOfStepsText);
		generatorPanel.add(nrOfSteps);
		generatorPanel.add(keyText);
		generatorPanel.add(keyChooser);

		// Add stuff to and configure stepPanel
		for (int i = 0; i < noteChooser.length; i++) {
			noteModel[i] = new SpinnerListModel(notes);
			noteChooser[i] = new JSpinner(noteModel[i]);
			noteChooser[i].setEditor(new JSpinner.DefaultEditor(noteChooser[i]));
			noteChooser[i].setPreferredSize(new Dimension(50, 25));
		}
		for (int i = 0; i < octaveChooser.length; i++) {
			octaveModel[i] = new SpinnerNumberModel(3, -1, 9, 1);
			octaveChooser[i] = new JSpinner(octaveModel[i]);
			octaveChooser[i].setEditor(new JSpinner.DefaultEditor(octaveChooser[i]));
			octaveChooser[i].setPreferredSize(new Dimension(40, 25));
		}
		for (int i = 0; i < velocityChooser.length; i++) {
			velocityModel[i] = new SpinnerNumberModel(100, 0, 127, 1);
			velocityChooser[i] = new JSpinner(velocityModel[i]);
			velocityChooser[i].setEditor(new JSpinner.DefaultEditor(velocityChooser[i]));
			velocityChooser[i].setPreferredSize(new Dimension(50, 25));
		}
		for (int i = 0; i < noteOnButton.length; i++) {
			noteOnButton[i] = new JButton("on");
			noteOnButton[i].setPreferredSize(new Dimension(40, 25));
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
			singleSteps[i].add(octaveChooser[i], singleStepsGbc);
			singleStepsGbc.gridx = 0;
			singleStepsGbc.gridy = 1;
			singleSteps[i].add(velocityChooser[i], singleStepsGbc);
			singleStepsGbc.gridx = 1;
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

	public void repaintSequencer(MidiNote[] sequence) {

		// Enable steps wich is included in sequence
		for (int i = 0; i < sequence.length; i++) {
			noteChooser[i].setEnabled(true);
			octaveChooser[i].setEnabled(true);
			velocityChooser[i].setEnabled(true);
			noteOnButton[i].setEnabled(false);
			singleSteps[i].setBackground(enabledStep);
		}

		// set note and velocity on steps
		for (int i = 0; i < sequence.length; i++) {
			velocityChooser[i].setValue(sequence[i].getVelo());
			noteChooser[i].setValue(sequence[i].getNote());
		}

		// Disable steps wich is not included in sequence
		for (int i = sequence.length; i < 16; i++) {
			noteChooser[i].setEnabled(false);
			octaveChooser[i].setEnabled(false);
			velocityChooser[i].setEnabled(false);
			noteOnButton[i].setEnabled(false);
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

	public JComboBox getDeviceChooser() {
		return deviceChooser;
	}

	public JButton getGenerateButton() {
		return generateButton;
	}

	public int getNrOfSteps() {
		return (int) nrOfSteps.getValue();
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
		// TODO Auto-generated method stub

	}

	public void enableGui() {
		// TODO Auto-generated method stub

	}

	public void markActiveStep(int currentStep, boolean isFirstNote, MidiNote[] sequence) {
		if (isFirstNote) {
			singleSteps[currentStep].setBackground(activeStep);
		} else if (currentStep == 0 && !isFirstNote) {
			singleSteps[currentStep].setBackground(activeStep);
			singleSteps[sequence.length - 1].setBackground(enabledStep);
		} else {
			singleSteps[currentStep].setBackground(activeStep);
			singleSteps[currentStep - 1].setBackground(enabledStep);

		}
	}

}
