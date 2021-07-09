package com.contactmanager.datamodel.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.contactmanager.utils.io.ConfigFileData;

public class Log extends Items{	
	
	public Log(Map<String,String> rowData) {
		initializeItemWrapper(rowData);
	}
	
	@Override
	protected void initializeMetaData() {
		metaData = ConfigFileData.getInstance().getLogsMetaData();
	}
	

	@Override
	public String[] getVisibleRowSpecific() {
		List<String> visibleRow = new ArrayList<>();
		getVisibleRow(ConfigFileData.getInstance().getLogVisibleColumns(),visibleRow);
 		return visibleRow.toArray(new String[visibleRow.size()]);
	}

}