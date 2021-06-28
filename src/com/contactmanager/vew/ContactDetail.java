package com.contactmanager.vew;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
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
import javax.swing.undo.UndoManager;

import com.contactmanager.datamodel.Contact;
import com.contactmanager.datamodel.CurrentContactInfo;
import com.contactmanager.datamodel.Log;
import com.contactmanager.datamodel.items.DataItemHandler;
import com.contactmanager.datamodel.items.DataType;
import com.contactmanager.datamodel.items.ExternalLoading;
import com.contactmanager.utils.io.ConfigFileData;
import com.contactmanager.utils.viewutils.DatePicker;
import com.contactmanager.utils.viewutils.TraversalPolicy;


public class ContactDetail extends JPanel {
	
	private static Integer LabelWidth = 100;
	private static Integer textFieldWidth = 200;
	private static Integer ySpacingBetweenElements = 25;
	
	private MainFrame pointerMainFrame;
	private Integer id = null;
	public boolean isInEditMode;
	

	private Map<String, JTextField> allTextFields = new HashMap<String, JTextField>();
	private Map<String, JLabel> allClickableLinks = new HashMap<String, JLabel>();
	private List<String> nonEditable = new ArrayList<String>();
	//List<String> readOnly = List.of(Contact.LASTCONTACT_FIELD, Contact.NEXTCONTACT_FIELD,Contact.CONTACTSTATUS_FIELD);
	
	private JLabel profilePictureLabel;
	private JButton btnEdit;
	private JButton btnSave;
	private JTextPane notesTextPane;
	
	private JTable table;
	private JScrollPane tableScrollPane;
	private DefaultTableModel tableModel;
	
	private CurrentContactInfo contactInfo;

