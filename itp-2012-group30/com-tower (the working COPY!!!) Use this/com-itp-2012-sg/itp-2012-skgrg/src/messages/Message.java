/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package messages;

import java.io.ByteArrayOutputStream;

// TODO: Auto-generated Javadoc
/**
 * The Class Message.
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
	
	/*public void sendMessage(ByteArrayOutputStream byteArrayOutputStream){
		
		byte[] messageHello = new byte[29];

		// First 8 bytes planeID
		for (int i = 0; i < planeID.length; i++) {
			messageHello[i] = planeID[i];
		}
		// Next 4 bytes for length of Message
		for (int i = 0; i < lengthArray.length; i++) {

			messageHello[i + 8] = lengthArray[i];
		}
		// Next 4 bytes for priority
		messageHello[12] = (byte) (priority >>> 24);
		messageHello[13] = (byte) (priority >> 16 & 0xFF);
		messageHello[14] = (byte) (priority >> 8 & 0xFF);
		messageHello[15] = (byte) (priority & 0xFF);
		// Next 4 bytes for posX of Plane
		messageHello[16] = (byte) (posx >>> 24);
		messageHello[17] = (byte) (posx >> 16 & 0xFF);
		messageHello[18] = (byte) (posx >> 8 & 0xFF);
		messageHello[19] = (byte) (posx & 0xFF);
		// Next 4 bytes for posY of Plane
		messageHello[20] = (byte) (posy >>> 24);
		messageHello[21] = (byte) (posy >> 16 & 0xFF);
		messageHello[22] = (byte) (posy >> 8 & 0xFF);
		messageHello[23] = (byte) (posy & 0xFF);
		// Next 4 bytes for Type Of Message
		messageHello[24] = (byte) (type.ordinal() >>> 24);
		messageHello[25] = (byte) (type.ordinal() >> 16 & 0xFF);
		messageHello[26] = (byte) (type.ordinal() >> 8 & 0xFF);
		messageHello[27] = (byte) (type.ordinal() & 0xFF);
	}*/
	
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
		// TODO Auto-generated method stub
		return planeID;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
