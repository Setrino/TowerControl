/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package messages;

import java.io.ByteArrayOutputStream;

// TODO: Auto-generated Javadoc
/**
 * The Class UnChoke.
 */
public class UnChoke extends Message{

	private static final int PRIORITY = 4;
	
	/**
	 * Instantiates a new un choke.
	 *
	 * @param planeID the plane id
	 * @param length the length
	 * @param priority the priority
	 * @param posx the posx
	 * @param posy the posy
	 */
	public UnChoke(byte[] planeID, int length, int posx,
			int posy) {
		super(planeID, length, posx, posy);
		type = MessageType.UNCHOKE;
		priority = PRIORITY;
	}

	/* (non-Javadoc)
	 * @see messages.Message#sendMessage(java.io.ByteArrayOutputStream)
	 */
	@Override
	public void sendMessage(ByteArrayOutputStream byteArrayOutputStream) {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see messages.Message#accept(messages.MessageHandlerVisitor)
	 */
	@Override
	public void accept(MessageHandlerVisitor visitor){
			
		visitor.visit(this, null, null);
	}


	@Override
	public String[][] generateArrayStringXML(String source, String destination) {
		// TODO Auto-generated method stub
		return null;
	}

}
