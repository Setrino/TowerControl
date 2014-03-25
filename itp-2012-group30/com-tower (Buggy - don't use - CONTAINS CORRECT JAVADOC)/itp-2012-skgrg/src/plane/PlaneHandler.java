/*
 * Sergei Kostevitch
 * Mar 14, 2012
 */

package plane;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import tower.Tower;
import crypto.RSAInputStream;
import crypto.RSAOutputStream;

import file.XMLFileCreator;
import gui.RadarFrame;
import gui.given.AirportPanel;

import messages.Hello;
import messages.Message;
import messages.MessageHandler;
import messages.MessageReader;
import messages.MessageSender;
import messages.SendRSAKey;

// TODO: Auto-generated Javadoc
/**
 * The Class PlaneHandler is used to read from InputStream, creates all instances for Plane 
 * (MessageSender, MessageReader, PlaneRouting, Plane)
 * Adds plane Thread to the GUI, initialises connection with Plane (HandShake)
 * If something plane landed or something goes wrong - .wait() all Threads for this Plane
 * Creates a separate folder for each plane
 */
public class PlaneHandler extends Thread {

	/** The socket. */
	private Socket socket;

	/** The input stream. */
	private InputStream inputStream;

	/** The output stream. */
	private OutputStream outputStream;

	/** The byte array output stream. */
	private ByteArrayOutputStream byteArrayOutputStream;

	/** The message reader. */
	private MessageReader messageReader;

	/** The message sender. */
	private MessageSender messageSender;

	/** The message handler. */
	private MessageHandler messageHandler = MessageHandler
			.getMessageHandlerInstance();

	/** The message. */
	private Message message;

	/** The time counter per plane. */
	private long timeCounterPerPlane;

	/** The xml file. */
	private XMLFileCreator xmlFile;

	/** The string array. */
	private String[] stringArray;

	/** The plane id. */
	private byte[] planeID;

	/** The tower. */
	private Tower tower;

	/** Time instance */
	private Date date;

	/** DateFormat */
	private SimpleDateFormat dateFormat;

	private Plane plane = null;

	private final String MAIN_FILE_PATH = System.getProperty("user.home")
			+ File.separator + "Documents" + File.separator
			+ "AirPlane Control";

	private RadarFrame radarFrame = null;
	
	@SuppressWarnings("unused")
	private AirportPanel airportPanel = null;

	@SuppressWarnings("unused")
	private File planeFile = null;

	@SuppressWarnings("unused")
	private boolean encryptedConnection = false;

	private String planeIdString = null;

	private Timer timer = null;

	// Delay between each Routing Message Sent

	private final long PERIOD_BETWEEN_ROUTING_MESSAGES = 1 * 500;

	// A minor delay to start sendingRoutingMessages

	private final long FIRST_TIME = 2 * 1000;
	
	private PlaneRouting planeRouting = null;
	
	private PlaneConnectionHandler planeConnectionHandler = null;
	
	private SendRSAKey sendRSA = null;

	/**
	 * Instantiates a new plane handler.
	 * 
	 * @param socket
	 *            the socket
	 */
	public PlaneHandler(Socket socket) {

		this.socket = socket;
		xmlFile = XMLFileCreator.getXMLFileCreatorInstance();
		tower = Tower.getTowerInstance();
		this.radarFrame = tower.getRadarFrame();
		this.airportPanel = tower.getAirportPanel();
		plane = new Plane(this);
		dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
		messageReader = new MessageReader();
		messageReader.setPlane(plane);

		System.out.println("In PlaneHandler");
		establistConnection();

	}

	/**
	 * Establist connection is the one that takes care of HandShake and onwards takes care of
	 * Reading from InputStream using the PlaneConnectionHandler
	 */
	public synchronized void establistConnection() {

		planeConnectionHandler = new PlaneConnectionHandler(socket);
	}

	/**
	 * The Class PlaneConnectionHandler.
	 */
	class PlaneConnectionHandler implements Runnable {

		/** The socket. */
		private Socket socket;

		/**
		 * Instantiates a new plane connection handler.
		 * 
		 * @param socket
		 *            the socket
		 */
		public PlaneConnectionHandler(Socket socket) {

			this.socket = socket;

			if (socket == null) {

				System.out.println("Socket is null");

				try {

					System.out.println("Closing socket");
					this.socket.close();

				} catch (IOException e) {

					System.out.println("Unable to close socket");
					e.printStackTrace();
				}
			} else {

				handShake();
				Thread planeThread = new Thread(this);
				planeThread.start();
			}
		}
		
		private int noOfTimes = 0;
		private long time = 0;
		private long timeDifference = 0;

		/**
		 * Read from InputStream, if no data received for a long time, kill the thread
		 */
		@Override
		public void run() {

			while (true) {

				try {

					while (inputStream.available() > 0) {

						messageReader.readMessage(inputStream);
						time = 0l;
						timeDifference = 0l;
						noOfTimes = 0;
					}
					
					if (inputStream.available() == 0) {
						
						if (noOfTimes == 0) {
							
							time = System.currentTimeMillis();
							noOfTimes++;
						}
						else{							
							timeDifference = System.currentTimeMillis() - time;
							noOfTimes++;
						}
						
						if (timeDifference > 3000) {
							
							stopThread();
						}
					}

					Thread.sleep(10);


				} catch (InterruptedException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();

					System.out.println("Plane Gone");
				}

			}
		}

		/** In case of MayDay ignore anything coming from plane */

		/** Restart the connection with the Plane */

		synchronized public void restart() {

			this.notify();
		}

		/**
		 * Handshake with plane, setPlaneID, PlaneName, Add plane to the TowerGUI, check for 
		 * RSA type of connection, starts MessageReader, MessageSender, PlaneRouting initialized
		 */
		private void handShake() {

			try {

				timeCounterPerPlane = System.currentTimeMillis();
				date = new Date(timeCounterPerPlane);
				System.out.println("Handshake with Plane at "
						+ dateFormat.format(date));
				outputStream = new DataOutputStream(socket.getOutputStream());
				inputStream = new DataInputStream(socket.getInputStream());
				byteArrayOutputStream = new ByteArrayOutputStream();
				messageSender = new MessageSender(outputStream, plane);
				plane.setMessageSender(messageSender);
				System.out.println("Connected and Established Input/Output"
						+ " MessageReader/MessageSender");
				
				if (inputStream.available() > 0) {

					message = messageReader.readMessage(inputStream);

					if (message instanceof Hello) {

						System.out.println("Inside");

						planeID = message.getPlaneID();
						plane.setPlaneID(planeID);
						planeIdString = new String(planeID);
						plane.setPlaneName(planeIdString);

						// ------------- ADD PLANE ------------//
						tower.addPlaneToMap(planeIdString, plane);
						tower.addPlaneToList(plane);
						tower.addPlaneToOrderOfArrivalInView(plane);
						// ------------- ADD PLANE ------------//

						stringArray = xmlFile
								.readFromXMLFileHelloMessage(planeID);

						String[][] messageArray = new String[5][2];
						messageArray[0][1] = "Tower";
						messageArray[1][1] = planeIdString;
						messageArray[2][0] = "PosX";
						messageArray[2][1] = stringArray[0];
						messageArray[3][0] = "PosY";
						messageArray[3][1] = stringArray[1];
						messageArray[4][0] = "Reserved";
						messageArray[4][1] = stringArray[2];

						messageHandler.visit((Hello) message, "Tower",
								planeIdString, messageArray);

						if (Integer.parseInt(stringArray[2]) == 0) {

							message = new Hello(planeID,
									Integer.parseInt(stringArray[0]),
									Integer.parseInt(stringArray[1]), (byte) 0);
							
							System.out.println("Output Stream " + outputStream);
							message.sendMessage(byteArrayOutputStream);
							byteArrayOutputStream.writeTo(outputStream);
							byteArrayOutputStream.flush();
							byteArrayOutputStream.reset();
							outputStream.flush();
						}

						// Check if the Plane has sent a reserved byte non-zero
						// and start using RSAOutputStream/RSAInputStream

						else if (Integer.parseInt(stringArray[2]) != 0) {

							System.out.println("Encrypted connection");

							encryptedConnection = true;

							message = new Hello(planeID,
									Integer.parseInt(stringArray[0]),
									Integer.parseInt(stringArray[1]),
									Byte.parseByte(stringArray[2]));
							
							message.sendMessage(byteArrayOutputStream);
							
							System.out.println("Output Stream " + outputStream);
							message.sendMessage(byteArrayOutputStream);
							byteArrayOutputStream.writeTo(outputStream);
							byteArrayOutputStream.flush();
							byteArrayOutputStream.reset();
							outputStream.flush();
							
							inputStream = new RSAInputStream(Tower.getTowerInstance().getKeyPair(), inputStream);
							
							Thread.sleep(900);
							
							do {
								Thread.sleep(100);
							} while (inputStream.available() == 0);
							
							message = messageReader.readMessage(inputStream);

							System.out.println("Passed");


							if (message instanceof SendRSAKey) {

								System.out.println("read");
								
								sendRSA = (SendRSAKey) message;
								
								outputStream = new RSAOutputStream(sendRSA.getKeyPair(), outputStream);
							}
						}
					
						// --------------------- IMPORTANT ----------------//
						radarFrame.addPlaneToTheFrame(planeIdString, plane);
						//airportPanel.addPlaneToTheFrame(planeIdString);
						// --------------------- IMPORTANT ----------------//

						planeFile = createPlaneFile(planeIdString);

						/**
						 * Add something with tower to tell the plane where
						 * about the currentCircleType
						 */
						
						planeRouting = new PlaneRouting(timeCounterPerPlane + FIRST_TIME,
								PERIOD_BETWEEN_ROUTING_MESSAGES, plane, planeID);
						messageSender.setPlaneRouting(planeRouting);
						tower.globalTowerRouting();
						timer = new Timer();
						timer.schedule(planeRouting, FIRST_TIME,
								PERIOD_BETWEEN_ROUTING_MESSAGES);
						
					}
				}

			} catch (Exception e) {

			}
		}
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}
	
	/**
	 * Creates a separate folder for each plane
	 * @param plainIDName
	 * @return
	 */

	public File createPlaneFile(String plainIDName) {

		File file = new File(MAIN_FILE_PATH + File.separator + plainIDName);

		if (!file.exists()) {

			file.mkdir();
		}

		return file;
	}

	public void encryptTheStream() {

	}
	
	/**
	 * In case of landing or crash - close it all
	 */
	
	public synchronized void stopThread() {

		try {
			plane.setCrashedStatus(true);
			tower.removePlaneFromAllLists(plane);
			tower.globalTowerRouting();
			socket.close();
			System.out.println("Connection with plane lost.");		
			timer.cancel();
			planeRouting.stop();
			messageSender.stopThread();
			planeConnectionHandler.wait();
			
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
