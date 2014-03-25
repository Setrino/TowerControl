/*
 * Sergei Kostevitch
 * Mar 14, 2012
 */

package gui;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import file.XMLFileCreator;

// TODO: Auto-generated Javadoc
/**
 * The Class DetailsTableModel.
 */
public class DetailsTableModel extends AbstractTableModel{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1838204791811711991L;
	
	/** The column names. */
	private String[] columnNames = {"Node", "Details"};
	
	/** The xml file. */
	private XMLFileCreator xmlFile = XMLFileCreator.getXMLFileCreatorInstance();
	
	/** The data. */
	private String[][] data;
	
	/**
	 * Instantiates a new details table model.
	 *
	 * @param rowIndex the row index
	 * @param model the model
	 */
	public DetailsTableModel(int rowIndex, MessageTableModel model){
		
		data = xmlFile.readFromXMLFile(rowIndex);
		
		System.out.println(data[2][1]);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column){		
		return columnNames[column];
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		
		return columnNames.length;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		
		return data.length;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int r, int c) {
		
		return data[r][c];
	}

}
