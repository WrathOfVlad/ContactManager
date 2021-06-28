package com.contactmanager.utils.io;

import java.awt.Image;
import java.util.List;

import com.contactmanager.datamodel.Contact;

public interface DataStorageHandler {
	
	static final String MAX_ID_FORMATTING = "%05d";
	static final String ICON_PATH = "/images/icon.png";
	static final String NO_PROFILE_IMAGE_PATH = "/images/NoProfilePicture.png";
	static final String PATH_OF_PROGRAM = System.getProperty("user.dir");
	
	String getChildClassName();
	
	void setConfigFileDataPointer(ConfigFileData configFileData);
	void initialize();
	
	String getNotes(int id);
	void saveNotes(int id,String notes);
	
	List<String[]> getLogs(int id);
	void saveLogs(int id, List<String[]> logs);
	
	Image getProfileImage(int id);
	void saveProfileImage(int id, Image image);
	
	List<String[]> getContactData();
	void saveContactData(List<Contact> data);
	
	void createBackup(int id);
	void deleteOldBackups();

	
}