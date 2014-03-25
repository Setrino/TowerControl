/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package messages;

import java.io.ByteArrayOutputStream;

// TODO: Auto-generated Javadoc
/**
 * The Class KeepAlive.
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

	/* (non-Javadoc)
	 * @see messages.Message#sendMessage(java.io.ByteArrayOutputStream)
	 */
	@Override
	public void sendMessage(ByteArrayOutputStream byteArrayOutputStream) {

	}

	/* (non-Javadoc)
	 * @see messages.Message#accept(messages.MessageHandlerVisitor)
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
