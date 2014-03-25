/*
 * @author Sergei Kostevitch
 */
package crypto;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import crypto.KeyPair;

// TODO: Auto-generated Javadoc
/**
 * The Class RSAInputStream. Extends class InputStream and redifine method
 * read() and close()
 */
public class RSAInputStream extends InputStream {

	/** The DataInputStream. */
	private DataInputStream dataInputStream;

	/**
	 * The RSA Key.
	 */
	private KeyPair keyRSA;

	/**
	 * Instantiates a new RSAInputStream.
	 *
	 * @param pair the pair
	 * @param inputStream the input stream
	 */
	public RSAInputStream(KeyPair pair, InputStream inputStream) {
		this.keyRSA = pair;
		this.dataInputStream = new DataInputStream(inputStream);
	}

	/**
	 * Redifine method read() of InputStream Decrypt stream before retourning
	 * the whole Number initially crypted by RSA.
	 *
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see java.io.InputStream#read()
	 */

	@Override
	public int read() throws IOException {

		byte[] inArray = new byte[((keyRSA.getKeySize() / 8 + 1))];
		try {
			dataInputStream.readFully(inArray);
			BigInteger bi = new BigInteger(inArray);
			BigInteger biDecrypted = keyRSA.decrypt(bi);
			int symmetricKeyDecrypted = biDecrypted.intValue();

			return symmetricKeyDecrypted;
		} catch (EOFException e) {
			return -1;
		}
	}
	
	/**
	 * Redifine simple InputStream using keypair decrypt
	 * method and write it in the b byte tables.
	 *
	 * @param b the b
	 * @return i It's the real number of read bytes.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public int read(byte[] b) throws IOException {
		
		int noOfReadBytes = 0;
		while (dataInputStream.available() != 0 && noOfReadBytes < b.length) {
			b[noOfReadBytes] = (byte) (this.read());
			noOfReadBytes++;
		}
		return noOfReadBytes;
	}
	
	/**
	 * Redifine skip() to skip non-encrypted input stream.
	 *
	 * @param arg0 the arg0
	 * @return noOfBytes skipped
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public long skip(long arg) throws IOException {
		return dataInputStream.skip(arg);
	}

	/**
	 * Redifine method close() of InputStream Close InputStream.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see java.io.InputStream#close()
	 */
	@Override
	public void close() throws IOException {
		dataInputStream.close();
	}
}
