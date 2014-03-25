/*
 * Sergei Kostevitch
 * May 2, 2012
 */

package file;

import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import messages.MoveType;
import routing.Circular;
import routing.Coordinate;
import routing.Straight;

public class CoordinatesFile {

	private static final long serialVersionUID = -4225550980079655496L;

	private static CoordinatesFile instanceCoordinatesFile = null;

	public final static int WIDTH = 1109;
	public final static int HEIGHT = 751;

	private String[] tokens;

	private MoveType moveType = null;

	public static synchronized CoordinatesFile getCoordinatesFileInstance() {

		if (instanceCoordinatesFile == null) {

			instanceCoordinatesFile = new CoordinatesFile();
		}

		return instanceCoordinatesFile;
	}

	private CoordinatesFile() {

	}

	public List<Coordinate> readFromFile(String fileName) {

		List<Coordinate> tempListOfCoordinate = new LinkedList<Coordinate>();
		File file = new File(fileName);
		Scanner scanner = null;
		FileInputStream fileInput = null;
		int noOfLinesToRead = 0;

		try {
			fileInput = new FileInputStream(file);
			scanner = new Scanner(fileInput);

			scanner.useDelimiter(", ");
			
			noOfLinesToRead = Integer.parseInt(scanner.nextLine());

			for (int i = 0; i < noOfLinesToRead; i++) {

				tokens = scanner.nextLine().split(", ");

				moveType = MoveType.valueOf(tokens[0]);

				switch (moveType) {

				case STRAIGHT:

					tempListOfCoordinate.add(new Straight(Double
							.parseDouble(tokens[1]), Double
							.parseDouble(tokens[2])));

					break;

				case CIRCULAR:

					tempListOfCoordinate.add(new Circular(Double
							.parseDouble(tokens[1]), Double
							.parseDouble(tokens[2]), Double
							.parseDouble(tokens[3])));

					break;
				}
			}

			scanner.close();
			fileInput.close();
			file = null;

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tempListOfCoordinate;
	}
}
