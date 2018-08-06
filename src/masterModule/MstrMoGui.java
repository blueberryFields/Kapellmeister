package masterModule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SwingConstants;

import arrangement.SoloMute;
import sequencer.SequencerController;

public class MstrMoGui extends JFrame {

	// Create colorscheme
	private Color backGroundColor = new Color(142, 175, 206);
	private Color muteColor = Color.BLUE;
	private Color soloColor = Color.YELLOW;
	// private Color disabledStepColor = new Color(76, 94, 112);
	// private Color enabledStepColor = new Color(193, 218, 242);
	private Color sepColor = new Color(95, 125, 153);

	private GridBagConstraints gbc = new GridBagConstraints();

	// Create components for masterpanel
	private JPanel masterPanel = new JPanel();
	private JButton[] playStopButtons = new JButton[] { new JButton("Play"), new JButton("Stop") };
	private JLabel bpmText = new JLabel("Bpm:");
	private JTextField bpmChooser = new JTextField("120", 2);
	private JLabel keyText = new JLabel("Key:");
	private String[] keyArr = new String[] { "Am", "C" };
	private SpinnerModel keyChooserModel = new SpinnerListModel(keyArr);
	private JSpinner keyChooser = new JSpinner(keyChooserModel);
	private JMenuBar saveLoadBar = new JMenuBar();
	private JMenu saveLoadMenu = new JMenu("Save & Load");
	private JMenuItem save = new JMenuItem("Save");
	private JMenuItem saveAs = new JMenuItem("Save As");
	private JMenuItem load = new JMenuItem("Load");
	private JButton openArr = new JButton("Arrange");

	private JSeparator sep = new JSeparator();

	// Create stuff for seqStrips
	private JPanel stripPanel = new JPanel();
	private JPanel[] seqPanels = new JPanel[8];
	private JTextField[] titles = new JTextField[8];
	private JLabel[] colon = new JLabel[8];
	private JButton[] openClose = new JButton[8];
	private JButton[] remove = new JButton[8];
	private JButton[] mute = new JButton[8];
	private JButton[] solo = new JButton[8];
	private JLabel[] soloMuteBar = new JLabel[8];

	// Create stuff for create panel
	private JPanel createPanel = new JPanel();
	private JMenuBar createNewBar = new JMenuBar();
	private JMenu createNewMenu = new JMenu("Create new");
	private JMenuItem standardSequencer = new JMenuItem("Standard Sequencer");

	// Size for masterPanel and seqStrips and more
	private Dimension stripDim = new Dimension(555, 35);
	private Dimension soloMuteBarColor = new Dimension(55, 20);

	// Create font for menus
	private Font menuFont = playStopButtons[0].getFont();

