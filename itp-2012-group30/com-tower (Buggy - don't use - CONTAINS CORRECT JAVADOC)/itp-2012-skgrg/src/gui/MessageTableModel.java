/*
 * Sergei Kostevitch
 * Mar 1, 2012
 */

package gui;


import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;
import file.XMLFileCreator;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageTableModel Creates the Columns and reads from XML for all the messages
 * Uses Observer pattern to check if a new Message Has been added - then adds it here
 */
public class MessageTableModel extends AbstractTableModel implements Observer{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7622692339657900923L;
	// private int[][] table = { new int[5], new int[5]};
	
	/** The column names. */
	private String[] columnNames = { "Priority", "Message Type", "Source",
			"Destination", "Date" };

	/** The file. */
	private XMLFileCreator file = XMLFileCreator.getXMLFileCreatorInstance();
	
	/** The data. */
	private Object[][] data;

	/**
	 * Instantiates a new message table model.
	 */
	public MessageTableModel() {

		data = file.readFromXMLFile();
		System.out.println("DATA CHANGED " + data.length);
	}

	
	/*Object[][] data = { {0, "A", new Double(1), Boolean.TRUE, new Date() },
	  {4, "B", new Double(2), Boolean.FALSE, new Integer(3)}, {4, "C", new
	  Double(9), Boolean.TRUE, new Integer(3) }, {8, "K", new Double(9),
	  Boolean.TRUE, new Integer(3) }, {2, "K", new Double(9), Boolean.TRUE, new
	  Integer(3) }, {3, "D", new Double(4), Boolean.FALSE, new Integer(3)} };
	 */

	
	

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
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

		return (data.length);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int r, int c) {

		return data[r][c];
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object object, int r, int c) {
		data[r][c] = ((Integer) object).intValue();
		fireTableCellUpdated(r, c);
	}


	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg1) {
		
		data = ((XMLFileCreator) o).readFromXMLFile();
		
		System.out.println("Read from file");
		this.fireTableDataChanged();
	}

}
