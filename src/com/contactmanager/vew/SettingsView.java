package com.contactmanager.vew;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.contactmanager.datamodel.Contact;
import com.contactmanager.utils.io.ConfigFileData;

public class SettingsView extends JPanel {
	private JTable table;
	private DefaultTableModel tableModel;
	
	private ConfigFileData pointerConfigFileData;
	private MainFrame pointerMainFrame;

	
	public SettingsView(MainFrame mainFrame, ConfigFileData configFileData) {
		pointerMainFrame = mainFrame;
		pointerConfigFileData = configFileData;
		
		table = new JTable();
		table.setDefaultEditor(Object.class, null);
		table.getTableHeader().setReorderingAllowed(false);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
					int row = table.getSelectedRow();
					String field = table.getValueAt(row, 0).toString();
					if (field != "Id") {
						if (table.getValueAt(row, 1) == "X") {
							table.setValueAt("", row, 1);
						}
						else {
							table.setValueAt("X", row, 1);
						}
					}
				}
			}
		});
		
		JButton btnNewButton = new JButton("Save");
		btnNewButton.setBounds(120, 50, 75, 20);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		setLayout(null);
		add(btnNewButton);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(null);
		scrollPane.setBounds(200, 50, 400, 800);
		add(scrollPane);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				exitPressed(arg0);
			}
		});
		btnExit.setBounds(25, 50, 75, 20);
		add(btnExit);
		createTable();
		addGlobalEventListener();
	}
	
	public void addGlobalEventListener() {
		KeyListener listener = new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				if(arg0.getKeyChar() == KeyEvent.VK_ESCAPE) {
					exit();
				}
			}
		};
		
		for (Component component: getComponents()) {
			component.addKeyListener(listener);
		}
		this.addKeyListener(listener);
	}
	
	private void exit() {
		createTable();
		pointerMainFrame.changePage(MainFrame.CONTACT_LIST);
	}
	
	private void save() {
		
		List<String> values = new ArrayList<>();
		for (int i = 0; i<table.getRowCount();i++) {
			if(table.getValueAt(i, 1) == "X") {
				values.add(table.getValueAt(i, 0).toString());
			}
		}
		Contact.setVisibleColumns(values);
		pointerConfigFileData.saveVisibleColumns();
		pointerMainFrame.isContactListViewerUpToDate = false;
	}
	
	public void createTable() {
		tableModel = new DefaultTableModel();
		table.setModel(tableModel);
		List<String> columns = Contact.getColumnNames(true);
		List<String> displayedColumns = Contact.getVisibleColumns();
		tableModel.addColumn("Column Names", columns.toArray());
		
		tableModel.addColumn("Is Visible");
		
		for (int i = 0; i < columns.size(); i++) {
			if (displayedColumns.contains(columns.get(i))) {
				tableModel.setValueAt("X", i, 1);
			}

		}
	}
	
	public void exitPressed(ActionEvent e) {
		exit();
	}
}
