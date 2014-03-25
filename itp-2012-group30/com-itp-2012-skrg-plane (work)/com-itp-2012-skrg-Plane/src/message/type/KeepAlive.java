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
 * The Class KeepAlive.
 */
public class KeepAlive extends Message {

	/** The length. */
	private static int length;

	/** The priority. */
	private static final int PRIORITY = 3;
	
	/** The Constant LENGTH. */
	private static final byte[] LENGTH = { (byte) 0, (byte) 0, (byte) 0,
			(byte) 0 };

	/**
	 * Instantiates a new keep alive.
	 * 
	 * @param planeID
	 *            the plane id
	 * @param posx
	 *            the posx
	 * @param posy
	 *            the posy
	 */
	public KeepAlive(byte[] planeID, int posx, int posy) {
		super(planeID, length, posx, posy);
		type = MessageType.KEEPALIVE;
		priority = PRIORITY;
		length = 0;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.Message#accept(messages.MessageHandlerVisitor)
	 */
	@Override
	public void accept(MessageHandlerVisitor visitor) {

		visitor.visit(this, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.Message#sendMessage(java.io.ByteArrayOutputStream)
	 */
	@Override
	public void sendMessage(DataOutputStream dataOutputStream) {
		
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
			dataOutputStream.write(messageKeepAlive);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

}
