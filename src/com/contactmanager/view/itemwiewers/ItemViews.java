package com.contactmanager.view.itemwiewers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.contactmanager.datamodel.CurrentContactInfo;
import com.contactmanager.datamodel.items.Items;
import com.contactmanager.datamodel.items.Log;
import com.contactmanager.datamodel.singleitem.ExternalLoading;
import com.contactmanager.datamodel.singleitem.Item;
import com.contactmanager.view.MainFrame;

public class ItemViews {
	public static final String LABLE_TYPE = "dataLabel";
	
	private Map<String, ItemView> itemViewMap = new LinkedHashMap<>();
	
	private CurrentContactInfo contactInfo;
	private MainFrame mainFrame;
	
	public ItemViews(Map<String, Map<String,Object>> metaData,MainFrame mainFrame) {
		this.contactInfo = mainFrame.getContactInfo();
		this.mainFrame = mainFrame;
		try {
			initializeMap(metaData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initializeMap(Map<String, Map<String,Object>> metaData) throws Exception {
		ItemViewFactory factory = new ItemViewFactory();
		for (String dataId : metaData.keySet()) {
			if(metaData.get(dataId).containsKey(LABLE_TYPE)) {
				addToMap(dataId, factory.getItemView(dataId, metaData.get(dataId),mainFrame));
			}
		}
	}
	
	private void addToMap(String dataId, ItemView itemView) {
		itemViewMap.put(dataId,itemView);
	}
	
	public void displayItems(JPanel panel, List<Map<Integer,JComponent>> tabOrder) {
		for (ItemView item : itemViewMap.values()) {
			item.putItemOnPanel(panel, tabOrder);	
		}
	}
	
	public Boolean saveToDataModel(Map<String, Item> tempDataMap,Items items) {
		List<Boolean> isValidList = new ArrayList<>();
		for (String dataId : itemViewMap.keySet()) {
			Item item = tempDataMap.get(dataId);
			if(item.getExternalLoading() != null) {
				if(item.getExternalLoading().equals(ExternalLoading.LOGS)) {
					Log log= contactInfo.getLogs().getLatestLog();
					if(log != null)  {
						Item logItem = log.getItemInfo(dataId);
						ItemView itemView = itemViewMap.get(dataId);
						String dataValue = logItem.getDataValue();
						itemView.setTextFieldText(dataValue);
						isValidList.add(item.setDataValue(dataValue));
						continue;
					}
				}
			}
			
			ItemView itemView = itemViewMap.get(dataId);
			isValidList.add(item.setDataValue(itemView.getTextFieldText()));
		
			
			
			
		}
		if(isValidList.contains(false)) {return false;}
		return true;
	}
	public void loadFromDataModel(Items items) {
		for (String key : itemViewMap.keySet()) {
			ItemView itemView = itemViewMap.get(key);
			itemView.setTextFieldText(items.getItemInfo(key).getDataValue());
		}
	}
	
	
	public void toggleEdit(Boolean activateEditModeIfTrue) {
		for (ItemView itemView : itemViewMap.values()) {
			itemView.toggleEdit(activateEditModeIfTrue);
		}
	}
	
	
	public ItemView getItemView(String dataId) {
		return itemViewMap.get(dataId);
	}
	
	public List<String> getItemIds(){
		return new ArrayList<String>(itemViewMap.keySet());
	}
}
