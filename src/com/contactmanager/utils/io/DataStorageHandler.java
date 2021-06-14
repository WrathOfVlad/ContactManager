package com.contactmanager.utils.io;

import java.awt.Image;
import java.util.List;

import com.contactmanager.datamodel.Contact;

public interface DataStorageHandler {
	
	public static String MAX_ID_FORMATTING = "%05d";
	
	public String getChildClassName();
	
	public void setConfigFileDataPointer(ConfigFileData configFileData);
	public void initialize();
	
	public String getNotes(int id);
	public void saveNotes(int id,String notes);
	
	public List<String[]> getLogs(int id);
	public void saveLogs(int id, String[][] logs);
	
	public Image getProfileImage(int id);
	public void saveProfileImage(int id, Image image);
	
	public List<String[]> getContactData();
	public void saveContactData(List<Contact> data);
}
