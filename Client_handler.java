// This class is a client-side event handler to handle all keyboad events from the client
// There is one thread per client
// This event handler will transmit all user data to the server
import java.io.*;

public class Client_handler extends Thread {
	public static final int ERROR = -1;
	private PrintWriter client_message;

	public Client_handler(PrintWriter message) {
		this.client_message = message;
	}

	public void run() {
	//sends messages form console to the server throught the socket
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
