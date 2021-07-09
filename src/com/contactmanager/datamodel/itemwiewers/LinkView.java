package com.contactmanager.datamodel.itemwiewers;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.contactmanager.datamodel.singleitem.LinkItem;

public class LinkView extends DefaultView {
	
	public LinkView(String dataId, Map<String, Object> metaData) throws Exception {
		super(dataId, metaData);
	}

	JLabel linkLabel = new JLabel();
	
	@Override
	protected void changedTextbox(JPanel panel ) {
		GridBagConstraints gbc_txtField = new GridBagConstraints();
		gbc_txtField.anchor = GridBagConstraints.WEST;
		gbc_txtField.insets = new Insets(5,5,0,0);
		gbc_txtField.gridx = 3*placement[0]-1;
		gbc_txtField.gridy = placement[1];
		gbc_txtField.gridwidth = 2;
		gbc_txtField.fill = GridBagConstraints.HORIZONTAL;
		
		linkLabel = new JLabel(dataLabel);
		linkLabel.setVisible(false);
		linkLabel.setEnabled(false);
		linkLabel.setForeground(Color.BLUE.darker());
		linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		linkLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					callURL();
				}
				
			}
		});
		panel.add(linkLabel,gbc_txtField);
	}
	@Override
	public void toggleEdit(Boolean activateEditModeIfTrue ) {
		linkLabel.setVisible(!activateEditModeIfTrue);
		textField.setVisible(activateEditModeIfTrue);
		textField.setEditable(activateEditModeIfTrue);
		linkLabel.setEnabled(LinkItem.validateURL(textField.getText()));
		
	}

	public void callURL() {
		if(isInEditMode) {
			return;
		}
		
		boolean isValid = LinkItem.validateURL(textField.getText());
		if (isValid) {
			try {
				mainFrame.openURL(textField.getText());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(mainFrame, "Something went wrong");
			}
		}
		else {
			JOptionPane.showMessageDialog(mainFrame, "Invalid Link");
		}
	}
}
