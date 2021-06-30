package com.contactmanager.datamodel;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.contactmanager.utils.io.DataStorageFile;
import com.contactmanager.utils.io.DataStorageHandler;

public class CurrentContactInfo {
	private Logs logs = new Logs();
	private String notes;
	//private int id;
	private Image image;
	
	private Contact contact = new Contact(null);
	
	private Contacts pointerContacts;
	private DataStorageHandler pointerDataStorage;

	public CurrentContactInfo(Contacts contacts, DataStorageHandler dataStorageHandler) {
		this.pointerContacts = contacts;
		this.pointerDataStorage = dataStorageHandler;
	}
	
	public void clear(){
		logs = new Logs();
		notes = "";
		
		contact = new Contact(null);
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

	public Boolean fileExplorer(int id) {
		if(pointerDataStorage instanceof DataStorageFile) {
			DataStorageFile fileStorage = (DataStorageFile)pointerDataStorage;
			fileStorage.goToPath(id);
			return true;
		}
		else {
			return false;
		}
		
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public int addNewContact() {
		int id = pointerContacts.getNextId();
		contact.setId(id);
		pointerContacts.addToMaps(contact);
		return id;
	}
	public void loadContactInfo(int id) {
		logs.loadLogsFromFile(id);
		notes = pointerDataStorage.getNotes(id);
		contact = pointerContacts.getContactById(id);
		image = pointerDataStorage.getProfileImage(id);
	}
	public void save(int id) {
		pointerDataStorage.createBackup(id);
		
		pointerContacts.save();
		pointerDataStorage.saveLogs(id, logs.getLogsAsList());
		pointerDataStorage.saveNotes(id, notes);
		pointerDataStorage.saveProfileImage(id, image);
	}

	public void setImage(Image image) {
		this.image = image;
		
	}
	
}
