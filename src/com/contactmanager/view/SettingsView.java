package com.contactmanager.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import com.contactmanager.datamodel.CurrentContactInfo;
import com.contactmanager.datamodel.items.Items;
import com.contactmanager.utils.io.ConfigFileData;
import com.contactmanager.utils.viewutils.BetterJTable;

public class SettingsView extends JPanel {
	private BetterJTable table;
	
	private MainFrame mainFrame;
	private CurrentContactInfo contactInfo;
	
	private JScrollPane scrollPane;
	

	
	public SettingsView(MainFrame mainFrame,CurrentContactInfo contactInfo) {
		this.mainFrame = mainFrame;
		this.contactInfo = contactInfo;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		
		gridBagLayout.columnWidths =  new int[] {100,100,0};
		gridBagLayout.rowHeights = new int[] {20,20,0};
		
		gridBagLayout.rowWeights = new double[] {0,0,1};
		gridBagLayout.columnWeights = new double[] {0,0,1};
		setLayout(gridBagLayout);
		
		Insets defaultPadding = new Insets(5,5,0,0);
		
		
		table = new BetterJTable();//CustomJTable();
		table.setDefaultEditor(Object.class, null);
		table.getTableHeader().setReorderingAllowed(false);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2 && table.getSelectedRow() != -1) {
					int row = table.getSelectedRow();
					if (table.getValueAt(row, 1) == "X") {
						table.setValueAt("", row, 1);
					}
					else {
						table.setValueAt("X", row, 1);
					}
				}
			}
		});
		
		JButton saveButton = new JButton("Save");		
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.insets = defaultPadding;		
		add(saveButton,gbc);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				exitPressed(arg0);
			}
		});
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = defaultPadding;	
		add(btnExit,gbc);
		
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 2;
		gbc.gridwidth =3;
		gbc.insets = defaultPadding;
		add(scrollPane,gbc);
	
		createTable();
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
	    getActionMap().put("Escape", new AbstractAction() {
	        @Override
	        public void actionPerformed(ActionEvent ae) {
	        	escPressed();
	        }
	    });
	    btnExit.setFocusable(true);
	    btnExit.requestFocus();
	}
	
	private void escPressed() {
		exit();
	}
	
	private void exit() {
		mainFrame.changePage(MainFrame.CONTACT_LIST);
		createTable();
	}
	
	private void save() {
		
		List<String> values = new ArrayList<>();
		for (int i = 0; i<table.getRowCount();i++) {
			if(table.getValueAt(i, 1) == "X") {
				values.add(contactInfo.getContacts().getDataIdByLabel(table.getValueAt(i, 0).toString()));
			}
		}
		ConfigFileData.getInstance().setVisibleColumns(values);
		ConfigFileData.getInstance().saveVisibleColumns();
		contactInfo.getContacts().loadVisibleColumns();
		mainFrame.isContactListViewerUpToDate = false;
		exit();
	}
	
	public void createTable() {
		
		//all Columns
		
		List<String> columns = contactInfo.getContacts().columnList();
		List<String> columnsLabels = new ArrayList<String>();
		
		for (String col : columns) {
			columnsLabels.add(contactInfo.getContacts().getLabel(col));
		}
		columnsLabels.add(Items.FULL_NAME_FIELD);
		
		columns = null;
		
		//displayed columns
		
		List<String> displayedColumns = mainFrame.getContactInfo().getContacts().getVisibleColumns();
		List<String> displayedColumnsLable = new ArrayList<String>();
		
		for (String dispCol : displayedColumns) {
			displayedColumnsLable.add(contactInfo.getContacts().getLabel(dispCol));
		}
		displayedColumns= null;
		
		String[][] allData = new String[columnsLabels.size()][2];
		
		
		for(int i = 0; i< displayedColumnsLable.size();i++) {
			allData[i][0] = displayedColumnsLable.get(i);
			allData[i][1] = "X";
			columnsLabels.remove(displayedColumnsLable.get(i));
		}
		
		for (int i = 0; i < columnsLabels.size(); i++) {
			allData[displayedColumnsLable.size()+i][0] = columnsLabels.get(i);
		}
		String[] cols = {"Column Name", "Is Visible"};
		table.loadData(cols, allData);
		
		scrollPane.setViewportView(table);
		
		setFocusable(true);
		requestFocus();
		
	}
	
	public void exitPressed(ActionEvent e) {
		exit();
	}
}
