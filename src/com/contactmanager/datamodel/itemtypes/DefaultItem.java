package com.contactmanager.datamodel.itemtypes;

import java.util.Map;

public class DefaultItem extends Item{
	
	
	public DefaultItem(String dataId, Map<String, Object> metaData) throws Exception {
		addGeneralInfo(dataId,metaData);

		this.dataType = DataType.DEFAULT;
	}
	
	
	
	public String getDataValue() {
		return dataValue;
	}
	public DataType getDataType() {
		return dataType;
	}

	public ExternalLoading getExternalLoading() {
		return loadingLocation;
	}
	
	public static String getRegex(DataType dataType) {
		switch (dataType) {
		case DATE: {
			return DATE_REGEX;
		}
		case EMAIL:{
			return EMAIL_REGEX;
		}
		case PHONE:{
			return PHONE_REGEX;
		}
		default:
			return null;
		}
	}
	
	protected void addGeneralInfo(String dataId, Map<String, Object> metaData) {
		this.dataId = dataId;
		if(metaData.containsKey(LOADING_LOCATION)) {
			this.loadingLocation = ExternalLoading.valueOf(metaData.get(LOADING_LOCATION).toString());
		}
		if(metaData.containsKey(IS_VISIBLE)) {
			this.isVisibleInRow = Boolean.parseBoolean(metaData.get(IS_VISIBLE).toString());
		}
		
	}
	
	public Boolean setValue(String value) {
		this.dataValue = value;
		return true;
	};
	
	public Boolean getIsVisible() {
		return isVisibleInRow;
	}

	
}
