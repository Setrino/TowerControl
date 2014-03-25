/*
 * Sergei Kostevitch
 * Mar 20, 2012
 */

package exceptions;

import message.type.Data;


// TODO: Auto-generated Javadoc
/**
 * The Class IllegalPacketSizeException.
 */
public class IllegalPacketSizeException extends IllegalArgumentException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5886869091071416551L;

	
	/**
	 * Instantiates a new illegal packet size exception.
	 *
	 * @param data the data
	 */
	public IllegalPacketSizeException(Data data){
		super();	
		System.err.println("Wrong packet length. Meant to be: " + Data.MAX_PACKET_SIZE + " but is: " + data.getPayload().length);
		
	}
}
