package com.contactmanager.datamodel.itemstypes;

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

}