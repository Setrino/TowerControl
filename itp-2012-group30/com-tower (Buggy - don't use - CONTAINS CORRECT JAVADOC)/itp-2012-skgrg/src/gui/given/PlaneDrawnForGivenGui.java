/*
 * Sergei Kostevitch
 * May 10, 2012
 */

package gui.given;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import plane.Plane;

import tower.Tower;

/**
 * The Class PlaneDrawnForGui is the Thread type of Plane added to the GUI
 * Here we make the drawing decisions for each plane
 */
public class PlaneDrawnForGivenGui extends JComponent implements Runnable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 673337009669267672L;

	/** The g2. */
	private Graphics2D g2;

	/** The rotation angle. */
	private double rotationAngle;

	/** The plane title. */
	private String planeTitle;

	/** The old and new plane position. */
	private Map<String, Point> oldAndNewPlanePosition;

	/** The tower. */
	private Tower tower;

	/** The basic stroke. */
	private BasicStroke basicStroke;

	/** The color. */
	private Color color;

	/** The radar image. */
	private Image planeImage;

	private Image explosionImage;

	private Point originalTowerRetrievedPoint = null;

	// Checker used for to see if the plane is outside the radar
	// (we see it can go to 1023 x 1023 and die)
	// If it is outside 1109 x 751 - make the image disappear by changing to
	// false
	private boolean planePresent = true;

	private HashMap<String, CircularBuffer<Point>> circularBuffer = null;

	// How long should aircraft trails be (in milliseconds)?
	private static final int AIRCRAFT_TRAIL_DURATION = 4000;

	private Plane plane = null;
	
	private Image drawnImage = null;

	/**
	 * Instantiates a new planeDrawn - update The Position and so on
	 * 
	 * @param planeTitle
	 *            the plane title
	 */
	public PlaneDrawnForGivenGui(String planeTitle, Plane plane) {

		this.planeTitle = planeTitle;
		tower = Tower.getTowerInstance();
		this.plane = plane;
		plane.setPlaneDrawn(this);
		basicStroke = new BasicStroke(3.0f);
		color = new Color(18, 255, 0);
		planeImage = Toolkit.getDefaultToolkit().getImage("img/plane.png");
		explosionImage = Toolkit.getDefaultToolkit().getImage("img/kboom.png");
		circularBuffer = new HashMap<String, CircularBuffer<Point>>();
		updatePosition();
	}

	/**
	 * Initiates if null, Updates the position of Plane based on the Tower List
	 * of Planes and Positions by Plane Name. Scaling takes place because of
	 * difference in image size and also offset of 31 on and 21 on y axis
	 */

	public void updatePosition() {

		originalTowerRetrievedPoint = tower.getPlanePosition().get(planeTitle);

		if (oldAndNewPlanePosition == null) {

			oldAndNewPlanePosition = new HashMap<String, Point>();
			oldAndNewPlanePosition.put("oldPosition", new Point(
					originalTowerRetrievedPoint.x,
					originalTowerRetrievedPoint.y));
			oldAndNewPlanePosition.put("newPosition", new Point(
					originalTowerRetrievedPoint.x,
					originalTowerRetrievedPoint.y));

		} else {

			if (oldAndNewPlanePosition.get("newPosition").x != originalTowerRetrievedPoint.x
					|| oldAndNewPlanePosition.get("newPosition").y != originalTowerRetrievedPoint.y) {

				oldAndNewPlanePosition.put("oldPosition",
						oldAndNewPlanePosition.get("newPosition"));
				oldAndNewPlanePosition.put("newPosition", new Point(
						originalTowerRetrievedPoint.x,
						originalTowerRetrievedPoint.y));

			}
		}
	}

	/**
	 * Start thread.
	 */
	public void start() {

		Thread thread = new Thread(this);
		thread.start();
	}

	/**
	 * Update the position 1/10 of second
	 * 
	 */

	public void run() {

		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		while (true) {

			updatePosition();
			repaint();

			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				// do nothing
			}

			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		g2 = (Graphics2D) g;
		drawTrail();
		drawPlane();

	}

	// Plane center at 273, 262 (left top at 11, 0)
	// Line center at 293, 283
	// 524/1109 - posX
	// 524/751 - posY
	// Outside square 524
	// Fram boundaries 31, 21, 555, 545

	/**
	 * Actual drawing of plane - planeImage, direction, theta, and so on (whether exploded or not)
	 */
	public void drawPlane() {

		g2.setStroke(basicStroke);
		g2.setColor(color);

		if (planePresent) {

			g2.drawString(planeTitle + " " + (originalTowerRetrievedPoint.x)
					+ " " + (originalTowerRetrievedPoint.y),
					originalTowerRetrievedPoint.x + 16,
					originalTowerRetrievedPoint.y);
			g2.setTransform(AffineTransform.getRotateInstance(rotation(),
					originalTowerRetrievedPoint.x,
					originalTowerRetrievedPoint.y));
			
			drawnImage = plane.hasCrashed() ? explosionImage : planeImage;

				g2.drawImage(drawnImage,
						originalTowerRetrievedPoint.x - 16,
						originalTowerRetrievedPoint.y - 16, this);
		}
	}

	/**
	 * Rotation.
	 * 
	 * @return the double
	 */
	public double rotation() {

		rotationAngle = Math.atan2(oldAndNewPlanePosition.get("newPosition")
				.getY() - oldAndNewPlanePosition.get("oldPosition").getY(),
				oldAndNewPlanePosition.get("newPosition").getX()
						- oldAndNewPlanePosition.get("oldPosition").getX());

		return rotationAngle;
	}
	
	/**
	 * Given by you used for drawing planeTrail
	 */

	public void drawTrail() {

		g2.setTransform(AffineTransform.getRotateInstance(0));
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.5f));
		g2.setStroke(basicStroke);
		CircularBuffer<Point> cb = circularBuffer.get(planeTitle);
		if (cb != null) {
			cb.add(originalTowerRetrievedPoint);
		} else {
			cb = new CircularBuffer<Point>(AIRCRAFT_TRAIL_DURATION * 24 / 1000);
			cb.add(originalTowerRetrievedPoint);
			cb.add(originalTowerRetrievedPoint);
			// Add it twice, so we have at least one line segment
			circularBuffer.put(planeTitle, cb);
		}

		// Draw trails so we can see the route that the planes have taken
		g2.setColor(Color.CYAN);

		CircularBuffer<Point> previousPos = circularBuffer.get(planeTitle);
		for (int j = 1; j < previousPos.size(); j++) {
			Point p1 = previousPos.get(j);
			Point p2 = previousPos.get(j - 1);
			if (p2.getX() >= 0 && p2.getY() >= 0)
				g2.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
		g2.setColor(color);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				1.0f));
	}
	
	/**
	 * .wait() on the thread if plane Crashed
	 */
	
	public synchronized void stop() {

		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
