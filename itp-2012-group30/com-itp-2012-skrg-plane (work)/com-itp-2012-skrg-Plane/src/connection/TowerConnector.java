package connection;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import Plane.Plane;
import Plane.PlaneHandler;

public class TowerConnector {

	private int port;
	private String host;
	private Socket socket;
	private Plane plane;

	/** The created connection. */
	boolean createdConnection = false;

	public TowerConnector(String host, int port, Plane plane) {

		this.host = host;
		this.port = port;
		this.plane = plane;
		initializeConnection();
	}

	/**
	 * Accepts a connection on a given port. Firstly a ServerSocket is used to
	 * accept the connection and when the connection is accepts a new socket
	 * called client is used for communication to the client.
	 * 
	 * @return return the client socket so that communication can begin
	 */
	public void initializeConnection() {

		try {

			socket = new Socket("localhost", port);
			new PlaneHandler(socket, plane);

		} catch (ConnectException e) {

			System.out.println("No Connection could be establised on " + host
					+ " at port " + port);

		} catch (IOException e) {

			e.printStackTrace();

		}
	}
}
