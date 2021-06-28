package com.contactmanager.vew;
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

import com.contactmanager.datamodel.Contact;
import com.contactmanager.datamodel.Contacts;
import com.contactmanager.datamodel.CurrentContactInfo;
import com.contactmanager.datamodel.Logs;
import com.contactmanager.utils.io.DataStorageHandler;
import com.contactmanager.utils.multiplatform.MultiPlatformSupportHandler;


public class MainFrame extends JFrame {
	
	public static final String CONTACT_LIST = "ContactList";
	public static final String CONTACT_DETAIL = "ContactDetail";
	public static final String CONTACT_LOG = "ContactLog";
	public static final String SETTINGS = "Settings";
	public static final String EMPTY = "empty";
	public static final String VERSION = "2.3.4";
	
	public boolean isContactListViewerUpToDate = true;
	
	private Contacts pointerContacts;
	private ContactList pointerContactList;
	private ContactDetail pointerContactDetail;
	private SettingsView pointerSettingsView;
	private ContactLog pointerContactLog;
	private MultiPlatformSupportHandler pointerMultiPlatformSupport;
	
	private JPanel contentPane;
	private JPanel screenLayout = new JPanel(new CardLayout());
	public String currentCard;
	
	public MainFrame(DataStorageHandler dataStorage, MultiPlatformSupportHandler multiPlatformSupport) {
		
		pointerContacts = new Contacts(dataStorage,this);
		pointerContacts.loadContacts();	
		Logs.setDataStorageHandler(dataStorage);
		
		pointerMultiPlatformSupport = multiPlatformSupport;
		
		pointerContactList = new ContactList(this, pointerContacts);
		CurrentContactInfo contactInfo = new CurrentContactInfo(pointerContacts,dataStorage);
		pointerContactDetail= new ContactDetail(this,contactInfo);
		pointerSettingsView = new SettingsView(this);
		pointerContactLog = new ContactLog(this);
		
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
		getContentPane().add(new JScrollPane(pointerContactDetail), CONTACT_DETAIL);
		getContentPane().add(new JScrollPane(pointerContactList), CONTACT_LIST);
		getContentPane().add(new JScrollPane(pointerContactLog), CONTACT_LOG);
		getContentPane().add(new JScrollPane(pointerSettingsView), SETTINGS);
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
	
	public void newLogInContactDetailView(String[] log) {
		pointerContactDetail.newLog(log);
	}
	public void loadDetailInContactDetailViewer(int id) {
		pointerContactDetail.loadDetail(id);
	}

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
		pointerContacts.getReminders();
	}
	
	
	public void openURL(String url) throws Exception {
		pointerMultiPlatformSupport.openLinkInBrowser(url);
	}



}
