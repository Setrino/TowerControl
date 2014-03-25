/*
 * Sergei Kostevitch
 * May 3, 2012
 */

package plane;

import java.awt.Point;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import file.CoordinatesFile;

import messages.MessageHandler;
import messages.MoveType;
import messages.RoutingMessage;
import messages.RoutingMessageType;

import routing.Circular;
import routing.Coordinate;
import routing.Straight;

public class PlaneRouting extends TimerTask {

	// Time at which the plane was create + a minor delay before the first
	// RoutingMessage sent
	@SuppressWarnings("unused")
	private long planeCreatedTime;
	private long periodBetweenRoutingMessages;
	// Know when the last message was sent
	private long planeLastRoutingMessageSent;
	// Know when the before last message was sent
	@SuppressWarnings("unused")
	private long planeTimeBeforeLastRoutingMessageSent;

	private MessageHandler messageHandler = null;
	private CoordinatesFile coordinatesFile = null;
	private RoutingMessage routingMessage = null;
	private RoutingMessageType routingMessageType = null;
	private Plane plane = null;
	private double planeSpeed = 0.0;
	private CircleType currentCircleType = null;
	private CircleType previousCircleType = null;
	private String planeIDName = null;
	private byte[] planeID = null;

	// Landing point x and y
	private final static int POS_X = 533;
	private final static int POS_Y = 437;

	// The next point where the plane should fly
	private Point instructedPoint;
	private double instructedAngle;

	private boolean landSentBoolean = false;

	// The three circles coordinates
	// Small Circle = 3 max
	// Medium Circle = 10 max
	// Large Circle = 100 max

	private List<Coordinate> smallCircleList = null;
	// The current instruction number
	private int smallCircleDoubles = 0;
	private boolean smallBoolean = true;

	private List<Coordinate> mediumCircleList = null;
	// The current instruction number
	private int mediumCircleDoubles = -1;
	private boolean middleBoolean = true;

	private List<Coordinate> largeCircleList = null;
	// The current instruction number
	private int largeCircleDoubles = 0;
	private boolean largeBoolean = true;

	// ------- Circular Logic ------- //
	private boolean oneTimeRadius = true;
	private double radius = 0.0;
	private double timeToDestination = 0.0;
	// Sent a circular message once
	private boolean sendCircular = true;
	// ------- Circular Logic ------- //

	// The actual point where plane is
	private Point currentPointFromPlaneAndTower;

	private Map<String, Point> planePosition;

	private boolean firstRoutingMessage = true;

	private boolean landing = false;

	private boolean smallCircleToLand = true;
	private boolean mediumCircleToSmall = true;
	private boolean largeCircleToMedium = true;
	
	/**
	 * Plane Routing is used for Actual Routing Message Type Communication with each plane
	 * It gets data from Tower (Current Plane Position) and CircleTypes (where plane should go)
	 * And accordingly sends routing and updates the plane routing
	 * Receives Actual Circle Details from three files (Small/Medium/Large Circle.txt) and routes
	 * planes accordingly
	 * @param planeCreatedTime
	 * @param periodBetweenRoutingMessages
	 * @param plane
	 * @param planeID
	 */

	public PlaneRouting(long planeCreatedTime,
			long periodBetweenRoutingMessages, Plane plane, byte[] planeID) {

		System.out.println("Created Plane Routing Decision Maker");

		plane.setRoutingDecisionMaker(this);
		this.planeCreatedTime = planeCreatedTime;
		this.periodBetweenRoutingMessages = periodBetweenRoutingMessages;
		this.planeLastRoutingMessageSent = planeCreatedTime;
		planePosition = new HashMap<String, Point>();
		messageHandler = MessageHandler.getMessageHandlerInstance();
		coordinatesFile = CoordinatesFile.getCoordinatesFileInstance();
		this.plane = plane;
		this.planeIDName = new String(planeID);
		this.planeID = planeID;

		smallCircleList = coordinatesFile.readFromFile("SmallCircle.txt");
		mediumCircleList = coordinatesFile.readFromFile("MediumCircle.txt");
		largeCircleList = coordinatesFile.readFromFile("LargeCircle.txt");
	}

