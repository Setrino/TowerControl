/*
 * Sergei Kostevitch
 * Gabriel Gomes
 */
package crypto;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

/**
 * The KeyPair Class gives the encrypt and decrypt methods, useful for RSA.
 * 
 * @see KeyGenerator.java
 */

public class KeyPair {

	/**
	 * The e value : It's the Public Key.
	 * Here, the value 65'537 is used.
	 * */
	private static BigInteger e;
	
	/** The n value : it's the modulus. */
	private static BigInteger n;

	/** The d value : it's the Private Key. */
	private static BigInteger d;

	/** The N value : multiple of 8, indicating the size of the private key (in bits). */
	private int N;

	private String destinationPath = System.getProperty("user.home")
			+ File.separator + "Documents" + File.separator
			+ "AirPlane Control" + File.separator + "rsaKey.towerkey";

	/** Write to file to give to plane */
	private DataOutputStream dataOutputStream = null;
	private ByteArrayOutputStream byteArrayOutputStream = null;
	private FileOutputStream fileOutputStream = null;

	/**
	 * Constructor of the KeyPair class.
	 * 
	 * @param n : the modulus.
	 * 
	 * @param PublicKey : the public key.
	 * 
	 * @param PrivateKey : the private key.
	 * 
	 * @param N integer, multiple of 8, indicating the size of the private key (in bits).
	 */
	public KeyPair(BigInteger n, BigInteger PublicKey, BigInteger PrivateKey,
			int N) {

		KeyPair.n = n;
		e = PublicKey;
		d = PrivateKey;
		this.N = N;
		
		//Exception if N isn't a multiple of 8
		if (N % 8 != 0) {
			throw new IllegalArgumentException("The key size isn't a multiple of 8");
		}
	}
	
	/**
	 * Gets the private key.
	 * 
	 * @return d : the private key
	 */
	public BigInteger getPrivateKey() {
		return d;
	}

	/**
	 * Sets the private key.
	 * 
	 * @param PrivateKey : the new private key
	 */
	public void setPrivateKey(BigInteger PrivateKey) {
		d = PrivateKey;
	}

	

	/**
	 * Encrypt a message.
	 * 
	 * @param toBeEncrypted : the message to encrypt.
	 * @return the encrypted message.
	 */
	public BigInteger encrypt(BigInteger toBeEncrypted) {

		if (toBeEncrypted.equals(null)) {
			throw new NullPointerException();
		}

		return toBeEncrypted.modPow(e, n);
	}

	/**
	 * Decrypt a message.
	 * 
	 * @param encrypted : the encrypted message.
	 * @return the decrypted message.
	 */
	public BigInteger decrypt(BigInteger encrypted) {

		if (encrypted.equals(null)) {
			throw new NullPointerException();
		}

		return encrypted.modPow(d, n);
	}

	/**
	 * Gets the public key.
	 * 
	 * @return the public key
	 */
	public byte[] getPublicKey() {

		return e.toByteArray();
	}

	/**
	 * Gets the modulus.
	 * 
	 * @return the modulus
	 */
	public byte[] getModulus() {

		return n.toByteArray();
	}

	/**
	 * Gets the key size.
	 * 
	 * @return the key size
	 */
	public int getKeySize() {

		return N;
	}

	/**
	 * Hide private key.
	 */
	public void hidePrivateKey() {

		d = null;
	}

	public void writeKeyToFile() {

		File file = new File(destinationPath);
		// file.deleteOnExit()

		System.out.println(N + " " + n.toByteArray().length + " " + n + " "
				+ e.toByteArray().length + " " + e);

		byteArrayOutputStream = new ByteArrayOutputStream();

		try {
			dataOutputStream = new DataOutputStream(byteArrayOutputStream);
			fileOutputStream = new FileOutputStream(file);
			dataOutputStream.writeInt(N);
			dataOutputStream.writeInt(n.toByteArray().length);
			dataOutputStream.write(n.toByteArray());
			dataOutputStream.writeInt(e.toByteArray().length);
			dataOutputStream.write(e.toByteArray());
			dataOutputStream.close();
			fileOutputStream.write(byteArrayOutputStream.toByteArray());
			fileOutputStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
