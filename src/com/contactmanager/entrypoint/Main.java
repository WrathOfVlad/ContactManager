package com.contactmanager.entrypoint;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.contactmanager.utils.io.DataStorageFactory;
import com.contactmanager.utils.io.DataStorageHandler;
import com.contactmanager.utils.multiplatform.MultiPlatformSupportFactory;
import com.contactmanager.utils.multiplatform.MultiPlatformSupportHandler;
import com.contactmanager.view.MainFrame;


public class Main 
{
	public static final  Logger parentLogger = LogManager.getLogger(Main.class.getName());

	public static void main(String[] args) throws SecurityException, IOException{		
	
		try {
			
		
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
			
			parentLogger.info("successful setup");
		}
		catch (Exception e) {
			String fullException = "";
			for (StackTraceElement exception : e.getStackTrace()) {
				fullException += exception.toString() + " ";
			}
			parentLogger.fatal(fullException);
		}
	

		
	}
}



