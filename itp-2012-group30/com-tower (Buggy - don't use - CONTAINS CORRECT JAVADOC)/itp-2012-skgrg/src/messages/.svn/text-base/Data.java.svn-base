/*
 * Sergei Kostevitch
 * Gabriel Rodrigues Gomes
 * Feb 28, 2012
 */

package messages;

import java.io.ByteArrayOutputStream;

// TODO: Auto-generated Javadoc
/**
 * The Class Data.
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
		//stringArray[5][0] = "Hash";
		//stringArray[5][1] = String.valueOf(hash);
		stringArray[5][0] = "Format";
		stringArray[5][1] = new String(format).trim();
		stringArray[6][0] = "FileSize";
		stringArray[6][1] = String.valueOf(fileSize);
		stringArray[7][0] = "PayloadLength";
		stringArray[7][1] = String.valueOf(length);
		//stringArray[9][0] = "Payload";
		//stringArray[9][1] = String.valueOf(payload);
		
		
		return stringArray;
	}
	
	public String getHashAndPlaneIDName() {
		return hashAndPlaneIDName;
	}
}
