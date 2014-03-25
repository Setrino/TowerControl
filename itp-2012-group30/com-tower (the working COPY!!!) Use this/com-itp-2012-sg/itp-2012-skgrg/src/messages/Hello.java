/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

// TODO: Auto-generated Javadoc
/**
 * The Class Hello.
 */
public class Hello extends Message {

	/** The reserved. */
	private byte reserved;
	
	/** The length. */
	private static int length;
	
	/** The Constant PRIORITY. */
	private static final int PRIORITY = 1;
	
	private ByteBuffer byteBuffer = null;

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


	/* (non-Javadoc)
	 * @see messages.Message#sendMessage(java.io.ByteArrayOutputStream)
	 */
	@Override
	public void sendMessage(ByteArrayOutputStream byteArrayOutputStream) {
		
		byteBuffer = ByteBuffer.allocate(29);
		byteBuffer.put(planeID, 0, 8);
		byteBuffer.putInt(8, length);
		byteBuffer.putInt(12, PRIORITY);
		byteBuffer.putInt(16, posx);
		byteBuffer.putInt(20, posy);
		byteBuffer.putInt(24, type.ordinal());
		byteBuffer.put(28, reserved);
		
		try {

			byteArrayOutputStream.write(byteBuffer.array());

		} catch (IOException e) {

			System.err.println("Message Hello failed to send");
			e.printStackTrace();
		}
	}
	
	@Override
	public void accept(MessageHandlerVisitor visitor) {

		visitor.visit(this, null, null, null);
	}

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
