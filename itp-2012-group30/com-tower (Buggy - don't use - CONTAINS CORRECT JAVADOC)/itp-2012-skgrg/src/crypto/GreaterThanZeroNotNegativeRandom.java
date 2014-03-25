/*
 * Sergei Kostevitch
 * Mar 13, 2012
 */

package crypto;

import java.util.Random;

/**
 * The Class GreaterThanZeroNotNegativeRandom.
 * Extends the class Random and redefines
 * the method nextBytes(byte[]).
 * 
 * @see java.util.Random#nextBytes(byte[]).
 */
public class GreaterThanZeroNotNegativeRandom extends Random {


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8705758005735745073L;

	/* (non-Javadoc)
	 * @see java.util.Random#nextBytes(byte[])
	 */
	@Override
	public void nextBytes(byte[] bytes) {

		int greaterThanZero = 0;

		for (int i = 0; i < bytes.length;) {
			do {
				greaterThanZero = nextInt();

			} while (greaterThanZero < 0);
			for (int rnd = greaterThanZero, n = Math.min(bytes.length - i, 4); n-- > 0; rnd >>= 8)
				bytes[i++] = (byte) rnd;
		}
	}
}
