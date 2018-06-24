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
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
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
		
		this.setLayout(new BorderLayout());
		
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//		headerPanel.setBackground(Color.);	
		headerPanel.setPreferredSize(new Dimension(900, 40));
		JLabel headerLabel = new JLabel("Request a Quote", JLabel.LEFT);
		headerLabel.setFont(new Font("Helvetica", Font.BOLD + Font.ITALIC, 22));
		headerPanel.add(headerLabel);
		getContentPane().add(headerPanel, BorderLayout.NORTH);

		GridBagConstraints remainderGBC = new GridBagConstraints();
		remainderGBC.gridx = GridBagConstraints.REMAINDER;
		remainderGBC.gridy = 1;
		
		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(900, 500));
		Border panelBorder = BorderFactory.createEtchedBorder();
		mainPanel.setBorder(BorderFactory.createTitledBorder(panelBorder, "Tour Date", TitledBorder.TOP, TitledBorder.CENTER));
		
		getContentPane().add(mainPanel);
				
		durationComboBox = new JComboBox<Integer>();
		radioButtonGroup = new ButtonGroup();
		
		HikeOptionViewModel gardinerVM = new HikeOptionViewModel("Gardiner Lake", HIKE.GARDINER, "gardiner-lake.jpg", "gardiner-lake_border.jpg", LAKE_DURATIONS);
		HikeOptionViewModel hellroaringVM = new HikeOptionViewModel("Hellroaring Plateau", HIKE.HELLROARING, "plateau.jpg", "plateau_border.jpg", PLATEAU_DURATIONS);
		HikeOptionViewModel beatenVM = new HikeOptionViewModel("The Beaten Path", HIKE.BEATEN, "beaten-path.jpg", "beaten-path_border.jpg", PATH_DURATIONS);
		
		JRadioButton gardinerButton = createStandardHikeRadioButton(gardinerVM);
		JRadioButton hellroaringButton = createStandardHikeRadioButton(hellroaringVM);
		JRadioButton beatenButton = createStandardHikeRadioButton(beatenVM);
	
		mainPanel.add(gardinerButton, "Gardiner Lake");
		mainPanel.add(hellroaringButton, "Hellroaring Plateau");
		mainPanel.add(beatenButton, "The Beaten Path");
		
		JButton openWebsiteButton = new JButton("View More Details...");
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
		
		JComboBox<String> monthComboBox = createMonthComboBox();
		JComboBox<Integer> dayComboBox = createDayComboBox();
		JComboBox<Integer> yearComboBox = createYearComboBox();
				
		mainPanel.add(durationLabel);
		mainPanel.add(durationComboBox);
		mainPanel.add(monthComboBox);
		mainPanel.add(dayComboBox);
		mainPanel.add(yearComboBox);
		
		requestQuoteButton = new JButton("Request Quote");
		requestQuoteButton.setEnabled(false);
		requestQuoteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				requestRate(yearComboBox, monthComboBox, dayComboBox, durationComboBox);
			}			
		});
		mainPanel.add(requestQuoteButton);
		costDisplayLabel = new JLabel("Cost: ");
		mainPanel.add(costDisplayLabel);
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
		
	private JLabel createStandardLabel(String text) {
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
