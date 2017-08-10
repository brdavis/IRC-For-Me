/**
* This is a server-side thread-based event handler 
* This class implements the functionality of the server for each client connection
* The primary functions are getting messages from the clients and sending messages to the clients
**/

import java.util.*;
import java.net.*;
import java.io.*;

public class Server_handler extends Thread{

	/**
	* Class variables
	**/
	public static final int ERROR = 1;

	private String name;
	private Socket socket;
	private PrintWriter server_output;
	private BufferedReader client_input;
	private int client_id;

	/**
	* Constructor method - setter
	**/
	public Server_handler(String name, Socket socket, int id) {
		this.name = "< " + name + " > ";
		this.socket = socket;
		this.client_id = id;
		Server.add_client(this, id);
	}
	
	/**
	* Getter methods
	**/
	public PrintWriter getWriter() {
		return server_output;
	}

	public int get_client_id() {
		return client_id;
	}

	/**
	* Standard run method to start thread functionality
	**/
	public void run() {
		System.out.println("client" + this.name + "is running");
		
		// show client list
		Server_handler[] client_list = Server.get_list();
		for (int i = 0; i < client_list.length; i++) {
			System.out.println(client_list[i]);
		}

		//relay messages
		try {
			server_output = new PrintWriter(socket.getOutputStream(), true);
			client_input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while(!socket.isClosed()) {
				String input = client_input.readLine();
				Broadcast(input);	
			} 
		} catch (IOException e) {
			System.out.println("ERROR: Cannot send or recieve messages");
			System.exit(ERROR);
		}
	}

	/**
	* Messaging functions
	**/

	// Send message to all clients
	public void Broadcast(String message) {
		synchronized (this) {
			Server_handler[] client_list = Server.get_list();
 		
			for (int i = 0; i < client_list.length; i++) {
 				if((client_list[i] != null) && (client_list[i] != this)) {
					client_list[i].getWriter().println(this.name + message);
				}	
 			}
		}
	}

	// Send message back to self
	public void Echo(String message) {
		server_output.println(this.name + message);
	}

	
}
