import java.net.*;
import java.io.*;

public class server {
	private static ServerSocket server;
	private static Socket client;
	private static BufferedReader dataFromClient;
	private static String inputLine;

	public static void main(String[] args) {
		try {
			server = new ServerSocket(1990);
			client = server.accept();
		
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