	/**
	 * TimerTask waiting for PlaneSpeed (that will be known as soon as Plane sends as the details
	 * about what kind of plane it is
	 * CurrentCircleType used is received from Tower 
	 */
	
	@Override
	public void run() {

		if (planeSpeed != 0.0 && currentCircleType != null) {

			setNewPlanePosition(currentPointFromPlaneAndTower.getX(),
					currentPointFromPlaneAndTower.getY());
			planeTimeBeforeLastRoutingMessageSent = planeLastRoutingMessageSent;
			planeLastRoutingMessageSent += periodBetweenRoutingMessages;
			routingMessageConstruction();
		}
	}
	
	/** 
	 * This method makes the decision where the plane should go next - we get the 
	 * currentCircleType and PreviousCircleType - we compare and see where to go next
	 * LANDING, SMALL, MEDIUM, LARGE
	 * 
	 * In case of MayDay - straight to landing
     */
	public void routingMessageConstruction() {

		switch (currentCircleType) {

		case LANDING:

			if (previousCircleType == CircleType.SMALL) {
				smallCircleToLanding();
			} else if (previousCircleType == CircleType.MEDIUM) {
				mediumCircleToSmallCircle();
			} else if (previousCircleType == CircleType.LARGE) {
				largeCircleToMediumCircle();
			} else {
				landingLogic();
			}

			break;
		case SMALL:

			if (previousCircleType == CircleType.SMALL) {
				smallCircleLogic();
			} else if(previousCircleType == CircleType.LARGE){
				largeCircleToMediumCircle();
			}else {
				mediumCircleToSmallCircle();
			}

			break;
		case MEDIUM:

			if (previousCircleType == CircleType.MEDIUM) {
				mediumCircleLogic();
			} else {
				largeCircleToMediumCircle();
			}

			break;

		case LARGE:

			largeCircleLogic();
			break;
		}

	}

	/**
	 * Landing place is Straight 400, 166; Landing 533, 437;
	 * 
	 */
	public void landingLogic() {

		if (!landSentBoolean) {
			if (landing) {
				land();
			} else {
				instructedPoint = new Point(400, 166);
				moveInStraightLine(periodBetweenRoutingMessages);
			}
		}
	}

	/**
	 * Small Circle Logic
	 * Tells a plane to go to specific point depending on Straight or Circular type
	 * of movement
	 * When point reached - next one chosen and plane is sent there
	 */
	public void smallCircleLogic() {

		if (smallBoolean) {

			if (smallCircleDoubles == smallCircleList.size() - 1) {
				smallCircleDoubles = -1;
			}

			smallCircleDoubles++;

			smallBoolean = false;

			if (instructedPoint == null) {
				instructedPoint = new Point((int) smallCircleList.get(
						smallCircleDoubles).getX(), (int) smallCircleList.get(
						smallCircleDoubles).getY());
			} else {
				instructedPoint.setLocation(
						smallCircleList.get(smallCircleDoubles).getX(),
						smallCircleList.get(smallCircleDoubles).getY());
			}
		}

		if (smallCircleList.get(smallCircleDoubles) instanceof Straight) {

			moveInStraightLine(periodBetweenRoutingMessages);

		} else if (mediumCircleList.get(smallCircleDoubles) instanceof Circular) {

			instructedAngle = ((Circular) smallCircleList
					.get(smallCircleDoubles)).getAngleOfRotation();
			moveInCircle(periodBetweenRoutingMessages);

		}
	}

