package com.contactmanager.datamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.contactmanager.datamodel.items.Contact;
import com.contactmanager.datamodel.items.Items;
import com.contactmanager.datamodel.singleitem.DateItem;
import com.contactmanager.datamodel.singleitem.Item;
import com.contactmanager.utils.io.ConfigFileData;
import com.contactmanager.utils.io.DataStorageHandler;
import com.contactmanager.view.MainFrame;

public class Contacts extends ItemsWrapper{
	
	public Contacts(DataStorageHandler dataStorage, MainFrame mainFrame) {
		super(dataStorage);
		this.mainFrame = mainFrame;
	}
	
	@Override
	public void save() {
		dataStorage.saveContactData(this);
	}
	
	@Override
	public void loadDataSpecific() {
		List<String[]> allContacts = dataStorage.loadContacts();
		metaDataMap = ConfigFileData.getInstance().getItemMetaData();
		columns = new ArrayList<String>(metaDataMap.keySet());
		columns.add(0,Items.ID_FIELD);
		
		super.loadData(allContacts);
		
		
	}
	
	@Override
	public Items getSpecificItemWrapperClass(Map<String, String> dataMap) {
		return new Contact(dataMap,metaDataMap);
	}
	
	public int getRowIndexById(int id) {
		return new ArrayList<Integer>(wrapperMap.keySet()).indexOf(id);
	}
	
	
	public void getReminders(){
		
		for (Items contact : wrapperMap.values()) {
			for (String dataId: columns) {
				Item item = contact.getItemInfo(dataId);
				if(item instanceof DateItem) {
					String[] titleAndContent = ((DateItem) item).isReminderToday();
					if(titleAndContent != null) {
						
						mainFrame.sendReminder((Contact)contact,titleAndContent[0],titleAndContent[1]);
					}
				}
			}
		}
	}

	@Override
	public void loadVisibleColumns() {
		visibleColumns = ConfigFileData.getInstance().getVisibleColumns();
		
	}
	
}