package com.contactmanager.utils.io;

import java.awt.Image;
import java.util.List;

import com.contactmanager.datamodel.Contact;

public abstract class DataStorageHandler {
	
	static final String MAX_ID_FORMATTING = "%05d";
	public static final String ICON_PATH = "/images/icon.png";
	public static final String NO_PROFILE_IMAGE_PATH = "/images/NoProfilePicture.png";
	static final String PATH_OF_PROGRAM = System.getProperty("user.dir");	
	
	public abstract String getNotes(int id);
	public abstract void saveNotes(int id,String notes);
	
	public abstract List<String[]> getLogs(int id);
	public abstract void saveLogs(int id, List<String[]> logs);
	
	public abstract Image getProfileImage(int id);
	public abstract void saveProfileImage(int id, Image image);
	
	public abstract List<String[]> getContactData();
	public abstract void saveContactData(List<Contact> data);
	
	public abstract void createBackup(int id);
	public abstract void deleteOldBackups();

	
}