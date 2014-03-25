/*
 * Sergei Kostevitch
 * Gabriel Rodrigues Gomes
 * Feb 28, 2012
 */

package messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * The Class UnChoke extends the abstract Class Message. It's used to cancel a
 * "choke" situation. So the communications between the tower and planes can be
 * again normally done.
 */
public class UnChoke extends Message {

	private static final int PRIORITY = 4;

	/** The Constant LENGTH. */
	private static final byte[] LENGTH = { (byte) 0, (byte) 0, (byte) 0,
			(byte) 0 };

	/**
	 * Instantiates a new un choke.
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
	public UnChoke(byte[] planeID, int length, int posx, int posy) {
		super(planeID, length, posx, posy);
		type = MessageType.UNCHOKE;
		priority = PRIORITY;
	}

	/**
	 * Redefine the method sendMessage from the Class Message. Not used.
	 */
	@Override
	public void sendMessage(ByteArrayOutputStream byteArrayOutputStream) {

		byte[] messageUnChoke = new byte[28];

		// First 8 bytes planeID
		for (int i = 0; i < planeID.length; i++) {
			messageUnChoke[i] = planeID[i];
		}
		// Next 4 bytes for length of message
		for (int i = 0; i < LENGTH.length; i++) {

			messageUnChoke[i + 8] = LENGTH[i];
		}
		// Next 4 bytes for priority of message
		messageUnChoke[12] = (byte) (PRIORITY >>> 24);
		messageUnChoke[13] = (byte) (PRIORITY >> 16 & 0xFF);
		messageUnChoke[14] = (byte) (PRIORITY >> 8 & 0xFF);
		messageUnChoke[15] = (byte) (PRIORITY & 0xFF);
		// Next 4 bytes for PosX of Plane
		messageUnChoke[16] = (byte) (posx >>> 24);
		messageUnChoke[17] = (byte) (posx >> 16 & 0xFF);
		messageUnChoke[18] = (byte) (posx >> 8 & 0xFF);
		messageUnChoke[19] = (byte) (posx & 0xFF);
		// Next 4 bytes for PosY of message
		messageUnChoke[20] = (byte) (posy >>> 24);
		messageUnChoke[21] = (byte) (posy >> 16 & 0xFF);
		messageUnChoke[22] = (byte) (posy >> 8 & 0xFF);
		messageUnChoke[23] = (byte) (posy & 0xFF);
		// Next 4 bytes for Type of message
		messageUnChoke[24] = (byte) (type.ordinal() >>> 24);
		messageUnChoke[25] = (byte) (type.ordinal() >> 16 & 0xFF);
		messageUnChoke[26] = (byte) (type.ordinal() >> 8 & 0xFF);
		messageUnChoke[27] = (byte) (type.ordinal() & 0xFF);

		try {

			byteArrayOutputStream.write(messageUnChoke);

		} catch (IOException e) {

			System.err.println("Message UnChoke failed to send");
			e.printStackTrace();
		}

	}

	/**
	 * Redefine the method accept from the Class Message.
	 * This method allows an object MessageHanadlerVisitor to visit
	 * an UnChoke message.
	 * 
	 * @ param visitor an object MessageHandlerVisitor
	 */
	@Override
	public void accept(MessageHandlerVisitor visitor) {

		visitor.visit(this, null, null, null);
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
