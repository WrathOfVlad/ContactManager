package com.contactmanager.entrypoint;


import java.io.IOException;

import org.ini4j.InvalidFileFormatException;

import com.contactmanager.datamodel.Contacts;
import com.contactmanager.datamodel.Logs;
import com.contactmanager.utils.io.ConfigFileData;
import com.contactmanager.utils.io.DataStorageFactory;
import com.contactmanager.utils.io.DataStorageHandler;
import com.contactmanager.utils.multiplatform.MultiPlatformSupportFactory;
import com.contactmanager.utils.multiplatform.MultiPlatformSupportHandler;
import com.contactmanager.vew.MainFrame;

public class Main 
{
	public static void main(String[] args) throws InvalidFileFormatException, IOException{
		
		ConfigFileData pointerConfigFileData = new ConfigFileData(); //get user configurations
		
		//set up factories
		DataStorageFactory pointerDataStorageFactory = new DataStorageFactory(); 
		MultiPlatformSupportFactory pointerMultiPlatformSupportFactory = new MultiPlatformSupportFactory();
		
		MultiPlatformSupportHandler pointerMultiPlatformSupport = pointerMultiPlatformSupportFactory.getSupportHandler();
		if(pointerMultiPlatformSupport == null) {
			System.out.println("Unsupported OS");
		}
		
		DataStorageHandler pointerDataStorage = pointerDataStorageFactory.getDataStorage(pointerConfigFileData);
		if(pointerDataStorage == null) {
			System.out.println("Invalid Storage Type");
			return;
		}
		
		
		pointerDataStorage.initialize();
		pointerConfigFileData.getVisibleColumns();
		
		
		Contacts pointerContacts = new Contacts(pointerDataStorage);
		pointerContacts.loadContacts();	
		Logs.setDataStorageHandler(pointerDataStorage);
		
		
		new MainFrame(pointerContacts, pointerDataStorage, pointerConfigFileData, pointerMultiPlatformSupport);

		pointerDataStorage.deleteOldBackups();
		
	}
}



