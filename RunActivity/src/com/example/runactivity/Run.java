package com.example.runactivity;

import java.util.Date;


public class Run {
	private Date mStartDate;
	
	public Run() {
		mStartDate = new Date();
	}
	
	public Date getStartDate() {
		return mStartDate;
	}
	
	public void setStartDate(Date startDate) {
		mStartDate = startDate;
	}

	public int getDuration(long endMillis) {
		return (int) ((endMillis - mStartDate.getTime()) / 1000);
	}
	
	public static String formatDuration(int durationSeconds) {
		int seconds = durationSeconds % 60;
		int minutes = ((durationSeconds - seconds) / 60) % 60;
		int hours = (durationSeconds - (60 * minutes) - seconds) / 60*60;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
}
