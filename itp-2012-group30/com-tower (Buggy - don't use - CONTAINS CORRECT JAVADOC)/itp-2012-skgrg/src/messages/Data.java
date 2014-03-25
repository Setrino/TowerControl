/*
 * Sergei Kostevitch
 * Gabriel Rodrigues Gomes
 * Feb 28, 2012
 */

package messages;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * The Class Data extends the class Message.
 * Multimedia files (images, sounds, videos) can be send
 * dividing them onto several Data messages.
 */
public class Data extends Message{
	
	/** The Constant MAX_PACKET_SIZE. */
	public static final int MAX_PACKET_SIZE = 1024;
	// 20 bytes
	/** The hash. */
	private byte[] hash;
	// Continuation or Piece Index
	/** The piece index. */
	private int pieceIndex;
	// 4 bytes file extension
	/** The format. */
	private byte[] format;
	
	/** The length. */
	private static int length;
	
	/** The file size. */
	private int fileSize;
	// length is payload.length here
	/** The payload. */
	private byte[] payload;
	
	/** The Constant PRIORITY. */
	private static final int PRIORITY = 4;
	
	private String hashAndPlaneIDName = null;
	
	private String[][] stringArray = null;

	/**
	 * Instantiates a new data.
	 *
	 * @param planeID the plane id
	 * @param pieceIndex the piece index
	 * @param posx the posx
	 * @param posy the posy
	 * @param hash the hash
	 * @param format the format
	 * @param fileSize the file size
	 * @param payload the payload
	 */
	public Data(byte[] planeID, int pieceIndex, int posx, int posy, byte[] hash, byte[] format, int fileSize, byte[] payload) {
		super(planeID, length, posx, posy);
		type = MessageType.DATA;
		length = payload.length;
		priority = PRIORITY;
		this.pieceIndex = pieceIndex;
		this.hash = hash;
		this.format = format;
		this.fileSize = fileSize;
		this.payload = payload;
		hashAndPlaneIDName = new String(hash) + new String(planeID);
		
	}

	/**
	 * Redefine the method sendMessage from the Class Message.
	 * Not used (but could be) :P
	 */
	@Override
	public void sendMessage(ByteArrayOutputStream byteArrayOutputStream) {
		
		byte[] messageData = new byte[60 + payload.length];

		// First 8 bytes planeID
		for (int i = 0; i < planeID.length; i++) {
			messageData[i] = planeID[i];
		}
		// Next 4 bytes for length of Message
		messageData[8] = (byte) (payload.length >>> 24);
		messageData[9] = (byte) (payload.length >> 16 & 0xFF);
		messageData[10] = (byte) (payload.length >> 8 & 0xFF);
		messageData[11] = (byte) (payload.length & 0xFF);
		// Next 4 bytes for priority
		messageData[12] = (byte) (PRIORITY >>> 24);
		messageData[13] = (byte) (PRIORITY >> 16 & 0xFF);
		messageData[14] = (byte) (PRIORITY >> 8 & 0xFF);
		messageData[15] = (byte) (PRIORITY & 0xFF);
		// Next 4 bytes for posX of Plane
		messageData[16] = (byte) (posx >>> 24);
		messageData[17] = (byte) (posx >> 16 & 0xFF);
		messageData[18] = (byte) (posx >> 8 & 0xFF);
		messageData[19] = (byte) (posx & 0xFF);
		// Next 4 bytes for posY of Plane
		messageData[20] = (byte) (posy >>> 24);
		messageData[21] = (byte) (posy >> 16 & 0xFF);
		messageData[22] = (byte) (posy >> 8 & 0xFF);
		messageData[23] = (byte) (posy & 0xFF);
		// Next 4 bytes for Type Of Message
		messageData[24] = (byte) (type.ordinal() >>> 24);
		messageData[25] = (byte) (type.ordinal() >> 16 & 0xFF);
		messageData[26] = (byte) (type.ordinal() >> 8 & 0xFF);
		messageData[27] = (byte) (type.ordinal() & 0xFF);
		// Next 4 byte for hash
		
		System.out.println("Hash " + hash.length);
		
		for (int i = 0; i < hash.length; i++) {
			
			messageData[i + 28] = hash[i];
			
		}		
		
		// Next 4 byte for piece Index
		messageData[48] = (byte) (pieceIndex >>> 24);
		messageData[49] = (byte) (pieceIndex >> 16 & 0xFF);
		messageData[50] = (byte) (pieceIndex >> 8 & 0xFF);
		messageData[51] = (byte) (pieceIndex & 0xFF);
		
		for (int i = 0; i < format.length; i++) {
			
			messageData[i + 52] = format[i];
		}
	
		// Next 4 byte for fileSize
		messageData[56] = (byte) (fileSize >>> 24);
		messageData[57] = (byte) (fileSize >> 16 & 0xFF);
		messageData[58] = (byte) (fileSize >> 8 & 0xFF);
		messageData[59] = (byte) (fileSize & 0xFF);
		
		for (int i = 0; i < payload.length; i++) {
			messageData[i + 60] = payload[i];
		}
		
		try {

			byteArrayOutputStream.write(messageData);

		} catch (IOException e) {

			System.err.println("Message Data failed to send");
			e.printStackTrace();
		}
	}

	/**
	 * Redefine the method accept from the Class Message.
	 * This method allows an object MessageHanadlerVisitor to visit
	 * a Data message.
	 * 
	 * @ param visitor an object MessageHandlerVisitor
	 */
	@Override
	public void accept(MessageHandlerVisitor visitor){
			
		visitor.visit(this, null, null, null, null, false);
	}
	
	/**
	 * Gets the hash.
	 *
	 * @return the hash
	 */
	public byte [] getHash() {
		return hash;
	}
	
	/**
	 * Gets the piece index.
	 *
	 * @return the piece index
	 */
	public int getPieceIndex() {
		return pieceIndex;
	}
	
	/**
	 * Gets the format.
	 *
	 * @return the format
	 */
	public byte[] getFormat() {
		return format;
	}
	
	/**
	 * Gets the file size.
	 *
	 * @return the file size
	 */
	public int getFileSize() {
		return fileSize;
	}
	
	/**
	 * Gets the payload.
	 *
	 * @return the payload
	 */
	public byte[] getPayload() {
		return payload;
	}


	@Override
	public String[][] generateArrayStringXML(String source, String destination) {
		
		stringArray = new String[8][2];
		stringArray[0][1] = source;
		stringArray[1][1] = destination;
		stringArray[2][0] = "PosX";
		stringArray[2][1] = String.valueOf(posx);
		stringArray[3][0] = "PosY";
		stringArray[3][1] = String.valueOf(posy);		
		stringArray[4][0] = "PieceIndex";
		stringArray[4][1] = String.valueOf(pieceIndex);
		stringArray[5][0] = "Format";
		stringArray[5][1] = new String(format).trim();
		stringArray[6][0] = "FileSize";
		stringArray[6][1] = String.valueOf(fileSize);
		stringArray[7][0] = "PayloadLength";
		stringArray[7][1] = String.valueOf(length);
		
		return stringArray;
	}
	
	public String getHashAndPlaneIDName() {
		return hashAndPlaneIDName;
	}
}
