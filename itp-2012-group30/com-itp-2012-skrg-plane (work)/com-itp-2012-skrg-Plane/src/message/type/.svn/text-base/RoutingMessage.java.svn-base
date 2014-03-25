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
 * The Class RoutingMessage.
 */
public class RoutingMessage extends Message {

	private static int length;

	private static final int PRIORITY = 2;

	/** The move type. */
	private final MoveType moveType;

	/** The payload. */
	private final byte[] payload;

	/** The routing message type. */
	private final RoutingMessageType routingMessageType;

	// length will be payload.length

	/**
	 * Instantiates a new routing message.
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
	 * @param routingMessageType
	 *            the routing message type
	 * @param moveType
	 *            the move type
	 */
	public RoutingMessage(byte[] planeID, int posx, int posy,
			RoutingMessageType routingMessageType, MoveType moveType,
			byte[] payload) {
		super(planeID, length, posx, posy);
		type = MessageType.ROUTING;
		length = payload.length;
		this.payload = payload;
		this.routingMessageType = routingMessageType;
		this.moveType = moveType;
		priority = PRIORITY;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.Message#accept(messages.MessageHandlerVisitor)
	 */
	@Override
	public void accept(MessageHandlerVisitor visitor) {

		visitor.visit(this, null, null);
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.Message#sendMessage(java.io.ByteArrayOutputStream)
	 */
	@Override
	public void sendMessage(DataOutputStream dataOutputStream) {

	}

	public int getX() {
		// TODO Auto-generated method stub
		return posx;
	}
	
	public int getY() {
		// TODO Auto-generated method stub
		return posy;
	}
	
	public static int getLength() {
		return length;
	}

	public MoveType getMoveType() {
		return moveType;
	}

	public byte[] getPayload() {
		return payload;
	}

	public RoutingMessageType getRoutingMessageType() {
		return routingMessageType;
	}
}
