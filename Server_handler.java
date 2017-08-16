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
* /LEAVE - enables client to leave a channel -- implemented
* /QUIT - enables client to exit IRC-for-me application -- implemented
* /NICK - enables client to change its screen name -- implemented
* /AWAY - enables client to create an away message -- implemented
* /WHOIS - displays information about a client in the channel-- implemented
* /KICK - enables channel owner to kick out a client from the channel
* /TOPIC - enables channel owner to view or change the topic of the channel -- implemented 
* /NAMES - shows a list of users in a channel, no channels specified shows all clients -- implemented  
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
	private String away_message;

	/**
	* Channel bookkeeping
	**/
	private ArrayList<Server_channel> current_channels_list;
	private Server_channel current_channel;	
	
	/**
	* Constructor method 
	**/
	public Server_handler(Socket socket) {
		this.socket = socket;
		this.current_channels_list = new ArrayList<Server_channel>();
		Server.add_client(this);
		this.is_away = false;
		this.away_message = "< AWAY MESSAGE > " + 
				    "I am currently away from my computer." +
				    "Pleave leave a message.";
		
	}

	/**
	* Setter methods
	**/
	public void set_name() {
		try {
			String name_prompt = "Enter your preferred screen name";
			server_output.println(name_prompt);
			String screen_name = client_input.readLine();
			this.name = "< " + screen_name + " >";
		} catch (IOException e) {
			System.out.println("ERROR: Cannot set screen name");
			System.exit(ERROR);
		}
	}

	// Set the current_channel
 	public void set_current_channel(Server_channel new_channel) {
 		this.current_channel = new_channel;
	} 

	/**
	* Getter methods
	**/

