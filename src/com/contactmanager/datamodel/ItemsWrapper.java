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
import com.contactmanager.vew.MainFrame;

public abstract class ItemsWrapper {
	
	protected Map<Integer, Items> wrapperMap = new LinkedHashMap<Integer, Items>();
	protected List<String> columns = new LinkedList<String>();
	
	protected List<String> visibleColumns = new ArrayList<>();
	
	protected DataStorageHandler dataStorage;
	protected MainFrame mainFrame;
	
	
	
	protected void addToMaps(Items newContact) {
		wrapperMap.put(newContact.getIdAsInt(),newContact);
	}
	
	protected abstract void loadDataSpecific();
	
	protected void loadData(List<String[]> allContacts) {
		if (allContacts != null && !allContacts.isEmpty()) {
			List<String> columns = new ArrayList<String>(Arrays.asList(allContacts.get(0)));
			this.columns = columns;
			
			for (Integer i=1; i < allContacts.size(); i++) {
				String[] row = allContacts.get(i);
				Map<String,String > dataMap = new HashMap<>();
				for (int j=0;j<columns.size();j++) {
					dataMap.put(columns.get(j),row[j]);
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
		List<List<String>> data = new ArrayList<>();
		for (Items wrapper : wrapperMap.values()) {
			data.add(wrapper.getElementsAsList());
		}
		return data;
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
