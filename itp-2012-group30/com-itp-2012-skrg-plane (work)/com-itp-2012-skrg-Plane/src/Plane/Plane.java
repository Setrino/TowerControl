/*
 * Sergei Kostevitch
 * Mar 14, 2012
 */

package Plane;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import message.Message;
import message.MessageSender;

import connection.TowerConnector;

import file.DataFile;
import file.PropertyFileRetrieve;

// TODO: Auto-generated Javadoc
/**
 * The Class Plane.
 */
public class Plane {

	private final static double PIXELS_SPEED = 340.3 / 12.0;
	private boolean choked = false;
	private boolean crashedPlane = false;

	@SuppressWarnings("unused")
	private int fuelConsumptionPerMinute;
	@SuppressWarnings("unused")
	private int maxFuelCapacity;
	@SuppressWarnings("unused")
	private int noOfPassengers;
	@SuppressWarnings("unused")
	private PlaneRouting planeRoutingDecisionMaker;
	
	private PlaneHandler planeHandler = null;

	

	/** The plane handler. */

	/** The plane id. */
	private byte[] planeID;

	/** The plane name. */
	private String planeName;

	private double planeSpeed;
	private PlaneType planeType;
	/** The point. */
	private Point point;
	/** The pos x. */
	private int posX = 0;
	/** The pos y. */
	private int posY = 0;

	private byte reserved;

	private String hostName;
	private int portNumber;
	private long messageTimeOut;
	private int planeIdSize = 8;
	private long keepAliveInterval;
	@SuppressWarnings("unused")
	private long dataInterval;
	@SuppressWarnings("unused")
	private long planeUpdateInterval;

	private Map<String, DataFile> dataFileMap = null;

	private MessageSender messageSender;

	private static Plane planeInstance = null;

	public static synchronized Plane getPlaneInstance() {

		if (planeInstance == null) {

			planeInstance = new Plane();
		}

		return planeInstance;
	}

	private final static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	private static String generateName(int planeSize){
		
		String name = "";
		for (int i = 0; i < planeSize; i++) {
			
			if (i < 2) {
				
				int chara = (int) (Math.random() * 26);
				name += alphabet.substring((chara), chara + 1);
			} else{
				
				name += (int) (Math.random() * 9);
			}
		}
		
		return name;
	}

   // Adds a dataFile to Map

	public void addDataFile(String hashAndPlaneIDName, DataFile dataFile) {

		dataFileMap.put(hashAndPlaneIDName, dataFile);
	}

	// Checks for DataFile existance based on hashKey

	public DataFile checkForDataFile(String hashAndPlaneIDName) {

		return (DataFile) dataFileMap.get(hashAndPlaneIDName);
	}

	/**
	 * Instantiates a new plane.
	 */
	private Plane() {

		setPlaneType("A320");
		setPosX(10);
		setPosY(20);
		setReserved((byte) 0);
		planeName = generateName(planeIdSize);
		planeID = planeName.getBytes();
		dataFileMap = new HashMap<String, DataFile>();
		new PropertyFileRetrieve(this);
		new TowerConnector(hostName, portNumber, this);
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
	 * Gets the plane name.
	 * 
	 * @return the plane name
	 */
	public String getPlaneName() {
		return planeName;
	}

	public double getPlaneSpeed() {
		return planeSpeed;
	}

	/**
	 * Gets the point.
	 * 
	 * @return the point
	 */
	public Point getPoint() {
		return point;
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
	 * Gets the pos y.
	 * 
	 * @return the pos y
	 */
	public int getPosY() {
		return posY;
	}

	public boolean hasCrashed() {

		return crashedPlane;
	}

	public boolean isChoked() {
		return choked;
	}

	// public void sendMessage(Message message) {
	//
	// messageSender.addMessageToQueue(message);
	//
	// if (firstTime) {
	// messageSender.start();
	// // messageSender.stopThread();
	// firstTime = false;
	// }
	//
	// // messageSender.restart();
	//
	// }

	public void setChoked(boolean choked) {
		this.choked = choked;
	}

	public void setCrashedStatus(boolean crashed) {

		crashedPlane = crashed;
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
	 * Sets the plane name.
	 * 
	 * @param planeName
	 *            the new plane name
	 */
	public void setPlaneName(String planeName) {
		this.planeName = planeName;
	}

	public void setPlaneSpeed(double planeSpeed) {
		this.planeSpeed = planeSpeed;
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
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public void setMessageTimeout(long messageTimeOut) {

		this.messageTimeOut = messageTimeOut;
	}

	public void setPlaneIdSize(int planeIdSize) {

		this.planeIdSize = planeIdSize;
	}

	public void setKeepAliveInterval(long keepAliveInterval) {

		this.keepAliveInterval = keepAliveInterval;
	}

	public void setDataInterval(long dataInterval) {

		this.dataInterval = dataInterval;
	}

	public void setPlaneUpdateInterval(long planeUpdateInterval) {

		this.planeUpdateInterval = planeUpdateInterval;
	}

	public void setTowerPort(int portNumber) {

		this.portNumber = portNumber;
	}

	public void setTowerHost(String hostName) {

		this.hostName = hostName;
	}

	public void setReserved(byte reserved) {

		this.reserved = reserved;
	}

	public void setPlaneHandler(PlaneHandler planeHandler) {

		this.planeHandler = planeHandler;
		planeHandler.setInitialPosX(posX);
		planeHandler.setInitialPosY(posY);
		planeHandler.setPlaneID(planeID);
		planeHandler.setReserved(reserved);
		planeHandler.setPlaneType(planeType);
		planeHandler.setMessageTimeOut(messageTimeOut);
	}

	public void setMessageSender(MessageSender messageSender) {

		this.messageSender = messageSender;
	}
	
	public void sendMessage(Message message){
		
		messageSender.addMessageToQueue(message);
	}

	public void setRoutingDecisionMaker(PlaneRouting planeRoutingDecisionMaker) {

		this.planeRoutingDecisionMaker = planeRoutingDecisionMaker;
		planeRoutingDecisionMaker.setSpeedOfPlane(planeSpeed);
	}

	public long getPlaneUpdateInterval() {
		return keepAliveInterval;
	}

	public synchronized void stop() {
		
		planeHandler.stopThread();
	}

}
