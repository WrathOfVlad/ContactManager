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

import com.contactmanager.datamodel.Contacts;
import com.contactmanager.datamodel.CurrentContactInfo;
import com.contactmanager.datamodel.items.Contact;
import com.contactmanager.datamodel.items.Items;
import com.contactmanager.datamodel.items.Log;
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
	
	private Contacts contacts;
	private ContactList pointerContactList;
	private ContactDetail pointerContactDetail;
	private SettingsView pointerSettingsView;
	private ContactLog pointerContactLog;
	private MultiPlatformSupportHandler pointerMultiPlatformSupport;
	
	private JPanel contentPane;
	private JPanel screenLayout = new JPanel(new CardLayout());
	public String currentCard;
	
	public MainFrame(DataStorageHandler dataStorage, MultiPlatformSupportHandler multiPlatformSupport) {
		
		contacts = new Contacts(dataStorage, this);
		contacts.loadDataSpecific();
		
		pointerMultiPlatformSupport = multiPlatformSupport;
		
		pointerContactList = new ContactList(this, contacts);
		CurrentContactInfo contactInfo = new CurrentContactInfo(contacts,dataStorage);
		pointerContactDetail= new ContactDetail(this,contactInfo);
		pointerSettingsView = new SettingsView(this);
		pointerContactLog = new ContactLog(this,contactInfo);
		
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
				changePage(CONTACT_DETAIL);
				pointerContactDetail.toggleEdit(true);
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
		contactDetailSP.setViewportView(pointerContactDetail);
		JScrollPane contactListSP = new JScrollPane();
		contactListSP.setViewportView(pointerContactList);
		JScrollPane contactLogSP = new JScrollPane();
		contactLogSP.setViewportView(pointerContactLog);
		JScrollPane settingsSP = new JScrollPane();
		settingsSP.setViewportView(pointerSettingsView);

		
		getContentPane().add(contactDetailSP, CONTACT_DETAIL);
		getContentPane().add(contactListSP, CONTACT_LIST);
		getContentPane().add(contactLogSP, CONTACT_LOG);
		getContentPane().add(settingsSP, SETTINGS);
		initialize();

	}
	
	/**
	 * 
	 * @see com.contactmanager.view.ContactLog#clear()
	 */
	public void clear() {
		pointerContactLog.clear();
	}
	public void setContactLog(Items items) {
		pointerContactLog.setContactLog((Log)items);
	}
	

	public void addRowToTable(int id) {
		pointerContactList.addRowToTable(id);
	}

	public void updateRowInTable(int id) {
		pointerContactList.updateRowInTable(id);
	}
	
	public void logToContactDetailView(Log log, Boolean isNewLog) {
		pointerContactDetail.newLog(log,isNewLog);
	}
	public void loadDetailInContactDetailViewer(int id) {
		pointerContactDetail.loadDetail(id);
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
		contactDetailSP.setViewportView(pointerContactDetail);
		contactDetailSP.getHorizontalScrollBar().setUnitIncrement(16);
		contactDetailSP.getVerticalScrollBar().setUnitIncrement(16);
		
		JScrollPane contactListSP = new JScrollPane();
		contactListSP.setViewportView(pointerContactList);
		contactListSP.getHorizontalScrollBar().setUnitIncrement(16);
		contactListSP.getVerticalScrollBar().setUnitIncrement(16);
		
		JScrollPane contactLogSP = new JScrollPane();
		contactLogSP.setViewportView(pointerContactLog);
		contactLogSP.getHorizontalScrollBar().setUnitIncrement(16);
		contactLogSP.getVerticalScrollBar().setUnitIncrement(16);
		
		JScrollPane settingsSP = new JScrollPane();
		settingsSP.setViewportView(pointerSettingsView);
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
			pointerContactDetail.requestFocus();
		}
		else if(pageName == CONTACT_LIST) {
			pointerContactDetail.exitPoint();
			if(!isContactListViewerUpToDate) {
				pointerContactList.loadData();
				isContactListViewerUpToDate = true;
			}
			
			pointerContactList.focusSelectedRow();
			
		}
	}
	
	public void sendReminder(Contact contact,String title, String rawContent) {
		
		String content = String.format(rawContent, contact.getFullName());
		pointerMultiPlatformSupport.sendNotification(title, content);
		
	}
	
	private void notificationReminder() {
		contacts.getReminders();
	}
	
	
	public void openURL(String url) throws Exception {
		pointerMultiPlatformSupport.openLinkInBrowser(url);
	}



}
