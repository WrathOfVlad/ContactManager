package com.contactmanager.utils.viewutils;

import javax.swing.text.*;

import java.awt.Component;
import java.awt.Toolkit;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


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

	public static void resizeAllColumns(JTable table) {
		for (int column = 0; column < table.getColumnCount(); column++)
		{
		    TableColumn tableColumn = table.getColumnModel().getColumn(column);
		    int preferredWidth = tableColumn.getMinWidth();
		    int maxWidth = tableColumn.getMaxWidth();
		    
		    TableCellRenderer headerRenderer = tableColumn.getHeaderRenderer();
		    Object headerValue = tableColumn.getHeaderValue();
		    if (headerRenderer == null) {
		      headerRenderer = table.getTableHeader().getDefaultRenderer();
		    }
		    Component headerComp =
		            headerRenderer.getTableCellRendererComponent(table, headerValue, false, false, 0, column);
		    
		    int width = headerComp.getPreferredSize().width + table.getIntercellSpacing().width + 15;
	    	width = Math.max(width, headerComp.getPreferredSize().width + 10);
	        preferredWidth = Math.max(preferredWidth, width);

		    
		    for (int row = 0; row < table.getRowCount(); row++)
		    {
		        TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
		        Component c = table.prepareRenderer(cellRenderer, row, column);
		        
	        	width = c.getPreferredSize().width + table.getIntercellSpacing().width + 15;
		        preferredWidth = Math.max(preferredWidth, width);

		        //  We've exceeded the maximum width, no need to check other rows
		 
		        if (preferredWidth >= maxWidth)
		        {
		            preferredWidth = maxWidth;
		            break;
		        }
		    }
		 
		    tableColumn.setPreferredWidth( preferredWidth );
		}
	}
}
