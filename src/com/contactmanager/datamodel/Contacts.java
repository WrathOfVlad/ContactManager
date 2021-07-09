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
import com.contactmanager.vew.MainFrame;

public class Contacts extends ItemsWrapper{
	
	public Contacts(DataStorageHandler dataStorage, MainFrame mainFrame) {
		this.dataStorage = dataStorage;
		this.mainFrame = mainFrame;
	}
	
	@Override
	public void save() {
		dataStorage.saveContactData(this);
	}
	
	@Override
	public void loadDataSpecific() {
		List<String[]> allContacts = dataStorage.loadContacts();
		loadData(allContacts);
	}
	
	@Override
	protected Items getSpecificItemWrapperClass(Map<String, String> dataMap) {
		return new Contact(dataMap);
	}
	
	public int getRowIndexById(int id) {
		return new ArrayList<Integer>(wrapperMap.keySet()).indexOf(id);
	}
	
	
	public void getReminders(){
		
		for (Items contact : wrapperMap.values()) {
			for (String dataId: ConfigFileData.getInstance().getColumns(false)) {
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
	protected void loadVisibleColumns() {
		visibleColumns = ConfigFileData.getInstance().getVisibleColumns();
		
	}
	
}

