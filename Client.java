// This class starts a new client and connects it to the server
// Its primary functionality is to start the client, client handler (to send messages), and recieve messages from the server

import java.io.*;
import java.util.*;
import java.net.*;

public class Client {
	public static final int ERROR = -1;
	public static final String SERVER_HOSTNAME = "localhost";
	public static final int SERVER_PORT = 1990;

	public static void main(String[] args) {
		BufferedReader server_output = null;
		PrintWriter client_output = null;
		//connecting client to server
		try {
			//connect client to IRC-For-Me server
			Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);

			//set up two-way communication channel between client and server
			server_output = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			client_output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			System.out.println("Connected to the server sucessfully");
		} catch (IOException er) {
			System.err.println("Error: Cannot connect to server");
			System.exit(ERROR);
		}		

		//Sending messages
		//Create and start the Client_handler for sending messages
		Client_handler client_messenger = new Client_handler(client_output);
		client_messenger.setDaemon(true);
		client_messenger.start();
		
		//Recieving messages
		//this will take messages from server and print them on the user console
		try {
			String incoming_message;
			while((incoming_message = server_output.readLine()) != null) {
				System.out.println(incoming_message);
			}
		} catch(IOException ioe) {
			System.err.println("Error: Failure to connect to server");
			System.exit(ERROR);
		}
	}
}
