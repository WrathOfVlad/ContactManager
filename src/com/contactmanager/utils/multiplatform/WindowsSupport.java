package com.contactmanager.utils.multiplatform;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.net.URI;

import javax.imageio.ImageIO;

import com.contactmanager.utils.io.DataStorageHandler;

public class WindowsSupport extends MultiPlatformSupportHandler{

	@Override
	public void sendNotification(String title, String message) {
		Image image = null;
		try {
			image = ImageIO.read(getClass().getResource(DataStorageHandler.ICON_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
		    SystemTray tray = SystemTray.getSystemTray();

		    TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
		    trayIcon.setImageAutoSize(true);
		    try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				e.printStackTrace();
			}

		    trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
	}

	@Override
	public void openLinkInBrowser(String url) throws Exception {
		Desktop desktop = Desktop.getDesktop();
		desktop.browse(URI.create(url));
	}

}
