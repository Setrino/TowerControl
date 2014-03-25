/*
 * Sergei Kostevitch
 * Gabriel Rodrigues Gomes
 * Feb 28, 2012
 */

package messages;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

import crypto.KeyPair;

/**
 * The Class SendRSAKey extends the abstract Class Message.
 * It's used after the message Hello, to send the public keys if
 * the communication will be crypted.
 */
public class SendRSAKey extends Message {

	/** The key size. */
	int keySize;

	/** The modulus length. */
	int modulusLength;

	/** The modulus. */
	byte[] modulus;

	/** The public key length. */
	int publicKeyLength;

	/** The public key. */
	byte[] publicKey;

	/** The priority. */
	private static final int PRIORITY = 2;
	
	private String[][] stringArray;
	
	private KeyPair keyPair = null;

	/**
	 * Instantiates a new send rsa key.
	 * 
	 * @param planeID
	 *            the plane id
	 * @param publicKey
	 *            the public key
	 * @param posx
	 *            the posx
	 * @param posy
	 *            the posy
	 */
	public SendRSAKey(byte[] planeID, int posx, int posy, int keySize,
			int modulusLength, byte[] modulus, int publicKeyLength,
			byte[] publicKey) {

		super(planeID, publicKey.length, posx, posy);
		priority = PRIORITY;
		type = MessageType.SENDRSA;
		this.keySize = keySize;
		this.modulusLength = modulusLength;
		this.modulus = modulus;
		this.publicKey = publicKey;
		publicKeyLength = publicKey.length;
		keyPair = new KeyPair(new BigInteger(modulus), new BigInteger(publicKey), null, keySize);

	}

	/**
	 * Redefine the method sendMessage from the Class Message.
	 * Not used (Implemented in Plane).
	 */
	@Override
	public void sendMessage(ByteArrayOutputStream byteArrayOutputStream) {
		// TODO Auto-generated method stub

	}

	/**
	 * Redefine the method accept from the Class Message.
	 * This method allows an object MessageHanadlerVisitor to visit
	 * a SendRSAKey message.
	 * 
	 * @ param visitor an object MessageHandlerVisitor
	 */
	@Override
	public void accept(MessageHandlerVisitor visitor) {

		visitor.visit(this, null, null);
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
		stringArray[4][0] = "KeySize";
		stringArray[4][1] = String.valueOf(keySize);
		stringArray[5][0] = "KeySize";
		stringArray[5][1] = String.valueOf(keySize);
		stringArray[6][0] = "ModulusLength";
		stringArray[6][1] = String.valueOf(modulusLength);
		stringArray[7][0] = "PublicKeyLength";
		stringArray[7][1] = String.valueOf(publicKeyLength);
				
		return stringArray;
	}

	public KeyPair getKeyPair() {
		return keyPair;
	}
}