	/**
	 * Medium Circle Logic
	 * Tells a plane to go to specific point depending on Straight or Circular type
	 * of movement
	 * When point reached - next one chosen and plane is sent there
	 */
	public void mediumCircleLogic() {

		if (middleBoolean) {

			if (mediumCircleDoubles == mediumCircleList.size() - 1) {
				mediumCircleDoubles = -1;
			}

			mediumCircleDoubles++;
			middleBoolean = false;
			
			if (instructedPoint == null) {
				instructedPoint = new Point((int) mediumCircleList.get(
						mediumCircleDoubles).getX(), (int) mediumCircleList
						.get(mediumCircleDoubles).getY());
			} else {
				instructedPoint.setLocation(
						mediumCircleList.get(mediumCircleDoubles).getX(),
						mediumCircleList.get(mediumCircleDoubles).getY());
			}
		}

		if (mediumCircleList.get(mediumCircleDoubles) instanceof Straight) {

			moveInStraightLine(periodBetweenRoutingMessages);

		} else if (mediumCircleList.get(mediumCircleDoubles) instanceof Circular) {

			instructedAngle = ((Circular) mediumCircleList
					.get(mediumCircleDoubles)).getAngleOfRotation();
			moveInCircle(periodBetweenRoutingMessages);

		}
	}

	/**
	 * Large Circle Logic
	 * Tells a plane to go to specific point depending on Straight or Circular type
	 * of movement
	 * When point reached - next one chosen and plane is sent there
	 */
	public void largeCircleLogic() {

		if (largeBoolean) {

			if (largeCircleDoubles == largeCircleList.size() - 1) {
				largeCircleDoubles = -1;
			}

			largeCircleDoubles++;

			largeBoolean = false;

			if (instructedPoint == null) {
				instructedPoint = new Point((int) largeCircleList.get(
						largeCircleDoubles).getX(), (int) largeCircleList.get(
						largeCircleDoubles).getY());
			} else {
				instructedPoint.setLocation(
						largeCircleList.get(largeCircleDoubles).getX(),
						largeCircleList.get(largeCircleDoubles).getY());
			}
		}

		if (largeCircleList.get(largeCircleDoubles) instanceof Straight) {

			moveInStraightLine(periodBetweenRoutingMessages);

		} else if (largeCircleList.get(largeCircleDoubles) instanceof Circular) {

			instructedAngle = ((Circular) largeCircleList
					.get(largeCircleDoubles)).getAngleOfRotation();
			moveInCircle(periodBetweenRoutingMessages);
		}
	}

	/**
	 * Intermediate movement decision - must go first to specific point in the small circle 
	 * and then go land
	 */
	public void smallCircleToLanding() {

		if (smallCircleToLand) {
			if (smallCircleDoubles == 1) {
				previousCircleType = CircleType.LANDING;
				smallCircleToLand = false;
				landing = false;
				landingLogic();
			} else {
				smallCircleLogic();
			}
		}
	}

	/**
	 * Intermediate movement decision - must go first to specific point in the medium circle 
	 * and then go small circle
	 */
	public void mediumCircleToSmallCircle() {

		if (mediumCircleToSmall) {
			if (mediumCircleDoubles == 5) {
				previousCircleType = CircleType.SMALL;
				mediumCircleToSmall = false;
				smallCircleDoubles = 4;
				smallCircleLogic();
			} else {
				mediumCircleLogic();
			}
		}
	}

	/**
	 * Intermediate movement decision - must go first to specific point in the large circle 
	 * and then go medium circle
	 */
	public void largeCircleToMediumCircle() {

		if (largeCircleToMedium) {
			if (largeCircleDoubles == 3) {
				previousCircleType = CircleType.MEDIUM;
				largeCircleToMedium = false;
				mediumCircleDoubles = 2;
				mediumCircleLogic();
			} else {
				largeCircleLogic();
			}
		}
	}

	/**
	 * Updates the details where for the plane to go next in a straight line - where the plane is,
	 * how long left to the end, whats the next point
	 * If reached the point (well just a bit earlier than that)
	 * go to the next type of Coordinate to Move to
	 * @param delta - update time interval
	 */
	
