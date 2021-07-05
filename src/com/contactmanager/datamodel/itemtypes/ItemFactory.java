package com.contactmanager.datamodel.itemtypes;

import java.util.Map;

public class ItemFactory {
	
	public Item getDataItem( String dataId,Map<String, Object> map ) throws Exception{
		
		String errorMsg = String.format("Data Type is missing for field %s", dataId);
		if(!map.containsKey(Item.DATA_TYPE_ID)) {throw new Exception(errorMsg);};
		
		String dataTypeString = map.get(Item.DATA_TYPE_ID).toString();
		DataType dataType = DataType.valueOf(dataTypeString);
		
		
		switch (dataType) {
		case EMAIL: {
			return new EmailItem(dataId,map);			
		}
		case LINK: {
			return new LinkItem(dataId,map);	
		}
		case DATE: {
			return new DateItem(dataId,map);
		}
		case NAME: {
			return new NameItem(dataId,map);			
		}
		case PHONE: {
			return new PhoneItem(dataId,map);
		}
		case COMBO:{
			return new ComboBoxItem(dataId, map);
		}
		case DEFAULT: {
			return new DefaultItem(dataId,map);
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + map);
		}
	}
}
