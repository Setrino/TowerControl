/*
 * Sergei Kostevitch
 * Mar 14, 2012
 */

package plane;

import gui.given.PlaneDrawnForGivenGui;

import java.awt.Point;
import java.awt.image.RasterOp;

import messages.Message;
import messages.MessageSender;

// TODO: Auto-generated Javadoc
/**
 * The Class Plane.
 */
public class Plane {

	/** The plane handler. */
	private PlaneHandler planeHandler;
	private MessageSender messageSender;
	private PlaneRouting planeRoutingDecisionMaker;
	private PlaneDrawnForGivenGui planeDrawn;

	/** The plane name. */
	private String planeName;

	/** The plane id. */
	private byte[] planeID;

	/** The pos x. */
	private int posX;

	/** The pos y. */
	private int posY;

	/** The point. */
	private Point point;

	private boolean crashedPlane = false;

	private boolean firstTime = true;

	private PlaneType planeType;
	private double planeSpeed;
	private final static double PIXELS_SPEED = 340.3 / 12.0;
	private int maxFuelCapacity;
	private int fuelConsumptionPerMinute;
	private int noOfPassengers;

	/**
	 * Instantiates a new plane.
	 * 
	 * @param planeHandler
	 *            the plane handler
	 */
	public Plane(PlaneHandler planeHandler) {

		this.planeHandler = planeHandler;

	}

	/**
	 * Gets the plane name.
	 * 
	 * @return the plane name
	 */
	public String getPlaneName() {
		return planeName;
	}

	/**
	 * Sets the plane name.
	 * 
	 * @param planeName
	 *            the new plane name
	 */
	public void setPlaneName(String planeName) {
		this.planeName = planeName;
	}

	/**
	 * Gets the plane id.
	 * 
	 * @return the plane id
	 */
	public byte[] getPlaneID() {
		return planeID;
	}

	/**
	 * Sets the plane id.
	 * 
	 * @param planeID
	 *            the new plane id
	 */
	public void setPlaneID(byte[] planeID) {
		this.planeID = planeID;
	}

	/**
	 * Gets the pos x.
	 * 
	 * @return the pos x
	 */
	public int getPosX() {
		return posX;
	}

	/**
	 * Sets the pos x.
	 * 
	 * @param posX
	 *            the new pos x
	 */
	public void setPosX(int posX) {
		this.posX = posX;
	}

	/**
	 * Gets the pos y.
	 * 
	 * @return the pos y
	 */
	public int getPosY() {
		return posY;
	}

	/**
	 * Sets the pos y.
	 * 
	 * @param posY
	 *            the new pos y
	 */
	public void setPosY(int posY) {
		this.posY = posY;
	}

	/**
	 * Gets the point.
	 * 
	 * @return the point
	 */
	public Point getPoint() {
		return point;
	}

	public void sendMessage(Message message) {

		messageSender.addMessageToQueue(message);

		if (firstTime) {
			messageSender.start();
			// messageSender.stopThread();
			firstTime = false;
		}

		// messageSender.restart();

	}

	/**
	 * Sets the point.
	 * 
	 * @param point
	 *            the new point
	 */
	public void setPoint(Point point) {
		this.point = point;
		if (planeRoutingDecisionMaker != null) {
			planeRoutingDecisionMaker.setCurrentPoint(point);
		}		
	}

	public void setStopFlag(boolean setFlag) {

		planeHandler.setStopFlag(setFlag);
	}

	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	public void setCrashedStatus(boolean crashed) {

		crashedPlane = crashed;
	}

	public boolean hasCrashed() {

		return crashedPlane;
	}

	public double getPlaneSpeed() {
		return planeSpeed;
	}

	public void setPlaneSpeed(double planeSpeed) {
		this.planeSpeed = planeSpeed;
	}
	
	public void setRoutingDecisionMaker(PlaneRouting planeRoutingDecisionMaker){
		
		this.planeRoutingDecisionMaker = planeRoutingDecisionMaker;
	}
	
	public String getPlaneType(){
		
		return planeType.toString();
	}
	
	public void setRoutingCircleType(CircleType circleType){
		
		planeRoutingDecisionMaker.setCurrentCircleType(circleType);
	}
	
	public void setPlaneDrawn(PlaneDrawnForGivenGui planeDrawn){
		
		this.planeDrawn = planeDrawn;
	}

	public void setPlaneType(String planeTypeString) {

		planeType = PlaneType.valueOf(planeTypeString);

		switch (planeType) {
		case A320:

			planeSpeed = 0.78 * PIXELS_SPEED;
			maxFuelCapacity = 10000;
			fuelConsumptionPerMinute = 60;
			noOfPassengers = 179;

			break;
		case A380:

			planeSpeed = 0.89 * PIXELS_SPEED;
			maxFuelCapacity = 80000;
			fuelConsumptionPerMinute = 115;
			noOfPassengers = 644;

			break;
		case B787_DREAMLINER:

			planeSpeed = 0.85 * PIXELS_SPEED;
			maxFuelCapacity = 15000;
			fuelConsumptionPerMinute = 63;
			noOfPassengers = 242;

			break;
		case CONCORDE:

			planeSpeed = 2.02 * PIXELS_SPEED;
			maxFuelCapacity = 120000;
			fuelConsumptionPerMinute = 461;
			noOfPassengers = 140;

			break;
		case GRIPEN:

			planeSpeed = 2.0 * PIXELS_SPEED;
			maxFuelCapacity = 45000;
			fuelConsumptionPerMinute = 200;
			noOfPassengers = 1;

			break;

		}
		planeRoutingDecisionMaker.setSpeedOfPlane(planeSpeed);
	}

	public void stopAll() {
				
		System.out.println("All Threads for Plane " + planeName + " closed.");
		planeDrawn.stop();
		planeHandler.stopThread();
	}
}
