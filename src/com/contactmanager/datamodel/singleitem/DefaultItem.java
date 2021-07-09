package com.contactmanager.datamodel.singleitem;

import java.util.Map;

public class DefaultItem extends Item{
	
	
	public DefaultItem(String dataId, Map<String, Object> metaData) throws Exception {
		addGeneralInfo(dataId,metaData);

		this.dataType = DataType.DEFAULT;
	}
	
	
	@Override
	public DataType getDataType() {
		return dataType;
	}

	@Override
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
		
	}
	
	public Boolean setValue(String value) {
		this.dataValue = value;
		return true;
	};

	@Override
	public String getDataValue() {
		return this.dataValue;
	}
	@Override
	public Boolean setDataValue(String value) {
		this.dataValue = value;
		return true;
	}
}
