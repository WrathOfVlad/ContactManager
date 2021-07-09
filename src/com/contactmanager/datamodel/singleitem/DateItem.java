package com.contactmanager.datamodel.singleitem;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateItem extends DefaultItem{
	
	public static final String dateFormat = "yyyy-MM-dd";
	
	private static final String IS_REMINDER_FIELD = "isReminder";
	private static final String IS_YEAR_INCLUDED_FIELD = "isYearIncluded";
	private static final String NOTIFICATION_HEADER_FIELD = "notificationHeader";
	private static final String NOTIFICATION_CONTENT_FIELD = "notificationContent"; 
	
	private Boolean isReminder = false;
	private Boolean isYearIncludedInReminder = false;
	private String reminderHeader;
	private String reminderContent;
	
	public DateItem(String dataId,Map<String, Object> metaData) throws Exception {
		super(dataId,metaData);
		
		this.dataType = DataType.DATE;
		this.regex = DATE_REGEX;		
		
		if(!metaData.containsKey(IS_REMINDER_FIELD)) {return;}
		if(!metaData.containsKey(NOTIFICATION_HEADER_FIELD)) {throw new IOException("Notification Header is missing");}
		if(!metaData.containsKey(NOTIFICATION_CONTENT_FIELD)) {throw new IOException("Notification Content is missing");}
		
		this.isReminder = Boolean.parseBoolean(metaData.get(IS_REMINDER_FIELD).toString());
		this.reminderHeader = metaData.get(NOTIFICATION_HEADER_FIELD).toString();
		this.reminderContent = metaData.get(NOTIFICATION_CONTENT_FIELD).toString();
		
		if(!metaData.containsKey(IS_YEAR_INCLUDED_FIELD)) {return;}
		this.isYearIncludedInReminder = Boolean.parseBoolean(metaData.get(IS_YEAR_INCLUDED_FIELD).toString());
		
	}
	public Boolean setValue(String value) {
		if (value == null || value.equals("")) {this.dataValue = value; return true;};
		Pattern pattern = Pattern.compile(DATE_REGEX);
		Matcher matcher = pattern.matcher(value);
		
		if(!matcher.find()) return false;
		
		this.dataValue = value;
		return true;
	}
	
	public String[] isReminderToday() {
		if(!isReminder) return null;
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate today = LocalDate.now();
		LocalDate date;
		try{
			date = LocalDate.parse(dataValue, formatter);
		}
		catch (Exception e) {
			return null;
		}
		
		String dateAsString = date.getMonth() + "-" + date.getDayOfMonth();
		String todayAsString = today.getMonth() + "-" + today.getDayOfMonth();
		
		String[] titleAndContent = {this.reminderHeader,this.reminderContent};
		
		if(!isYearIncludedInReminder) {
			if (todayAsString.equals(dateAsString)) {
				return titleAndContent;
			}
		}
		else {
			if(date.equals(today)) {
				return titleAndContent;
			}
		}
		
		return null;

	}
}
