package com.contactmanager.vew;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.contactmanager.datamodel.Contact;
import com.contactmanager.datamodel.Contacts;
import com.contactmanager.utils.io.ConfigFileData;
import com.contactmanager.utils.io.DataStorageHandler;


public class MainFrame extends JFrame {
	
	public static final String CONTACT_LIST = "ContactList";
	public static final String CONTACT_DETAIL = "ContactDetail";
	public static final String CONTACT_LOG = "ContactLog";
	public static final String SETTINGS = "Settings";
	public static final String EMPTY = "empty";
	
	public MainFrame(Contacts contacts, DataStorageHandler dataStorage, ConfigFileData configFileData) {
		pointerContacts = contacts;
		pointerDataStorage = dataStorage;
		pointerConfigFileData = configFileData;
		
		pointerContactList = new ContactList(this, pointerContacts);
		pointerContactDetail= new ContactDetail(this, pointerContacts,pointerDataStorage);
		pointerSettingsView = new SettingsView(this, pointerConfigFileData);
		pointerContactLog = new ContactLog(this);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnMenu_1 = new JMenu("Menu");
		menuBar.add(mnMenu_1);
		
		JMenuItem mntmList = new JMenuItem("List");
		mntmList.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				changePage(MainFrame.CONTACT_LIST);
			}
		});
		mnMenu_1.add(mntmList);
		
		JMenuItem mntmNewContact = new JMenuItem("New Contact");
		mntmNewContact.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				changePage(CONTACT_DETAIL);
				pointerContactDetail.clearLoadedDetails();
			}
		});
		mnMenu_1.add(mntmNewContact);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmSettings = new JMenuItem("Settings");
		mntmSettings.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				changePage(SETTINGS);
			}
		});
		mnHelp.add(mntmSettings);
		
		JMenuItem mntmVersion = new JMenuItem("Version:   2.3.1");
		mnHelp.add(mntmVersion);
		
		getContentPane().setLayout(new CardLayout(0, 0));
		
		JPanel contactDetail = pointerContactDetail;
		getContentPane().add(new JScrollPane(contactDetail), CONTACT_DETAIL);
		
		JPanel contactList = pointerContactList;
		getContentPane().add(new JScrollPane(contactList), CONTACT_LIST);
		
		JPanel contactLog = pointerContactDetail;
		getContentPane().add(new JScrollPane(contactLog), CONTACT_LOG);
		
		JPanel settings = pointerSettingsView;
		getContentPane().add(new JScrollPane(settings), SETTINGS);
		initialize();
		
	}
	
	/**
	 * 
	 * @see com.contactmanager.vew.ContactLog#clear()
	 */
	public void clear() {
		pointerContactLog.clear();
	}

	public void addRowToTable(int id) {
		pointerContactList.addRowToTable(id);
	}

	public void updateRowInTable(int id) {
		pointerContactList.updateRowInTable(id);
	}

	public boolean isContactListViewerUpToDate = true;
	
	private Contacts pointerContacts;
	private DataStorageHandler pointerDataStorage;
	private ConfigFileData pointerConfigFileData;
	
	private ContactList pointerContactList;
	private ContactDetail pointerContactDetail;
	
	public void newLogInContactDetailView(String[] log) {
		pointerContactDetail.newLog(log);
	}
	public void loadDetailInContactDetailViewer(int id) {
		pointerContactDetail.loadDetail(id);
	}


	private SettingsView pointerSettingsView;
	private ContactLog pointerContactLog;
	
	
	
	
	private JPanel contentPane;

	private JPanel screenLayout = new JPanel(new CardLayout());
	
	public String currentCard;
	
	
	/**
	 * Launch the application.
	 */

	public void initialize() {
		UIManager.getLookAndFeelDefaults().put("defaultFont", new Font(Font.SANS_SERIF, Font.PLAIN , 14));
		//JDesktopIcon icon = new JDesktopIcon(pointerDataSaveLoadHandler.getIcon());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenHight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		
		setBounds(0, 0, screenWidth, screenHight);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(pointerContactDetail);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		
		screenLayout.add(contentPane, EMPTY);
		screenLayout.add(pointerContactList, CONTACT_LIST);
		screenLayout.add(scrollPane, CONTACT_DETAIL);
		screenLayout.add(pointerSettingsView, SETTINGS);
		screenLayout.add(pointerContactLog, CONTACT_LOG);
		
		//setUIFont(pointereMetaData.font);

		this.setContentPane(screenLayout);
		changePage(CONTACT_LIST);
		
		setVisible(true);
		
		notificationReminder();
		
	}
	
	public void changePage(String pageName){
		CardLayout cardLayout = (CardLayout) screenLayout.getLayout();
		cardLayout.show(screenLayout, pageName);
		currentCard = pageName;
		if(pageName == CONTACT_DETAIL) {
			pointerContactDetail.requestFocus();
		}
		else if(pageName == CONTACT_LIST && !isContactListViewerUpToDate) {
			pointerContactList.loadData();
			isContactListViewerUpToDate = true;
		}
	}
	
	private void sendAllReminders(String title, String rawContent, String field) {
		List<Contact> reminders = pointerContacts.getReminders(field);
		for (Contact contact : reminders) {
			try {
				String content = String.format(rawContent, contact.getFullName());
				sendNotification(title, content);
			} catch (IOException | AWTException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void notificationReminder() {
		sendAllReminders("Birthday Notification", "It's %s's birthday today!", Contact.BIRTHDAY_FIELD);
		sendAllReminders("Appointment Notification", "you scheduled a meeting with %s today.", Contact.NEXTCONTACT_FIELD);
		
	}
	
	private void sendNotification(String title, String message) throws IOException, AWTException {
		Image image = ImageIO.read(getClass().getResource("/icon.png"));
		String path = getClass().getResource("/icon.png").getPath();
		
		String os = System.getProperty("os.name");
		if (os.contains("Linux")) {
		    ProcessBuilder builder = new ProcessBuilder(
		        "notify-send",
		         title,
		        message, 
		        "-i",path);
		    builder.inheritIO().start();
		} else if (os.contains("Mac")) {
		    ProcessBuilder builder = new ProcessBuilder(
		        "osascript", "-e",
		        "display notification \"" + message + "\""
		            + " with title \"" + title + "\"");
		    builder.inheritIO().start();
		} else if (SystemTray.isSupported()) {
		    SystemTray tray = SystemTray.getSystemTray();

		    TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
		    trayIcon.setImageAutoSize(true);
		    tray.add(trayIcon);

		    trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
		}

	}
	


}
