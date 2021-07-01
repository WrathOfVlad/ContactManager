package com.contactmanager.vew;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

import com.contactmanager.datamodel.Contact;
import com.contactmanager.datamodel.CurrentContactInfo;
import com.contactmanager.datamodel.Log;
import com.contactmanager.datamodel.items.DataItemHandler;
import com.contactmanager.datamodel.items.DataType;
import com.contactmanager.datamodel.items.ExternalLoading;
import com.contactmanager.utils.io.ConfigFileData;
import com.contactmanager.utils.io.DataStorageHandler;
import com.contactmanager.utils.viewutils.CustomDatePicker;
import com.contactmanager.utils.viewutils.TraversalPolicy;


public class ContactDetail extends JPanel {
	private static final int NUM_COLS = 17;
	private static final int NUM_ROWS = 35;
	private static final int COL_WIDTHS = 100;
	private static final int ROW_HEIGHTS = 20;
	
	private MainFrame pointerMainFrame;
	private Integer id = null;
	private boolean isInEditMode;

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
		
		Border blackline = BorderFactory.createLineBorder(Color.black);
		GridBagLayout gridBagLayout = new GridBagLayout();
		
		
		int[] colWidths = new int[NUM_COLS+1];
		int[] rowHeights = new int[NUM_ROWS];
		
		Arrays.fill(colWidths,COL_WIDTHS);
		Arrays.fill(rowHeights, ROW_HEIGHTS);
		
		colWidths[0] = 10;
		gridBagLayout.columnWidths = colWidths;
		gridBagLayout.rowHeights = rowHeights;
		
		
		double[] rowWeights = new double[gridBagLayout.rowHeights.length];
		double[] columnWeights = new double[gridBagLayout.columnWidths.length];
		
		Arrays.fill(rowWeights, 0);
		Arrays.fill(columnWeights, 0);
		
		gridBagLayout.rowWeights = rowWeights;
		gridBagLayout.columnWeights = columnWeights;
		
		setLayout(gridBagLayout);
		
		//last row&column have an empty label with a weight of 1, so if there's extra space, it's added to the last row/column
		JLabel empty = new JLabel("");
		GridBagConstraints gbc_empty = new GridBagConstraints();
		gbc_empty.anchor = GridBagConstraints.NORTHWEST;
		gbc_empty.insets = new Insets(0, 0, 0, 0);
		gbc_empty.gridx = gridBagLayout.columnWidths.length-1;
		gbc_empty.gridy = gridBagLayout.rowHeights.length-1;
		gbc_empty.weightx = 1;
		gbc_empty.weighty = 1;
		add(empty,gbc_empty);
		
		
		Insets defaultPadding = new Insets(5,5,0,0);
		Vector<Component> order = new Vector<Component>();
		
		GridBagConstraints gbc_ppl = new GridBagConstraints();
		gbc_ppl.anchor = GridBagConstraints.NORTHWEST;
		gbc_ppl.insets = defaultPadding;
		gbc_ppl.gridx = 1;
		gbc_ppl.gridy = 1;
		gbc_ppl.gridheight = 6;
		gbc_ppl.gridwidth = 1;
		
		profilePictureLabel = new JLabel();
		profilePictureLabel.setHorizontalAlignment(JLabel.CENTER);
		profilePictureLabel.setIconTextGap(0);
		profilePictureLabel.setBorder(blackline);
		profilePictureLabel.setPreferredSize(new Dimension(gbc_ppl.gridwidth*COL_WIDTHS,gbc_ppl.gridheight*ROW_HEIGHTS));
		profilePictureLabel.setSize(new Dimension(gbc_ppl.gridwidth*COL_WIDTHS,gbc_ppl.gridheight*ROW_HEIGHTS));
		
		profilePictureLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent ae) {
				if(ae.getButton() == MouseEvent.BUTTON1) {
					if(isInEditMode) {
						Image image = imageChooser();
						if(image != null) {
							contactInfo.setImage(image);
							loadImage();
						}
						
					}
					else {
						showImageBig();
					}
					
				}
						
			}
		});
		add(profilePictureLabel, gbc_ppl);
		
		btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				editPressed(arg0);
			}
		});	
		GridBagConstraints gbc_btnEdit = new GridBagConstraints();
		gbc_btnEdit.anchor = GridBagConstraints.WEST;
		gbc_btnEdit.fill = GridBagConstraints.VERTICAL;
		gbc_btnEdit.insets = defaultPadding;
		gbc_btnEdit.gridx = 2;
		gbc_btnEdit.gridy = 1;
		
		btnEdit.setPreferredSize(new Dimension(gbc_btnEdit.gridwidth*COL_WIDTHS,gbc_btnEdit.gridheight*ROW_HEIGHTS));
		add(btnEdit, gbc_btnEdit);
		
		
		order.add(btnEdit);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				savePressed(e);
			}
		});
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.anchor = GridBagConstraints.WEST;
		gbc_btnSave.fill = GridBagConstraints.VERTICAL;
		gbc_btnSave.insets = defaultPadding;
		gbc_btnSave.gridx = 3;
		gbc_btnSave.gridy = 1;
		
		btnSave.setPreferredSize(new Dimension(gbc_btnEdit.gridwidth*COL_WIDTHS,gbc_btnEdit.gridheight*ROW_HEIGHTS));
		add(btnSave, gbc_btnSave);
		order.add(btnSave);
		
		JButton btnFileExplorer = new JButton("File Explorer");
		btnFileExplorer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileExplorerPressed(arg0);
			}
		});
		GridBagConstraints gbc_btnFileExplorer = new GridBagConstraints();
		gbc_btnFileExplorer.anchor = GridBagConstraints.WEST;
		gbc_btnFileExplorer.fill = GridBagConstraints.VERTICAL;
		gbc_btnFileExplorer.insets = defaultPadding;
		gbc_btnFileExplorer.gridx = 5;
		gbc_btnFileExplorer.gridy = 1;
		//gbc_btnFileExplorer.gridwidth=2;
		
		//btnFileExplorer.setPreferredSize(new Dimension(gbc_btnEdit.gridwidth*COL_WIDTHS,gbc_btnEdit.gridheight*ROW_HEIGHTS));
		add(btnFileExplorer, gbc_btnFileExplorer);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addPressed(arg0);
			}
		});
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		//gbc_btnAdd.fill = GridBagConstraints.BOTH; 
		gbc_btnAdd.anchor = GridBagConstraints.WEST;
		gbc_btnAdd.insets = defaultPadding;
		gbc_btnAdd.gridx = 1;
		gbc_btnAdd.gridy = 19;
		
		//btnAdd.setPreferredSize(new Dimension(gbc_btnEdit.gridwidth*COL_WIDTHS,gbc_btnEdit.gridheight*ROW_HEIGHTS));
		add(btnAdd, gbc_btnAdd);
		
		tableScrollPane = new JScrollPane();
		tableScrollPane.setFocusTraversalKeysEnabled(false);
		GridBagConstraints gbc_tableScrollPane = new GridBagConstraints();
		gbc_tableScrollPane.insets = new Insets(5, 5, 10, 10);
		gbc_tableScrollPane.gridx = 1;
		gbc_tableScrollPane.gridwidth = 10;
		gbc_tableScrollPane.gridheight = 15;
		gbc_tableScrollPane.gridy = 20;
		gbc_tableScrollPane.fill = GridBagConstraints.BOTH;
		tableScrollPane.setPreferredSize(new Dimension(COL_WIDTHS*gbc_tableScrollPane.gridwidth,ROW_HEIGHTS*gbc_tableScrollPane.gridheight));
		add(tableScrollPane, gbc_tableScrollPane);
		
		Map<String, Map<String, Object>> metaData = ConfigFileData.getInstance().getItemMetaData();
		List<String> fields = ConfigFileData.getInstance().getColumns(false);
		
		
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
			
			GridBagConstraints gbc_label = new GridBagConstraints();
			gbc_label.fill = GridBagConstraints.HORIZONTAL;
			gbc_label.insets = defaultPadding;
			gbc_label.gridx = 3*gridPlacement[0]-2;
			gbc_label.gridy = gridPlacement[1];
			add(label,gbc_label);
			
			String regex = DataItemHandler.getRegex(dataItemMetaData.get(DataItemHandler.DATA_TYPE_ID).toString());
			
			JTextField textField =	new JTextField(); //myComponents.createFilteredField(regex,null);
			textField.getDocument().addDocumentListener(new DocumentListener()
		    {
		      @Override
		      public void removeUpdate(DocumentEvent e)
		      {
		        validateInput(regex,textField);
		      }

		      @Override
		      public void insertUpdate(DocumentEvent e)
		      {
		        validateInput(regex,textField);
		      }

		      @Override
		      public void changedUpdate(DocumentEvent e) {
		    	  validateInput(regex,textField);
		      } // Not needed for plain-text fields
		  });
			GridBagConstraints gbc_txtField = new GridBagConstraints();
			gbc_txtField.anchor = GridBagConstraints.WEST;
			gbc_txtField.insets = defaultPadding;
			gbc_txtField.gridx = 3*gridPlacement[0]-1;
			gbc_txtField.gridy = gridPlacement[1];
			gbc_txtField.gridwidth = 2;
			gbc_txtField.fill = GridBagConstraints.HORIZONTAL;
			
			Boolean isEditable =  !dataItemMetaData.containsKey(DataItemHandler.IS_EDITABLE_FIELD) || Boolean.parseBoolean(dataItemMetaData.get(DataItemHandler.IS_EDITABLE_FIELD).toString());
			
			if(isEditable) {
				if(dataItemMetaData.get(DataItemHandler.DATA_TYPE_ID).equals(DataType.DATE.toString())) {
					nonEditable.add(field);
					textField.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent ae) {
							if(ae.getButton() == MouseEvent.BUTTON1) {
								if(isInEditMode) {
									String date = new CustomDatePicker(mainFrame).setPickedDate();
									if(date.equals("")) return;
									
									textField.setText(date);
								}
							}
						}
					});
				}
				else {
					Map<Integer,JTextField> yMap = new TreeMap<Integer,JTextField>();
					while(tabOrder.size()<=gridPlacement[0]-1) {
						tabOrder.add(gridPlacement[0]-1, yMap);
					}
					tabOrder.get(gridPlacement[0]-1).put(gridPlacement[1], textField);	
				}
			}
			else {
				nonEditable.add(field);
			}
			textField.setMinimumSize(new Dimension(COL_WIDTHS*gbc_txtField.gridwidth,ROW_HEIGHTS));
			textField.setMaximumSize(new Dimension((int) (COL_WIDTHS*gbc_txtField.gridwidth+COL_WIDTHS*0.5),ROW_HEIGHTS));
			add(textField,gbc_txtField);
			allTextFields.put(field, textField);
			
			if (dataItemMetaData.get(DataItemHandler.DATA_TYPE_ID).equals(DataType.LINK.toString())) {
				JLabel linkLabel = new JLabel(labelString);
				linkLabel.setVisible(false);
				linkLabel.setEnabled(false);
				linkLabel.setForeground(Color.BLUE.darker());
				linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				linkLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if(e.getButton() == MouseEvent.BUTTON1) {
							callURL(field);
						}
						
					}
				});
				add(linkLabel,gbc_txtField);
				allClickableLinks.put(field, linkLabel);
			}
			
			
			
		}
		
		
		GridBagConstraints gbc_notes = new GridBagConstraints();
		gbc_notes.gridx = 10;
		gbc_notes.gridy = 1;
		gbc_notes.insets = defaultPadding;
		gbc_notes.fill = GridBagConstraints.HORIZONTAL; 
		gbc_notes.fill = GridBagConstraints.VERTICAL; 
		
		JLabel notes = new JLabel("Notes:");
		add(notes,gbc_notes);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setFocusTraversalKeysEnabled(false);
		
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridx = 11;
		gbc_scrollPane.gridy = 1;
		gbc_scrollPane.gridwidth = 7;
		gbc_scrollPane.gridheight = 34;
		gbc_scrollPane.insets = new Insets(5, 5, 10, 10);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		
		scrollPane.setPreferredSize(new Dimension(COL_WIDTHS*gbc_scrollPane.gridwidth,ROW_HEIGHTS*gbc_scrollPane.gridheight));
		JPanel notesPanel = new JPanel();
		notesPanel.setLayout(new BorderLayout());
		notesTextPane = new JTextPane();
		notesPanel.add(notesTextPane, BorderLayout.CENTER);
		
		scrollPane.setViewportView(notesPanel);
		notesTextPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		add(scrollPane, gbc_scrollPane);
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
		
		getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
	    getActionMap().put("Escape", new AbstractAction() {
	        @Override
	        public void actionPerformed(ActionEvent ae) {
	        	escPressed();
	        }
	    });
		
		//addGlobalEventListener(this);
		
		for (Map<Integer, JTextField> map : tabOrder) {
			for (JTextField field : map.values()) {
				order.add(field);
			}
		}
		
	    //order.add(table);
		this.setFocusCycleRoot(true);
	    this.setFocusTraversalPolicy(new TraversalPolicy(order));

	}
	
	private void validateInput(String regex,JTextField field) {
		if(regex == null) {
			field.setForeground(Color.BLACK);
			return;
		}
		String text = field.getText();
	    Pattern r = Pattern.compile(regex);
	    Matcher m = r.matcher(text);
	    if (m.matches())
	    {
	    	field.setForeground(Color.BLACK);
	    }
	    else {
	    	field.setForeground(Color.RED);
	    }
	}
	
	public void entryPoint() {
		profilePictureLabel.requestFocus();
	}
	public void exitPoint() {
		contactInfo.clear();
		clearLoadedDetails();
	}
	
	public void escPressed() {
		if(isInEditMode && id != null) {
			contactInfo.getContact().loadContactDataFromDatamodel(allTextFields);
			String notes = contactInfo.getNotes();
			notesTextPane.setText(notes);
			toggleEdit(false);
		}
		else {
			pointerMainFrame.changePage(MainFrame.CONTACT_LIST);
		}
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
	
	public void loadImage() {
	    
		ImageIcon imageIcon = new ImageIcon(getScaledImage(contactInfo.getImage(), profilePictureLabel));
		profilePictureLabel.setIcon(imageIcon);
	}
	private Image getScaledImage(Image rawImage, JLabel container) {
		BufferedImage image = (BufferedImage)rawImage;
		
		Dimension original = new Dimension(image.getHeight(),image.getWidth());
		Dimension boundary = new Dimension(container.getHeight(),container.getWidth());
	
		double widthRatio = boundary.getWidth() / original.getWidth();
	    double heightRatio = boundary.getHeight() / original.getHeight();
	    double ratio = Math.min(widthRatio, heightRatio);
		
	    Image scaledImage = image.getScaledInstance((int) (original.height*ratio),(int)(original.width*ratio), Image.SCALE_SMOOTH);
	    
	    return scaledImage;
	}

	private Image imageChooser() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", DataStorageHandler.IMAGE_EXTENSIONS);
        chooser.setFileFilter(filter);
        int returnValue = chooser.showOpenDialog(null);
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            try {
				Image selectedImage = ImageIO.read(selectedFile);
				return selectedImage;
			} 
            catch (IOException e) {e.printStackTrace();}
        }
        return null;
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
		if(latestLog == null) return;
		
		Contact contact = contactInfo.getContact();
		for (String field : allTextFields.keySet()) {
			if(contact.getItemInfo(field).getExternalLoading() == ExternalLoading.LOGS) {
				allTextFields.get(field).setText(latestLog.getItemInfo(field).getDataValue());
			}
		}
		
		save();
	}
	
	private String[] getVisibleLogs(String[] row) {
		List<String> visibleColumns = ConfigFileData.getInstance().getLogVisibleColumns();
		List<String> columns = Log.getColumns();
		
		String[] result = new String[visibleColumns.size()];
		Log log = contactInfo.getLogs().getLogFromDate(row[columns.indexOf(Log.LAST_DATE_FIELD)]);
		for(int j = 0; j<visibleColumns.size();j++) {
			result[j] = log.getItemInfo(visibleColumns.get(j)).getDataValue();
		}
		return result;
	}
	private void loadLogs() {
		List<String> visibleColumns = ConfigFileData.getInstance().getLogVisibleColumns();
		
		List<String[]> rawData = contactInfo.getLogs().getLogsAsList();
		
		
		String[][] visibleData = new String[rawData.size()][visibleColumns.size()];
		
		for(int i = 0; i<visibleData.length;i++) {
			visibleData[i] = getVisibleLogs(rawData.get(i));
		}
		
		tableModel =  new DefaultTableModel(visibleData, visibleColumns.toArray(new String[visibleColumns.size()]));
		table = new JTable(tableModel);
		
		table.setDefaultEditor(Object.class, null);
		//table.setPreferredSize(new Dimension(getWidth(),getHeight()));
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setReorderingAllowed(true);
		tableScrollPane.setViewportView(table);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2 && table.getSelectedRow() != -1) {
					getLogToSet();
		        }
			}
		});
		
		TableColumnModel colModel = table.getColumnModel();
		colModel.moveColumn(colModel.getColumnIndex(Log.LAST_DATE_FIELD),0);
		
		
	}
	
	private void getLogToSet() {
		int rowIndex = table.getSelectedRow();
		
		int correctedIndex = table.convertRowIndexToModel(rowIndex);
    	String date = table.getModel().getValueAt(correctedIndex, table.getColumn(Log.LAST_DATE_FIELD).getModelIndex()).toString();
    	
        pointerMainFrame.setContactLog(contactInfo.getLogs().getLogFromDate(date));
        pointerMainFrame.changePage(MainFrame.CONTACT_LOG);
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
			if(!contactInfo.getContact().saveFromContactDetailView(allTextFields)) {
				invalidRegexMessage();
				return;
			}
			pointerMainFrame.updateRowInTable(id);
		}
		else {
			id = contactInfo.addNewContact();
			if(!contactInfo.getContact().saveFromContactDetailView(allTextFields)) {
				invalidRegexMessage();
				return;
			}
			pointerMainFrame.addRowToTable(id);
		}
		
		
		contactInfo.setNotes(notesTextPane.getText());
		contactInfo.save(id);
		
		toggleEdit(false);
		
	}
	private void invalidRegexMessage() {
		JOptionPane.showMessageDialog(null, "One of the values is invalid, please check that all the data is correct before saving");
	}
	
	private void showImageBig() {
		JDialog d = new JDialog();
		d.setTitle("Profile Picture");
		
		
		JLabel bigImage = new JLabel();
		bigImage.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
		bigImage.getActionMap().put("Escape", new AbstractAction() {
	        @Override
	        public void actionPerformed(ActionEvent ae) {
	        	d.dispose();
	        }
	    });
		int scale = 5;
		bigImage.setPreferredSize(new Dimension(profilePictureLabel.getWidth()*scale,profilePictureLabel.getHeight()*scale));
		bigImage.setSize(bigImage.getPreferredSize());
	
		ImageIcon imageIcon = new ImageIcon(getScaledImage(contactInfo.getImage(), bigImage));
		
		bigImage.setIcon(imageIcon);
		bigImage.setHorizontalAlignment(JLabel.CENTER);
		d.add(bigImage);
		
		d.pack();
	        //set location
	    d.setLocationRelativeTo(pointerMainFrame);
		d.setVisible(true);
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
		if(!activateEditModeIfTrue) {profilePictureLabel.requestFocus();}
		
		notesTextPane.setEditable(activateEditModeIfTrue);
		
		//enable and disable buttons
		btnEdit.setEnabled(!activateEditModeIfTrue);
		btnEdit.setFocusable(!activateEditModeIfTrue);
		
		btnSave.setEnabled(activateEditModeIfTrue);
		btnSave.setFocusable(activateEditModeIfTrue);

		
		isInEditMode = activateEditModeIfTrue;
		
	}
	public void newLog(Log log, Boolean isNewLog) {
		if(isNewLog) {
			contactInfo.getLogs().addLog(log);
			
			String[] cols = new String[tableModel.getColumnCount()];
			for(int i =0; i<tableModel.getColumnCount();i++) {
				cols[i] = tableModel.getColumnName(i);
			}
			String[] data = getVisibleLogs(log.getLog());

			
			tableModel.addRow(data);
			contactInfo.save(id);
			setFieldsFromLogs();
		}
		else {
			contactInfo.getLogs().changeLog(log.getItemInfo(Log.LAST_DATE_FIELD).getDataValue(),log);
			String[] row = getVisibleLogs(log.getLog());
			//There's no need to add one here, as the column name row is only on mainTable, not on the actual displayed table
			int rowNumber = contactInfo.getLogs().getRowIndexByDate(log.getItemInfo(Log.LAST_DATE_FIELD).getDataValue());
			
			for(int i = 0; i < row.length; i++) {
				tableModel.setValueAt(row[i], rowNumber, i);
			}
		}
		contactInfo.save(id);
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

