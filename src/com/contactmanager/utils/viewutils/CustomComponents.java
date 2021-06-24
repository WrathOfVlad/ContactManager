package com.contactmanager.utils.viewutils;

import javax.swing.text.*;

import java.awt.Toolkit;

import javax.swing.*;


public class CustomComponents {
	
	public JTextField createFilteredField(String regex,int maxCharacters) {
		   JTextField field = new JTextField(10);
		   
		   if(regex == null) {
			   return field;
		   }
		   
		   
		   AbstractDocument document = (AbstractDocument) field.getDocument();
		   document.setDocumentFilter(new DocumentFilter() {

		      public void remove(FilterBypass fb, int offset, int length) throws
		            BadLocationException {
		         String text = fb.getDocument().getText(0, fb.getDocument().getLength());
		         String newText = text.substring(0, offset) + text.substring(offset + length);
		         if (newText.matches(regex) || newText.length() == 0) {
		            super.remove(fb, offset, length);
		         }
		      }

		      public void replace(FilterBypass fb, int offset, int length,
		                          String _text, AttributeSet attrs) throws BadLocationException {

		         String text = fb.getDocument().getText(0, fb.getDocument().getLength());
		         String newText = text.substring(0, offset) + _text + text.substring(offset + length);
		         if (newText.length() <= maxCharacters && newText.matches(regex)) {
		            super.replace(fb, offset, length, _text, attrs);
		         } else {
		            Toolkit.getDefaultToolkit().beep();
		         }
		      }

		      public void insertString(FilterBypass fb, int offset, String string,
		                               AttributeSet attr) throws BadLocationException {

		         String text = fb.getDocument().getText(0, fb.getDocument().getLength());
		         String newText = text.substring(0, offset) + string + text.substring(offset);
		         if ((fb.getDocument().getLength() + string.length()) <= maxCharacters
		               && newText.matches(regex)) {
		            super.insertString(fb, offset, string, attr);
		         } else {
		            Toolkit.getDefaultToolkit().beep();
		         }
		      }
		   });
		   return field;
		}
}
