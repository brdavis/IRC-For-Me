import java.util.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server_channel {
	//name of channel
	private static String channel_name;
	//client that made the channel now owns it
	private static Server_handler channel_operator;
	// array of all clients in channel
	private static ArrayList<Server_handler> channel_list;

	/**
	* Constructor method
	**/
	Server_channel(String name, Server_handler client) {
		this.channel_name = name;
		this.channel_operator = client;
		this.channel_list = new ArrayList<Server_handler>();
		channel_list.add(channel_operator);
	}
	
	
	/**
	* Setter method
	**/
	
	// Add a client to the channel
//	public void add_client(Server_handler client) {
//		channel_list		
//	}

	// Remove a client from the channel

	// Change the name of the channel
	public void change_name(String new_name) {
		this.channel_name = new_name;
	}

	/**
	* Getter methods
	**/
	
	// Get the name of the channel
	public String get_channel_name() {
		return this.channel_name;
	}
	
	// Get the list of clients active in the channel
	public ArrayList<Server_handler> get_channel_list() {
		return this.channel_list;
	}
	
 
}
