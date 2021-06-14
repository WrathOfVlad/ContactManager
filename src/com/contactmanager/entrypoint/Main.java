package com.contactmanager.entrypoint;


import com.contactmanager.datamodel.Contacts;
import com.contactmanager.datamodel.Logs;
import com.contactmanager.utils.io.ConfigFileData;
import com.contactmanager.utils.io.DataStorageFactory;
import com.contactmanager.utils.io.DataStorageHandler;
import com.contactmanager.vew.MainFrame;



public class Main 
{
	public static void main(String[] args){
		//System.out.println(System.getProperty("user.dir"));
		DataStorageFactory pointerDataStorageFactory = new DataStorageFactory();
		ConfigFileData pointerConfigFileData = new ConfigFileData();
		

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
		
		
		new MainFrame(pointerContacts, pointerDataStorage, pointerConfigFileData);

		//mainFrame.setVisible(true);
		
	}
}



