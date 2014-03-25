/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package messages;

/**
 * The Enum RoutingMessageType.
 * It contains all the possible form of messages that RoutingMessage
 * can take.
 */
public enum RoutingMessageType {

	/** The NEWFIRST used after MayDay has occurred. */
	NEWFIRST,
	
	/** The LAST is sent at all times except for in case of Mayday. */
	LAST,
	
	/** The REPLACECALL sent the first time the first message sent. */
	REPLACEALL
}
