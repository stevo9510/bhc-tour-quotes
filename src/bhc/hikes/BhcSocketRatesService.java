/**
 * Copyright 2018
 * Steven Anderson
 * All rights reserved
 * 
 * Homework 6 - BHC Hike Quotes
 * BhcSocketRatesService.java - Socket webservice for retrieving BHC quote information. Extends RatesService interface.
 * 07/02/2018
 */

package bhc.hikes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.StringJoiner;

import com.rbevans.bookingrate.Rates;

/** 
 * 
 * @author sande107
 */
public class BhcSocketRatesService implements RatesService {
	private static final String HOST = "web7.jhuep.com";
	private static final int PORT = 20025;
	
	// The cost value when an invalid quote request is made.  Defined in assignment's business rules. 
	private static final double PROBLEM_COST = -0.01; 
	
	// Messages displayed when an invalid quote request is performed.  
	// These are used to help expose specific error details to consumers of this interface so that they can formulate more specific error messages to users.
	private static final String INVALID_DATE_MSG = "One of the dates was not a valid day";
	private static final String INVALID_SEASONAL_DATE_MSG = "begin or end date was out of season";
	
	// Delimiter for messages passed to and from service
	private static final String SERVICE_MSG_DELIMITER = ":";
	
	// HashMap to hold Hike enum to Integer ID mapping, per assignment's business rules.  
	private static HashMap<Rates.HIKE, Integer> hikeToIdMapping = new HashMap<Rates.HIKE, Integer>();

	// Fields for the begin date and duration.  These are passed from outside of this class.
	private int beginYear;
	private int beginMonth;
	private int beginDay;
	private int duration;
	
	// Encapsulated fields that are exposed to consumers via getters (cost information and request details)
	private double cost;
	private String details;
	
	public BhcSocketRatesService() {
		setDefaultValues();
		hikeToIdMapping.put(Rates.HIKE.GARDINER, 0);
		hikeToIdMapping.put(Rates.HIKE.HELLROARING, 1);
		hikeToIdMapping.put(Rates.HIKE.BEATEN, 2);
	}	

	private void setDefaultValues() {
		// reset these to prevent possibility of getting into a "bad state"
		cost = PROBLEM_COST; 
		details = null;
	}

	/**
	 * Request Quote Details from JHU Web Service.  This will populate fields that are exposed to consumers of this class.
	 */
	@Override
	public void requestQuoteDetails(Rates.HIKE hikeType, int year, int month, int day, int duration) {
		setDefaultValues();  // reset to default values to prevent getting into a valid state if exceptions are thrown
		this.beginYear = year;
		this.beginMonth = month;
		this.beginDay = day;
		this.duration = duration;
		
		// create a request parameter that will be passed to the service
		String requestParameter = createDelimitedParameter(hikeType, year, month, day, duration);
		
		String results = null;
		try {
			results = requestQuoteFromWebService(requestParameter);
		} catch (IOException e) {
			System.err.println(e);
		}
		
		parseResults(results);
	}

	/** 
	 * Perform a quote request from the webservice 
	 * @param requestParameter
	 * @return
	 * @throws IOException
	 */
	private String requestQuoteFromWebService(String requestParameter) throws IOException {
		Socket bhcSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            bhcSocket = new Socket(HOST, PORT);
            out = new PrintWriter(bhcSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(bhcSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + HOST);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + HOST);
        }
		
		out.println(requestParameter);
		
		String inLine;
		StringBuilder builder = new StringBuilder();
		while ((inLine=in.readLine()) != null) {
			builder.append(inLine);
		}
		
		out.close();
		in.close();
		bhcSocket.close();
		return builder.toString();
	}
	
	/**
	 * Parse results from the Web Service into fields exposed by this interface
	 * @param result
	 */
	private void parseResults(String result) {
		String[] results = result.split(SERVICE_MSG_DELIMITER);
		this.cost = Double.parseDouble(results[0]);
		this.details = results[1];
	}
	
	/** 
	 * Create a delimited parameter for use of passing to the web service.  The returned parameter will have this String form:     
	 * hike_id:begin_year:begin_month:begin_day:duration (e.g: 1:2008:7:1:3)
	 * @param hikeType
	 * @param year
	 * @param month
	 * @param day
	 * @param duration
	 * @return
	 */
	private String createDelimitedParameter(Rates.HIKE hikeType, int year, int month, int day, int duration) {
		Integer[] vals = new Integer[] { hikeToIdMapping.get(hikeType), year, month, day, duration };
		StringJoiner delimiterJoiner = new StringJoiner(SERVICE_MSG_DELIMITER);
		for(Integer val : vals) {
			delimiterJoiner.add(val.toString());
		}	
		
		return delimiterJoiner.toString();
	}

	@Override
	public double getCost() {
		return cost;
	}

	@Override
	public String getDetails() {
		return details;
	}

	/**
	 * Ensures not the "bad" cost value specified in assignment instructions
	 */
	@Override
	public boolean isValidCost() {
		// We'll follow the defined business rules and documentation of the webservice for this, even though something like cost >= 0 would probably work just fine.
		return getCost() != PROBLEM_COST;
	}

	/**
	 * Returns whether the details contain a message that the date is not real/invalid.
	 */
	@Override
	public boolean isInvalidDate() {
		return details != null && details.contains(INVALID_DATE_MSG);
	}

	/** 
	 * Returns whether the details contain a message that the date is not a valid season date.
	 */
	@Override
	public boolean isInvalidSeasonalDates() {
		return details != null && details.contains(INVALID_SEASONAL_DATE_MSG);
	}

	/** 
	 * Returns beginDate if it is a valid date.
	 */
	@Override
	public Date getBeginDate() {
		GregorianCalendar beginDate = new GregorianCalendar(this.beginYear, this.beginMonth - 1, this.beginDay);
		beginDate.setLenient(false);
		try {
			return beginDate.getTime();
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Returns the endDate if it is a valid date.
	 */
	@Override
	public Date getEndDate() {
		GregorianCalendar calendar = new GregorianCalendar(this.beginYear, this.beginMonth - 1, this.beginDay);
		calendar.setLenient(false);
		try {
			calendar.add(Calendar.DAY_OF_MONTH, duration - 1);
			Date endDate = calendar.getTime();
			return endDate;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
}
