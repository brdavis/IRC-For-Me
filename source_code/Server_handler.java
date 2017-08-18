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
* Description: This is a server-side thread-based event handler that implements the functionality
*              of the IRC protocol for the IRC-For-ME application
*
* Functionality:
* 1) Create a thread per client
* 2) Implement IRC protocol commands
* 3) Send messages to others in application
*
* The IRC protocol commands implemented in this application are:
* 
* /HELP - shows a general list of commands to client 
* /JOIN - enables client to join a channel 
* /LIST - shows a list of all current channels
* /LEAVE - enables client to leave a channel 
* /QUIT - enables client to exit IRC-for-me application 
* /NICK - enables client to change its screen name 
* /AWAY - enables client to create an away message
* /WHOIS - displays information about a client in the channel
* /KICK - enables channel operator to kick out a client from the channel
* /TOPIC - enables channel operator to view or change the topic of the channel  
* /NAMES - shows a list of users in a channel, no channels specified shows all clients  
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
	private String name; //screen name
	private Socket socket; 
	private PrintWriter server_output;
	private BufferedReader client_input;

	private Boolean is_away; //indicates status
	private String away_message; //away message
	//bookkeeping variables
	private ArrayList<Server_channel> current_channels_list; //tracks all channels client is in
	private Server_channel current_channel;	//indicates channel client is currently in
	
	/**
	* Constructor method
	* Functionality 1: Create thread per client connection 
	**/
	public Server_handler(Socket socket) {
		this.socket = socket;// socket connection
		this.current_channels_list = new ArrayList<Server_channel>(); //initialize list to add channels
		Server.add_client(this); // add this client connection to the list of all client connections maintained by Server

		// initial status is active and default away message is set
		this.is_away = false;
		this.away_message = "< AWAY MESSAGE > " + 
				    "I am currently away from my computer." +
				    "Pleave leave a message.";		
	}

	/**
	* Setter methods
	**/
	// Set or change screen name
	public void set_name() {
		try {
			String name_prompt = "Enter your preferred screen name";
			server_output.println(name_prompt);
			String screen_name = client_input.readLine();
			this.name = "< " + screen_name + " >"; //format screen name
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
	// Allows you to send a message to this client 
	public PrintWriter get_writer() {
		return server_output;
	}

	// Get screen name
	public String get_name() {
		return name;
	}

	// Get the away message
	public String get_away_message() {
		return away_message;
	}

	// Get the current_channel
	public Server_channel get_current_channel() {
		return current_channel;
	}

	// Get status of client
	public Boolean get_is_away() {
		return is_away;
	}

	// Get the welcome message for the application that explains the basics of IRC-For-Me
	public String application_welcome_message() {
		String welcome_message = "\t\t Welcome to IRC-For-ME\n\n" +

					 "The application that allows you to chat with all your friends!\n" +
					 "To begin, let's review all of the available command in IRC-For-ME\n\n" +

                                         "\t\t ALL AVAILABLE IRC COMMANDS\n\n" +
                                         "/HELP - shows a general list of commands\n" +
                                         "/LIST - shows a list of all current channels\n" +
                                         "/JOIN - enables user to join a channel\n" +
                                         "/LEAVE - enables user to leave a channel\n" +
                                         "/QUIT - enables user to exit IRC-for-me application\n" +
                                         "/NICK - enables user to change screen name\n" +
                                         "/AWAY - enables user to create an away message\n" +
                                         "/WHOIS - displays information about a user in the channel\n" +
                                         "/KICK- enables channel operator to kick out a client from the channel\n" +
                                         "/TOPIC - enables channel operator to change the topic of the channel\n" +
                                         "/NAMES - shows a list of users in a channel\n\n" +

					 "If you ever need a review of this list just type '/HELP'\n" +
					 "\tThese commands are all case sensitive\n\n" +

					 "\t\t\t ENJOY!\n\n";

		return welcome_message;  
	}

	/**
	*
	* Standard run method to start thread functionality
	*
	* Functionality 2: Implement IRC protocol commands
	*
	**/
	public void run() {
		try {
			// Initialize the two-way communication channel for this client to the IRC-For-ME Server
			server_output = new PrintWriter(socket.getOutputStream(), true);
			client_input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	
			// Send welcome message to client to explain application
			Self_message(application_welcome_message());

			// Set screen name for client
			set_name();

			// Parse through client input to respond to IRC commands
			// IRC command directory shown below 
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
					Broadcast(input); //irc command was not given, continue to message in current channel
				}	
			} 
		} catch (IOException e) {
			System.out.println("ERROR: Cannot send or recieve messages");
			System.exit(ERROR);
		}
	}

	/**
	*
	*
	* IRC command functions
	*
	*
	**/

	//	HELP()
	// 
	// Functionality: Sends delineated list of the IRC commands back to the client
	public void HELP() {
		String help_menu = "HELP MENU\n" +
				   "ALL AVAILABLE IRC COMMANDS\n" +
				   "/HELP - shows a general list of commands\n" +
				   "/LIST - shows a list of all current channels\n" + 
				   "/JOIN - enables user to join a channel\n" + 
 			 	   "/LEAVE - enables user to leave a channel\n" +
				   "/QUIT - enables user to exit IRC-for-me application\n" +
				   "/NICK - enables user to change screen name\n" + 
				   "/AWAY - enables user to create an away message\n" + 
				   "/WHOIS - displays information about a user in the channel\n" +
				   "/KICK- enables channel operator to kick out a user from the channel\n" +
				   "/TOPIC - enables channel operator to change the topic of the channel" +
				   "/NAMES - shows a list of users in a channel, no channels specified shows all users"; 	
		Self_message(help_menu);
	} 
	
	// 	JOIN(String)
	//
	// Functionality: 
	// 1) Scans list of current channels for the channel request, if the channel already exists then the client automatically joins that channel  
	// 2) If no such channel exists a new channel is created and the requesting client is designated as the channel operator 
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
	
			// Scan list of current channels to see if it already exists
			ArrayList<Server_channel> all_channels = Server.get_all_irc_channels();

			for(int j = 0; j < all_channels.size(); j++) {
				Server_channel channel = all_channels.get(j);
				// If channel aready exists, add client to that channel
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

	//	LIST()
	//
	// Functionality: Sends a list of all of the acitve channels on the irc back to the client
	public void LIST() {
	   synchronized (this) {
		// Get the list of all the active channels from the Server
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

	//	LEAVE(String)
	//
	// Functionality: Enables client to leave a particular channel
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
			}

			// Find channel that client wants to leave
			ArrayList<Server_channel> channels = Server.get_all_irc_channels(); //get all the channels from Server
		
			// Find channel that matches leave request
			for(int i = 0; i < channels.size(); i++) {
				Server_channel channel = channels.get(i);
				if(channel.get_channel_name().equals(leave_channel)) {
					boolean able_to_leave_channel = channel.remove_from_channel_list(this);
					if (able_to_leave_channel) {
						Self_message("You have left channel " + leave_channel);
					} else {
						Self_message("You are not a part of channel " + leave_channel);
					}
					return;
				}
			}

			// If channel requested does not exist
			Self_message("The channel you wished to leave does not exist");
		}
	}

	//	QUIT()
	//
	// Functionality: Exits out of entire application
	public void QUIT() {
		// Leave every channel you are a part of
		for (int i = 0; i < this.current_channels_list.size(); i++) {
			String channel = current_channels_list.get(i).get_channel_name();
			LEAVE(channel);
		} 

		// Remove yourself from Server's list of clients
		Server.leave_application(this);

		// Send exit message
		Self_message("Thank you for using IRC-For-ME.\n" +
			     "Goodbye.");
	}

	// 	NICK()
	//
	// Functionality: Changes your screen name
	public void NICK() {
		set_name();
	}

	// 	AWAY(String)
	//
	// Functionality: 
        // 1) Flips your status from its previous status
	// If you were active you will now be away, if you were away you will now be active 
	// 2) If you are now AWAY, you can set your away message
	public void AWAY(String input) {
		//Flips your status
		this.is_away = (!is_away);
		
		// If you're now away, you can personalize your away message
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

	//	TOPIC(String)
	//
	// Functionality: This enables you to change the topic of your current channel 
	// 1) /TOPIC <topic> will change the topic of the current channel into <topic>
	// 2) If /TOPIC then the current topic of the channel will be shown 
	public void TOPIC(String input) {
		String topic_request = input;
		String channel_name = this.current_channel.get_channel_name();
		// Change current topic
		if (topic_request.length() > 7) {
			topic_request = topic_request.substring(7).trim();
			this.current_channel.set_channel_topic(topic_request);
			Self_message("The topic of channel " + channel_name + " has now been set to " + topic_request);
		// Show current topic
		} else {
			String current_topic = this.current_channel.get_channel_topic();
			Self_message("The topic of channel " + channel_name + " is " + current_topic);
		}	
	}

	//	NAMES(String)
	// 
	// FUnctionalkity:
	// 1)  if /NAMES <channel> lists names in the channel
	// 2) if /NAMES lists all clients in application
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
						names = channel.get_channel_list();
						for (int i = 0; i < names.size(); i++) {
							Server_handler client = names.get(i);
							Self_message(client.get_name());
						}
						return;
					} 
				}
				// <channel> argument given does not exist
				Self_message("The channel you specified could not be found. Please try again.");		
			// No <channel> argument give, list all the clients in the IRC-For-me application	
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

	//	WHOIS(String)
	//
	// Functionality: Provides information about a particular client
	// 	Information it provides is:
	//	Client's name, current channel, and status
	public void WHOIS(String input) {
		synchronized (this) {
			// Get name of client requested 
			String whois_client = input;
			if (whois_client.length() > 7) {
				whois_client = whois_client.substring(7).trim();
				whois_client = "< " + whois_client + " >";
			} else {
				Self_message("Please try again with the format /WHOIS <name>");
				return;
			}

			// Gather information about the client
			ArrayList<Server_handler> all_clients = Server.get_list();
			
			// Find client
			for(int i = 0; i < all_clients.size(); i++) {
				Server_handler client = all_clients.get(i);
				if(client.get_name().equals(whois_client)) {
					//get all client information

					//get name
					String name = client.get_name();

					//get channel
					Server_channel channel = client.get_current_channel();
					String interpreted_channel;
					if (channel != null) {
						interpreted_channel = channel.get_channel_name();
					} else {
						interpreted_channel = "no current channel";
					}
					
					//get status
					Boolean status = client.get_is_away();
					String interpreted_status;
					if (status) {
						interpreted_status = "Away\n " + client.get_away_message();
					} else {
						interpreted_status = "Active";
					}
					
					//send all information found about requested client
					Self_message("Name: " + name + "\n" +
						     "Current channel: " + interpreted_channel + "\n" +
						     "Status: " + interpreted_status + "\n");
					return;
				} 
			}
			
			// requested client does not exist
			Self_message(whois_client + " cannot be found");
		}
	}

	/**
	*
	* Message sending functions
	*
	* Functionality 3: Send messages to others in application
	*
	**/

	//	Broadcast(String)
	//
	// Functionality: Takes input and sends it to every active client in current_channel
	public void Broadcast(String message) {
		synchronized (this) {
			if (this.current_channel != null) { // if current_channel is set
				//get the list from the channel of all client that belong to channel
				ArrayList<Server_handler> broadcast_recipients = this.current_channel.get_channel_list();
				// iterate throught list to get each individual client
				// check to make sure they are not myself and that they are currently in this channel
				// if they are away - get their away message, otherwise send them my message
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
			// if my current_channel is not set - I'm not in an irc-channel and so I will be informed of that
			} else {
				Self_message("You are currently not in an irc-channel.\n" +
					     "To proceed, please join a channel.");
			}
		}
	}

	//	Self_message(String)
	// 
	// Functionality: Send message back to client
	public void Self_message(String message) {
		server_output.println(message);
	}
}
