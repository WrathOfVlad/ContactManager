package com.contactmanager.datamodel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.contactmanager.utils.io.DataStorageHandler;

public class Logs {
	
	private int id;
	private List<Log> logList = new ArrayList<>();
	
	private static DataStorageHandler pointerDataStorageHandler;
	
	public static void setDataStorageHandler(DataStorageHandler dataStorageHandler) {
		pointerDataStorageHandler = dataStorageHandler;
	}
	
	public Logs(int id) {
		this.id = id;
		loadLogsFromFile();
	}
	
	private void addToMap(Log log) {
		logList.add(log);
	}
	
	public void addLog(String[] logData) {
		Log log = new Log(logData);
		addToMap(log);		
	}
	
	public List<String[]> getLogs() {
		List<String[]> logs = new ArrayList<>();
		for (Log log : logList) {
			logs.add(log.getLog());
		}
		return logs;
	}
	
	public Log getLatestLog() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Log maxLog = logList.get(0);
		LocalDate maxDate = LocalDate.parse(maxLog.getDate(), formatter);
		
		for (Log log : logList) {
			try {
				LocalDate date = LocalDate.parse(log.getDate(), formatter);
				if(date.isAfter(maxDate)) {
					maxDate = date;
					maxLog = log;
				}
			}
			catch (Exception e) {
			}
			
		}
		
		return maxLog;
		
	}
	
	
	public void loadLogsFromFile() {
		List<String[]> allLogs = pointerDataStorageHandler.getLogs(id);
		for (String[] log : allLogs) {
			addLog(log);
		}
	}

	
	public void saveLogsToFile() {
		List<String[]> logs = getLogs();
		String[][] allLogs = new String[logs.size()][];
		
		for (int i = 0; i < allLogs.length; i++) {
			allLogs[i] = logs.get(i);
		}
		pointerDataStorageHandler.saveLogs(id, allLogs);
		
		
	}
	
}
