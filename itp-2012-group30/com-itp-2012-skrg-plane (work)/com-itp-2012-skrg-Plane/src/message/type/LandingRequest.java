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
 * The Class LandingRequest.
 */
public class LandingRequest extends Message {

	private static int lenght = 0;

	private static final int PRIORITY = 2;

	/**
	 * Instantiates a new landing request.
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
	 * @param type
	 *            the type
	 */
	public LandingRequest(byte[] planeID, int posx, int posy) {
		super(planeID, lenght, posx, posy);

		lenght = this.length;
		type = MessageType.LANDINGREQUEST;
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

	@Override
	public void sendMessage(DataOutputStream dataOutputStream) {
		super.sendMessage(dataOutputStream);

	}

}
