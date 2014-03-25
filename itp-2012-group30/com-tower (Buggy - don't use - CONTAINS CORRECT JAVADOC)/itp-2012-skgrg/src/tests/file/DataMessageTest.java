package tests.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import junit.framework.Assert;

import messages.Data;
import file.DataFile;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * Pour faire fonctionner le test il faut placer le fichier "test_image_itp.png"
 * dans un répertoire "test" dans le même dossier que "src".
 * 
 * L'implémentation utilisée ici est peut-être différente de la vôtre, il faudra
 * donc modifier le code pour le faire fonctionner dans votre projet.
 * 
 * La classe DataFile possède deux constructeurs :
 * 
 * -Un pour lire depuis un fichier déjà existant, qui ne prend qu'un seul
 * paramètre , le chemin vers ce fichier. -Un deuxième pour créer un fichier à
 * partir de messages Data. Son constructeur prend deux paramètres : le chemin
 * du nouveau fichier, et le premier message Data reçu, qui contient des
 * informations utiles telles que le hash, la taille totale du fichier et le
 * format ( pour compléter le nom du fichier ).
 * 
 * @author Jeremy Gotteland
 * 
 */
public class DataMessageTest {

	/** The pieces. */
	private static LinkedList<Data> pieces;
	
	/** The data file. */
	private static DataFile dataFile;

	// Constants computed from the test file. Students have to compute
	// themselves to get to the same result.
	/** The Constant fileHash. */
	private final static byte[] fileHash = { 107, -72, -59, 15, 51, -46, 119,
			98, -90, 43, 96, -84, -78, -127, -64, 68, -128, 10, -128, 121 };
	
	/** The Constant pngFormat. */
	private final static byte[] pngFormat = { 112, 110, 103, 32 };
	
	/** The no format. */
	private final byte[] noFormat = { 32, 32, 32, 32 };

	/**
	 * Sets the up class.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@BeforeClass
	public static void setUpClass() throws IOException {

		dataFile = new DataFile("");

		pieces = generateDataList(dataFile);
		
	}

	/**
	 * Si l'extension ne contient que trois lettres, rajouter un espace pour
	 * remplir les 4 bytes obligatoires du format. Ici "png ".getBytes("ASCII")
	 * = { 112, 110, 103, 32 }
	 * 
	 */
	@Ignore
	@Test
	public void readingDataTest() {

		Assert.assertTrue(Arrays.equals(fileHash, dataFile.getHash()));
		Assert.assertTrue(Arrays.equals(pngFormat, dataFile.getFormat()));
	}

	/**
	 * L'extension n'est pas ajoutée car en temps normal on ne la connait pas.
	 * 
	 * Il faut utiliser l'attribut "format" de l'objet Data passé en paramètre
	 * dans le constructeur pour créer le fichier proprement avec la bonne
	 * extension.
	 *
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	//@Ignore
	@Test
	public void writingDataTest() throws UnsupportedEncodingException {
		
		DataFile written = new DataFile("Test_File", pieces.removeFirst());
		
		System.out.println("No of pieces " + pieces.size());
		
		//System.out.println(written.createFile(fileNamePath));
		
		System.out.println("File Name " + written.getName());
		
		written.deleteOnExit();

		for (Data block : pieces) {
			written.writeData(block);
			
			
		}

		Assert.assertTrue(written.isComplete());
		Assert.assertTrue(Arrays.equals(fileHash, written.getHash()));
		Assert.assertTrue(Arrays.equals(pngFormat, written.getFormat()));
		Assert.assertTrue(written.getName().endsWith(".png"));

	}

	/**
	 * Format de mon constructeur Data :
	 * 
	 * public Data(byte[] planeID, int continuation, int posx, int posy, byte[]
	 * hash, byte[] format,int filesize, byte[] payload)
	 * 
	 * Adaptez en fonction du vôtre.
	 * 
	 * Ce test devrait lancer une "IllegalArgumentException" car on essaye
	 * d'écrire un bloc de donnée de taille inférieure à 1024 bytes alors que ce
	 * n'est pas le dernier bloc.
	 * 
	 */
	@Ignore
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void badDataSizeTest() {

		System.out.println("Bad DataSize Test");

		int fileSize = 4097;

		Data firstDataBlock = new Data(null, 0, -1, -1, null, noFormat,
				fileSize, new byte[Data.MAX_PACKET_SIZE]);
		Data wrongDataBlock = new Data(null, 2, -1, 1, null, noFormat,
				fileSize, new byte[Data.MAX_PACKET_SIZE / 2]);

		DataFile test = new DataFile("dummy", firstDataBlock);

		test.writeData(wrongDataBlock);

		test.delete();
	}

	/**
	 * Ici aucune exception n'est levée car il s'agit du dernier bloc donc sa
	 * taille peut-être inférieure à 1024 bytes.
	 */
	@Ignore
	@Test
	public void lastPacketSizeTest() {

		int fileSize = 1536;

		Data firstDataBlock = new Data(null, 0, -1, -1, null, noFormat,
				fileSize, new byte[Data.MAX_PACKET_SIZE]);
		Data lastBlock = new Data(null, 1, -1, 1, null, noFormat, fileSize,
				new byte[Data.MAX_PACKET_SIZE / 2]);

		DataFile test = new DataFile("dummy", firstDataBlock);

		test.writeData(lastBlock);

		//test.deleteOnExit();
		test.delete();

	}

	/**
	 * 
	 * Used to get the file as a list of Data.
	 * 
	 * Reference error was corrected.
	 * 
	 * @param dataFile
	 *            which we want to split in blocks.
	 * @return chunks of the file in the form of a "Data" list.
	 * @throws IOException
	 *             in case of there is a problem reading the file
	 */
	private static LinkedList<Data> generateDataList(File dataFile)
			throws IOException {

		LinkedList<Data> pieces = new LinkedList<Data>();

		int fileSize = (int) dataFile.length();

		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(
				dataFile));

		byte[] buffer = new byte[Data.MAX_PACKET_SIZE];
		int available = 0;
		int i = 0;

		while ((available = bufferedInputStream.available()) > 0) {

			if (available < Data.MAX_PACKET_SIZE) {
				buffer = new byte[available];
			} else {
				buffer = new byte[Data.MAX_PACKET_SIZE];
			}

			bufferedInputStream.read(buffer, 0, buffer.length);

			pieces.add(new Data(null, i, -1, -1, fileHash, pngFormat, fileSize,
					buffer));
			i++;
		}
		
		System.out.println("No of pieces: " + i);

		Collections.shuffle(pieces);

		return pieces;
	}

}
