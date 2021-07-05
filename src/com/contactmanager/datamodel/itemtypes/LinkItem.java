package com.contactmanager.datamodel.items;

import java.net.URL;
import java.util.Map;

public class LinkItem extends DataItemHandler{

	public LinkItem(String dataId, Map<String, Object> metaData) throws Exception {
		addGeneralInfo(dataId,metaData);
		this.dataType = DataType.LINK;
		this.maxValueLenght = 150;
	}
	
	public Boolean setValue(String value) {
		if (value == null || value.equals("")) {this.dataValue = value; return true;};
		try {
            new URL(value).toURI();
            this.dataValue = value;
            return true;
        }
        catch (Exception e) {
            return false;
        }
	}
}
