/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package message.type;

import java.io.DataOutputStream;

import message.Message;
import message.MessageHandlerVisitor;
import message.MessageType;


// TODO: Auto-generated Javadoc
/**
 * The Class SendRSAKey.
 */
public class SendRSAKey extends Message {

	/** The priority. */
	private static final int PRIORITY = 2;

	/** The key size. */
	int keySize;

	/** The modulus. */
	byte[] modulus;

	/** The modulus length. */
	int modulusLength;

	/** The public key. */
	byte[] publicKey;

	/** The public key length. */
	int publicKeyLength;

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

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messages.Message#accept(messages.MessageHandlerVisitor)
	 */
	@Override
	public void accept(MessageHandlerVisitor visitor) {

		visitor.visit(this, null);
	}


	@Override
	public void sendMessage(DataOutputStream dataOutputStream) {
		super.sendMessage(dataOutputStream);

	}
}
