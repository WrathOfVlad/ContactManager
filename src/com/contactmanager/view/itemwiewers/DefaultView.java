package com.contactmanager.view.itemwiewers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.contactmanager.datamodel.singleitem.DefaultItem;

public class DefaultView extends ItemView{
	
	protected JTextField textField = new JTextField();
	
	public DefaultView(String dataId, Map<String, Object> metaData) throws Exception {
		initiate(dataId, metaData);
	}
	
	protected void putTextBoxOnPanel(JPanel panel) {
		GridBagConstraints gbc_txtField = new GridBagConstraints();
		gbc_txtField.anchor = GridBagConstraints.WEST;
		gbc_txtField.insets = new Insets(5,5,0,0);
		gbc_txtField.gridx = 3*placement[0]-1;
		gbc_txtField.gridy = placement[1];
		gbc_txtField.gridwidth = 2;
		gbc_txtField.fill = GridBagConstraints.HORIZONTAL;
		String regex = DefaultItem.getRegex(dataType);
		textField =	new JTextField();
		textField.getDocument().addDocumentListener(new DocumentListener()
	    {
	      @Override
	      public void removeUpdate(DocumentEvent e)
	      {
	        validateInput(regex,textField);
	      }
	      @Override
	      public void insertUpdate(DocumentEvent e)
	      {
	        validateInput(regex,textField);
	      }
	      @Override
	      public void changedUpdate(DocumentEvent e) {
	    	  validateInput(regex,textField);
	      } // Not needed for plain-text fields
	    });
		
		textField.setPreferredSize(new Dimension(100*gbc_txtField.gridwidth,20));
		panel.add(textField,gbc_txtField);
		textField.setEditable(false);
	}
	private void validateInput(String regex,JTextField field) {
		if(regex == null) {
			field.setForeground(Color.BLACK);
			return;
		}
		String text = field.getText();
	    Pattern r = Pattern.compile(regex);
	    Matcher m = r.matcher(text);
	    if (m.matches())
	    {
	    	field.setForeground(Color.BLACK);
	    }
	    else {
	    	field.setForeground(Color.RED);
	    }
	}

	protected void addToTabIndex(List<Map<Integer, JComponent>> tabOrder) {
		Map<Integer,JComponent> yMap = new TreeMap<Integer,JComponent>();
		while(tabOrder.size()<=placement[0]-1) {
			tabOrder.add(yMap);
		}
		tabOrder.get(placement[0]-1).put(placement[1], textField);	
	}
	
	@Override
	public void toggleEdit(Boolean activateEditModeIfTrue) {
		textField.setEditable(activateEditModeIfTrue && isEditable);
		isInEditMode = activateEditModeIfTrue;
	};
	
	@Override
	public String getTextFieldText() {
		return textField.getText();
	}
	
	@Override
	public void setTextFieldText(String text) {
		textField.setText(text);
	}

	protected void putLabelOnPanel(JPanel panel) {
		JLabel label = new JLabel( dataLabel+ ":");
		
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.fill = GridBagConstraints.HORIZONTAL;
		gbc_label.insets = new Insets(5, 5, 0, 0);
		gbc_label.gridx = 3*placement[0]-2;
		gbc_label.gridy = placement[1];
		panel.add(label,gbc_label);
	}
	
	@Override
	public void putItemOnPanel(JPanel panel,List<Map<Integer, JComponent>> tabOrder) {
		if(placement[0] == null || placement[1] == null) {return;}
		putLabelOnPanel(panel);
		putTextBoxOnPanel(panel);
		changedTextbox(panel);
		if(isEditable) {
			addToTabIndex(tabOrder);
		}
	}
	
	
	protected void changedTextbox(JPanel panel) {	};
}
