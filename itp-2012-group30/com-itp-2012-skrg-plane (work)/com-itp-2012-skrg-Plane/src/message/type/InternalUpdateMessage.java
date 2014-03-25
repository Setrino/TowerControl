/*
 * Sergei Kostevitch
 * Mar 13, 2012
 */

package message.type;

import java.io.DataOutputStream;

import message.Message;
import message.MessageHandlerVisitor;


// TODO: Auto-generated Javadoc
/**
 * The Class InternalUpdateMessage.
 */
public class InternalUpdateMessage extends Message {

	/**
	 * Instantiates a new internal update message.
	 * 
	 * @param planeID
	 *            the plane id
	 */
	public InternalUpdateMessage(byte[] planeID) {
		super(planeID);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.Message#accept(messages.MessageHandlerVisitor)
	 */
	@Override
	public void accept(MessageHandlerVisitor visitor) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.Message#sendMessage(java.io.ByteArrayOutputStream)
	 */
	@Override
	public void sendMessage(DataOutputStream dataOutputStream) {
		// TODO Auto-generated method stub

	}

}
