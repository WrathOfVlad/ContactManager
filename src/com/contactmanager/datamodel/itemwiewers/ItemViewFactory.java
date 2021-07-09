package com.contactmanager.datamodel.itemwiewers;

import java.util.Map;

import com.contactmanager.datamodel.singleitem.DataType;
import com.contactmanager.datamodel.singleitem.Item;

public class ItemViewFactory {
	
	public ItemView getItemView(String dataId, Map<String, Object> metaDataMap) throws Exception {
		
		DataType dataType = DataType.valueOf(metaDataMap.get(Item.DATA_TYPE_ID).toString());
		switch (dataType) {
		case LINK: {
			return new LinkView(dataId,metaDataMap);
		}
		case DATE:{
			return new DateView(dataId, metaDataMap);
		}
		case COMBO:{
			return new ComboBoxView(dataId, metaDataMap);
		}
		default:
			return new DefaultView(dataId, metaDataMap);
		}
	}
}
