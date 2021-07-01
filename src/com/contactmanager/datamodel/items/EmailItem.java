package com.contactmanager.datamodel.items;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailItem extends DataItemHandler{

	public EmailItem(String dataId, Map<String,Object> metaData) throws Exception {
		addGeneralInfo(dataId,metaData);
		this.dataType = DataType.EMAIL;
		this.regex = EMAIL_REGEX;
	}

	@Override
	public Boolean setValue(String value) {
		if (value == null || value.equals("")) {this.dataValue = value; return true;};
		Pattern pattern = Pattern.compile(EMAIL_REGEX);
		Matcher matcher = pattern.matcher(value);
		
		if(!matcher.find()) return false;
		
		this.dataValue = value;
		return true;
	}
}
