package Gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import sequncerMotor.Am;
import sequncerMotor.NoteGenerator;

public class PrototypeGui extends JFrame {

	private NoteGenerator key;

	// Create components for steppanel
	private JPanel stepPanel = new JPanel();
	private JPanel singleSteps[] = new JPanel[16];
	private SpinnerModel octaveModel[] = new SpinnerNumberModel[16];
	private JSpinner octaveChooser[] = new JSpinner[16];
	private String[] notes = new String[] { "C ", "C#", "D ", "D#", "E ", "F ", "F#", "G ", "G#", "A ", "Bb", "B " };
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
	
	//Create components for tempopanel	
	private JPanel tempoPanel = new JPanel();
	private String[] partNotes = new String[] {"1 bar", "1/2", "1/4", "1/8", "1/16"};
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
	private String[] keyArr = new String[] { "Am" };
	private SpinnerModel keyChooserModel = new SpinnerListModel(keyArr);
	private JSpinner keyChooser = new JSpinner(keyChooserModel);

	public PrototypeGui(Info[] infos) {
		super("KapellMeister");
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
		
		
		//Add stuff to tempoPanel
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
		keyChooser.setPreferredSize(new Dimension(48, 25));
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
			singleSteps[i].setBackground(Color.WHITE);
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

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	public NoteGenerator getKey() {
		if (keyChooser.equals("Am")) {
			return key = new Am();
		} else
			return key = new Am();
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
		} catch (Exception e){
			JOptionPane.showMessageDialog(this, bpmChooser.getText() + " is not an accepteble Bpm-value! /n Initializing Bpm to 120");
		}	
		return bpm;
	}
	
	public String getPartnotes() {
		return (String) partNotesChooser.getValue();
	}
}
