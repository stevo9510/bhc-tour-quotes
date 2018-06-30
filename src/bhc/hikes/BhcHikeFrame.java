/**
 * Copyright 2018
 * Steven Anderson
 * All rights reserved
 * 
 * Homework 5 - BHC Hike Quotes
 * BhcHikeFrame.java - JFrame used to allow users to request quote/cost information about different hikes (tours). 
 * 06/30/2018
 */

package bhc.hikes;

import java.awt.Color;
import java.awt.Cursor;
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
import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
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
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import com.rbevans.bookingrate.BookingDay;
import com.rbevans.bookingrate.Rates;
import com.rbevans.bookingrate.Rates.HIKE;

@SuppressWarnings("serial")
public class BhcHikeFrame extends JFrame {
	
	private static final String BHC_HOME_PAGE_URL = "https://web7.jhuep.com/~sande107/bhc_site_v1/Homework3.html#tourInfoLabel";
		
	// Note: These are based off BookingDay.java.  Ideally these would be defined / shared between the two classes, but I did not want to modify the provided BookingDay class.
	private static final int VALID_START_YEAR = 2007;
	private static final int VALID_END_YEAR = 2020;

	private static final List<Integer> GARINDER_DURATIONS = Collections.unmodifiableList(Arrays.asList(3, 5));
	private static final List<Integer> HELLROARING_DURATIONS = Collections.unmodifiableList(Arrays.asList(2, 3, 4));
	private static final List<Integer> BEATEN_DURATIONS = Collections.unmodifiableList(Arrays.asList(5, 7));

	// Header related constants (palette help from Color.Adobe.com)
	private static final Color HEADER_BACKGROUND_COLOR = new Color(25, 45, 64);
	private static final Color HEADER_TEXT_COLOR = new Color(150, 172, 191);
	private static final Font HEADER_FONT = new Font("Helvetica", Font.BOLD + Font.ITALIC, 22);
	private static final Color MAIN_BACKGROUND_COLOR = new Color(212, 221, 235);
	
	// Fonts for the results related components.  These are a little larger than default for emphasis to the user.  
	private static final Font RESULTS_LABEL_FONT = new Font("Default", Font.PLAIN, 18);
	private static final Font RESULTS_FIELD_FONT = new Font("Default", Font.BOLD, 18);
	
	private static final int PAGE_WIDTH = 900;
	private static final int PAGE_HEIGHT = 600;
	
	private static final int BTN_IMG_SIZE = 20;
	
	/* 
	 * Controls or mappings that are easiest if defined globally.
	 */
	private JComboBox<Integer> durationComboBox;
	private JButton requestQuoteButton;
	private ButtonGroup radioButtonGroup;
	private JFormattedTextField resultsStartDateTextField;
	private JFormattedTextField resultsEndDateTextField;
	private JTextField hikeTextField;
	private JTextField costTextField;
	private HashMap<JRadioButton, HikeOptionViewModel> buttonToViewModel = new HashMap<JRadioButton, HikeOptionViewModel>();

	public BhcHikeFrame() {
		// Initialize JFrame
		setTitle("BHC Tour Options");
		setSize(PAGE_WIDTH, PAGE_HEIGHT);
		setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridBagLayout());
		setBackground(MAIN_BACKGROUND_COLOR);
		
		int mainYCountForGBC = 0;
		
		/* Setup header */
		JPanel headerPanel = createHeaderPanel();
		JLabel headerLabel = createHeaderLabel();
		headerPanel.add(headerLabel);		
		
		GridBagConstraints headerPanelGBC = createStandardMainRowGBC(mainYCountForGBC++, 0.0, new Insets(3, 3, 3, 3));
		getContentPane().add(headerPanel, headerPanelGBC);
		
		/* Setup Select Hike Options GUI */
		JPanel selectHikeOptionsPanel = createStandardBorderPanel("Step 1: Select a Tour Type");
		selectHikeOptionsPanel.setPreferredSize(new Dimension(PAGE_WIDTH, 250));
		
