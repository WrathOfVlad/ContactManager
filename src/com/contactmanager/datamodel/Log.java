package com.contactmanager.datamodel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.JTextComponent;

import com.contactmanager.datamodel.items.DataItemHandler;
import com.contactmanager.datamodel.items.ItemFactory;
import com.contactmanager.utils.io.ConfigFileData;

public class Log {	
	public static final String LAST_DATE_FIELD = "Last Contact";
	
	private Map<String, DataItemHandler> dataMap = new LinkedHashMap<String, DataItemHandler>();
	
	
	
	public Log(Map<String,String> rowData) {
		Map<String, Map<String, Object>> metaData = ConfigFileData.getInstance().getLogsMetaData();
		
		ItemFactory itemFactory = new ItemFactory();
		
		for (String dataId : metaData.keySet()) {
			
			DataItemHandler dataItem = null;
			try {
				dataItem = itemFactory.getDataItem(dataId, metaData.get(dataId));
			} catch (Exception e) {
				e.printStackTrace();
			}
			addToMap(dataId, dataItem);
			
			if(rowData == null) {continue;};
			
			dataItem.setValue(rowData.get(dataId));
			
			
		}
	}
	public static List<String> getColumns() {
		List<String> columns = new ArrayList<String>(ConfigFileData.getInstance().getLogsMetaData().keySet());
		return columns;
	}
	
	private void addToMap(String dataId, DataItemHandler dataItem) {
		dataMap.put(dataId, dataItem);
	}
	
	public String[] getLog() {
		List<String> log = new ArrayList<>();
		for (String dataId : dataMap.keySet()) {
			log.add(dataMap.get(dataId).getDataValue());
		}
		return log.toArray(new String[log.size()]);
	}
	
	public DataItemHandler getItemInfo(String dataId) {
		return dataMap.get(dataId);
	}
	

	public void setValuesFromView(Map<String, JTextComponent> textFieldMap) {
		for (String dataId : textFieldMap.keySet()) {
			dataMap.get(dataId).setValue(textFieldMap.get(dataId).getText());
		}
		
	}
}