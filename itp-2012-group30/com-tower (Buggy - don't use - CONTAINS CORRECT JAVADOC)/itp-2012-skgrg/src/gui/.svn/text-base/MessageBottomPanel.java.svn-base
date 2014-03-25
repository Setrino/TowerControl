/*
 * Sergei Kostevitch
 * Mar 2, 2012
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.regex.PatternSyntaxException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

import file.XMLFileCreator;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageBottomPanel.
 */
public class MessageBottomPanel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -37612118569715296L;

	/** The details. */
	private JButton details;
	
	/** The close. */
	private JButton close;
	
	/** The right panel. */
	private JPanel rightPanel;
	
	/** The left panel. */
	private JPanel leftPanel;
	
	/** The filter combo box. */
	private JComboBox filterComboBox;
	
	/** The filter by. */
	private JLabel filterBy;
	
	/** The search field. */
	private JTextField searchField;
	
	/** The search button. */
	private JButton searchButton;
	
	/** The sorter. */
	private TableRowSorter<MessageTableModel> sorter;
	
	/** The model. */
	private MessageTableModel model;
	
	/** The details frame. */
	private DetailsFrame detailsFrame;
	
	/** The selected row. */
	private int selectedRow;
	
	/** The xml file. */
	private XMLFileCreator xmlFile = XMLFileCreator.getXMLFileCreatorInstance();
	
	/** The message log. */
	private FrameMessageLog messageLog;

	/**
	 * Instantiates a new message bottom panel.
	 *
	 * @param sorter the sorter
	 * @param model the model
	 * @param frame the frame
	 */
	public MessageBottomPanel(final TableRowSorter<MessageTableModel> sorter, MessageTableModel model, FrameMessageLog frame) {

		this.sorter = sorter;
		this.model = model;
		messageLog = frame;
		rightPanel = new JPanel(new FlowLayout(1, 3, 3));
		details = new JButton("Details");
		details.addActionListener(detailListener);
		close = new JButton("Close");
		close.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				messageLog.dispose();
			}
		});
		
		this.setLayout(new BorderLayout());
		this.add(rightPanel, BorderLayout.EAST);
		rightPanel.add(details);
		rightPanel.add(close);

		comboBox();
		searchButton = new JButton("Search");
		searchField = new JTextField("Search filter...  ");
		searchButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {

				if (!searchField.getText().equals("Search filter...  ")) {

					System.out.println(searchField.getText());
					// newFilter();
				}
				;
			}
		});

		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = searchField.getText();
				if (text.equals("Search filter...  ") || text.equals("")) {
					sorter.setRowFilter(null);
				} else {

					sorter.setRowFilter(RowFilter.regexFilter(text));

				}
			}
		});

		filterBy = new JLabel("Filter by");
		searchField.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {

				if (e.getSource() == searchField
						&& searchField.getText().equals("")) {
					searchField.setText("Search filter...  ");
				}
			}

			@Override
			public void focusGained(FocusEvent e) {

				if (e.getSource() == searchField) {
					searchField.setText("");
				}
			}
		});

		/*
		 * searchField.getDocument().addDocumentListener(new DocumentListener()
		 * { public void changedUpdate(DocumentEvent e) { //newFilter(); }
		 * 
		 * public void insertUpdate(DocumentEvent e) { //newFilter(); }
		 * 
		 * public void removeUpdate(DocumentEvent e) { //newFilter(); } });
		 */

		// searchField.setSize
		leftPanel = new JPanel(new FlowLayout(1, 3, 3));
		this.add(leftPanel, BorderLayout.WEST);
		leftPanel.add(filterBy);
		leftPanel.add(filterComboBox);
		leftPanel.add(searchField);
		leftPanel.add(searchButton);

	}

	/**
	 * Combo box.
	 */
	public void comboBox() {

		String[] filterChoices = { "Priority", "Message Type", "Source",
				"Destination", "Date" };
		filterComboBox = new JComboBox(filterChoices);
		filterComboBox.setSelectedIndex(2);
		filterComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JComboBox box = (JComboBox) e.getSource();
				String choice = box.getSelectedItem().toString();
				box.setSelectedItem(choice);
				
				//System.out.println();
				
				System.out.println(box.getSelectedIndex());

				sorter.toggleSortOrder(box.getSelectedIndex());

				// sorter.setRowFilter(RowFilter.regexFilter(choice));
			}
		});

	}

	/**
	 * New filter.
	 */
	private void newFilter() {

		RowFilter<MessageTableModel, Object> rowFilter = null;

		try {

			/*
			 * if (searchField.getText().equals("Search filter...  ")) {
			 * 
			 * sorter.setRowFilter(null);
			 * 
			 * } else {
			 */

			rowFilter = RowFilter.regexFilter(searchField.getText(), 0);

		} catch (PatternSyntaxException e) {

			e.getStackTrace();
		}

		sorter.setRowFilter(rowFilter);
	}
	
	/** The detail listener. */
	ActionListener detailListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			//xmlFile.checkFilePresenceAndWriteMessageToFile("HELLO", "Tower", "ALLUO", "4");
			model.update(xmlFile, model);
			model.fireTableDataChanged();
			detailsFrame = new DetailsFrame(selectedRow, model);
			detailsFrame.setTitle("Details");
			detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			//detailsFrame.setPreferredSize(new Dimension (200, 400));
			detailsFrame.setLocation(500, 300);
			detailsFrame.setVisible(true);
			detailsFrame.isAlwaysOnTop();
			detailsFrame.setMinimumSize(new Dimension(350, 200));
			
		}
	};

	/**
	 * Sets the selected row.
	 *
	 * @param selectedRow the new selected row
	 */
	public void setSelectedRow(int selectedRow) {
		
		this.selectedRow = selectedRow;
	}

}
