package com.contactmanager.datamodel.itemtypes;

import java.util.Map;

public class ComboBoxItem extends DefaultItem{
	
	public ComboBoxItem(String dataId, Map<String,Object> metaData) throws Exception {
		super(dataId, metaData);
		this.dataType = DataType.COMBO;
	}
	

	@Override
	public Boolean setValue(String value) {
		dataValue = value;
		return true;
	}

}
