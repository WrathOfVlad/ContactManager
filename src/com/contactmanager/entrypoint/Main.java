package com.contactmanager.entrypoint;


import java.io.IOException;

import com.contactmanager.utils.io.DataStorageFactory;
import com.contactmanager.utils.io.DataStorageHandler;
import com.contactmanager.utils.multiplatform.MultiPlatformSupportFactory;
import com.contactmanager.utils.multiplatform.MultiPlatformSupportHandler;
import com.contactmanager.vew.MainFrame;

public class Main 
{
	public static void main(String[] args) throws IOException{
		
		//set up factories
		DataStorageFactory pointerDataStorageFactory = new DataStorageFactory();
		MultiPlatformSupportFactory pointerMultiPlatformSupportFactory = new MultiPlatformSupportFactory();
		
		MultiPlatformSupportHandler pointerMultiPlatformSupport = pointerMultiPlatformSupportFactory.getSupportHandler();
		if(pointerMultiPlatformSupport == null) {
			System.out.println("Unsupported OS");
		}
		
		DataStorageHandler pointerDataStorage = pointerDataStorageFactory.getDataStorage();
		if(pointerDataStorage == null) {
			System.out.println("Invalid Storage Type");
			return;
		}

		
		new MainFrame(pointerDataStorage, pointerMultiPlatformSupport);

		pointerDataStorage.deleteOldBackups();
		
	}
}



