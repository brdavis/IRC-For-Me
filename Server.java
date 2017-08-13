/**
* This class starts the server for the application
* Its functionality is to establish the server and to accept new client connections
* Each client connection is then passed on to a Client handler thread
**/

import java.net.*;
import java.util.*;
import java.io.*;
import java.util.ArrayList;

public class Server {

	public static final int ERROR = -1;
	public static final int SERVER_PORT = 1990;
	public static ServerSocket serverSocket = null;

	/**
	* Bookkeeping information about client connections
	**/

	// Establish an ArrayList for tracking all clients connected to IRC-For-ME Server
	private static ArrayList<Server_handler> client_connections = new ArrayList<Server_handler>();

	//ArrayList for all active channels
	public static ArrayList<Server_channel> all_irc_channels = new ArrayList<Server_channel>();

	public static void add_irc_channel(Server_channel new_channel) {
		all_irc_channels.add(new_channel);
		for(int i = 0; i < all_irc_channels.size(); i++) {
			System.out.println(all_irc_channels.get(i).get_channel_name());
		}
	}

	public static ArrayList<Server_channel> get_all_irc_channels() {
		return all_irc_channels;
	}
	
	public static void main(String[] args) {
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
				
				System.out.println("The server has just accepted a new client request");
		
				int i = client_connections.size() + 1;
				Server_handler new_client = new Server_handler(socket, i);
				new_client.start();
				client_connections.add(new_client);
			
			} catch(IOException e) {
				System.err.println("An error has occured in accepting new client connection");
			}
		}
	}
	

	public static void add_client(Server_handler newest_client, int i) {
		client_connections.add(newest_client);
	}

	public static ArrayList<Server_handler> get_list() {
		return client_connections;
	}

}
