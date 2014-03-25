/* 
 * Sergei Kostevitch
 * Gabriel Rodrigues Gomes
 */

package tower;

import file.DataFile;
import gui.RadarFrame;
import gui.given.AirportPanel;

import java.awt.Point;
import java.io.File;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import plane.CircleType;
import plane.Plane;
import plane.PlaneAccepter;

import messages.Choke;
import messages.Message;
import messages.MessageHandler;
import messages.UnChoke;
import crypto.KeyGenerator;
import crypto.KeyPair;

// TODO: Auto-generated Javadoc
/**
 * The Class Tower.
 */
public class Tower {

	/**
	 * The instance tower.
	 * The use of the word "volatile" avoids the case when "Tower.instance"
	 * is non-zero but still not really instantiated.
	 */
	private static volatile Tower instanceTower = null;

	/** The queue for all messages that go out to all planes. */
	private PriorityBlockingQueue<Message> outputQueue;

	/**
	 * The queue for all messages that are received from all planes. String[0]
	 * is priority of Message, String[1] is type of Message, String[2] is
	 * planeName.
	 */
	private PriorityBlockingQueue<String[]> inputQueue;

	/** The map of planes. */
	private Map<String, Plane> mapOfPlanes;

	private List<Plane> listOfPlanes;

	/** 
	 * Stores planePosition and Associates it with planeName
	 * The plane position. 
	 */
	private Map<String, Point> planePosition;

	private List<Plane> landingRequestPerPlane = null;

	/** The plane accepter. */
	private PlaneAccepter planeAccepter;

	private RadarFrame radarFrame;

	@SuppressWarnings("unused")
	private File mainFile = null;

	private final String MAIN_FILE_PATH = System.getProperty("user.home")
			+ File.separator + "Documents" + File.separator
			+ "AirPlane Control";

	private Map<String, DataFile> dataFileMap = null;

	private KeyPair keyPair = null;

	private int totalNoOfPlanes = 0;

	private AirportPanel airportPanel = null;

	private List<Point> listOfPointFromRouting = null;

	/**
	 * Integer to remember the exact order in which the planes have been
	 * arriving
	 */
	private List<Plane> orderOfPlanesArrival = null;

	// Setting up a Singleton Pattern
	/**
	 * Gets the tower instance.
	 * 
	 * @return the tower instance
	 */
	public static synchronized Tower getTowerInstance() {

		if (instanceTower == null) {

			instanceTower = new Tower();
		}

		return Tower.instanceTower;
	}

	/**
	 * Instantiates a new tower.
	 * The Tower does multiple things - create a file with TowerKey,
	 * control of planes where to go using globalTowerRouting() method,
	 * takes care of MayDay, Unchoke, Choke, stores the points of 
	 * each plane, contains list of planes, map of planes with
	 * associated names as String, start the ServerSocker on 
	 * PlaneAccepter, contains the list of landing requests by the
	 * order of them received
	 */
	private Tower() {

		mapOfPlanes = new HashMap<String, Plane>(25);
		listOfPlanes = new LinkedList<Plane>();
		dataFileMap = new HashMap<String, DataFile>();
		orderOfPlanesArrival = new LinkedList<Plane>();
		landingRequestPerPlane = new LinkedList<Plane>();
		outputQueue = new PriorityBlockingQueue<Message>(100,
				blockingQueueComparator());
		inputQueue = new PriorityBlockingQueue<String[]>(100,
				blockingInputQueueComparator());
		planePosition = new HashMap<String, Point>();
		planeAccepter = new PlaneAccepter();
		planeAccepter.setDaemon(true);
		planeAccepter.start();
		mainFile = checkAndCreateMainFile();
		keyPair = KeyGenerator.generateRSAKeyPair(128);
		//keyPair.writeKeyToFile();

		listOfPointFromRouting = new LinkedList<Point>();
	}

	/**
	 * The method that takes care of the actual plane routing
	 * 
	 * If only 1 plane (or first) by the list, we land it,
	 * next 3 go in SMALL circle, next 10 in MEDIUM circle, and the
	 * rest in last LARGE circle
	 * Updated after Bye, or Plane Crashing
	 */
	
