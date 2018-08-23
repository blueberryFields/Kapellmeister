package arrangement;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

/**
 * Graphic user interface for handling the arrangement section of the master
 * module. Allows user to choose which pattern should be played by each
 * sequencer in each scene and which scenes should be included in the play back.
 */

public class ArrangementWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4745498012660500713L;
	// Create colorscheme
	private Color backGroundColor = new Color(142, 175, 206);
	private Color disabledStepColor = new Color(76, 94, 112);
	private Color enabledStepColor = new Color(193, 218, 242);
	private Color currentSceneColor = Color.RED;
	private Color muteColor = Color.BLUE;
	private Color soloColor = Color.YELLOW;
	private Color enabledText = Color.BLACK;
	private Color disabledText = Color.GRAY;
	private Color sepColor = new Color(95, 125, 153);
	private Color activeSceneColor = new Color(193, 218, 242);

	// Create dimensions
	private Dimension buttonDimSmall = new Dimension(55, 25);
	private Dimension buttonDimLarge = new Dimension(75, 25);
	private Dimension veloChooserDim = new Dimension(50, 25);
	private Dimension noteChooserDim = new Dimension(50, 25);
	private Dimension soloMuteBarDim = new Dimension(55, 20);
	private Dimension patternChooserDim = new Dimension(110, 25);
	private Dimension titleLabelDim = new Dimension(100, 13);

	// Create components for arrangementWindow
	private JPanel loopPanel = new JPanel();
	private JLabel loopLabel = new JLabel("Loop: ");
	private JCheckBox loopCheck = new JCheckBox();
	private JLabel[] titles = new JLabel[8];
	private JButton[] sceneButtons = new JButton[8];

	private SpinnerModel[] lengthModel = new SpinnerNumberModel[8];
	private JSpinner[] lengthChoosers = new JSpinner[8];

	private String[] patternNames;
	private JComboBox<String>[][] patternChoosers = new JComboBox[8][8];

	private JPanel titlePanel = new JPanel();

	private JPanel[] scenePanels = new JPanel[8];

	private GridBagConstraints gbc = new GridBagConstraints();
	private GridBagConstraints sepGbc = new GridBagConstraints();

	private Insets scenePanelInsets = new Insets(0, 8, 0, 8);

	/**
	 * Constructor. Creates the base of the arrangementWindow
	 */
	public ArrangementWindow() {
		super("Arrangement Window");

		setLayout(new GridBagLayout());

		titlePanel.setLayout(new GridBagLayout());
		titlePanel.setBackground(backGroundColor);
		sepGbc.gridx = 0;
		sepGbc.gridy = 0;
		sepGbc.weightx = 1;
		sepGbc.weighty = 1;
		sepGbc.fill = SwingConstants.VERTICAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		loopPanel.setBackground(backGroundColor);
		loopPanel.setPreferredSize(new Dimension(120, 30));
		loopPanel.add(loopLabel);
		loopPanel.add(loopCheck);
		loopCheck.setSelected(true);
		titlePanel.add(loopPanel, gbc);
		add(titlePanel, gbc);

		for (int i = 0; i < scenePanels.length; i++) {
			scenePanels[i] = new JPanel();
			scenePanels[i].setBackground(backGroundColor);
			scenePanels[i].setLayout(new GridBagLayout());
			sceneButtons[i] = new JButton("Scene " + (i + 1));
			sceneButtons[i].setPreferredSize(buttonDimLarge);
			gbc.gridx = 1;
			gbc.gridy = 0;
			scenePanels[i].add(sceneButtons[i], gbc);
			lengthModel[i] = new SpinnerNumberModel(4, 1, 32, 1);
			lengthChoosers[i] = new JSpinner(lengthModel[i]);
			lengthChoosers[i].setEditor(new JSpinner.DefaultEditor(lengthChoosers[i]));
			lengthChoosers[i].setPreferredSize(new Dimension(42, 25));
			gbc.gridx = 2;
			scenePanels[i].add(lengthChoosers[i], gbc);
			gbc.gridx = (i + 1);
			GridBagConstraints scenePanelGbc = new GridBagConstraints();
			scenePanelGbc.insets = scenePanelInsets;
			add(scenePanels[i], scenePanelGbc);
		}

		getContentPane().setBackground(backGroundColor);
		setBackground(backGroundColor);
		setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * Adds a sequencer and a row of patternchoosers, one for each scene
	 * 
	 * @param nextIndex
	 *            next free index, this is where in the different arrays that the
	 *            associated components will be placed
	 * @param title
	 *            the title/name of the sequencers that will be added
	 * @param patternNames
	 *            an array of Strings containging the names of the patterns the new
	 *            sequencer knows
	 */
	public void addSequencer(int nextIndex, String title, String[] patternNames) {
		titles[nextIndex] = new JLabel(title + ":");
		titles[nextIndex].setPreferredSize(titleLabelDim);
		titles[nextIndex].setHorizontalAlignment(SwingConstants.RIGHT);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = nextIndex + 1;
		gbc.insets = new Insets(5, 5, 5, 5);
		titlePanel.add(titles[nextIndex], gbc);
		gbc.gridx = 1;
		gbc.gridy = nextIndex + 1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 0, 0, 0);
		for (int i = 0; i < scenePanels.length; i++) {
			sepGbc.gridx = 0;
			sepGbc.gridy = 0;
			scenePanels[i].add(patternChoosers[nextIndex][i] = new JComboBox<String>(patternNames), gbc);
			patternChoosers[nextIndex][i].setPreferredSize(new Dimension(120, 25));
			patternChoosers[nextIndex][i].setSelectedIndex(i);
		}
		repaint();
		pack();
	}

	/**
	 * Removes a sequencer and the associated patternChoosers from the
	 * arrangeWindow.
	 * 
	 * @param index
	 *            index of the sequencer to remove
	 */
	public void removeInstrument(int index) {
		titlePanel.remove(titles[index]);
		for (int i = 0; i < scenePanels.length; i++) {
			scenePanels[i].remove(patternChoosers[index][i]);
		}
	}

	/**
	 * Removes all sequencers and their patternchooseres from the arrangeWindow
	 * 
	 * @param lastUsedIndex
	 *            last used index of the arrays containing the components
	 */
	public void removeAllInstruments(int lastUsedIndex) {
		for (int i = 0; i <= lastUsedIndex; i++) {
			removeInstrument(i);
		}
	}

	/**
	 * Simply calls repaint() and pack();
	 */
	public void repaintAndPack() {
		repaint();
		pack();
	}

	/**
	 * Change title of a sequencer
	 * 
	 * @param title
	 *            the new title
	 * @param index
	 *            index of the sequencer to be renamed
	 */
	public void changeTitle(String title, int index) {
		titles[index].setText(title + ":");
	}

	/**
	 * Paints a scene in the arrangementWindow to indicate it is active and will be
	 * included in the playback
	 * 
	 * @param scene
	 *            index of the scene to be marked
	 */
	public void markActiveScene(int scene) {
		scenePanels[scene].setBackground(activeSceneColor);
	}

	/**
	 * Unmark a scene that has been marked as active by the markActiveScene()-method
	 * 
	 * @param scene
	 *            index of the scene to be unmarked
	 */
	public void unmarkAciveScene(int scene) {
		scenePanels[scene].setBackground(backGroundColor);
	}

	/**
	 * Marks the currently playing scene to indicate that it is playing right now
	 * 
	 * @param currentScene
	 *            index of the scene to be marked as currently playing
	 */
	public void markCurrentScene(int currentScene) {
		scenePanels[currentScene].setBackground(currentSceneColor);
	}

	/**
	 * Unmark a scene that has been marked as currently playing by the
	 * markCurrentScene()-method
	 * 
	 * @param currentScene
	 *            index of the scene to be marked as currently playing
	 */
	public void unMarkCurrentScene(int currentScene) {
		scenePanels[currentScene].setBackground(activeSceneColor);
	}

	/**
	 * By rightclicking a sceneButton this method will be called to create a
	 * popup-menu where you can type in a new scenename
	 * 
	 * @param sceneIndex
	 *            index of the scene to be renamed
	 * @return a String containing the new name for the scene
	 */
	public String renameScene(int sceneIndex) {
		String newName = JOptionPane.showInputDialog("New scene name:");
		if (newName != null) {
			sceneButtons[sceneIndex].setText(newName);
			return newName;
		} else {
			return sceneButtons[sceneIndex].getText();
		}
	}

	// The rest is simple getters and setters
	public JButton[] getSceneButtons() {
		return sceneButtons;
	}

	public JComboBox<String>[][] getSequenceChoosers() {
		return patternChoosers;
	}

	public int getSequenceChoice(int instrument, int scene) {
		return patternChoosers[instrument][scene].getSelectedIndex();
	}

	public JSpinner[] getLengthChoosers() {
		return lengthChoosers;
	}

	public boolean loopIsSelected() {
		return loopCheck.isSelected();
	}

	public String[] getPatternNames() {
		return patternNames;
	}

	public void setPatternNames(String[] patternNames) {
		this.patternNames = patternNames;
	}
}
