package com.contactmanager.vew;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

import com.contactmanager.datamodel.Log;
import com.contactmanager.datamodel.items.DataItemHandler;
import com.contactmanager.datamodel.items.DataType;
import com.contactmanager.utils.io.ConfigFileData;
import com.contactmanager.utils.viewutils.CustomDatePicker;

public class ContactLog extends JPanel {
	
	private static final int NUM_COLS = 10;
	private static final int NUM_ROWS = 10;
	private static final int COL_WIDTHS = 100;
	private static final int ROW_HEIGHTS = 20;
	
	
	private Map<String, Map<String, Object>> metaData = ConfigFileData.getInstance().getLogsMetaData();
	private Map<String, JTextComponent> textFieldMap = new HashMap<>();
	
	private Boolean isNewLog = true;
	private MainFrame pointerMainFrame;

	
	public ContactLog(MainFrame mainFrame) {
		pointerMainFrame = mainFrame;
		
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
		
		for (String dataId : metaData.keySet()) {
			if(dataId.equals("notes")) {continue;};
			
			JLabel label = new JLabel(metaData.get(dataId).get(DataItemHandler.DATA_LABEL_FIELD).toString() + ":");
			
			String[] placementsAsString = metaData.get(dataId).get(DataItemHandler.PLACEMENT_ON_DETAILS).toString().split(",");
			Integer[] gridPlacement = new Integer[2];
			gridPlacement[0] = Integer.parseInt(placementsAsString[0]);
			gridPlacement[1] = Integer.parseInt(placementsAsString[1]);

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = defaultPadding;
			gbc.gridx = gridPlacement[0]-1;
			gbc.gridy = gridPlacement[1];
			
			
			add(label,gbc);
			
			JTextComponent textField = new JTextField();
			gbc.gridx = gridPlacement[0];
			gbc.gridy = gridPlacement[1];
			gbc.gridwidth = 2;
			gbc.gridheight = 1;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			textField.setPreferredSize(new Dimension(gbc.gridwidth*COL_WIDTHS,gbc.gridheight*ROW_HEIGHTS));
			
			add(textField,gbc);
			textFieldMap.put(dataId, textField);

			if (!metaData.get(dataId).get(DataItemHandler.DATA_TYPE_ID).equals(DataType.DATE.toString())) {
				continue;
			}
			
			textField.setEditable(false);
			textField.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent ae) {
					if(ae.getButton() == MouseEvent.BUTTON1) {
						String date = new CustomDatePicker(mainFrame).setPickedDate();
						if(date.equals("")) return;
						
						textField.setText(date);
					}
					
				}
			});
			
			
		}
		
		JLabel lblNotes = new JLabel("Notes:");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = defaultPadding;
		gbc.gridx = 3;
		gbc.gridy = 0;
		add(lblNotes,gbc);
		
		JTextComponent textPane = new JTextPane();
		textPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		
		JScrollPane scrollPane= new JScrollPane(textPane);
		gbc = new GridBagConstraints();
		gbc.insets = defaultPadding;
		gbc.gridx = 4;
		gbc.gridy = 0;
		gbc.gridwidth = 6;
		gbc.gridheight = 10;
		gbc.fill = GridBagConstraints.BOTH;
		scrollPane.setPreferredSize(new Dimension(gbc.gridwidth*COL_WIDTHS,gbc.gridheight*ROW_HEIGHTS));
		add(scrollPane,gbc);
		textFieldMap.put("notes", textPane);
		
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
		
		addGlobalEventListener();
	}
	
	public void addGlobalEventListener() {
		KeyListener listener = new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				if(arg0.getKeyChar() == KeyEvent.VK_ESCAPE) {
					clear();
					exit();
				}	
			}
		};
		
		for (Component component: getComponents()) {
			component.addKeyListener(listener);
		}
		this.addKeyListener(listener);
	}
	
	public void clear() {
		for (String fieldKey : textFieldMap.keySet()) {
			textFieldMap.get(fieldKey).setText("");
		}
		isNewLog = true;
	}
	
	public void setContactLog(Log log) {
		for (String fieldKey : textFieldMap.keySet()) {
			textFieldMap.get(fieldKey).setText(log.getItemInfo(fieldKey).getDataValue());
		}
		isNewLog = false;;
	}
	
	
	private void exit() {
		pointerMainFrame.changePage(MainFrame.CONTACT_DETAIL);
	}
	private void save() {

		Log log = new Log(null);
		log.setValuesFromView(textFieldMap);

		pointerMainFrame.logToContactDetailView(log,isNewLog);
		exit();
	}
	
	
	
	private void exitPressed(ActionEvent e) {
		exit();
	}
	private void savePressed(ActionEvent e) {
		save();
	}
}
