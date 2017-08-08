/**
* This class is a client-side event handler to handle all keyboad events from the client
* This event handler will transmit all user data to the server
**/

import java.io.*;

public class Client_handler extends Thread {
	
	/**
	* Class variables
	**/	
	public static final int ERROR = -1;
	private PrintWriter client_message;

	/**
	* Constructor method
	**/
	public Client_handler(PrintWriter message) {
		this.client_message = message;
	}

	/**
	* Standard run method to start thread functionality
	**/
	public void run() {
		try {
			BufferedReader client_input = new BufferedReader(new InputStreamReader(System.in));
			while(!isInterrupted()) {
				String message = client_input.readLine();
				client_message.println(message);
				client_message.flush();
			}
		} catch (IOException ioe){
			System.out.println("Error: Unable to transmit user data");
			System.exit(ERROR);
		}
	} 
}
