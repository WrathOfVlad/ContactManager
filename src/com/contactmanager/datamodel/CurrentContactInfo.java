package com.contactmanager.datamodel;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.contactmanager.datamodel.items.Contact;
import com.contactmanager.utils.io.DataStorageFile;
import com.contactmanager.utils.io.DataStorageHandler;
import com.contactmanager.view.MainFrame;

public class CurrentContactInfo {
	
	private DataStorageHandler dataStorage;
	private Contacts contacts;

	
	private Logs logs = null;
	private String notes;
	private Image image;
	
	private Contact contact = null;
	
	private Map<String, Boolean> isChanged = new HashMap<String, Boolean>();
	

	public CurrentContactInfo(MainFrame mainFrame, DataStorageHandler dataStorageHandler) {
		this.contacts = new Contacts(dataStorageHandler,mainFrame);
		contacts.loadDataSpecific();
		
		this.dataStorage = dataStorageHandler;
		logs = new Logs(dataStorage);
		contact = (Contact) contacts.getSpecificItemWrapperClass(null);
		
		
		isChanged.put("notes", false);
		isChanged.put("contacts", false);
		isChanged.put("logs", false);
		isChanged.put("image", false);
		
		
	}
	
	public void clear(){
		
		notes = "";
		
		contact = (Contact) contacts.getSpecificItemWrapperClass(null);
		URL noImageStream = getClass().getResource(DataStorageHandler.NO_PROFILE_IMAGE_PATH);
		try {
			image = ImageIO.read(noImageStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Logs getLogs() {
		return logs;
	}
	public String getNotes() {
		return notes;
	}
	public Contact getContact() {
		return contact;
	}
	public Image getImage() {
		return image;
	}
	
	public Contacts getContacts() {
		return contacts;
	}
	
	public Boolean fileExplorer(int id) {
		if(dataStorage instanceof DataStorageFile) {
			DataStorageFile fileStorage = (DataStorageFile)dataStorage;
			fileStorage.goToPath(id);
			return true;
		}
		else {
			return false;
		}
		
	}

	public void setNotes(String notes) {
		this.notes = notes;
		isChanged.replace("notes", true);
		
	}
	
	public int addNewContact() {
		int id = contacts.getNextId();
		contact.setId(id);
		contacts.addToMaps(contact);
		return id;
	}
	public void loadContactInfo(int id) {
		logs.loadLogs(id);
		notes = dataStorage.getNotes(id);
		contact = (Contact) contacts.getWrapperById(id);
		image = dataStorage.getProfileImage(id);
	}
	public void save(int id) {
		dataStorage.createBackup(id);
		
		saveContacts();
		saveLogs(id);
		saveNotes(id);
		saveProfileImage(id);
	}
	
	private void saveContacts() {
		contacts.save();	
	}
	private void saveLogs(int id) {
		dataStorage.saveLogs(id, logs);
	}
	private void saveNotes(int id) {
		if(isChanged.get("notes")) {
			dataStorage.saveNotes(id, notes);
		}
	}
	private void saveProfileImage(int id) {
		if(isChanged.get("image")) {
			dataStorage.saveProfileImage(id, image);
		}
	}
	
	

	public void setImage(Image image) {
		this.image = image;
		isChanged.replace("image", true);
		
	}
	
}
