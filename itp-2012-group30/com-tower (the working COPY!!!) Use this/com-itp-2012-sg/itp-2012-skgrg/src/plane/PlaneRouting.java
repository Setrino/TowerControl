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
				generalCircleLogic(smallBoolean, smallCircleDoubles, smallCircleList);
			} else if(previousCircleType == CircleType.LARGE){
				largeCircleToMediumCircle();
			}else {
				mediumCircleToSmallCircle();
			}

			break;
		case MEDIUM:

			if (previousCircleType == CircleType.MEDIUM) {
				generalCircleLogic(middleBoolean, mediumCircleDoubles, mediumCircleList);
			} else {
				largeCircleToMediumCircle();
			}

			break;

		case LARGE:

			generalCircleLogic(largeBoolean, largeCircleDoubles, largeCircleList);
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
	 * The two points from where to create cubic splines are x: 155.50 y: 515:35
	 * to x: 173.50 y: 515.35 (BOTTOM HORIZONTAL) and x: 270.77 y: 604.50 to x:
	 * 270.77 y: 628.50 (RIGHT VERTICAL)
	 */
	public void generalCircleLogic(boolean circleBoolean, int circleDoubles, List<Coordinate> circleList) {

		if (circleBoolean) {

			if (circleDoubles == circleList.size() - 1) {
				circleDoubles = -1;
			}

			circleDoubles++;

			circleBoolean = false;

			if (instructedPoint == null) {
				instructedPoint = new Point((int) circleList.get(
						circleDoubles).getX(), (int) circleList.get(
								circleDoubles).getY());
			} else {
				instructedPoint.setLocation(
						circleList.get(circleDoubles).getX(),
						circleList.get(circleDoubles).getY());
			}
		}

		if (circleList.get(circleDoubles) instanceof Straight) {

			moveInStraightLine(periodBetweenRoutingMessages);

		} else if (circleList.get(circleDoubles) instanceof Circular) {

			instructedAngle = ((Circular) circleList
					.get(circleDoubles)).getAngleOfRotation();
			moveInCircle(periodBetweenRoutingMessages);
		}
	}

	public void smallCircleToLanding() {

		if (smallCircleToLand) {
			if (smallCircleDoubles == 1) {
				previousCircleType = CircleType.LANDING;
				smallCircleToLand = false;
				landing = false;
				landingLogic();
			} else {
				generalCircleLogic(smallBoolean, smallCircleDoubles, smallCircleList);
			}
		}
	}

	public void mediumCircleToSmallCircle() {

		if (mediumCircleToSmall) {
			if (mediumCircleDoubles == 5) {
				previousCircleType = CircleType.SMALL;
				mediumCircleToSmall = false;
				smallCircleDoubles = 4;
				generalCircleLogic(smallBoolean, smallCircleDoubles, smallCircleList);
			} else {
				generalCircleLogic(middleBoolean, mediumCircleDoubles, mediumCircleList);
			}
		}
	}

	public void largeCircleToMediumCircle() {

		if (largeCircleToMedium) {
			if (largeCircleDoubles == 3) {
				previousCircleType = CircleType.MEDIUM;
				largeCircleToMedium = false;
				mediumCircleDoubles = 2;
				generalCircleLogic(middleBoolean, mediumCircleDoubles, mediumCircleList);
			} else {
				generalCircleLogic(largeBoolean, largeCircleDoubles, largeCircleList);
			}
		}
	}

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

			if (actualTimeUntilArrivalAtDestination == deltaTimeSeconds
					|| distance < planeSpeed + 5.0) {

				if (previousCircleType.ordinal() == 0) {
					landing = true;
					landingLogic();
				}

				if (previousCircleType.ordinal() == 1) {
					smallBoolean = true;
					generalCircleLogic(smallBoolean, smallCircleDoubles, smallCircleList);

				}

				if (previousCircleType.ordinal() == 2) {
					middleBoolean = true;
					generalCircleLogic(middleBoolean, mediumCircleDoubles, mediumCircleList);

				}

				if (previousCircleType.ordinal() == 3) {
					largeBoolean = true;
					generalCircleLogic(largeBoolean, largeCircleDoubles, largeCircleList);
				}

			} else {

				newX = currentPointFromPlaneAndTower.getX() + rateOfChangeOfX
						* actualTimeUntilArrivalAtDestination * planeSpeed;
				newY = currentPointFromPlaneAndTower.getY() + rateOfChangeOfY
						* actualTimeUntilArrivalAtDestination * planeSpeed;

				if (firstRoutingMessage) {
					routingMessageType = RoutingMessageType.REPLACEALL;
					firstRoutingMessage = false;
				} else {
					routingMessageType = RoutingMessageType.LAST;
				}

				routingMessage = new RoutingMessage(planeID, (int) newX,
						(int) newY, routingMessageType, MoveType.STRAIGHT, null);

				messageHandler.visit(routingMessage, "Tower", planeIDName,
						plane);
			}
		}
	}

	public void moveInCircle(double delta) {

		double deltaTimeSeconds = delta / 1000.0;
		double deltaTheta = 0.0;
		double instructionAngleRadian = 0.0;
		double instructionAngleSign = 0.0;
		double intermediateX = 0.0;
		double intermediateY = 0.0;
		double rotationSpeed = 0.0;
		double distanceToDestination = 0.0;
		double neededTime = 0.0;
		double theta = 0.0;

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

		if (deltaTheta < Math.abs(instructionAngleRadian)) {

			theta = theta + instructionAngleSign * deltaTheta;

		}

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

			if (previousCircleType.ordinal() == 0) {
				landing = true;
				landingLogic();
			}

			if (previousCircleType.ordinal() == 1) {
				smallBoolean = true;
				generalCircleLogic(smallBoolean, smallCircleDoubles, smallCircleList);
			}

			if (previousCircleType.ordinal() == 2) {
				middleBoolean = true;
				generalCircleLogic(middleBoolean, mediumCircleDoubles, mediumCircleList);

			}

			if (previousCircleType.ordinal() == 3) {
				largeBoolean = true;
				generalCircleLogic(largeBoolean, largeCircleDoubles, largeCircleList);
			}
		}
	}

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
	}

	public void setSpeedOfPlane(double planeSpeed) {

		this.planeSpeed = planeSpeed;
	}

	public void setCurrentCircleType(CircleType currentCircleType) {
		this.currentCircleType = currentCircleType;

		if (previousCircleType == null) {

			previousCircleType = currentCircleType;
		}
	}

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
