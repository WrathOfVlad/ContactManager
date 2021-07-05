package com.contactmanager.datamodel.itemtypes;

import java.util.Map;

public class NameItem extends DefaultItem{

	private static final String ORDER_FIELD = "order";
	private Integer order = null;
	
	public NameItem(String dataId, Map<String, Object> metaData) throws Exception {
		super(dataId, metaData);
		
		this.dataType = DataType.NAME;
		this.order = Integer.parseInt(metaData.get(ORDER_FIELD).toString());
	}
	
	
	public Boolean setValue(String value) {
		this.dataValue = value;
		return true;
	}
	
	public Integer getOrder() {
		return order;
	}

}
