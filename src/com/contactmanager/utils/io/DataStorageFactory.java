package com.contactmanager.utils.io;

public class DataStorageFactory {

	public DataStorageHandler getDataStorage(ConfigFileData configFileData) {
		DataStorageHandler dataStrorage;
		
		String type = configFileData.getType();
		if (type.equals("file") ) {
			dataStrorage = new DataStorageFile();
		}
		else {
			return null;
		}
		
		dataStrorage.setConfigFileDataPointer(configFileData);
		return dataStrorage;
	}
}
