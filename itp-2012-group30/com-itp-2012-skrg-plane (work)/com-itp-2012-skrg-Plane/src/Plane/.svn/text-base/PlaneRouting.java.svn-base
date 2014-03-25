/*
 * Sergei Kostevitch
 * May 3, 2012
 */

package Plane;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;
import message.MessageHandler;
import message.type.Bye;
import message.type.KeepAlive;
import message.type.MoveType;
import message.type.RoutingMessage;
import message.type.RoutingMessageType;

public class PlaneRouting extends TimerTask {

	// Time at which the plane was create + a minor delay before the first
	// RoutingMessage sent

	@SuppressWarnings("unused")
	private long planeCreatedTime;
	@SuppressWarnings("unused")
	private long planeTimeBeforeLastRoutingMessageSent;

	private long periodBetweenRoutingMessages;
	// Know when the last message was sent
	private long planeLastRoutingMessageSent;
	// Know when the before last message was sent

	private MessageHandler messageHandler = null;
	private Plane plane = null;
	private double planeSpeed = 0.0;
	private String planeIDName = null;
	private byte[] planeID = null;

	// The next point where the plane should fly
	private Point instructedPoint;
	private double instructedAngle;
	private KeepAlive keepAlive = null;

	// The three circles coordinates
	// Small Circle = 3 max
	// Medium Circle = 10 max
	// Large Circle = 100 max

	// ------- Circular Logic ------- //
	private boolean oneTimeRadius = true;
	private double radius = 0.0;
	private double timeToDestination = 0.0;
	// ------- Circular Logic ------- //

	private List<RoutingMessage> routingList = null;

	private MoveType moveType = null;

	private RoutingMessage firstMessage = null;

	private boolean firstTime = true;
	private boolean landing = false;
	
	private boolean firstRoutingMessage = true;

	// Where the plane is now
	private Point currentPlanePosition = null;

	public PlaneRouting(long planeCreatedTime,
			long periodBetweenRoutingMessages, Plane plane, byte[] planeID) {

		System.out.println("Created Plane Routing Decision Maker");

		plane.setRoutingDecisionMaker(this);
		this.planeCreatedTime = planeCreatedTime;
		this.periodBetweenRoutingMessages = periodBetweenRoutingMessages;
		this.planeLastRoutingMessageSent = planeCreatedTime;
		messageHandler = MessageHandler.getMessageHandlerInstance();
		this.plane = plane;
		this.planeIDName = new String(planeID);
		this.planeID = planeID;
	}

	@Override
	public void run() {

		planeTimeBeforeLastRoutingMessageSent = planeLastRoutingMessageSent;
		planeLastRoutingMessageSent += periodBetweenRoutingMessages;

		if (routingList != null) {

			firstMessage = routingList.get(0);
			instructedPoint = new Point(firstMessage.getX(),
					firstMessage.getY());
			moveType = firstMessage.getMoveType();
			routingMessageConstruction();
		} else if (routingList == null && firstTime) {

			instructedPoint = new Point(625, 445);
			moveType = MoveType.STRAIGHT;
			routingMessageConstruction();
		} else {

			System.out.println("No Instructions! Crashing...");
			plane.stop();
			this.stop();
		}

	}

	public void routingMessageConstruction() {

		switch (moveType) {

		case STRAIGHT:
			moveInStraightLine(periodBetweenRoutingMessages);
			break;

		case CIRCULAR:
			moveInCircle(periodBetweenRoutingMessages);
			break;

		case LANDING:
			landing = true;
			moveInToLand(periodBetweenRoutingMessages);
			break;

		case NONE:
			break;

		case DESTRUCTION:
			break;

		}

	}

	/**
	 * Landing place is Straight 400, 166; Landing 533, 437;
	 * 
	 */

	private boolean landed = false;

