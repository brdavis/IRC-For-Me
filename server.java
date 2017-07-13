import java.net.*;
import java.io.*;

public class server {
	private static ServerSocket server;
	private static Socket client;
	private static BufferedReader dataFromClient;
	private static String inputLine;

	public static void main(String[] args) {
		try {
			//start server
			server = new ServerSocket(1990);
			System.out.println("Server is running");
			
			//accept client
			client = server.accept();
			System.out.println("Client has connected to server");		
			//obtain and print data from client
			dataFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));

			while ((inputLine = dataFromClient.readLine()) != null){
				System.out.println(inputLine);
			}
		} catch(IOException e) {
			System.out.println(e);
		}
	}
}
