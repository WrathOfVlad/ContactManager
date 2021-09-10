package com.contactmanager.view.itemwiewers;

import java.util.Map;

import javax.swing.JTextPane;

public class TextPaneView extends DefaultView{

	JTextPane textPane = new JTextPane();
	
	public TextPaneView(String dataId, Map<String, Object> metaData) throws Exception {
		super(dataId, metaData);
	}
	
	@Override
	public String getTextFieldText() {
		return textPane.getText();
	}
	
	@Override
	public void setTextFieldText(String text) {
		textPane.setText(text);
	}
	

	
	
}
