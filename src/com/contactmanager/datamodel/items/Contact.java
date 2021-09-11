package com.contactmanager.datamodel.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.contactmanager.datamodel.singleitem.DataType;
import com.contactmanager.datamodel.singleitem.Item;
import com.contactmanager.datamodel.singleitem.NameItem;
import com.contactmanager.utils.io.ConfigFileData;
import com.contactmanager.view.itemwiewers.ItemViews;

public class Contact extends Items{

	private static final String FULL_NAME_LABEL = "Full Name";
	
	private String fullName;
	
	
	public Contact(Map<String, String> rowData,Map<String, Map<String, Object>> metaData){
		super(rowData,metaData);
				
		setFullName();
		
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
			visibleCols.set(visibleCols.indexOf(FULL_NAME_LABEL),FULL_NAME_FIELD);
		}
		
		getVisibleRow(visibleCols,visibleRowContentList);
		
		return visibleRowContentList.toArray(new String[visibleRowContentList.size()]);
	}


}
