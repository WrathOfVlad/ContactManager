package com.contactmanager.datamodel.items;

import java.util.Map;

public class ComboBoxItem extends DataItemHandler{

	private String[] comboItems;
	
	public ComboBoxItem(String dataId, Map<String, Object> metaData) throws Exception {
		addGeneralInfo(dataId,metaData);
		
		this.dataType = DataType.COMBO;
		this.comboItems = metaData.get("comboItems").toString().split(",");
	}
	
	public Boolean setValue(String value) {
		this.dataValue = value;
		return true;
	}
	
	public String[] getComboItems() {
		return comboItems.clone();
	}
	
}
