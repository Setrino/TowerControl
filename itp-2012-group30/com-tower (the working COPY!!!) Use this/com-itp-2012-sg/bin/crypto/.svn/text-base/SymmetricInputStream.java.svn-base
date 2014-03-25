/*
 * @author Sergei Kostevitch
 */
package crypto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

// TODO: Auto-generated Javadoc
/**
 * The Class SymmetricInputStream.
 * Extends class InputStream and redefine method read() and clone()
 */
public class SymmetricInputStream extends InputStream {

	/** The dataInputStream decrypted. */
	private InputStream disDecrypted;
	
	/** The inputStream. */
	private InputStream in;
	
	/** The symmetric keys. */
	private byte[] symmetricKeys;
	
	/** The index. */
	int index = 0;

	/**
	 * Instantiates a new symmetric input stream.
	 *
	 * @param myKey the my key
	 * @param dis the dis
	 */
	public SymmetricInputStream(byte[] myKey, ByteArrayInputStream dis) {
		this.symmetricKeys = myKey;
		this.in = dis;
	}

	/**
	 * Redifine method read() of InputStream
	 * Decrypte stream before returning a whole number initially crypted as XOR.
	 *
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see java.io.InputStream#read()
	 */
	public int read() throws IOException {
		int is = in.read();
		if (is == -1) {
			return -1;
		}
		is = is ^ symmetricKeys[index % symmetricKeys.length];
		index++;
		return is;
	}


	/**
	 * Redifine methode close() of InputStream
	 * Close InputStream.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see java.io.InputStream#close()
	 */
	public void close() throws IOException {
		in.close();
	}

}
