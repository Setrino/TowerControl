/*
 * Sergei Kostevitch
 * Mar 6, 2012
 */

package file;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLFileCreator.
 */
public class XMLFileCreator extends Observable {

	/** The root. */
	private Element root;

	/** The destination path. */
	private String destinationPath = System.getProperty("user.home")
			+ File.separator + "Documents" + File.separator + "AirPlane Control"
			+ File.separator + "messagesLog.xml";

	/** The instance xml. */
	private static XMLFileCreator instanceXML = null;

	/** The document builder factory. */
	private DocumentBuilderFactory documentBuilderFactory;

	/** The document builder. */
	private DocumentBuilder documentBuilder;

	/** The document. */
	private Document document;

	/** The file. */
	private File file;

	private SimpleDateFormat format = null;

	// Setting up Singleton Design Pattern for this class

	/**
	 * Gets the xML file creator instance.
	 * 
	 * @return the xML file creator instance
	 */
	public static synchronized XMLFileCreator getXMLFileCreatorInstance() {

		if (instanceXML == null) {

			instanceXML = new XMLFileCreator();
		}

		return instanceXML;
	}

	/**
	 * Instantiates a new XML file creator.
	 */
	private XMLFileCreator() {

		try {

			// Create Empty XML

			documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentBuilderFactory.newDocumentBuilder();

			file = new File(destinationPath);

			if (!file.exists()) {

				file = new File(System.getProperty("user.home")
						+ File.separator + "Documents" + File.separator + "AirPlane Control");
				file.mkdir();

				if (!file.exists()) {
					file.mkdirs();
				}

				file = new File(destinationPath);

				document = documentBuilder.newDocument();
				document.setXmlStandalone(true);
				root = document.createElement("MessagesLog");
				document.appendChild(root);

			} else if (file.exists()) {

				document = documentBuilder.parse(file);
				document.getDocumentElement().normalize();
				root = document.getDocumentElement();
			}
		} catch (Exception e) {

			e.getStackTrace();
		}

		format = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
	}

	// Create an XML Node with Message and all its details
	/**
	 * Creates the message.
	 * 
	 * @param document
	 *            the document
	 * @param rootNodeMessageLog
	 *            the root node message log
	 * @param messageType
	 *            the message type
	 * @param source
	 *            the source
	 * @param destination
	 *            the destination
	 * @param priorityLevelNumber
	 *            the priority level number
	 */
	public void createMessage(Document document, Element rootNodeMessageLog,
			String messageType, String source, String destination,
			String priorityLevelNumber) {

		Date date = new Date();
		Element message = document.createElement("Message");
		rootNodeMessageLog.appendChild(message);
		String priorityLevel = null;

		createChild(document, "Type", "id", messageType, message);
		createChild(document, "Source", source, message);
		createChild(document, "Destination", destination, message);
		createChild(document, "Date", format.format(date), message);

		switch (Integer.parseInt(priorityLevelNumber)) {
		case 0:
			priorityLevel = "high";
			break;

		case 2:
			priorityLevel = "medium";
			break;

		case 4:
			priorityLevel = "low";
			break;
		}

		createChild(document, "Priority", priorityLevel, priorityLevelNumber,
				message);
	}

	/**
	 * Creates the message.
	 * 
	 * @param messageType
	 *            the message type
	 * @param stringArray
	 *            the string array
	 * @param priorityLevelNumber
	 *            the priority level number
	 */
	public void createMessage(String messageType, String[][] stringArray,
			String priorityLevelNumber) {

		Date date = new Date();
		Element message = document.createElement("Message");
		root.appendChild(message);
		String priorityLevel = null;

		createChild(document, "Type", "id", messageType, message);
		createChild(document, "Source", stringArray[0][1], message);
		createChild(document, "Destination", stringArray[1][1], message);
		createChild(document, "Date", format.format(date), message);

		switch (Integer.parseInt(priorityLevelNumber)) {
		case 0:
			priorityLevel = "high";
			break;

		case 1:
			priorityLevel = "important";
			break;

		case 2:
			priorityLevel = "medium";
			break;

		case 3:
			priorityLevel = "medium";
			break;

		case 4:
			priorityLevel = "low";
			break;
		}

		createChild(document, "Priority", priorityLevel, priorityLevelNumber,
				message);

		for (int i = 2; i < stringArray.length; i++) {

			createChild(document, stringArray[i][0], stringArray[i][1], message);
		}
	}

	// Create XML node that has id and value
	/**
	 * Creates the child.
	 * 
	 * @param document
	 *            the document
	 * @param elementMessageName
	 *            the element message name
	 * @param idName
	 *            the id name
	 * @param valueNameAndTextName
	 *            the value name and text name
	 * @param root
	 *            the root
	 */
	public void createChild(Document document, String elementMessageName,
			String idName, String valueNameAndTextName, Element root) {

		Element child = document.createElement(elementMessageName);
		child.setAttribute(idName, valueNameAndTextName);
		root.appendChild(child);

		Text text = document.createTextNode(valueNameAndTextName);
		child.appendChild(text);
	}

	// Create XML node that doesn't have id and value
	/**
	 * Creates the child.
	 * 
	 * @param document
	 *            the document
	 * @param elementMessageName
	 *            the element message name
	 * @param elementText
	 *            the element text
	 * @param root
	 *            the root
	 */
	public void createChild(Document document, String elementMessageName,
			String elementText, Element root) {
		
		Element child = document.createElement(elementMessageName);
		root.appendChild(child);

		Text text = document.createTextNode(elementText);
		child.appendChild(text);
	}

