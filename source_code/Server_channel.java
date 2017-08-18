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
* Server_channel Class explanation
*
* Description: The Server_channel maintains all the information pertaining to each active IRC channel in the IRC-For-ME application
* 
* Functionality:
* 1) Maintains name of channel
* 2) Maintains operator of channel
* 3) Keeps a current list of all clients in the channel
* 4) Maintains channel topic
*
**/


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
		this.channel_topic = "not set";	//by default the channel is not set

		// Create channel and add creating client as the channel operator
		this.channel_list = new ArrayList<Server_handler>();
		channel_list.add(channel_operator);
	
		// Since this is a new channel that is just being created, add it to the all_irc_channels list
		// where the Sever is maintaining the list of all active irc channel in the application
		Server.all_irc_channels.add(this);
	}
	
	/**
	* Setter methods
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

	// Set the topic of the channel
	public void set_channel_topic(String new_topic) {
		this.channel_topic = new_topic;
	}

	// Remove a client from the channel_list
	public boolean remove_from_channel_list(Server_handler leaving_client) {
		int client = channel_list.indexOf(leaving_client);
		if (client != -1) { //if the client belongs to this channel
			channel_list.remove(client); //remove the client from the channel
			return true;
		} else { //the client was never in this channel
			return false; 
		}
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

	// Get the list of clients that are in the channel
	public ArrayList<Server_handler> get_channel_list() {
		return this.channel_list;
	}

	// Get the topic of the channel
	public String get_channel_topic() {
		return this.channel_topic;
	}
}	
