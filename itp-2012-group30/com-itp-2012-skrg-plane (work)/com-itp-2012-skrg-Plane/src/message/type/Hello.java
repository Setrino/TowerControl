/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package message.type;

import java.io.DataOutputStream;
import java.io.IOException;

import message.Message;
import message.MessageHandlerVisitor;
import message.MessageType;


// TODO: Auto-generated Javadoc
/**
 * The Class Hello.
 */
public class Hello extends Message {

	/** The length. */
	private static int length;

	/** The Constant PRIORITY. */
	private static final int PRIORITY = 1;
	
	/** The Constant LENGTH. */
	private static final byte[] LENGTH = { (byte) 0, (byte) 0, (byte) 0,
			(byte) 0 };

	/** The reserved. */
	private final byte reserved;

	/**
	 * Instantiates a new hello.
	 * 
	 * @param planeID
	 *            the plane id
	 * @param posx
	 *            the posx
	 * @param posy
	 *            the posy
	 * @param reserved
	 *            the reserved
	 */
	public Hello(byte[] planeID, int posx, int posy, byte reserved) {
		super(planeID, length, posx, posy);
		length = 0;
		priority = PRIORITY;
		type = MessageType.HELLO;
		this.reserved = reserved;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.Message#accept(messages.MessageHandlerVisitor)
	 */
	@Override
	public void accept(MessageHandlerVisitor visitor) {

		visitor.visit(this);
	}

	/**
	 * Gets the length.
	 * 
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.Message#getPlaneID()
	 */
	@Override
	public byte[] getPlaneID() {
		return planeID;
	}

	/**
	 * Gets the posx.
	 * 
	 * @return the posx
	 */
	public int getPosX() {
		return posx;
	}

	/**
	 * Gets the posy.
	 * 
	 * @return the posy
	 */
	public int getPosY() {
		return posy;
	}

	/**
	 * Gets the reserved.
	 * 
	 * @return the reserved
	 */
	public byte getReserved() {
		return reserved;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.Message#sendMessage(java.io.ByteArrayOutputStream)
	 */
	@Override
	public void sendMessage(DataOutputStream dataOutputStream) {
		
		byte[] messageHello = new byte[29];

		// First 8 bytes planeID
		for (int i = 0; i < planeID.length; i++) {
			messageHello[i] = planeID[i];
		}
		// Next 4 bytes for length of Message
		for (int i = 0; i < LENGTH.length; i++) {

			messageHello[i + 8] = LENGTH[i];
		}
		// Next 4 bytes for priority
		messageHello[12] = (byte) (PRIORITY >>> 24);
		messageHello[13] = (byte) (PRIORITY >> 16 & 0xFF);
		messageHello[14] = (byte) (PRIORITY >> 8 & 0xFF);
		messageHello[15] = (byte) (PRIORITY & 0xFF);
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
		//Next 1 byte for reserved - Encrypted coding or not
		messageHello[28] = reserved;
		
		try {

			// Next 1 byte for reserved - Encrypted coding or not
			dataOutputStream.write(messageHello);

		} catch (IOException e) {

			System.err.println("Message Hello failed to send");
			e.printStackTrace();
		}
	}

}
