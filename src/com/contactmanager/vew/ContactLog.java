package com.contactmanager.vew;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;

import com.contactmanager.datamodel.Log;

public class ContactLog extends JPanel {
	private JTextPane textPane;
	
	private Map<String, JTextField> textFieldMap = new HashMap<>();
	
	private MainFrame pointerMainFrame;

	/**
	 * Create the panel.
	 */
	public ContactLog(MainFrame mainFrame) {
		pointerMainFrame = mainFrame;
		setLayout(null);
		
		String[] columns = Log.getColumnNames();
		
		JLabel lblDate = new JLabel(columns[0] + ":");
		lblDate.setBounds(26, 76, 50, 15);
		add(lblDate);
		
		JLabel lblType = new JLabel(columns[1] + ":");
		lblType.setBounds(26, 101, 50, 15);
		add(lblType);
		
		JLabel lblNextTime = new JLabel(columns[2]+":");
		lblNextTime.setBounds(26, 126, 82, 15);
		add(lblNextTime);
		
		JLabel lblAction = new JLabel(columns[3] + ":");
		lblAction.setBounds(26, 151, 70, 15);
		add(lblAction);
		
		JLabel lblStatus = new JLabel(columns[4] + ":");
		lblStatus.setBounds(26, 176, 70, 15);
		add(lblStatus);
		
		JTextField textField_1 = new JTextField();
		textField_1.setBounds(101, 99, 125, 19);
		add(textField_1);
		textField_1.setColumns(10);
		textFieldMap.put(columns[1], textField_1);
		
		JTextField textField_3 = new JTextField();
		textField_3.setBounds(101, 151, 125, 19);
		add(textField_3);
		textField_3.setColumns(10);
		textFieldMap.put(columns[3], textField_3);
		
		JTextField textField_4 = new JTextField();
		textField_4.setBounds(101, 176, 125, 19);
		add(textField_4);
		textField_4.setColumns(10);
		textFieldMap.put(columns[4], textField_4);
		
		
		JLabel lblNotes = new JLabel("Notes:");
		lblNotes.setBounds(300, 25, 70, 15);
		add(lblNotes);
		
		textPane = new JTextPane();
		textPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		textPane.setBounds(360, 25, 500, 500);
		add(textPane);
		
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
		
		MaskFormatter dateFormat = null;
		try {
			dateFormat = new MaskFormatter("####-##-##");

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JFormattedTextField formattedTextField = new JFormattedTextField(dateFormat);
		formattedTextField.setBounds(100, 75, 125, 19);
		add(formattedTextField);
		textFieldMap.put(columns[0], formattedTextField);	
		
		JFormattedTextField formattedTextField_1 = new JFormattedTextField(dateFormat);
		formattedTextField_1.setBounds(100, 125, 125, 19);
		add(formattedTextField_1);
		textFieldMap.put(columns[2], formattedTextField_1);	
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
		textPane.setText("");
		textPane.requestFocus();
	}
	
	private void exit() {
		pointerMainFrame.changePage(MainFrame.CONTACT_DETAIL);
	}
	private void save() {
		String[] columns = Log.getColumnNames();
		try{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate.parse(textFieldMap.get(columns[0]).getText(), formatter);
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(pointerMainFrame, "Invalid Date");
			return;
		}
		
		String[] log = new String[Log.getColumnNames().length];
		
		
		
		log[0] = textFieldMap.get(columns[0]).getText();
		log[1] = textFieldMap.get(columns[1]).getText();
		log[2] = textFieldMap.get(columns[2]).getText();
		log[3] = textFieldMap.get(columns[3]).getText();
		log[4] = textFieldMap.get(columns[4]).getText();
		log[5] = textPane.getText();
		
		pointerMainFrame.newLogInContactDetailView(log);
		exit();
	}
	
	
	
	private void exitPressed(ActionEvent e) {
		exit();
	}
	private void savePressed(ActionEvent e) {
		save();
	}
}