	private void moveInStraightLine(double delta) {

		double deltaTimeSeconds = delta / 1000.0;
		double rateOfChangeOfX = 0.0;
		double rateOfChangeOfY = 0.0;
		double requiredTimeToReachDestination = 0.0;
		double actualTimeUntilArrivalAtDestination = 0.0;
		double newX = 0.0;
		double newY = 0.0;

		double distance = Math.sqrt(Math.pow(instructedPoint.getX()
				- currentPointFromPlaneAndTower.getX(), 2)
				+ Math.pow(instructedPoint.getY()
						- currentPointFromPlaneAndTower.getY(), 2));

		if (distance > 0) {

			rateOfChangeOfX = (instructedPoint.getX() - currentPointFromPlaneAndTower
					.getX()) / distance;
			rateOfChangeOfY = (instructedPoint.getY() - currentPointFromPlaneAndTower
					.getY()) / distance;

			requiredTimeToReachDestination = distance / planeSpeed;
			actualTimeUntilArrivalAtDestination = (requiredTimeToReachDestination > deltaTimeSeconds) ? requiredTimeToReachDestination
					: deltaTimeSeconds;

			/*
			 * System.out
			 * .println("----------- Routing Straight Started ----------");
			 * System.out.println("Plane speed " + planeSpeed);
			 * System.out.println("Distance to destination " + distance);
			 * System.out.println("Delta " + deltaTimeSeconds);
			 * System.out.println("Time needed " +
			 * requiredTimeToReachDestination);
			 * System.out.println("Actual time until destination " +
			 * actualTimeUntilArrivalAtDestination);
			 * System.out.println("Current Point x " +
			 * currentPointFromPlaneAndTower.getX() + " y " +
			 * currentPointFromPlaneAndTower.getY());
			 * System.out.println("Instructed Point x " + instructedPoint.x +
			 * " y " + instructedPoint.y);
			 */

			if (actualTimeUntilArrivalAtDestination == deltaTimeSeconds
					|| distance < planeSpeed + 5.0) {

				if (previousCircleType.ordinal() == 0) {
					landing = true;
					landingLogic();
				}

				if (previousCircleType.ordinal() == 1) {
					smallBoolean = true;
					smallCircleLogic();

				}

				if (previousCircleType.ordinal() == 2) {
					middleBoolean = true;
					mediumCircleLogic();

				}

				if (previousCircleType.ordinal() == 3) {
					largeBoolean = true;
					largeCircleLogic();
				}

				/*
				 * System.out
				 * .println("----------- Arrived at Waypoint ----------");
				 * System.out.println("Plane " + planeIDName +
				 * " arrived at waypoint " + instructedPoint.x + " , " +
				 * instructedPoint.y); System.out
				 * .println("----------- Arrived at Waypoint ----------");
				 */

			} else {

				newX = currentPointFromPlaneAndTower.getX() + rateOfChangeOfX
						* actualTimeUntilArrivalAtDestination * planeSpeed;
				newY = currentPointFromPlaneAndTower.getY() + rateOfChangeOfY
						* actualTimeUntilArrivalAtDestination * planeSpeed;

				/*
				 * System.out.println("New Point X " + newX + " New Point Y " +
				 * newY);
				 */

				if (firstRoutingMessage) {
					routingMessageType = RoutingMessageType.REPLACEALL;
					firstRoutingMessage = false;
				} else {
					routingMessageType = RoutingMessageType.LAST;
				}

				// --------------------- TEST PHASE ------------------- //
				// tower.addPointToListFromRouting(new Point((int) newX,
				// (int) newY));
				// --------------------- TEST PHASE ------------------- //

				routingMessage = new RoutingMessage(planeID, (int) newX,
						(int) newY, routingMessageType, MoveType.STRAIGHT, null);

				messageHandler.visit(routingMessage, "Tower", planeIDName,
						plane);
			}
		}

		// System.out.println("----------- Routing Straight Ended ----------");
	}
	
