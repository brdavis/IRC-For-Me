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
* Client_handler Class Explanation
*
* Description:
* This class is a client-side event-handler to handle all keyboad events from the client
* 
* Functionality:
* 1) This event-handler will transmit all client input data to the server
*
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
		/**
		* Functionality 1: transmit data to server
		* */
		try {
			// listen to server, and while connection is not broken, send input to server
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