	public void globalTowerRouting() {

		int totalNoOfPlanes = listOfPlanes.size();

		// Landing
		if (totalNoOfPlanes >= 1) {

			listOfPlanes.get(0).setRoutingCircleType(CircleType.LANDING);

			// Small Circle
			if (totalNoOfPlanes <= 4) {

				for (int i = 1; i < listOfPlanes.size(); i++) {

					listOfPlanes.get(i).setRoutingCircleType(CircleType.SMALL);
				}
			}
			// Medium Circle
			if ((totalNoOfPlanes < 15) && (totalNoOfPlanes > 4)) {

				for (int j = 4; j < listOfPlanes.size(); j++) {

					listOfPlanes.get(j).setRoutingCircleType(CircleType.MEDIUM);
				}
			}
			// Large Circle
			if (totalNoOfPlanes > 14) {

				for (int k = 14; k < listOfPlanes.size(); k++) {

					listOfPlanes.get(k).setRoutingCircleType(CircleType.LARGE);
				}
			}
		}
	}

	public Map<String, Plane> getMapOfPlanes() {
		return mapOfPlanes;
	}

	public List<Plane> getListPlanes() {
		return listOfPlanes;
	}

	// Adding Plane to Big List of Planes for further control
	/**
	 * Adds the plane to list.
	 * 
	 * @param plane
	 *            the plane
	 */
	public synchronized void addPlaneToMap(String planeIdString, Plane plane) {

		mapOfPlanes.put(planeIdString, plane);

	}

	public synchronized void addPlaneToList(Plane plane) {

		listOfPlanes.add(plane);

	}

	// Add a Message to a big queue for further control in case of MayDay
	/**
	 * Adds the message to priority blocking queue.
	 * 
	 * @param message
	 *            the message
	 */
	public synchronized void addMessageToPriorityBlockingQueue(Message message) {

		outputQueue.add(message);
	}

	// Remove the Message to insure the queue is synchronized
	/**
	 * Removes the message from priority blocking queue.
	 * 
	 * @param message
	 *            the message
	 */
	public synchronized void removeMessageFromPriorityBlockingQueue(
			Message message) {

		outputQueue.remove(message);
	}

	/**
	 * Sets the order in which the planes will be landed depending on the order
	 * they have first came into view and communication with tower
	 */
	public void setOrderOfLandingByOrderOfAppearanceOnMap() {

		listOfPlanes.clear();
		listOfPlanes.addAll(orderOfPlanesArrival);
	}

	/**
	 * Sets the order in which the planes will be landed depending on the order
	 * of which plane sent the first landing request
	 */
	public void setOrderOfLandingByOrderOfLandingRequests() {

		listOfPlanes.clear();
		listOfPlanes.addAll(landingRequestPerPlane);

	}

	/**
	 * Blocking queue comparator for outputStream.
	 * 
	 * @return the comparator
	 */
	public Comparator<Message> blockingQueueComparator() {

		Comparator<Message> comparator = new Comparator<Message>() {

			public int compare(Message m1, Message m2) {
				return m1.getPriority() < m2.getPriority() ? -1 : 1;
			}
		};

		return comparator;

	}

	/** Compare the first entries of String[] equivalent to priority */

	public Comparator<String[]> blockingInputQueueComparator() {

		Comparator<String[]> comparator = new Comparator<String[]>() {

			public int compare(String[] first, String[] second) {
				return new Integer(first[0]) < new Integer(second[0]) ? -1 : 1;
			}
		};
		return comparator;
	}
	
	/**
	 * Chokes all planes if a MayDay has been received
	 * and the one with MayDay is sent straight to Landing
	 * 
	 * @param plane
	 */

	public void chokeAllPlanes(Plane plane) {

		Choke choke = null;
		String planeName = null;

		for (Iterator<Plane> iterator = listOfPlanes.iterator(); iterator
				.hasNext();) {

			Plane planeIt = iterator.next();
			planeName = new String(planeIt.getPlaneID());
			if (!plane.equals(plane)) {

				choke = new Choke(planeIt.getPlaneID(), 0, planeIt.getPosX(),
						planeIt.getPosY());

				MessageHandler.getMessageHandlerInstance().visit(choke,
						"Tower", planeName, planeIt);
			} else {
				plane.setBothRoutingCircleType(CircleType.LANDING);
			}
		}
	}
	

