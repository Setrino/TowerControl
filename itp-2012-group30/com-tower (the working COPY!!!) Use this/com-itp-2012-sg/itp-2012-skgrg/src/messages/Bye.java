/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * The Class Bye.
 */
public class Bye extends Message {

	/** The Constant LENGTH. */
	private static final byte[] LENGTH = { (byte) 0, (byte) 0, (byte) 0, (byte) 0 };
	
	/** The posy. */
	private static int length, posx, posy = 0;
	
	/** The plane id. */
	private byte[] planeID;
	
	/** The Constant PRIORITY. */
	private static final int PRIORITY = 3;
	
	private String[][] stringArray = null;

	/**
	 * Instantiates a new bye.
	 *
	 * @param planeID the plane id
	 */
	public Bye(byte[] planeID) {
		super(planeID, length, posx, posy);
		type = MessageType.BYE;
		this.planeID = planeID;
		priority = PRIORITY;

	}

	/* (non-Javadoc)
	 * @see messages.Message#sendMessage(java.io.ByteArrayOutputStream)
	 */
	@Override
	public void sendMessage(ByteArrayOutputStream byteArrayOutputStream) {
		
		byte[] messageBye = new byte[28];
		
		
		//First 8 bytes planeID
		for (int i = 0; i < planeID.length; i++) {		
			messageBye[i] = planeID[i];
		}
		//Next 4 bytes for length of message
		for (int i = 0; i < LENGTH.length; i++) {
			
			messageBye[i + 8] = LENGTH[i];
		}		
		//Next 4 bytes for priority of message
		messageBye[12] = (byte) (PRIORITY   >>> 24);
		messageBye[13] = (byte) (PRIORITY >> 16 & 0xFF);
		messageBye[14] = (byte) (PRIORITY >> 8 & 0xFF);
		messageBye[15] = (byte) (PRIORITY & 0xFF);
		//Next 4 bytes for PosX of Plane
		messageBye[16] = (byte) (posx   >>> 24);
		messageBye[17] = (byte) (posx >> 16 & 0xFF);
		messageBye[18] = (byte) (posx >> 8 & 0xFF);
		messageBye[19] = (byte) (posx & 0xFF);
		//Next 4 bytes for PosY of message
		messageBye[20] = (byte) (posy   >>> 24);
		messageBye[21] = (byte) (posy >> 16 & 0xFF);
		messageBye[22] = (byte) (posy >> 8 & 0xFF);
		messageBye[23] = (byte) (posy & 0xFF);
		//Next 4 bytes for Type of message
		messageBye[24] = (byte) (type.ordinal() >>> 24);
		messageBye[25] = (byte) (type.ordinal() >> 16 & 0xFF);
		messageBye[26] = (byte) (type.ordinal() >> 8 & 0xFF);
		messageBye[27] = (byte) (type.ordinal() & 0xFF);

		try {
			
			byteArrayOutputStream.write(messageBye);
			
			
		} catch (IOException e) {
			
			System.err.println("Message Bye failed to send");
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see messages.Message#accept(messages.MessageHandlerVisitor)
	 */
	@Override
	public void accept(MessageHandlerVisitor visitor) {

		visitor.visit(this, null, null, null);
	}

	@Override
	public String[][] generateArrayStringXML(String source, String destination) {
		
		stringArray = new String[4][2];
		stringArray[0][1] = source;
		stringArray[1][1] = destination;
		stringArray[2][0] = "PosX";
		stringArray[2][1] = String.valueOf(posx);
		stringArray[3][0] = "PosY";
		stringArray[3][1] = String.valueOf(posy);
		
		return stringArray;
	}

}
