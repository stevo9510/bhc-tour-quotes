package bhc.tours;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class BhcTourFrame extends JFrame {
	public BhcTourFrame() {
		setTitle("BHC Tour Options");
		setSize(900, 600);
		
		JPanel mainPanel = new JPanel();
		Border panelBorder = BorderFactory.createEtchedBorder();
		mainPanel.setBorder(BorderFactory.createTitledBorder(panelBorder, "Tour Date", TitledBorder.TOP, TitledBorder.CENTER));
		
		getContentPane().add(mainPanel);
		Icon deselectedIcon = new ImageIcon(getClass().getResource("plateau.jpg"));
		Icon selectedIcon = new ImageIcon(getClass().getResource("plateau_border.jpg"));
		JRadioButton button = new JRadioButton("Steven", deselectedIcon, true);
		button.setSelectedIcon(selectedIcon);
		
		mainPanel.add(button, "foo");
	}
	
    public static void main(String args[])
    {
    	// Create an instance of the test application
		final BhcTourFrame mainFrame = new BhcTourFrame();
		Runnable showFrame = new Runnable() {
			public void run() {
			    mainFrame.setVisible(true);
			}
	    };
		SwingUtilities.invokeLater(showFrame);
    }
}
