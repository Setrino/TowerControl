/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package messages;

import java.io.ByteArrayOutputStream;

/**
 * The Class LandingRequest extends the abstract Class Message.
 * It's a message send from a plane to the tower to ask the permission
 * to land at the airport.
 */
public class LandingRequest extends Message{
	
	private static final int PRIORITY = 2;
	
	private static int lenght = 0;
	
	private String[][] stringArray = null;
	/**
	 * Instantiates a new landing request.
	 *
	 * @param planeID the plane id
	 * @param length the length
	 * @param priority the priority
	 * @param posx the posx
	 * @param posy the posy
	 * @param type the type
	 */
	public LandingRequest(byte[] planeID, int posx,
			int posy) {
		super(planeID, lenght, posx, posy);
		
		
		lenght = this.length;
		type = MessageType.LANDINGREQUEST;
		priority = PRIORITY;
	}

	/**
	 * Redefine the method sendMessage from the Class Message.
	 * Not used.
	 */
	@Override
	public void sendMessage(ByteArrayOutputStream byteArrayOutputStream) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Redefine the method accept from the Class Message.
	 * This method allows an object MessageHanadlerVisitor to visit
	 * a LandingRequest message.
	 * 
	 * @ param visitor an object MessageHandlerVisitor
	 */
	@Override
	public void accept(MessageHandlerVisitor visitor){
			
		visitor.visit(this, null, null);
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