		// Create view models that hold hike information
		HikeOptionViewModel gardinerVM = new HikeOptionViewModel("Gardiner Lake", HIKE.GARDINER, ResourceNames.GARDINER_LAKE_IMG, ResourceNames.GARDINER_LAKE_IMG_WITH_BORDER, GARINDER_DURATIONS);
		HikeOptionViewModel hellroaringVM = new HikeOptionViewModel("Hellroaring Plateau", HIKE.HELLROARING, ResourceNames.HELLROARING_PLATEAU_IMG, ResourceNames.HELLROARING_PLATEAU_IMG_WITH_BORDER, HELLROARING_DURATIONS);
		HikeOptionViewModel beatenVM = new HikeOptionViewModel("The Beaten Path", HIKE.BEATEN, ResourceNames.BEATEN_PATH_IMG, ResourceNames.BEATEN_PATH_IMG_WITH_BORDER, BEATEN_DURATIONS);
		
		radioButtonGroup = new ButtonGroup();
		
		JRadioButton gardinerButton = createStandardHikeRadioButton(gardinerVM, radioButtonGroup, buttonToViewModel);
		JRadioButton hellroaringButton = createStandardHikeRadioButton(hellroaringVM, radioButtonGroup, buttonToViewModel);
		JRadioButton beatenButton = createStandardHikeRadioButton(beatenVM, radioButtonGroup, buttonToViewModel);
	
		selectHikeOptionsPanel.add(gardinerButton, gardinerVM.getDisplayName());
		selectHikeOptionsPanel.add(hellroaringButton, hellroaringVM.getDisplayName());
		selectHikeOptionsPanel.add(beatenButton, beatenVM.getDisplayName());
		
		GridBagConstraints selectHikePanelGBC = createStandardMainRowGBC(mainYCountForGBC++, 0.0);
		getContentPane().add(selectHikeOptionsPanel, selectHikePanelGBC);
		
		/* Setup the duration and button displays */
		
		// Grid Bag Layout to host two panels (step 2 and step 3)
		JPanel step2and3Host = new JPanel(new GridBagLayout());
		
		/* Setup step 2 host for setting time frame */
		JPanel selectTimeFramePanel = createStandardBorderPanel("Step 2: Select Timeframe");
		
		// Start date label and fields
		JLabel startDateLabel = createStandardFieldLabel("Start Date");		
		JComboBox<String> monthComboBox = createMonthComboBox();
		JComboBox<Integer> dayComboBox = createDayComboBox();
		JComboBox<Integer> yearComboBox = createYearComboBox();
		
		selectTimeFramePanel.add(startDateLabel);
		selectTimeFramePanel.add(monthComboBox);
		selectTimeFramePanel.add(dayComboBox);
		selectTimeFramePanel.add(yearComboBox);

		// Duration label and fields
		JLabel durationLabel = createStandardFieldLabel("Duration (in days)");		
		setStandardLabelPadding(durationLabel);  // pad from previous fields/label
		durationComboBox = new JComboBox<Integer>();
		durationComboBox.setPreferredSize(new Dimension(60, durationComboBox.getPreferredSize().height));
		
		selectTimeFramePanel.add(durationLabel);
		selectTimeFramePanel.add(durationComboBox);
		
		int secondaryXCountForGBC = 0;
		
		// add duration panel to the host
		GridBagConstraints timeframeGBC = createSecondaryRowGBC(secondaryXCountForGBC++);
		step2and3Host.add(selectTimeFramePanel, timeframeGBC);		
		
		/* Setup step 3 host for action buttons */
		JPanel actionButtonsPanel = createStandardBorderPanel("Step 3: Request Quote");
		
		// Create request button and add to panel
		requestQuoteButton = createRequestQuoteButton(monthComboBox, dayComboBox, yearComboBox, durationComboBox);
		actionButtonsPanel.add(requestQuoteButton);

