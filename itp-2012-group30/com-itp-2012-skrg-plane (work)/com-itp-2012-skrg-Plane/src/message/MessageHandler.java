/*
 * Sergei Kostevitch
 * Mar 10, 2012
 */

package message;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import file.DataFile;

import Plane.Plane;
import Plane.PlaneRouting;

import message.type.Bye;
import message.type.Choke;
import message.type.Data;
import message.type.Hello;
import message.type.InternalQuitMessage;
import message.type.InternalUpdateMessage;
import message.type.KeepAlive;
import message.type.LandingRequest;
import message.type.MayDay;
import message.type.RoutingMessage;
import message.type.RoutingMessageType;
import message.type.SendRSAKey;
import message.type.UnChoke;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageHandler.
 */
public class MessageHandler implements MessageHandlerVisitor {

	// Implementing VisitorPattern to be used for MessageWriter

	/** The message handler instance. */
	private static MessageHandler messageHandlerInstance = null;

	private DataFile dataFile = null;
	
	private SimpleDateFormat format = null;
	
	private final String MAIN_FILE_PATH = System.getProperty("user.home")
			+ File.separator + "Documents" + File.separator
			+ "Plane";

	// MessageHandler Singleton
	/**
	 * Instantiates a new message handler.
	 */
	private MessageHandler() {

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

	

	@Override
	public void visit(Bye bye, Plane plane) {
		
		plane.sendMessage(bye);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.MessageHandlerVisitor#visit(messages.Choke,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void visit(Choke choke, Plane plane) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.MessageHandlerVisitor#visit(messages.Data,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void visit(Data data, Plane plane, MessageReader messageReader) {

		dataFile = plane.checkForDataFile(data.getHashAndPlaneIDName());

		if (dataFile == null) {

			String tempString = MAIN_FILE_PATH + File.separator
					+ new String(data.getPlaneID()) + File.separator
					+ new String(data.getPlaneID())+ " " + format.format(new Date());
			dataFile = new DataFile(tempString, data);
			plane.addDataFile(data.getHashAndPlaneIDName(), dataFile);
		} else {

			dataFile.writeData(data);
		}
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
	public void visit(Hello hello) {

		System.out.println("Message Hello To Be Added");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.MessageHandlerVisitor#visit(messages.KeepAlive,
	 * java.lang.String, java.lang.String, java.lang.String[][])
	 */
	@Override
	public void visit(KeepAlive keepAlive, Plane plane) {

		plane.sendMessage(keepAlive);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.MessageHandlerVisitor#visit(messages.LandingRequest,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void visit(LandingRequest landingRequest, Plane plane) {

		System.out.println("Landing Request Received");


	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.MessageHandlerVisitor#visit(messages.MayDay,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void visit(MayDay mayDay, Plane plane) {

		plane.sendMessage(mayDay);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.MessageHandlerVisitor#visit(messages.RoutingMessage,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void visit(RoutingMessage routingMessage, PlaneRouting planerouting, RoutingMessageType routingType) {
		
		planerouting.addRoutingMessage(routingMessage, routingType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.MessageHandlerVisitor#visit(messages.SendRSAKey,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void visit(SendRSAKey sendRSAkey, Plane plane) {


	}

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

	@Override
	public void visit(UnChoke unChoke, Plane plane) {
		// TODO Auto-generated method stub
		
	}

}
