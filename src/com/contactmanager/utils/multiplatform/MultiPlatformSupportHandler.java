package com.contactmanager.utils.multiplatform;

public abstract class MultiPlatformSupportHandler {	
	public abstract void sendNotification(String title, String message);
	public abstract void openLinkInBrowser(String url) throws Exception;
	
}
