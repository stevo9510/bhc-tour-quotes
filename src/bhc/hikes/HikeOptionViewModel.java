/**
 * Copyright 2018
 * Steven Anderson
 * All rights reserved
 * 
 * Homework 5 - BHC Hike Quotes
 * HikeOptionViewModel.java - View model used to encapsulate information about hike options for use in building GUI components and event handling
 * 06/30/2018
 */

package bhc.hikes;

import java.util.Collections;
import java.util.List;

import com.rbevans.bookingrate.Rates;
import com.rbevans.bookingrate.Rates.HIKE;

public class HikeOptionViewModel {

	private String displayName;

	private Rates.HIKE hikeType;
	
	private String normalIconFilePath;
	
	private String borderedIconFilePath;
		
	private final List<Integer> tourDurations;
	
	public HikeOptionViewModel(String displayName, HIKE hikeType, String normalIconFilePath, String borderedIconFilePath,
			List<Integer> tourDurations) {
		this.displayName = displayName;
		this.normalIconFilePath = normalIconFilePath;
		this.borderedIconFilePath = borderedIconFilePath;
		this.hikeType = hikeType;
		this.tourDurations = Collections.unmodifiableList(tourDurations);
	}

	public String getDisplayName() {
		return displayName;
	}

	public Rates.HIKE getHikeType() {
		return hikeType;
	}

	public String getNormalIconFilePath() {
		return normalIconFilePath;
	}

	public String getBorderedIconFilePath() {
		return borderedIconFilePath;
	}

	public List<Integer> getHikeDurations() {
		return tourDurations;
	}	
	
}
