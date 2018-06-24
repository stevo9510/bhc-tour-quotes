package bhc.tours;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class BhcTourFrame extends JFrame {
	
	private static final String BHC_HOME_PAGE_URL = "https://web7.jhuep.com/~sande107/bhc_site_v1/Homework3.html";
	
	
	public BhcTourFrame() {
		setTitle("BHC Tour Options");
		setSize(1000, 600);
		
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
		
		mainPanel.add(lakeButton, "Gardiner Lake");
		mainPanel.add(plateauButton, "Hellroaring Plateau");
		mainPanel.add(pathButton, "The Beaten Path");
		
		JButton openWebsiteButton = new JButton("View More Details");
		openWebsiteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					openWebpage(new URL(BHC_HOME_PAGE_URL));
				} catch (MalformedURLException e) {
					// TODO: Add exception handling here
				}
			}			
		});
		
		mainPanel.add(openWebsiteButton);
		
		JLabel durationLabel = createStandardLabel("Duration");
		
		JComboBox monthComboBox = new JComboBox();
		monthComboBox.addItem("January");
		
		mainPanel.add(durationLabel);
		mainPanel.add(monthComboBox);
	}
	
	private JLabel createStandardLabel(String text) {
		JLabel label = new JLabel(text + ":");
		return label;		
	}
	
	private JRadioButton createStandardRadioButton(String text, String deselectedIconPath, String selectedIconPath) {
		
		Icon deselectedIcon = new ImageIcon(getClass().getResource(deselectedIconPath));
		Icon selectedIcon = new ImageIcon(getClass().getResource(selectedIconPath));
		JRadioButton radioButton = new JRadioButton(text, deselectedIcon, false);
		radioButton.setHorizontalTextPosition(SwingConstants.CENTER);
		radioButton.setVerticalTextPosition(SwingConstants.TOP);
		radioButton.setSelectedIcon(selectedIcon);
		radioButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return radioButton;
	}
	
	// TODO: Utility Method
	private static boolean openWebpage(URL url) {
	    try {
	        return openWebpage(url.toURI());
	    } catch (URISyntaxException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	// TODO: Utility Method
	private static boolean openWebpage(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return false;
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
