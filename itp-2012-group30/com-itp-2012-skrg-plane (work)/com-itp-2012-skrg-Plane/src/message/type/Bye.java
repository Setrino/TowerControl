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
 * The Class Bye.
 */
public class Bye extends Message {

	/** The posy. */
	private static int length, posx, posy = 0;

	/** The Constant PRIORITY. */
	private static final int PRIORITY = 4;
	

	/** The Constant LENGTH. */
	private static final byte[] LENGTH = { (byte) 0, (byte) 0, (byte) 0,
			(byte) 0 };


	/**
	 * Instantiates a new bye.
	 * 
	 * @param planeID
	 *            the plane id
	 */
	public Bye(byte[] planeID, int posX, int posY) {
		super(planeID, length, posx, posy);
		posx = posX;
		posy = posY;
		length = 0;
		type = MessageType.BYE;
		this.planeID = planeID;
		priority = PRIORITY;

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
		
		byte[] messageBye = new byte[28];

		// First 8 bytes planeID
		for (int i = 0; i < planeID.length; i++) {
			messageBye[i] = planeID[i];
		}
		// Next 4 bytes for length of Message
		for (int i = 0; i < LENGTH.length; i++) {

			messageBye[i + 8] = LENGTH[i];
		}
		// Next 4 bytes for priority
		messageBye[12] = (byte) (PRIORITY >>> 24);
		messageBye[13] = (byte) (PRIORITY >> 16 & 0xFF);
		messageBye[14] = (byte) (PRIORITY >> 8 & 0xFF);
		messageBye[15] = (byte) (PRIORITY & 0xFF);
		// Next 4 bytes for posX of Plane
		messageBye[16] = (byte) (posx >>> 24);
		messageBye[17] = (byte) (posx >> 16 & 0xFF);
		messageBye[18] = (byte) (posx >> 8 & 0xFF);
		messageBye[19] = (byte) (posx & 0xFF);
		// Next 4 bytes for posY of Plane
		messageBye[20] = (byte) (posy >>> 24);
		messageBye[21] = (byte) (posy >> 16 & 0xFF);
		messageBye[22] = (byte) (posy >> 8 & 0xFF);
		messageBye[23] = (byte) (posy & 0xFF);
		// Next 4 bytes for Type Of Message
		messageBye[24] = (byte) (type.ordinal() >>> 24);
		messageBye[25] = (byte) (type.ordinal() >> 16 & 0xFF);
		messageBye[26] = (byte) (type.ordinal() >> 8 & 0xFF);
		messageBye[27] = (byte) (type.ordinal() & 0xFF);
		
		
		try {
			dataOutputStream.write(messageBye);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
