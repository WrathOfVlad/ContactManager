package com.contactmanager.datamodel;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.contactmanager.utils.io.DataStorageHandler;

public class Contacts {
	
	private List<Contact> contactList = new ArrayList<>();
	private Map<Integer, Contact> contactsMapById = new HashMap<Integer, Contact>();
	private List<Integer> allIds = new ArrayList<Integer>();
	
	private DataStorageHandler pointerDataStorage;
	
	public Contacts(DataStorageHandler dataStorage) {
		pointerDataStorage = dataStorage;
	}
	
	public List<Integer> getIds(){
		return allIds;
	}
	
	public List<String[]> getVisibleData(){
		List<String[]> data = new ArrayList<>();
		for (Contact contact : contactList) {
			String[] contactData = null;
			data.add(contact.getElementsAsList(true).toArray(contactData));
		}
		return data;
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
	
	public Contact addNewContact() {
		int newId = Collections.max(allIds) + 1; //new id is one more than the highest one in the list
		
		Contact newContact = new Contact(null, null);
		newContact.setId(newId);
		
		addToMaps(newContact);
		return newContact;
	}
	
	public void setSpecificValue(int id, String varName, String varValue) {
		Contact getContact = getContactById(id);
		String methodName = "set" + varName.replaceAll("\\s", "");
		try {
			Method variableSetMethod = getContact.getClass().getDeclaredMethod(methodName, String.class);
			variableSetMethod.invoke(getContact, varValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
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
				Contact newContact = new Contact(allContacts.get(i), columns);
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
	
	public List<Contact> getReminders(String field){
		List<Contact> todayReminders = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate today = LocalDate.now();
		
		for (Contact contact : contactList) {
			try {
				if(field == Contact.BIRTHDAY_FIELD) {
				
					LocalDate date = LocalDate.parse(contact.getBirthday(), formatter);
					String dateAsString = date.getMonth() + "-" + date.getDayOfMonth();
					String todayAsString = today.getMonth() + "-" + today.getDayOfMonth();
					if (todayAsString.equals(dateAsString)) {
						todayReminders.add(contact);
					}
				}
				else if (field == Contact.NEXTCONTACT_FIELD) {
					LocalDate date = LocalDate.parse(contact.getNextContact(), formatter);
					if(date.equals(today)) {
						todayReminders.add(contact);
					}
				}
			}
					
			catch (Exception e) {
			}	
		}
		return todayReminders;
	}

}

