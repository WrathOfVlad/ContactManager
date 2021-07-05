package com.contactmanager.datamodel.itemwiewers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.contactmanager.datamodel.itemtypes.DataType;
import com.contactmanager.utils.viewutils.CustomDatePicker;

public class DateView extends ItemView{
	
	public DateView() {
		this.dataType = DataType.DATE;
		
	}
	
	@Override
	protected void changedTextbox(JPanel panel) {
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent ae) {
				if(ae.getButton() == MouseEvent.BUTTON1) {
					if(isInEditMode && isEditable) {
						String date = new CustomDatePicker().setPickedDate();
						if(date.equals("")) return;
						textField.setText(date);
					}
				}
			}
		});
	}
	
	@Override
	public void toggleEdit(Boolean activateEditModeIfTrue ) {
		if (isEditable) {
			isInEditMode = activateEditModeIfTrue;
		}
	}
	
	@Override
	protected void addToTabIndex(List<Map<Integer, JComponent>> tabOrder) {	}


}
