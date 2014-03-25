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
import twitter.TweetATweet;

import file.DataFile;
import file.XMLFileCreator;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageHandler.
 */
public class MessageHandler implements MessageHandlerVisitor {

	// Implementing VisitorPattern to be used for MessageWriter

	// A constructor for adding messages to XMLFile
	/** The xml file. */
	private XMLFileCreator xmlFile = XMLFileCreator.getXMLFileCreatorInstance();

	/** The message handler instance. */
	private static MessageHandler messageHandlerInstance = null;

	private Tower tower = null;
	private TweetATweet tweet = null;
	private DataFile dataFile = null;
	private String dataFormat = null;
	private String planeType = null;
	
	private SimpleDateFormat format = null;
	
	private final String MAIN_FILE_PATH = System.getProperty("user.home")
			+ File.separator + "Documents" + File.separator
			+ "AirPlane Control";

	// MessageHandler Singleton
	/**
	 * Instantiates a new message handler.
	 */
	private MessageHandler() {

		tower = Tower.getTowerInstance();
		tweet = TweetATweet.getTweetATweetInstance();
		format = new SimpleDateFormat("HH mm ss");
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.MessageHandlerVisitor#visit(messages.Bye, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void visit(Bye bye, String sourceName, String destinationName, Plane plane) {

		// Check XMLFile existence and add Message to the File
		xmlFile.checkFilePresenceAndWriteMessageToFile("BYE",
				bye.generateArrayStringXML(sourceName, destinationName), "4");
		tweet.postTweet(sourceName);
		
		tower.removePlaneFromAllLists(plane);
		tower.globalTowerRouting();
		plane.stopAll();		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.MessageHandlerVisitor#visit(messages.Choke,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void visit(Choke choke, String sourceName, String destinationName,
			Plane plane) {

		plane.sendMessage(choke);
		plane.setStopFlag(true);

		xmlFile.checkFilePresenceAndWriteMessageToFile("CHOKE", sourceName,
				destinationName, "1");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.MessageHandlerVisitor#visit(messages.Data,
	 * java.lang.String, java.lang.String)
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
					+ new String(data.getPlaneID())+ " " + format.format(new Date());
			dataFile = new DataFile(tempString, data);
			tower.addDataFile(data.getHashAndPlaneIDName(), dataFile);
		} else {

			dataFile.writeData(data);
		}

		if (dataFile.isComplete() && dataFile.testFileTypeToTweet()) {

			tweet.postPicture(dataFile.getAbsolutePath(),
					new String(data.getPlaneID()), dataFormat);
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

		System.out.println("Message Hello To Be Added");

		xmlFile.checkFilePresenceAndWriteMessageToFile("HELLO", stringArray,
				"1");
	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.MessageHandlerVisitor#visit(messages.KeepAlive,
	 * java.lang.String, java.lang.String, java.lang.String[][])
	 */
	@Override
	public void visit(KeepAlive keepAlive, String sourceName,
			String destinationName) {

		xmlFile.checkFilePresenceAndWriteMessageToFile("KEEPALIVE",
				keepAlive.generateArrayStringXML(sourceName, destinationName),
				"3");

		// tower.routingMessages();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.MessageHandlerVisitor#visit(messages.LandingRequest,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void visit(LandingRequest landingRequest, String sourceName,
			String destinationName) {

		System.out.println("Landing Request Received");

		xmlFile.checkFilePresenceAndWriteMessageToFile("LANDINGREQUEST",
				landingRequest.generateArrayStringXML(sourceName,
						destinationName), "2");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.MessageHandlerVisitor#visit(messages.MayDay,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void visit(MayDay mayDay, String sourceName, String destinationName) {

		xmlFile.checkFilePresenceAndWriteMessageToFile("MAYDAY", sourceName,
				destinationName, "0");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.MessageHandlerVisitor#visit(messages.RoutingMessage,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void visit(RoutingMessage routingMessage, String sourceName,
			String destinationName, Plane plane) {

		plane.sendMessage(routingMessage);

		xmlFile.checkFilePresenceAndWriteMessageToFile("ROUTINGMESSAGE",
				routingMessage.generateArrayStringXML(sourceName,
						destinationName), "2");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.MessageHandlerVisitor#visit(messages.SendRSAKey,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void visit(SendRSAKey sendRSAkey, String sourceName,
			String destinationName) {

		xmlFile.checkFilePresenceAndWriteMessageToFile("SENDRSAKEY",
				sendRSAkey.generateArrayStringXML(sourceName, destinationName),
				"2");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.MessageHandlerVisitor#visit(messages.UnChoke,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void visit(UnChoke unChoke, String sourceName, String destinationName) {

		xmlFile.checkFilePresenceAndWriteMessageToFile("UNCHOKE", sourceName,
				destinationName, "4");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.MessageHandlerVisitor#visit(messages.InternalQuitMessage,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void visit(InternalQuitMessage internalQuitMessage,
			String sourceName, String destinationName) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.MessageHandlerVisitor#visit(messages.InternalUpdateMessage,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void visit(InternalUpdateMessage internalUpdateMessage,
			String sourceName, String destinationName) {
		// TODO Auto-generated method stub

	}

}
