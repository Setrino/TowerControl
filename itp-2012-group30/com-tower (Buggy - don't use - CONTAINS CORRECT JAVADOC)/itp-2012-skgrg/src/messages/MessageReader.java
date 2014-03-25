/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package messages;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import plane.Plane;
import tower.Tower;

/**
 * The Class MessageReader.
 * It reads the incoming messages and throws different methods,
 * depending on the message type.
 */
public class MessageReader {

	/** The message handler. */
	private MessageHandler messageHandler = MessageHandler
			.getMessageHandlerInstance();

	/** The data input stream. */
	private DataInputStream dataInputStream = null;

	/** The type. */
	private MessageType type;

	/** The plane id. */
	private byte[] planeID = new byte[8];

	/** The pos x. */
	private int posX;

	/** The pos y. */
	private int posY;

	/** The plane name. */
	private String planeIdString;
	/** The length. */
	private int length;

	/** The message priority. */
	@SuppressWarnings("unused")
	private int messagePriority;
	/** The reserved. */
	private byte reserved;

	/** The string array. */
	private String[][] stringArray;

	private Plane plane = null;

	private Tower tower;

	private int landingRequestsCounter = 0;
	
	private boolean firstTxtFile = true;

	/**
	 * Instantiates a new message reader.
	 */
	public MessageReader() {

		tower = Tower.getTowerInstance();
	}

	/**
	 * Read the message, set the new Point of the Plane
	 * 
	 * @param inputStream
	 *            the input stream
	 * @return the message
	 */
	public Message readMessage(InputStream inputStream) {

		dataInputStream = new DataInputStream(inputStream);
		Message message = null;

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

			plane.setPosX(posX);
			plane.setPosY(posY);
			plane.setPoint(new Point(posX, posY));
			tower.setPlanePosition(planeIdString, new Point(posX, posY));

			switch (type) {
			case HELLO:

				reserved = dataInputStream.readByte();

				message = new Hello(planeID, posX, posY, reserved);

				stringArray = message.generateArrayStringXML(planeIdString,
						"Tower");

				messageHandler.visit((Hello) message, planeIdString, "Tower",
						stringArray);
				break;
			case DATA:
				
				System.out.println("length " + length);

				byte[] hash = new byte[20];
				dataInputStream.read(hash);
				int pieceIndex = dataInputStream.readInt();
				byte[] format = new byte[4];
				dataInputStream.read(format);
				int fileSize = dataInputStream.readInt();
				byte[] payload = new byte[length];
				dataInputStream.read(payload);

				message = new Data(planeID, pieceIndex, posX, posY, hash,
						format, fileSize, payload);

				messageHandler.visit((Data) message, planeIdString, "Tower", plane, this, firstTxtFile);

				break;
			case MAYDAY:
				
				byte[] cause = new byte[length];
				dataInputStream.read(cause);				
				message = new MayDay(planeID, posX, posY, new String(cause));

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

				messageHandler.visit((SendRSAKey) message, planeIdString,
						"Tower");

				break;
			case CHOKE:

				break;
			case UNCHOKE:

				break;
			case BYE:

				message = new Bye(planeID);

				messageHandler.visit((Bye) message, planeIdString, "Tower", plane);

				break;
			case ROUTING:

				break;
			case KEEPALIVE:

				message = new KeepAlive(planeID, posX, posY);

				messageHandler.visit((KeepAlive) message, planeIdString,
						"Tower");

				break;
			case LANDINGREQUEST:

				message = new LandingRequest(planeID, posX, posY);
				
				messageHandler.visit((LandingRequest) message, planeIdString, "Tower");
				
				landingRequestsCounter++;
				
				tower.addLandingRequestPerPlane(plane);

				break;

			default:
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return message;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}

	public void setFirstTxtFile(boolean firstTxtFile) {
		this.firstTxtFile = firstTxtFile;
	}

}
