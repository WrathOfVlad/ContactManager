package com.contactmanager.entrypoint;

import java.io.IOException;

import com.contactmanager.utils.io.DataStorageFactory;
import com.contactmanager.utils.io.DataStorageHandler;
import com.contactmanager.utils.multiplatform.MultiPlatformSupportFactory;
import com.contactmanager.utils.multiplatform.MultiPlatformSupportHandler;
import com.contactmanager.view.MainFrame;


public class Main 
{
	public static final String VERSION = "2.5.3";

	
	public static void main(String[] args) throws SecurityException, IOException{		
		Handler globalExceptionHandler = new Handler();
        Thread.setDefaultUncaughtExceptionHandler(globalExceptionHandler);
        
		DataStorageFactory pointerDataStorageFactory = new DataStorageFactory();
		MultiPlatformSupportFactory pointerMultiPlatformSupportFactory = new MultiPlatformSupportFactory();
		
		MultiPlatformSupportHandler pointerMultiPlatformSupport = pointerMultiPlatformSupportFactory.getSupportHandler();
		if(pointerMultiPlatformSupport == null) {
			System.out.println("Unsupported OS");
			Handler.LOGGER.fatal("Unsupported OS");
		}
		
		DataStorageHandler pointerDataStorage = pointerDataStorageFactory.getDataStorage();
		if(pointerDataStorage == null) {
			System.out.println("Invalid Storage Type");
			Handler.LOGGER.fatal("Invalid Storage Type");
			return;
		}

		new MainFrame(pointerDataStorage, pointerMultiPlatformSupport);
		
		pointerDataStorage.deleteOldBackups();
		
		Handler.LOGGER.info("successful setup for version " + VERSION);

	

		
	}
}



