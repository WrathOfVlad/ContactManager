package com.contactmanager.datamodel.items;

import java.util.Map;

public class DefaultItem extends DataItemHandler{

	
	public DefaultItem(String dataId, Map<String, Object> metaData) throws Exception {
		addGeneralInfo(dataId,metaData);
		
		this.dataType = DataType.DEFAULT;
	}
	
	public Boolean setValue(String value) {
		this.dataValue = value;
		return true;
	}
	
}
