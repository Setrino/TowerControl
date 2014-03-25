/*
 * Sergei Kostevitch
 * Mar 1, 2012
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import file.XMLFileCreator;

/**
 * The Class FrameMessageLog is the actual Messages Log Table
 * Has resizable function, the one that listens on focus depending on part of Table Model used
 * and so on
 */
public class FrameMessageLog extends JFrame implements ComponentListener, WindowListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6729844980788438867L;

	/** The model. */
	private final MessageTableModel model;
	
	/** The message table. */
	private final JTable messageTable;
	
	/** The message scrollpane. */
	private JScrollPane messageScrollpane;
	
	/** The message table cell. */
	private MessageTableCellRenderer messageTableCell;
	
	/** The sorter. */
	private TableRowSorter<MessageTableModel> sorter;
	
	/** The message bottom panel. */
	private MessageBottomPanel messageBottomPanel;
	
	/** The xml file. */
	private XMLFileCreator xmlFile = XMLFileCreator.getXMLFileCreatorInstance();
	
	/** The bar. */
	private JMenuBar bar;

	/**
	 * Instantiates a new frame message log.
	 *
	 * @param bar the bar
	 */
	public FrameMessageLog(JMenuBar bar) {

		this.bar = bar;
		setJMenuBar(bar);
		this.setPreferredSize(new Dimension(590, 350));
		this.setSize(new Dimension(590, 350));
		this.setMinimumSize(new Dimension(590, 350));
		this.setLocation(100, 100);
		model = new MessageTableModel();
		xmlFile.addObserver(model);

		messageTableCell = new MessageTableCellRenderer();
		messageTable = new JTable(model);
		sorter = new TableRowSorter<MessageTableModel>(model);
		messageTable.setRowSorter(sorter);
		messageTable.setRowHeight(20);
		this.addComponentListener(this);
		setCellTable();
		// messageTable.getColumnModel().getColumn(0).
		messageTable.setDefaultRenderer(Object.class, messageTableCell);
		System.out.println(messageTable.getRowCount());
		messageTable.getTableHeader().setReorderingAllowed(false);
		messageTable.getTableHeader().setFont(new Font("Arial", 0, 15));
		messageTable.setGridColor(Color.GRAY);
		messageScrollpane = new JScrollPane(messageTable,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		messageTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		messageTable.setRowSelectionAllowed(true);
		//messageTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		this.addWindowListener(this);
		
		messageTable.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				
				messageTable.getSelectionModel().clearSelection();

			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		messageTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent e) {
		    	
		    	int selectedRow = messageTable.getSelectedRow();
		    	
		    	if (selectedRow != -1) {
		    		
		    		messageBottomPanel.setSelectedRow(selectedRow);
				}
		        
		    }
		});
		
		// messageTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		getContentPane().add(messageScrollpane, BorderLayout.CENTER);
		// model.setValueAt(new Integer(1), 0, 0);

		
		getContentPane().add(messageBottomPanel = new MessageBottomPanel(sorter, model, this), BorderLayout.SOUTH);

		pack();
	}

	/**
	 * Sets the cell table size - each column has optimal size.
	 */
	public void setCellTable() {

		TableColumn columnSize = messageTable.getColumnModel().getColumn(0);
		columnSize.setMinWidth(55);
		columnSize.setPreferredWidth(65);
		// columnSize.setMaxWidth(80);
		TableColumn columnSize1 = messageTable.getColumnModel().getColumn(1);
		columnSize1.setMinWidth(140);
		columnSize1.setPreferredWidth(150);
		// columnSize1.setMaxWidth(100);
		TableColumn columnSize2 = messageTable.getColumnModel().getColumn(2);
		columnSize2.setMinWidth(120);
		columnSize2.setPreferredWidth(130);
		// columnSize2.setMaxWidth(100);
		TableColumn columnSize3 = messageTable.getColumnModel().getColumn(3);
		columnSize3.setMinWidth(125);
		columnSize3.setPreferredWidth(143);
		// columnSize3.setMaxWidth(100);
		TableColumn columnSize4 = messageTable.getColumnModel().getColumn(4);
		columnSize4.setMinWidth(133);
		columnSize4.setPreferredWidth(140);
		// columnSize4.setMaxWidth(100);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentMoved(ComponentEvent e) {
		
		System.out.println(this.getWidth());

		if (e.getComponent().getClass().getName()
				.equalsIgnoreCase("gui.FrameMessageLog")) {

			if (this.getWidth() >= (messageTable.getWidth() + 17)) {

				messageTable
						.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
			}

			else {
				messageTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent e) {
		
		this.setJMenuBar(bar);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
