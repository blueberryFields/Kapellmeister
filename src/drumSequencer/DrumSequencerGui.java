package drumSequencer;

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
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import note.NoteOn;
import pattern.DrumPattern;
import pattern.PatternBase;
import pattern.StandardPattern;
import sequencerBase.SequencerGuiBase;
import sequencerBase.SubSequencerGui;

public class DrumSequencerGui extends SequencerGuiBase implements SubSequencerGui {

	private JPanel stepPanel = new JPanel();
	private JPanel stepStrips[] = new JPanel[8];
	private JPanel[][] singleSteps = new JPanel[8][16];
	private JPanel[] instrumentHeaders = new JPanel[8];
	private JLabel[] instrumentTitles = new JLabel[8];
	private JSpinner[] noteChooser = new JSpinner[8];
	private SpinnerListModel[] noteModel = new SpinnerListModel[8];
	private JButton[] muteButtons = new JButton[8];
	private JButton[] soloButtons = new JButton[8];
	private JButton[][] noteOnButtons = new JButton[8][16];
	private SpinnerModel[][] velocityModel = new SpinnerNumberModel[8][16];
	private JSpinner velocityChooser[][] = new JSpinner[8][16];

	private Dimension buttonDimMiddle = new Dimension(65, 25);
	private Dimension generatorAlgorithmChooserDimension = new Dimension(175, 25);

	private Color instrHeaderColor = new Color(117, 134, 167);
	private Color noteOnColor = new Color(62, 105, 191);