	/**
	 * 
	 * When the mayDay has Passed, unchokeAll planes and update
	 * globalTowerRouting
	 */
	
	public void unChokeAll() {

		UnChoke unChoke = null;

		for (Iterator<Plane> iterator = listOfPlanes.iterator(); iterator
				.hasNext();) {

			Plane plane = iterator.next();

			unChoke = new UnChoke(plane.getPlaneID(), 0, plane.getPosX(),
					plane.getPosY());

			MessageHandler.getMessageHandlerInstance().visit(unChoke, "Tower",
					new String(plane.getPlaneID()), plane);
		}
		globalTowerRouting();
	}

	public synchronized void addMessageToInputQueue(String[] stringMessageArray) {

		inputQueue.add(stringMessageArray);
	}

	public synchronized void removeMessageFromInputQueue(
			String[] stringMessageArray) {

		inputQueue.remove(stringMessageArray);
	}

	// Sets the pointPosition for specific plane

	/**
	 * Sets the plane position.
	 * 
	 * @param planeIdString
	 *            the plane id string
	 * @param point
	 *            the point
	 */
	public void setPlanePosition(String planeIdString, Point point) {

		planePosition.put(planeIdString, point);
	}

	// Retrieves planePosition HashMap

	/**
	 * Gets the plane position.
	 * 
	 * @return the plane position
	 */
	public Map<String, Point> getPlanePosition() {

		return planePosition;
	}

	public void setRadarFrame(RadarFrame radarFrame) {

		this.radarFrame = radarFrame;
	}

	public RadarFrame getRadarFrame() {

		return radarFrame;
	}

	public File checkAndCreateMainFile() {

		File file = new File(MAIN_FILE_PATH);

		if (!file.exists()) {

			file.mkdirs();
		}

		return file;
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
	 * Added for future use to know who was the first to send the
	 * planeLandingRequest and land them on that order
	 * 
	 * @param planeIdString
	 * @param landingRequestsCounter
	 *            no of times plane with associated name sent LandingRequest
	 */

	public void addLandingRequestPerPlane(Plane plane) {

		landingRequestPerPlane.add(plane);

	}

	// Red points given from routing a plane
	public void addPointToListFromRouting(Point point) {

		listOfPointFromRouting.add(point);
	}

	public List<Point> listOfPointsFromRouting() {

		return listOfPointFromRouting;
	}

	// Given GUI Airport Panel
	public void setAirportPanel(AirportPanel airportPanel) {

		this.airportPanel = airportPanel;
	}

	public AirportPanel getAirportPanel() {

		return airportPanel;
	}

	public int getTotalNoOfPlanes() {
		return totalNoOfPlanes;
	}

	public void setTotalNoOfPlanes(int totalNoOfPlanes) {
		this.totalNoOfPlanes = totalNoOfPlanes;
		System.out.println("No of planes " + totalNoOfPlanes);
	}

	// Used to keep track of orderOfPlanes arrival in tower view
	public List<Plane> getOrderOfPlanesArrival() {
		return orderOfPlanesArrival;
	}

	public void setOrderOfPlanesArrival(List<Plane> orderOfPlanesArrival) {
		this.orderOfPlanesArrival = orderOfPlanesArrival;
	}

	/**
	 * Keep the exact track of order of planes arrival
	 * 
	 * @param plane
	 */
	public void addPlaneToOrderOfArrivalInView(Plane plane) {

		orderOfPlanesArrival.add(plane);
	}

	/**
	 * If plane landed - remove that plane from list of planes in air
	 * 
	 * @param plane
	 */
	public void removePlaneFromAllLists(Plane plane) {

		listOfPlanes.remove(plane);
		orderOfPlanesArrival.remove(plane);

		if (landingRequestPerPlane.contains(plane)) {
			landingRequestPerPlane.remove(plane);
		}
	}

	public KeyPair getKeyPair() {
		return keyPair;
	}


}
