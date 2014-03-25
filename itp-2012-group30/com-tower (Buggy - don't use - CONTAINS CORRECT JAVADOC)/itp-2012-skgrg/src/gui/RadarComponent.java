/*
 * Sergei Kostevitch
 * Mar 25, 2012
 */

package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JComponent;

// TODO: Auto-generated Javadoc
/**
 * The Class RadarComponent was used for my radar Frame when I first time created it on March 25th
 * Its used with combination with PlaneDrawn, so both are DEPRECATED now :/
 */
public class RadarComponent extends JComponent {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3726980199403031754L;

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D) g;

		Image radarImage = Toolkit.getDefaultToolkit().getImage(
				"radar_Control540B.png");

		g2.drawImage(radarImage, 24, 13, this);
		//g2.finalize();
	}
}