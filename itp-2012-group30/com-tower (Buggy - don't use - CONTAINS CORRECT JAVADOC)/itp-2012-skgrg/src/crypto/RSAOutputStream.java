/*
 * @author Sergei Kostevitch
 */
package crypto;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;


// TODO: Auto-generated Javadoc
/**
 * The Class RSAOutputStream.
 * Extends class OutputStream and redifines method write(), flush() and close()
 */
public class RSAOutputStream extends OutputStream{

	/** The out. */
	private DataOutputStream dataOutputStream;
	
	/** The key RSA. */
	private KeyPair keyRSA;

	/** The random. */
	private GreaterThanZeroNotNegativeRandom random = new GreaterThanZeroNotNegativeRandom(); 
	
	/**
	 * Instantiates a new RSAOutputStream.
	 *
	 * @param keyRSA the key rsa
	 * @param outputStream the output stream
	 */
	public RSAOutputStream(KeyPair keyRSA, OutputStream outputStream) {
		this.keyRSA = keyRSA;
		dataOutputStream = new DataOutputStream(outputStream);

	}

	
	/**
	 * Redifine method write of OutputStream
	 * Encrypt a whole Number before sending to Stream.
	 *
	 * @param b the b
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		byte[] outArray = new byte[((keyRSA.getKeySize() / 8 + 1))];
		b = b & 0xFF;
		BigInteger bi = BigInteger.valueOf(b);
		BigInteger biEncrypted = keyRSA.encrypt(bi);
		byte[] disEncrypted = biEncrypted.toByteArray();
		for (int i = 0; i < disEncrypted.length; i++) {
			outArray[i + outArray.length - disEncrypted.length] = disEncrypted[i];
		}
		
		
		dataOutputStream.write(outArray);
	}
	
	// Get the modulus n to check maxNumber of blocksWe can use
	/**
	 * Padding.
	 *
	 * @param bigInteger the big integer
	 * @param outArray the out array
	 * @return the big integer
	 */
	public BigInteger padding(BigInteger bigInteger, byte[] outArray){
		
		int blockSize = (bigInteger.bitLength() - 1) / 8;
		byte[] bufferArray = new byte[blockSize - 2];
		byte[] padding = new byte[blockSize - 1 - bufferArray.length];
		random.nextBytes(padding);
		BigInteger bigIntegerPadded;
		BigInteger toOutputStreamBigInteger;
		byte[] paddedArray = new byte[blockSize + 1];
		//Randomly chosen number from padded Array from 0 to whatever size
		int intermediate = random.nextInt(padding.length);
		//Number difference of encrypted BigInteger compared to modulus
		int differenceBigInteger;
		for (int i = 0; i < bufferArray.length; i++) {
			
			bufferArray[i] = outArray[i];
		}
		
		for (int i = 0; i < paddedArray.length; i++) {
			
			paddedArray[i] = (byte) ((byte) 0 | padding[intermediate] | (byte) 0 | bufferArray[i]); 
		}
		
		bigIntegerPadded = new BigInteger(paddedArray);
		toOutputStreamBigInteger = keyRSA.encrypt(bigIntegerPadded);
		
		
		differenceBigInteger = (bigInteger.bitLength() / 8 + 1) - toOutputStreamBigInteger.bitLength();
		if (differenceBigInteger == 0) {
			
			toOutputStreamBigInteger.shiftRight(differenceBigInteger);
		}
		
		return toOutputStreamBigInteger;
	
	}
	
	/**
	 * This method override write(byte[] b) method of output stream class to
	 * write byte table in the correct and non-encrypted output stream.
	 *
	 * @param bytes the bytes
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void write(byte[] bytes) throws IOException {
		for (byte b : bytes) {
			this.write(b & 0xFF);
		}
	}


	/**
	 * Redifinie method flush() of OutputStream.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.	 *
	 * @see java.io.OutputStream#close()
	 */
	@Override
	public void flush() throws IOException {
		dataOutputStream.flush();
	}

	/**
     * Redifinie method close() of OutputStream
	 * Ferme l'OutputStream.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.	 *
	 * @see java.io.OutputStream#close()
	 */
	@Override
	public void close() throws IOException {
		dataOutputStream.close();
	}
}
