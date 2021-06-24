package com.contactmanager.datamodel;

public class Log {
	private String date;
	private String type;
	private String nextTime;
	private String actions;
	private String status;
	private String notes;
	
	private static String[] columns = {"Date","Type","Next Time","Actions","Status","Notes"};
	
	public static String[] getColumnNames() {
		return columns;
	}
	
	public Log(String[] row) {
		date = row[0];
		type = row[1];
		nextTime = row[2];
		actions = row[3];
		status = row[4];
		notes = row[5];
	}
	public String[] getLog() {
		String[] log = {date,type,nextTime,actions,status,notes};
		return log;
	}
	
	public String getValue(String field) {
		if(field == "lastContact") {
			return date;
		}
		if(field == "nextContact") {
			return nextTime;
		}
		if(field == "contactStatus") {
			return status;
		}
		return null;
	}
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNextTime() {
		return nextTime;
	}
	public void setNextTime(String nextTime) {
		this.nextTime = nextTime;
	}
	public String getActions() {
		return actions;
	}
	public void setActions(String actions) {
		this.actions = actions;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
