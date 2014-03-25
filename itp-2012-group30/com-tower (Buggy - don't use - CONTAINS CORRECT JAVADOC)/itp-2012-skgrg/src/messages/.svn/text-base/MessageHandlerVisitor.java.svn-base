/*
 * Sergei Kostevitch
 * Mar 10, 2012
 */

package messages;

import plane.Plane;

// TODO: Auto-generated Javadoc
/**
 * The Interface MessageHandlerVisitor.
 */
public interface MessageHandlerVisitor {

	//Visitor pattern interface
	//DestinationName and SourceName of Messages
	
	/**
	 * Visit.
	 *
	 * @param bye the bye
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(Bye bye, String sourceName, String destinationName, Plane plane);
	
	/**
	 * Visit.
	 *
	 * @param choke the choke
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(Choke choke, String sourceName, String destinationName, Plane plane);
	
	/**
	 * Visit.
	 *
	 * @param data the data
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(Data data, String sourceName, String destinationName, Plane plane, MessageReader messageReader, boolean firstTxtFile);
	
	/**
	 * Visit.
	 *
	 * @param hello the hello
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 * @param stringArray the string array
	 */
	public void visit(Hello hello, String sourceName, String destinationName, String[][] stringArray);
	
	/**
	 * Visit.
	 *
	 * @param keepAlive the keep alive
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 * @param stringArray the string array
	 */
	public void visit(KeepAlive keepAlive, String sourceName, String destinationName);
	
	/**
	 * Visit.
	 *
	 * @param landingRequest the landing request
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(LandingRequest landingRequest, String sourceName, String destinationName);
	
	/**
	 * Visit.
	 *
	 * @param mayDay the may day
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(MayDay mayDay, String sourceName, String destinationName);
	
	/**
	 * Visit.
	 *
	 * @param routingMessage the routing message
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(RoutingMessage routingMessage, String sourceName, String destinationName, Plane plane);
	
	/**
	 * Visit.
	 *
	 * @param sendRSAkey the send rs akey
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(SendRSAKey sendRSAkey, String sourceName, String destinationName);
	
	/**
	 * Visit.
	 *
	 * @param unChoke the un choke
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(UnChoke unChoke, String sourceName, String destinationName);
	
	/**
	 * Visit.
	 *
	 * @param internalQuitMessage the internal quit message
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(InternalQuitMessage internalQuitMessage, String sourceName, String destinationName);
	
	/**
	 * Visit.
	 *
	 * @param internalUpdateMessage the internal update message
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(InternalUpdateMessage internalUpdateMessage, String sourceName, String destinationName);
}
