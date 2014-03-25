/*
 * Sergei Kostevitch
 * Mar 25, 2012
 */

package gui;

import gui.given.PlaneDrawnForGivenGui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

import tower.Tower;

// TODO: Auto-generated Javadoc
/**
 * The Class TurningLineComponent.
 */
public class TurningLineComponent extends JComponent implements Runnable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2762006079733822119L;

	/** The g2. */
	private Graphics2D g2;

	/** The value. */
	private double value = 0.0;

	/** The color. */
	private Color color;

	/** The list of planes. */
	private List<PlaneDrawnForGivenGui> listOfPlanes;

	/** The position of the tower in the background image. */
	private static final double TOWER_X = 625.0;
	private static final double TOWER_Y = 445.0;

	private Image radarImage = null;
	private Image radarLine = null;
	
	private BasicStroke basicStroke2 = null;
	private BasicStroke basicStroke3 = null;

	/**
	 * Instantiates a new turning line component.
	 */
	public TurningLineComponent() {

		color = new Color(18, 255, 0);
		basicStroke2 = new BasicStroke(2.0f);
		basicStroke3 = new BasicStroke(3.0f);
		listOfPlanes = new LinkedList<PlaneDrawnForGivenGui>();
		radarLine = Toolkit.getDefaultToolkit().getImage("radar_Line.png");
		radarImage = Toolkit.getDefaultToolkit().getImage("img/map.png");
	}

	/**
	 * Start.
	 */
	public void start() {

		Thread thread = new Thread(this);
		thread.start();
	}

	/**
	 * Stop.
	 */
	public void stop() {

	}

	/**
	 * Destroy.
	 */
	public void destroy() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		while (true) {

			if (value >= 0 && value < 361) {

				value += 0.2;
			} else {
				value = 0;
			}

			repaint();

			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				// do nothing
			}

			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);

		g2 = (Graphics2D) g;
		g2.drawImage(radarImage, 0, 0, this);
		BasicStroke stroke = new BasicStroke(2.0f);

		g2.setStroke(stroke);
		g2.setColor(color);
		g2.rotate(value, TOWER_X, TOWER_Y);
		g2.drawImage(radarLine, (int) TOWER_X, (int) TOWER_Y, this);

		g2.setTransform(AffineTransform.getRotateInstance(0));
		drawCirclesRipple();
		drawLine(g2);
	}
	
	/**
	 * Circles coming from the Plane Tower
	 */

	public void drawCirclesRipple() {

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.5f));
		g2.setStroke(basicStroke3);
		for (int i = 0; i < 10; i++) {
			g2.drawOval((int) TOWER_X - i * 75, (int) TOWER_Y - i * 75,
					i * 150, i * 150);
		}
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1.0f));
	}

	/**
	 * Draw the outline of the path the plane must go to
	 * 
	 * @param g2
	 *            the g2
	 */
	public void drawLine(Graphics2D g2) {

		g2.setStroke(basicStroke2);
		g2.setColor(Color.RED);
		if (Tower.getTowerInstance().listOfPointsFromRouting().size() > 2) {

			for (int i = 1; i < Tower.getTowerInstance()
					.listOfPointsFromRouting().size(); i++) {

				g2.drawLine(
						Tower.getTowerInstance().listOfPointsFromRouting()
								.get(i - 1).x,
						Tower.getTowerInstance().listOfPointsFromRouting()
								.get(i - 1).y,
						Tower.getTowerInstance().listOfPointsFromRouting()
								.get(i).x,
						Tower.getTowerInstance().listOfPointsFromRouting()
								.get(i).y);
			}
		}
		
		g2.setColor(color);
	}

	/**
	 * Adds the plane drawing to radar.
	 * 
	 * @param planeDrawn
	 *            the plane drawn
	 */
	public void addPlaneDrawingToRadar(PlaneDrawnForGivenGui planeDrawn) {

		listOfPlanes.add(planeDrawn);
	}
}