	/**
	 * Updates the details where for the plane to go next in a circular type - where the plane is,
	 * how long left to the end, whats the next point
	 * If reached the point (well just a bit earlier than that)
	 * go to the next type of Coordinate to Move to
	 * @param delta - update time interval
	 */

	// SuppressWarning is done due to just for some System.out.println() used - I left there
	// here just in case you want to check details ;)
	@SuppressWarnings("unused")
	public void moveInCircle(double delta) {

		double deltaTimeSeconds = delta / 1000.0;
		double deltaTheta = 0.0;
		double rateOfChangeOfX = 0.0;
		double rateOfChangeOfY = 0.0;
		double requiredTimeToReachDestination = 0.0;
		double actualTimeUntilArrivalAtDestination = 0.0;
		double instructionAngleRadian = 0.0;
		double instructionAngleSign = 0.0;
		double intermediateX = 0.0;
		double intermediateY = 0.0;
		double rotationSpeed = 0.0;
		double distanceToDestination = 0.0;
		double neededTime = 0.0;
		double theta = 0.0;
		double newX = 0.0;
		double newY = 0.0;

		instructionAngleRadian = Math.toRadians(instructedAngle);

		if (oneTimeRadius) {

			radius = Math.sqrt(Math.pow(currentPointFromPlaneAndTower.getX()
					- instructedPoint.getX(), 2)
					+ Math.pow(currentPointFromPlaneAndTower.getY()
							- instructedPoint.getY(), 2));

			rotationSpeed = planeSpeed / radius;
			neededTime = Math.abs(instructionAngleRadian) / rotationSpeed;
			timeToDestination = neededTime;

			oneTimeRadius = false;
		}

		double angularVelocity = Math.abs(instructionAngleRadian) / radius;
		
		double timeToTurn = (2 * Math.PI * radius)
				/ (planeSpeed * (360.0 / instructedAngle));

		// System.out.println("Angular Velocity " + angularVelocity);
		// System.out.println("Time to turn " + timeToTurn);

		instructionAngleSign = Math.signum(instructionAngleRadian);

		intermediateX = (currentPointFromPlaneAndTower.getX() - instructedPoint
				.getX());
		intermediateY = (currentPointFromPlaneAndTower.getY() - instructedPoint
				.getY());

		theta = computeTheta(intermediateX, intermediateY, 0, 0);
		rotationSpeed = planeSpeed / radius;
		deltaTheta = rotationSpeed * deltaTimeSeconds;
		neededTime = Math.abs(instructionAngleRadian) / rotationSpeed;
		distanceToDestination = timeToDestination * planeSpeed;
		timeToDestination = timeToDestination - deltaTimeSeconds;

		/*
		 * System.out.println("----------- Routing Circle Started ----------");
		 * System.out.println("Radius " + radius);
		 * System.out.println("Rotation speed " + rotationSpeed);
		 * System.out.println("Instructed Point x " + instructedPoint.x + " y "
		 * + instructedPoint.y); System.out.println("InstructionAngleRadians " +
		 * instructionAngleRadian); System.out.println("InstructionAngleSign " +
		 * instructionAngleSign); System.out.println("Delta theta " +
		 * deltaTheta); System.out.println("Needed time " + neededTime);
		 * System.out.println("Time to destination " + timeToDestination);
		 * System.out.println("Distance to destination " +
		 * distanceToDestination);
		 */

		if (deltaTheta < Math.abs(instructionAngleRadian)) {

			theta = theta + instructionAngleSign * deltaTheta;

			newX = (int) (radius * Math.cos(theta) + instructedPoint.getX());
			newY = (int) (radius * Math.sin(theta) + instructedPoint.getY());

		}

		/*
		 * System.out.println("New Point X " + newX + " Y " + newY);
		 * System.out.println("Theta " + theta);
		 */

		// --------------------- TEST PHASE ------------------- //
		// tower.addPointToListFromRouting(new Point((int) newX, (int) newY));
		// --------------------- TEST PHASE ------------------- //

		if (firstRoutingMessage) {
			routingMessageType = RoutingMessageType.REPLACEALL;
			firstRoutingMessage = false;
		} else {
			routingMessageType = RoutingMessageType.LAST;
		}

		if (sendCircular) {

			ByteBuffer bb = ByteBuffer.allocate(4);
			bb.putInt((int) instructedAngle);
			byte[] payload = bb.array();

			routingMessage = new RoutingMessage(planeID, instructedPoint.x,
					instructedPoint.y, routingMessageType, MoveType.CIRCULAR,
					payload);

			messageHandler.visit(routingMessage, "Tower", planeIDName, plane);

			sendCircular = false;
		}

		if (distanceToDestination < (planeSpeed + 20.0)) {

			oneTimeRadius = true;
			sendCircular = true;

			/*
			 * System.out
			 * .println("----------- Arrived at Circle Waypoint ----------");
			 * System.out.println("Plane " + planeIDName +
			 * " arrived at waypoint " + instructedPoint.x + " , " +
			 * instructedPoint.y); System.out
			 * .println("----------- Arrived at Circle Waypoint ----------");
			 */

			if (previousCircleType.ordinal() == 0) {
				landing = true;
				landingLogic();
			}

			if (previousCircleType.ordinal() == 1) {
				smallBoolean = true;
				smallCircleLogic();
			}

			if (previousCircleType.ordinal() == 2) {
				middleBoolean = true;
				mediumCircleLogic();

			}

			if (previousCircleType.ordinal() == 3) {
				largeBoolean = true;
				largeCircleLogic();
			}

		}

		// System.out.println("----------- Routing Circle Ended ----------");
	}
	
