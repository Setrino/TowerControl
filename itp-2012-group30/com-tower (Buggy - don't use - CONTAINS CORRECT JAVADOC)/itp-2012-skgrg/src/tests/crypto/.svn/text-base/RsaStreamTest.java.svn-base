package tests.crypto;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

import crypto.*;

// TODO: Auto-generated Javadoc
/**
 * The Class RsaStreamTest.
 */
public class RsaStreamTest {

	/**
	 * Test.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void test() throws IOException {
		KeyPair keyPair = KeyGenerator.generateRSAKeyPair(1 << 4);
		byte[] clearText = "Hello, world!".getBytes();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		OutputStream ros = new RSAOutputStream(keyPair, new DataOutputStream(
				byteArrayOutputStream));

		ros.write(clearText);
		ros.flush();
		ros.close();

		
		DataInputStream dataInputStream = new DataInputStream(
				new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
		System.out.println(dataInputStream.available());
		InputStream ris = new RSAInputStream(keyPair, dataInputStream);


		byte[] readText = new byte[clearText.length];
		for (int i = 0; i < readText.length; ++i) {
			readText[i] = (byte) ris.read();
		}

		assertArrayEquals(clearText, readText);
	}

}
