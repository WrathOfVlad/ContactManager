package com.contactmanager.vew;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import javax.swing.text.MaskFormatter;
import javax.swing.undo.UndoManager;

import com.contactmanager.datamodel.Contact;
import com.contactmanager.datamodel.CurrentContactInfo;
import com.contactmanager.datamodel.Log;


public class ContactDetail extends JPanel {
	
	private MainFrame pointerMainFrame;
	private Integer id = null;
	public boolean isInEditMode;
	

	private Map<String, JTextField> allTextFields = new HashMap<String, JTextField>();
	private Map<String, JLabel> allClickableLinks = new HashMap<String, JLabel>();
	List<String> readOnly = List.of(Contact.LASTCONTACT_FIELD, Contact.NEXTCONTACT_FIELD,Contact.CONTACTSTATUS_FIELD);
	
	private JLabel profilePictureLabel;
	private JButton btnEdit;
	private JButton btnSave;
	private JTextPane textPane;
	
	private JTable table;
	private JScrollPane scrollPane_1;
	private DefaultTableModel tableModel;
	
	private CurrentContactInfo contactInfo;

	public ContactDetail(MainFrame mainFrame, CurrentContactInfo contactInfo) {
		pointerMainFrame = mainFrame;
		this.contactInfo = contactInfo;
		//pointerDataStorage = dataStorageHandler;
		setFocusTraversalKeysEnabled(false);
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()-20;
		int screenHight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()-120;
		setPreferredSize(new Dimension(screenWidth,screenHight));
		setBounds(0, 0, screenWidth, screenHight);
		setLayout(null);

		profilePictureLabel = new JLabel();

		profilePictureLabel.setHorizontalAlignment(JLabel.CENTER);
		profilePictureLabel.setFocusTraversalKeysEnabled(false);
		profilePictureLabel.setBounds(25, 25, 100, 119);
		profilePictureLabel.setIconTextGap(0);
		profilePictureLabel.setPreferredSize(new Dimension(100,150));
		
		Border blackline = BorderFactory.createLineBorder(Color.black);
		profilePictureLabel.setBorder(blackline);
		add(profilePictureLabel);
		
		
		MaskFormatter dateFormat = null;
		try {
			dateFormat = new MaskFormatter("####-##-##");
			dateFormat.setPlaceholderCharacter('_');

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		InputVerifier verifier = new InputVerifier() {
			
			@Override
			public boolean verify(JComponent input) {
				Boolean returnValue = false;
				JFormattedTextField textField = (JFormattedTextField)input;
				AbstractFormatter formater = textField.getFormatter();
				if(formater != null) {
					try {
				         formater.stringToValue(textField.getText());
				         returnValue = true;
				    } 
					catch (Exception e) {
						returnValue = false;
						
				    }
				    return returnValue;
				}
				else {
					return true;
				}
			}
			public boolean shouldYieldFocus(JComponent input) {
				Boolean isValid =verify(input);
				if(!isValid) {
					((JFormattedTextField)input).setValue(null);
					isValid = verify(input);
				}
				return true;
			}
		};
		
		JFormattedTextField birthdayTextField = new JFormattedTextField(dateFormat);
		birthdayTextField.setBounds(130, 150, 85, 20);
		add(birthdayTextField);
		birthdayTextField.setInputVerifier(verifier);
		allTextFields.put(Contact.BIRTHDAY_FIELD, birthdayTextField);
		
		JFormattedTextField formattedTextField = new JFormattedTextField(dateFormat);
		formattedTextField.setFocusTraversalKeysEnabled(false);
		formattedTextField.setEditable(false);
		formattedTextField.setBounds(500, 325, 85, 20);
		add(formattedTextField);
		formattedTextField.setInputVerifier(verifier);
		allTextFields.put(Contact.NEXTCONTACT_FIELD, formattedTextField);
		
		JFormattedTextField formattedTextField_1 = new JFormattedTextField(dateFormat);
		formattedTextField_1.setFocusTraversalKeysEnabled(false);
		formattedTextField_1.setEditable(false);
		formattedTextField_1.setBounds(500, 350, 85, 20);
		add(formattedTextField_1);
		formattedTextField_1.setInputVerifier(verifier);
		allTextFields.put(Contact.LASTCONTACT_FIELD, formattedTextField_1);
		
		
		
		btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				editPressed(arg0);
			}
		});		
		btnEdit.setBounds(150, 25, 75, 25);
		add(btnEdit);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				savePressed(e);
			}
		});
		btnSave.setBounds(250, 25, 75, 25);
		add(btnSave);
		
		
		JLabel lblLinkedin = new JLabel(Contact.LINKEDIN_FIELD);
		lblLinkedin.setFocusTraversalKeysEnabled(false);
		lblLinkedin.setVisible(false);
		lblLinkedin.setEnabled(false);
		lblLinkedin.setBounds(130, 175, 70, 15);
		lblLinkedin.setForeground(Color.BLUE.darker());
		lblLinkedin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		lblLinkedin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				callURL(Contact.LINKEDIN_FIELD);
			}
		});
		
		add(lblLinkedin);
		allClickableLinks.put(Contact.LINKEDIN_FIELD, lblLinkedin);
		
		JLabel lblFacebook = new JLabel(Contact.FACEBOOK_FIELD);
		lblFacebook.setFocusTraversalKeysEnabled(false);
		lblFacebook.setVisible(false);
		lblFacebook.setEnabled(false);
		lblFacebook.setForeground(Color.BLUE.darker());
		lblFacebook.setBounds(130, 200, 70, 15);
		lblFacebook.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		lblFacebook.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				callURL(Contact.FACEBOOK_FIELD);
			}
		});
		
		add(lblFacebook);
		allClickableLinks.put(Contact.FACEBOOK_FIELD, lblFacebook);
		
		JLabel lblCompanyUrl_1 = new JLabel(Contact.COMPANYURL_FIELD);
		lblCompanyUrl_1.setFocusTraversalKeysEnabled(false);
		lblCompanyUrl_1.setVisible(false);
		lblCompanyUrl_1.setEnabled(false);
		lblCompanyUrl_1.setForeground(Color.BLUE.darker());
		lblCompanyUrl_1.setBounds(130, 225, 150, 15);
		lblCompanyUrl_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		lblCompanyUrl_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				callURL(Contact.COMPANYURL_FIELD);
			}
		});
		
		add(lblCompanyUrl_1);
		allClickableLinks.put(Contact.COMPANYURL_FIELD, lblCompanyUrl_1);
		
		
		  /////////////////////////////////////////////////////////////
		 /////////////////////////Labels/////////////////////////////
		///////////////////////////////////////////////////////////
		
		JLabel lblBirthday_2 = new JLabel(Contact.BIRTHDAY_FIELD+ ":");
		lblBirthday_2.setFocusable(false);
		lblBirthday_2.setFocusTraversalKeysEnabled(false);
		lblBirthday_2.setBounds(25, 150, 70, 15);
		add(lblBirthday_2);
		
		JLabel lblName_1 = new JLabel(Contact.NAME_FIELD + ":");
		lblName_1.setFocusable(false);
		lblName_1.setFocusTraversalKeysEnabled(false);
		lblName_1.setBounds(400, 75, 70, 15);
		add(lblName_1);
		
		JLabel lblSurname_1 = new JLabel(Contact.SURNAME_FIELD + ":");
		lblSurname_1.setFocusable(false);
		lblSurname_1.setFocusTraversalKeysEnabled(false);
		lblSurname_1.setBounds(400, 100, 70, 15);
		add(lblSurname_1);
		
		JLabel lblEmail_3 = new JLabel(Contact.EMAIL1_FIELD+ ":");
		lblEmail_3.setFocusable(false);
		lblEmail_3.setFocusTraversalKeysEnabled(false);
		lblEmail_3.setBounds(400, 125, 70, 15);
		add(lblEmail_3);
		
		JLabel lblEmail_4 = new JLabel(Contact.EMAIL2_FIELD + ":");
		lblEmail_4.setFocusable(false);
		lblEmail_4.setFocusTraversalKeysEnabled(false);
		lblEmail_4.setBounds(400, 150, 70, 15);
		add(lblEmail_4);
		
		JLabel lblEmail_5 = new JLabel(Contact.EMAIL3_FIELD + ":");
		lblEmail_5.setFocusable(false);
		lblEmail_5.setFocusTraversalKeysEnabled(false);
		lblEmail_5.setBounds(400, 175, 70, 15);
		add(lblEmail_5);
		
		JLabel lblCompany_1 = new JLabel(Contact.COMPANY_FIELD + ":");
		lblCompany_1.setFocusable(false);
		lblCompany_1.setFocusTraversalKeysEnabled(false);
		lblCompany_1.setBounds(400, 200, 70, 15);
		add(lblCompany_1);
		
		JLabel lblLocation_1 = new JLabel(Contact.LOCATION_FIELD + ":");
		lblLocation_1.setFocusable(false);
		lblLocation_1.setFocusTraversalKeysEnabled(false);
		lblLocation_1.setBounds(400, 225, 70, 15);
		add(lblLocation_1);
		
		JLabel lblAddress_1 = new JLabel(Contact.ADDRESS_FIELD + ":");
		lblAddress_1.setFocusable(false);
		lblAddress_1.setFocusTraversalKeysEnabled(false);
		lblAddress_1.setBounds(400, 250, 70, 15);
		add(lblAddress_1);
		
		JLabel lblPhone_2 = new JLabel(Contact.PHONE1_FIELD + ":");
		lblPhone_2.setFocusable(false);
		lblPhone_2.setFocusTraversalKeysEnabled(false);
		lblPhone_2.setBounds(740, 75, 70, 15);
		add(lblPhone_2);
		
		JLabel lblPhone_3 = new JLabel(Contact.PHONE2_FIELD + ":");
		lblPhone_3.setFocusable(false);
		lblPhone_3.setFocusTraversalKeysEnabled(false);
		lblPhone_3.setBounds(740, 100, 70, 15);
		add(lblPhone_3);
		
		JLabel lblRole_1 = new JLabel(Contact.ROLE_FIELD + ":");
		lblRole_1.setFocusable(false);
		lblRole_1.setFocusTraversalKeysEnabled(false);
		lblRole_1.setBounds(740, 150, 70, 15);
		add(lblRole_1);
		
		JLabel lblLinkedin_1 = new JLabel(Contact.LINKEDIN_FIELD + ":");
		lblLinkedin_1.setFocusable(false);
		lblLinkedin_1.setFocusTraversalKeysEnabled(false);
		lblLinkedin_1.setBounds(25, 175, 75, 15);
		add(lblLinkedin_1);
		
		JLabel lblFacebook_1 = new JLabel(Contact.FACEBOOK_FIELD + ":");
		lblFacebook_1.setFocusable(false);
		lblFacebook_1.setFocusTraversalKeysEnabled(false);
		lblFacebook_1.setBounds(25, 200, 85, 15);
		add(lblFacebook_1);
		
		JLabel lblSkype = new JLabel(Contact.SKYPE_FIELD + ":");
		lblSkype.setFocusable(false);
		lblSkype.setFocusTraversalKeysEnabled(false);
		lblSkype.setBounds(25, 250, 70, 15);
		add(lblSkype);
		
		JLabel lblStatusContact = new JLabel(Contact.CONTACTSTATUS_FIELD + ":");
		lblStatusContact.setFocusable(false);
		lblStatusContact.setFocusTraversalKeysEnabled(false);
		lblStatusContact.setBounds(740, 325, 125, 15);
		add(lblStatusContact);
		
		
		JLabel lblNextContact = new JLabel(Contact.NEXTCONTACT_FIELD + ":");
		lblNextContact.setFocusable(false);
		lblNextContact.setFocusTraversalKeysEnabled(false);
		lblNextContact.setBounds(400, 325, 100, 15);
		add(lblNextContact);
		
		JLabel lblLastContact = new JLabel(Contact.LASTCONTACT_FIELD + ":");
		lblLastContact.setFocusable(false);
		lblLastContact.setFocusTraversalKeysEnabled(false);
		lblLastContact.setBounds(400, 350, 100, 15);
		add(lblLastContact);
		
		JLabel lblCompanyUrl = new JLabel(Contact.COMPANYURL_FIELD + ":");
		lblCompanyUrl.setFocusable(false);
		lblCompanyUrl.setFocusTraversalKeysEnabled(false);
		lblCompanyUrl.setBounds(25, 225, 115, 15);
		add(lblCompanyUrl);
		
		
		JLabel lblNotes = new JLabel("Notes:");
		lblNotes.setFocusable(false);
		lblNotes.setFocusTraversalKeysEnabled(false);
		lblNotes.setBounds(1025, 75, 48, 15);
		add(lblNotes);
		
		  /////////////////////////////////////////////
		 ///////////////Text Fields//////////////////
		///////////////////////////////////////////

		
		JTextField textField = new JTextField();
		textField.setBounds(475, 75, 250, 20);
		add(textField);
		textField.setColumns(10);
		allTextFields.put(Contact.NAME_FIELD, textField);
		
		JTextField textField_2 = new JTextField();
		textField_2.setBounds(475, 100, 250, 20);
		add(textField_2);
		textField_2.setColumns(10);
		allTextFields.put(Contact.SURNAME_FIELD, textField_2);
		
		JTextField textField_3 = new JTextField();
		textField_3.setBounds(475, 125, 250, 20);
		add(textField_3);
		textField_3.setColumns(10);
		allTextFields.put(Contact.EMAIL1_FIELD, textField_3);
		
		JTextField textField_4 = new JTextField();
		textField_4.setBounds(475, 150, 250, 20);
		add(textField_4);
		textField_4.setColumns(10);
		allTextFields.put(Contact.EMAIL2_FIELD, textField_4);
		
		JTextField textField_5 = new JTextField();
		textField_5.setBounds(475, 175, 250, 20);
		add(textField_5);
		textField_5.setColumns(10);
		allTextFields.put(Contact.EMAIL3_FIELD, textField_5);
		
		JTextField textField_6 = new JTextField();
		textField_6.setBounds(475, 200, 250, 20);
		add(textField_6);
		textField_6.setColumns(10);
		allTextFields.put(Contact.COMPANY_FIELD, textField_6);
		
		JTextField textField_7 = new JTextField();
		textField_7.setBounds(475, 225, 250, 20);
		add(textField_7);
		textField_7.setColumns(10);
		allTextFields.put(Contact.LOCATION_FIELD, textField_7);
		
		JTextField textField_8 = new JTextField();
		textField_8.setBounds(475, 250, 250, 20);
		add(textField_8);
		textField_8.setColumns(10);
		allTextFields.put(Contact.ADDRESS_FIELD, textField_8);
		
		JTextField textField_9 = new JTextField();
		textField_9.setBounds(800, 75, 200, 20);
		add(textField_9);
		textField_9.setColumns(10);
		allTextFields.put(Contact.PHONE1_FIELD, textField_9);
		
		JTextField textField_10 = new JTextField();
		textField_10.setBounds(800, 100, 200, 20);
		add(textField_10);
		textField_10.setColumns(10);
		allTextFields.put(Contact.PHONE2_FIELD, textField_10);
		
		JTextField textField_11 = new JTextField();
		textField_11.setBounds(800, 150, 200, 20);
		add(textField_11);
		textField_11.setColumns(10);
		allTextFields.put(Contact.ROLE_FIELD, textField_11);
		
		JTextField textField_12 = new JTextField();
		textField_12.setBounds(130, 175, 250, 20);
		add(textField_12);
		textField_12.setColumns(10);
		allTextFields.put(Contact.LINKEDIN_FIELD, textField_12);
		
		JTextField textField_13 = new JTextField();
		textField_13.setBounds(130, 200, 250, 20);
		add(textField_13);
		textField_13.setColumns(10);
		allTextFields.put(Contact.FACEBOOK_FIELD, textField_13);		
		
		JTextField textField_15 = new JTextField();
		textField_15.setBounds(130, 250, 175, 20);
		add(textField_15);
		textField_15.setColumns(10);
		allTextFields.put(Contact.SKYPE_FIELD, textField_15);
		
		JTextField textField_14 = new JTextField();
		textField_14.setBounds(130, 225, 250, 19);
		add(textField_14);
		textField_14.setColumns(10);
		allTextFields.put(Contact.COMPANYURL_FIELD, textField_14);
		
		JTextField textField_16 = new JTextField();
		textField_16.setEditable(false);
		textField_16.setBounds(850, 325, 150, 20);
		add(textField_16);
		textField_16.setColumns(10);
		allTextFields.put(Contact.CONTACTSTATUS_FIELD, textField_16);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setFocusTraversalKeysEnabled(false);
		scrollPane.setBounds(1075, 75, 750, 750);
		add(scrollPane);
		
		textPane = new JTextPane();
		textPane.setFocusCycleRoot(false);
		scrollPane.setViewportView(textPane);
		textPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setFocusTraversalKeysEnabled(false);
		scrollPane_1.setBounds(25, 375, 950, 250);
		add(scrollPane_1);
		
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addPressed(arg0);
			}
		});
		btnAdd.setBounds(25, 340, 65, 25);
		add(btnAdd);
		
		JButton btnFileExplorer = new JButton("FIle Explorer");
		btnFileExplorer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileExplorerPressed(arg0);
			}
		});
		btnFileExplorer.setBounds(355, 25, 125, 25);
		add(btnFileExplorer);
		
		Document doc = textPane.getDocument();
		
		InputMap inputMap = textPane.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap actionMap = textPane.getActionMap();
		
		UndoManager undoManager = new UndoManager();
		doc.addUndoableEditListener(new UndoableEditListener() {
		    @Override
		    public void undoableEditHappened(UndoableEditEvent e) {

		        //System.out.println("Add edit");
		        undoManager.addEdit(e.getEdit());

		    }
		});

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "Undo");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "Redo");

		actionMap.put("Undo", new AbstractAction() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        try {
		            if (undoManager.canUndo()) {
		                undoManager.undo();
		            }
		        } catch (Exception exp) {
		            exp.printStackTrace();
		        }
		    }
		});
		actionMap.put("Redo", new AbstractAction() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        try {
		            if (undoManager.canRedo()) {
		                undoManager.redo();
		            }
		        } catch (Exception exp) {
		            exp.printStackTrace();
		        }
		    }
		});
		addGlobalEventListener();
		
		Vector<Component> order = new Vector<Component>();
		order.add(textField);
		order.add(textField_2);
		order.add(textField_3);
		order.add(textField_4);
		order.add(textField_5);
		order.add(textField_6);
		order.add(textField_7);
		order.add(textField_8);
		order.add(textField_9);
		order.add(textField_10);
		order.add(textField_11);
		order.add(birthdayTextField);
		order.add(textField_12);
		order.add(textField_13);
		order.add(textField_14);
		order.add(textField_15);
		order.add(btnAdd);
		order.add(btnEdit);
		order.add(btnSave);
		
	    //order.add(table);
		this.setFocusCycleRoot(true);
	    this.setFocusTraversalPolicy(new CustomTraversalPolicy(order));

	}
	
	
	public void entryPoint() {
		profilePictureLabel.requestFocus();
	}
	public void exitPoint() {
		contactInfo.clear();
		clearLoadedDetails();
	}
	
	public boolean validateURL(String url) {
		try {
            new URL(url).toURI();
            return true;
        }
        catch (Exception e) {
            return false;
        }
	}
	
	public void callURL(String field) {
		if(!allClickableLinks.get(field).isEnabled()) {
			return;
		}
		
		String url = allTextFields.get(field).getText();
		boolean isValid = validateURL(url);
		if (isValid) {
			try {
				pointerMainFrame.openURL(url);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(pointerMainFrame, "Something went wrong");
			}
		}
		else {
			JOptionPane.showMessageDialog(pointerMainFrame, "Invalid Link");
		}
	}
	
	public void addGlobalEventListener() {
		KeyListener listener = new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				if(arg0.getKeyChar() == KeyEvent.VK_ESCAPE) {
					if(isInEditMode && id != null) {
						contactInfo.getContact().loadContactDataFromDatamodel(allTextFields);
						toggleEdit(false);
					}
					else {
						pointerMainFrame.changePage(MainFrame.CONTACT_LIST);
					}
				}	
			}
		};
		for (Component component: getComponents()) {
			component.addKeyListener(listener);
		}
		this.addKeyListener(listener);
	}

	public void loadImage() {
		BufferedImage image = (BufferedImage)contactInfo.getImage();
	
		Dimension original = new Dimension(image.getHeight(),image.getWidth());
		Dimension boundary = new Dimension(profilePictureLabel.getHeight(),profilePictureLabel.getWidth());
		
		double widthRatio = boundary.getWidth() / original.getWidth();
	    double heightRatio = boundary.getHeight() / original.getHeight();
	    double ratio = Math.min(widthRatio, heightRatio);
		
	    Image scaledImage = image.getScaledInstance((int) (original.height*ratio),(int)(original.width*ratio), Image.SCALE_SMOOTH);
	    
	    
		ImageIcon imageIcon = new ImageIcon(scaledImage);
		profilePictureLabel.setIcon(imageIcon);

	
	}

	public void loadDetail(int id) {
		this.id = id;
		contactInfo.loadContactInfo(id);
		
		contactInfo.getContact().loadContactDataFromDatamodel(allTextFields);
		String notes = contactInfo.getNotes();
		textPane.setText(notes);
		
		loadLogs();
		loadImage();
		toggleEdit(false);
	}
	
	public void setFieldsFromLogs() {
		Log latestLog = contactInfo.getLogs().getLatestLog();  
		
		((JFormattedTextField)allTextFields.get(Contact.LASTCONTACT_FIELD)).setValue(latestLog.getDate());
		
		((JFormattedTextField)allTextFields.get(Contact.NEXTCONTACT_FIELD)).setValue(latestLog.getNextTime());
		
		allTextFields.get(Contact.CONTACTSTATUS_FIELD).setText(latestLog.getStatus());
		save();
	}
	
	private void loadLogs() {

		List<String[]> allLogsList = contactInfo.getLogs().getLogsAsList();
		String[] columns = Log.getColumnNames();
		
		String[][] allLogsArray = new String[allLogsList.size()][];
		
		for (int i = 0; i < allLogsArray.length; i++) {
			allLogsArray[i] = allLogsList.get(i);
		}
		
		tableModel =  new DefaultTableModel(allLogsArray, columns);
		table = new JTable(tableModel);
		
		table.setDefaultEditor(Object.class, null);
		//table.setPreferredSize(new Dimension(getWidth(),getHeight()));
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane_1.setViewportView(table);
		
	}
	
	public void clearLoadedDetails() {
		this.id = null;

		for (String field : allTextFields.keySet()) {
			JTextField textField = allTextFields.get(field);
			if( textField instanceof JFormattedTextField) {
				((JFormattedTextField) textField).setValue(null);
			}
			else {
				textField.setText(null);
			}
		}
		textPane.setText("");
		loadImage();
		toggleEdit(true);
	}
	
	public void save() {
		
		if (id != null) {
			contactInfo.getContact().loadFromContactDetail(allTextFields);
			pointerMainFrame.updateRowInTable(id);
		}
		else {
			id = contactInfo.addNewContact();
			contactInfo.getContact().loadFromContactDetail(allTextFields);
			pointerMainFrame.addRowToTable(id);
		}
		
		
		contactInfo.setNotes(textPane.getText());
		contactInfo.save(id);
		
		toggleEdit(false);
		
	}
	
	public void toggleEdit(Boolean activateEditModeIfTrue) {

		//Toggle editablity of textboxes (if they aren't in the readonly list)
		for (String field : allTextFields.keySet()) {
			JTextField textBox = allTextFields.get(field);
			if(textBox != null && !readOnly.contains(field)) {
				textBox.setEditable(activateEditModeIfTrue);
			}
		}
		textPane.setEditable(activateEditModeIfTrue);
		
		//enable and disable buttons
		btnEdit.setEnabled(!activateEditModeIfTrue);
		btnEdit.setFocusable(!activateEditModeIfTrue);
		
		btnSave.setEnabled(activateEditModeIfTrue);
		btnSave.setFocusable(activateEditModeIfTrue);
		
		//set links
		allTextFields.get(Contact.LINKEDIN_FIELD).setVisible(activateEditModeIfTrue);
		allTextFields.get(Contact.FACEBOOK_FIELD).setVisible(activateEditModeIfTrue);
		allTextFields.get(Contact.COMPANYURL_FIELD).setVisible(activateEditModeIfTrue);
		
		for (String link : allClickableLinks.keySet()) {
			allClickableLinks.get(link).setVisible(!activateEditModeIfTrue);
			//allClickableLinks.get(link).setEnabled(!activateEditModeIfTrue);
			
			if (!validateURL(allTextFields.get(link).getText())) {
				allClickableLinks.get(link).setEnabled(false);
			}
			else {
				allClickableLinks.get(link).setEnabled(true);
			}
			
			
		}
		
		isInEditMode = activateEditModeIfTrue;
		
	}
	public void newLog(String[] log) {
		contactInfo.getLogs().addLog(log);
		tableModel.addRow(log);
		contactInfo.save(id);
		setFieldsFromLogs();
	}
	
	private void add() {
		if (id != null) {
			pointerMainFrame.changePage(MainFrame.CONTACT_LOG);
			pointerMainFrame.clear();
		}
		
		else {
			JOptionPane.showMessageDialog(pointerMainFrame, "Contact needs to be created before logs can be added.");
		}
	}
	
	private void savePressed(ActionEvent e) {
		save();
	}
	private void editPressed(ActionEvent e) {
		toggleEdit(true);
	}
	private void addPressed(ActionEvent e) {
		add();
	}
	private void fileExplorerPressed(ActionEvent e) {
		if(id != null) {
			if(!contactInfo.fileExplorer(id)) {
				JOptionPane.showMessageDialog(pointerMainFrame, "Viewing Directory unavailable in your selected storage type.");
			}
		}
		
		
	}
	
    public static class CustomTraversalPolicy extends FocusTraversalPolicy{
		Vector<Component> order;
		
		public CustomTraversalPolicy(Vector<Component> order) {
			this.order = new Vector<Component>(order.size());
			this.order.addAll(order);
		}
		public Component getComponentAfter(Container focusCycleRoot,Component aComponent){
			int idx = (order.indexOf(aComponent) + 1) % order.size();
			return order.get(idx);
		}
		
		public Component getComponentBefore(Container focusCycleRoot,Component aComponent){
			int idx = order.indexOf(aComponent) - 1;
			if (idx < 0) {
			  idx = order.size() - 1;
		}
			return order.get(idx);
		}
		
		public Component getDefaultComponent(Container focusCycleRoot) {
			return order.get(0);
		}
		
		public Component getLastComponent(Container focusCycleRoot) {
			return order.lastElement();
		}
		
		public Component getFirstComponent(Container focusCycleRoot) {
			return order.get(0);
		}
	}
}

