// This is a server-side thread-based event handler 
// This class implements the functionality of the server for each client connection
// The primary functions are: getting messages from the clients, and sending messages to the clients

import java.util.*;
import java.net.*;
import java.io.*;

public class Server_handler extends Thread {

	public static final int ERROR = 1;
	private Socket socket;
	private String name;
	private PrintWriter server_output;
	private BufferedReader client_input;
	private final Server_handler[] threads;
	private int maxClientsCount;

	public Server_handler(String name, Socket socket, Server_handler[] threads) {
		this.name = "< " + name + " > ";
		this.socket = socket;
		this.threads = threads;
		maxClientsCount = threads.length;
	}


	@Override
	public void run() {
		//update the client threads list so as to have a record of all active clients
		int maxClientsCount = this.maxClientsCount;
		Server_handler[] threads = this.threads;

		System.out.println("I am running");
		//relay messages
		try {
			server_output = new PrintWriter(socket.getOutputStream(), true);
			client_input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while(!socket.isClosed()) {
				String input = client_input.readLine();
				//echo message back to user
				System.out.println(name + input);
				server_output.println(name + input);	
			} 
		} catch (IOException e) {
			System.out.println("ERROR: Cannot send or recieve messages");
			System.exit(ERROR);
		}
	}

	public void Broadcast(String message) {
		for(Server_handler thread: threads) {
			thread.getWriter().println(message);
		}
	}

	public PrintWriter getWriter() {
		return server_output;
	} 

}