	public ContactDetail(MainFrame mainFrame, CurrentContactInfo contactInfo) {
		pointerMainFrame = mainFrame;
		this.contactInfo = contactInfo;
		
		setFocusTraversalKeysEnabled(false);
		int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()-20;
		int screenHight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()-120;
		setPreferredSize(new Dimension(screenWidth,screenHight));
		setBounds(0, 0, screenWidth, screenHight);
		setLayout(null);

		profilePictureLabel = new JLabel();
		profilePictureLabel.setHorizontalAlignment(JLabel.CENTER);
		profilePictureLabel.setFocusTraversalKeysEnabled(false);
		profilePictureLabel.setBounds(25, 25, 100, 120);
		profilePictureLabel.setIconTextGap(0);
		profilePictureLabel.setPreferredSize(new Dimension(100,150));
		
		Border blackline = BorderFactory.createLineBorder(Color.black);
		profilePictureLabel.setBorder(blackline);
		add(profilePictureLabel);
		

		//CustomComponents myComponents = new CustomComponents();
		
		Map<String, Map<String, Object>> metaData = ConfigFileData.getInstance().getMetaData();
		List<String> fields = ConfigFileData.getInstance().getColumns(false);
		
		Vector<Component> order = new Vector<Component>();
		
		Map<Integer, Integer[]> columnStartingPositionsMap = new HashMap<>();
		Integer[] col1 = {25,150};
		Integer[] col2 = {400,75};
		Integer[] col3 = {775,75};
		
		columnStartingPositionsMap.put(1, col1);
		columnStartingPositionsMap.put(2, col2);
		columnStartingPositionsMap.put(3, col3);
		
		Integer maxY = 0;
		
		List<Map<Integer, JTextField>> tabOrder = new LinkedList<Map<Integer, JTextField>>();
		
		for (String field:fields) {
			Map<String,Object> dataItemMetaData = metaData.get(field);
			
			if(!dataItemMetaData.containsKey(DataItemHandler.DATA_LABEL_FIELD)) {continue;};
			
			String labelString = dataItemMetaData.get(DataItemHandler.DATA_LABEL_FIELD).toString();
			JLabel label = new JLabel( labelString+ ":");
			
			Integer[] gridPlacement = new Integer[2];
			String[] placementsAsString = dataItemMetaData.get(DataItemHandler.PLACEMENT_ON_DETAILS).toString().split(",");
			gridPlacement[0] = Integer.parseInt(placementsAsString[0]);
			gridPlacement[1] = Integer.parseInt(placementsAsString[1]);
			
			Integer startX = columnStartingPositionsMap.get(gridPlacement[0])[0]; 
			Integer startY = columnStartingPositionsMap.get(gridPlacement[0])[1]; 
			
			Integer currentX = startX;
			Integer currentY = startY + ySpacingBetweenElements*gridPlacement[1];
			
			if(currentY>maxY) {maxY = currentY;}
			
			label.setBounds(currentX, currentY, LabelWidth, 20);
			add(label);
			
			
			JTextField textField = new JTextField();//myComponents.createFilteredField(dataItem.getRegex(),dataItem.getMaxLength() );
			textField.setBounds(currentX + LabelWidth,currentY,textFieldWidth,20);
			add(textField);
			allTextFields.put(field, textField);
			
			if (dataItemMetaData.get(DataItemHandler.DATA_TYPE_ID).equals(DataType.LINK.toString())) {
				JLabel linkLabel = new JLabel(labelString);
				linkLabel.setVisible(false);
				linkLabel.setEnabled(false);
				linkLabel.setBounds(currentX + LabelWidth, currentY, textFieldWidth, 20);
				linkLabel.setForeground(Color.BLUE.darker());
				linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				linkLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						callURL(textField.getText());
					}
				});
				add(linkLabel);
				allClickableLinks.put(field, linkLabel);
			}
			Boolean isEditable =  !dataItemMetaData.containsKey(DataItemHandler.IS_EDITABLE_FIELD) || Boolean.parseBoolean(dataItemMetaData.get(DataItemHandler.IS_EDITABLE_FIELD).toString());
			
			if(isEditable) {
				
				Map<Integer,JTextField> yMap = new TreeMap<Integer,JTextField>();
				while(tabOrder.size()<=gridPlacement[0]-1) {
					tabOrder.add(gridPlacement[0]-1, yMap);
				}
				tabOrder.get(gridPlacement[0]-1).put(gridPlacement[1], textField);	
				
				if (!dataItemMetaData.get(DataItemHandler.DATA_TYPE_ID).equals(DataType.DATE.toString())) {
					continue;
				}
				
				textField.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent ae) {
						if(isInEditMode) {
							String date = new DatePicker(mainFrame).setPickedDate();
							if(date.equals("")) return;
							
							textField.setText(date);
						}
					}
				});
			}
			nonEditable.add(field);
			
		}
		
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
		
		JButton btnFileExplorer = new JButton("FIle Explorer");
		btnFileExplorer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileExplorerPressed(arg0);
			}
		});
		btnFileExplorer.setBounds(355, 25, 125, 25);
		add(btnFileExplorer);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addPressed(arg0);
			}
		});
		Integer addY = maxY < 350? 350 : maxY + ySpacingBetweenElements; 
		btnAdd.setBounds(25, addY, 65, 25);
		add(btnAdd);
		
		tableScrollPane = new JScrollPane();
		tableScrollPane.setFocusTraversalKeysEnabled(false);
		tableScrollPane.setBounds(25, addY + ySpacingBetweenElements, 950, 250);
		add(tableScrollPane);
		
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setFocusTraversalKeysEnabled(false);
		scrollPane.setBounds(1250, 75, 750, 750);
		add(scrollPane);
		
		notesTextPane = new JTextPane();
		notesTextPane.setFocusCycleRoot(false);
		scrollPane.setViewportView(notesTextPane);
		notesTextPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		Document doc = notesTextPane.getDocument();
		
		InputMap inputMap = notesTextPane.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap actionMap = notesTextPane.getActionMap();
		
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
		
		
		order.add(btnEdit);
		order.add(btnSave);
		
		for (Map<Integer, JTextField> map : tabOrder) {
			for (JTextField field : map.values()) {
				order.add(field);
			}
		}
		
	    //order.add(table);
		this.setFocusCycleRoot(true);
	    this.setFocusTraversalPolicy(new TraversalPolicy(order));

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
		notesTextPane.setText(notes);
		
		loadLogs();
		loadImage();
		toggleEdit(false);
	}
	
	public void setFieldsFromLogs() {
		Log latestLog = contactInfo.getLogs().getLatestLog();  
		
		Contact contact = contactInfo.getContact();
		for (String field : allTextFields.keySet()) {
			if(contact.getItemInfo(field).getExternalLoading() == ExternalLoading.LOGS) {
				allTextFields.get(field).setText(latestLog.getValue(field));
			}
		}
		
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
		tableScrollPane.setViewportView(table);
		
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
		notesTextPane.setText("");
		loadImage();
		toggleEdit(true);
	}
	
	public void save() {
		
		if (id != null) {
			contactInfo.getContact().saveFromContactDetailView(allTextFields);
			pointerMainFrame.updateRowInTable(id);
		}
		else {
			id = contactInfo.addNewContact();
			contactInfo.getContact().saveFromContactDetailView(allTextFields);
			pointerMainFrame.addRowToTable(id);
		}
		
		
		contactInfo.setNotes(notesTextPane.getText());
		contactInfo.save(id);
		
		toggleEdit(false);
		
	}
	
	public void toggleEdit(Boolean activateEditModeIfTrue) {

		//Toggle editablity of textboxes (if they aren't in the readonly list)
		
		//foreach field, if it's editable, set the textbox to editable, and if it's a link, set it to visible when it needs to be edited
		for (String field : allTextFields.keySet()) {
			JTextField textBox = allTextFields.get(field);
			textBox.setEditable(activateEditModeIfTrue);
			
			Boolean nonEditableBool = nonEditable.contains(field);
			
			Boolean isLink = allClickableLinks.keySet().contains(field);
			
			if(textBox != null && nonEditableBool) {
				textBox.setEditable(false);
			}
			
			if(textBox != null && isLink) {	
				allTextFields.get(field).setVisible(activateEditModeIfTrue);
				allClickableLinks.get(field).setVisible(!activateEditModeIfTrue);
				allClickableLinks.get(field).setEnabled(validateURL(allTextFields.get(field).getText()));
				
			}
			
		}
		
		notesTextPane.setEditable(activateEditModeIfTrue);
		
		//enable and disable buttons
		btnEdit.setEnabled(!activateEditModeIfTrue);
		btnEdit.setFocusable(!activateEditModeIfTrue);
		
		btnSave.setEnabled(activateEditModeIfTrue);
		btnSave.setFocusable(activateEditModeIfTrue);

		
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
	
    }

