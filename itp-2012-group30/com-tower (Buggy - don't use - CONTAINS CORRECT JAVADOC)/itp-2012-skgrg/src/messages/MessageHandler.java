/*
 * Sergei Kostevitch
 * Mar 10, 2012
 */

package messages;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import plane.Plane;
import tower.Tower;
import file.DataFile;
import file.XMLFileCreator;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageHandler.
 */
public class MessageHandler implements MessageHandlerVisitor {

	/**
	 * Implementing VisitorPattern to be used for XML, MessageSender
	 * 
	 * Singleton Pattern
	 */
	private static MessageHandler messageHandlerInstance = null;

	/**
	 * Gets the message handler instance.
	 * 
	 * @return the message handler instance
	 */
	public static synchronized MessageHandler getMessageHandlerInstance() {

		if (messageHandlerInstance == null) {

			messageHandlerInstance = new MessageHandler();
		}

		return messageHandlerInstance;
	}

	private DataFile dataFile = null;
	private String dataFormat = null;
	private SimpleDateFormat format = null;
	private final String MAIN_FILE_PATH = System.getProperty("user.home")
			+ File.separator + "Documents" + File.separator
			+ "AirPlane Control";
	private String planeType = null;

	private Tower tower = null;

	// A constructor for adding messages to XMLFile
	/** The xml file. */
	private final XMLFileCreator xmlFile = XMLFileCreator
			.getXMLFileCreatorInstance();

	// MessageHandler Singleton
	/**
	 * Instantiates a new message handler.
	 */
	private MessageHandler() {

		tower = Tower.getTowerInstance();

		format = new SimpleDateFormat("HH mm ss");
	}

	/**
	 * Add Bye to XML, stop planeRouting since landed, also the MessageSender
	 * and update the planes to land
	 * 
	 */
	@Override
	public void visit(Bye bye, String sourceName, String destinationName,
			Plane plane) {

		// Check XMLFile existence and add Message to the File
		xmlFile.checkFilePresenceAndWriteMessageToFile("BYE",
				bye.generateArrayStringXML(sourceName, destinationName), "4");

		tower.removePlaneFromAllLists(plane);
		tower.globalTowerRouting();
		plane.stopAll();
	}

	/**
	 * Chokes all planes in case of MayDay, added to XML
	 * 
	 */
	@Override
	public void visit(Choke choke, String sourceName, String destinationName,
			Plane plane) {

		plane.sendMessage(choke);

		xmlFile.checkFilePresenceAndWriteMessageToFile("CHOKE", sourceName,
				destinationName, "1");

	}

	/**
	 * Added to XML, check the file existance and completion for each plane. If
	 * type is .png, .jpg or .bmp - post to Twitter Else if text - read it
	 */
	@Override
	public void visit(Data data, String sourceName, String destinationName,
			Plane plane, MessageReader messageReader, boolean firstTxtFile) {

		dataFormat = new String(data.getFormat()).trim();

		System.out.println("Message Data from " + sourceName + " to "
				+ destinationName);

		dataFile = tower.checkForDataFile(data.getHashAndPlaneIDName());

		if (dataFile == null) {

			String tempString = MAIN_FILE_PATH + File.separator
					+ new String(data.getPlaneID()) + File.separator
					+ new String(data.getPlaneID()) + " "
					+ format.format(new Date());
			dataFile = new DataFile(tempString, data);
			tower.addDataFile(data.getHashAndPlaneIDName(), dataFile);
		} else {

			dataFile.writeData(data);
		}

		if (dataFile.isComplete() && dataFile.testFileTypeToTweet()) {

		} else if (dataFile.isComplete()
				&& (dataFormat.equalsIgnoreCase("txt")) && firstTxtFile == true) {

			planeType = dataFile.readPlaneName();
			System.out.println("Plane name " + planeType);
			plane.setPlaneType(planeType);
			messageReader.setFirstTxtFile(false);
		}

		xmlFile.checkFilePresenceAndWriteMessageToFile("DATA",
				data.generateArrayStringXML(sourceName, destinationName), "4");

	}

	/**
	 * Receive a message from MessageReader or Message Sender, and store it in
	 * XML and if Tower is source, send it.
	 * 
	 * @param hello
	 *            the hello
	 * @param sourceName
	 *            the source name
	 * @param destinationName
	 *            the destination name
	 * @param stringArray
	 *            the string array
	 */

	@Override
	public void visit(Hello hello, String sourceName, String destinationName,
			String[][] stringArray) {

		xmlFile.checkFilePresenceAndWriteMessageToFile("HELLO", stringArray,
				"1");

	}

	@Override
	public void visit(InternalQuitMessage internalQuitMessage,
			String sourceName, String destinationName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(InternalUpdateMessage internalUpdateMessage,
			String sourceName, String destinationName) {
		// TODO Auto-generated method stub

	}

	/**
	 * Received from the Plane, added to XML
	 */

	@Override
	public void visit(KeepAlive keepAlive, String sourceName,
			String destinationName) {

		xmlFile.checkFilePresenceAndWriteMessageToFile("KEEPALIVE",
				keepAlive.generateArrayStringXML(sourceName, destinationName),
				"3");

	}

	/**
	 * 
	 * Received LandingRequest, added to XML, added to Tower for order
	 */

	@Override
	public void visit(LandingRequest landingRequest, String sourceName,
			String destinationName) {

		xmlFile.checkFilePresenceAndWriteMessageToFile("LANDINGREQUEST",
				landingRequest.generateArrayStringXML(sourceName,
						destinationName), "2");

	}

	/**
	 * Redirect the Plane in MayDay to landing if the landing is empty land the
	 * plane, added to XML
	 * 
	 */

	@Override
	public void visit(MayDay mayDay, String sourceName, String destinationName,
			Plane plane) {

		tower.chokeAllPlanes(plane);

		xmlFile.checkFilePresenceAndWriteMessageToFile("MAYDAY",
				mayDay.generateArrayStringXML(sourceName, destinationName), "0");

	}

	/**
	 * Send message to specific plane, add to XML
	 */

	@Override
	public void visit(RoutingMessage routingMessage, String sourceName,
			String destinationName, Plane plane) {

		plane.sendMessage(routingMessage);

		xmlFile.checkFilePresenceAndWriteMessageToFile("ROUTINGMESSAGE",
				routingMessage.generateArrayStringXML(sourceName,
						destinationName), "2");

	}

	/**
	 * 
	 * Add to XML, sent only from the Plane
	 */

	@Override
	public void visit(SendRSAKey sendRSAkey, String sourceName,
			String destinationName) {

		xmlFile.checkFilePresenceAndWriteMessageToFile("SENDRSAKEY",
				sendRSAkey.generateArrayStringXML(sourceName, destinationName),
				"2");

	}

	/**
	 * 
	 * If the MayDay situation has passed, UnChoke All Planes
	 */

	@Override
	public void visit(UnChoke unChoke, String sourceName,
			String destinationName, Plane plane) {

		plane.sendMessage(unChoke);

		xmlFile.checkFilePresenceAndWriteMessageToFile("UNCHOKE",
				unChoke.generateArrayStringXML(sourceName, destinationName),
				"4");

	}

}