	/**
	 * Tells the Plane to go land at 533, 437
	 */

	private void land() {

		routingMessage = new RoutingMessage(planeID, POS_X, POS_Y,
				RoutingMessageType.LAST, MoveType.LANDING, null);

		messageHandler.visit(routingMessage, "Tower", planeIDName, plane);

		landSentBoolean = true;
	}

	private void setNewPlanePosition(double posX, double posY) {

		if (planePosition.containsKey("PlaneNewPosition")) {
			planePosition.put("PlanePreviousPosition",
					planePosition.get("PlaneNewPosition"));
		}
		planePosition
				.put("PlaneNewPosition", new Point((int) posX, (int) posY));
	}

	private double computeTheta(double px, double py, double cx, double cy) {

		return Math.atan2(py - cy, px - cx);
	}

	/**
	 * 
	 * @param currentPointFromPlane
	 *            the actual point updated directly from plane to tower
	 */
	public void setCurrentPoint(Point currentPointFromPlane) {

		this.currentPointFromPlaneAndTower = currentPointFromPlane;

		// System.out.println("Current Point From Plane "
		// + currentPointFromPlane.x + " " + currentPointFromPlane.y);
	}

	public void setSpeedOfPlane(double planeSpeed) {

		this.planeSpeed = planeSpeed;
	}
	
	/**
	 * Received from Tower -> Plane -> PlaneRouting to tell where to go next
	 * @param currentCircleType
	 */

	public void setCurrentCircleType(CircleType currentCircleType) {
		this.currentCircleType = currentCircleType;

		if (previousCircleType == null) {

			previousCircleType = currentCircleType;
		}
	}
	
	/**
	 * Normally updated in this class to decide where we came from for intermediate cases
	 * from large to medium, medium to small, small to landing types of movement
	 * @param currentCircleType
	 */

	public void setPreviousCircleType(CircleType previousCircleType) {
		this.previousCircleType = previousCircleType;
	}

	public synchronized void stop() {

		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
