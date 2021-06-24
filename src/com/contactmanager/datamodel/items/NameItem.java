package com.contactmanager.datamodel.items;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameItem extends DataItemHandler{

	private static final String ORDER_FIELD = "order";
	private Integer order = null;
	
	public NameItem(String dataId, Map<String, Object> metaData) throws Exception {
		addGeneralInfo(dataId,metaData);
		this.dataType = DataType.NAME;
		this.regex = NAME_REGEX;
		this.order = Integer.parseInt(metaData.get(ORDER_FIELD).toString());
	}
	
	
	public Boolean setValue(String value) {
		if (value == null) {this.dataValue = value; return true;};
		Pattern pattern = Pattern.compile(NAME_REGEX);
		Matcher matcher = pattern.matcher(value);
		
		if(!matcher.find()) return false;
		
		this.dataValue = value;
		return true;
	}
	
	public Integer getOrder() {
		return order;
	}

}
