/*
 * Sergei Kostevitch
 * Mar 14, 2012
 */

package plane;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class PlaneAccepter is a ServerSocket waiting for planes to connect
 * Creates a PlaneHandler when a new Plane.jar is connected
 */
public class PlaneAccepter extends Thread {

	/** The list of server sockets. */
	private  static List<ServerSocket> listOfServerSockets = new LinkedList<ServerSocket>();
	
	/** The plane handler. */
	private PlaneHandler planeHandler = null;
	
	/** The created connection. */
	boolean createdConnection = false;
	
	/** The Constant PORT. */
	private final static int PORT = 6969;

	/**
	 * Instantiates a new plane accepter.
	 *
	 * @param tower the tower
	 */
	public PlaneAccepter(){
	}

	/**
	 * Accepts a connection on a given port. Firstly a ServerSocket is used to
	 * accept the connection and when the connection is accepts a new socket
	 * called client is used for communication to the client.
	 * 
	 * @return return the client socket so that communication can begin
	 */
	public void run() {

		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(PORT);
			listOfServerSockets.add(serverSocket);

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Using the following port " + PORT
				+ " to connect");

		while (true) {

			Socket planeSocket = null;
			try {
				System.out.println("Waiting for a Plane to Connect");
				planeSocket = serverSocket.accept();
				sleep(500);

				System.out.println("GOT A CONNECTION");
				System.out.println(planeSocket);

				
				System.out.println("Plane Accepter" + " Plane Handler" + " Plane created");
				
				
				planeHandler = new PlaneHandler(planeSocket);
				planeHandler.start();
				
				

				System.out.println("Accepted");	


			}
			catch (IOException e) {

				e.printStackTrace();

			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
	}
}
