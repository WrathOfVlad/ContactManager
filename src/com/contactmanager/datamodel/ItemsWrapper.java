package com.contactmanager.datamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.contactmanager.datamodel.items.Items;
import com.contactmanager.utils.io.DataStorageHandler;
import com.contactmanager.view.MainFrame;

public abstract class ItemsWrapper {
	
	protected Map<Integer, Items> wrapperMap = new LinkedHashMap<Integer, Items>();
	
	
	protected List<String> columns = new LinkedList<String>();
	protected Map<String, Map<String, Object>> metaDataMap = new HashMap<>();
	
	protected List<String> visibleColumns = new ArrayList<>();
	
	protected DataStorageHandler dataStorage;
	protected MainFrame mainFrame;
	
	protected ItemsWrapper(DataStorageHandler dataStorage) {
		this.dataStorage = dataStorage;
	}
	
	
	protected void addToMaps(Items newContact) {
		wrapperMap.put(newContact.getIdAsInt(),newContact);
	}
	
	protected abstract void loadDataSpecific();
	
	private String[] loadSingleElement(Integer j, List<String> tempCols, String[] row) {
		
		String[] output = new String[2];
		
		if(tempCols.contains(columns.get(j))) {
			int tempIndex = tempCols.indexOf(columns.get(j));
			output[0] = tempCols.get(tempIndex);
			output[1] = "";
			
			if(row.length > tempIndex) {
				output[1] = row[tempIndex];
			}
			
		}
		else {
			output[0] = columns.get(j);
			output[1] = "";
		}
		
		return output;
		
	}
	
	protected void loadData(List<String[]> allContacts) {
		wrapperMap = new LinkedHashMap<Integer, Items>();
		
		if (allContacts != null && !allContacts.isEmpty()) {
			List<String> tempCols = new ArrayList<String>(Arrays.asList(allContacts.get(0)));
			
			for (Integer i=1; i < allContacts.size(); i++) {
				String[] row = allContacts.get(i);
				Map<String,String > dataMap = new HashMap<>();
				
				for (int j=0;j<columns.size();j++) {
					
					String[] element = loadSingleElement(j, tempCols,row);
					dataMap.put(element[0], element[1]);
					
				}
				
				Items newContact = getSpecificItemWrapperClass(dataMap);
				addToMaps(newContact);
			}
			
		}
		loadVisibleColumns();
	}
	  
	protected abstract void loadVisibleColumns();
	
	protected abstract Items getSpecificItemWrapperClass(Map<String, String> dataMap);
	
	public void addItem(Items items) {
		items.setId(getNextId());
		addToMaps(items);
	}
	
	public List<String> getVisibleColumns(){
		return new ArrayList<String>(visibleColumns);
	}
	public List<String> columnList(){
		return new ArrayList<>(columns);
	}
	public int getIndexById(int id) {
		return (new ArrayList<Integer>(wrapperMap.keySet())).indexOf(id);
	}
	public Items getWrapperById(Integer id) {
		return wrapperMap.get(id);
	}
	public List<List<String>> getAllData(){
		List<List<String>> dataList = new ArrayList<>();
		
		for (Items wrapper : wrapperMap.values()) {
			Map<String, String> dataMap = wrapper.getElementsAsMap();
			List<String> tempList = new ArrayList<String>();
			for(Integer i = 0; i<dataMap.size();i++) {
				if(columns.get(i).equals(Items.ID_FIELD)) {
					tempList.add(wrapper.getIdAsString());
				}
				else {
					tempList.add(dataMap.get(columns.get(i)));
				}
				
			}
			dataList.add(tempList);
		}
		return dataList;
	}
	public List<Integer> getIds(){
		return new ArrayList<Integer>(wrapperMap.keySet());
	}
	public int getNextId() {
		if (wrapperMap.isEmpty()) {return 1;};
		
		return Collections.max(wrapperMap.keySet()) + 1;
	}
	
	public abstract void save();
}
