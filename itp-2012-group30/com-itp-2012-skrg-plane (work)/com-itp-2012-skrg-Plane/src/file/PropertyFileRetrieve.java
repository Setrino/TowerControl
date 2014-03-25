/*
 * Sergei Kostevitch
 * Apr 17, 2012
 */

package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import Plane.Plane;

public class PropertyFileRetrieve {

	private Properties properties = null;
	private File file = null;
	private String planeDefault = "properties" + File.separator
			+ "planeDefault";
	
	private Plane plane = null;

	public PropertyFileRetrieve(Plane plane) {

		properties = new Properties();
		file = new File(planeDefault + ".properties");
		this.plane = plane;
		readProperties();
	}

	public void readProperties() {

		try {
			file = new File(planeDefault + ".properties");
			properties.load(new FileInputStream(file));
			plane.setMessageTimeout(Long.parseLong(properties.getProperty("MESSAGE_TIMEOUT")));
			plane.setPlaneIdSize(Integer.parseInt(properties.getProperty("PLANE_ID_SIZE")));
			plane.setKeepAliveInterval(Long.parseLong(properties.getProperty("KEEPALIVE_INTERVAL")));
			plane.setDataInterval(Long.parseLong(properties.getProperty("DATA_INTERVAL")));
			plane.setPlaneUpdateInterval(Long.parseLong(properties.getProperty("PLANE_UPDATE_INTERVAL")));	
			plane.setTowerHost(properties.getProperty("TOWERHOST"));	
			plane.setTowerPort(Integer.parseInt(properties.getProperty("TOWERPORT")));

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void createProperties() {

		try // set the properties value
		{
			properties.setProperty("MESSAGE_TIMEOUT", "5000");
			properties.setProperty("PLANE_ID_SIZE", "8");
			properties.setProperty("KEEPALIVE_INTERVAL", "500");
			properties.setProperty("DATA_INTERVAL", "100");
			properties.setProperty("PLANE_UPDATE_INTERVAL", "16");
			properties.setProperty("TOWERHOST", "localhost");
			properties.setProperty("TOWERPORT", "6969");

			// save properties to project root folder
			properties.store(new FileOutputStream(file), null);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
