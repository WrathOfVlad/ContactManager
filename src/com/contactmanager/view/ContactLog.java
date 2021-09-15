package com.contactmanager.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

import com.contactmanager.datamodel.CurrentContactInfo;
import com.contactmanager.datamodel.items.Log;
import com.contactmanager.utils.io.ConfigFileData;
import com.contactmanager.view.itemwiewers.ItemViews;

public class ContactLog extends JPanel {
	
	private static final int NUM_COLS = 10;
	private static final int NUM_ROWS = 10;
	private static final int COL_WIDTHS = 100;
	private static final int ROW_HEIGHTS = 20;
	
	private Boolean isNewLog = true;
	private MainFrame pointerMainFrame;
	private Log log;
	
	private JTextPane textPane;
	
	private ItemViews itemViews;
	private CurrentContactInfo contactInfo;
	
	
	public ContactLog(MainFrame mainFrame, CurrentContactInfo contactInfo) {
		pointerMainFrame = mainFrame;
		this.contactInfo = contactInfo;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		
		int[] cols = new int[NUM_COLS];
		Arrays.fill(cols, COL_WIDTHS);
		
		int[] rows = new int[NUM_ROWS];
		Arrays.fill(rows, ROW_HEIGHTS);
		
		gridBagLayout.columnWidths = cols;
		gridBagLayout.rowHeights = rows;
		
		
		setLayout(gridBagLayout);	
		
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
		
		List<Map<Integer, JComponent>> tabOrder = new LinkedList<Map<Integer, JComponent>>();
		
		itemViews = new ItemViews(ConfigFileData.getInstance().getLogsMetaData());
		
		itemViews.displayItems(this, tabOrder);
		
		itemViews.toggleEdit(true);
		
		
		
		JLabel lblNotes = new JLabel("Notes:");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = defaultPadding;
		gbc.gridx = 3;
		gbc.gridy = 0;
		add(lblNotes,gbc);
		
		textPane = new JTextPane();
		textPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		
		JScrollPane scrollPane= new JScrollPane(textPane);
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 20, 20);
		gbc.gridx = 4;
		gbc.gridy = 0;
		gbc.gridwidth = 6;
		gbc.gridheight = 10;
		gbc.fill = GridBagConstraints.BOTH;
		scrollPane.setPreferredSize(new Dimension(gbc.gridwidth*COL_WIDTHS,gbc.gridheight*ROW_HEIGHTS));
		add(scrollPane,gbc);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitPressed(e);
			}
		});
		btnExit.setToolTipText("Exit Without Saving");
		gbc = new GridBagConstraints();
		gbc.insets = defaultPadding;
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(btnExit,gbc);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				savePressed(e);
			}
		});
		gbc = new GridBagConstraints();
		gbc.insets = defaultPadding;
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(btnSave,gbc);
		
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Escape");
	    getActionMap().put("Escape", new AbstractAction() {
	        @Override
	        public void actionPerformed(ActionEvent ae) {
	        	escPressed();
	        }
	    });
	}
	
	private void escPressed() {
		exit();
	}
	
	public void clear() {
		log= (Log)contactInfo.getLogs().getSpecificItemWrapperClass(null);
		log.loadFromDataModel(itemViews);
		textPane.setText("");
		isNewLog = true;
	}
	
	public void setContactLog(Log items) {
		if(items != null) {
			this.log = items;
			items.loadFromDataModel(itemViews);
			textPane.setText(items.getItemInfo("notes").getDataValue());
			isNewLog = false;
		}
		else {
			clear();
		}
		
	}
	
	
	private void exit() {
		clear();
		pointerMainFrame.changePage(MainFrame.CONTACT_DETAIL);
	}
	private void save() {
		log.saveToDataModel(itemViews);
		log.getItemInfo("notes").setDataValue(textPane.getText());
		if(isNewLog){
			contactInfo.getLogs().addItem(log);
		}
		pointerMainFrame.getContactDetail().newLog(log, isNewLog);
		exit();
	}
	
	
	
	private void exitPressed(ActionEvent e) {
		exit();
	}
	private void savePressed(ActionEvent e) {
		save();
	}
}
