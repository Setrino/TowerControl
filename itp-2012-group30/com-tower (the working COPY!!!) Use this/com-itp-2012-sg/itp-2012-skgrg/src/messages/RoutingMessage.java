/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

// TODO: Auto-generated Javadoc
/**
 * The Class RoutingMessage.
 */
public class RoutingMessage extends Message {

	/** The routing message type. */
	private int routingMessageType;

	/** The move type. */
	private int moveType;

	/** The payload. */
	private byte[] payload;

	private static final int PRIORITY = 2;

	private byte[] messageRouting;

	private static int length;

	private ByteBuffer byteBuffer = null;

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
		length = 0;
		if (payload != null) {
			length = payload.length;
			this.payload = payload;
			System.out.println("Payload head " + payload);
			System.out.println("Length head " + length);
		}
		this.routingMessageType = routingMessageType.ordinal();
		this.moveType = moveType.ordinal();
		priority = PRIORITY;

	}

	@Override
	public synchronized void sendMessage(
			ByteArrayOutputStream byteArrayOutputStream) {

		try {

			if (length == 4 && payload == null) {

				byteArrayOutputStream.reset();

			} else {

				if (length == 0) {

					byteBuffer = ByteBuffer.allocate(36);
					byteBuffer.put(planeID, 0, 8);
					byteBuffer.putInt(8, length);
					byteBuffer.putInt(12, PRIORITY);
					byteBuffer.putInt(16, posx);
					byteBuffer.putInt(20, posy);
					byteBuffer.putInt(24, type.ordinal());
					byteBuffer.putInt(28, routingMessageType);
					byteBuffer.putInt(32, moveType);

				} else {

					byteBuffer = ByteBuffer.allocate(36 + length);
					byteBuffer.put(planeID, 0, 8);
					byteBuffer.putInt(8, length);
					byteBuffer.putInt(12, PRIORITY);
					byteBuffer.putInt(16, posx);
					byteBuffer.putInt(20, posy);
					byteBuffer.putInt(24, type.ordinal());
					byteBuffer.putInt(28, routingMessageType);
					byteBuffer.putInt(32, moveType);
					byteBuffer.put(payload, 36, length);
				}

				byteArrayOutputStream.write(byteBuffer.array());
			}
		} catch (ArrayIndexOutOfBoundsException e) {

			e.printStackTrace();
			System.out.println("Message Routing size " + messageRouting.length);
			System.out.println("Payload size " + payload);
			System.out.println("Length " + length);

		} catch (NullPointerException e) {

			e.printStackTrace();
			System.out.println("Length " + length);
			System.out.println("Payload " + payload);
			System.out.println("Problemo");

		} catch (IOException e) {

			System.out.println("Message Routing hasn't been sent");
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.Message#accept(messages.MessageHandlerVisitor)
	 */
	@Override
	public void accept(MessageHandlerVisitor visitor) {

		visitor.visit(this, null, null, null);
	}

	@Override
	public String[][] generateArrayStringXML(String source, String destination) {

		stringArray = new String[6][2];
		stringArray[0][1] = source;
		stringArray[1][1] = destination;
		stringArray[2][0] = "PosX";
		stringArray[2][1] = String.valueOf(posx);
		stringArray[3][0] = "PosY";
		stringArray[3][1] = String.valueOf(posy);
		stringArray[4][0] = "RoutingMessageType";
		stringArray[4][1] = String.valueOf(routingMessageType);
		stringArray[5][0] = "MoveType";
		stringArray[5][1] = String.valueOf(moveType);

		return stringArray;
	}

}
