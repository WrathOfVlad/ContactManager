package com.contactmanager.datamodel.itemstypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.contactmanager.datamodel.itemtypes.DataType;
import com.contactmanager.datamodel.itemtypes.Item;
import com.contactmanager.datamodel.itemtypes.NameItem;
import com.contactmanager.utils.io.ConfigFileData;

public class Contact extends Items{
	
	private String fullName;

	private Map<String, Item> dataMap = new LinkedHashMap<String, Item>();
	
	public Contact(Map<String, String> rowData){
		if(!initializeItemWrapper(rowData)) {return;};
		
		setFullName();
		
	}
	
	@Override
	protected void initializeMetaData() {
		metaData = ConfigFileData.getInstance().getItemMetaData();
	}
	public List<String> getElementAsList(Boolean isFullNameIncluded){
		List<String> elementList = super.getElementsAsList();
		
		if(!isFullNameIncluded) {return elementList;};
		elementList.add(1,fullName);
		
		return elementList;
	}

	
	@Override
	public Boolean saveToDataModel() {
		if(!super.saveToDataModel()) {return false;}
		setFullName();
		return true;
	}
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName() {
		
		Map<Integer,Item> nameOrder = new HashMap<Integer,Item>();
		for (Item dataItem : dataMap.values()) {
			if(dataItem.getDataType() == DataType.NAME) {
				Integer order = ((NameItem)dataItem).getOrder();
				if(order != null) {
					nameOrder.put(order, dataItem);
				}
			}
		}
		List<Integer> sortedOrder =new ArrayList<Integer>(nameOrder.keySet());
		Collections.sort(sortedOrder);
		
		String fullNameTemp = "";
		for(int i = 0; i<sortedOrder.size();i++) {
			fullNameTemp += nameOrder.get(sortedOrder.get(i)).getDataValue() + " ";
		}
		this.fullName = fullNameTemp;

	}

}
