package com.contactmanager.vew;

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
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

import com.contactmanager.datamodel.itemstypes.Contact;
import com.contactmanager.utils.io.ConfigFileData;

public class SettingsView extends JPanel {
	private JTable table;
	private DefaultTableModel tableModel;
	
	private MainFrame pointerMainFrame;
	
	private JScrollPane scrollPane;

	
	public SettingsView(MainFrame mainFrame) {
		pointerMainFrame = mainFrame;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		
		gridBagLayout.columnWidths =  new int[] {100,100,0};
		gridBagLayout.rowHeights = new int[] {20,20,0};
		
		gridBagLayout.rowWeights = new double[] {0,0,1};
		gridBagLayout.columnWeights = new double[] {0,0,1};
		setLayout(gridBagLayout);
		
		Insets defaultPadding = new Insets(5,5,0,0);
		
		
		table = new JTable();//CustomJTable();
		table.setDefaultEditor(Object.class, null);
		table.getTableHeader().setReorderingAllowed(false);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2 && table.getSelectedRow() != -1) {
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
		values.add(0,Contact.ID_FIELD);
		ConfigFileData.getInstance().setVisibleColumns(values);
		ConfigFileData.getInstance().saveVisibleColumns();
		pointerMainFrame.isContactListViewerUpToDate = false;
	}
	
	public void createTable() {
		tableModel = new DefaultTableModel();
		table.setModel(tableModel);
		
		
		List<String> columns = ConfigFileData.getInstance().getColumns(false);
		//columns.add(0,Contact.FULL_NAME_FIELD);
		List<String> displayedColumns = ConfigFileData.getInstance().getVisibleColumns();
		tableModel.addColumn("Column Names", columns.toArray());
		
		tableModel.addColumn("Is Visible");
		
		for (int i = 0; i < columns.size(); i++) {
			if (displayedColumns.contains(columns.get(i))) {
				tableModel.setValueAt("X", i, 1);
			}
		}
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//CustomComponents.resizeAllColumns(table);
		scrollPane.setViewportView(table);
		
		setFocusable(true);
		requestFocus();
		
	}
	
	public void exitPressed(ActionEvent e) {
		exit();
	}
}
