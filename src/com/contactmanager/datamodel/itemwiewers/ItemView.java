package com.contactmanager.datamodel.itemwiewers;

import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.contactmanager.datamodel.singleitem.DataType;
import com.contactmanager.datamodel.singleitem.Item;
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
	
	
	protected Boolean isInEditMode = false;
	protected MainFrame mainFrame;
	
	
	protected void initiate(String dataId, Map<String, Object> metaData) throws Exception {
		
		dataType = DataType.valueOf(metaData.get(Item.DATA_TYPE_ID).toString());
		
		if(!metaData.containsKey(PLACEMENT_ON_DETAILS)) {throw new Exception("Invalid Placement On Contact Detail");};
		
		String[] placementStrings = metaData.get(PLACEMENT_ON_DETAILS).toString().split(",");
		placement[0] = Integer.parseInt(placementStrings[0]);
		placement[1] = Integer.parseInt(placementStrings[1]);
		
		this.dataLabel = metaData.get(DATA_LABEL_FIELD).toString();
		
		
		if(metaData.containsKey(IS_EDITABLE_FIELD)) {
			this.isEditable = Boolean.parseBoolean(metaData.get(IS_EDITABLE_FIELD).toString());
		}
	}
	
	public abstract void putItemOnPanel(JPanel panel,List<Map<Integer, JComponent>> tabOrder);

	public abstract String getTextFieldText();
	
	public abstract void setTextFieldText(String value);
	
	public abstract void toggleEdit(Boolean activateEditModeIfTrue);
}
