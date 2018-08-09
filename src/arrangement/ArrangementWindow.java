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
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

public class ArrangementWindow extends JFrame {

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

	private JPanel loopPanel = new JPanel();
	private JLabel loopLabel = new JLabel("Loop: ");
	private JCheckBox loopCheck = new JCheckBox();
	private JLabel[] titles = new JLabel[8];
	private JButton[] sceneButtons = new JButton[8];

	private SpinnerModel[] lengthModel = new SpinnerNumberModel[8];
	private JSpinner[] lengthChoosers = new JSpinner[8];

	private String[] sequenceNames;
	private JComboBox<String>[][] sequenceChoosers = new JComboBox[8][8];

	private JPanel titlePanel = new JPanel();

	private JPanel[] scenePanels = new JPanel[8];

	private JSeparator[][] rowSeps = new JSeparator[9][8];
	private JSeparator[] instrSeps = new JSeparator[8];

	private GridBagConstraints gbc = new GridBagConstraints();
	private GridBagConstraints sepGbc = new GridBagConstraints();

	private Insets scenePanelInsets = new Insets(0, 8, 0, 8);
	private Insets normalInsets = new Insets(0, 5, 0, 5);

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

		// create separators
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				rowSeps[i][j] = new JSeparator(SwingConstants.VERTICAL);
				rowSeps[i][j].setForeground(sepColor);
			}
		}

		for (int i = 0; i < scenePanels.length; i++) {
			scenePanels[i] = new JPanel();
			scenePanels[i].setBackground(backGroundColor);
			scenePanels[i].setLayout(new GridBagLayout());
			sceneButtons[i] = new JButton("Scene " + (i + 1));
			sceneButtons[i].setPreferredSize(buttonDimLarge);
			// scenePanels[i].add(rowSeps[0][i], sepGbc);
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

	public void addInstrument(int nextIndex, String title, String[] sequenceNames) {
		titles[nextIndex] = new JLabel(title);
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
			// scenePanels[i].add(rowSeps[nextIndex][i], sepGbc);
			scenePanels[i].add(sequenceChoosers[nextIndex][i] = new JComboBox<String>(sequenceNames), gbc);
			sequenceChoosers[nextIndex][i].setPreferredSize(new Dimension(120, 25));
			sequenceChoosers[nextIndex][i].setSelectedIndex(i);
		}
		repaint();
		pack();
	}

	public void changeTitle(String title, int index) {
		titles[index + 1].setText(title);
	}

	public void markActiveScene(int scene) {
		scenePanels[scene].setBackground(activeSceneColor);
	}

	public void unmarkAciveScene(int scene) {
		scenePanels[scene].setBackground(backGroundColor);
	}

	public void markCurrentScene(int currentScene) {
		scenePanels[currentScene].setBackground(currentSceneColor);
	}

	public void unMarkCurrentScene(int currentScene) {
		scenePanels[currentScene].setBackground(activeSceneColor);
	}

	public JButton[] getSceneButtons() {
		return sceneButtons;
	}

	public JComboBox<String>[][] getSequenceChoosers() {
		return sequenceChoosers;
	}

	public int getSequenceChoice(int instrument, int scene) {
		return sequenceChoosers[instrument][scene].getSelectedIndex();
	}

	public JSpinner[] getLengthChoosers() {
		return lengthChoosers;
	}

	public boolean loopIsSelected() {
		return loopCheck.isSelected();
	}
}
