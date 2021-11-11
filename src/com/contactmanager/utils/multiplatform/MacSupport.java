package com.contactmanager.utils.multiplatform;

import java.io.IOException;

public class MacSupport extends MultiPlatformSupportHandler{

	@Override
	public void sendNotification(String title, String message) {
		ProcessBuilder builder = new ProcessBuilder(
		        "osascript", "-e",
		        "display notification \"" + message + "\""
		            + " with title \"" + title + "\"");
		    try {
				builder.inheritIO().start();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void openLinkInBrowser(String url) throws Exception {
		Runtime rt = Runtime.getRuntime();
		rt.exec("open " + url);
	}

}
