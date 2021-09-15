package com.contactmanager.entrypoint;

//import java.util.logging.Logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Handler implements Thread.UncaughtExceptionHandler{

	public static final  Logger LOGGER = LogManager.getLogger(Main.class.getName());
	
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		String errorTrace = e.getMessage()+ "\n";
		for(StackTraceElement stElement :e.getStackTrace()){
			errorTrace += stElement.toString() + "\n";
		}
		LOGGER.fatal(errorTrace);
		System.out.println(errorTrace);
		
	}

}
