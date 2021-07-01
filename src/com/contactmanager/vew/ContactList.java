package com.contactmanager.vew;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.contactmanager.datamodel.Contact;
import com.contactmanager.datamodel.Contacts;
import com.contactmanager.utils.io.ConfigFileData;
import com.contactmanager.utils.viewutils.CustomComponents;


public class ContactList extends JPanel{
	
	private JTable table = new JTable();
	private DefaultTableModel tableModel;
	private JScrollPane scrollPane;
	private TableRowSorter<TableModel> sorter;
	
	private MainFrame pointerMainFrame;
	private Contacts pointerContacts;
	
	
	private JTextField searchField;
	
	public ContactList(MainFrame mainFrame, Contacts contacts) {
		this.pointerContacts = contacts;
		this.pointerMainFrame = mainFrame;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {100,500,0};
		gridBagLayout.rowHeights = new int[] {20,0};
		setLayout(gridBagLayout);
		

		
		
		JLabel lblSearch = new JLabel("Search:");
		GridBagConstraints gbc_search = new GridBagConstraints();
		gbc_search.fill = GridBagConstraints.HORIZONTAL;
		gbc_search.insets = new Insets(5,5, 0, 0);
		gbc_search.gridx = 0;
		gbc_search.gridy = 0;
		
		add(lblSearch,gbc_search);
		
		
		searchField = new JTextField();
		GridBagConstraints gbc_searchField = new GridBagConstraints();
		gbc_searchField.fill = GridBagConstraints.HORIZONTAL;
		gbc_searchField.insets = new Insets(5, 5, 0, 0);
		gbc_searchField.gridx = 1;
		gbc_searchField.gridy = 0;
		add(searchField,gbc_searchField);
		
		searchField.addKeyListener(new KeyAdapter() {
			@Override
		    public void keyReleased(KeyEvent e) {
		    	if(e.getKeyChar() != KeyEvent.VK_ESCAPE) {		    		
			        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchField.getText()));
		    	}
		    }
			
		});
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		gbc_scrollPane.weightx = 1;
		gbc_scrollPane.weighty = 1;
		add(scrollPane, gbc_scrollPane);
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setDefaultEditor(Object.class, null);
		table.setAutoCreateRowSorter(true);
		table.getTableHeader().setReorderingAllowed(false);
		
		loadData();
		
		table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
	    table.getActionMap().put("Enter", new AbstractAction() {
	        @Override
	        public void actionPerformed(ActionEvent ae) {
	        	enterContactDetail();
	        }
	    });
	   
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2 && table.getSelectedRow() != -1) {
					enterContactDetail();
		        }
			}
		});
		
		addTableToScrollPane();
	} 
	
	public void resizeAllColumns() {
		CustomComponents.resizeAllColumns(table);
	}
	
	public void addTableToScrollPane(){
		scrollPane.setViewportView(table);
	}
	
	public void addRowToTable(int id) {
		String[] finalRow = getVisibleDataFromFullRow(id);
		
		tableModel.addRow(finalRow);
		resizeAllColumns();
	}
	
	public String[] getVisibleDataFromFullRow(int id) {
		Contact contact = pointerContacts.getContactById(id);
		List<String> visibleRow = new ArrayList<>();
		List<String> visibleColumns = ConfigFileData.getInstance().getVisibleColumns();
		
		for (int i = 0; i < visibleColumns.size(); i++) {
			if(visibleColumns.get(i).equals(Contact.ID_FIELD)) {
				visibleRow.add(contact.getIdAsString());
			}
			else if(visibleColumns.get(i).equals(Contact.FULL_NAME_FIELD)) {
				visibleRow.add(contact.getFullName());
			}
			else {
				visibleRow.add(contact.getItemInfo(visibleColumns.get(i)).getDataValue());
			}
		}
		return visibleRow.toArray(new String[visibleRow.size()]);
	}
	
	
	public void updateRowInTable(int id) {
		String[] row = getVisibleDataFromFullRow(id);
		//There's no need to add one here, as the column name row is only on mainTable, not on the actual displayed table
		int rowNumber = pointerContacts.getRowIndexById(id);
		
		for(int i = 0; i < row.length; i++) {
			tableModel.setValueAt(row[i], rowNumber, i);
		}
	}
	
	public void loadData() {
		List<String> visibleColumns = ConfigFileData.getInstance().getVisibleColumns();
		String[][] rawData = pointerContacts.contactsAs2DArray();
		String[][] visibleData = new String[rawData.length][visibleColumns.size()];
		List<Integer> ids = pointerContacts.getIds();
		
		int i = 0;
		for (Integer id : ids) {
			String[] row = getVisibleDataFromFullRow(id);
			for (int j = 0; j < row.length; j++) {
				visibleData[i][j] = row[j];
			}
			i ++;
		}
		
		tableModel = new DefaultTableModel(visibleData, visibleColumns.toArray());
		sorter = new TableRowSorter<TableModel>(tableModel);
		table.setModel(tableModel);
		table.setRowSorter(sorter);
		
		JTableHeader header = table.getTableHeader();
		Font newFont = table.getFont().deriveFont(Font.BOLD);
		header.setFont(newFont);
		
		resizeAllColumns();
	}
	
	private void enterContactDetail() {
		int rowIndex = table.getSelectedRow();
		
		int correctedIndex = table.convertRowIndexToModel(rowIndex);
    	
    	int id = Integer.parseInt(table.getModel().getValueAt(correctedIndex, 0).toString());
        pointerMainFrame.changePage(MainFrame.CONTACT_DETAIL);
        pointerMainFrame.loadDetailInContactDetailViewer(id);
	}

	public void focusSelectedRow() {
		table.requestFocus();
	}
}
