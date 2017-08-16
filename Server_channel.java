import java.util.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server_channel{
	//name of channel
	private String channel_name;
	//client that made the channel now owns it
	private Server_handler channel_operator;
	// array of all clients in channel
	private ArrayList<Server_handler> channel_list;
	// topic of channel
	private String channel_topic;

	/**
	* Constructor method
	**/
	Server_channel(String name, Server_handler client) {

		this.channel_name = name;

		this.channel_operator = client;

		this.channel_list = new ArrayList<Server_handler>();
		channel_list.add(channel_operator);

		this.channel_topic = "not set";

		Server.all_irc_channels.add(this);
		System.out.println("Channel " + this.channel_name + " was created by " + this.channel_operator.get_name());
	}
	
	
	/**
	* Setter method
	**/
	
	// Add a client to the channel
	public void join_list(Server_handler requesting_client) {
		for(int i = 0; i < channel_list.size(); i++) {
			Server_handler existing_client = channel_list.get(i);
			if (existing_client == requesting_client) {
				return;
			}
		}	
		this.channel_list.add(requesting_client);		
	}

	// set the topic of the channel
	public void set_channel_topic(String new_topic) {
		this.channel_topic = new_topic;
	}


	/**
	* Getter methods
	**/
	
	// Get the name of the channel
	public String get_channel_name() {
		return this.channel_name;
	}
	
	// Get the operator of the channel
	public Server_handler get_channel_operator() {
		return this.channel_operator;
	}

	// Get the list of clients active in the channel
	public ArrayList<Server_handler> get_channel_list() {
		return this.channel_list;
	}

	// Get the topic of the channel
	public String get_channel_topic() {
		return this.channel_topic;
	}


	// Remove a client from the channel_list
	public boolean remove_from_channel_list(Server_handler leaving_client) {
		int client = channel_list.indexOf(leaving_client);
		if (client != -1) {
			channel_list.remove(client);
			return true;
		} else {
			return false;
		}
	}


}	
