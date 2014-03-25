/* 
 * Sergei Kostevitch
 * Gabriel Rodrigues Gomes
 */

package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;

import exceptions.IllegalPacketSizeException;

import messages.Data;

// TODO: Auto-generated Javadoc
/**
 * The Class DataFile.
 */
public class DataFile extends File {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9172827055694896882L;

	/** The hash. */
	private byte[] hash;

	/** The piece counter. */
	private int pieceCounter = 0;
	// Check whether received Piece or not
	/** The verify packets. */
	private boolean[] verifyPackets;

	/** The length last packet. */
	private int lengthLastPacket;

	/** The size. */
	private int size;

	/** The output file. */
	private File outputFile;

	/** The random access file. */
	RandomAccessFile randomAccessFile;

	/** The format. */
	private byte format[] = { 0, 0, 0, 0 };

	private boolean allowedToTweet = false;
	
	private String fileNamePath = null;

	/**
	 * Instantiates a new data file.
	 * 
	 * @param fileName
	 *            the file name
	 */
	public DataFile(String fileName) {
		super(fileName);

		try {

			size = (int) this.length();
			byte[] previous = new byte[size];
			randomAccessFile = new RandomAccessFile(this, "rws");
			randomAccessFile.readFully(previous);
			hash = DataFile.createHash(previous);
			Arrays.fill(format, (byte) 32);

			byte[] formatBis = (fileName.substring(fileName.indexOf(".") + 1,
					fileName.length())).getBytes();

			for (int i = 0; i < formatBis.length; i++) {
				format[i] = formatBis[i];
			}

			// System.out.println(new String(Arrays.toString(format)));
			construct();
			randomAccessFile.close();
		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String readPlaneName(){
		
		String planeName = null;
		String tempString = null;
		int endingNumber = 0;
		
		
		Scanner scanner = null;
		FileInputStream fileInputStream = null;
		
		try {
			fileInputStream = new FileInputStream(fileNamePath);
			scanner = new Scanner(fileInputStream);
			
			tempString = scanner.nextLine();
			endingNumber = tempString.indexOf(";");
			
			planeName = tempString.substring(11, endingNumber).trim();
						
			scanner.close();
			fileInputStream.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e){
			e.printStackTrace();
			
		}
		
		return planeName;
	}

	// Creating a file based on its file Name

	/**
	 * Instantiates a new data file.
	 * 
	 * @param fileNamePath
	 *            the file name path
	 * @param data
	 *            the data
	 */
	public DataFile(String fileNamePath, Data data) {
		super(fileNamePath + "." + new String(data.getFormat()).trim());

		this.fileNamePath = fileNamePath + "." + new String(data.getFormat()).trim();
		createFile(fileNamePath);
		format = data.getFormat();
		size = data.getFileSize();
		hash = data.getHash();
		construct();
		this.writeData(data);
	}

	/**
	 * Create a file from MainPath, planeIDName and fileName if file doesn't
	 * exist.
	 * 
	 * @param fileNamePath
	 *            the file name path
	 */
	public void createFile(String fileNamePath) {

		File f = new File(fileNamePath);

		if (!f.exists()) {

			outputFile = new File(fileNamePath);
			outputFile.getAbsoluteFile();
		}

		System.out.println("Created File at " + fileNamePath);

	}

	/**
	 * Construct.
	 */
	private void construct() {

		verifyPackets = new boolean[(int) Math.ceil((double) size
				/ Data.MAX_PACKET_SIZE)];
		lengthLastPacket = (int) ((size % Data.MAX_PACKET_SIZE == 0) ? Data.MAX_PACKET_SIZE
				: size % Data.MAX_PACKET_SIZE);
	}

	/**
	 * Gets the index.
	 * 
	 * @param i
	 *            the i
	 * @param data
	 *            the data
	 * @return the index
	 * @throws IllegalPacketSizeException
	 *             the illegal packet size exception
	 */
	public int getIndex(int i, Data data) throws IllegalPacketSizeException {

		if ((i == -1) && data.getPayload().length == lengthLastPacket) {

			return 0;
		}
		if (data.getPieceIndex() == verifyPackets.length - 1
				&& data.getPayload().length == lengthLastPacket) {

			return data.getPieceIndex();
		}

		else if ((data.getPieceIndex() == verifyPackets.length - 1 && data
				.getPayload().length != lengthLastPacket)
				| (data.getPayload().length != Data.MAX_PACKET_SIZE && data
						.getPieceIndex() != verifyPackets.length - 1)) {

			throw new IllegalPacketSizeException(data);
		}

		return i;
	}

	/**
	 * Write data.
	 * 
	 * @param data
	 *            the data
	 */
	public void writeData(Data data) {
		try {
			randomAccessFile = new RandomAccessFile(this, "rws");
		}

		catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}

		int index = getIndex(data.getPieceIndex(), data);

		try {
			randomAccessFile.seek((long) (index * Data.MAX_PACKET_SIZE));
			System.out.println(Arrays.toString(data.getPayload()));
			randomAccessFile.write(data.getPayload(), 0,
					data.getPayload().length);
			randomAccessFile.close();
		}

		catch (IOException e) {
			e.printStackTrace();
		}

		this.verifyPackets[index] = true;
		//System.out.println("Piece total received " + pieceCounter + " writing "
		//		+ "to index " + index + " value " + verifyPackets[index]);
		pieceCounter++;
	}

	/**
	 * Gets the packet.
	 * 
	 * @param i
	 *            the i
	 * @param length
	 *            the length
	 * @return an array of bytes stocking the hash in SHA-1 of the initial
	 *         array.
	 */

	public byte[] getPacket(int i, int length) {
		byte[] data = new byte[length];
		try {
			this.randomAccessFile.seek(1024 * i);
			this.randomAccessFile.readFully(data);
		}

		catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

	/**
	 * Creates the hash.
	 * 
	 * @param data
	 *            the data
	 * @return the byte[]
	 */
	public static byte[] createHash(byte[] data) {
		MessageDigest shaDigest;
		byte[] hash = null;
		try {
			shaDigest = MessageDigest.getInstance("SHA-1");
			shaDigest.update(data);
			hash = shaDigest.digest();
		}

		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return hash;
	}

	/**
	 * Tests the hash file saved on the disk and the initial hash file. If there
	 * is a mistake, the file is deleted.
	 * 
	 * @return true if the files are both the same.
	 * 
	 */

	public synchronized boolean verify() {

		byte[] digestionSHA = null;
		byte[] data = null;

		try {
			randomAccessFile = new RandomAccessFile(this, "rws");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (this.exists() && this.length() != 0) {

			construct();

			for (int i = 0; i < verifyPackets.length; i++) {

				if (i == (verifyPackets.length - 1)) {

					data = getPacket(i, lengthLastPacket);
				} else {

					data = getPacket(i, Data.MAX_PACKET_SIZE);
				}

				digestionSHA = createHash(data);

				if (Arrays.equals(hash, digestionSHA)) {

					verifyPackets[i] = true;

				} else {
					System.out.println(hash.toString());
					System.out.println(digestionSHA.toString());
					return false;
				}
			}

			return true;
		}

		else {
			return false;
		}
	}

	/**
	 * Checks if is complete.
	 * 
	 * @return true, if is complete
	 */
	public boolean isComplete() {

		for (int i = 0; i < this.verifyPackets.length; i++) {
			if (!verifyPackets[i]) {
				System.out.println("Not compelete packet at index : " + i);
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets the hash.
	 * 
	 * @return the hash
	 */
	public byte[] getHash() {
		return hash;
	}

	/**
	 * Gets the format.
	 * 
	 * @return the format
	 */
	public byte[] getFormat() {
		return format;
	}

	public boolean testFileTypeToTweet() {

		String formatString = new String(format).trim();

		if (formatString.equalsIgnoreCase("png") || formatString.equalsIgnoreCase("jpeg")
				|| formatString.equalsIgnoreCase("bmp") || formatString.equalsIgnoreCase("jpg")) {
			allowedToTweet = true;
		} else {
			allowedToTweet = false;
		}

		return allowedToTweet;
	}

}