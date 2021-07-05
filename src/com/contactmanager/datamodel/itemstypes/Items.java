package com.contactmanager.datamodel.itemstypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.contactmanager.datamodel.itemtypes.Item;
import com.contactmanager.datamodel.itemtypes.ItemFactory;

public abstract class Items {
	public static final String ID_FIELD = "Id";
	
	protected String id;

	protected Map<String, Item> dataMap = new LinkedHashMap<String, Item>();
	protected Map<String, Map<String, Object>> metaData; 
	
	protected abstract void initializeMetaData();
	
	protected Boolean initializeItemWrapper(Map<String, String> rowData) {
		initializeMetaData();
		initializeDataMap();
		if(rowData == null) {
			setId("");
			return false;
		}
		setId(rowData.get(ID_FIELD));

		for (String textField : rowData.keySet()) {
			if(textField.equals(ID_FIELD)) {continue;}
			dataMap.get(textField).setValue(rowData.get(textField));
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

	public Boolean saveToDataModel() {
		if(dataMap.isEmpty()) {
			initializeDataMap();
		}
		Map<String, Item> tempDataMap = new HashMap<>(dataMap);
		List<Boolean> isValidList = new ArrayList<>();
		for (String dataId : dataMap.keySet()) {
			Item item = tempDataMap.get(dataId);
			isValidList.add(item.setValue(item.getTextFieldText()));
			
		}
		if(isValidList.contains(false)) {return false;}
		
		dataMap = tempDataMap;
		return true;
	}
	public void loadFromDataModel() {
		for (String key : dataMap.keySet()) {
			Item dataItem = dataMap.get(key);
			dataItem.setTextFieldText(dataItem.getDataValue());
		}
	}
	
	public List<String> getElementsAsList() {
		List<String> listElements = new ArrayList<>();
		listElements.add(id);
		
		for (String dataId : dataMap.keySet()) {
			listElements.add(dataMap.get(dataId).getDataValue());
		}
		return listElements;
	}
	
	public String[] getVisibleRow(){
		List<String> visibleRow = new ArrayList<>();
		visibleRow.add(getIdAsString());
		for(Item item : dataMap.values()) {
			if(item.getIsVisible()) {
				visibleRow.add(item.getDataValue());
			}
		}
		return visibleRow.toArray(new String[visibleRow.size()]);
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

	public void displayItems(JPanel panel, List<Map<Integer,JComponent>> tabOrder) {
		for (Item item : dataMap.values()) {
			item.putItemOnPanel(panel, tabOrder);
		}
	}
}
