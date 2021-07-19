package com.contactmanager.datamodel.items;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.contactmanager.datamodel.ItemsWrapper;
import com.contactmanager.datamodel.Logs;
import com.contactmanager.datamodel.itemwiewers.ItemViews;
import com.contactmanager.datamodel.singleitem.ExternalLoading;
import com.contactmanager.datamodel.singleitem.Item;
import com.contactmanager.datamodel.singleitem.ItemFactory;

public abstract class Items {
	public static final String ID_FIELD = "Id";
	public static final String NAME_PLACEHOLDER = "pLaCdehoewledr";
	
	protected String id = null;

	protected Map<String, Item> dataMap = new LinkedHashMap<String, Item>();
	protected Map<String, Map<String, Object>> metaData; 
	
	protected ItemsWrapper itemsWrapper;
	
	protected abstract void initializeMetaData();
	
	protected Boolean initializeItems(Map<String, String> rowData, ItemsWrapper parent) {
		itemsWrapper = parent;
		initializeMetaData();
		initializeDataMap();
		if(rowData == null) {
			setId("");
			return false;
		}
		setId(rowData.get(ID_FIELD));

		for (String textField : rowData.keySet()) {
			if(textField.equals(ID_FIELD)) {continue;}
			dataMap.get(textField).setDataValue(rowData.get(textField));
		}
		return true; 
	}
	
	protected void initializeDataMap() {
		ItemFactory itemFactory = new ItemFactory();
		for (String dataId : metaData.keySet()) {
			
			Item dataItem = null;
			try {
				dataItem = itemFactory.getDataItem(dataId, metaData.get(dataId));
			} catch (Exception e) {
				e.printStackTrace();
			}			
			addToMap(dataId, dataItem);
		}
	}
	protected void addToMap(String dataId,Item dataItem) {
		dataMap.put(dataId, dataItem);
	}

	public Boolean saveToDataModel(ItemViews itemViews) {
		Map<String, Item> tempDataMap = new LinkedHashMap<String,Item>(dataMap);
		if(itemViews.saveToDataModel(tempDataMap,this)) {
			dataMap = tempDataMap;
			return true;
			
		}
		return false;
	}
	public void getFromExternalLocation(ItemViews itemViews,ItemsWrapper items) {
		for (String field : dataMap.keySet()) {
			Item item = dataMap.get(field);
			if(item.getExternalLoading() == ExternalLoading.LOGS) {
				Log latestLog = ((Logs)items).getLatestLog(); 
				if(latestLog == null) {continue;}
				
				itemViews.getItemView(field).setTextFieldText(latestLog.getItemInfo(field).getDataValue());
			}
		}
		saveToDataModel(itemViews);
	}
	
	public void loadFromDataModel(ItemViews itemViews) {
		itemViews.loadFromDataModel(this);
	}
	
	public List<String> getElementsAsList() {
		List<String> listElements = new ArrayList<>();
		//listElements.add(id);
		
		for (String dataId : itemsWrapper.columnList()) {
			if(!dataMap.containsKey(dataId)) {continue;};
			
			listElements.add(dataMap.get(dataId).getDataValue());
		}
		return listElements;
	}
	
	protected List<String> getVisibleRow(List<String> visibleCols, List<String>visibleRow){
		for(int i =0; i<visibleCols.size();i++ ) {
			if(visibleCols.get(i).equals(ID_FIELD)) {
				visibleRow.set(i,id);
			}
			else if(visibleCols.get(i).equals(NAME_PLACEHOLDER)) {	}
			else {
				visibleRow.set(i,dataMap.get(visibleCols.get(i)).getDataValue());
			}
		}
		return visibleRow;
	}
	
	
	public List<String> getItemIds(){
		return new ArrayList<String>(dataMap.keySet());
	}
	public Item getItemInfo(String dataId) {
		return dataMap.get(dataId);
	}
	public int getIdAsInt() {
		return Integer.parseInt(this.id);
	}
	public String getIdAsString() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setId(int id) {
		this.id = String.format("%05d", id);
	}

	public abstract String[] getVisibleRowSpecific();

}
