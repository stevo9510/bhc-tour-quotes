package bhc.tours;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
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
		
		JRadioButton lakeButton = createStandardRadioButton("Gardiner Lake", "gardiner-lake.jpg", "gardiner-lake_border.jpg");
		JRadioButton plateauButton = createStandardRadioButton("Hellroaring Plateau", "plateau.jpg", "plateau_border.jpg");
		JRadioButton pathButton = createStandardRadioButton("The Beaten Path", "beaten-path.jpg", "beaten-path_border.jpg");
		ButtonGroup radioButtonGroup = new ButtonGroup();
		radioButtonGroup.add(lakeButton);
		radioButtonGroup.add(plateauButton);
		radioButtonGroup.add(pathButton);
		
		lakeButton.setSelected(true);

//		https://web7.jhuep.com/~sande107/bhc_site_v1/Homework3.html
		mainPanel.add(lakeButton, "Gardiner Lake");
		mainPanel.add(plateauButton, "Hellroaring Plateau");
		mainPanel.add(pathButton, "The Beaten Path");
	}
	
	private JRadioButton createStandardRadioButton(String text, String deselectedIconPath, String selectedIconPath) {
		
		Icon deselectedIcon = new ImageIcon(getClass().getResource(deselectedIconPath));
		Icon selectedIcon = new ImageIcon(getClass().getResource(selectedIconPath));
		JRadioButton radioButton = new JRadioButton(text, deselectedIcon, false);
		radioButton.setSelectedIcon(selectedIcon);
		radioButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return radioButton;
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