	/**
	 * Out put xml.
	 * 
	 * @param document
	 *            the document
	 */
	public void outPutXML(Document document) {

		try {

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			StreamResult streamResult = new StreamResult(file);
			DOMSource domSource = new DOMSource(document);
			transformer.transform(domSource, streamResult);

			setChanged();

		} catch (TransformerConfigurationException e) {

			e.printStackTrace();
		} catch (TransformerException e) {

			e.printStackTrace();
		}
	}

	// Add a CloneNotSupported to insure no cloning

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {

		throw new CloneNotSupportedException();
	}

	/**
	 * Check file presence and write message to file.
	 * 
	 * @param messageType
	 *            the message type
	 * @param sourceName
	 *            the source name
	 * @param destination
	 *            the destination
	 * @param priorityLevelNumber
	 *            the priority level number
	 */
	public void checkFilePresenceAndWriteMessageToFile(String messageType,
			String sourceName, String destination, String priorityLevelNumber) {

		//System.out.println(messageType + " " + sourceName + " " + destination
	    //	+ " " + priorityLevelNumber);

		if (!file.exists()) {

			this.createMessage(document, root, messageType, sourceName,
					destination, priorityLevelNumber);
			outPutXML(this.document);

		} else if (file.exists()) {

			this.createMessage(document, root, messageType, sourceName,
					destination, priorityLevelNumber);
			outPutXML(this.document);

		}
	}

	/**
	 * Check file presence and write message to file.
	 * 
	 * @param messageType
	 *            the message type
	 * @param stringArray
	 *            the string array
	 * @param priorityLevelNumber
	 *            the priority level number
	 */
	public void checkFilePresenceAndWriteMessageToFile(String messageType,
			String[][] stringArray, String priorityLevelNumber) {

		//System.out.println(messageType + " " + stringArray[0][1] + " "
		//		+ stringArray[1][1] + " " + priorityLevelNumber);

		if (!file.exists()) {

			this.createMessage(messageType, stringArray, priorityLevelNumber);
			outPutXML(this.document);

		} else if (file.exists()) {

			// readFromXMLFile();
			this.createMessage(messageType, stringArray, priorityLevelNumber);
			outPutXML(this.document);

		}

	}

	// Read from XMl file and display data in Table
	/**
	 * Read from xml file.
	 * 
	 * @return the object[][]
	 */
	public synchronized Object[][] readFromXMLFile() {

		notifyObservers();

		NodeList nodeList = document.getElementsByTagName("Message");

		Object[][] messages = new Object[nodeList.getLength()][5];

		for (int i = 0; i < nodeList.getLength(); i++) {

			Node node = nodeList.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {

				Element element = (Element) node;

				messages[i][0] = Integer.parseInt(getNodeChildValue("Priority",
						element));
				messages[i][1] = getNodeChildValue("Type", element);
				messages[i][2] = getNodeChildValue("Source", element);
				messages[i][3] = getNodeChildValue("Destination", element);
				messages[i][4] = getNodeChildValue("Date", element);
			}

		}

		return messages;
	}

	/**
	 * Read from xml file.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @return the string[][]
	 */
	public synchronized String[][] readFromXMLFile(int rowIndex) {

		notifyObservers();

		NodeList nodeList = document.getElementsByTagName("Message");
		Node node = nodeList.item(rowIndex);
		NodeList childNodeList = node.getChildNodes();

		String[][] messageDetails = new String[(int) Math.floor(childNodeList
				.getLength() / 2)][2];
		Node nodeValue;

		if (node.getNodeType() == Node.ELEMENT_NODE) {
			// element.getTextContent()

			for (int j = 0; j < childNodeList.getLength(); j++) {

				if (!childNodeList.item(j).getNodeName().equals("#text")) {
					messageDetails[(j - 1) / 2][0] = childNodeList.item(j)
							.getNodeName();
					nodeValue = (Node) childNodeList.item(j);
					messageDetails[(j - 1) / 2][1] = nodeValue.getTextContent();
				}

			}

		}

		return messageDetails;
	}

	/**
	 * Read from xml file hello message.
	 * 
	 * @param planeID
	 *            the plane id
	 * @return the string[]
	 */
	public synchronized String[] readFromXMLFileHelloMessage(byte[] planeID) {

		String planeIDString = new String(planeID);
		NodeList nodeList = document.getElementsByTagName("Message");
		String[] helloMessage = new String[3];

		for (int i = 0; i < nodeList.getLength(); i++) {

			Node node = nodeList.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {

				Element element = (Element) node;

				String string = getNodeChildValue("Type", element);

				if (string.equals("HELLO")
						&& getNodeChildValue("Source", element).equals(
								planeIDString)) {

					helloMessage[0] = getNodeChildValue("PosX", element);
					helloMessage[1] = getNodeChildValue("PosY", element);
					helloMessage[2] = getNodeChildValue("Reserved", element);

				}
			}
		}

		return helloMessage;
	}

	// Get for a specific Message its child by name
	/**
	 * Gets the node child value.
	 * 
	 * @param childName
	 *            the child name
	 * @param element
	 *            the element
	 * @return the node child value
	 */
	public String getNodeChildValue(String childName, Element element) {

		NodeList nodeList = element.getElementsByTagName(childName).item(0)
				.getChildNodes();

		Node nodeValue = (Node) nodeList.item(0);

		return nodeValue.getNodeValue();
	}

	/**
	 * Gets the root.
	 * 
	 * @return the root
	 */
	public Element getRoot() {
		return root;
	}

	/**
	 * Gets the document.
	 * 
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}

}
