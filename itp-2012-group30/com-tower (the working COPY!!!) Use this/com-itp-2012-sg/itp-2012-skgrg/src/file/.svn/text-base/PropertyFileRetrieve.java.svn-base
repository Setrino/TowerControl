/*
 * Sergei Kostevitch
 * Apr 17, 2012
 */

package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileRetrieve {

	private Properties properties = null;
	private File file = null;
	private String planeIDName = null;;

	public PropertyFileRetrieve(String planeIDName) {

		properties = new Properties();
		this.planeIDName = planeIDName;

	}

	public void readProperties() {

		try {
			file = new File(planeIDName + ".properties");
			properties.load(new FileInputStream(file));
			properties.getProperty("MESSAGE_TIMEOUT");
			properties.getProperty("PLANE_ID_SIZE");
			properties.getProperty("KEEPALIVE_INTERVAL");
			properties.getProperty("DATA_INTERVAL");
			properties.getProperty("PLANE_UPDATE_INTERVAL");
			properties.getProperty("TOWERHOST");
			properties.getProperty("TOWERPORT");

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
