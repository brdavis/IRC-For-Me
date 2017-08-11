/**
* This is a server-side thread-based event handler 
* This class implements the functionality of the server for each client connection
* The primary functions are getting messages from the clients and sending messages to the clients
*
* The IRC protocol commands implemented in this application are:
* 
* /HELP - shows a general list of commands to client -- implemented
* /LIST - shows a list of all current channels
* /JOIN - enables client to join a channel
* /LEAVE - enables client to leave a channel
* /QUIT - enables client to exit IRC-for-me application
* /NICK - enables client to change its screen name
* /AWAY - enables client to create an away message
* /WHOIS - displays information about a client in the channel
* /KICK - enables channel owner to kick out a client from the channel
* /TOPIC - enables channel owner to change the topic of the channel 
*
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
	* Constructor method 
	**/
	public Server_handler(Socket socket, int id) {
		this.socket = socket;
		this.client_id = id;
		Server.add_client(this, id);
	}

	/**
	* Setter methods
	**/
	public void set_name() {
		try {
			String name_prompt = "Enter your preferred screen name";
			server_output.println(name_prompt);
			String screen_name = client_input.readLine();
			this.name = "< " + screen_name + " > ";
		} catch (IOException e) {
			System.out.println("ERROR: Cannot set screen name");
			System.exit(ERROR);
		}
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

	public String get_name() {
		return name;
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
			
			set_name();

			//IRC command directory
			while(!socket.isClosed()) {
				String input = client_input.readLine();
				if (input.startsWith("/HELP")) {
					Help();
			//	} else if (input.startsWith("/NICK") {
			//		NICK();
				} else {
					Broadcast(input);
				}	
			} 
		} catch (IOException e) {
			System.out.println("ERROR: Cannot send or recieve messages");
			System.exit(ERROR);
		}
	}

	/**
	* IRC command functions
	**/

	// sends a list back to client of all the different IRC commands
	public void Help() {
		String help_menu = "HELP MENU\n" +
				   "ALL AVAILABLE IRC COMMANDS\n" +
				   "/HELP - shows a general list of commands to client\n" +
				   "/LIST - shows a list of all current channels\n" + 
				   "/JOIN - enables client to join a channel\n" + 
 			 	   "/LEAVE - enables client to leave a channel\n" +
				   "/QUIT - enables client to exit IRC-for-me application\n" +
				   "/NICK - enables client to change its screen name\n" + 
				   "/AWAY - enables client to create an away message\n" + 
				   "/WHOIS - displays information about a client in the channel\n" +
				   "/KICK- enables channel owner to kick out a client from the channel\n" +
				   "/TOPIC - enables channel owner to change the topic of the channel";

		Self_message(help_menu);
	} 
	
	// Changes the screen name of the client
//	pubic void NICK_part_one() {
//		String get_nick = "What would you like to change your name to?";
//		Self_message(get_nick); // needs to be private message to self to get response directly back to this method
//		set_name(new_nick);
//	}


	/**
	* Message sending functions
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
	public void Self_message(String message) {
		server_output.println(message);
	}
}
