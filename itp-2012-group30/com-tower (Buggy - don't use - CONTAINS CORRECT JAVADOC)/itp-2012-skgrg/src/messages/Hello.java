/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * The Class Hello extends the Class Message.
 * Hello is a message used to establish the connection
 * between the tower and the plane. (Handshake)
 * If the tower answers Hello to a plane, it means that
 * the communication is started.
 */
public class Hello extends Message {

	/** The reserved. */
	private byte reserved;
	
	/** The length. */
	private static int length;
	
	/** The Constant LENGTH. */
	private static final byte[] LENGTH = { (byte) 0, (byte) 0, (byte) 0,
			(byte) 0 };
	
	/** The Constant PRIORITY. */
	private static final int PRIORITY = 1;

	/**
	 * Instantiates a new hello.
	 *
	 * @param planeID the plane id
	 * @param posx the posx
	 * @param posy the posy
	 * @param reserved the reserved
	 */
	public Hello(byte[] planeID, int posx, int posy, byte reserved) {
		super(planeID, length, posx, posy);
		length = 0;
		priority = PRIORITY;
		type = MessageType.HELLO;
		this.reserved = reserved;
	
	}


	/**
	 * Redefine the method sendMessage from the Class Message.
	 * Used to send a Hello Message.
	 */
	@Override
	public void sendMessage(ByteArrayOutputStream byteArrayOutputStream) {

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

			byteArrayOutputStream.write(messageHello);

		} catch (IOException e) {

			System.err.println("Message Hello failed to send");
			e.printStackTrace();
		}
	}

	/**
	 * Redefine the method accept from the Class Message.
	 * This method allows an object MessageHanadlerVisitor to visit
	 * a Hello message.
	 * 
	 * @ param visitor an object MessageHandlerVisitor
	 */
	@Override
	public void accept(MessageHandlerVisitor visitor) {

		visitor.visit(this, null, null, null);
	}

	/**
	 * Gets the plane identity
	 * 
	 * @return the plane identity
	 */
	public byte[] getPlaneID() {
		return planeID;
	}

	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Gets the posx.
	 *
	 * @return the posx
	 */
	public int getPosx() {
		return posx;
	}

	/**
	 * Gets the posy.
	 *
	 * @return the posy
	 */
	public int getPosy() {
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

	@Override
	public String[][] generateArrayStringXML(String source, String destination) {
		
		stringArray = new String[5][2];
		stringArray[0][1] = source;
		stringArray[1][1] = destination;
		stringArray[2][0] = "PosX";
		stringArray[2][1] = String.valueOf(posx);
		stringArray[3][0] = "PosY";
		stringArray[3][1] = String.valueOf(posy);
		stringArray[4][0] = "Reserved";
		stringArray[4][1] = String.valueOf(reserved);
		
		return stringArray;
	}

}