		// create open website button and add to panel
		JButton openWebsiteButton = createOpenWebsiteButton();
		actionButtonsPanel.add(openWebsiteButton);
		
		// Add button panel to host
		GridBagConstraints buttonPanelGBC = createSecondaryRowGBC(secondaryXCountForGBC++);
		step2and3Host.add(actionButtonsPanel, buttonPanelGBC);
		
		// Add the Step2 and Step3 Host
		GridBagConstraints step2and3HostGBC = createStandardMainRowGBC(mainYCountForGBC++, 0.0);
		getContentPane().add(step2and3Host, step2and3HostGBC);
		
		/* Setup the Results Information Panel */ 
		JPanel resultsPanel = createStandardBorderPanel("Results");
		
		// Create Hike Text Field
		JLabel resultsSelectedHikeLabel = createStandardResultsLabel("Hike Type");
		hikeTextField = createResultsTextField(10);
		resultsPanel.add(resultsSelectedHikeLabel);
		resultsPanel.add(hikeTextField);
		
		// Create Start/End Date Result Fields
		JLabel resultsStartDateLabel = createStandardResultsLabel("From");
		setStandardLabelPadding(resultsStartDateLabel);
		resultsStartDateTextField = createResultsDateField();
		JLabel resultsEndDateLabel = createStandardResultsLabel("to");
		resultsEndDateTextField = createResultsDateField();
		
		resultsPanel.add(resultsStartDateLabel);
		resultsPanel.add(resultsStartDateTextField);
		resultsPanel.add(resultsEndDateLabel);
		resultsPanel.add(resultsEndDateTextField);
		
		// Create Cost label and text field
		JLabel costDisplayLabel = createStandardResultsLabel("Cost");		
		setStandardLabelPadding(costDisplayLabel);
		costTextField = createResultsTextField(5);
		resultsPanel.add(costDisplayLabel);
		resultsPanel.add(costTextField);
		
		// Add results panel 
		GridBagConstraints informationPanelGBC = createStandardMainRowGBC(mainYCountForGBC++, 1.0); 
		getContentPane().add(resultsPanel, informationPanelGBC);
		
