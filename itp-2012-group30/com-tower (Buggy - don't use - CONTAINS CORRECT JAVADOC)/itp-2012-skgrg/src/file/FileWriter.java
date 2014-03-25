/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package file;

// TODO: Auto-generated Javadoc
/**
 * The Class FileWriter.
 */
public class FileWriter {

	/**
	 * Instantiates a new file writer.
	 */
	public FileWriter(){
		
		XMLFileCreator file = XMLFileCreator.getXMLFileCreatorInstance();
		file.checkFilePresenceAndWriteMessageToFile("HELLO", "ALYOUPO", "Tower", "4");
	}
}
