package com.contactmanager.datamodel.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.contactmanager.datamodel.ItemsWrapper;
import com.contactmanager.utils.io.ConfigFileData;

public class Log extends Items{	
	
	public Log(Map<String,String> rowData,ItemsWrapper parent) {
		initializeItems(rowData,parent);
	}
	
	@Override
	protected void initializeMetaData() {
		metaData = ConfigFileData.getInstance().getLogsMetaData();
	}
	

	@Override
	public String[] getVisibleRowSpecific() {
		List<String> visibleRow = new ArrayList<>();
		List<String> visibleCols = ConfigFileData.getInstance().getLogVisibleColumns();

		for (int i=0;i<visibleCols.size();i++) {
			visibleRow.add("");
		}
		getVisibleRow(visibleCols,visibleRow);
 		return visibleRow.toArray(new String[visibleRow.size()]);
	}

}