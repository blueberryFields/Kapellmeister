package Gui;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class PrototypeGui extends JFrame {

    // Create components for steppanel
    private JPanel stepPanel = new JPanel();

    // Create components for masterpanel
    private JPanel masterPanel = new JPanel();
    private JButton[] startStopButtons = new JButton[] { new JButton("Start"), new JButton("Stop") };
    private String[] availibleDevices = new String[] { "device1", "device2", "device3" };
    private JComboBox deviceChooser = new JComboBox(availibleDevices);
    
    // Create components for generatorpanel
    private JPanel generatorPanel = new JPanel();
    private JButton generateButton = new JButton("Generate");
    private SpinnerModel nrOfSteps = new SpinnerNumberModel(8, 1, 32, 1);
    private String[] keyArr = new String[] { "Am" };
    private SpinnerModel keyChooser = new SpinnerListModel(keyArr);    

    public PrototypeGui() {
	//Add stuff to masterPanel
	for (int i = 0; i < startStopButtons.length; i++) {
	    masterPanel.add(startStopButtons[i]);
	}
	masterPanel.add(deviceChooser);

	generatorPanel.add(generateButton);
	generatorPanel.add((Component) nrOfSteps);
	generatorPanel.add((Component) keyChooser);

    }

}
