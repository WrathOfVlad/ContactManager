package com.contactmanager.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.contactmanager.datamodel.ItemsWrapper;
import com.contactmanager.utils.viewutils.CustomJTable;


public class ContactList extends JPanel{
	
	private CustomJTable table;
	
	private JScrollPane scrollPane;
	
	private MainFrame mainFrame;
	private JTextField searchField;
	
	private ItemsWrapper contacts;
	
	public ContactList(MainFrame mainFrame, ItemsWrapper contacts) {
		this.mainFrame = mainFrame;
		this.contacts = contacts;
		table = new CustomJTable(contacts);
		
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
			        table.setRowFilter("(?i)" + searchField.getText());
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
		
		scrollPane.setViewportView(table);
	} 

	public void loadData() {
		table.loadData();
	}
	
	private void enterContactDetail() {
		mainFrame.changePage(MainFrame.CONTACT_DETAIL);
        mainFrame.loadDetailInContactDetailViewer(table.getSelectedId());
	}
	public void focusSelectedRow() {
		table.requestFocus();
	}

	public void addRowToTable(int id) {
		table.addRowToTable(contacts.getWrapperById(id));
	}

	public void updateRowInTable(int id) {
		table.updateRowInTable(contacts.getWrapperById(id));
	}
}
