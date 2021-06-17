package com.contactmanager.utils.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ini4j.Ini;
import org.ini4j.Wini;

import com.contactmanager.datamodel.Contact;

public class ConfigFileData {
	
	private static final String USER_SETTINGS = "USERSETTINGS";
	private static final String GENERAL = "GENERAL";
	private static final String VISIBLE_COLUMNS = "visibleColumns";
	private static final String STORING_TYPE = "storageType";
	private static final String PATH = "path";
	
	private static final String CONFIG_PATH = DataStorageHandler.BASE_PATH + "/config.ini";
	private static final String CONFIG_DEFAULT_PATH = "/configurations/config.ini";
	
	private Ini ini = new Ini();
	
	public ConfigFileData(){
		try {
			ini.load(new FileReader(CONFIG_PATH));
			
		} catch (IOException e) {
			try {
				InputStream iStream = getClass().getResource(CONFIG_DEFAULT_PATH).openStream();
				
				File configCopy = new File(CONFIG_PATH);
				Files.copy(iStream, configCopy.toPath(),StandardCopyOption.REPLACE_EXISTING);
				Wini iniStore = new Wini(configCopy);
				iniStore.put(GENERAL, PATH, DataStorageHandler.BASE_PATH + "/Data");
				iniStore.store();
				
				ini.load(configCopy);
				
			}
			catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public String getPath() {
		return ini.get(GENERAL,PATH);
	}
	
	public String getType() {
		return ini.get(GENERAL,STORING_TYPE);
	}
	
	public void saveVisibleColumns() {
		List<String> values = Contact.getVisibleColumns();
		String endValues = "";
		for (String value : values) {
			endValues += value;
			endValues += ",";
		}
		try {
			Wini iniStore = new Wini(new File(CONFIG_PATH));
			iniStore.put(USER_SETTINGS, VISIBLE_COLUMNS, endValues);
			iniStore.store();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	
	public void getVisibleColumns() {
		String visibleColumnsString = ini.get(USER_SETTINGS,VISIBLE_COLUMNS);
		String[] visibleColumnsArray = visibleColumnsString.split(",");
		
		List<String> visibleList = new ArrayList<>(Arrays.asList(visibleColumnsArray));
		if (!visibleList.contains("Id")) {
			visibleList.add(0, "Id");
		}
		
		Contact.setVisibleColumns(visibleList);
	}
}
