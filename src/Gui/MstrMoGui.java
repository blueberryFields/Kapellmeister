package Gui;

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
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;

public class MstrMoGui extends JFrame {

	// Create colorscheme
	private Color backGroundColor = new Color(142, 175, 206);
	private Color disabledStepColor = new Color(76, 94, 112);
	private Color enabledStepColor = new Color(193, 218, 242);
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

	private JMenuBar createNewBar = new JMenuBar();
	private JMenu createNewMenu = new JMenu("Create new");
	private JMenuItem standardSequencer = new JMenuItem("Standard Sequencer");

	private JMenuBar saveLoadBar = new JMenuBar();
	private JMenu saveLoadMenu = new JMenu("Save & Load");
	private JMenuItem save = new JMenuItem("Save");
	private JMenuItem saveAs = new JMenuItem("Save As");
	private JMenuItem load = new JMenuItem("Load");
	
	private JSeparator sep = new JSeparator();
	
	//Size for masterPanel and seqStrips
	private Dimension stripDim = new Dimension(550, 35);

	// Create stuff for seqStrips
	private List<JSeparator> separators = new LinkedList<>();
	private List<JPanel> seqPanels = new LinkedList<>();
	private List<JLabel> titles = new LinkedList<>();
	private List<JButton> open = new LinkedList<>();
	private List<JButton> remove = new LinkedList<>();
	private List<JButton> mute = new LinkedList<>();
	private List<JButton> solo = new LinkedList<>();

	// Create font for menus
	private Font menuFont = playStopButtons[0].getFont();

	public MstrMoGui() {
		super("Master Module");

		// Set colors and fonts
		this.getContentPane().setBackground(backGroundColor);
		setBackground(backGroundColor);
		masterPanel.setBackground(backGroundColor);
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

		createNewMenu.add(standardSequencer);
		createNewBar.add(createNewMenu);
		masterPanel.add(createNewBar);

		saveLoadMenu.add(save);
		saveLoadMenu.add(saveAs);
		saveLoadMenu.add(load);
		saveLoadBar.add(saveLoadMenu);
		masterPanel.add(saveLoadBar);

		setLayout(new GridBagLayout());
		GridBagConstraints masterPanelGbc = new GridBagConstraints();
		
		masterPanelGbc.gridx = 0;
		masterPanelGbc.gridy = 0;
		add(masterPanel, gbc);
		masterPanelGbc.gridy = 1;
		masterPanelGbc.fill = GridBagConstraints.HORIZONTAL;
		add(sep, masterPanelGbc);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	public void addNewSeqStrip(String title, int index) {
		separators.add(new JSeparator());
		separators.get(index).setForeground(sepColor);
		titles.add(new JLabel(title));
		titles.get(index).setPreferredSize(new Dimension(250, 25));
		open.add(new JButton("Open"));
		remove.add(new JButton("Remove"));
		mute.add(new JButton("Mute"));
		solo.add(new JButton("Solo"));
		seqPanels.add(new JPanel());
		seqPanels.get(index).setLayout(new GridBagLayout());
		seqPanels.get(index).setBackground(backGroundColor);
		GridBagConstraints seqPanelGbc = new GridBagConstraints();
		seqPanelGbc.gridx = 0;
		seqPanelGbc.gridy = 0;
		seqPanelGbc.gridwidth = 2;
		seqPanels.get(index).add(titles.get(index), seqPanelGbc);
		seqPanelGbc.gridwidth = 1;
		seqPanelGbc.gridx = 2;
		seqPanelGbc.gridy = 0;
		seqPanels.get(index).add(open.get(index), seqPanelGbc);
		seqPanelGbc.gridx = 3;
		seqPanelGbc.gridy = 0;
		seqPanels.get(index).add(remove.get(index), seqPanelGbc);
		seqPanelGbc.gridx = 4;
		seqPanelGbc.gridy = 0;
		seqPanels.get(index).add(mute.get(index), seqPanelGbc);
		seqPanelGbc.gridx = 5;
		seqPanelGbc.gridy = 0;
		seqPanels.get(index).add(solo.get(index), seqPanelGbc);
		seqPanelGbc.gridx = 0;
		seqPanelGbc.gridy = 1;
		
		gbc.gridy = index + 2;
		seqPanels.get(index).setPreferredSize(stripDim);
		add(seqPanels.get(index), gbc);
		pack();
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
}
