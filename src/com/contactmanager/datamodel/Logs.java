package com.contactmanager.datamodel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.contactmanager.datamodel.items.DateItem;
import com.contactmanager.utils.io.DataStorageHandler;

public class Logs {
	
	//private List<Log> logList = new ArrayList<>();
	private Map<String,Log> logMap = new LinkedHashMap<>();
	
	private static DataStorageHandler pointerDataStorageHandler;
	
	public static void setDataStorageHandler(DataStorageHandler dataStorageHandler) {
		pointerDataStorageHandler = dataStorageHandler;
	}
	
	
	private void addToMap(Log log) {
		logMap.put(log.getItemInfo(Log.LAST_DATE_FIELD).getDataValue(), log);
	}
	
	public void addLog(Map<String, String> logData) {
		Log log = new Log(logData);
		addToMap(log);		
	}
	public void addLog(Log log) {
		addToMap(log);
	}
	
	public List<String[]> getLogsAsList() {	
		List<String[]> logs = new ArrayList<>();
		for (Log log : logMap.values()) {
			logs.add(log.getLog());
		}
		return logs;
	}
	
	public Log getLatestLog() {
		
		if(logMap.isEmpty()) return null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateItem.dateFormat);
				
		Log maxLog = null;
		LocalDate maxDate = LocalDate.MIN;
		
		for (Log log : logMap.values()) {
			try {
				LocalDate date = LocalDate.parse(log.getItemInfo(Log.LAST_DATE_FIELD).getDataValue(), formatter);
				if(date.isAfter(maxDate)) {
					maxDate = date;
					maxLog = log;
				}
			}
			catch (Exception e) {	}
		}
		
		return maxLog;
		
	}
	
	public void loadLogsFromFile(int id) {
		List<String[]> allLogs = pointerDataStorageHandler.getLogs(id);
		if(allLogs.isEmpty()) return;
		
		String[] columns = allLogs.get(0);		
		allLogs.remove(0);
		
		for (String[] log : allLogs) {
			Map<String,String> dataMap = new HashMap<>();
			
			for (int i = 0; i<columns.length;i++) {
				dataMap.put(columns[i], log[i]);
			}
			addLog(dataMap);
		}
	}

	public Log getLogFromDate(String date) {
		return logMap.get(date);
		
		
	}

	public int getRowIndexByDate(String date) {
		return (new ArrayList<>(logMap.keySet())).indexOf(date);
	}
	
	public void changeLog(String date,Log log) {
		logMap.replace(date, log);
	}
}
