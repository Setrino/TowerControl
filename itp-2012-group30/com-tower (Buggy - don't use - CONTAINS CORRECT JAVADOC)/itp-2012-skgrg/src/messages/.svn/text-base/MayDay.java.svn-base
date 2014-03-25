/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package messages;

import java.io.ByteArrayOutputStream;

// TODO: Auto-generated Javadoc
/**
 * The Class MayDay.
 */
public class MayDay extends Message{
	
	/** The cause. */
	String cause;
	
	private static final int PRIORITY = 0;
	
	//length is cause.length

	/**
	 * Instantiates a new may day.
	 *
	 * @param planeID the plane id
	 * @param length the length
	 * @param priority the priority
	 * @param posx the posx
	 * @param posy the posy
	 */
	public MayDay(byte[] planeID, int length, int posx, int posy) {
		super(planeID, length, posx, posy);
		type = MessageType.MAYDAY;
		priority = PRIORITY;
		
		
	}
	
	/**
	 * String to ascii.
	 *
	 * @param cause the cause
	 * @return the byte[]
	 */
	public byte[] stringToASCII(String cause){
		
		byte[] causeASCII = new byte[cause.length()];
		
		for ( int i = 0; i < cause.length(); ++i ) {
		   causeASCII[i] = (byte) cause.charAt(i);	  
		}
		
		return causeASCII;
	}
	
	/**
	 * Byte to string.
	 *
	 * @param messageASCII the message ascii
	 * @return the string
	 */
	public String byteToString(byte[] messageASCII){
		
		return messageASCII.toString();
	}

	/* (non-Javadoc)
	 * @see messages.Message#sendMessage(java.io.ByteArrayOutputStream)
	 */
	@Override
	public void sendMessage(ByteArrayOutputStream byteArrayOutputStream) {
		
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