	public DrumSequencerGui(Info[] infos, String title) {
		super(infos, title);

		// Add stuff to generator algorithmPanel
		genAlgorithmStrings = new String[] { "Rnd 4 on the floor" };
		generatorAlgorithmChooser = new JComboBox<>(genAlgorithmStrings);
		generatorAlgorithmChooser.setPreferredSize(generatorAlgorithmChooserDimension);
		generatorAlgorithmPanel.add(generatorAlgorithmText);
		generatorAlgorithmPanel.add(generatorAlgorithmChooser);

		// Create, configure and add stuff to instrumentheaders
		for (int i = 0; i < 8; i++) {
			instrumentHeaders[i] = new JPanel();
			instrumentHeaders[i].setLayout(new GridBagLayout());
			instrumentHeaders[i].setBackground(instrHeaderColor);
			instrumentTitles[i] = new JLabel("Instr" + " " + (i + 1));
			instrumentTitles[i].setHorizontalAlignment(SwingConstants.RIGHT);

			noteModel[i] = new SpinnerListModel(notes);
			noteChooser[i] = new JSpinner(noteModel[i]);
			noteChooser[i].setEditor(new JSpinner.DefaultEditor(noteChooser[i]));
			noteChooser[i].setPreferredSize(noteChooserDim);

			muteButtons[i] = new JButton("MUTE");
			muteButtons[i].setPreferredSize(buttonDimMiddle);
			soloButtons[i] = new JButton("SOLO");
			soloButtons[i].setPreferredSize(buttonDimMiddle);

			GridBagConstraints instrHeadGbc = new GridBagConstraints();
			instrHeadGbc.insets = new Insets(2, 0, 2, 0);
			instrHeadGbc.gridx = 0;
			instrHeadGbc.gridy = 0;
			// instrHeadGbc.gridwidth = 2;
			instrumentHeaders[i].add(instrumentTitles[i], instrHeadGbc);
			instrHeadGbc.gridx = 1;
			instrHeadGbc.gridy = 0;
			instrumentHeaders[i].add(noteChooser[i], instrHeadGbc);
			// instrHeadGbc.gridwidth = 1;
			instrHeadGbc.gridx = 0;
			instrHeadGbc.gridy = 1;
			instrumentHeaders[i].add(muteButtons[i], instrHeadGbc);
			instrHeadGbc.gridx = 1;
			instrumentHeaders[i].add(soloButtons[i], instrHeadGbc);
		}

		// Create and configure noteOnButtons
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 16; j++) {
				noteOnButtons[i][j] = new JButton("OFF");
				noteOnButtons[i][j].setPreferredSize(buttonDimSmall);
			}
		}

		// Create and configure velocity choosers
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 16; j++) {
				velocityModel[i][j] = new SpinnerNumberModel(100, 0, 127, 1);
				velocityChooser[i][j] = new JSpinner(velocityModel[i][j]);
				velocityChooser[i][j].setEditor(new JSpinner.DefaultEditor(velocityChooser[i][j]));
				velocityChooser[i][j].setPreferredSize(veloChooserDim);
			}
		}

		// Create and configure singleSteps
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 16; j++) {
				singleSteps[i][j] = new JPanel();
				singleSteps[i][j].setLayout(new GridBagLayout());
				singleSteps[i][j].setBackground(disabledStepColor);
				GridBagConstraints singleStepsGbc = new GridBagConstraints();
				singleStepsGbc.gridx = 0;
				singleStepsGbc.gridy = 0;
				singleSteps[i][j].add(noteOnButtons[i][j], singleStepsGbc);
				singleStepsGbc.gridx = 0;
				singleStepsGbc.gridy = 1;
				singleSteps[i][j].add(velocityChooser[i][j], singleStepsGbc);
			}
		}

		// Create, configure and add stuff to stepStrips
		for (int i = 0; i < stepStrips.length; i++) {
			stepStrips[i] = new JPanel();
			stepStrips[i].setBackground(backGroundColor);
			stepStrips[i].setLayout(new GridBagLayout());
			GridBagConstraints stepStripGbc = new GridBagConstraints();
			stepStripGbc.insets = new Insets(5, 5, 5, 5);
			stepStripGbc.gridx = 0;
			stepStrips[i].add(instrumentHeaders[i], stepStripGbc);
			for (int j = 0; j < 16; j++) {
				stepStripGbc.gridx++;
				stepStrips[i].add(singleSteps[i][j], stepStripGbc);
			}
		}

		// Add components to stepPanel
		stepPanel.setLayout(new GridBagLayout());
		GridBagConstraints stepPanelGbc = new GridBagConstraints();
		stepPanelGbc.gridx = 0;
		stepPanelGbc.gridy = 0;
		for (int i = 0; i < 8; i++) {
			stepPanel.add(stepStrips[i], stepPanelGbc);
			stepPanelGbc.gridy++;
		}

		// Configure and add stuff to PatternSettingsPanel
		partNotesChooser.setValue("1/16");

		copyPaste[0] = new JButton("Copy");
		copyPaste[1] = new JButton("Paste");
		for (int i = 0; i < copyPaste.length; i++) {
			copyPaste[i].setPreferredSize(buttonDimLarge);
			patternSettingsPanel.add(copyPaste[i]);
		}

		// configure and add stuff to frame
		frame.setLayout(new GridBagLayout());
		GridBagConstraints frameGbc = new GridBagConstraints();
		frameGbc.insets = new Insets(5, 5, 5, 5);
		frameGbc.gridx = 0;
		frameGbc.gridy = 0;
		frame.add(channelPanel, frameGbc);

		frameGbc.gridx = 1;
		frame.add(generatePanel, frameGbc);

		frameGbc.gridx = 2;
		frame.add(rndVeloPanel, frameGbc);

		frameGbc.gridx = 3;
		frame.add(generatorAlgorithmPanel, frameGbc);

		frameGbc.gridwidth = 4;
		frameGbc.gridx = 0;
		frameGbc.gridy = 2;
		frame.add(stepPanel, frameGbc);

		frameGbc.gridwidth = 2;
		frameGbc.gridx = 0;
		frameGbc.gridy = 3;
		frame.add(patternPanel, frameGbc);

		frameGbc.gridx = 2;
		frameGbc.gridy = 3;
		frame.add(patternSettingsPanel, frameGbc);

		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void repaintSequencer(PatternBase pattern) {
		// Configure headers i.e. set value for noteChoosers
		for (int i = 0; i < 8; i++) {
			noteChooser[i].setValue(((DrumPattern) pattern).getSingleStep(i, 0).getNote());
		}

		// Enable steps wich is included in sequence
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < ((DrumPattern) pattern).getPattern()[1].length; j++) {
				velocityChooser[i][j].setEnabled(true);
				noteOnButtons[i][j].setEnabled(true);
				singleSteps[i][j].setBackground(enabledStepColor);
			}
		}

		// set NoteOn and velocity on steps
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < ((DrumPattern) pattern).getPattern()[1].length; j++) {
				velocityChooser[i][j].setValue(((DrumPattern) pattern).getSingleStep(i, j).getVelo());
				switch (((DrumPattern) pattern).getSingleStep(i, j).getNoteOn()) {
				case ON:
					noteOnButtons[i][j].setText("On");
					singleSteps[i][j].setBackground(noteOnColor);
					break;
				case OFF:
					noteOnButtons[i][j].setText("Off");
					singleSteps[i][j].setBackground(enabledStepColor);
					break;
				default:
					break;
				}
			}
		}

		// Disable steps wich is not included in sequence
		for (int i = 0; i < 8; i++) {
			for (int j = ((DrumPattern) pattern).getPattern()[1].length; j < 16; j++) {
				velocityChooser[i][j].setEnabled(false);
				noteOnButtons[i][j].setEnabled(false);
				noteOnButtons[i][j].setText("Off");
				singleSteps[i][j].setBackground(disabledStepColor);
			}
		}

	}

	@Override
	public void markActiveStep(int currentStep, boolean isFirstNote, PatternBase pattern) {
		for (int i = 0; i < 8; i++) {
			singleSteps[i][currentStep].setBackground(activeStepColor);
			disableStep(i, currentStep);
		}
	}

	@Override
	public void unmarkActiveStep(int currentStep, boolean isFirstNote, PatternBase pattern) {
		for (int i = 0; i < 8; i++) {
			if (((DrumPattern) pattern).getSingleStep(i, currentStep).getNoteOn() == NoteOn.OFF) {
				singleSteps[i][currentStep].setBackground(enabledStepColor);
			} else {
				singleSteps[i][currentStep].setBackground(noteOnColor);
			}
			enableStep(i, currentStep);
		}
	}

	@Override
	public void disableGui() {
		deviceChooser.setEnabled(false);
		midiChannelChooser.setEnabled(false);
		generateButton.setEnabled(false);
		rndVeloCheckBox.setEnabled(false);
		veloLowChooser.setEnabled(false);
		veloHighChooser.setEnabled(false);
		generatorAlgorithmChooser.setEnabled(false);
		refreshButton.setEnabled(false);
		for (int i = 0; i < patternChoosers.length; i++) {
			patternChoosers[i].setEnabled(false);
		}
		partNotesChooser.setEnabled(false);
		nrOfStepsChooser.setEnabled(false);
		copyPaste[0].setEnabled(false);
		copyPaste[1].setEnabled(false);
		for (int i = 0; i < noteChooser.length; i++) {
			noteChooser[i].setEnabled(false);
		}
	}

	@Override
	public void enableGui() {
		deviceChooser.setEnabled(true);
		midiChannelChooser.setEnabled(true);
		generateButton.setEnabled(true);
		rndVeloCheckBox.setEnabled(true);
		veloLowChooser.setEnabled(true);
		veloHighChooser.setEnabled(true);
		generatorAlgorithmChooser.setEnabled(true);
		refreshButton.setEnabled(false);
		for (int i = 0; i < patternChoosers.length; i++) {
			patternChoosers[i].setEnabled(true);
		}
		partNotesChooser.setEnabled(true);
		nrOfStepsChooser.setEnabled(true);
		copyPaste[0].setEnabled(true);
		copyPaste[1].setEnabled(true);
		for (int i = 0; i < noteChooser.length; i++) {
			noteChooser[i].setEnabled(true);
		}
	}

	public void disableStep(int indexRow, int indexCol) {
		velocityChooser[indexRow][indexCol].setEnabled(false);
		noteOnButtons[indexRow][indexCol].setEnabled(false);

	}

	public void enableStep(int indexRow, int indexCol) {
		velocityChooser[indexRow][indexCol].setEnabled(true);
		noteOnButtons[indexRow][indexCol].setEnabled(true);

	}

	// The rest is simple getters and setters

	public JSpinner[] getNoteChooserArray() {
		return noteChooser;
	}

	public JSpinner getNoteChooser(int i) {
		return noteChooser[i];
	}

	public JButton[][] getNoteOnButtons() {
		return noteOnButtons;
	}

	public JButton getNoteOnButton(int indexRow, int indexCol) {
		return noteOnButtons[indexRow][indexCol];
	}

	public String getNoteOnButtonText(int indexRow, int indexCol) {
		return noteOnButtons[indexRow][indexCol].getText();
	}

	public void setNoteOnButton(int indexRow, int indexCol, String text) {
		noteOnButtons[indexRow][indexCol].setText(text);
		if (text.equals("On")) {
			singleSteps[indexRow][indexCol].setBackground(noteOnColor);
		}
		if (text.equals("Off")) {
			singleSteps[indexRow][indexCol].setBackground(enabledStepColor);
		}
	}

}
