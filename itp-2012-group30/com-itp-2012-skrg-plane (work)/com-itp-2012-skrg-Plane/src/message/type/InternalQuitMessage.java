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
 * The Class InternalQuitMessage.
 */
public class InternalQuitMessage extends Message {

	/**
	 * Instantiates a new internal quit message.
	 * 
	 * @param planeID
	 *            the plane id
	 */
	public InternalQuitMessage(byte[] planeID) {
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
		super.sendMessage(dataOutputStream);

	}

}
