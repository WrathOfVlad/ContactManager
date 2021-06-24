package com.contactmanager.utils.io;

public class DataStorageFactory {

	public DataStorageHandler getDataStorage() {
		DataStorageHandler dataStrorage;
		
		String type = ConfigFileData.getInstance().getType();
		if (type.equals("file") ) {
			dataStrorage = new DataStorageFile();
		}
		else {
			return null;
		}
		
		return dataStrorage;
	}
}
