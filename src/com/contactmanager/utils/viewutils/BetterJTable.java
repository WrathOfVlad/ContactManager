package com.contactmanager.utils.viewutils;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class BetterJTable extends JTable{
	
	
	protected DefaultTableModel tableModel;
	protected TableRowSorter<TableModel> sorter;
	
	public BetterJTable() {
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setDefaultEditor(Object.class, null);
		setUI(new MyTableUI());
		
	}
	
	public void setRowFilter(String filter) {
		sorter.setRowFilter(RowFilter.regexFilter(filter));
	}
	
	protected void resizeAllColumns() {
		for (int column = 0; column < getColumnCount(); column++)
		{
		    TableColumn tableColumn = getColumnModel().getColumn(column);
		    int preferredWidth = tableColumn.getMinWidth();
		    int maxWidth = tableColumn.getMaxWidth();
		    
		    TableCellRenderer headerRenderer = tableColumn.getHeaderRenderer();
		    Object headerValue = tableColumn.getHeaderValue();
		    if (headerRenderer == null) {
		      headerRenderer = getTableHeader().getDefaultRenderer();
		    }
		    Component headerComp =
		            headerRenderer.getTableCellRendererComponent(this, headerValue, false, false, 0, column);
		    
		    int width = headerComp.getPreferredSize().width + getIntercellSpacing().width + 15;
	    	width = Math.max(width, headerComp.getPreferredSize().width + 10);
	        preferredWidth = Math.max(preferredWidth, width);

		    
		    for (int row = 0; row < getRowCount(); row++)
		    {
		        TableCellRenderer cellRenderer = getCellRenderer(row, column);
		        Component c = prepareRenderer(cellRenderer, row, column);
		        
	        	width = c.getPreferredSize().width + getIntercellSpacing().width + 15;
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
	
	public void clear() {
		if(tableModel == null) {
			return;
		}
		tableModel.setRowCount(0);
	}

	public void loadData(String[] cols, String[][] data) {
		tableModel = new DefaultTableModel(data,cols);
		setModel(tableModel);
		resizeAllColumns();
	}
}
