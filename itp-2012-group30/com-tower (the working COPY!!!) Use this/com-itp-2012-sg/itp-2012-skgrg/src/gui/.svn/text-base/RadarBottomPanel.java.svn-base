/*
 * Sergei Kostevitch
 * Mar 25, 2012
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

// TODO: Auto-generated Javadoc
/**
 * The Class RadarBottomPanel.
 */
public class RadarBottomPanel extends JPanel{
	
	/** The radar frame. */
	private RadarFrame radarFrame;
	
	/** The left label. */
	private JLabel leftLabel;
	
	/** The right label. */
	private JLabel rightLabel;
	
	/** The constrains. */
	private GridBagConstraints constrains;
	
	/** The pos x label. */
	private JLabel posXLabel;
	
	/** The pos y label. */
	private JLabel posYLabel;
	
	/** The plane id label. */
	private JLabel planeIDLabel;

	/**
	 * Instantiates a new radar bottom panel.
	 *
	 * @param radarFrame the radar frame
	 */
	public RadarBottomPanel(RadarFrame radarFrame){
		
		constrains = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		this.setBackground(Color.BLACK);
		this.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.WHITE));
		//this.setLayout(new BorderLayout(20,20));
		
		leftLabel = new JLabel(new ImageIcon("plane_IconF.png"));
		constrains.fill = GridBagConstraints.HORIZONTAL;
		constrains.anchor = GridBagConstraints.CENTER;
		constrains.ipady = 10;
		constrains.insets = new Insets(2, 0, 0, 0);
		constrains.gridx = 0;
		constrains.gridheight = 3;
		constrains.weightx = 0.0;
		constrains.gridy = 0;
		this.add(leftLabel, constrains);
		
		posXLabel = new JLabel("Pos X: " + "123");
		posXLabel.setForeground(Color.WHITE);
		constrains.fill = GridBagConstraints.HORIZONTAL;
		constrains.gridx = 1;
		constrains.gridy = 0;
		constrains.insets = new Insets(1, 2, 0, 0);
		this.add(posXLabel, constrains);
		
		posYLabel = new JLabel("Pos Y: " + "123");
		posYLabel.setForeground(Color.WHITE);
		constrains.fill = GridBagConstraints.HORIZONTAL;
		//constrains.insets = new Insets(0, 0, 10, 0);
		constrains.insets = new Insets(26, 2, 0, 0);
		constrains.gridx = 1;
		constrains.gridy = 1;
		this.add(posYLabel, constrains);
		
		planeIDLabel = new JLabel("PlaneID: " + "123");
		planeIDLabel.setForeground(Color.WHITE);
		constrains.fill = GridBagConstraints.HORIZONTAL;
		constrains.insets = new Insets(0, 2, 26, 0);
		constrains.gridx = 1;
		constrains.gridy = 2;
		this.add(planeIDLabel, constrains);
		
	}
}
