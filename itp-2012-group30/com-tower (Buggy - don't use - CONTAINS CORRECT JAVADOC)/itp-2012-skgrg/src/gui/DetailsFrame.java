/*
 * Sergei Kostevitch
 * Mar 14, 2012
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * The Details Table Frame is a Frame that pops up if a specific type of message is chosen
 * and details are wanted for it
 */
public class DetailsFrame extends JFrame{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4236661837296159844L;
	
	/** The details table. */
	private JTable detailsTable;
	
	/** The details model. */
	private DetailsTableModel detailsModel;
	
	/** The cell renderer. */
	private DetailsTableCellRenderer cellRenderer;
	
	/** The details table header. */
	private JTableHeader detailsTableHeader;

	/**
	 * Instantiates a new details frame.
	 *
	 * @param rowIndex the row index
	 * @param model the model
	 */
	public DetailsFrame(int rowIndex, MessageTableModel model){
		
		detailsModel = new DetailsTableModel(rowIndex, model);
		cellRenderer = new DetailsTableCellRenderer();
		detailsTable = new JTable(detailsModel);
		detailsTable.setRowHeight(20);
		detailsTableHeader = detailsTable.getTableHeader();
		detailsTableHeader.setBackground(Color.LIGHT_GRAY);
		detailsTable.setDefaultRenderer(Object.class, cellRenderer);
		detailsTable.getTableHeader().setReorderingAllowed(false);
		detailsTable.getTableHeader().setFont(new Font("Arial", 0, 15));
		detailsTable.setGridColor(Color.GRAY);
		detailsTable.setRowSelectionAllowed(false);
		detailsTable.setFocusable(false);
		getContentPane().add(detailsTable, BorderLayout.CENTER);
		getContentPane().add(detailsTableHeader, BorderLayout.NORTH);
		
		setCellTable();
	}
	
	/**
	 * Sets the cell table.
	 */
	private void setCellTable() {

		TableColumn columnSize = detailsTable.getColumnModel().getColumn(0);
		columnSize.setMinWidth(55);
		columnSize.setPreferredWidth(65);
		TableColumn columnSize1 = detailsTable.getColumnModel().getColumn(1);
		columnSize1.setMinWidth(100);
		columnSize1.setPreferredWidth(120);
	}
}
