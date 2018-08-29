package drumSequencer;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import pattern.StandardPattern;
import sequencerBase.SequencerGuiBase;
import sequencerBase.SubSequencerGui;

public class DrumSequencerGui extends SequencerGuiBase implements SubSequencerGui {

	private JPanel stepPanel = new JPanel();
	private JPanel[][] singleSteps = new JPanel[8][16];
	private String[] instrumentTitles = new String[8];
	private JButton[][] noteOnButtons = new JButton[8][16];
	private JLabel noteOnLabel[][] = new JLabel[8][16];
	private Color noteOnColor = Color.BLUE;
	private Color noteOffColor = Color.GRAY;
	private SpinnerModel[][] velocityModel = new SpinnerNumberModel[8][16];
	private JSpinner velocityChooser[][] = new JSpinner[8][16];

	public DrumSequencerGui(Info[] infos, String title) {
		super(infos, title);

		// Create and configure noteOnButtons
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 16; i++) {
				noteOnButtons[i][j] = new JButton();
				noteOnLabel[i][j] = new JLabel();
				noteOnLabel[i][j].setBackground(noteOffColor);
				noteOnButtons[i][j].add(noteOnLabel[i][j]);
			}
		}

		// Create and configure velocity choosers
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 16; i++) {
				velocityModel[i][j] = new SpinnerNumberModel(100, 0, 127, 1);
				velocityChooser[i][j] = new JSpinner(velocityModel[i][j]);
				velocityChooser[i][j].setEditor(new JSpinner.DefaultEditor(velocityChooser[i][j]));
				velocityChooser[i][j].setPreferredSize(veloChooserDim);
			}
		}

		// Create and configure singleSteps
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 16; i++) {
				singleSteps[i][j] = new JPanel();
				singleSteps[i][j].setLayout(new GridBagLayout());
				singleSteps[i][j].setBackground(enabledStepColor);
				GridBagConstraints singleStepsGbc = new GridBagConstraints();
				singleStepsGbc.gridx = 0;
				singleStepsGbc.gridy = 0;
				singleSteps[i][j].add(noteOnButtons[i][j]);
				singleStepsGbc.gridx = 0;
				singleStepsGbc.gridy = 1;
				singleSteps[i][j].add(noteOnButtons[i][j]);
			}
		}

		// Add components to stepPanel
		stepPanel.setLayout(new GridBagLayout());
		GridBagConstraints stepPanelGbc = new GridBagConstraints();
		stepPanelGbc.gridx = 0;
		stepPanelGbc.gridy = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 16; i++) {
				stepPanel.add(singleSteps[i][j]);
				stepPanelGbc.gridx++;
			}
			stepPanelGbc.gridx = 0;
			stepPanelGbc.gridy++;
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
		frame.add(stepPanel, frameGbc);
		
		frameGbc.gridy = 5;
		frame.add(patternPanel, frameGbc);
		
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void repaintSequencer(StandardPattern pattern) {
		// TODO Auto-generated method stub

	}

	@Override
	public void markActiveStep(int currentStep, boolean isFirstNote, StandardPattern pattern) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unmarkActiveStep(int currentStep, boolean isFirstNote, StandardPattern pattern) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableGui() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enableGui() {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableStep(int stepIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enableStep(int stepIndex) {
		// TODO Auto-generated method stub

	}

}
