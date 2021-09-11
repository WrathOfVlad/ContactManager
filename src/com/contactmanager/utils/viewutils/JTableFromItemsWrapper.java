package com.contactmanager.utils.viewutils;

import java.awt.Font;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.contactmanager.datamodel.ItemsWrapper;
import com.contactmanager.datamodel.items.Items;

public class JTableFromItemsWrapper extends BetterJTable {
	
	private ItemsWrapper itemsWrapper;
	
	public JTableFromItemsWrapper(ItemsWrapper itemsWrapper) {
		super();
		this.itemsWrapper = itemsWrapper;
	}
	
	private String[] getVisibleDataFromFullRow(Items items) {
		return items.getVisibleRowSpecific();
	}
	
	public void addRowToTable(Items items) {
		String[] finalRow = getVisibleDataFromFullRow(items);
		
		tableModel.addRow(finalRow);
		resizeAllColumns();
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

	
	public int getSelectedId() {
		int rowIndex = getSelectedRow();
		
		int correctedIndex = convertRowIndexToModel(rowIndex);
    	
    	int id = Integer.parseInt(getModel().getValueAt(correctedIndex, getColumn(Items.ID_FIELD).getModelIndex()).toString());
        return id;
	}
}
