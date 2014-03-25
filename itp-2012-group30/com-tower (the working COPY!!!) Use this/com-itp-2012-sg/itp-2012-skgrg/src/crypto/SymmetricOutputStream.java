/*
 * @author Sergei Kostevitch
 */
package crypto;

import java.io.IOException;
import java.io.OutputStream;

// TODO: Auto-generated Javadoc
/**
 * The Class SymmetricOutputStream.
 * Extends class OutputStream and redifine method write(), flush() and close()
 */

public class SymmetricOutputStream extends OutputStream {

	/** The OutputStream. */
	private OutputStream outputStream;

	/** The symmetric keys. */
	private byte[] symmetricKeys;

	/** The index. */
	private int index ;

	/**
	 * Instantiates a new symmetric output stream.
	 *
	 * @param theKey the the key
	 * @param outputStream the output stream
	 */
	public SymmetricOutputStream(byte[] theKey, OutputStream outputStream) {
		this.symmetricKeys = theKey;
		this.outputStream = outputStream;
		this.index = 0;
	}


	/**
	 * Redifine method write of OutputStream
	 * Encrypt a whole number before sending on the stream.
	 *
	 * @param b the b
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see java.io.OutputStream#write(int)
	 */
	public void write(int b) throws IOException {
		int x = b ^ symmetricKeys[index % symmetricKeys.length];
		index++;
		outputStream.write(x);
	}


	/**
	 * Redifine method flush() of OutputStream.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.	 *
	 * @see java.io.OutputStream#close()
	 */
	public void flush() throws IOException {
		outputStream.flush();
	}

	/**
	 * Redifine method close() of OutputStream
	 * close() l'OutputStream.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.	 *
	 * @see java.io.OutputStream#close()
	 */
	public void close() throws IOException {
		outputStream.close();
	}

}
