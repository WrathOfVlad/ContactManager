package com.contactmanager.utils.multiplatform;

import java.io.IOException;

import com.contactmanager.utils.io.DataStorageHandler;

public class LinuxSupport extends MultiPlatformSupportHandler{

	@Override
	public void sendNotification(String title, String message) {
		String path = getClass().getResource(DataStorageHandler.ICON_PATH).getPath();
		ProcessBuilder builder = new ProcessBuilder(
		        "notify-send",
		         title,
		        message, 
		        "-i",path);
		    try {
				builder.inheritIO().start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public void openLinkInBrowser(String url) throws Exception {
		Runtime rt = Runtime.getRuntime();

		String browser = "python -m webbrowser -n " + "\"" + url + "\"";
		rt.exec(new String[] { "sh", "-c", browser});
		/*
		String[] browsers = { "xdg-open", "firefox","google-chrome", "mozilla", "konqueror",
		             "netscape", "opera", "links", "lynx" };
		StringBuffer cmd = new StringBuffer();
		for (int i = 0; i < browsers.length; i++)
		    if(i == 0)
		        cmd.append(String.format(    "%s \"%s\"", browsers[i], url));
		    else
		        cmd.append(String.format(" || %s \"%s\"", browsers[i], url)); 
		    // If the first didn't work, try the next browser and so on

		*/
	}

}
