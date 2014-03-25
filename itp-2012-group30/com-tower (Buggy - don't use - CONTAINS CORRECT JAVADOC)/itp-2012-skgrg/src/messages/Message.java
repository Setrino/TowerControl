/*
 * Sergei Kostevitch
 * Gabriel Gomes
 * Feb 28, 2012
 */

package messages;

import java.io.ByteArrayOutputStream;

/**
 * The abstract Class Message.
 * Contains the common properties of all message types
 * and contains some of the principal methods of all message types,
 * that will be redefined (those declared in abstract).
 */

public abstract class Message {
	
	/** The plane id. */
	protected byte[] planeID;
	// Length of Message
	/** The length. */
	protected int length;
	
	/** The priority. */
	protected int priority;
	
	/** The posx. */
	protected int posx;
	
	/** The posy. */
	protected int posy;
	// Enum type of message
	/** The type. */
	protected MessageType type;
	
	protected String[][] stringArray;
	
	protected byte[] lengthArray; 

	
	/**
	 * Instantiates a new message.
	 *
	 * @param planeID the plane id
	 * @param length the length
	 * @param priority the priority
	 * @param posx the posx
	 * @param posy the posy
	 */
	public Message(byte[] planeID, int length, int priority, int posx, int posy){
		super();
		this.planeID = planeID;
		this.length = length;
		this.priority = priority;
		this.posx = posx;
		this.posy = posy;
		
	}	
	
	/**
	 * Instantiates a new message.
	 *
	 * @param planeID the plane id
	 * @param length the length
	 * @param posx the posx
	 * @param posy the posy
	 */
	public Message(byte[] planeID, int length, int posx, int posy){
		super();
		this.planeID = planeID;
		this.length = length;
		this.posx = posx;
		this.posy = posy;
	
	}
	
	/**
	 * Instantiates a new message.
	 *
	 * @param planeID the plane id
	 */
	public Message(byte[] planeID){
		super();
		this.planeID = planeID;
	}
	
	/**
	 * Send message.
	 *
	 * @param byteArrayOutputStream the byte array output stream
	 */
	
	public abstract void sendMessage(ByteArrayOutputStream byteArrayOutputStream);
	/**
	 * Accept.
	 *
	 * @param visitor the visitor
	 */
	public abstract void accept(MessageHandlerVisitor visitor);
	
	/**
	 * Generate a String array for use in XML 
	 * 
	 * 
	 */
	
	public abstract String[][] generateArrayStringXML(String source, String destination);
	
	/**
	 * Gets the priority.
	 *
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Gets the plane id.
	 *
	 * @return the plane id
	 */
	public byte[] getPlaneID() {
		
		return planeID;
	}
	
	@Override
	public boolean equals(Object object){
		
		boolean check = false;

		if (this.getClass() == object.getClass()) {
			check = true;
		}else{
			check = false;
		}
		
		return check;
	}

}
