/**
* License Information
*
* MIT License
*
* Copyright (c) 2017 Blair Davis
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*
**/

/**
* Server Class explanation
*
* Description: The Server class starts and maintains the Server for the application
*
* Functionality:
* 1) This class starts the server for the application
* 2) The Server continuously accepts new client connection requests
* 3) Each client connection is then passed on to Server_handler class as a thread
* 
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
	*
	* 	       Bookkeeping Information
	*
	* The Server tracks all active client connections 
	*                        and
	*               all the active channels
	*
	**/

	/**
	*  ArrayList to track  all clients client connections in  IRC-For-ME application
	**/
 
	private static ArrayList<Server_handler> client_connections = new ArrayList<Server_handler>();

        /**
        * Setters for client_connections
        **/

	// Add client to list_connections list
 	public static void add_client(Server_handler newest_client) {
		client_connections.add(newest_client);
	}    

	// Remove client from connections_list
	public static void leave_application(Server_handler exiting_client) {
		int client = client_connections.indexOf(exiting_client);
		if (client != -1) {
			client_connections.remove(client);
		} else {
			System.out.println("Error: Was not able to remove client");
		}	
	}

	/**
 	* Getters for client_connections
	**/
 
 	// Get client_connections list
 	public static ArrayList<Server_handler> get_list() {
		return client_connections;
	}

	/**
	* ArrayList to track all active channels in IRC-For-ME application
	**/

	public static ArrayList<Server_channel> all_irc_channels = new ArrayList<Server_channel>();

	/**
	* Setters for all_irc_channels
	**/

	// Add channel to list
	public static void add_channel(Server_channel new_channel) {
		all_irc_channels.add(new_channel);
	}

	/**
	* Getters for all_irc_channels
	**/

	// Get all_irc_channels list
	public static ArrayList<Server_channel> get_all_irc_channels() {
		return all_irc_channels;
	}
	
	/**
	* Functionality 1: This class starts the server for the application
	**/
	public static void main(String[] args) {
		//Opens server socket port to listen (server comes online)
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
	* Functionality 2: Accepts new client connection requests continuously while server is online
	* Functionality 3: Transfers accepted clients to the Server_handler as a thread
	**/
	public static void accept_clients() {
		while(true) {
			try {
				// Accept client
				Socket socket = serverSocket.accept();
				// Transfer to Server_handler as thread
				Server_handler new_client = new Server_handler(socket);
				new_client.start();
			
			} catch(IOException e) {
				System.err.println("An error has occured in accepting new client connection");
			}
		}
	}
}