		// fit everything
		this.pack();
	}
	
	/**
	 * Create a standard readonly JTextField to be used for the Request Quote Results.  
	 * @param columns
	 * @return
	 */
	private static JTextField createResultsTextField(int columns) {
		JTextField resultsTextField = new JTextField("", columns);
		resultsTextField.setFont(RESULTS_FIELD_FONT);
		resultsTextField.setEditable(false);
		return resultsTextField;
	}

	/**
	 * Create a standard readonly date-formatted JFormattedTextField to be used for the Request Quote Results.
	 * @return
	 */
	private static JFormattedTextField createResultsDateField() {
		JFormattedTextField resultsDateFormattedTextField = new JFormattedTextField(new SimpleDateFormat("MM/dd/yyyy"));
		resultsDateFormattedTextField.setFont(RESULTS_FIELD_FONT);
		resultsDateFormattedTextField.setColumns(7);
		resultsDateFormattedTextField.setEditable(false);
		return resultsDateFormattedTextField;
	}

	/**
	 * Create a standard JLabel to be used for the Request Quote Results
	 * @param labelText
	 * @return
	 */
	private static JLabel createStandardResultsLabel(String labelText) {
		JLabel resultsLabel = createStandardFieldLabel(labelText);
		resultsLabel.setFont(RESULTS_LABEL_FONT);
		return resultsLabel;
	}

	/** 
	 * Create a JButton with request quote functionality.  Subscribes to action listener for button click
	 * @param monthComboBox
	 * @param dayComboBox
	 * @param yearComboBox
	 * @return
	 */
	private JButton createRequestQuoteButton(JComboBox<String> monthComboBox, JComboBox<Integer> dayComboBox, JComboBox<Integer> yearComboBox, JComboBox<Integer> durationComboBox) {
		JButton reqQuoteButton = new JButton("Request Quote");
		reqQuoteButton.setToolTipText("Get the cost of the tour based on the selected duration and start date.");
		reqQuoteButton.setEnabled(false);
		reqQuoteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				requestRate(yearComboBox, monthComboBox, dayComboBox, durationComboBox);
			}			
		});		
		
		ImageIcon moneyIcon = new ImageIcon(getClass().getResource(ResourceNames.MONEY_ICON));
		moneyIcon = new ImageIcon(moneyIcon.getImage().getScaledInstance(BTN_IMG_SIZE, BTN_IMG_SIZE, Image.SCALE_SMOOTH));
		reqQuoteButton.setIcon(moneyIcon);
		
		return reqQuoteButton;
	}

	/** 
	 * Create a JButton that will open the BHC website with more information.
	 * @return
	 */
	private static JButton createOpenWebsiteButton() {
		JButton openWebsiteButton = new JButton("View More Tour Details...");
		openWebsiteButton.setToolTipText("Open BHC Website with More Details and Tour Information");
		openWebsiteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					Utility.openWebpage(new URL(BHC_HOME_PAGE_URL));
				} catch (MalformedURLException e) {
					JOptionPane.showMessageDialog(null, "The URL for the BHC Website is malformed and not available at the moment", "Invalid URL", JOptionPane.ERROR_MESSAGE);
				}
			}			
		});
		
		ImageIcon infoIcon = (ImageIcon)UIManager.getIcon("OptionPane.informationIcon");  // This takes advantage of built-in Java icons 
		Image scaledInfoImage = infoIcon.getImage().getScaledInstance(BTN_IMG_SIZE, BTN_IMG_SIZE, Image.SCALE_SMOOTH);
		openWebsiteButton.setIcon(new ImageIcon(scaledInfoImage));
		
		return openWebsiteButton;
	}

	/** 
	 * Create an empty border around label to serve as "padding" from the component to its left.  Use this to separate the label from another field. 
	 * Logically associated fields should not use this (e.g. a label and its corresponding component should not use this) 
	 * @param label
	 */
	private static void setStandardLabelPadding(JLabel label) {
		EmptyBorder padding = new EmptyBorder(0, 40, 0, 0); 
		label.setBorder(padding);
	}

	/** 
	 * Create a standard FlowLayout JPanel with a titled border.  
	 * @param borderTitle
	 * @return
	 */
	private static JPanel createStandardBorderPanel(String borderTitle) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(MAIN_BACKGROUND_COLOR);
		Border border = BorderFactory.createEtchedBorder();
		panel.setBorder(BorderFactory.createTitledBorder(border, borderTitle, TitledBorder.LEFT, TitledBorder.CENTER));
		return panel;
	}
	
	/** 
	 * Create a Standard GridBagConstraints object for Main Rows of the View.  Calls into overloaded version of this method
	 * @param gridy
	 * @param weighty
	 * @return
	 */
	private static GridBagConstraints createStandardMainRowGBC(int gridy, double weighty) {
		return createStandardMainRowGBC(gridy, weighty, new Insets(0, 0, 0, 0));
	}
	
	/** 
	 * Create a Standard GridBagConstraints object for Main Rows of the View to make code more readable/reduce amount of boilerplate code.
	 * Assumes to contain multiple rows only for a GridBagLayout (and not multiple columns)
	 * @param gridy
	 * @param weighty
	 * @param insets
	 * @return
	 */
	private static GridBagConstraints createStandardMainRowGBC(int gridy, double weighty, Insets insets) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = gridy;
		gbc.weightx = 1.0;
		gbc.weighty = weighty;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = insets;
		return gbc;
	}
	
	/** 
	 * Used for secondary row GridBagConstraints that assumes to contain multiple columns only for a GridBagLayout. 
	 * @param gridx
	 * @return
	 */
	private static GridBagConstraints createSecondaryRowGBC(int gridx) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gridx;
		gbc.gridy = 0;
		gbc.weightx = 0.5;  
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.BOTH;
		return gbc;
	}

	/** 
	 * Create the Label for the Header
	 * @return
	 */
	private static JLabel createHeaderLabel() {
		JLabel headerLabel = new JLabel("Request a Quote", JLabel.LEFT);
		headerLabel.setFont(HEADER_FONT);
		headerLabel.setForeground(HEADER_TEXT_COLOR);
		return headerLabel;
	}

	/** 
	 * Create the Panel for the Header
	 * @return
	 */
	private static JPanel createHeaderPanel() {
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		headerPanel.setBackground(HEADER_BACKGROUND_COLOR);	
		headerPanel.setPreferredSize(new Dimension(PAGE_WIDTH, 40));
		return headerPanel;
	}	
	
	/** 
	 * Request a rate based on the information filled out.  Will determine the selected hike and then call into final requestRate method. 
	 * @param yearComboBox
	 * @param monthComboBox
	 * @param dayComboBox
	 * @param durationComboBox
	 */
	private void requestRate(JComboBox<Integer> yearComboBox, JComboBox<String> monthComboBox, JComboBox<Integer> dayComboBox, JComboBox<Integer> durationComboBox) {
		JRadioButton selectedButton = null;

		for(AbstractButton button : Collections.list(radioButtonGroup.getElements())) {
			if(button.isSelected()) {
				selectedButton = (JRadioButton)button; 
				break;
			}
		}
		
		// this should not happen based on Presentation logic of this JFrame, but as a safeguard, handle this case
		if(selectedButton == null) {
			JOptionPane.showMessageDialog(this, "There was no selected tour option.  Please select one to continue", "No Tour Selection", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		HikeOptionViewModel viewModel = buttonToViewModel.get(selectedButton);
		requestRate((int)yearComboBox.getSelectedItem(), monthComboBox.getSelectedIndex(), (int)dayComboBox.getSelectedItem(), (int)durationComboBox.getSelectedItem(), viewModel);
	}
		
	/** 
	 * Create a booking date and request a rate using that and the selected hike option view model. 
	 * Display the cost and other pertinent result related information to the user.
	 * If there is an error or problem calculating, then display that information to the user.
	 * @param year
	 * @param month
	 * @param day
	 * @param duration
	 * @param hikeOptionVM
	 */
	private void requestRate(int year, int month, int day, int duration, HikeOptionViewModel hikeOptionVM) {
		BookingDay bookingDate = new BookingDay(year, month + 1, day);
		clearResultsFields(); // clear results fields before each time
		if(bookingDate.isValidDate()) {
			Rates quoteHelper = new Rates(hikeOptionVM.getHikeType());
			quoteHelper.setBeginDate(bookingDate);
			quoteHelper.setDuration(duration);
			if(quoteHelper.isValidDates()) {
				hikeTextField.setText(hikeOptionVM.getDisplayName());
				costTextField.setText("$" + quoteHelper.getCost());
				resultsStartDateTextField.setValue(quoteHelper.getBeginDate().getTime());
				resultsEndDateTextField.setValue(quoteHelper.getEndDate().getTime());
			} else {
				JOptionPane.showMessageDialog(this, "The selected timeframe is invalid for the following reason: " + quoteHelper.getDetails(), 
						"Invalid Timeframe Selected", JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "The selected date is not a valid day of that month and/or year. Please choose another date.", 
					"Invalid Date Selected", JOptionPane.ERROR_MESSAGE);
		}
	}

	/** 
	 * Clear all the result related fields
	 */
	private void clearResultsFields() {
		costTextField.setText("");
		resultsStartDateTextField.setText("");
		resultsEndDateTextField.setText("");
		hikeTextField.setText("");
	}

	/** 
	 * Create a JComboBox filled with all the Months (via DateFormatSymbols .GetMonths())
	 * @return
	 */
	private static JComboBox<String> createMonthComboBox() {
		JComboBox<String> monthComboBox = new JComboBox<String>();
		DateFormatSymbols dfs = new DateFormatSymbols();
	
		for(String month : dfs.getMonths()) {
			if(month.trim().length() > 0)
				monthComboBox.addItem(month);
		}
		return monthComboBox;
	}
	
	/** 
	 * Create a JComboBox filled with 1-31 possible days.
	 * @return
	 */
	private static JComboBox<Integer> createDayComboBox() {
		JComboBox<Integer> dayComboBox = new JComboBox<Integer>();
		for(int dayIndex = 0; dayIndex < 31; dayIndex++) {
			dayComboBox.addItem(dayIndex + 1);
		}
		return dayComboBox;
	}
	
	/** 
	 * Create a JComboBox filled with the valid year range for booking.
	 * @return
	 */
	private static JComboBox<Integer> createYearComboBox() {
		JComboBox<Integer> yearComboBox = new JComboBox<Integer>();
		for(int yearIndex = VALID_START_YEAR; yearIndex < VALID_END_YEAR + 1; yearIndex++) {
			yearComboBox.addItem(yearIndex);
		}		
		return yearComboBox;
	}
		
	/**
	 * Create a standard JLabel that should be associated with a field.  
	 * @param text
	 * @return
	 */
	private static JLabel createStandardFieldLabel(String text) {
		JLabel label = new JLabel(text + ":");
		return label;		
	}
	
	/**
	 * Used to create a standard hike button and add it to required button group and view models 
	 * @param viewModel
	 * @param buttonGroup
	 * @param buttonToViewModel
	 * @return
	 */
	private JRadioButton createStandardHikeRadioButton(HikeOptionViewModel viewModel, ButtonGroup buttonGroup, 
			HashMap<JRadioButton, HikeOptionViewModel> buttonToViewModel) {
		JRadioButton radioButton = createStandardHikeRadioButton(viewModel);
		buttonGroup.add(radioButton);
		buttonToViewModel.put(radioButton, viewModel);
		return radioButton;
	}
	
	/** 
	 * Create a JRadioButton from a HikeOptionViewModel object, and wire up event listener.  
	 * @param viewModel
	 * @return
	 */
	private JRadioButton createStandardHikeRadioButton(HikeOptionViewModel viewModel) {
		Icon deselectedIcon = new ImageIcon(getClass().getResource(viewModel.getNormalIconFilePath()));
		Icon selectedIcon = new ImageIcon(getClass().getResource(viewModel.getBorderedIconFilePath()));
		JRadioButton radioButton = new JRadioButton(viewModel.getDisplayName(), deselectedIcon, false);
		radioButton.setHorizontalTextPosition(SwingConstants.CENTER);
		radioButton.setVerticalTextPosition(SwingConstants.TOP);
		radioButton.setSelectedIcon(selectedIcon);
		radioButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		radioButton.setBackground(MAIN_BACKGROUND_COLOR);
		radioButton.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent evt) {
				if(evt.getStateChange() == ItemEvent.SELECTED) {
					handleHikeRadioButtonSelected(viewModel.getHikeDurations());	
				}				
			}
		});
		return radioButton;
	}
	
	/**
	 * Enable the request quote button when a hike is selected.
	 * @param durationOptions
	 */
	private void handleHikeRadioButtonSelected(List<Integer> durationOptions) {
		requestQuoteButton.setEnabled(true);
		addDurationOptionsToList(durationOptions);
	}

	/** 
	 * Add duration options to the duration JComboBox
	 * @param durationOptions
	 */
	private void addDurationOptionsToList(List<Integer> durationOptions) {
		durationComboBox.removeAllItems();
		for(int durationOption : durationOptions) {
			durationComboBox.addItem(durationOption);
		}
	}			
		
	/**
	 * Starting point of GUI.  
	 * @param args
	 */
    public static void main(String args[])
    {
    	// Create an instance of the test application
		final BhcHikeFrame mainFrame = new BhcHikeFrame();
		Runnable showFrame = new Runnable() {
			public void run() {
			    mainFrame.setVisible(true);
			}
	    };
		SwingUtilities.invokeLater(showFrame);
    }
       
}
