/**
* This is a server-side thread-based event handler 
* This class implements the functionality of the server for each client connection
* The primary functions are getting messages from the clients and sending messages to the clients
*
* The IRC protocol commands implemented in this application are:
* 
* /HELP - shows a general list of commands to client -- implemented
* /JOIN - enables client to join a channel -- implemented
* /LIST - shows a list of all current channels -- implemented
* /LEAVE - enables client to leave a channel
* /QUIT - enables client to exit IRC-for-me application
* /NICK - enables client to change its screen name -- implemented
* /AWAY - enables client to create an away message -- implemented
* /WHOIS - displays information about a client in the channel
* /KICK - enables channel owner to kick out a client from the channel
* /TOPIC - enables channel owner to change the topic of the channel 
*
**/

import java.util.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server_handler extends Thread{
        public static final int ERROR = 1;

	/**
	* Class variables
	**/
	private String name;
	private Socket socket;
	private PrintWriter server_output;
	private BufferedReader client_input;
	private Boolean is_away;

	/**
	* Channel bookkeeping
	**/
	private ArrayList<Server_channel> channels;
	private Server_channel current_channel;	
	
	/**
	* Constructor method 
	**/
	public Server_handler(Socket socket) {
		this.socket = socket;
		this.channels = new ArrayList<Server_channel>();
		Server.add_client(this);
		this.is_away = false;
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

	public void set_current_channel(Server_channel new_channel) {
		this.current_channel = new_channel;
		channels.add(new_channel);
	}
	
	public PrintWriter getWriter() {
		return server_output;
	}

	public String get_name() {
		return name;
	}

	/**
	* Standard run method to start thread functionality
	**/
	public void run() {
		System.out.println("client" + this.name + "is running");
		
		//show client list
		ArrayList<Server_handler> client_list = Server.get_list();
		for(int i = 0; i < client_list.size(); i++) {
			System.out.println(client_list.get(i));
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
					HELP();
				} else if (input.startsWith("/JOIN")) {
					JOIN(input);
				} else if (input.startsWith("/LIST")) {
					LIST();
				} else if (input.startsWith("/NICK")) {
					NICK();
				} else if (input.startsWith("/AWAY")) {
					AWAY();
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

	// The HELP() function sends delineated list of the IRC commands back to the client
	public void HELP() {
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
	
	// The JOIN() function accomplishes two functions: 
	// 1) Scans list of channel request and allows client to join active channel or 
	// 2) If no such channel request exists a new channel is created and the requesting client is designated as the channel operator 
	public void JOIN(String input) {
		synchronized (this) {
			// Get name for channel request from client
			String channel_request = input;
			if (channel_request.length() > 6) {
				channel_request = channel_request.substring(6).trim();
			} else {
				Self_message("Please try again with the format /JOIN <name>");
				return;
			}
	
			ArrayList<Server_channel> all_channels = Server.get_all_irc_channels();

			//check if channel already exists
			for(int j = 0; j < all_channels.size(); j++) {
				//if channel exists, join the channel
				//String channel_name = all_channels.get(j).channel_name;
				//if(channel_name.equals(channel_request)) {
				Server_channel channel = all_channels.get(j);
				if(channel.get_channel_name().equals(channel_request)) {
					channel.join_list(this);
					set_current_channel(channel);
					//all_channels.get(j).join_list(this);
					//set_current_channel(all_channels.get(j));
					Self_message("Found a match, already existing channel");
					return;
				}
			}

			// If channel does not exit, create a new channel
			Server_channel new_channel = new Server_channel(channel_request, this);
			set_current_channel(new_channel);
		}
	}

	// sends a list of all of the acitve channels on the irc back to the client
	public void LIST() {
	   synchronized (this) {
		//get the list of all the active channels from the Server
		ArrayList<Server_channel> list = Server.get_all_irc_channels();

		// Check if there are active channels, if there are no active channels tell the client that, 
		// Otherwise, send back the list of active channels
		if (list.size() != 0) {
			Self_message("IRC-FOR-ME channels: ");
			for (int i = 0; i < list.size(); i++) {
				String channel_name = list.get(i).get_channel_name();
				Self_message(channel_name);
			}	
		} else {
			Self_message("There are currently no active channels on IRC-FOR-ME");
		}
	   }			
	}

	// Changes your screen name
	public void NICK() {
		set_name();
	}

	// presents an away message
        // Flips your status from its previous status
	// If you were active you will now be away, if you were away you will now be active 
	public void AWAY() {
		this.is_away = (!is_away);
	}

	/**
	* Message sending functions
	**/

	// Send message to all clients
	public void Broadcast(String message) {
		synchronized (this) {
		//	ArrayList<Server_handler> client_list;
		//	ArrayList<Server_handler> broadcast_recipients;
		//	Server_channel the_senders_current_channel = this.current_channel;

			if (this.current_channel != null) { 
				ArrayList<Server_handler> broadcast_recipients = this.current_channel.get_channel_list();
	                         for(int i = 0; i < broadcast_recipients.size(); i++) {
					Server_handler client = broadcast_recipients.get(i);
                                	if ((client != this) && (client.current_channel == this.current_channel)) {
						if (client.is_away) {
							this.getWriter().println("I am currently away from my computer.\n" +
										 "Pleave leave a message");
						} else {
				     	 		client.getWriter().println(this.name + message);
                                 		}
					}       
                        	 }	 			
			} else {
			//	client_list = Server.get_list();
				Self_message("You are currently not in an irc-channel");
				Self_message("To proceed, please join a channel");
			}
	
		}
	}

	// Send message back to self
	public void Self_message(String message) {
		server_output.println(message);
	}
}
