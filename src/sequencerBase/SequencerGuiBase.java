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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import pattern.StandardPattern;

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
	protected JButton[] copyPaste = new JButton[2];

	// Create components for patternsPanel
	protected JPanel patternPanel = new JPanel();
	protected JButton[] patternChoosers = new JButton[8];
	protected JPanel patternSettingsPanel = new JPanel();
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

		patternSettingsPanel.setBackground(backGroundColor);
		patternSettingsPanel.add(renamePattern);
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
	public void setPatternNames(StandardPattern[] patterns) {
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
}
