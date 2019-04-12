package sequencerBase;

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
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import pattern.PatternBase;
import pattern.StandardPattern;

public class SequencerGuiBase {

	/**
	 * The base for the graphic user interface for sequencers.
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
	protected JInternalFrame frame;

	// Create notes
	protected String[] notes = new String[] { "C-1", "C#-1", "D-1", "D#-1", "E-1", "F#-1", "G-1", "G#-1", "A-1", "A#-1",
			"B-1", "C0", "C#0", "D0", "D#0", "E0", "F0", "F#0", "G0", "G#0", "A0", "A#0", "B0", "C1", "C#1", "D1",
			"D#1", "E1", "F1", "F#1", "G1", "G#1", "A1", "A#1", "B1", "C2", "C#2", "D2", "D#2", "E2", "F2", "F#2", "G2",
			"G#2", "A2", "A#2", "B2", "C3", "C#3", "D3", "D#3", "E3", "F3", "F#3", "G3", "G#3", "A3", "A#3", "B3", "C4",
			"C#4", "D4", "D#4", "E4", "F4", "F#4", "G4", "G#4", "A4", "A#4", "B4", "C5", "C#5", "D5", "D#5", "E5", "F5",
			"F#5", "G5", "G#5", "A5", "A#5", "B5", "C6", "C#6", "D6", "D#6", "E6", "F6", "F#6", "G6", "G#6", "A6",
			"A#6", "B6", "C7", "C#7", "D7", "D#7", "E7", "F7", "F#7", "G7", "G#7", "A7", "A#7", "B7", "C8", "C#8", "D8",
			"D#8", "E8", "F8", "F#8", "G8", "G#8", "A8", "A#8", "B8", "C9", "C#9", "D9", "D#9", "E9", "F9", "F#9",
			"G9", };

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
	protected JButton[] copyPaste = new JButton[2];

	// Create components for patternsPanel
	protected JPanel patternPanel = new JPanel();
	protected JButton[] patternChoosers = new JButton[8];
	protected JPanel patternSettingsPanel = new JPanel();
	protected JButton renamePattern = new JButton("Rename");

	//Create components for patternSettingsPanel
		protected JLabel nrOfStepsText = new JLabel("Nr of steps:");
		protected SpinnerModel nrOfStepsModel = new SpinnerNumberModel(8, 1, 16, 1);
		protected JSpinner nrOfStepsChooser = new JSpinner(nrOfStepsModel);
		protected String[] partNotes = new String[] { "1 bar", "1/2", "1/4", "1/8", "1/16" };
		protected SpinnerModel partNotesModel = new SpinnerListModel(partNotes);
		protected JSpinner partNotesChooser = new JSpinner(partNotesModel);
		protected JLabel partNotesText = new JLabel("Partnotes:");
	
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
		frame = new JInternalFrame(title);

		// Set colors for panels n the like
		rndVeloCheckPanel.setBackground(backGroundColor);
		rndVeloPanel.setBackground(backGroundColor);
		veloLowPanel.setBackground(backGroundColor);
		veloHighPanel.setBackground(backGroundColor);
		generatorPanel.setBackground(backGroundColor);
		generatePanel.setBackground(backGroundColor);
		generatorAlgorithmPanel.setBackground(backGroundColor);
		channelPanel.setBackground(backGroundColor);
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

		// Configure and add stuff to patternSettingsPanel
		nrOfStepsChooser.setEditor(new JSpinner.DefaultEditor(nrOfStepsChooser));
		nrOfStepsChooser.setPreferredSize(new Dimension(43, 25));
		partNotesChooser.setEditor(new JSpinner.DefaultEditor(partNotesChooser));
//		partNotesChooser.setValue("1/8");
		partNotesChooser.setPreferredSize(new Dimension(60, 25));
		patternSettingsPanel.add(nrOfStepsText);
		patternSettingsPanel.add(nrOfStepsChooser);
		patternSettingsPanel.add(partNotesText);
		patternSettingsPanel.add(partNotesChooser);		
		patternSettingsPanel.add(renamePattern);
		patternSettingsPanel.setBackground(backGroundColor);
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

	/**
	 * Makes a popup appear where you can type in a new name for the active pattern
	 */
	public String renamePattern(int activePattern) {
		String newName = JOptionPane.showInputDialog("New name:", null);
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
	public void setPatternNames(PatternBase[] patterns) {
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
	 * Sets the frame to visible if its not.
	 */
	public void open() {
		frame.setVisible(true);
	}

	// Returns the frame, intended for later adding to desktop
	public JInternalFrame getFrame() {
		return frame;
	}
	
	// The rest here is mostly basic getters and setters	
	
	public JButton[] getPatternChoosers() {
		return patternChoosers;
	}

	public JPanel getPatternPanels() {
		return patternSettingsPanel;
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
	
	public JSpinner getNrOfStepsChooser() {
		return nrOfStepsChooser;
	}

	public JSpinner getPartNotesChooser() {
		return partNotesChooser;
	}
	
	public int getNrOfSteps() {
		return (int) nrOfStepsChooser.getValue();
	}

	public String getPartnotes() {
		return (String) partNotesChooser.getValue();
	}
}
