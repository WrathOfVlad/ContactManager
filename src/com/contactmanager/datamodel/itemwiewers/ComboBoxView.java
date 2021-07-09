package com.contactmanager.datamodel.itemwiewers;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ComboBoxView extends DefaultView{
	
	private JComboBox<String> comboBox;
	
	private String[] comboItems;
	
	public ComboBoxView(String dataId, Map<String,Object> metaData) throws Exception {
		super(dataId, metaData);
		this.comboItems = metaData.get(COMBO_FIELD).toString().split(",");
	}

	public String[] getComboItems() {
		return comboItems.clone();
	}
	
	
	protected void changedTextbox(JPanel panel) {
		GridBagConstraints gbc_txtField = new GridBagConstraints();
		gbc_txtField.anchor = GridBagConstraints.WEST;
		gbc_txtField.insets = new Insets(5,5,0,0);
		gbc_txtField.gridx = 3*placement[0]-1;
		gbc_txtField.gridy = placement[1];
		gbc_txtField.gridwidth = 2;
		gbc_txtField.fill = GridBagConstraints.HORIZONTAL;
		
		comboBox= new JComboBox<>(comboItems);
		comboBox.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
		        @SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String selected= (String)cb.getSelectedItem();
		        textField.setText(selected);
		        
		    }
		});
		comboBox.setVisible(false);
		comboBox.setEnabled(false);
		comboBox.setEditable(true);
		panel.add(comboBox,gbc_txtField);
	}
	
	@Override
	public void toggleEdit(Boolean activateEditModeIfTrue) {
		textField.setVisible(!activateEditModeIfTrue);
		comboBox.setVisible(activateEditModeIfTrue);
		comboBox.setEnabled(activateEditModeIfTrue);
	}
	
	@Override
	protected void addToTabIndex(List<Map<Integer, JComponent>> tabOrder) {	}
}
