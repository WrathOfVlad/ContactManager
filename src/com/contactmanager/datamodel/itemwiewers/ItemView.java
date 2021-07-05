package com.contactmanager.datamodel.itemwiewers;

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

import com.contactmanager.datamodel.itemtypes.DataType;
import com.contactmanager.datamodel.itemtypes.DefaultItem;
import com.contactmanager.vew.MainFrame;

public abstract class ItemView {
	public static final String IS_EDITABLE_FIELD = "isEditable";
	public static final String DATA_LABEL_FIELD = "dataLabel";
	public static final String PLACEMENT_ON_DETAILS = "placementOnDetails";
	public static final String COMBO_FIELD = "comboField";
	
	protected DataType dataType;
	
	protected String dataLabel;
	protected Boolean isEditable = true;
	protected Integer[] placement = new Integer[2];
	
	
	protected JTextField textField = new JTextField();
	protected Boolean isInEditMode;
	protected MainFrame mainFrame;
	
	
	protected void initiate(String dataId, Map<String, Object> metaData) throws Exception {
		if(!metaData.containsKey(DATA_LABEL_FIELD)) {throw new Exception("Invalid Label");};
		if(!metaData.containsKey(PLACEMENT_ON_DETAILS)) {throw new Exception("Invalid Placement On Contact Detail");};
		
		this.dataLabel = metaData.get(DATA_LABEL_FIELD).toString();
		
		
		if(metaData.containsKey(IS_EDITABLE_FIELD)) {
			this.isEditable = Boolean.parseBoolean(metaData.get(IS_EDITABLE_FIELD).toString());
		}
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
	
	public void putItemOnPanel(JPanel panel,List<Map<Integer, JComponent>> tabOrder) {
		if(placement[0] == null || placement[1] == null) {return;}
		putLabelOnPanel(panel);
		putTextBoxOnPanel(panel);
		changedTextbox(panel);
		if(isEditable) {
			addToTabIndex(tabOrder);
		}
	}
	
	protected void changedTextbox(JPanel panel) {	}
	
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
		
		textField.setMinimumSize(new Dimension(100*gbc_txtField.gridwidth,20));
		textField.setMaximumSize(new Dimension((int) (100*gbc_txtField.gridwidth+100*0.5),20));
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
			tabOrder.add(placement[0]-1, yMap);
		}
		tabOrder.get(placement[0]-1).put(placement[1], textField);	
	}
	
	public void toggleEdit(Boolean activateEditModeIfTrue) {
		textField.setEditable(activateEditModeIfTrue);
		isInEditMode = activateEditModeIfTrue;
	};
	
	public String getTextFieldText() {
		return textField.getText();
	}
	public void setTextFieldText(String text) {
		textField.setText(text);
	}
	
}
