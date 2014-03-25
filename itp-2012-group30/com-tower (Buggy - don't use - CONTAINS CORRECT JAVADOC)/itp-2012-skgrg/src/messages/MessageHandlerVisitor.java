/*
 * Sergei Kostevitch
 * Gabriel Rodrigues Gomes
 * Mar 10, 2012
 */

package messages;

import plane.Plane;

/**
 * The Interface MessageHandlerVisitor.
 */
public interface MessageHandlerVisitor {

	//Visitor pattern interface
	//DestinationName and SourceName of Messages
	
	/**
	 * This method is going to implement a specific algorithm used
	 * when a message Bye is received.
	 *
	 * @param bye the bye
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(Bye bye, String sourceName, String destinationName, Plane plane);
	
	/**
	 * This method is going to implement a specific algorithm used
	 * when a message Choke is received.
	 *
	 * @param choke the choke
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(Choke choke, String sourceName, String destinationName, Plane plane);
	
	/**
	 * This method is going to implement a specific algorithm used
	 * when a message Data is received.
	 *
	 * @param data the data
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(Data data, String sourceName, String destinationName, Plane plane, MessageReader messageReader, boolean firstTxtFile);
	
	/**
	 * This method is going to implement a specific algorithm used
	 * when a message Hello is received.
	 *
	 * @param hello the hello
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 * @param stringArray the string array
	 */
	public void visit(Hello hello, String sourceName, String destinationName, String[][] stringArray);
	
	/**
	 * This method is going to implement a specific algorithm used
	 * when a message KeepAlive is received.
	 *
	 * @param keepAlive the keep alive
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 * @param stringArray the string array
	 */
	public void visit(KeepAlive keepAlive, String sourceName, String destinationName);
	
	/**
	 * This method is going to implement a specific algorithm used
	 * when a message LandingRequest is received.
	 *
	 * @param landingRequest the landing request
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(LandingRequest landingRequest, String sourceName, String destinationName);
	
	/**
	 * This method is going to implement a specific algorithm used
	 * when a message MayDay is received.
	 *
	 * @param mayDay the may day
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(MayDay mayDay, String sourceName, String destinationName, Plane plane);
	
	/**
	 * This method is going to implement a specific algorithm used
	 * when a message RoutingMessage is received.
	 *
	 * @param routingMessage the routing message
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(RoutingMessage routingMessage, String sourceName, String destinationName, Plane plane);
	
	/**
	 * This method is going to implement a specific algorithm used
	 * when a message SendRSAKey is received.
	 *
	 * @param sendRSAkey the send rs akey
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(SendRSAKey sendRSAkey, String sourceName, String destinationName);
	
	/**
	 * This method is going to implement a specific algorithm used
	 * when a message UnChoke is received.
	 *
	 * @param unChoke the UnChoke
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(UnChoke unChoke, String sourceName, String destinationName, Plane plane);
	
	/**
	 * This method is going to implement a specific algorithm used
	 * when a message InternalQuitMessage is received.
	 * DEPRECATED
	 * 
	 * @param internalQuitMessage the internal quit message
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(InternalQuitMessage internalQuitMessage, String sourceName, String destinationName);
	
	/**
	 * This method is going to implement a specific algorithm used
	 * when a message InternalUpdateMessage is received.
	 * DEPRECATED
	 *
	 * @param internalUpdateMessage the internal update message
	 * @param sourceName the source name
	 * @param destinationName the destination name
	 */
	public void visit(InternalUpdateMessage internalUpdateMessage, String sourceName, String destinationName);
}
