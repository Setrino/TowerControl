/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package message;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import message.type.Data;
import message.type.Hello;
import message.type.MoveType;
import message.type.RoutingMessage;
import message.type.RoutingMessageType;
import message.type.SendRSAKey;

import Plane.Plane;
import Plane.PlaneRouting;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageReader.
 */
public class MessageReader {

	/** The data input stream. */
	private DataInputStream dataInputStream = null;

	// private byte[] messageBody;
	/** The length. */
	private int length;

	@SuppressWarnings("unused")
	private int messagePriority = 0;

	/** The message handler. */

	MessageHandler messageHandler = MessageHandler.getMessageHandlerInstance();

	private Plane plane = null;
	/** The plane id. */
	private final byte[] planeID = new byte[8];

	private PlaneRouting planeRouting = null;

	/** The plane name. */
	@SuppressWarnings("unused")
	private String planeIdString;
	/** The pos x. */
	private int posX;

	private Message message;

	/** The pos y. */
	private int posY;

	// for Hello message
	/** The reserved. */
	private byte reserved;

	/** The type. */
	private MessageType type;

	/**
	 * Read the message, set the new Point of the Plane
	 * 
	 * @param inputStream
	 *            the input stream
	 * @return the message
	 */
	public Message readMessage(InputStream inputStream) {

		dataInputStream = new DataInputStream(inputStream);

		int messageTypeValue = 0;

		try {

			dataInputStream.read(planeID);
			planeIdString = new String(planeID);
			length = dataInputStream.readInt();
			messagePriority = dataInputStream.readInt();
			posX = dataInputStream.readInt();
			posY = dataInputStream.readInt();
			messageTypeValue = dataInputStream.readInt();
			type = MessageType.values()[messageTypeValue];

			switch (type) {
			case HELLO:

				System.out.println("Voila");

				reserved = dataInputStream.readByte();

				message = new Hello(planeID, posX, posY, reserved);

				break;
			case DATA:

				byte[] hash = new byte[20];
				dataInputStream.read(hash);
				int pieceIndex = dataInputStream.readInt();
				byte[] format = new byte[4];
				dataInputStream.read(format);
				System.out.println("Length of payload is " + length);
				int fileSize = dataInputStream.readInt();
				System.out.println("FileSize " + fileSize);
				byte[] payload = new byte[length];
				dataInputStream.read(payload);

				message = new Data(planeID, pieceIndex, posX, posY, hash,
						format, fileSize, payload);

				break;

			case SENDRSA:

				System.out.println("SendRSA received");

				int keySize = dataInputStream.readInt();
				int modulusLength = dataInputStream.readInt();
				byte[] modulus = new byte[modulusLength];
				dataInputStream.read(modulus);
				int publicKeyLength = dataInputStream.readInt();
				byte[] publicKey = new byte[publicKeyLength];
				dataInputStream.read(publicKey);

				message = new SendRSAKey(planeID, posX, posY, keySize,
						modulusLength, modulus, publicKeyLength, publicKey);

				break;
			case CHOKE:

				plane.setChoked(true);

				break;
			case UNCHOKE:

				plane.setChoked(false);

				break;

			case ROUTING:
				
				System.out.println("PosX " + posX + " PosY " + posY);

				int routingIntType = dataInputStream.readInt();
				RoutingMessageType routingType = null;
				switch (routingIntType) {
				case 0:
					routingType = RoutingMessageType.NEWFIRST;
					break;
				case 1:
					routingType = RoutingMessageType.LAST;
					break;
				case 2:
					routingType = RoutingMessageType.REPLACEALL;
					break;

				}

				int moveIntType = dataInputStream.readInt();
				MoveType moveType = null;
				switch (moveIntType) {
				case 0:
					moveType = MoveType.STRAIGHT;
					break;
				case 1:
					moveType = MoveType.CIRCULAR;
					break;
				case 2:
					moveType = MoveType.LANDING;
					break;
				case 3:
					moveType = MoveType.NONE;
					break;
				case 4:
					moveType = MoveType.DESTRUCTION;
					break;

				}

				byte[] movePayload = new byte[4];

				if (length != 0) {
					payload = new byte[4];
					dataInputStream.read(movePayload);
				}

				message = new RoutingMessage(planeID, posX, posY, routingType,
						moveType, movePayload);
				messageHandler.visit((RoutingMessage) message, planeRouting,
						routingType);

				break;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return message;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}

	public void setPlaneRouting(PlaneRouting planeRouting) {

		this.planeRouting = planeRouting;
	}

}
