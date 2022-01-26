package com.contactmanager.datamodel.singleitem;

public abstract class Item {
	public static final String DATA_TYPE_ID = "dataType";	
	public static final String LOADING_LOCATION = "externalLoading";

	public static final String IS_VISIBLE = "IsVisible";
	
	protected DataType dataType;
	
	//private static final String DATA_LABEL_ID = "dataLabel";
	
	protected static final String EMAIL_REGEX = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+";
	protected static final String PHONE_REGEX = "^(\\+\\d{1,3}\\s( )?)([0-9 ()-]+)$";
	//protected static final String NAME_REGEX = "[a-zA-Z']+";
	protected static final String DATE_REGEX = "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}";
	
	
	protected String dataId;
	

	
	protected String dataValue = "";
	protected String regex = null;
	
	protected ExternalLoading loadingLocation = null;
	
	public abstract String getDataValue(); 
	public abstract Boolean setValue(String value);

	public abstract DataType getDataType();
	public abstract ExternalLoading getExternalLoading();
}
