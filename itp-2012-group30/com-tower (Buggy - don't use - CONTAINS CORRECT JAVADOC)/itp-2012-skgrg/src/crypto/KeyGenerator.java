/*
 * Sergei Kostevitch
 * Gabriel Gomes
 */
package crypto;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * The Class KeyGenerator is used to instantiate a KeyPair object.
 * To be more precise, it generates in particular the values of p and q,
 * useful for RSA.
 * 
 * @see KeyPair.java
 */

public class KeyGenerator {

	/** The Constant RANDOM. */
	private final static SecureRandom RANDOM = new SecureRandom();

	/**
	 * The e value : It's the Public Key.
	 * Here, the value 65'537 is used.
	 * */
	private static BigInteger e;
	
	/** The n value : it's the modulus. */
	private static BigInteger n;

	/** The d value : it's the Private Key. */
	private static BigInteger d;

	/** The p value. */
	private static BigInteger p;
	
	/** The q value. */
	private static BigInteger q;
	
	/** The phi value = (p-1)*(q-1). */
	private static BigInteger phi;

	/**
	 * Main method of the class generating attributes for RSA
	 * and stocking them in a new KeyPair object.
	 *
	 * @param N integer, multiple of 8, indicating the size of the private key (in bits).
	 * @return a KeyPair object
	 */
	public static KeyPair generateRSAKeyPair(int N) {

		
			if (N % 8 != 0) {
				
				throw new IllegalArgumentException();
				
			} else if (N < 8) {
					
				throw new ArithmeticException();
			}else{

					do {
						p = BigInteger.probablePrime(N / 2, RANDOM);
						q = BigInteger.probablePrime(N / 2, RANDOM);
					} while (p.gcd(q).intValue() != 1);
					// The loop continues as the pgcd of p and q isn't equal to 1
					
					phi = (p.subtract(BigInteger.ONE)).multiply(q
							.subtract(BigInteger.ONE)); // phi = (p-1)*(q-1)
					n = p.multiply(q);
					e = new BigInteger("65537");
					d = e.modInverse(phi);
				}

		return new KeyPair(n, e, d, N);
	}
}