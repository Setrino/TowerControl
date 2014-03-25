/*
 * Sergei Kostevitch
 * Mar 10, 2012
 */

package message;

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
import Plane.Plane;
import Plane.PlaneRouting;

// TODO: Auto-generated Javadoc
/**
 * The Interface MessageHandlerVisitor.
 */
public interface MessageHandlerVisitor {

	// Visitor pattern interface
	// DestinationName and SourceName of Messages

	/**
	 * Visit.
	 * 
	 * @param bye
	 *            the bye
	 * @param sourceName
	 *            the source name
	 * @param destinationName
	 *            the destination name
	 */
	public void visit(Bye bye, Plane plane);

	/**
	 * Visit.
	 * 
	 * @param choke
	 *            the choke
	 * @param sourceName
	 *            the source name
	 * @param destinationName
	 *            the destination name
	 */
	public void visit(Choke choke, Plane plane);

	/**
	 * Visit.
	 * 
	 * @param data
	 *            the data
	 * @param sourceName
	 *            the source name
	 * @param destinationName
	 *            the destination name
	 */
	public void visit(Data data, Plane plane, MessageReader messageReader);

	/**
	 * Visit.
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
	public void visit(Hello hello);

	/**
	 * Visit.
	 * 
	 * @param internalQuitMessage
	 *            the internal quit message
	 * @param sourceName
	 *            the source name
	 * @param destinationName
	 *            the destination name
	 */
	public void visit(InternalQuitMessage internalQuitMessage,
			String sourceName, String destinationName);

	/**
	 * Visit.
	 * 
	 * @param internalUpdateMessage
	 *            the internal update message
	 * @param sourceName
	 *            the source name
	 * @param destinationName
	 *            the destination name
	 */
	public void visit(InternalUpdateMessage internalUpdateMessage,
			String sourceName, String destinationName);

	/**
	 * Visit.
	 * 
	 * @param keepAlive
	 *            the keep alive
	 * @param sourceName
	 *            the source name
	 * @param destinationName
	 *            the destination name
	 * @param stringArray
	 *            the string array
	 */
	public void visit(KeepAlive keepAlive, Plane plane);

	/**
	 * Visit.
	 * 
	 * @param landingRequest
	 *            the landing request
	 * @param sourceName
	 *            the source name
	 * @param destinationName
	 *            the destination name
	 */
	public void visit(LandingRequest landingRequest, Plane plane);

	/**
	 * Visit.
	 * 
	 * @param mayDay
	 *            the may day
	 * @param sourceName
	 *            the source name
	 * @param destinationName
	 *            the destination name
	 */
	public void visit(MayDay mayDay, Plane plane);

	/**
	 * Visit.
	 * 
	 * @param routingMessage
	 *            the routing message
	 * @param sourceName
	 *            the source name
	 * @param destinationName
	 *            the destination name
	 */
	public void visit(RoutingMessage routingMessage, PlaneRouting planeRouting, RoutingMessageType routingType);

	/**
	 * Visit.
	 * 
	 * @param sendRSAkey
	 *            the send rs akey
	 * @param sourceName
	 *            the source name
	 * @param destinationName
	 *            the destination name
	 */
	public void visit(SendRSAKey sendRSAkey, Plane plane);

	/**
	 * Visit.
	 * 
	 * @param unChoke
	 *            the un choke
	 * @param sourceName
	 *            the source name
	 * @param destinationName
	 *            the destination name
	 */
	public void visit(UnChoke unChoke, Plane plane);
}
