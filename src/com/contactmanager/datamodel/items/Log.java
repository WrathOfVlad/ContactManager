package com.contactmanager.datamodel.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.contactmanager.utils.io.ConfigFileData;

public class Log extends Items{	
		
	public Log(Map<String, String> rowData,Map<String, Map<String, Object>> metaData){
		super(rowData,metaData);
		
	}

	@Override
	public String[] getVisibleRowSpecific() {
		List<String> visibleRow = new ArrayList<>();
		List<String> visibleCols = ConfigFileData.getInstance().getLogVisibleColumns();
		
		if(!visibleCols.contains(ID_FIELD)) {
			visibleCols.add(ID_FIELD);
		}
		
		for (int i=0;i<visibleCols.size();i++) {
			visibleRow.add("");
		}
		getVisibleRow(visibleCols,visibleRow);
 		return visibleRow.toArray(new String[visibleRow.size()]);
	}


}