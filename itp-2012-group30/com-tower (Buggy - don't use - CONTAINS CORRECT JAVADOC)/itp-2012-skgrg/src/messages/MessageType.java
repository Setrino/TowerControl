/*
 * Sergei Kostevitch
 * Gabriel Gomes
 * Feb 28, 2012
 */

package messages;

/**
 * MessageType is a Class that contains a list (enum) of all possible messages.
 */
public enum MessageType {

	/** The HELLO. */
	HELLO,
	
	/** The DATA. */
	DATA,
	
	/** The MAYDAY. */
	MAYDAY,
	
	/** The SENDRSA. */
	SENDRSA,
	
	/** The CHOKE. */
	CHOKE,
	
	/** The UNCHOKE. */
	UNCHOKE,
	
	/** The BYE. */
	BYE,
	
	/** The ROUTING. */
	ROUTING,
	
	/** The KEEPALIVE. */
	KEEPALIVE,
	
	/** The LANDINGREQUEST. */
	LANDINGREQUEST
}
