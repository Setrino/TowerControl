/*
 * Sergei Kostevitch
 * Gabriel Gomes
 * Feb 28, 2012
 */

package messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * The Class Choke extends Message.
 * It is used to put a communication between the tower
 * and a plane in standby, for example when the tower has to deal
 * with an emergency case. (Another plane send to the tower
 * a MayDay message)
 */
public class Choke extends Message{

	private static final int PRIORITY = 1;
	
	/** The Constant LENGTH. */
	private static final byte[] LENGTH = { (byte) 0, (byte) 0, (byte) 0,
			(byte) 0 };
	
	/**
	 * Instantiates a new choke.
	 *
	 * @param planeID the plane id
	 * @param length the length
	 * @param priority the priority
	 * @param posx the posx
	 * @param posy the posy
	 */
	public Choke(byte[] planeID, int length, int posx, int posy) {
		super(planeID, length, posx, posy);
		type = MessageType.CHOKE;
		priority = PRIORITY;
	}

	/**
	 * Redefine the method sendMessage from the Class Message.
	 * Not used.
	 */
	@Override
	public void sendMessage(ByteArrayOutputStream byteArrayOutputStream) {
		
		byte[] messageChoke = new byte[28];

		//First 8 bytes planeID
		for (int i = 0; i < planeID.length; i++) {		
			messageChoke[i] = planeID[i];
		}
		//Next 4 bytes for length of message
		for (int i = 0; i < LENGTH.length; i++) {
			
			messageChoke[i + 8] = LENGTH[i];
		}		
		//Next 4 bytes for priority of message
		messageChoke[12] = (byte) (PRIORITY   >>> 24);
		messageChoke[13] = (byte) (PRIORITY >> 16 & 0xFF);
		messageChoke[14] = (byte) (PRIORITY >> 8 & 0xFF);
		messageChoke[15] = (byte) (PRIORITY & 0xFF);
		//Next 4 bytes for PosX of Plane
		messageChoke[16] = (byte) (posx   >>> 24);
		messageChoke[17] = (byte) (posx >> 16 & 0xFF);
		messageChoke[18] = (byte) (posx >> 8 & 0xFF);
		messageChoke[19] = (byte) (posx & 0xFF);
		//Next 4 bytes for PosY of message
		messageChoke[20] = (byte) (posy   >>> 24);
		messageChoke[21] = (byte) (posy >> 16 & 0xFF);
		messageChoke[22] = (byte) (posy >> 8 & 0xFF);
		messageChoke[23] = (byte) (posy & 0xFF);
		//Next 4 bytes for Type of message
		messageChoke[24] = (byte) (type.ordinal() >>> 24);
		messageChoke[25] = (byte) (type.ordinal() >> 16 & 0xFF);
		messageChoke[26] = (byte) (type.ordinal() >> 8 & 0xFF);
		messageChoke[27] = (byte) (type.ordinal() & 0xFF);

		try {
			
			byteArrayOutputStream.write(messageChoke);
			
			
		} catch (IOException e) {
			
			System.err.println("Message Bye failed to send");
			e.printStackTrace();
		}
		
	}

	/**
	 * Redefine the method accept from the Class Message.
	 * This method allows an object MessageHanadlerVisitor to visit
	 * a choke message.
	 * 
	 * @ param visitor an object MessageHandlerVisitor
	 */
	@Override
	public void accept(MessageHandlerVisitor visitor){
			
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
