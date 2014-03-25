/*
 * Sergei Kostevitch
 * Gabriel Rodrigues Gomes
 * Feb 28, 2012
 */

package messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * The Class RoutingMessage extends the abstract Class Message.
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
		}	
		this.routingMessageType = routingMessageType.ordinal();
		this.moveType = moveType.ordinal();
		priority = PRIORITY;

	}

	/**
	 * Redefine the method sendMessage from the Class Message.
	 * Used to send a RoutingMessage.
	 */
	@Override
	public synchronized void sendMessage(
			ByteArrayOutputStream byteArrayOutputStream) {

		/*
		 * System.out.println("PlaneID " + new String(planeID) + " Length " +
		 * length + " Priority " + PRIORITY + " posx " + posx + " posy " + posy
		 * + " RoutingMessageType " + routingMessageType + " Move Type " +
		 * moveType + " payload length " + payload.length + " MessageType " +
		 * type);
		 */

		messageRouting = new byte[36 + length];

		try {

			if (length == 4 && payload == null) {
				
				byteArrayOutputStream.reset();
				
			}else{
			
			// First 8 bytes planeID
			for (int i = 0; i < planeID.length; i++) {
				messageRouting[i] = planeID[i];
			}
			// Next 4 bytes for length of Message
			messageRouting[8] = (byte) (length >>> 24);
			messageRouting[9] = (byte) (length >> 16 & 0xFF);
			messageRouting[10] = (byte) (length >> 8 & 0xFF);
			messageRouting[11] = (byte) (length & 0xFF);
			// Next 4 bytes for priority
			messageRouting[12] = (byte) (PRIORITY >>> 24);
			messageRouting[13] = (byte) (PRIORITY >> 16 & 0xFF);
			messageRouting[14] = (byte) (PRIORITY >> 8 & 0xFF);
			messageRouting[15] = (byte) (PRIORITY & 0xFF);
			// Next 4 bytes for posX of Plane
			messageRouting[16] = (byte) (posx >>> 24);
			messageRouting[17] = (byte) (posx >> 16 & 0xFF);
			messageRouting[18] = (byte) (posx >> 8 & 0xFF);
			messageRouting[19] = (byte) (posx & 0xFF);
			// Next 4 bytes for posY of Plane
			messageRouting[20] = (byte) (posy >>> 24);
			messageRouting[21] = (byte) (posy >> 16 & 0xFF);
			messageRouting[22] = (byte) (posy >> 8 & 0xFF);
			messageRouting[23] = (byte) (posy & 0xFF);
			// Next 4 bytes for Type Of Message
			messageRouting[24] = (byte) (type.ordinal() >>> 24);
			messageRouting[25] = (byte) (type.ordinal() >> 16 & 0xFF);
			messageRouting[26] = (byte) (type.ordinal() >> 8 & 0xFF);
			messageRouting[27] = (byte) (type.ordinal() & 0xFF);
			// Next 4 bytes for RoutingMessageType
			messageRouting[28] = (byte) (routingMessageType >>> 24);
			messageRouting[29] = (byte) (routingMessageType >> 16 & 0xFF);
			messageRouting[30] = (byte) (routingMessageType >> 8 & 0xFF);
			messageRouting[31] = (byte) (routingMessageType & 0xFF);
			// Next 4 bytes for MoveType
			messageRouting[32] = (byte) (moveType >>> 24);
			messageRouting[33] = (byte) (moveType >> 16 & 0xFF);
			messageRouting[34] = (byte) (moveType >> 8 & 0xFF);
			messageRouting[35] = (byte) (moveType & 0xFF);
			// Next unknown number of bytes for payload
			
			if (length != 0) {
				for (int i = 0; i < length; i++) {

					messageRouting[i + 36] = payload[i];
				}
			}
			
			byteArrayOutputStream.write(messageRouting);
			}
			
		} catch (ArrayIndexOutOfBoundsException e) {

			e.printStackTrace();
			System.out.println("Message Routing size " + messageRouting.length);
			System.out.println("Payload size " + payload);
			System.out.println("Length " + length);
			
		} catch (NullPointerException e){
			
			e.printStackTrace();
			System.out.println("Length " + length);
			System.out.println("Payload " + payload);
			System.out.println("Problemo");
			
		} catch (IOException e) {

			System.out.println("Message Routing hasn't been sent");
			e.printStackTrace();
		}
	}

	/**
	 * Redefine the method accept from the Class Message.
	 * This method allows an object MessageHanadlerVisitor to visit
	 * a RoutingMessage.
	 * 
	 * @ param visitor an object MessageHandlerVisitor
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
