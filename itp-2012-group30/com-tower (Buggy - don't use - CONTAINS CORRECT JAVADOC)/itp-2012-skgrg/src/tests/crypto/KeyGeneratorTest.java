
package tests.crypto;

import crypto.KeyGenerator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * The Class KeyGeneratorTest.
 *
 * @author gvero
 */
public class KeyGeneratorTest {
    
    /**
     * Instantiates a new key generator test.
     */
    public KeyGeneratorTest() {
    }

    /**
     * Sets the up class.
     *
     * @throws Exception the exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     * Tear down class.
     *
     * @throws Exception the exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
    }
    
    /**
     * Tear down.
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of generateRSAKeyPair method, of class KeyGenerator.
     */
    @Test(expected=java.lang.ArithmeticException.class)
    public void testGenerateRSAKeyPair1() {
        System.out.println("generateRSAKeyPair1");
        KeyGenerator.generateRSAKeyPair(0);
    }
    
    /**
     * Test generate rsa key pair2.
     */
    @Test(expected=java.lang.IllegalArgumentException.class)
    public void testGenerateRSAKeyPair2() {
        System.out.println("generateRSAKeyPair2");
        KeyGenerator.generateRSAKeyPair(4);
    }

        
    /**
     * Test generate rsa key pair3.
     */
    @Test
    public void testGenerateRSAKeyPair3() {
        System.out.println("generateRSAKeyPair3");
        KeyGenerator.generateRSAKeyPair(8);
    }

}
