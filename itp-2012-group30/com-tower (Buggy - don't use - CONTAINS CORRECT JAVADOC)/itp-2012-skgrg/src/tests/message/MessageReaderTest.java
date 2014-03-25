package tests.message;


import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import messages.Bye;
import messages.Hello;
import messages.Message;
import messages.MessageReader;
import org.junit.Test;

/**
 * The Class MessageReaderTest.
 */
public class MessageReaderTest {

	
	/** The message reader. */
	MessageReader messageReader = new MessageReader();

	
    /**
     * Test message reader with message.
     *
     * @param message the message
     */
    private void testMessageReaderWithMessage(Message message) {

    	ByteArrayOutputStream os = new ByteArrayOutputStream();
        message.sendMessage(os);
        
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        
        // Une possibilité est de faire avec une méthode statique dans une classe qu'on pourrait appeler MessageReader
        // Une autre possibilité serait de mettre la fonctionnalité de lecture dans une méthode statique de Message
        Message actual = messageReader.readMessage(is);
       
        
        assertEquals(message, actual);
    }
    
   
    /**
     * Test hello.
     */
    @Test
    public void testHello() {
    	Hello hello = new Hello("HB-45235".getBytes(), 10, 20, (byte) (1 << 4));
    	testMessageReaderWithMessage(hello);
    }
    
	
    /**
     * Test bye.
     */
    @Test
    public void testBye() {
    	Bye bye = new Bye("HB-45235".getBytes());
    	testMessageReaderWithMessage(bye);
    }
    
    // Add more tests for other message types
    
  
    /**
     * Test hello encode decode.
     */
    @Test
    public void testHelloEncodeDecode() {
    	// If your project follows the protocol specification, you should be able to decode and encode messages according to the specified format
    	byte[] messageBytes = {
    	    72, 66, 45, 52, 53, 50, 51, 53,  // Plane Id, in this case HB-45235
    	    0, 0, 0, 0,                      // Length: int = zero
    	    0, 0, 0, 0,                      // Priority: int = zero
    	    0, 0, 0, 10,                     // x coordinate: int = 10  (note that we require big endian encoding)
    	    0, 0, 0, 20,                     // y coordinate: int = 20
    	    0, 0, 0, 0,                      // MessageType: Enum = HELLO, encoded as int
    	    16                               // The "reserved" byte
    	};
        
    	InputStream is = new ByteArrayInputStream(messageBytes);
        Message actual = messageReader.readMessage(is);

        assert (actual instanceof Hello);
        Hello actualHello = (Hello) actual;

        byte[] expectedPlaneId = {72, 66, 45, 52, 53, 50, 51, 53};
        assertArrayEquals(actual.getPlaneID(), expectedPlaneId); //8 + 4 for priority
        assertEquals(actualHello.getLength(), 0); //16
        assertEquals(actualHello.getPosx(), 10); //20
        assertEquals(actualHello.getPosy(), 20); //24
        assertEquals(actualHello.getReserved(), 16);
    }
}