	// Konstruktor
	public MstrMoGui() {
		super("Master Module");

		// Set colors and fonts
		getContentPane().setBackground(backGroundColor);
		masterPanel.setBackground(backGroundColor);
		createPanel.setBackground(backGroundColor);
		stripPanel.setBackground(backGroundColor);
		createNewMenu.setFont(menuFont);
		standardSequencer.setFont(menuFont);
		saveLoadMenu.setFont(menuFont);
		save.setFont(menuFont);
		saveAs.setFont(menuFont);
		load.setFont(menuFont);
		sep.setForeground(sepColor);

		// Add stuff to and confugure masterPanel
		masterPanel.setPreferredSize(stripDim);
		for (int i = 0; i < playStopButtons.length; i++) {
			masterPanel.add(playStopButtons[i]);
		}

		masterPanel.add(bpmText);
		masterPanel.add(bpmChooser);

		keyChooser.setEditor(new JSpinner.DefaultEditor(keyChooser));
		keyChooser.setPreferredSize(new Dimension(55, 25));
		masterPanel.add(keyText);
		masterPanel.add(keyChooser);

		saveLoadMenu.add(save);
		saveLoadMenu.add(saveAs);
		saveLoadMenu.add(load);
		saveLoadBar.add(saveLoadMenu);
		masterPanel.add(saveLoadBar);
		masterPanel.add(openArr);

		// Add stuff to and configure create panel
		createPanel.setPreferredSize(stripDim);
		// createPanel.setLayout(new BorderLayout());

		createNewMenu.add(standardSequencer);
		createNewBar.add(createNewMenu);
		createPanel.add(createNewBar);

		// Configure stripPanel
		stripPanel.setLayout(new GridBagLayout());

		setLayout(new GridBagLayout());
		GridBagConstraints masterPanelGbc = new GridBagConstraints();

		masterPanelGbc.gridx = 0;
		masterPanelGbc.gridy = 0;
		add(masterPanel, gbc);
		masterPanelGbc.gridy = 1;
		masterPanelGbc.fill = GridBagConstraints.HORIZONTAL;
		add(sep, masterPanelGbc);
		masterPanelGbc.fill = GridBagConstraints.NONE;
		masterPanelGbc.gridy = 2;
		add(stripPanel, masterPanelGbc);
		masterPanelGbc.gridy = 3;
		add(createPanel, masterPanelGbc);

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	public void addNewSeqStrip(String title, int index) {
		titles[index] = new JTextField(title, 10);
		titles[index].setHorizontalAlignment(JTextField.RIGHT);
		titles[index].setBackground(backGroundColor);
		colon[index] = new JLabel(":");
		openClose[index] = new JButton("Open");
		remove[index] = new JButton("Remove");
		mute[index] = new JButton("Mute");
		solo[index] = new JButton("Solo");
		soloMuteBar[index] = new JLabel();
		soloMuteBar[index].setOpaque(true);
		soloMuteBar[index].setPreferredSize(soloMuteBarColor);
		soloMuteBar[index].setBackground(backGroundColor);
		soloMuteBar[index].setHorizontalAlignment(SwingConstants.CENTER);
		seqPanels[index] = new JPanel();
		seqPanels[index].setLayout(new GridBagLayout());
		seqPanels[index].setBackground(backGroundColor);
		GridBagConstraints seqPanelGbc = new GridBagConstraints();
		// GridBagConstraints seqSepGbc = new GridBagConstraints();
		// titlePanels.get(index).add(titles.get(index));
		seqPanelGbc.gridx = 0;
		seqPanelGbc.gridy = 0;
		// seqPanelGbc.gridwidth = 2;
		seqPanels[index].add(titles[index], seqPanelGbc);
		// seqPanelGbc.gridwidth = 1;
		seqPanelGbc.gridx = 2;
		seqPanelGbc.gridy = 0;
		seqPanels[index].add(colon[index], seqPanelGbc);
		seqPanelGbc.gridx = 3;
		seqPanelGbc.gridy = 0;
		seqPanels[index].add(openClose[index], seqPanelGbc);
		seqPanelGbc.gridx = 4;
		seqPanelGbc.gridy = 0;
		seqPanels[index].add(remove[index], seqPanelGbc);
		seqPanelGbc.gridx = 5;
		seqPanelGbc.gridy = 0;
		seqPanels[index].add(mute[index], seqPanelGbc);
		seqPanelGbc.gridx = 6;
		seqPanelGbc.gridy = 0;
		seqPanels[index].add(solo[index], seqPanelGbc);
		seqPanelGbc.gridx = 7;
		seqPanelGbc.gridy = 0;
		seqPanels[index].add(soloMuteBar[index], seqPanelGbc);

		gbc.gridy = index;
		seqPanels[index].setPreferredSize(stripDim);
		stripPanel.add(seqPanels[index], gbc);
		pack();
	}

	public String getTitle(int index) {
		return titles[index].getText();
	}

	public void setSoloMuteBar(SoloMute soloMute, int index) {
		switch (soloMute) {
		case MUTE:
			soloMuteBar[index].setBackground(muteColor);
			soloMuteBar[index].setText("MUTE");
			break;
		case SOLO:
			soloMuteBar[index].setBackground(soloColor);
			soloMuteBar[index].setText("SOLO");
			break;
		case AUDIBLE:
			soloMuteBar[index].setBackground(backGroundColor);
			soloMuteBar[index].setText("");
			break;
		default:
			soloMuteBar[index].setBackground(backGroundColor);
			soloMuteBar[index].setText("");
		}
	}

	public void removeAllSeqStrips() {
		for (int i = 0; i < seqPanels.length; i++) {
			if (seqPanels[i] != null) {
				removeSeqStrip(i);
			}
		}
		// repaint();
		// pack();
	}

	public void removeSeqStrip(int index) {
		seqPanels[index].remove(titles[index]);
		seqPanels[index].remove(colon[index]);
		seqPanels[index].remove(remove[index]);
		seqPanels[index].remove(mute[index]);
		seqPanels[index].remove(solo[index]);
		stripPanel.remove(seqPanels[index]);

		titles[index] = null;
		colon[index] = null;
		remove[index] = null;
		mute[index] = null;
		solo[index] = null;
		seqPanels[index] = null;
		// repaint();
		// pack();
	}

	public void paintAndPack() {
		repaint();
		pack();
	}

	public void disableGui(int lastUsedIndex) {
		bpmChooser.setEnabled(false);
		keyChooser.setEnabled(false);
		createNewMenu.setEnabled(false);
		saveLoadMenu.setEnabled(false);
		for (int i = 0; i <= lastUsedIndex; i++) {
			remove[i].setEnabled(false);
		}
	}

	public void enableGui(int lastUsedIndex) {
		bpmChooser.setEnabled(true);
		keyChooser.setEnabled(true);
		saveLoadMenu.setEnabled(true);
		createNewMenu.setEnabled(true);
		for (int i = 0; i <= lastUsedIndex; i++) {
			remove[i].setEnabled(true);
		}
	}

	// public void setSoloMuteColor(SoloMute soloMute) {
	//
	// }

	public JTextField[] getTitles() {
		return titles;
	}

	public JLabel[] getRename() {
		return colon;
	}

	public JButton[] getOpen() {
		return openClose;
	}

	public JButton[] getRemove() {
		return remove;
	}

	public JButton[] getMute() {
		return mute;
	}

	public JButton[] getSolo() {
		return solo;
	}

	public String getKey() {
		return (String) keyChooser.getValue();
	}

	public JMenuItem getStandardSequencer() {
		return standardSequencer;
	}

	public JMenuItem getSave() {
		return save;
	}

	public JMenuItem getSaveAs() {
		return saveAs;
	}

	public JMenuItem getLoad() {
		return load;
	}

	public JButton[] getPlayStopButtons() {
		return playStopButtons;
	}

	public int getBpm() {
		return Integer.parseInt(bpmChooser.getText());
	}

	public JSpinner getKeyChooser() {
		return keyChooser;
	}

	public JTextField getBpmChooser() {
		return bpmChooser;
	}

	public JButton getOpenArr() {
		return openArr;
	}

	public JPanel[] getSeqPanels() {
		return seqPanels;
	}
}
