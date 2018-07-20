package Gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;

public class MstrMoGui extends JFrame {

	// Create colorscheme
	private Color backGroundColor = new Color(142, 175, 206);
	private Color disabledStepColor = new Color(76, 94, 112);
	private Color enabledStepColor = new Color(193, 218, 242);

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

	// Create font for menus
	private Font menuFont = playStopButtons[0].getFont();

	public MstrMoGui() {
		super("Master Module");

		// Set colors and fonts
		masterPanel.setBackground(backGroundColor);
		createNewMenu.setFont(menuFont);
		standardSequencer.setFont(menuFont);
		saveLoadMenu.setFont(menuFont);
		save.setFont(menuFont);
		saveAs.setFont(menuFont);
		load.setFont(menuFont);

		// Add stuff to and confugure masterPanel

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

		add(masterPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
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
}
