/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package message;

import java.io.DataOutputStream;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * The Class Message.
 */
public abstract class Message {

	// Length of Message
	/** The length. */
	protected int length;
	protected byte[] lengthArray;

	/** The plane id. */
	protected byte[] planeID;

	/** The posx. */
	protected int posx;

	/** The posy. */
	protected int posy;

	/** The priority. */
	protected int priority;

	// Enum type of message
	/** The type. */
	protected MessageType type;

	/**
	 * Instantiates a new message.
	 * 
	 * @param planeID
	 *            the plane id
	 */
	public Message(byte[] planeID) {
		super();
		this.planeID = planeID;
	}

	/**
	 * Instantiates a new message.
	 * 
	 * @param planeID
	 *            the plane id
	 * @param length
	 *            the length
	 * @param posx
	 *            the posx
	 * @param posy
	 *            the posy
	 */
	public Message(byte[] planeID, int length, int posx, int posy) {
		super();
		this.planeID = planeID;
		this.length = length;
		this.posx = posx;
		this.posy = posy;

	}

	/**
	 * Instantiates a new message.
	 * 
	 * @param planeID
	 *            the plane id
	 * @param length
	 *            the length
	 * @param priority
	 *            the priority
	 * @param posx
	 *            the posx
	 * @param posy
	 *            the posy
	 */
	public Message(byte[] planeID, int length, int priority, int posx, int posy) {
		super();
		this.planeID = planeID;
		this.length = length;
		this.priority = priority;
		this.posx = posx;
		this.posy = posy;

	}

	public abstract void accept(MessageHandlerVisitor messageVisitior);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {

		return true;
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

	/**
	 * Gets the priority.
	 * 
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Send message.
	 * 
	 * @param byteArrayOutputStream
	 *            the byte array output stream
	 */

	public void sendMessage(DataOutputStream dataOutputStream) {

		try {

			// First 8 bytes planeID
			dataOutputStream.write(planeID);
			// Next 4 bytes for length of Message
			dataOutputStream.writeInt(length);
			// Next 4 bytes for priority
			dataOutputStream.writeInt(priority);
			// Next 4 bytes for posX of Plane
			dataOutputStream.writeInt(posx);
			// Next 4 bytes for posY of Plane
			dataOutputStream.writeInt(posy);
			// Next 4 bytes for Type Of Message
			dataOutputStream.writeInt(type.ordinal());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
