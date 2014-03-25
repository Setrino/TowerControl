/*
 * Sergei Kostevitch
 * Mar 27, 2012
 */

package gui;

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
import java.util.Random;

import javax.swing.JComponent;

import tower.Tower;

// TODO: Auto-generated Javadoc
/**
 * The Class PlaneDrawn.
 */
public class PlaneDrawn extends JComponent implements Runnable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 673337009669267672L;

	/** The g2. */
	private Graphics2D g2;

	/** The pos x. */
	private int posX;

	/** The pos y. */
	private int posY;

	/** The old point. */
	private Point oldPoint;

	/** The new point. */
	private Point newPoint;

	/** The rotation angle. */
	private double rotationAngle;

	/** The plane title. */
	private String planeTitle;

	/** The old and new plane position. */
	private Map<String, Point> oldAndNewPlanePosition;

	/** The tower. */
	private Tower tower;

	/** The random. */
	private Random random;

	/** The basic stroke. */
	private BasicStroke basicStroke;

	/** The color. */
	private Color color;

	/** The radar image. */
	private Image planeImage;

	/** The theta. */
	private double theta;

	/** The offset. */
	private Point offset;

	/** The offset1. */
	private Point offset1;

	/** The offset2. */
	private Point offset2;

	/** The offset3. */
	private Point offset3;

	/** The PLAN e_ toplef t_ x. */
	private final int PLANE_TOPLEFT_X = 11;

	/** The PLAN e_ toplef t_ y. */
	private final int PLANE_TOPLEFT_Y = 0;

	/** The PLAN e_ cente r_ x. */
	private final int PLANE_CENTER_X = 273;

	/** The PLAN e_ cente r_ y. */
	private final int PLANE_CENTER_Y = 262;

	/** The CIRLC e_ cente r_ x. */
	private final int CIRLCE_CENTER_X = 293;

	/** The CIRCL e_ cente r_ y. */
	private final int CIRCLE_CENTER_Y = 283;

	/** The CIRLC e_ radius. */
	private final int CIRLCE_RADIUS = 262;

	/** The DIAMETER. */
	private final int DIAMETER = 524;

	/** The SCALE d_ x. */
	private final double SCALED_X = (524.0 / 1109.0);

	/** The SCALE d_ y. */
	private final double SCALED_Y = (524.0 / 751.0);

	/** Once is a checker to see if plane has just appeared on radar. */
	private boolean once = true;

	private Point originalTowerRetrievedPoint = null;

	// The two points used for storage SCALED_X*newPoint
	private int scaledNewPointX = 0;
	private int scaledNewPointY = 0;

	// Offset x and y values after first run
	private int offsetX = 0;
	private int offsetY = 0;

	// Checker used for to see if the plane is outside the radar
	// (we see it can go to 1023 x 1023 and die)
	// If it is outside 1109 x 751 - make the image disappear by changing to
	// false
	private boolean planePresent = true;

	// Offset value to be used later on so that the plane moves along with the
	// offset value
	// we created to force the plane onto the radar
	private Point offsetScaleValueForLater;

	/**
	 * Instantiates a new plane drawn.
	 * 
	 * @param planeTitle
	 *            the plane title
	 */
	public PlaneDrawn(String planeTitle) {

		this.planeTitle = planeTitle;
		tower = Tower.getTowerInstance();
		random = new Random();
		basicStroke = new BasicStroke(2.0f);
		color = new Color(18, 255, 0);
		planeImage = Toolkit.getDefaultToolkit().getImage("plane_IconF.png");
		updatePosition();
		offset = planeToCircleOffset(oldAndNewPlanePosition.get("newPosition"));
		//System.out.println("Offset with minus 20 " + (offset.x - 20) + " "
		//		+ (offset.y - 20));
		//System.out.println("Offset " + offset.x + " " + offset.y);
		offsetScaleValueForLater = new Point(offset.x - newPoint.x, offset.y
				- newPoint.x);

	}

	/**
	 * Initiates if null, Updates the position of Plane based on the Tower List
	 * of Planes and Positions by Plane Name. Scaling takes place because of
	 * difference in image size and also offset of 31 on and 21 on y axis
	 */

	public void updatePosition() {

		// Need to add the offset
		originalTowerRetrievedPoint = tower.getPlanePosition().get(planeTitle);
		newPoint = tower.getPlanePosition().get(planeTitle);

		scaledNewPointX = (int) (SCALED_X * newPoint.getX() + 31);
		scaledNewPointY = (int) (SCALED_Y * newPoint.getY() + 21);

		//System.out.println(originalTowerRetrievedPoint.x + " "
		//		+ originalTowerRetrievedPoint.y);

		if (oldAndNewPlanePosition == null) {

			oldAndNewPlanePosition = new HashMap<String, Point>();
			oldAndNewPlanePosition.put("oldPosition", new Point(
					scaledNewPointX, scaledNewPointY));
			oldAndNewPlanePosition.put("newPosition", new Point(
					scaledNewPointX, scaledNewPointY));

		//	System.out.println("Old x "
		//			+ oldAndNewPlanePosition.get("oldPosition").x + " Old y "
		//			+ oldAndNewPlanePosition.get("oldPosition").y + " New x "
		//			+ oldAndNewPlanePosition.get("newPosition").x + " New y "
		//			+ oldAndNewPlanePosition.get("newPosition").y);

		} else {

			if (oldAndNewPlanePosition.get("newPosition").x != scaledNewPointX
					&& oldAndNewPlanePosition.get("newPosition").y != scaledNewPointY) {

				once = false;

				oldAndNewPlanePosition.put("oldPosition",
						oldAndNewPlanePosition.get("newPosition"));
				oldAndNewPlanePosition.put("newPosition", new Point(
						scaledNewPointX, scaledNewPointY));

			//	System.out.println("Old x "
			//			+ oldAndNewPlanePosition.get("oldPosition").x
			//			+ " Old y "
			//			+ oldAndNewPlanePosition.get("oldPosition").y
			//			+ " New x "
			//			+ oldAndNewPlanePosition.get("newPosition").x
			//			+ " New y "
			//			+ oldAndNewPlanePosition.get("newPosition").y);
			}

			offset = planeToCircleOffset(oldAndNewPlanePosition
					.get("newPosition"));

			//System.out.println("Offset is " + offset.x + " " + offset.y);

			// If the plane is outside the circle, kill it
			// --------------------------- IMPORTANT ---------------------//
			if ((offset.x < (oldAndNewPlanePosition.get("newPosition").x + offsetScaleValueForLater.x) || offset.y < (oldAndNewPlanePosition
					.get("newPosition").y + offsetScaleValueForLater.y))
					&& (offset.x > 293 || offset.y > 293)) {
				planePresent = false;
			}
			// --------------------------- IMPORTANT ---------------------//
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
	 * Temporarily testing using arbitrary values for posX and posY
	 * 
	 */

	public void run() {

		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		while (true) {

			updatePosition();
			repaint();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				// do nothing
			}

			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		g2 = (Graphics2D) g;
		drawPlane();

	}

	// Plane center at 273, 262 (left top at 11, 0)
	// Line center at 293, 283
	// 524/1109 - posX
	// 524/751 - posY
	// Outside square 524
	// Fram boundaries 31, 21, 555, 545

	/**
	 * Draw plane.
	 */
	public void drawPlane() {

		g2.setStroke(basicStroke);
		g2.setColor(color);
		g2.drawRect(31, 21, 524, 524);

		if (!once && planePresent) {

			offsetX = (oldAndNewPlanePosition.get("newPosition").x + offsetScaleValueForLater.x);
			offsetY = (oldAndNewPlanePosition.get("newPosition").y + offsetScaleValueForLater.y);

			g2.setColor(Color.RED);
			g2.drawLine(CIRLCE_CENTER_X, CIRCLE_CENTER_Y, offsetX, offsetY);
			g2.drawString(planeTitle + " " + (originalTowerRetrievedPoint.x)
					+ " " + (originalTowerRetrievedPoint.y), offsetX + 20,
					offsetY);
			g2.rotate(rotation(), offsetX, offsetY);
			g2.drawImage(planeImage, offsetX - 20, offsetY - 20, this);
		}

		if (once && planePresent) {

			g2.setColor(Color.RED);
			g2.drawLine(CIRLCE_CENTER_X, CIRCLE_CENTER_Y, offset.x, offset.y);
			g2.drawString(planeTitle + " " + (originalTowerRetrievedPoint.x)
					+ " " + (originalTowerRetrievedPoint.y), offset.x + 20,
					offset.y);
			g2.rotate(rotation(), offset.x, offset.y);
			g2.drawImage(planeImage, offset.x - 20, offset.y - 20, this);

		}

	}

	/**
	 * Rotation.
	 * 
	 * @return the double
	 */
	public double rotation() {

		/*
		 * double intermediateX = 41; double intermediateY = 31; double
		 * oldInterPosX = CIRLCE_CENTER_X; double oldInterPosY =
		 * CIRCLE_CENTER_Y; rotationAngle = Math.atan2(oldInterPosY -
		 * intermediateY, oldInterPosX - intermediateX);
		 */

		// GET RID OF THE COMMENTS AFTERWARDS TO MAKE IT WORK CORRECTLY (DONE)

		if (once) {

			rotationAngle = Math.atan2(CIRCLE_CENTER_Y
					- (originalTowerRetrievedPoint.getY() + 21),
			CIRLCE_CENTER_X - (originalTowerRetrievedPoint.getX() + 31));
			
		} else {

			rotationAngle = Math.atan2(
					oldAndNewPlanePosition.get("newPosition").getY()
							- oldAndNewPlanePosition.get("oldPosition").getY(),
					oldAndNewPlanePosition.get("newPosition").getX()
							- oldAndNewPlanePosition.get("oldPosition").getX());
		}

		return rotationAngle;
	}

	// Calculate theta with respect to center of circle - if the plane is
	// outside the 262 radius
	// force it inside
	/**
	 * Plane to circle offset.
	 * 
	 * @param planePosition
	 *            the plane position
	 * @return the point
	 */
	public Point planeToCircleOffset(Point planePosition) {

		Point point = new Point();

		//System.out.println("Was " + planePosition.x + " " + planePosition.y);

		theta = Math.atan((planePosition.getY() - CIRCLE_CENTER_Y)
				/ (planePosition.getX() - CIRLCE_CENTER_X));
		if (planePosition.x >= 31 && planePosition.x <= 293) {

			point.setLocation(
					(CIRLCE_CENTER_X + CIRLCE_RADIUS
							* Math.cos(Math.PI - theta)),
					(CIRCLE_CENTER_Y + CIRLCE_RADIUS * Math.sin(-theta)));
		} else if (planePosition.x > 293 && planePosition.x <= 555) {

			point.setLocation(
					(CIRLCE_CENTER_X + CIRLCE_RADIUS * Math.cos(theta)),
					(CIRCLE_CENTER_Y + CIRLCE_RADIUS * Math.sin(theta)));
		}

		//System.out.println(point.x + " " + point.y);

		return point;
	}
}