	private void moveInToLand(double delta) {

		if (!landed) {
			moveInStraightLine(delta);
		} else {
			System.out.println("Plane Succesfully landed");
			messageHandler.visit(new Bye(planeID, instructedPoint.x, instructedPoint.y), plane);
			this.stop();
			plane.stop();
			
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

		double distance = Math
				.sqrt(Math.pow(
						instructedPoint.getX() - currentPlanePosition.getX(), 2)
						+ Math.pow(instructedPoint.getY()
								- currentPlanePosition.getY(), 2));

		if (distance > 0) {

			rateOfChangeOfX = (instructedPoint.getX() - currentPlanePosition
					.getX()) / distance;
			rateOfChangeOfY = (instructedPoint.getY() - currentPlanePosition
					.getY()) / distance;

			requiredTimeToReachDestination = distance / planeSpeed;
			actualTimeUntilArrivalAtDestination = (requiredTimeToReachDestination > deltaTimeSeconds) ? requiredTimeToReachDestination
					: deltaTimeSeconds;

			System.out
					.println("----------- Routing Straight Started ----------");
			System.out.println("Plane speed " + planeSpeed);
			System.out.println("Distance to destination " + distance);
			System.out.println("Delta " + deltaTimeSeconds);
			System.out.println("Time needed " + requiredTimeToReachDestination);
			System.out.println("Actual time until destination "
					+ actualTimeUntilArrivalAtDestination);
			System.out.println("Instructed Point x " + instructedPoint.x
					+ " y " + instructedPoint.y);

			if (distance < planeSpeed) {

				routingList.remove(0);

				firstTime = false;
				

				System.out
						.println("----------- Arrived at Waypoint ----------");
				System.out.println("Plane " + planeIDName
						+ " arrived at waypoint " + instructedPoint.x + " , "
						+ instructedPoint.y);
				System.out
						.println("----------- Arrived at Waypoint ----------");
				
				keepAlive = new KeepAlive(planeID, instructedPoint.x, instructedPoint.y);
				currentPlanePosition = new Point(instructedPoint.x, instructedPoint.y);
				messageHandler.visit(keepAlive, plane);

				if (landing) {
					landed = true;
					moveInToLand(periodBetweenRoutingMessages);
				}

			} else {

				newX = currentPlanePosition.getX() + rateOfChangeOfX
						* deltaTimeSeconds * planeSpeed;
				newY = currentPlanePosition.getY() + rateOfChangeOfY
						* deltaTimeSeconds * planeSpeed;

				System.out.println("New Point X " + newX + " New Point Y "
						+ newY);

				keepAlive = new KeepAlive(planeID, (int) newX, (int) newY);
				currentPlanePosition = new Point((int) newX, (int) newY);
				messageHandler.visit(keepAlive, plane);
			}
		}

		System.out.println("----------- Routing Straight Ended ----------");
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
		double newX = 0.0;
		double newY = 0.0;

		instructionAngleRadian = Math.toRadians(instructedAngle);

		if (oneTimeRadius) {

			radius = Math.sqrt(Math.pow(currentPlanePosition.getX()
					- instructedPoint.getX(), 2)
					+ Math.pow(
							currentPlanePosition.getY()
									- instructedPoint.getY(), 2));

			rotationSpeed = planeSpeed / radius;
			neededTime = Math.abs(instructionAngleRadian) / rotationSpeed;
			timeToDestination = neededTime;

			oneTimeRadius = false;
		}

		double angularVelocity = Math.abs(instructionAngleRadian) / radius;
		double timeToTurn = (2 * Math.PI * radius)
				/ (planeSpeed * (360.0 / instructedAngle));

		System.out.println("Angular Velocity " + angularVelocity);
		System.out.println("Time to turn " + timeToTurn);

		instructionAngleSign = Math.signum(instructionAngleRadian);

		intermediateX = (currentPlanePosition.getX() - instructedPoint.getX());
		intermediateY = (currentPlanePosition.getY() - instructedPoint.getY());

		theta = computeTheta(intermediateX, intermediateY, 0, 0);
		rotationSpeed = planeSpeed / radius;
		deltaTheta = rotationSpeed * deltaTimeSeconds;
		neededTime = Math.abs(instructionAngleRadian) / rotationSpeed;
		distanceToDestination = timeToDestination * planeSpeed;
		timeToDestination = timeToDestination - deltaTimeSeconds;

		System.out.println("----------- Routing Circle Started ----------");
		System.out.println("Radius " + radius);
		System.out.println("Rotation speed " + rotationSpeed);
		System.out.println("Instructed Point x " + instructedPoint.x + " y "
				+ instructedPoint.y);
		System.out.println("InstructionAngleRadians " + instructionAngleRadian);
		System.out.println("InstructionAngleSign " + instructionAngleSign);
		System.out.println("Delta theta " + deltaTheta);
		System.out.println("Needed time " + neededTime);
		System.out.println("Time to destination " + timeToDestination);
		System.out.println("Distance to destination " + distanceToDestination);

		if (deltaTheta < Math.abs(instructionAngleRadian)) {

			theta = theta + instructionAngleSign * deltaTheta;

			newX = (int) (radius * Math.cos(theta) + instructedPoint.getX());
			newY = (int) (radius * Math.sin(theta) + instructedPoint.getY());

			System.out.println("New Point X " + newX + " New Point Y " + newY);

			keepAlive = new KeepAlive(planeID, (int) newX, (int) newY);
			currentPlanePosition = new Point((int) newX, (int) newY);
			messageHandler.visit(keepAlive, plane);

		}

		System.out.println("Theta " + theta);

		if (distanceToDestination < planeSpeed) {

			oneTimeRadius = true;
			routingList.remove(0);

			System.out
					.println("----------- Arrived at Circle Waypoint ----------");
			System.out.println("Plane " + planeIDName + " arrived at waypoint "
					+ instructedPoint.x + " , " + instructedPoint.y);
			System.out
					.println("----------- Arrived at Circle Waypoint ----------");

		}

		System.out.println("----------- Routing Circle Ended ----------");
	}

	private double computeTheta(double px, double py, double cx, double cy) {

		return Math.atan2(py - cy, px - cx);
	}

	public void setSpeedOfPlane(double planeSpeed) {

		this.planeSpeed = planeSpeed;
	}

	public synchronized void stop() {

		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void addRoutingMessage(RoutingMessage routingMessage,
			RoutingMessageType routingType) {

		if (routingList == null) {
			routingList = new LinkedList<RoutingMessage>();
		}
		
		if (routingList.size() != 0) {
			if (routingList.get(routingList.size() - 1).getX() != routingMessage
					.getX()
					&& routingList.get(routingList.size() - 1).getX() != routingMessage
							.getX()) {

				switch (routingType) {
				case NEWFIRST:
					for (int i = 0; i < routingList.size(); i--) {
						routingList.set(i + 1, routingList.get(i));
					}
					routingList.set(0, routingMessage);
					break;

				case LAST:
					routingList.add(routingMessage);
					break;

				case REPLACEALL:
					routingList.clear();
					routingList.add(routingMessage);
					break;
				}
			}
		}
		
		if (firstRoutingMessage) {
			
			routingList.add(routingMessage);
			firstRoutingMessage = false;
		}
	}

	public void setCurrentPlanePosition(Point currentPlanePosition) {
		this.currentPlanePosition = currentPlanePosition;
	}
}
