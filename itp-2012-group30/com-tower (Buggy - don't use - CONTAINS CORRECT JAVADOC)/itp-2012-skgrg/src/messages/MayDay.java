/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * The Class MayDay extends the abstract Class Message.
 * It is used by a plane to notify the tower that he is
 * in an emergency situation.
 */

public class MayDay extends Message {

	/** The cause. */
	private String cause;

	private static final int PRIORITY = 0;

	private static int length;

	private byte[] bytedCause;

	/**
	 * Instantiates a new may day.
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
	public MayDay(byte[] planeID, int posx, int posy, String cause) {
		super(planeID, length, posx, posy);
		type = MessageType.MAYDAY;
		bytedCause = stringToASCII(cause);
		length = bytedCause.length;
		priority = PRIORITY;
		this.cause = cause;

	}

	/**
	 * String to ascii.
	 * 
	 * @param cause
	 *            the cause
	 * @return the byte[]
	 */
	public byte[] stringToASCII(String cause) {

		byte[] temp = null;
		try {
			temp = cause.getBytes("ASCII");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return temp;
	}

	/**
	 * Byte to string.
	 * 
	 * @param messageASCII
	 *            the message ascii
	 * @return the string
	 */
	public String byteToString(byte[] messageASCII) {

		return messageASCII.toString();
	}

	/**
	 * Redefine the method sendMessage from the Class Message.
	 * Not used.
	 */
	@Override
	public void sendMessage(ByteArrayOutputStream byteArrayOutputStream) {

		byte[] messageMayDay = new byte[28 + bytedCause.length];

		// First 8 bytes planeID
		for (int i = 0; i < planeID.length; i++) {
			messageMayDay[i] = planeID[i];
		}
		// Next 4 bytes for length of Message
		messageMayDay[8] = (byte) (length >>> 24);
		messageMayDay[9] = (byte) (length >> 16 & 0xFF);
		messageMayDay[10] = (byte) (length >> 8 & 0xFF);
		messageMayDay[11] = (byte) (length & 0xFF);
		// Next 4 bytes for priority
		messageMayDay[12] = (byte) (PRIORITY >>> 24);
		messageMayDay[13] = (byte) (PRIORITY >> 16 & 0xFF);
		messageMayDay[14] = (byte) (PRIORITY >> 8 & 0xFF);
		messageMayDay[15] = (byte) (PRIORITY & 0xFF);
		// Next 4 bytes for posX of Plane
		messageMayDay[16] = (byte) (posx >>> 24);
		messageMayDay[17] = (byte) (posx >> 16 & 0xFF);
		messageMayDay[18] = (byte) (posx >> 8 & 0xFF);
		messageMayDay[19] = (byte) (posx & 0xFF);
		// Next 4 bytes for posY of Plane
		messageMayDay[20] = (byte) (posy >>> 24);
		messageMayDay[21] = (byte) (posy >> 16 & 0xFF);
		messageMayDay[22] = (byte) (posy >> 8 & 0xFF);
		messageMayDay[23] = (byte) (posy & 0xFF);
		// Next 4 bytes for Type Of Message
		messageMayDay[24] = (byte) (type.ordinal() >>> 24);
		messageMayDay[25] = (byte) (type.ordinal() >> 16 & 0xFF);
		messageMayDay[26] = (byte) (type.ordinal() >> 8 & 0xFF);
		messageMayDay[27] = (byte) (type.ordinal() & 0xFF);
		// Next 4 byte for hash

		for (int i = 0; i < length; i++) {

			messageMayDay[i + 28] = bytedCause[i];
		}

		try {

			byteArrayOutputStream.write(messageMayDay);

		} catch (IOException e) {

			System.err.println("Message MayDay failed to send");
			e.printStackTrace();
		}
	}

	/**
	 * Redefine the method accept from the Class Message.
	 * This method allows an object MessageHanadlerVisitor to visit
	 * a MayDay message.
	 * 
	 * @ param visitor an object MessageHandlerVisitor
	 */
	
	@Override
	public void accept(MessageHandlerVisitor visitor) {

		visitor.visit(this, null, null, null);
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
		stringArray[4][0] = "Cause";
		stringArray[4][1] = cause;

		return stringArray;
	}
	
	public String getCause() {
		return cause;
	}

}
