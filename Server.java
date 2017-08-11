/**
* This class starts the server for the application
* Its functionality is to establish the server and to accept new client connections
* Each client connection is then passed on to a Client handler thread
**/

import java.net.*;
import java.util.*;
import java.io.*;

public class Server {

	public static final int ERROR = -1;
	public static final int SERVER_PORT = 1990;
	public static ServerSocket serverSocket = null;

	/**
	* Bookkeeping information about client connections
	**/
	// All client connections
	private static final int maxClientsCount = 10;
	private static Server_handler[] client_connections = null;

	// Array for channel objects
//	private static Server_channel[] channels = null;

	public static void main(String[] args) {
		client_connections = new Server_handler[10];
		
		/**
		* Opens server socket port to listen (server comes online)
		**/
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
			System.out.println("The IRC-For-Me server has started on port " + SERVER_PORT);
			accept_clients();
		} catch (IOException e) {
			System.err.println("ERROR: Server was unable to start on port " + SERVER_PORT);
			System.exit(ERROR);
		}

	}


	/**
	* Accepts new client connection requests continuously while server port is online
	* Transfers accepted clients to the Server_handler
	**/
	public static void accept_clients() {
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				System.out.println("The server has just accepted a new client");
		
				//check for empty spot on client_connections	
				for(int i = 0; i < 10; i++) {
					if(client_connections[i] == null) {
						//start a thead to handle this client connection
						(client_connections[i] = new Server_handler(socket, i)).start();
						break;
					}
				}
			} catch(IOException e) {
				System.err.println("An error has occured in accepting new client connection");
			}
		}
	}
	

	public static void add_client(Server_handler newest_client, int i) {
		client_connections[i] = newest_client;
	}

	public static Server_handler[] get_list() {
		return client_connections;
	}

}
