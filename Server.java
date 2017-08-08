// This class starts the server for the application
// Its functionality is to establish the server and to accept new client connections
// Each client connection is then passed on to a Client handler thread

import java.net.*;
import java.util.*;
import java.io.*;

public class Server {

	public static final int ERROR = -1;
	public static final int SERVER_PORT = 1990;
	public static ServerSocket serverSocket = null;
	private static final int maxClientsCount = 10;
	private static final Server_handler[] threads = new Server_handler[maxClientsCount];


	public static void main(String[] args) {
		//open server socket for listening
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
			System.out.println("The IRC-For-Me server has started on port " + SERVER_PORT);
			accept_clients();
		} catch (IOException e) {
			System.err.println("ERROR: Server was unable to start on port " + SERVER_PORT);
			System.exit(ERROR);
		}

	}


	public static void accept_clients() {
		//Use ArrayList to store all client socket connections
		ArrayList<Server_handler> client_list = new ArrayList<Server_handler>();
	
		//accept and client connection requests and transfer them to the client handler
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				System.out.println("The server has just accepted a new client");
			
				//check threads array
				for(int i = 0; i < maxClientsCount; i++) {
					if(threads[i] == null) {

						//get screen name
						PrintWriter name_prompt = new PrintWriter(socket.getOutputStream(), true);
                                 		BufferedReader prompt_return = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                  		name_prompt.println("Enter a screen name");
                                  		String screen_name = prompt_return.readLine();

						//start a thead to handle this client connection
						(threads[i] = new Server_handler(screen_name, socket, threads)).start();
						break;
					}
				}
			} catch(IOException e) {
				System.err.println("An error has occured in accepting new client connection");
			}
		}
	}

}
