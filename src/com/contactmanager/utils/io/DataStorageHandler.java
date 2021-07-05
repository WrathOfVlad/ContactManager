package com.contactmanager.utils.io;

import java.awt.Image;
import java.util.List;

import com.contactmanager.datamodel.ItemsWrapper;

public abstract class DataStorageHandler {
	
	static final String MAX_ID_FORMATTING = "%05d";
	public static final String ICON_PATH = "/images/icon.png";
	public static final String NO_PROFILE_IMAGE_PATH = "/images/NoProfilePicture.png";
	public static final String[] IMAGE_EXTENSIONS = new String[] {"png","jpeg","jpg"};
	
	static final String PATH_OF_PROGRAM = System.getProperty("user.dir");	
	
	public abstract String getNotes(int id);
	public abstract void saveNotes(int id,String notes);
	
	public abstract void saveLogs(int id, ItemsWrapper logs);
	
	public abstract Image getProfileImage(int id);
	public abstract void saveProfileImage(int id, Image image);
	


	
	public abstract void createBackup(int id);
	public abstract void deleteOldBackups();

	public abstract List<String[]> loadContacts();
	public abstract List<String[]> loadLogs(int id);
	
	public abstract void saveContactData(ItemsWrapper data);

}