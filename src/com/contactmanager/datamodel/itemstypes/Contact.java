package com.contactmanager.datamodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTextField;

import com.contactmanager.datamodel.items.DataItemHandler;
import com.contactmanager.datamodel.items.DataType;
import com.contactmanager.datamodel.items.ItemFactory;
import com.contactmanager.datamodel.items.NameItem;
import com.contactmanager.utils.io.ConfigFileData;

public class Contact {
	public static final String ID_FIELD = "Id";
	public static final String FULL_NAME_FIELD = "Full Name";
	
	private String id;
	private String fullName;

	private Map<String, DataItemHandler> dataMap = new LinkedHashMap<String, DataItemHandler>();
	
	public Contact(Map<String, String> rowData){
		if(rowData == null) {
			setId("");
			return;
		}
		setId(rowData.get(ID_FIELD));
		createContact();
		
		Map<String, DataItemHandler> tempDataMap = new HashMap<>(dataMap);
		for (String textField : rowData.keySet()) {
			if(textField.equals(ID_FIELD)) {continue;}
			tempDataMap.get(textField).setValue(rowData.get(textField));
		}
		dataMap = tempDataMap;
		setFullName();
		
	}
	private void createContact() {
		Map<String, Map<String, Object>> metaData = ConfigFileData.getInstance().getItemMetaData();
		ItemFactory itemFactory = new ItemFactory();
		for (String dataId : metaData.keySet()) {
			
			DataItemHandler dataItem = null;
			try {
				dataItem = itemFactory.getDataItem(dataId, metaData.get(dataId));
			} catch (Exception e) {
				e.printStackTrace();
			}			
			addToMap(dataId, dataItem);
		}
	}
	
	private void addToMap(String dataId,DataItemHandler dataItem) {
		dataMap.put(dataId, dataItem);
	}
	
	public List<String> getElementsAsList(boolean includeFullName) {
		List<String> listElements = new ArrayList<>();
		listElements.add(id);
		
		if (includeFullName) {
			listElements.add(fullName);
		}
		
		for (String dataId : ConfigFileData.getInstance().getColumns(false)) {
			listElements.add(dataMap.get(dataId).getDataValue());
		}
		return listElements;
	
	}

	

	public Boolean saveFromContactDetailView(Map<String, JTextField> textFieldMap) {
		if(dataMap.isEmpty()) {
			createContact();
		}
		Map<String, DataItemHandler> tempDataMap = new HashMap<>(dataMap);
		List<Boolean> isValidList = new ArrayList<>();
		for (String textField : textFieldMap.keySet()) {
			isValidList.add(tempDataMap.get(textField).setValue(textFieldMap.get(textField).getText()));
		}
		if(isValidList.contains(false)) {return false;}
		
		dataMap = tempDataMap;
		setFullName();
		return true;
	}

	public void loadContactDataFromDatamodel(Map<String, JTextField> textFieldMap) {
		
		for (String key : textFieldMap.keySet()) {
			DataItemHandler dataItem = dataMap.get(key);
			textFieldMap.get(key).setText(dataItem.getDataValue());
			//dataItem.setValue(textFieldMap.get(key).getText());
		}
		
	}
	
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName() {
		
		Map<Integer,DataItemHandler> nameOrder = new HashMap<Integer,DataItemHandler>();
		for (DataItemHandler dataItem : dataMap.values()) {
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

	public DataItemHandler getItemInfo(String dataId) {
		return dataMap.get(dataId);
	}
}
