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
* Client.java Class explanation
*
* Functionality:
* 1) The Client connects to the IRC-For-Me Server
* 2) The Client starts the Client_handler thread to continually poll for user input (user input to send to the Server) 
* 3) The Client listens for data coming from the Server (messages recieved)
*
**/

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

		/**
		* Functionality 1: Connecting client to IRC-For-Me server
		**/
		try {
			//connect client to IRC-For-Me server
			Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);

			//set up two-way communication channel between client and server
			server_output = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			client_output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

		} catch (IOException er) {
			System.err.println("Error: Cannot connect to server");
			System.exit(ERROR);
		}		

		/**
		* Functionality 2: Create and start the Client_handler for sending messages
		**/
		Client_handler client_messenger = new Client_handler(client_output);
		client_messenger.setDaemon(true);
		client_messenger.start();
		
		/**
		* Functionality 3: List to Server for recieving messages
		**/
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
