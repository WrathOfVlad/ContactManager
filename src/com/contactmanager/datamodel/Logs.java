package com.contactmanager.datamodel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
		super(dataStorageHandler);
		loadVisibleColumns();
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
		metaDataMap = ConfigFileData.getInstance().getLogsMetaData();
		columns = new ArrayList<String>(metaDataMap.keySet());
		loadData(allContacts);
	}

	@Override
	public Items getSpecificItemWrapperClass(Map<String, String> dataMap) {
		return new Log(dataMap,metaDataMap);
	}

	@Override
	public void save() {
		dataStorage.saveLogs(id, this);
	}


	public void clear() {
		id = null;
		wrapperMap = new LinkedHashMap<Integer, Items>();
		
	}
	@Override
	protected void loadVisibleColumns() {
		visibleColumns = ConfigFileData.getInstance().getLogVisibleColumns();
		
	}
	
}
