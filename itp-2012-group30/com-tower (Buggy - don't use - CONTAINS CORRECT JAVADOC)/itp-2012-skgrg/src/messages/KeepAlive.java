/*
 * Sergei Kostevitch
 * Gabriel Rodrigues Gomes
 * Feb 28, 2012
 */

package messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * The Class KeepAlive extends the Class Message.
 * It's a message that is send from a plane to the tower to
 * communicate the plane's position.
 */
public class KeepAlive extends Message {

	/** The Constant LENGTH. */
	private static final byte[] LENGTH = { (byte) 0, (byte) 0, (byte) 0,
		(byte) 0 };
	
	/** The length. */
	private static int length;
	
	/** The priority. */
	private static final int PRIORITY = 3;

	/**
	 * Instantiates a new keep alive.
	 *
	 * @param planeID the plane id
	 * @param posx the posx
	 * @param posy the posy
	 */
	public KeepAlive(byte[] planeID, int posx, int posy) {
		super(planeID, length, posx, posy);
		type = MessageType.KEEPALIVE;
		priority = PRIORITY;
		length = 0;

	}

	/**
	 * Redefine the method sendMessage from the Class Message.
	 * Not used.
	 */
	@Override
	public void sendMessage(ByteArrayOutputStream byteArrayOutputStream) {

		byte[] messageKeepAlive = new byte[28];

		// First 8 bytes planeID
		for (int i = 0; i < planeID.length; i++) {
			messageKeepAlive[i] = planeID[i];
		}
		// Next 4 bytes for length of Message
		for (int i = 0; i < LENGTH.length; i++) {

			messageKeepAlive[i + 8] = LENGTH[i];
		}
		// Next 4 bytes for priority
		messageKeepAlive[12] = (byte) (PRIORITY >>> 24);
		messageKeepAlive[13] = (byte) (PRIORITY >> 16 & 0xFF);
		messageKeepAlive[14] = (byte) (PRIORITY >> 8 & 0xFF);
		messageKeepAlive[15] = (byte) (PRIORITY & 0xFF);
		// Next 4 bytes for posX of Plane
		messageKeepAlive[16] = (byte) (posx >>> 24);
		messageKeepAlive[17] = (byte) (posx >> 16 & 0xFF);
		messageKeepAlive[18] = (byte) (posx >> 8 & 0xFF);
		messageKeepAlive[19] = (byte) (posx & 0xFF);
		// Next 4 bytes for posY of Plane
		messageKeepAlive[20] = (byte) (posy >>> 24);
		messageKeepAlive[21] = (byte) (posy >> 16 & 0xFF);
		messageKeepAlive[22] = (byte) (posy >> 8 & 0xFF);
		messageKeepAlive[23] = (byte) (posy & 0xFF);
		// Next 4 bytes for Type Of Message
		messageKeepAlive[24] = (byte) (type.ordinal() >>> 24);
		messageKeepAlive[25] = (byte) (type.ordinal() >> 16 & 0xFF);
		messageKeepAlive[26] = (byte) (type.ordinal() >> 8 & 0xFF);
		messageKeepAlive[27] = (byte) (type.ordinal() & 0xFF);
				
		try {
			byteArrayOutputStream.write(messageKeepAlive);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * Redefine the method accept from the Class Message.
	 * This method allows an object MessageHanadlerVisitor to visit
	 * a KeepAlive message.
	 * 
	 * @ param visitor an object MessageHandlerVisitor
	 */
	@Override
	public void accept(MessageHandlerVisitor visitor) {

		visitor.visit(this, null, null);
	}


	@Override
	public String[][] generateArrayStringXML(String source, String destination) {
	
		stringArray = new String[4][2];
		stringArray[0][1] = source;
		stringArray[1][1] = destination;
		stringArray[2][0] = "PosX";
		stringArray[2][1] = String.valueOf(posx);
		stringArray[3][0] = "PosY";
		stringArray[3][1] = String.valueOf(posy);
		
		return stringArray;
	}

}
