package com.contactmanager.view;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.contactmanager.datamodel.CurrentContactInfo;
import com.contactmanager.datamodel.items.Contact;
import com.contactmanager.utils.io.DataStorageHandler;
import com.contactmanager.utils.multiplatform.MultiPlatformSupportHandler;


public class MainFrame extends JFrame {
	
	public static final String CONTACT_LIST = "ContactList";
	public static final String CONTACT_DETAIL = "ContactDetail";
	public static final String CONTACT_LOG = "ContactLog";
	public static final String SETTINGS = "Settings";
	public static final String EMPTY = "empty";
	public static final String VERSION = "2.5.1";
	
	public boolean isContactListViewerUpToDate = true;
	
	private CurrentContactInfo contactInfo;
	private ContactList contactList;
	private ContactDetail contactDetail;
	private SettingsView settingsView;
	private ContactLog contactLog;
	private MultiPlatformSupportHandler pointerMultiPlatformSupport;
	
	private JPanel contentPane;
	private JPanel screenLayout = new JPanel(new CardLayout());
	public String currentCard;
	
	public MainFrame(DataStorageHandler dataStorage, MultiPlatformSupportHandler multiPlatformSupport) {
		
		contactInfo = new CurrentContactInfo(this,dataStorage);

		
		pointerMultiPlatformSupport = multiPlatformSupport;
		
		contactList = new ContactList(this);
		
		contactDetail= new ContactDetail(this,contactInfo);
		settingsView = new SettingsView(this,contactInfo);
		contactLog = new ContactLog(this,contactInfo);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(DataStorageHandler.ICON_PATH)));

		
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
				contactDetail.clearLoadedDetails();
				changePage(CONTACT_DETAIL);
				contactDetail.toggleEdit(true);
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
		
		JMenuItem mntmVersion = new JMenuItem("Version:   " + VERSION);
		mnHelp.add(mntmVersion);
		
		getContentPane().setLayout(new CardLayout(0, 0));
		
		JScrollPane contactDetailSP = new JScrollPane();
		contactDetailSP.setViewportView(contactDetail);
		JScrollPane contactListSP = new JScrollPane();
		contactListSP.setViewportView(contactList);
		JScrollPane contactLogSP = new JScrollPane();
		contactLogSP.setViewportView(contactLog);
		JScrollPane settingsSP = new JScrollPane();
		settingsSP.setViewportView(settingsView);

		
		getContentPane().add(contactDetailSP, CONTACT_DETAIL);
		getContentPane().add(contactListSP, CONTACT_LIST);
		getContentPane().add(contactLogSP, CONTACT_LOG);
		getContentPane().add(settingsSP, SETTINGS);
		initialize();

	}
	
	public ContactDetail getContactDetail() {
		return contactDetail;
	}
	public ContactList getContactList() {
		return contactList;
	}
	public ContactLog getContactLog() {
		return contactLog;
	}
	public SettingsView getSettingsView() {
		return settingsView;
	}
	public CurrentContactInfo getContactInfo() {
		return contactInfo;
	}
	
	public void initialize() {
		UIManager.getLookAndFeelDefaults().put("defaultFont", new Font(Font.SANS_SERIF, Font.PLAIN , 14));
		//JDesktopIcon icon = new JDesktopIcon(pointerDataSaveLoadHandler.getIcon());
		
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenHight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setBounds(0, 0, screenWidth, screenHight);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		
		JScrollPane contactDetailSP = new JScrollPane();
		contactDetailSP.setViewportView(contactDetail);
		contactDetailSP.getHorizontalScrollBar().setUnitIncrement(16);
		contactDetailSP.getVerticalScrollBar().setUnitIncrement(16);
		
		JScrollPane contactListSP = new JScrollPane();
		contactListSP.setViewportView(contactList);
		contactListSP.getHorizontalScrollBar().setUnitIncrement(16);
		contactListSP.getVerticalScrollBar().setUnitIncrement(16);
		
		JScrollPane contactLogSP = new JScrollPane();
		contactLogSP.setViewportView(contactLog);
		contactLogSP.getHorizontalScrollBar().setUnitIncrement(16);
		contactLogSP.getVerticalScrollBar().setUnitIncrement(16);
		
		JScrollPane settingsSP = new JScrollPane();
		settingsSP.setViewportView(settingsView);
		settingsSP.getHorizontalScrollBar().setUnitIncrement(16);
		settingsSP.getVerticalScrollBar().setUnitIncrement(16);
		
		screenLayout.add(new JScrollPane(), EMPTY);
		
		screenLayout.add(contactListSP, CONTACT_LIST);
		screenLayout.add(contactDetailSP, CONTACT_DETAIL);
		screenLayout.add(settingsSP, SETTINGS);
		screenLayout.add(contactLogSP, CONTACT_LOG);
		
		//setUIFont(pointereMetaData.font);

		this.setContentPane(screenLayout);		
		changePage(CONTACT_LIST);
		
		setVisible(true);
		
		notificationReminder();
		
	}
	
	public void changePage(String pageName){
		
		
		//switch the page
		CardLayout cardLayout = (CardLayout) screenLayout.getLayout();
		cardLayout.show(screenLayout, pageName);
		currentCard = pageName;
		
		//after switching the page
		if(pageName == CONTACT_DETAIL) {
			contactDetail.requestFocus();
		}
		else if(pageName == CONTACT_LIST) {
			contactDetail.exitPoint();
			if(!isContactListViewerUpToDate) {
				contactList.loadData();
				isContactListViewerUpToDate = true;
			}
			
			contactList.focusSelectedRow();
			
		}
	}
	
	public void sendReminder(Contact contact,String title, String rawContent) {
		
		String content = String.format(rawContent, contact.getFullName());
		pointerMultiPlatformSupport.sendNotification(title, content);
		
	}
	
	private void notificationReminder() {
		contactInfo.getContacts().getReminders();
	}
	
	
	public void openURL(String url) throws Exception {
		pointerMultiPlatformSupport.openLinkInBrowser(url);
	}



}
