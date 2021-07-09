package com.contactmanager.datamodel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.contactmanager.datamodel.items.Items;
import com.contactmanager.datamodel.items.Log;
import com.contactmanager.datamodel.singleitem.DateItem;
import com.contactmanager.utils.io.ConfigFileData;
import com.contactmanager.utils.io.DataStorageHandler;

public class Logs extends ItemsWrapper{
	
	private Integer id;
	
	public Logs(DataStorageHandler dataStorageHandler) {
		dataStorage = dataStorageHandler;
	}
	
	
	public Log getLatestLog() {
		
		if(wrapperMap.isEmpty()) return null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateItem.dateFormat);
		Items maxLog = null;
		LocalDate maxDate = LocalDate.MIN;
		
		for (Items log : wrapperMap.values()) {
			try {
				LocalDate date = LocalDate.parse(log.getItemInfo(Items.ID_FIELD).getDataValue(), formatter);
				if(date.isAfter(maxDate)) {
					maxDate = date;
					maxLog = log;
				}
			}
			catch (Exception e) {	}
		}
		
		return (Log)maxLog;
		
	}

	public void loadLogs(int id) {
		this.id = id;
		loadDataSpecific();
	}
	@Override
	protected void loadDataSpecific() {
		List<String[]> allContacts = dataStorage.loadLogs(id);
		loadData(allContacts);
	}

	@Override
	protected Items getSpecificItemWrapperClass(Map<String, String> dataMap) {
		return new Log(dataMap);
	}

	@Override
	public void save() {
		dataStorage.saveLogs(id, this);
	}


	@Override
	protected void loadVisibleColumns() {
		visibleColumns = ConfigFileData.getInstance().getLogVisibleColumns();
		
	}
	
}
