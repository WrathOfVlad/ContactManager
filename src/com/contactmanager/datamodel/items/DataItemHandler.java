package com.contactmanager.datamodel.items;

import java.util.Map;

public abstract class DataItemHandler {
	public static final String DATA_TYPE_ID = "dataType";
	public static final String IS_EDITABLE_FIELD = "isEditable";
	public static final String DATA_LABEL_FIELD = "dataLabel";
	public static final String LOADING_LOCATION = "externalLoading";
	public static final String PLACEMENT_ON_DETAILS = "placementOnDetails";
	
	protected DataType dataType;
	
	
	//private static final String DATA_LABEL_ID = "dataLabel";
	
	protected static final String EMAIL_REGEX = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+";
	protected static final String PHONE_REGEX = "^(\\+\\d{1,3}\\s( )?)([0-9 ()-]+)$";
	protected static final String NAME_REGEX = "[a-zA-Z]+";
	protected static final String DATE_REGEX = "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}";
	protected static final String ID_REGEX = "\\d+";
	
	protected String dataId;
	protected String dataValue = "";
	protected String dataLabel;
	protected Boolean isEditable = true;
	protected ExternalLoading loadingLocation = null;
	protected Integer[] placement = new Integer[2];
	protected String regex = null;
	protected Integer maxValueLenght = 50; 
	
	public String getDataValue() {
		return dataValue;
	}
	public DataType getDataType() {
		return dataType;
	}

	public ExternalLoading getExternalLoading() {
		return loadingLocation;
	}

	
	protected void addGeneralInfo(String dataId, Map<String, Object> metaData) throws Exception {
		this.dataId = dataId;
		
		if(!metaData.containsKey(DATA_LABEL_FIELD)) {return;};
		if(!metaData.containsKey(PLACEMENT_ON_DETAILS)) {throw new Exception("Invalid Placement On Contact Detail");};
		
		this.dataLabel = metaData.get(DATA_LABEL_FIELD).toString();
		
		
		if(metaData.containsKey(IS_EDITABLE_FIELD)) {
			this.isEditable = Boolean.parseBoolean(metaData.get(IS_EDITABLE_FIELD).toString());
		}
		if(metaData.containsKey(LOADING_LOCATION)) {
			this.loadingLocation = ExternalLoading.valueOf(metaData.get(LOADING_LOCATION).toString());
		}
	}
	
	public abstract Boolean setValue(String value);
	
}
