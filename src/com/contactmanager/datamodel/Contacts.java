package com.contactmanager.datamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.contactmanager.datamodel.items.DataItemHandler;
import com.contactmanager.datamodel.items.DateItem;
import com.contactmanager.utils.io.ConfigFileData;
import com.contactmanager.utils.io.DataStorageHandler;
import com.contactmanager.vew.MainFrame;

public class Contacts {
	
	private List<Contact> contactList = new ArrayList<>();
	private Map<Integer, Contact> contactsMapById = new HashMap<Integer, Contact>();
	private List<Integer> allIds = new ArrayList<Integer>();
	
	private DataStorageHandler pointerDataStorage;
	private MainFrame mainFrame;
	
	public Contacts(DataStorageHandler dataStorage, MainFrame mainFrame) {
		pointerDataStorage = dataStorage;
		this.mainFrame = mainFrame;
	}
	
	public List<Integer> getIds(){
		return allIds;
	}
	public int getNextId() {
		return Collections.max(allIds) + 1;
	}
	
	public String[][] contactsAs2DArray(){
		List<String[]> contacts = new ArrayList<>();
		for (Contact contact : contactList) {
			List<String> elements = contact.getElementsAsList(true);
			contacts.add(elements.toArray(new String[elements.size()]));
		}
		String[][] contactsAsArray = new String[contacts.size()][];
		Iterator<String[]> iterator = contacts.iterator();
		
		int i = 0;
		while(iterator.hasNext()){
            contactsAsArray[i] = iterator.next();
            i++;
        }
		
		return contactsAsArray;
		
		
	}	
	
	public void addToMaps(Contact newContact) {
		contactList.add(newContact);
		
		int id = newContact.getIdAsInt();
		allIds.add(id);
		contactsMapById.put(id, newContact);

	}

	public Contact getContactById(int id) {
		return contactsMapById.get(id);
	}
	
	public void loadContacts() {
		List<String[]> allContacts = pointerDataStorage.getContactData();
		
		if (allContacts != null) {
			List<String> columns = new ArrayList<String>(Arrays.asList(allContacts.get(0)));
			
			for (Integer i=1; i < allContacts.size(); i++) {
				Map<String,String > dataMap = new HashMap<>();
				for (int j=0;j<columns.size();j++) {
					dataMap.put(columns.get(j),allContacts.get(i)[j]);
				}
				
				Contact newContact = new Contact(dataMap);
				addToMaps(newContact);
			}
		}
		
	}
	
	public void save() {
		pointerDataStorage.saveContactData(contactList);
	}
	
	public int getRowIndexById(int id) {
		return allIds.indexOf(id);
	}
	
	public void getReminders(){
		
		for (Contact contact : contactList) {
			for (String dataId: ConfigFileData.getInstance().getColumns(false)) {
				DataItemHandler item = contact.getItemInfo(dataId);
				if(item instanceof DateItem) {
					String[] titleAndContent = ((DateItem) item).isReminderToday();
					if(titleAndContent != null) {
						mainFrame.sendReminder(contact,titleAndContent[0],titleAndContent[1]);
					}
				}
			}
		}
	}
	
}

