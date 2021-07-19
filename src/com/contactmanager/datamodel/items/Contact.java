package com.contactmanager.datamodel.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.contactmanager.datamodel.ItemsWrapper;
import com.contactmanager.datamodel.itemwiewers.ItemViews;
import com.contactmanager.datamodel.singleitem.DataType;
import com.contactmanager.datamodel.singleitem.Item;
import com.contactmanager.datamodel.singleitem.NameItem;
import com.contactmanager.utils.io.ConfigFileData;

public class Contact extends Items{

	private static final String FULL_NAME_LABEL = "Full Name";
	
	private String fullName;

	
	public Contact(Map<String, String> rowData, ItemsWrapper parent){
		if(!initializeItems(rowData,parent)) {return;};
		
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
	public Boolean saveToDataModel(ItemViews itemViews) {
		if(!super.saveToDataModel(itemViews)) {return false;}
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

	@Override
	public String[] getVisibleRowSpecific() {
		
		List<String> visibleCols = ConfigFileData.getInstance().getVisibleColumns();
		
		List<String> visibleRowContentList  = new ArrayList<>();
		for (int i=0;i<visibleCols.size();i++) {
			visibleRowContentList.add("");
		}
		
		if(visibleCols.contains(FULL_NAME_LABEL)) {
			visibleRowContentList.set(visibleCols.indexOf(FULL_NAME_LABEL),fullName);
			visibleCols.set(visibleCols.indexOf(FULL_NAME_LABEL),NAME_PLACEHOLDER);
		}
		
		getVisibleRow(visibleCols,visibleRowContentList);
		
		return visibleRowContentList.toArray(new String[visibleRowContentList.size()]);
	}
	

}
