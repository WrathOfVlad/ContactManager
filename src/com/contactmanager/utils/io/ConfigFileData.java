
package com.contactmanager.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.contactmanager.datamodel.Contact;


public class ConfigFileData {
	private static ConfigFileData instance = null;
	
	//First Level Fields
	private static final String ITEM_META_DATA = "itemMetaData";
	private static final String USER_SETTINGS = "USER_SETTINGS";
	private static final String GENERAL = "GENERAL";
	
	//Second Level Fields
	private static final String VISIBLE_COLUMNS = "visibleColumns";
	private static final String STORING_TYPE = "storageType";
	private static final String PATH = "path";
	private static final String BACKUP_TIME_LIMIT = "backupTimeLimit";
	
	//path to config file
	private static final String CONFIG_PATH = DataStorageHandler.PATH_OF_PROGRAM + File.separator+"config.json";
	private static final String CONFIG_DEFAULT_PATH = File.separator +"configurations" + File.separator +"config.json";
	
	private JSONObject metaDataMap;
	private List<String> visibleColumns = new ArrayList<String>();
	private List<String> columns = new ArrayList<String>();
	
	
	@SuppressWarnings("unchecked")
	private ConfigFileData(){
		try {
			InputStream is = new FileInputStream(CONFIG_PATH);
			metaDataMap = loadMetaData(is);
			
		} catch (Exception e) {
			try {
				InputStream is = getClass().getResourceAsStream(CONFIG_DEFAULT_PATH);
				metaDataMap = loadMetaData(is);
	
				((JSONObject)metaDataMap.get(GENERAL)).put(PATH,DataStorageHandler.PATH_OF_PROGRAM + File.separator +"Data");
				
				FileWriter file = new FileWriter(CONFIG_PATH);
				file.write(metaDataMap.toJSONString());
				file.close();
				loadVisibleColumns();
				
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	public static ConfigFileData getInstance() {
		if(instance == null) {
			synchronized (ConfigFileData.class) {
				if(instance == null) {
					instance = new ConfigFileData();
				}
			}
		}
		return instance;
	}
	
	public String getPath() {
		return ((JSONObject)metaDataMap.get(GENERAL)).get(PATH).toString();
	}
	public String getType() {
		return ((JSONObject)metaDataMap.get(GENERAL)).get(STORING_TYPE).toString();
	}
	public int getBackupTimeLimit() {
		return Integer.parseInt(((JSONObject)metaDataMap.get(USER_SETTINGS)).get(BACKUP_TIME_LIMIT).toString());
	}
	
	public void setVisibleColumns(List<String> visibleColumns) {
		this.visibleColumns = visibleColumns;
	}
	@SuppressWarnings("unchecked")
	public void saveVisibleColumns() {
		String endValues = "";
		for (String value : visibleColumns) {
			endValues += value;
			endValues += ",";
		}
		try {
			((JSONObject)metaDataMap.get(USER_SETTINGS)).put(VISIBLE_COLUMNS,endValues);
			FileWriter file = new FileWriter(CONFIG_PATH);
			file.write(metaDataMap.toJSONString());
			file.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Map<String, Object>> getMetaData(){
		
		Map<String,Map<String, Object>> metaData = (HashMap<String, Map<String, Object>>) metaDataMap.get(ITEM_META_DATA);
		return metaData;
	}
	
	private JSONObject loadMetaData(InputStream is) {
		 
		 JSONParser parser = new JSONParser();
		 Object obj = null;
		try {
			obj = parser.parse(new java.io.InputStreamReader(is));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		 JSONObject jsonObject = (JSONObject) obj;
		 
		 return jsonObject;
	}
	
	private void loadVisibleColumns() {
		String visibleColumnsString = ((JSONObject)metaDataMap.get(USER_SETTINGS)).get(VISIBLE_COLUMNS).toString();
		String[] visibleColumnsArray = visibleColumnsString.split(",");
		
		List<String> visibleList = new ArrayList<>(Arrays.asList(visibleColumnsArray));
		if (!visibleList.contains(Contact.ID_FIELD)) {
			visibleList.add(0, Contact.ID_FIELD);
		}
		visibleColumns = visibleList;
	}
	
	public List<String> getVisibleColumns(){
		return visibleColumns;
	}

	public void setColumns(List<String> cols) {
		Map<String, Object> itemMetaData = getMetaData().get(ITEM_META_DATA);
		columns = new ArrayList<String>();
		for (String col : cols) {
			if(!itemMetaData.keySet().contains(col)) {continue;}
			
			columns.add(col);
		}
	}
	public List<String> getColumns(Boolean isIdIncluded){
		if(isIdIncluded) {
			ArrayList<String> tempCols = new ArrayList<String>(columns);
			tempCols.add(0,Contact.ID_FIELD);
			return tempCols;
		}
		return columns;
	}
}
