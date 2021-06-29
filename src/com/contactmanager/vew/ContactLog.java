package com.contactmanager.vew;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import com.contactmanager.utils.viewutils.DatePicker;

public class ContactLog extends JPanel {
	
	private static Integer LabelWidth = 100;
	private static Integer textFieldWidth = 200;
	private static Integer ySpacingBetweenElements = 25;
	
	private Map<String, Map<String, Object>> metaData = ConfigFileData.getInstance().getLogsMetaData();
	private Map<String, JTextComponent> textFieldMap = new HashMap<>();
	
	private Boolean isNewLog = true;
	private MainFrame pointerMainFrame;

	
	public ContactLog(MainFrame mainFrame) {
		pointerMainFrame = mainFrame;
		setLayout(null);
		
		Integer startX = 25;
		Integer startY = 75;
		
		
		for (String dataId : metaData.keySet()) {
			if(dataId.equals("notes")) {continue;};
			
			JLabel label = new JLabel(metaData.get(dataId).get(DataItemHandler.DATA_LABEL_FIELD).toString() + ":");
			
			String[] placementsAsString = metaData.get(dataId).get(DataItemHandler.PLACEMENT_ON_DETAILS).toString().split(",");
			Integer placement = Integer.parseInt(placementsAsString[0]);
			
			Integer currentX = startX;
			Integer currentY = startY + placement * ySpacingBetweenElements;
			
			label.setBounds(currentX,currentY,LabelWidth,20);
			add(label);
			
			JTextComponent textField = new JTextField();
			textField.setBounds(currentX + LabelWidth, currentY, textFieldWidth, 20);
			add(textField);
			textFieldMap.put(dataId, textField);

			if (!metaData.get(dataId).get(DataItemHandler.DATA_TYPE_ID).equals(DataType.DATE.toString())) {
				continue;
			}
			
			textField.setEditable(false);
			textField.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent ae) {
					String date = new DatePicker(mainFrame).setPickedDate();
					if(date.equals("")) return;
					
					textField.setText(date);
				}
			});
			
			
		}
		
		JLabel lblNotes = new JLabel("Notes:");
		lblNotes.setBounds(300, 25, 70, 15);
		add(lblNotes);
		
		JTextComponent textPane = new JTextPane();
		textPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		
		JScrollPane scrollPane= new JScrollPane(textPane);
		scrollPane.setBounds(360, 25, 500, 500);
		add(scrollPane);
		textFieldMap.put("notes", textPane);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitPressed(e);
			}
		});
		btnExit.setToolTipText("Exit Without Saving");
		btnExit.setBounds(25, 25, 60, 25);
		add(btnExit);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				savePressed(e);
			}
		});
		btnSave.setBounds(100, 25, 70, 25);
		add(btnSave);
		
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
