package bhc.tours;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractButton;
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
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import com.rbevans.bookingrate.BookingDay;
import com.rbevans.bookingrate.Rates;
import com.rbevans.bookingrate.Rates.HIKE;

public class BhcTourFrame extends JFrame {
	
	private static final String BHC_HOME_PAGE_URL = "https://web7.jhuep.com/~sande107/bhc_site_v1/Homework3.html";
	
	// Note: These are based off BookingDay.java
	private static final int VALID_START_YEAR = 2007;
	private static final int VALID_END_YEAR = 2020;

	private static final List<Integer> LAKE_DURATIONS = 
			Collections.unmodifiableList(Arrays.asList(3, 5));
	private static final List<Integer> PLATEAU_DURATIONS = 
			Collections.unmodifiableList(Arrays.asList(2, 3, 4));
	private static final List<Integer> PATH_DURATIONS = 
			Collections.unmodifiableList(Arrays.asList(5, 7));
	
	private JComboBox<Integer> durationComboBox;
	private JButton requestQuoteButton;
	private ButtonGroup radioButtonGroup;
	private HashMap<JRadioButton, HikeOptionViewModel> buttonToViewModel = new HashMap<JRadioButton, HikeOptionViewModel>();
	private JLabel costDisplayLabel;	
	
	public BhcTourFrame() {
		setTitle("BHC Tour Options");
		setSize(900, 600);
		
		setLayout(new GridBagLayout());
				
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		headerPanel.setBackground(Color.black);	
		headerPanel.setPreferredSize(new Dimension(900, 40));

		JLabel headerLabel = new JLabel("Request a Quote", JLabel.LEFT);
		headerLabel.setFont(new Font("Helvetica", Font.BOLD + Font.ITALIC, 22));
		headerPanel.add(headerLabel);		
		
		GridBagConstraints headerGBC = new GridBagConstraints();
		headerGBC.gridx = 0;
		headerGBC.gridy = 0;
		headerGBC.fill = GridBagConstraints.BOTH; // prevent collapsing from resize
		headerGBC.weightx = 1.0; 
		headerGBC.weighty = 0.0; // don't resize 
		headerGBC.insets = new Insets(3, 3, 3, 3);
		getContentPane().add(headerPanel, headerGBC);
		
		GridBagConstraints remainderGBC = new GridBagConstraints();
		remainderGBC.gridx = 0;
		remainderGBC.gridy = 1;	
		remainderGBC.fill = GridBagConstraints.BOTH; // prevent collapsing from resize
		remainderGBC.weightx = 1.0; 
		remainderGBC.weighty = 0.0; // don't resize in this direction
		JPanel selectTourOptionPanel = new JPanel();
		selectTourOptionPanel.setPreferredSize(new Dimension(900, 250));
		Border panelBorder = BorderFactory.createEtchedBorder();
		selectTourOptionPanel.setBorder(BorderFactory.createTitledBorder(panelBorder, "Step 1: Select a Tour Type", TitledBorder.LEFT, TitledBorder.CENTER));
		
		durationComboBox = new JComboBox<Integer>();
		durationComboBox.setPreferredSize(new Dimension(60, durationComboBox.getPreferredSize().height));
		radioButtonGroup = new ButtonGroup();
		
		HikeOptionViewModel gardinerVM = new HikeOptionViewModel("Gardiner Lake", HIKE.GARDINER, "gardiner-lake.jpg", "gardiner-lake_border.jpg", LAKE_DURATIONS);
		HikeOptionViewModel hellroaringVM = new HikeOptionViewModel("Hellroaring Plateau", HIKE.HELLROARING, "plateau.jpg", "plateau_border.jpg", PLATEAU_DURATIONS);
		HikeOptionViewModel beatenVM = new HikeOptionViewModel("The Beaten Path", HIKE.BEATEN, "beaten-path.jpg", "beaten-path_border.jpg", PATH_DURATIONS);
		
		JRadioButton gardinerButton = createStandardHikeRadioButton(gardinerVM);
		JRadioButton hellroaringButton = createStandardHikeRadioButton(hellroaringVM);
		JRadioButton beatenButton = createStandardHikeRadioButton(beatenVM);
	
		selectTourOptionPanel.add(gardinerButton, "Gardiner Lake");
		selectTourOptionPanel.add(hellroaringButton, "Hellroaring Plateau");
		selectTourOptionPanel.add(beatenButton, "The Beaten Path");

		getContentPane().add(selectTourOptionPanel, remainderGBC);
		
		JButton openWebsiteButton = new JButton("View More Tour Details...");
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
		
		JPanel step2and3Host = new JPanel(new GridBagLayout());
						
		JPanel selectDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		Border dateInfoBorder = BorderFactory.createEtchedBorder();
		selectDatePanel.setBorder(BorderFactory.createTitledBorder(dateInfoBorder, "Step 2: Select Timeframe", TitledBorder.LEFT, TitledBorder.CENTER));
				
		JLabel durationLabel = createStandardFieldLabel("Duration (in days)");		
		JLabel startDateLabel = createStandardFieldLabel("Start Date");
		EmptyBorder labelPadding = new EmptyBorder(0, 17, 0, 0); 
		startDateLabel.setBorder(labelPadding);
		
		JComboBox<String> monthComboBox = createMonthComboBox();
		JComboBox<Integer> dayComboBox = createDayComboBox();
		JComboBox<Integer> yearComboBox = createYearComboBox();
				
		selectDatePanel.add(durationLabel);
		selectDatePanel.add(durationComboBox);
		selectDatePanel.add(startDateLabel);
		selectDatePanel.add(monthComboBox);
		selectDatePanel.add(dayComboBox);
		selectDatePanel.add(yearComboBox);
		
		GridBagConstraints dateGBC = new GridBagConstraints();
		dateGBC.gridx = 0;
		dateGBC.gridy = 0;
		dateGBC.fill = GridBagConstraints.BOTH;
		dateGBC.weightx = 0.5;
		dateGBC.weighty = 0.0;
		
		step2and3Host.add(selectDatePanel, dateGBC);		
		
		requestQuoteButton = new JButton("Request Quote");
		requestQuoteButton.setEnabled(false);
		requestQuoteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				requestRate(yearComboBox, monthComboBox, dayComboBox, durationComboBox);
			}			
		});
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		Border buttonsBorder = BorderFactory.createEtchedBorder();
		buttonsPanel.setBorder(BorderFactory.createTitledBorder(buttonsBorder, "Step 3: Request Quote", TitledBorder.LEFT, TitledBorder.CENTER));
		
		buttonsPanel.add(requestQuoteButton);
		buttonsPanel.add(openWebsiteButton);

		GridBagConstraints buttonPanelGBC = new GridBagConstraints();
		buttonPanelGBC.gridx = 1;
		buttonPanelGBC.gridy = 0;
		buttonPanelGBC.fill = GridBagConstraints.BOTH;
		buttonPanelGBC.weightx = 0.5; // prevent collapsing from resize
		buttonPanelGBC.weighty = 0.0; 		
		
		ImageIcon infoIcon = (ImageIcon)UIManager.getIcon("OptionPane.informationIcon");
		Image scaledInfoImage = infoIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		openWebsiteButton.setIcon(new ImageIcon(scaledInfoImage));
		
		ImageIcon moneyIcon = new ImageIcon(getClass().getResource("money_icon.png"));
		moneyIcon = new ImageIcon(moneyIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		requestQuoteButton.setIcon(moneyIcon);
		
		step2and3Host.add(buttonsPanel, buttonPanelGBC);
		
		GridBagConstraints step2and3HostGBC = new GridBagConstraints();
		step2and3HostGBC.gridx = 0;
		step2and3HostGBC.gridy = 2;
		step2and3HostGBC.fill = GridBagConstraints.BOTH;
		step2and3HostGBC.weightx = 1.0; 
		step2and3HostGBC.weighty = 0.0; 
		getContentPane().add(step2and3Host, step2and3HostGBC);
		
		JPanel informationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		Border informationBorder = BorderFactory.createEtchedBorder();
		informationPanel.setBorder(BorderFactory.createTitledBorder(informationBorder, "Request Results", TitledBorder.LEFT, TitledBorder.CENTER));
		
		GridBagConstraints informationPanelGBC = new GridBagConstraints();
		informationPanelGBC.gridx = 0;
		informationPanelGBC.gridy = 3;
		informationPanelGBC.weightx = 1.0;
		informationPanelGBC.weighty = 1.0;
		informationPanelGBC.fill = GridBagConstraints.BOTH;
		costDisplayLabel = createStandardFieldLabel("Cost");
		informationPanel.add(costDisplayLabel);
		
		getContentPane().add(informationPanel, informationPanelGBC);
				
		this.pack();
	}
	
	private void requestRate(JComboBox<Integer> yearComboBox, JComboBox<String> monthComboBox, JComboBox<Integer> dayComboBox, JComboBox<Integer> durationComboBox) {
		JRadioButton selectedButton = null;

		for(AbstractButton button : Collections.list(radioButtonGroup.getElements())) {
			if(button.isSelected()) {
				selectedButton = (JRadioButton)button; 
				break;
			}
		}
		if(selectedButton == null) {
			// TODO:
		}
		
		HikeOptionViewModel viewModel = buttonToViewModel.get(selectedButton);
		requestRate((int)yearComboBox.getSelectedItem(), monthComboBox.getSelectedIndex(), (int)dayComboBox.getSelectedItem(), (int)durationComboBox.getSelectedItem(), viewModel.getHikeType());
	}
		
	private void requestRate(int year, int month, int day, int duration, Rates.HIKE hikeType) {
		BookingDay bookingDate = new BookingDay(year, month + 1, day);
		if(bookingDate.isValidDate()) {
			Rates quoteHelper = new Rates(hikeType);
			quoteHelper.setBeginDate(bookingDate);
			quoteHelper.setDuration(duration);
			if(quoteHelper.isValidDates()) {
				costDisplayLabel.setText("Cost: " + quoteHelper.getCost());
				
			} else {
				// TODO:
			}
		} else {
			// TODO:
		}
	}

	private JComboBox<String> createMonthComboBox() {
		JComboBox<String> monthComboBox = new JComboBox<String>();
		DateFormatSymbols dfs = new DateFormatSymbols();
	
		for(String month : dfs.getMonths()) {
			if(month.trim().length() > 0)
				monthComboBox.addItem(month);
		}
		return monthComboBox;
	}
	
	private JComboBox<Integer> createDayComboBox() {
		JComboBox<Integer> dayComboBox = new JComboBox<Integer>();
		for(int dayIndex = 0; dayIndex < 31; dayIndex++) {
			dayComboBox.addItem(dayIndex + 1);
		}
		return dayComboBox;
	}
	
	private JComboBox<Integer> createYearComboBox() {
		JComboBox<Integer> yearComboBox = new JComboBox<Integer>();
		for(int yearIndex = VALID_START_YEAR; yearIndex < VALID_END_YEAR + 1; yearIndex++) {
			yearComboBox.addItem(yearIndex);
		}		
		return yearComboBox;
	}
		
	private JLabel createStandardFieldLabel(String text) {
		JLabel label = new JLabel(text + ":");
		return label;		
	}
	
	private JRadioButton createStandardHikeRadioButton(HikeOptionViewModel viewModel) {
		Icon deselectedIcon = new ImageIcon(getClass().getResource(viewModel.getNormalIconFilePath()));
		Icon selectedIcon = new ImageIcon(getClass().getResource(viewModel.getBorderedIconFilePath()));
		JRadioButton radioButton = new JRadioButton(viewModel.getDisplayName(), deselectedIcon, false);
		radioButton.setHorizontalTextPosition(SwingConstants.CENTER);
		radioButton.setVerticalTextPosition(SwingConstants.TOP);
		radioButton.setSelectedIcon(selectedIcon);
		radioButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		radioButton.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent evt) {
				if(evt.getStateChange() == ItemEvent.SELECTED) {
					handleTrailRadioButtonSelected(viewModel.getTourDurations());	
				}				
			}
		});
		radioButtonGroup.add(radioButton);
		buttonToViewModel.put(radioButton, viewModel);
		return radioButton;
	}
	
	private void handleTrailRadioButtonSelected(List<Integer> durationOptions) {
		requestQuoteButton.setEnabled(true);
		addDurationOptionsToList(durationOptions);
	}

	private void addDurationOptionsToList(List<Integer> durationOptions) {
		durationComboBox.removeAllItems();
		for(int durationOption : durationOptions) {
			durationComboBox.addItem(durationOption);
		}
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
