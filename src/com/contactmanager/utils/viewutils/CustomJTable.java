package com.contactmanager.utils.viewutils;

import java.awt.Component;
import java.awt.Font;
import java.util.List;

import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.contactmanager.datamodel.ItemsWrapper;
import com.contactmanager.datamodel.itemstypes.Items;

public class CustomJTable extends JTable {
	
	private ItemsWrapper itemsWrapper;
	private DefaultTableModel tableModel;
	private TableRowSorter<TableModel> sorter;
	
	public CustomJTable(ItemsWrapper itemsWrapper) {
		this.itemsWrapper = itemsWrapper;
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setDefaultEditor(Object.class, null);
		setAutoCreateRowSorter(true);
		//getTableHeader().setReorderingAllowed(false);
	}
	
	private String[] getVisibleDataFromFullRow(Items items) {
		return items.getVisibleRow();
	}
	
	public void addRowToTable(Items items) {
		String[] finalRow = getVisibleDataFromFullRow(items);
		
		tableModel.addRow(finalRow);
		resizeAllColumns();
	}
	
	private void resizeAllColumns() {
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

	public void updateRowInTable(Items items) {
		String[] row = getVisibleDataFromFullRow(items);
		//There's no need to add one here, as the column name row is only on mainTable, not on the actual displayed table
		int rowNumber = itemsWrapper.getIndexById(items.getIdAsInt());
		
		for(int i = 0; i < row.length; i++) {
			tableModel.setValueAt(row[i], rowNumber, i);
		}
	}
	
	public void loadData() {
		List<List<String>> rawData = itemsWrapper.getAllData();
		List<String> visibleColumns = itemsWrapper.getVisibleColumns();
		String[][] visibleData = new String[rawData.size()][visibleColumns.size()];
		List<Integer> ids = itemsWrapper.getIds();
		
		for (int i = 0; i<ids.size();i++) {
			String[] row = getVisibleDataFromFullRow(itemsWrapper.getWrapperById(ids.get(i)));
			for (int j = 0; j < row.length; j++) { 
				visibleData[i][j] = row[j];
			}
		}
		
		tableModel = new DefaultTableModel(visibleData, visibleColumns.toArray());
		sorter = new TableRowSorter<TableModel>(tableModel);
		setModel(tableModel);
		setRowSorter(sorter);
		
		JTableHeader header = getTableHeader();
		Font newFont = getFont().deriveFont(Font.BOLD);
		header.setFont(newFont);
		
		resizeAllColumns();
	}

	public void setRowFilter(String filter) {
		sorter.setRowFilter(RowFilter.regexFilter(filter));
	}
	
	public int getSelectedId() {
		int rowIndex = getSelectedRow();
		
		int correctedIndex = convertRowIndexToModel(rowIndex);
    	
    	int id = Integer.parseInt(getModel().getValueAt(correctedIndex, getColumn(Items.ID_FIELD).getModelIndex()).toString());
        return id;
	}
}
