/*
 * Sergei Kostevitch
 * Mar 1, 2012
 */

package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageTableCellRenderer.
 */
public class MessageTableCellRenderer extends DefaultTableCellRenderer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2424303029678435553L;

	

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {


		// System.out.println(column);

		for (int i = 0; i <= row; i++) {

			// System.out.println(hasFocus);

			if ((Integer) table.getValueAt(i, 0) == 0) {

				if (isSelected == true) {

					setBackground(Color.BLUE);
					setForeground(Color.BLACK);

				} else {

					setBackground(Color.RED);
					setForeground(Color.WHITE);
				}
			}

			else if ((Integer) table.getValueAt(i, 0) == 4) {

				if (isSelected == true) {

					setBackground(Color.BLUE);
					setForeground(Color.BLACK);

				} else {

					setBackground(Color.CYAN);
					setForeground(Color.BLACK);
				}
			}

			else if ((Integer) table.getValueAt(i, 0) == 2) {

				if (isSelected == true) {

					setBackground(Color.BLUE);
					setForeground(Color.BLACK);

				} else {

					setBackground(Color.YELLOW);
					setForeground(Color.BLACK);
				}

			}
			
			else if ((Integer) table.getValueAt(i, 0) == 3) {

				if (isSelected == true) {

					setBackground(Color.BLUE);
					setForeground(Color.BLACK);

				} else {

					setBackground(Color.ORANGE);
					setForeground(Color.BLACK);
				}

			}
			
			else if ((Integer) table.getValueAt(i, 0) == 1) {

				if (isSelected == true) {

					setBackground(Color.BLUE);
					setForeground(Color.BLACK);

				} else {

					setBackground(Color.GREEN);
					setForeground(Color.BLACK);
				}

			}

			else if (isSelected == true) {

				setBackground(Color.BLUE);
				setForeground(Color.BLACK);

			}

			else {

				setBackground(Color.WHITE);
				setForeground(Color.BLACK);
			}

		}

		setText(value != null ? value.toString() : "");
		return this;
	}

}