//	public void set_current_channel(Server_channel new_channel) {
//		this.current_channel = new_channel;
//	}
	
	public PrintWriter get_writer() {
		return server_output;
	}

	public String get_name() {
	//	return this.name;
		return name;
	}

	public String get_away_message() {
		return away_message;
	}

	public Server_channel get_current_channel() {
		return current_channel;
	}

	public Boolean get_is_away() {
		return is_away;
	}

	public String application_welcome_message() {
		String welcome_message = "\t\t Welcome to IRC-For-ME\n\n" +

					 "The application that allows you to chat with all your friends!\n" +
					 "To begin, let's review all of the available command in IRC-For-ME\n\n" +

                                         "\t\t ALL AVAILABLE IRC COMMANDS\n\n" +
                                         "/HELP - shows a general list of commands to client\n" +
                                         "/LIST - shows a list of all current channels\n" +
                                         "/JOIN - enables client to join a channel\n" +
                                         "/LEAVE - enables client to leave a channel\n" +
                                         "/QUIT - enables client to exit IRC-for-me application\n" +
                                         "/NICK - enables client to change its screen name\n" +
                                         "/AWAY - enables client to create an away message\n" +
                                         "/WHOIS - displays information about a client in the channel\n" +
                                         "/KICK- enables channel owner to kick out a client from the channel\n" +
                                         "/TOPIC - enables channel owner to change the topic of the channel\n" +
                                         "/NAMES - shows a list of users in a channel\n\n" +

					 "If you ever need a review of this list just type '/HELP'\n" +
					 "\tThese commands are all case sensitive\n\n" +

					 "\t\t\t ENJOY!\n\n";

		return welcome_message;  
	}

	/**
	* Standard run method to start thread functionality
	**/
	public void run() {
		//System.out.println("client" + this.name + "is running");
		
		//show client list
	//	ArrayList<Server_handler> client_list = Server.get_list();
	//	for(int i = 0; i < client_list.size(); i++) {
	//		System.out.println(client_list.get(i));
	//	}

		//relay messages
		try {
			server_output = new PrintWriter(socket.getOutputStream(), true);
			client_input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	
			Self_message(application_welcome_message());
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
				} else if (input.startsWith("/LEAVE")) {
					LEAVE(input);
				} else if (input.startsWith("/QUIT")) {
					QUIT();
				} else if (input.startsWith("/NICK")) {
					NICK();
				} else if (input.startsWith("/AWAY")) {
					AWAY(input);
				} else if (input.startsWith("/WHOIS")) {
					WHOIS(input);
				} else if (input.startsWith("/TOPIC")) {
					TOPIC(input);
				} else if (input.startsWith("/NAMES")) {
					NAMES(input);
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
				   "/TOPIC - enables channel owner to change the topic of the channel" +
				   "/NAMES - shows a list of users in a channel, no channels specified shows all clients"; 	
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
				Server_channel channel = all_channels.get(j);
				if(channel.get_channel_name().equals(channel_request)) {
					channel.join_list(this);
					set_current_channel(channel);
					current_channels_list.add(channel);
					return;
				}
			}

			// If channel does not exit, create a new channel
			Server_channel new_channel = new Server_channel(channel_request, this);
			set_current_channel(new_channel);
			current_channels_list.add(new_channel);
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

	//enables client to leave a particular channel
	public void LEAVE(String input) {
		synchronized (this) {
			// Get name of channel that the client wants to leave
			String leave_channel;
			if (input.startsWith("/LEAVE")) {
				leave_channel = input;
				if (leave_channel.length() > 7) {
					leave_channel = leave_channel.substring(7).trim();
				} else {
					Self_message("Please try again with the format /LEAVE <name>");
					return;
				}
			} else {
				leave_channel = input;
				//Self_message("leave_channel is " + leave_channel);
			}

			// Get the ArrayList of all channels
			ArrayList<Server_channel> channels = Server.get_all_irc_channels();
		
			// Find channel that client wants to leave
			for(int i = 0; i < channels.size(); i++) {
				Server_channel channel = channels.get(i);
				if(channel.get_channel_name().equals(leave_channel)) {
					//Self_message("Channel has been matched");
					boolean able_to_leave_channel = channel.remove_from_channel_list(this);
					if (able_to_leave_channel) {
						Self_message("You have left channel " + leave_channel);
					} else {
						Self_message("You are not a part of channel " + leave_channel);
					}
					return;
				}
			}

			// Not an existing channel
			Self_message("The channel you wished to leave does not exist");
		}
	}

	//QUIT
	// exits out of entire application
	public void QUIT() {
		// Leave every channel you are a part of
		for (int i = 0; i < this.current_channels_list.size(); i++) {
			String channel = current_channels_list.get(i).get_channel_name();
			//Self_message("Channel you are leaving" + channel);
			LEAVE(channel);
		} 

		// Remove yourself from Server's list of clients
		Server.leave_application(this);

		// Send exit message
		Self_message("Thank you for using IRC-For-ME.\n" +
			     "Goodbye.");
		
	}

	// Changes your screen name
	public void NICK() {
		set_name();
	}

	// presents an away message
        // Flips your status from its previous status
	// If you were active you will now be away, if you were away you will now be active 
	public void AWAY(String input) {
		this.is_away = (!is_away);

		if(is_away) {
			String new_away_message = input; 	
                        if (new_away_message.length() > 6) {
                        	new_away_message = new_away_message.substring(6).trim();
				this.away_message = "< AWAY MESSAGE > "+ new_away_message;
                         } else {
                                 return;
                         }
		}

	}

	// this enables you to change the 'topic' or name of the channel you are currently in
	// /TOPIC <topic> will change the topic of the current channel into <topic>
	// if <topic> is omitted then the current topic of the channel will be shown 
	public void TOPIC(String input) {
		String topic_request = input;
		String channel_name = this.current_channel.get_channel_name();
		if (topic_request.length() > 7) {
			topic_request = topic_request.substring(7).trim();
			this.current_channel.set_channel_topic(topic_request);
			Self_message("The topic of channel " + channel_name + " has now been set to " + topic_request);
		} else {
			String current_topic = this.current_channel.get_channel_topic();
			Self_message("The topic of channel " + channel_name + " is " + current_topic);
		}	
	}

	//NAMES 
	// if /NAMES <channel> lists names in the channel
	// if /NAMES lists all clients in application
	public void NAMES(String input) {
		synchronized (this) {
			String specified_channel = input;
			//list names in that channel
			if (specified_channel.length() > 7) {
				specified_channel = specified_channel.substring(7).trim();
				// find specified channels
				ArrayList<Server_channel> all_channels = Server.get_all_irc_channels();
				ArrayList<Server_handler> names;
				for(int j = 0; j < all_channels.size(); j++) {
					Server_channel channel = all_channels.get(j);
					if (channel.get_channel_name().equals(specified_channel)) {
						Self_message("All of the clients in channel " + specified_channel + " : ");
						//Self_message("That channel exists");
						names = channel.get_channel_list();
						for (int i = 0; i < names.size(); i++) {
							Server_handler client = names.get(i);
							Self_message(client.get_name());
						}
						return;
					} 
				//	Self_message("The channel you specified could not be found. Please try again.");
				}
				Self_message("The channel you specified could not be found. Please try again.");			
			} else {
				Self_message("All of the clients in IRC-For-ME :");
				ArrayList<Server_handler> names = Server.get_list();
				for (int i = 0; i < names.size(); i++) {
					Server_handler client = names.get(i);
					Self_message(client.get_name());
				}
			}
		}
	}

	//WHOIS
	// provides information about a particular user on IRC
	// information it provides is:
	// name, current channel, status
	public void WHOIS(String input) {
		synchronized (this) {
			// Get name of client requested by WHOIS
			String whois_client = input;
			if (whois_client.length() > 7) {
				whois_client = whois_client.substring(7).trim();
				whois_client = "< " + whois_client + " >";
			//	Self_message(whois_client);
			} else {
				Self_message("Please try again with the format /WHOIS <name>");
				return;
			}

			// Gather information about the client
			ArrayList<Server_handler> all_clients = Server.get_list();
			
			

			// Find client
			for(int i = 0; i < all_clients.size(); i++) {
				Server_handler client = all_clients.get(i);
				//Self_message(client.get_name());
				if(client.get_name().equals(whois_client)) {
					//get and send all client information
				//	Self_message("Client requested does exist");
					String name = client.get_name();

					Server_channel channel = client.get_current_channel();
					String interpreted_channel;
					if (channel != null) {
						interpreted_channel = channel.get_channel_name();
					} else {
						interpreted_channel = "no current channel";
					}

					Boolean status = client.get_is_away();
					String interpreted_status;
					if (status) {
						interpreted_status = "Away\n " + client.get_away_message();
					} else {
						interpreted_status = "Active";
					}
					
					Self_message("Name: " + name + "\n" +
						     "Current channel: " + interpreted_channel + "\n" +
						     "Status: " + interpreted_status + "\n");
					return;
				} 
			}
			
			// WHOIS client does not exist
			Self_message(whois_client + " cannot be found");
		}
	}

	/**
	* Message sending functions
	**/

	// Send message to all clients
	public void Broadcast(String message) {
		synchronized (this) {
			if (this.current_channel != null) { 
				ArrayList<Server_handler> broadcast_recipients = this.current_channel.get_channel_list();
	                         for(int i = 0; i < broadcast_recipients.size(); i++) {
					Server_handler client = broadcast_recipients.get(i);
                                	if ((client != this) && (client.current_channel == this.current_channel)) {
						if (client.is_away) {
							this.get_writer().println(client.get_away_message());
						} else {
				     	 		client.get_writer().println(this.name + message);
                                 		}
					}       
                        	 }	 			
			} else {
				Self_message("You are currently not in an irc-channel.\n" +
					     "To proceed, please join a channel.");
			}
	
		}
	}

	// Send message back to self
	public void Self_message(String message) {
		server_output.println(message);
	}
}
