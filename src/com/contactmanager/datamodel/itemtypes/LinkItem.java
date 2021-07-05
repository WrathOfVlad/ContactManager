package com.contactmanager.datamodel.itemtypes;

import java.net.URL;
import java.util.Map;

public class LinkItem extends DefaultItem{

	
	public LinkItem(String dataId, Map<String, Object> metaData) throws Exception {
		super(dataId, metaData);
		this.dataType = DataType.LINK;
	}
	
	public Boolean setValue(String value) {
		if (value == null || value.equals("")) {this.dataValue = value; return true;};
		if(validateURL(value)) {
			this.dataValue = value;
			return true;
		}
		return false;
	}
	
	public static boolean validateURL(String url) {
		try {
            new URL(url).toURI();
            return true;
        }
        catch (Exception e) {
            return false;
        }
	}
}
