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
 * The Class Choke.
 */
public class Choke extends Message {

	private static final int PRIORITY = 1;

	/**
	 * Instantiates a new choke.
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
	public Choke(byte[] planeID, int length, int posx, int posy) {
		super(planeID, length, posx, posy);
		type = MessageType.CHOKE;
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
		super.sendMessage(dataOutputStream);

	}
}
