/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package message.type;

import java.io.DataOutputStream;

import message.Message;
import message.MessageHandlerVisitor;
import message.MessageType;


// TODO: Auto-generated Javadoc
/**
 * The Class MayDay.
 */
public class MayDay extends Message {

	private static final int PRIORITY = 0;

	/** The cause. */
	String cause;

	// length is cause.length

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
	public MayDay(byte[] planeID, int length, int posx, int posy, String cause) {
		super(planeID, length, posx, posy);
		type = MessageType.MAYDAY;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.Message#sendMessage(java.io.ByteArrayOutputStream)
	 */
	@Override
	public void sendMessage(DataOutputStream dataOutputStream) {
		super.sendMessage(dataOutputStream);
	}

	/**
	 * String to ascii.
	 * 
	 * @param cause
	 *            the cause
	 * @return the byte[]
	 */
	public byte[] stringToASCII(String cause) {

		byte[] causeASCII = new byte[cause.length()];

		for (int i = 0; i < cause.length(); ++i) {
			causeASCII[i] = (byte) cause.charAt(i);
		}

		return causeASCII;
	}

}
